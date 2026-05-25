package cli;

import cli.commands.ChildCommand;
import cli.commands.ChildrenCommand;
import cli.commands.CloseCommand;
import cli.commands.DeleteCommand;
import cli.commands.ExitCommand;
import cli.commands.HelpCommand;
import cli.commands.OpenCommand;
import cli.commands.PrintCommand;
import cli.commands.SaveCommand;
import cli.commands.SaveAsCommand;
import cli.commands.SelectCommand;
import cli.commands.SetCommand;
import cli.commands.NewChildCommand;
import cli.commands.TextCommand;
import cli.commands.XPathCommand;
import io.FileService;

import java.util.Scanner;

/** REPL цикъл: чете команди от stdin и ги изпълнява. */
public class CommandLineInterface {
    private final CommandRegistry registry = new CommandRegistry();
    private final Session session = new Session();
    private final ArgumentTokenizer argTokenizer = new ArgumentTokenizer();
    private boolean running = true;

    public CommandLineInterface() {
        FileService fs = new FileService();
        registry.register(new OpenCommand(fs));
        registry.register(new CloseCommand());
        registry.register(new SaveCommand(fs));
        registry.register(new SaveAsCommand(fs));
        registry.register(new PrintCommand());
        registry.register(new SelectCommand());
        registry.register(new SetCommand());
        registry.register(new DeleteCommand());
        registry.register(new ChildrenCommand());
        registry.register(new ChildCommand());
        registry.register(new TextCommand());
        registry.register(new NewChildCommand());
        registry.register(new XPathCommand());
        registry.register(new HelpCommand(registry));
        registry.register(new ExitCommand(() -> running = false));
    }

    /** Стартира REPL цикъла. */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        Command help = registry.lookup(new String[] { "help" });
        if (help != null) help.execute(new String[0], session, System.out);
        while (running && scanner.hasNextLine()) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;
            String[] tokens = argTokenizer.tokenize(input);
            Command c = registry.lookup(tokens);
            if (c == null) { System.out.println("Unknown command. Type 'help' for the list."); continue; }
            int consumed = registry.leadingTokensConsumed(tokens);
            String[] rest = new String[tokens.length - consumed];
            System.arraycopy(tokens, consumed, rest, 0, rest.length);
            try {
                c.execute(rest, session, System.out);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
