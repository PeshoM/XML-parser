package parser;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private final String input;
    private int pos;
    private int line = 1;
    private int column = 1;
    private boolean inTag;

    public Tokenizer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (pos < input.length()) {
            if (!inTag) {
                readOutsideTag(tokens);
            } else {
                readInsideTag(tokens);
            }
        }
        tokens.add(new Token(TokenType.EOF, "", line, column));
        return tokens;
    }

    private void readOutsideTag(List<Token> tokens) {
        int startLine = line, startCol = column;
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && peek() != '<') {
            sb.append(advance());
        }
        String text = decodeEntities(sb.toString());
        if (!text.trim().isEmpty()) {
            tokens.add(new Token(TokenType.TEXT, text.trim(), startLine, startCol));
        }
        if (pos >= input.length()) return;

        // peek() == '<'
        if (matches("<?")) { skipUntil("?>"); return; }
        if (matches("<!--")) { skipUntil("-->"); return; }

        if (matches("</")) {
            tokens.add(new Token(TokenType.TAG_END_OPEN, "</", line, column));
            advance(); advance();
        } else {
            tokens.add(new Token(TokenType.TAG_OPEN, "<", line, column));
            advance();
        }
        inTag = true;
    }

    private void readInsideTag(List<Token> tokens) {
        skipWhitespace();
        if (pos >= input.length()) return;
        char c = peek();
        if (c == '>') {
            tokens.add(new Token(TokenType.TAG_CLOSE, ">", line, column));
            advance();
            inTag = false;
        } else if (c == '/' && peekAt(1) == '>') {
            tokens.add(new Token(TokenType.SELF_CLOSE, "/>", line, column));
            advance(); advance();
            inTag = false;
        } else if (c == '=') {
            tokens.add(new Token(TokenType.EQUALS, "=", line, column));
            advance();
        } else if (c == ':') {
            tokens.add(new Token(TokenType.COLON, ":", line, column));
            advance();
        } else if (c == '"' || c == '\'') {
            tokens.add(readString());
        } else if (isIdentStart(c)) {
            tokens.add(readIdentifier());
        } else {
            throw new ParseException("Unexpected character '" + c + "'", line, column);
        }
    }

    private Token readIdentifier() {
        int startLine = line, startCol = column;
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && isIdentPart(peek())) {
            sb.append(advance());
        }
        return new Token(TokenType.IDENTIFIER, sb.toString(), startLine, startCol);
    }

    private Token readString() {
        int startLine = line, startCol = column;
        char quote = advance(); // " or '
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && peek() != quote) {
            sb.append(advance());
        }
        if (pos >= input.length()) {
            throw new ParseException("Unterminated string", startLine, startCol);
        }
        advance(); // closing quote
        return new Token(TokenType.STRING, decodeEntities(sb.toString()), startLine, startCol);
    }

    private boolean isIdentStart(char c) {
        return Character.isLetter(c) || c == '_';
    }

    private boolean isIdentPart(char c) {
        return Character.isLetterOrDigit(c) || c == '_' || c == '-' || c == '.';
    }

    private char peek() { return input.charAt(pos); }

    private char peekAt(int offset) {
        return (pos + offset < input.length()) ? input.charAt(pos + offset) : '\0';
    }

    private char advance() {
        char c = input.charAt(pos++);
        if (c == '\n') { line++; column = 1; } else { column++; }
        return c;
    }

    private boolean matches(String s) {
        if (pos + s.length() > input.length()) return false;
        return input.regionMatches(pos, s, 0, s.length());
    }

    private void skipUntil(String marker) {
        while (pos < input.length() && !matches(marker)) advance();
        for (int i = 0; i < marker.length() && pos < input.length(); i++) advance();
    }

    private void skipWhitespace() {
        while (pos < input.length() && Character.isWhitespace(peek())) advance();
    }

    private String decodeEntities(String s) {
        StringBuilder out = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            if (s.charAt(i) == '&') {
                int semi = s.indexOf(';', i);
                if (semi > 0) {
                    String entity = s.substring(i + 1, semi);
                    String decoded = switch (entity) {
                        case "lt" -> "<";
                        case "gt" -> ">";
                        case "amp" -> "&";
                        case "quot" -> "\"";
                        case "apos" -> "'";
                        default -> {
                            if (entity.startsWith("#x")) yield String.valueOf((char) Integer.parseInt(entity.substring(2), 16));
                            else if (entity.startsWith("#")) yield String.valueOf((char) Integer.parseInt(entity.substring(1)));
                            else yield "&" + entity + ";";
                        }
                    };
                    out.append(decoded);
                    i = semi + 1;
                    continue;
                }
            }
            out.append(s.charAt(i++));
        }
        return out.toString();
    }
}
