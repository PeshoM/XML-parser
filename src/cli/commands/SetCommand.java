package cli.commands;

import cli.Command;
import cli.Session;
import model.XmlElement;

import java.io.PrintStream;

public class SetCommand implements Command {
    @Override public String name() { return "set"; }
    @Override public String helpLine() { return "set <id> <key> <value>  sets attribute"; }

    @Override
    public void execute(String[] args, Session session, PrintStream out) {
        session.requireOpen();
        if (args.length < 3) { out.println(helpLine()); return; }
        if (args[1].equals("id")) {
            out.println("Error: cannot modify 'id' attribute (system-managed)");
            return;
        }
        XmlElement el = session.getDoc().findById(args[0]);
        if (el == null) { out.println("Error: element with id '" + args[0] + "' not found"); return; }
        el.getAttributes().put(args[1], args[2]);
        session.markModified();
        out.println("Attribute set.");
    }
}
