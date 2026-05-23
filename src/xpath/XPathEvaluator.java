package xpath;

import model.XmlElement;

import java.util.ArrayList;
import java.util.List;

/** Изпълнява XPath заявка спрямо начален елемент. */
public class XPathEvaluator {
    /** Изпълнява заявката от стартовия елемент. */
    public List<Object> evaluate(XmlElement start, List<XPathStep> steps) {
        List<XmlElement> current = new ArrayList<>();
        current.add(start);
        List<Object> finalResult = null;

        for (int i = 0; i < steps.size(); i++) {
            XPathStep step = steps.get(i);
            boolean isLast = (i == steps.size() - 1);

            List<XmlElement> nextSet = new ArrayList<>();
            for (XmlElement el : current) {
                nextSet.addAll(expandAxis(el, step));
            }

            XPathPredicate.AttrSelector finalAttr = null;
            List<XPathPredicate> filters = new ArrayList<>();
            for (XPathPredicate p : step.predicates) {
                if (p instanceof XPathPredicate.AttrSelector sel) {
                    finalAttr = sel;
                } else {
                    filters.add(p);
                }
            }

            for (XPathPredicate p : filters) {
                nextSet = applyPredicate(nextSet, p);
            }

            current = nextSet;

            if (isLast && finalAttr != null) {
                List<Object> out = new ArrayList<>();
                for (XmlElement el : current) {
                    String v = el.getAttributes().get(finalAttr.name);
                    if (v != null) out.add(v);
                }
                finalResult = out;
            }
        }

        if (finalResult != null) return finalResult;
        List<Object> out = new ArrayList<>(current);
        return out;
    }

    private List<XmlElement> expandAxis(XmlElement el, XPathStep step) {
        List<XmlElement> set = new ArrayList<>();
        switch (step.axis) {
            case CHILD -> set.addAll(el.getChildren());
            case SELF -> set.add(el);
            case PARENT -> { if (el.getParent() != null) set.add(el.getParent()); }
            case ANCESTOR -> {
                XmlElement a = el.getParent();
                while (a != null) { set.add(a); a = a.getParent(); }
            }
            case DESCENDANT -> collectDescendants(el, set);
        }
        List<XmlElement> filtered = new ArrayList<>();
        for (XmlElement candidate : set) {
            if (step.name.equals("*") || candidate.getLocalName().equals(step.name)
                || candidate.getQualifiedName().equals(step.name)) {
                filtered.add(candidate);
            }
        }
        return filtered;
    }

    private void collectDescendants(XmlElement el, List<XmlElement> out) {
        for (XmlElement c : el.getChildren()) {
            out.add(c);
            collectDescendants(c, out);
        }
    }

    private List<XmlElement> applyPredicate(List<XmlElement> nodes, XPathPredicate p) {
        List<XmlElement> out = new ArrayList<>();
        if (p instanceof XPathPredicate.Index ix) {
            if (ix.index >= 0 && ix.index < nodes.size()) out.add(nodes.get(ix.index));
        } else if (p instanceof XPathPredicate.AttrEquals eq) {
            for (XmlElement el : nodes) {
                if (eq.value.equals(el.getAttributes().get(eq.name))) out.add(el);
                else if (childTextMatches(el, eq.name, eq.value)) {
                    out.add(el);
                }
            }
        }
        return out;
    }

    private boolean childTextMatches(XmlElement el, String childName, String value) {
        for (XmlElement c : el.getChildren()) {
            if (c.getLocalName().equals(childName) && value.equals(c.getText())) return true;
        }
        return false;
    }
}
