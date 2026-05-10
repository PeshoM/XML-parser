package xpath;

import java.util.ArrayList;
import java.util.List;

public class XPathParser {
    private final List<XPathToken> tokens;
    private int pos;

    public XPathParser(List<XPathToken> tokens) { this.tokens = tokens; }

    public List<XPathStep> parse() {
        List<XPathStep> steps = new ArrayList<>();
        if (peek().type == XPathTokenType.SLASH) advance();
        steps.add(parseStep());
        while (peek().type == XPathTokenType.SLASH) {
            advance();
            steps.add(parseStep());
        }
        if (peek().type != XPathTokenType.EOF) {
            throw new XPathException("Unexpected token after XPath: '" + peek().value + "'");
        }
        return steps;
    }

    private XPathStep parseStep() {
        XPathStep step = new XPathStep();

        if (peek().type == XPathTokenType.IDENTIFIER) {
            XPathToken first = peek();
            if (pos + 1 < tokens.size() && tokens.get(pos + 1).type == XPathTokenType.AXIS_SEP) {
                advance(); advance();
                step.axis = Axis.fromName(first.value);
            }
        }

        if (peek().type == XPathTokenType.AT) {
            advance();
            XPathToken nameTok = expect(XPathTokenType.IDENTIFIER);
            step.name = "*";
            step.predicates.add(new XPathPredicate.AttrSelector(nameTok.value));
            return step;
        }

        if (peek().type == XPathTokenType.STAR) {
            advance();
            step.name = "*";
        } else {
            XPathToken nameTok = expect(XPathTokenType.IDENTIFIER);
            step.name = nameTok.value;
        }

        if (peek().type == XPathTokenType.OPEN_PAREN) {
            advance();
            expect(XPathTokenType.AT);
            XPathToken nameTok = expect(XPathTokenType.IDENTIFIER);
            expect(XPathTokenType.CLOSE_PAREN);
            step.predicates.add(new XPathPredicate.AttrSelector(nameTok.value));
            return step;
        }

        while (peek().type == XPathTokenType.OPEN_BRACKET) {
            advance();
            step.predicates.add(parsePredicate());
            expect(XPathTokenType.CLOSE_BRACKET);
        }
        return step;
    }

    private XPathPredicate parsePredicate() {
        if (peek().type == XPathTokenType.NUMBER) {
            int n = Integer.parseInt(advance().value);
            return new XPathPredicate.Index(n);
        }
        if (peek().type == XPathTokenType.AT) {
            advance();
            XPathToken nameTok = expect(XPathTokenType.IDENTIFIER);
            if (peek().type == XPathTokenType.EQUALS) {
                advance();
                XPathToken val = (peek().type == XPathTokenType.STRING || peek().type == XPathTokenType.IDENTIFIER)
                    ? advance() : expect(XPathTokenType.STRING);
                return new XPathPredicate.AttrEquals(nameTok.value, val.value);
            }
            return new XPathPredicate.AttrSelector(nameTok.value);
        }
        if (peek().type == XPathTokenType.IDENTIFIER) {
            XPathToken nameTok = advance();
            expect(XPathTokenType.EQUALS);
            XPathToken val = (peek().type == XPathTokenType.STRING || peek().type == XPathTokenType.IDENTIFIER)
                ? advance() : expect(XPathTokenType.STRING);
            return new XPathPredicate.AttrEquals(nameTok.value, val.value);
        }
        throw new XPathException("Unexpected token in predicate: '" + peek().value + "'");
    }

    private XPathToken peek() { return tokens.get(pos); }
    private XPathToken advance() { return tokens.get(pos++); }
    private XPathToken expect(XPathTokenType t) {
        if (peek().type != t) {
            throw new XPathException("Expected " + t + " but got " + peek().type + " '" + peek().value + "'");
        }
        return advance();
    }
}
