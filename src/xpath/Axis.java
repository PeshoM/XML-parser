package xpath;

public enum Axis {
    CHILD, PARENT, ANCESTOR, DESCENDANT, SELF;

    public static Axis fromName(String s) {
        return switch (s) {
            case "child" -> CHILD;
            case "parent" -> PARENT;
            case "ancestor" -> ANCESTOR;
            case "descendant" -> DESCENDANT;
            case "self" -> SELF;
            default -> throw new XPathException("Unknown axis: " + s);
        };
    }
}
