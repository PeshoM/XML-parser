package cli.commands;

import cli.Command;
import cli.Session;
import io.FileService;
import serializer.PrettyPrinter;

import java.io.IOException;
import java.io.PrintStream;

public class SaveCommand implements Command {
    private final FileService fs;
    private final PrettyPrinter printer = new PrettyPrinter();

    public SaveCommand(FileService fs) { this.fs = fs; }

    @Override public String name() { return "save"; }
    @Override public String helpLine() { return "save             saves the currently open file"; }

    @Override
    public void execute(String[] args, Session session, PrintStream out) {
        session.requireOpen();
        try {
            fs.writeAll(session.getPath(), printer.print(session.getDoc()));
            session.clearModified();
            out.println("Successfully saved " + session.getPath().getFileName());
        } catch (IOException e) {
            out.println("Error: " + e.getMessage());
        }
    }
}
