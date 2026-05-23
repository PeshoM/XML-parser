package model;

import java.util.LinkedHashMap;
import java.util.Map;

/** XML документ: корен и индекс по id. */
public class XmlDocument {
    private final XmlElement root;
    private final Map<String, XmlElement> idIndex = new LinkedHashMap<>();
    private int genCounter;

    public XmlDocument(XmlElement root) {
        this.root = root;
    }

    public XmlElement getRoot() { return root; }

    public Map<String, XmlElement> getIdIndex() { return idIndex; }

    /** Намира елемент по id, или null. */
    public XmlElement findById(String id) { return idIndex.get(id); }

    /** Регистрира id → елемент. */
    public void registerId(String id, XmlElement el) {
        idIndex.put(id, el);
    }

    /** Генерира уникален id от вида _gen_N. */
    public String nextGeneratedId() {
        while (true) {
            String candidate = "_gen_" + (++genCounter);
            if (!idIndex.containsKey(candidate)) return candidate;
        }
    }
}
