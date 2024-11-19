package main;

import config.Configuration;

import java.io.IOException;
import java.util.Scanner;

public class TicketingSystemCLI {
    private static Configuration configuration;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Real-Time Event Ticketing System");

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Setup Configuration");
            System.out.println("2. Save Configuration");
            System.out.println("3. Load Configuration");
            System.out.println("4. View Configuration");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> setupConfiguration(scanner);
                case 2 -> saveConfiguration(scanner);
                case 3 -> loadConfiguration(scanner);
                case 4 -> viewConfiguration();
                case 5 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void setupConfiguration(Scanner scanner) {
        System.out.println("Enter total tickets:");
        int totalTickets = scanner.nextInt();

        System.out.println("Enter ticket release rate:");
        int ticketReleaseRate = scanner.nextInt();

        System.out.println("Enter customer retrieval rate:");
        int customerRetrievalRate = scanner.nextInt();

        System.out.println("Enter maximum ticket capacity:");
        int maxTicketCapacity = scanner.nextInt();

        configuration = new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
        System.out.println("Configuration setup completed!");
    }

    private static void saveConfiguration(Scanner scanner) {
        if (configuration == null) {
            System.out.println("No configuration to save. Setup configuration first.");
            return;
        }

        System.out.print("Enter filename to save configuration: ");
        String filename = scanner.nextLine();

        try {
            configuration.saveToFile(filename);
            System.out.println("Configuration saved to " + filename);
        } catch (Exception e) {
            System.out.println("Error saving configuration: " + e.getMessage());

        }
    }

    private static void loadConfiguration(Scanner scanner) {
        System.out.print("Enter filename to load configuration: ");
        String filename = scanner.nextLine();

        try {
            configuration = Configuration.loadFromFile(filename);
            System.out.println("Configuration loaded from " + filename);
        } catch (Exception e) {
            System.out.println("Error loading configuration: " + e.getMessage());
        }
    }

    private static void viewConfiguration() {
        if (configuration == null) {
            System.out.println("No configuration available. Setup or load a configuration first.");
        } else {
            System.out.println("Current Configuration:");
            System.out.println(configuration.toString());
        }
    }
}
