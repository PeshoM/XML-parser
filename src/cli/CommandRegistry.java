package cli;

import java.util.LinkedHashMap;
import java.util.Map;

public class CommandRegistry {
    private final Map<String, Command> byName = new LinkedHashMap<>();

    public void register(Command c) {
        byName.put(c.name(), c);
    }

    public Command lookup(String[] tokens) {
        if (tokens.length >= 2) {
            Command twoWord = byName.get(tokens[0] + " " + tokens[1]);
            if (twoWord != null) return twoWord;
        }
        if (tokens.length >= 1) {
            return byName.get(tokens[0]);
        }
        return null;
    }

    public Iterable<Command> all() {
        return byName.values();
    }

    public int leadingTokensConsumed(String[] tokens) {
        if (tokens.length >= 2 && byName.containsKey(tokens[0] + " " + tokens[1])) return 2;
        return 1;
    }
}
