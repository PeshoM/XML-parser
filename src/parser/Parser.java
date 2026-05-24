package parser;

import model.XmlElement;

import java.util.List;

/** Парсър на XML (рекурсивно слизане). */
public class Parser {
    private final List<Token> tokens;
    private int pos;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    /** Парсва документа и връща кореновия елемент. */
    public XmlElement parseDocument() {
        XmlElement root = parseElement();
        expect(TokenType.EOF);
        return root;
    }

    private XmlElement parseElement() {
        expect(TokenType.TAG_OPEN);
        String prefix = null;
        Token nameTok = expect(TokenType.IDENTIFIER);
        String name = nameTok.value;
        if (peek().type == TokenType.COLON) {
            advance();
            prefix = name;
            name = expect(TokenType.IDENTIFIER).value;
        }
        XmlElement el = new XmlElement(name);
        el.setNsPrefix(prefix);

        // attributes
        while (peek().type == TokenType.IDENTIFIER) {
            String attrPrefix = null;
            String attrName = advance().value;
            if (peek().type == TokenType.COLON) {
                advance();
                attrPrefix = attrName;
                attrName = expect(TokenType.IDENTIFIER).value;
            }
            String fullAttr = qualified(attrPrefix, attrName);
            expect(TokenType.EQUALS);
            String value = expect(TokenType.STRING).value;
            el.getAttributes().put(fullAttr, value);
        }

        if (peek().type == TokenType.SELF_CLOSE) {
            advance();
            return el;
        }

        expect(TokenType.TAG_CLOSE);

        // children or text
        StringBuilder textBuf = new StringBuilder();
        while (peek().type != TokenType.TAG_END_OPEN) {
            if (peek().type == TokenType.TEXT) {
                if (textBuf.length() > 0) textBuf.append(' ');
                textBuf.append(advance().value);
            } else if (peek().type == TokenType.TAG_OPEN) {
                el.addChild(parseElement());
            } else {
                Token t = peek();
                throw new ParseException("Unexpected token " + t.type + " '" + t.value + "'", t.line, t.column);
            }
        }
        if (el.getChildren().isEmpty()) {
            el.setText(textBuf.toString());
        } else {
            el.setText("");
        }

        // closing tag
        expect(TokenType.TAG_END_OPEN);
        String closePrefix = null;
        String closeName = expect(TokenType.IDENTIFIER).value;
        if (peek().type == TokenType.COLON) {
            advance();
            closePrefix = closeName;
            closeName = expect(TokenType.IDENTIFIER).value;
        }
        if (!closeName.equals(el.getLocalName()) ||
            !java.util.Objects.equals(closePrefix, el.getNsPrefix())) {
            Token t = tokens.get(pos - 1);
            String expected = el.getQualifiedName();
            String got = qualified(closePrefix, closeName);
            throw new ParseException("Closing tag '" + got + "' does not match opening '" + expected + "'",
                t.line, t.column);
        }
        expect(TokenType.TAG_CLOSE);
        return el;
    }

    private static String qualified(String prefix, String local) {
        return (prefix == null) ? local : prefix + ":" + local;
    }

    private Token peek() { return tokens.get(pos); }
    private Token advance() { return tokens.get(pos++); }

    private Token expect(TokenType t) {
        Token tok = peek();
        if (tok.type != t) {
            throw new ParseException("Expected " + t + " but got " + tok.type + " '" + tok.value + "'",
                tok.line, tok.column);
        }
        return advance();
    }
}
