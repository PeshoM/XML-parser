package cli.commands;

import cli.Command;
import cli.Session;
import io.FileService;
import serializer.PrettyPrinter;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;

public class SaveAsCommand implements Command {
    private final FileService fs;
    private final PrettyPrinter printer = new PrettyPrinter();

    public SaveAsCommand(FileService fs) { this.fs = fs; }

    @Override public String name() { return "save as"; }
    @Override public String helpLine() { return "save as <file>   saves the currently open file in <file>"; }

    @Override
    public void execute(String[] args, Session session, PrintStream out) {
        session.requireOpen();
        if (args.length < 1) { out.println(helpLine()); return; }
        Path target = Path.of(args[0]);
        try {
            fs.writeAll(target, printer.print(session.getDoc()));
            session.clearModified();
            session.open(target, session.getDoc());
            out.println("Successfully saved " + target.getFileName());
        } catch (IOException e) {
            out.println("Error: " + e.getMessage());
        }
    }
}
