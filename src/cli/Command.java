package cli;

import java.io.PrintStream;

public interface Command {
    String name();
    String helpLine();
    void execute(String[] args, PrintStream out);
}
