package cli;

import java.io.PrintStream;

/** Интерфейс за всички CLI команди. */
public interface Command {
    String name();
    String helpLine();
    void execute(String[] args, Session session, PrintStream out);
}
