package cli.commands;

import cli.Command;
import cli.Session;
import model.XmlElement;

import java.io.PrintStream;
import java.util.stream.Collectors;

/** Командата children. */
public class ChildrenCommand implements Command {
    @Override public String name() { return "children"; }
    @Override public String helpLine() { return "children <id>    lists attributes of child elements"; }

    @Override
    public void execute(String[] args, Session session, PrintStream out) {
        session.requireOpen();
        if (args.length < 1) { out.println(helpLine()); return; }
        XmlElement el = session.getDoc().findById(args[0]);
        if (el == null) { out.println("Error: element with id '" + args[0] + "' not found"); return; }
        for (XmlElement c : el.getChildren()) {
            String attrs = c.getAttributes().entrySet().stream()
                .map(e -> e.getKey() + "=\"" + e.getValue() + "\"")
                .collect(Collectors.joining(" "));
            out.println("<" + c.getQualifiedName() + (attrs.isEmpty() ? "" : " " + attrs) + ">");
        }
    }
}
