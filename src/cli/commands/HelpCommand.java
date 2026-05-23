package cli.commands;

import cli.Command;
import cli.CommandRegistry;
import cli.Session;

import java.io.PrintStream;

/** Командата help. */
public class HelpCommand implements Command {
    private final CommandRegistry registry;

    public HelpCommand(CommandRegistry registry) {
        this.registry = registry;
    }

    @Override public String name() { return "help"; }
    @Override public String helpLine() { return "help             prints this information"; }

    @Override
    public void execute(String[] args, Session session, PrintStream out) {
        out.println("The following commands are supported:");
        for (Command c : registry.all()) {
            out.println(c.helpLine());
        }
    }
}
