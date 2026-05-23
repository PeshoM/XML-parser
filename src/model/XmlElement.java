package model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/** XML елемент: id, атрибути, наследници, текст, родител. */
public class XmlElement {
    private String id;
    private final LinkedHashMap<String, String> attributes = new LinkedHashMap<>();
    private final List<XmlElement> children = new ArrayList<>();
    private String text = "";
    private XmlElement parent;
    private String localName;
    private String nsPrefix;

    /** Създава елемент с дадено локално име. */
    public XmlElement(String localName) {
        this.localName = localName;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LinkedHashMap<String, String> getAttributes() { return attributes; }

    public List<XmlElement> getChildren() { return children; }

    /** Добавя наследник и задава родителя му. */
    public void addChild(XmlElement child) {
        child.parent = this;
        children.add(child);
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public XmlElement getParent() { return parent; }

    public String getLocalName() { return localName; }
    public void setLocalName(String localName) { this.localName = localName; }

    public String getNsPrefix() { return nsPrefix; }
    public void setNsPrefix(String nsPrefix) { this.nsPrefix = nsPrefix; }

    /** Връща prefix:local или само local. */
    public String getQualifiedName() {
        return (nsPrefix == null) ? localName : nsPrefix + ":" + localName;
    }

    public boolean hasChildren() { return !children.isEmpty(); }
}
