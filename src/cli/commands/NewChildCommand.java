package cli.commands;

import cli.Command;
import cli.Session;
import model.XmlDocument;
import model.XmlElement;

import java.io.PrintStream;

/** Командата newchild. */
public class NewChildCommand implements Command {
    @Override public String name() { return "newchild"; }
    @Override public String helpLine() { return "newchild <id>    adds a new child to element"; }

    @Override
    public void execute(String[] args, Session session, PrintStream out) {
        session.requireOpen();
        if (args.length < 1) { out.println(helpLine()); return; }
        XmlDocument doc = session.getDoc();
        XmlElement parent = doc.findById(args[0]);
        if (parent == null) { out.println("Error: element with id '" + args[0] + "' not found"); return; }
        XmlElement child = new XmlElement("element");
        String newId = doc.nextGeneratedId();
        child.setId(newId);
        child.getAttributes().put("id", newId);
        parent.addChild(child);
        doc.registerId(newId, child);
        session.markModified();
        out.println("Added child with id " + newId);
    }
}
