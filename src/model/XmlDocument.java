package model;

import java.util.LinkedHashMap;
import java.util.Map;

public class XmlDocument {
    private final XmlElement root;
    private final Map<String, XmlElement> idIndex = new LinkedHashMap<>();
    private int genCounter;

    public XmlDocument(XmlElement root) {
        this.root = root;
    }

    public XmlElement getRoot() { return root; }

    public Map<String, XmlElement> getIdIndex() { return idIndex; }

    public XmlElement findById(String id) { return idIndex.get(id); }

    public void registerId(String id, XmlElement el) {
        idIndex.put(id, el);
    }

    public String nextGeneratedId() {
        while (true) {
            String candidate = "_gen_" + (++genCounter);
            if (!idIndex.containsKey(candidate)) return candidate;
        }
    }
}
