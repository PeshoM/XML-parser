package serializer;

import model.XmlDocument;
import model.XmlElement;

public class PrettyPrinter {
    private static final String INDENT = "  ";

    public String print(XmlDocument doc) {
        StringBuilder sb = new StringBuilder();
        renderElement(doc.getRoot(), 0, sb);
        return sb.toString();
    }

    private void renderElement(XmlElement el, int depth, StringBuilder sb) {
        String pad = INDENT.repeat(depth);
        sb.append(pad).append('<').append(el.getQualifiedName());
        for (var e : el.getAttributes().entrySet()) {
            sb.append(' ').append(e.getKey()).append("=\"")
              .append(escapeAttr(e.getValue())).append('"');
        }
        if (!el.hasChildren() && (el.getText() == null || el.getText().isEmpty())) {
            sb.append("/>\n");
            return;
        }
        if (!el.hasChildren()) {
            sb.append('>').append(escapeText(el.getText()))
              .append("</").append(el.getQualifiedName()).append(">\n");
            return;
        }
        sb.append(">\n");
        for (XmlElement c : el.getChildren()) {
            renderElement(c, depth + 1, sb);
        }
        sb.append(pad).append("</").append(el.getQualifiedName()).append(">\n");
    }

    private String escapeText(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private String escapeAttr(String s) {
        return escapeText(s).replace("\"", "&quot;");
    }
}
