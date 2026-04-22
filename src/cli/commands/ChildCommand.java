package cli.commands;

import cli.Command;
import cli.Session;
import model.XmlElement;
import serializer.PrettyPrinter;

import java.io.PrintStream;

public class ChildCommand implements Command {
    private final PrettyPrinter printer = new PrettyPrinter();

    @Override public String name() { return "child"; }
    @Override public String helpLine() { return "child <id> <n>   shows nth child (0-based)"; }

    @Override
    public void execute(String[] args, Session session, PrintStream out) {
        session.requireOpen();
        if (args.length < 2) { out.println(helpLine()); return; }
        XmlElement el = session.getDoc().findById(args[0]);
        if (el == null) { out.println("Error: element with id '" + args[0] + "' not found"); return; }
        int n;
        try { n = Integer.parseInt(args[1]); }
        catch (NumberFormatException ex) { out.println("Error: invalid index '" + args[1] + "'"); return; }
        if (n < 0 || n >= el.getChildren().size()) { out.println("Error: index out of range"); return; }
        XmlElement child = el.getChildren().get(n);
        out.print(printer.print(new model.XmlDocument(child)));
    }
}
