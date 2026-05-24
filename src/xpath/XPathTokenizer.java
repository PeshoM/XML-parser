package xpath;

import java.util.ArrayList;
import java.util.List;

/** Токенизатор за XPath изразите. */
public class XPathTokenizer {
    private final String input;
    private int pos;

    public XPathTokenizer(String input) { this.input = input; }

    /** Връща всички токени от XPath израза. */
    public List<XPathToken> tokenize() {
        List<XPathToken> out = new ArrayList<>();
        while (pos < input.length()) {
            char c = input.charAt(pos);
            if (Character.isWhitespace(c)) { pos++; continue; }
            if (c == '/') { out.add(new XPathToken(XPathTokenType.SLASH, "/")); pos++; }
            else if (c == '[') { out.add(new XPathToken(XPathTokenType.OPEN_BRACKET, "[")); pos++; }
            else if (c == ']') { out.add(new XPathToken(XPathTokenType.CLOSE_BRACKET, "]")); pos++; }
            else if (c == '(') { out.add(new XPathToken(XPathTokenType.OPEN_PAREN, "(")); pos++; }
            else if (c == ')') { out.add(new XPathToken(XPathTokenType.CLOSE_PAREN, ")")); pos++; }
            else if (c == '@') { out.add(new XPathToken(XPathTokenType.AT, "@")); pos++; }
            else if (c == '=') { out.add(new XPathToken(XPathTokenType.EQUALS, "=")); pos++; }
            else if (c == '*') { out.add(new XPathToken(XPathTokenType.STAR, "*")); pos++; }
            else if (c == ':' && pos + 1 < input.length() && input.charAt(pos + 1) == ':') {
                out.add(new XPathToken(XPathTokenType.AXIS_SEP, "::")); pos += 2;
            } else if (c == '"' || c == '\'') {
                out.add(readString(c));
            } else if (Character.isDigit(c)) {
                out.add(readNumber());
            } else if (Character.isLetter(c) || c == '_') {
                out.add(readIdentifier());
            } else {
                throw new XPathException("Unexpected character '" + c + "' in XPath");
            }
        }
        out.add(new XPathToken(XPathTokenType.EOF, ""));
        return out;
    }

    private XPathToken readString(char quote) {
        pos++;
        int start = pos;
        while (pos < input.length() && input.charAt(pos) != quote) pos++;
        if (pos >= input.length()) throw new XPathException("Unterminated string in XPath");
        String v = input.substring(start, pos);
        pos++;
        return new XPathToken(XPathTokenType.STRING, v);
    }

    private XPathToken readNumber() {
        int startPos = pos;
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) pos++;
        return new XPathToken(XPathTokenType.NUMBER, input.substring(startPos, pos));
    }

    private XPathToken readIdentifier() {
        int start = pos;
        while (pos < input.length()) {
            char c = input.charAt(pos);
            if (Character.isLetterOrDigit(c) || c == '_' || c == '-' || c == '.') pos++;
            else break;
        }
        return new XPathToken(XPathTokenType.IDENTIFIER, input.substring(start, pos));
    }
}
