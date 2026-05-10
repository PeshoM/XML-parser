package cli.commands;

import cli.Command;
import cli.Session;
import model.XmlElement;
import serializer.PrettyPrinter;
import xpath.XPathEvaluator;
import xpath.XPathParser;
import xpath.XPathStep;
import xpath.XPathTokenizer;

import java.io.PrintStream;
import java.util.List;

public class XPathCommand implements Command {
    private final PrettyPrinter printer = new PrettyPrinter();

    @Override public String name() { return "xpath"; }
    @Override public String helpLine() { return "xpath <id> <expr>  evaluates XPath starting at element"; }

    @Override
    public void execute(String[] args, Session session, PrintStream out) {
        session.requireOpen();
        if (args.length < 2) { out.println(helpLine()); return; }
        XmlElement start = session.getDoc().findById(args[0]);
        if (start == null) { out.println("Error: element with id '" + args[0] + "' not found"); return; }
        String expr = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
        var tokens = new XPathTokenizer(expr).tokenize();
        List<XPathStep> steps = new XPathParser(tokens).parse();
        List<Object> results = new XPathEvaluator().evaluate(start, steps);
        for (Object r : results) {
            if (r instanceof XmlElement el) {
                out.print(printer.print(new model.XmlDocument(el)));
            } else {
                out.println(r);
            }
        }
    }
}
