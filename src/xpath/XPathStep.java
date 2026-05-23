package xpath;

import java.util.ArrayList;
import java.util.List;

/** Една стъпка от XPath израз. */
public class XPathStep {
    public Axis axis = Axis.CHILD;
    public String name;
    public List<XPathPredicate> predicates = new ArrayList<>();
}
