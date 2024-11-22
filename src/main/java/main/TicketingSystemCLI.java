package main;

import config.Configuration;
import entities.Customer;
import entities.Vendor;
import model.TicketPool;

import java.util.Scanner;

public class TicketingSystemCLI {
    private static Configuration configuration;
    private static TicketPool ticketPool;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Real-Time Event Ticketing System");
        setupConfiguration(scanner);
        startSystem();

//        while (true) {
//            System.out.println("\nMenu:");
//            System.out.println("1. Setup Configuration");
//            System.out.println("2. Save Configuration");
//            System.out.println("3. Load Configuration");
//            System.out.println("4. View Configuration");
//            System.out.println("5. Exit");
//            System.out.print("Choose an option: ");
//
//            int choice = scanner.nextInt();
//            scanner.nextLine(); // Consume newline
//
//            switch (choice) {
//                case 1 -> setupConfiguration(scanner);
//                case 2 -> saveConfiguration(scanner);
//                case 3 -> loadConfiguration(scanner);
//                case 4 -> viewConfiguration();
//                case 5 -> {
//                    System.out.println("Exiting...");
//                    return;
//                }
//                default -> System.out.println("Invalid choice. Try again.");
//            }
//        }
    }

    private static void setupConfiguration(Scanner scanner) {
        int maxTicketCapacity;

        System.out.print("Enter total tickets: ");
        int totalTickets = getValidatedInput(scanner, "Total tickets must be a non-negative integer: ");

        System.out.print("Enter ticket release rate: ");
        int ticketReleaseRate = getValidatedInput(scanner, "Ticket release rate must be a non-negative integer: ");

        System.out.print("Enter customer retrieval rate: ");
        int customerRetrievalRate = getValidatedInput(scanner, "Customer retrieval rate must be a non-negative integer: ");

        System.out.print("Enter maximum ticket capacity: ");
        while (true){
            maxTicketCapacity = getValidatedInput(scanner, "Maximum ticket capacity must be a non-negative integer: ");
            if(maxTicketCapacity > totalTickets){
                System.out.print("Maximum ticket capacity cannot be greater than the total ticket capacity: ");
            }
            else break;
        }

        configuration = new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
        System.out.println("Configuration setup completed!");
        ticketPool = new TicketPool(maxTicketCapacity);
    }

    private static void startSystem() {
        int numVendors = 2;  // Assuming 2 vendors
        int numCustomers = 3; // Assuming 3 customers

        // Start vendor threads
        for (int i = 0; i < numVendors; i++) {
            Vendor vendor = new Vendor(ticketPool, configuration.getTicketReleaseRate(), "Vendor-" + (i + 1));
            Thread vendorThread = new Thread(vendor);
            vendorThread.start();
        }

        // Start customer threads
        for (int i = 0; i < numCustomers; i++) {
            Customer customer = new Customer(ticketPool, configuration.getCustomerRetrievalRate(), "Customer-" + (i + 1));
            Thread customerThread = new Thread(customer);
            customerThread.start();
        }
    }

    private static int getValidatedInput(Scanner scanner, String errorMessage) {
        int value;
        while (true) {
            try {
                value = scanner.nextInt();
                if (value < 0) {
                    System.out.print(errorMessage);
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.print("Invalid input. Please enter a valid integer: ");
                scanner.next(); // Clear the invalid input
            }
        }
        return value;
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
