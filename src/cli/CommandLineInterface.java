package cli;

import cli.commands.ExitCommand;
import cli.commands.HelpCommand;

import java.util.Scanner;

public class CommandLineInterface {
    private final CommandRegistry registry = new CommandRegistry();
    private boolean running = true;

    public CommandLineInterface() {
        registry.register(new HelpCommand(registry));
        registry.register(new ExitCommand(() -> running = false));
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (running && scanner.hasNextLine()) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] tokens = line.split("\\s+");
            Command c = registry.lookup(tokens);
            if (c == null) {
                System.out.println("Unknown command. Type 'help' for the list.");
                continue;
            }
            int consumed = registry.leadingTokensConsumed(tokens);
            String[] rest = new String[tokens.length - consumed];
            System.arraycopy(tokens, consumed, rest, 0, rest.length);
            try {
                c.execute(rest, System.out);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
