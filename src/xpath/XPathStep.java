package xpath;

import java.util.ArrayList;
import java.util.List;

public class XPathStep {
    public Axis axis = Axis.CHILD;
    public String name;
    public List<XPathPredicate> predicates = new ArrayList<>();
}
