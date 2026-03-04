package cli;

import java.util.Scanner;

public class CommandLineInterface {
    private boolean running = true;

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (running && scanner.hasNextLine()) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            if (line.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the program...");
                running = false;
            } else {
                System.out.println("Unknown command. (No commands wired yet.)");
            }
        }
    }
}
