package cli.commands;

import cli.Command;
import cli.Session;
import model.XmlElement;

import java.io.PrintStream;

public class SelectCommand implements Command {
    @Override public String name() { return "select"; }
    @Override public String helpLine() { return "select <id> <key>  prints value of attribute"; }

    @Override
    public void execute(String[] args, Session session, PrintStream out) {
        session.requireOpen();
        if (args.length < 2) { out.println(helpLine()); return; }
        XmlElement el = session.getDoc().findById(args[0]);
        if (el == null) { out.println("Error: element with id '" + args[0] + "' not found"); return; }
        String val = el.getAttributes().get(args[1]);
        if (val == null) { out.println("Error: attribute '" + args[1] + "' not found"); return; }
        out.println(val);
    }
}
