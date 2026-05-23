package xpath;

/** Предикат в XPath стъпка. */
public abstract class XPathPredicate {
    public static class Index extends XPathPredicate {
        public final int index;
        public Index(int index) { this.index = index; }
    }
    public static class AttrEquals extends XPathPredicate {
        public final String name; public final String value;
        public AttrEquals(String name, String value) { this.name = name; this.value = value; }
    }
    public static class AttrSelector extends XPathPredicate {
        public final String name;
        public AttrSelector(String name) { this.name = name; }
    }
}
