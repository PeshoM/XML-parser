package cli;

import java.util.ArrayList;
import java.util.List;

/** Разделя ред на аргументи, зачитайки кавички. */
public class ArgumentTokenizer {
    /** Разделя ред на аргументи. */
    public String[] tokenize(String input) {
        List<String> out = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (Character.isWhitespace(c) && !inQuotes) {
                if (current.length() > 0) {
                    out.add(current.toString());
                    current.setLength(0);
                }
            } else {
                current.append(c);
            }
        }
        if (current.length() > 0) out.add(current.toString());
        return out.toArray(new String[0]);
    }
}
