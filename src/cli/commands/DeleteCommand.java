package cli.commands;

import cli.Command;
import cli.Session;
import model.XmlElement;

import java.io.PrintStream;

/** Командата delete. */
public class DeleteCommand implements Command {
    @Override public String name() { return "delete"; }
    @Override public String helpLine() { return "delete <id> <key>  removes attribute"; }

    @Override
    public void execute(String[] args, Session session, PrintStream out) {
        session.requireOpen();
        if (args.length < 2) { out.println(helpLine()); return; }
        if (args[1].equals("id")) {
            out.println("Error: cannot delete 'id' attribute (system-managed)");
            return;
        }
        XmlElement el = session.getDoc().findById(args[0]);
        if (el == null) { out.println("Error: element with id '" + args[0] + "' not found"); return; }
        if (el.getAttributes().remove(args[1]) == null) {
            out.println("Error: attribute '" + args[1] + "' not found");
            return;
        }
        session.markModified();
        out.println("Attribute deleted.");
    }
}
