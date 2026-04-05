package cli.commands;

import cli.Command;
import cli.Session;
import serializer.PrettyPrinter;

import java.io.PrintStream;

public class PrintCommand implements Command {
    private final PrettyPrinter printer = new PrettyPrinter();

    @Override public String name() { return "print"; }
    @Override public String helpLine() { return "print            prints the document"; }

    @Override
    public void execute(String[] args, Session session, PrintStream out) {
        session.requireOpen();
        out.print(printer.print(session.getDoc()));
    }
}
