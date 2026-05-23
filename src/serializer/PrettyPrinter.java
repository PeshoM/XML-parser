package serializer;

import model.XmlDocument;
import model.XmlElement;

/** Сериализира XML документ с форматирани отстъпи. */
public class PrettyPrinter {
    private static final String INDENT = "  ";

    /** Връща форматиран XML низ за документа. */
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
        if (s == null || s.isEmpty()) return "";
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '&' -> sb.append("&amp;");
                case '<' -> sb.append("&lt;");
                case '>' -> sb.append("&gt;");
                default -> sb.append(c);
            }
        }
        return sb.toString();
    }

    private String escapeAttr(String s) {
        if (s == null) return "";
        return escapeText(s).replace("\"", "&quot;");
    }
}
