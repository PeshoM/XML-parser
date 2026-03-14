package cli;

import java.io.PrintStream;

public interface Command {
    String name();
    String helpLine();
    void execute(String[] args, Session session, PrintStream out);
}
