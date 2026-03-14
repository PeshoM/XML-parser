package cli.commands;

import cli.Command;
import cli.Session;
import io.FileService;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;

public class OpenCommand implements Command {
    private final FileService fs;

    public OpenCommand(FileService fs) { this.fs = fs; }

    @Override public String name() { return "open"; }
    @Override public String helpLine() { return "open <file>      opens <file>"; }

    @Override
    public void execute(String[] args, Session session, PrintStream out) {
        if (args.length < 1) { out.println(helpLine()); return; }
        Path p = Path.of(args[0]);
        try {
            String content = fs.exists(p) ? fs.readAll(p) : "";
            session.open(p, content);
            out.println("Successfully opened " + p.getFileName());
        } catch (IOException e) {
            out.println("Error: " + e.getMessage());
        }
    }
}
