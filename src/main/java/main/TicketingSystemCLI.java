package main;

import config.Configuration;
import entities.Customer;
import entities.Vendor;
import model.TicketPool;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TicketingSystemCLI {
    private static Configuration configuration;
    private static TicketPool ticketPool;
    private static Thread vendorThread;
    private static Thread customerThread;
    private static volatile boolean isSystemRunning = false; // Flag to control thread execution

    public static void main(String[] args) {
        System.out.println("Welcome to the Real-Time Event Ticketing System");
        menu();
    }

    private static void menu() {
        Scanner scanner = new Scanner(System.in);
        boolean isValid = true;

        while (isValid) {
            System.out.println("\nMenu:");
            System.out.println("1. Setup Configuration");
            System.out.println("2. Start System");
            System.out.println("3. Save Configuration");
            System.out.println("4. Load Configuration");
            System.out.println("5. View Configuration");
            System.out.println("6. Stop All Threads");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            try {
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        setupConfiguration(scanner);
                        break;
                    case 2:
                        startSystem();
                        break;
                    case 3:
                        saveConfiguration(scanner);
                        break;
                    case 4:
                        loadConfiguration(scanner);
                        break;
                    case 5:
                        viewConfiguration();
                        break;
                    case 6:
                        stopAllThreads();
                        break;
                    case 7:
                        System.out.println("Exiting program.....");
                        isValid = false;
                        stopAllThreads(); // Stop threads when exiting
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException ex) {
                System.out.println("WARNING!..... Please enter a valid integer.");
                scanner.nextLine(); // Clear invalid input
            }
        }
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
        while (true) {
            maxTicketCapacity = getValidatedInput(scanner, "Maximum ticket capacity must be a non-negative integer: ");
            if (maxTicketCapacity > totalTickets) {
                System.out.print("Maximum ticket capacity cannot be greater than the total ticket capacity: ");
            } else {
                break;
            }
        }

        configuration = new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
        System.out.println("Configuration setup completed!");
        ticketPool = new TicketPool(maxTicketCapacity);
    }

    private static void startSystem() {
        if (configuration == null || ticketPool == null) {
            System.out.println("Configuration is not set up. Please set up the configuration first.");
            return;
        }

        if (isSystemRunning) {
            System.out.println("System is already running. Please stop the system before starting it again.");
            return;
        }

        // Start vendor thread
        Vendor vendor = new Vendor(ticketPool, configuration.getTicketReleaseRate(), configuration.getTotalTickets());
        vendorThread = new Thread(vendor);
        vendorThread.start();

        // Start customer thread
        Customer customer = new Customer(ticketPool, configuration.getCustomerRetrievalRate());
        customerThread = new Thread(customer);
        customerThread.start();

        isSystemRunning = true;
        System.out.println("System started. Vendor and Customer threads are running.");
    }

    private static void stopAllThreads() {
        if (vendorThread != null && vendorThread.isAlive()) {
            vendorThread.interrupt();
            System.out.println("Vendor thread interrupted.");
        } else {
            System.out.println("Vendor thread is not running.");
        }

        if (customerThread != null && customerThread.isAlive()) {
            customerThread.interrupt();
            System.out.println("Customer thread interrupted.");
        } else {
            System.out.println("Customer thread is not running.");
        }

        isSystemRunning = false;
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
        try {
            configuration.saveToFile("ticket_config.json");
            System.out.println("Configuration saved successfully");
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
