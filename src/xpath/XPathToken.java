package xpath;

/** XPath токен. */
public class XPathToken {
    public final XPathTokenType type;
    public final String value;

    public XPathToken(XPathTokenType type, String value) {
        this.type = type;
        this.value = value;
    }
}
