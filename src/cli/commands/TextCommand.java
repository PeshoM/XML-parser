package cli.commands;

import cli.Command;
import cli.Session;
import model.XmlElement;

import java.io.PrintStream;

public class TextCommand implements Command {
    @Override public String name() { return "text"; }
    @Override public String helpLine() { return "text <id>        prints text of element"; }

    @Override
    public void execute(String[] args, Session session, PrintStream out) {
        session.requireOpen();
        if (args.length < 1) { out.println(helpLine()); return; }
        XmlElement el = session.getDoc().findById(args[0]);
        if (el == null) { out.println("Error: element with id '" + args[0] + "' not found"); return; }
        out.println(el.getText() == null ? "" : el.getText());
    }
}
