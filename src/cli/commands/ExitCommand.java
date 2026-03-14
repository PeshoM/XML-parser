package cli.commands;

import cli.Command;
import cli.Session;

import java.io.PrintStream;

public class ExitCommand implements Command {
    private final Runnable shutdown;

    public ExitCommand(Runnable shutdown) {
        this.shutdown = shutdown;
    }

    @Override public String name() { return "exit"; }
    @Override public String helpLine() { return "exit             exits the program"; }

    @Override
    public void execute(String[] args, Session session, PrintStream out) {
        out.println("Exiting the program...");
        shutdown.run();
    }
}
