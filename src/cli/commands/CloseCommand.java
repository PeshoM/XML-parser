package cli.commands;

import cli.Command;
import cli.Session;

import java.io.PrintStream;

public class CloseCommand implements Command {
    @Override public String name() { return "close"; }
    @Override public String helpLine() { return "close            closes currently opened file"; }

    @Override
    public void execute(String[] args, Session session, PrintStream out) {
        session.requireOpen();
        Object filename = session.getPath().getFileName();
        session.close();
        out.println("Successfully closed " + filename);
    }
}
