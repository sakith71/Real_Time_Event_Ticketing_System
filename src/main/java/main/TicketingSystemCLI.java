package main;

import config.Configuration;
import entities.Customer;
import entities.Vendor;
import model.TicketPool;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class TicketingSystemCLI {
    private static Configuration configuration;
    private static TicketPool ticketPool;
    private static List<Thread> vendorThreads = new ArrayList<>();
    private static List<Thread> customerThreads = new ArrayList<>();
    private static boolean isSystemRunning = false;

    public static void main(String[] args) {
        System.out.println("Welcome to the Real-Time Event Ticketing System");
        menu();
    }

    private static void menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
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
                        startSystem(scanner);
                        break;
                    case 3:
                        saveConfiguration();
                        break;
                    case 4:
                        loadConfiguration();
                        break;
                    case 5:
                        viewConfiguration();
                        break;
                    case 6:
                        stopAllThreads();
                        break;
                    case 7:
                        stopAllThreads();
                        System.out.println("Exiting program...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException ex) {
                System.out.println("Please enter a valid integer.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private static void setupConfiguration(Scanner scanner) {
        System.out.print("Enter total tickets: ");
        int totalTickets = getValidatedInput(scanner, "Total tickets must be a positive integer: ");

        System.out.print("Enter ticket release rate (ms): ");
        int ticketReleaseRate = getValidatedInput(scanner, "Ticket release rate must be a positive integer: ");

        System.out.print("Enter customer retrieval rate (ms): ");
        int customerRetrievalRate = getValidatedInput(scanner, "Customer retrieval rate must be a positive integer: ");

        System.out.print("Enter maximum ticket pool capacity: ");
        int maxTicketCapacity = getValidatedInput(scanner, "Maximum ticket capacity must be a positive integer: ");

        if (maxTicketCapacity > totalTickets) {
            System.out.println("Maximum ticket capacity cannot exceed total tickets.");
            return;
        }

        configuration = new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
        ticketPool = new TicketPool(maxTicketCapacity);

        System.out.println("Configuration setup completed!");
    }

    private static void startSystem(Scanner scanner) {
        if (configuration == null || ticketPool == null) {
            System.out.println("Please set up the configuration first.");
            return;
        }

        if (isSystemRunning) {
            System.out.println("System is already running. Stop it before restarting.");
            return;
        }

        System.out.print("Enter number of vendors: ");
        int numVendors = getValidatedInput(scanner, "Number of vendors must be a positive integer: ");

        System.out.print("Enter number of customers: ");
        int numCustomers = getValidatedInput(scanner, "Number of customers must be a positive integer: ");

        // Start vendor threads
        for (int i = 0; i < numVendors; i++) {
            int ticketsPerVendor = configuration.getTotalTickets() / numVendors;
            if (i == numVendors - 1) {
                ticketsPerVendor += configuration.getTotalTickets() % numVendors;
            }
            Vendor vendor = new Vendor(ticketPool, configuration.getTicketReleaseRate(), ticketsPerVendor);
            Thread vendorThread = new Thread(vendor, "Vendor-" + i);
            vendorThreads.add(vendorThread);
            vendorThread.start();
        }

        // Start customer threads
        for (int i = 0; i < numCustomers; i++) {
            Customer customer = new Customer(ticketPool, configuration.getCustomerRetrievalRate());
            Thread customerThread = new Thread(customer, "Customer-" + i);
            customerThreads.add(customerThread);
            customerThread.start();
        }

        isSystemRunning = true;
    }

    private static void stopAllThreads() {
        if (!isSystemRunning) {
            System.out.println("System is not running.");
            return;
        }

        // Interrupt vendor threads
        for (Thread vendorThread : vendorThreads) {
            if (vendorThread.isAlive()) {
                vendorThread.interrupt();
            }
        }

        // Interrupt customer threads
        for (Thread customerThread : customerThreads) {
            if (customerThread.isAlive()) {
                customerThread.interrupt();
            }
        }

        isSystemRunning = false;
        System.out.println("All threads stopped.");
    }

    private static void saveConfiguration() {
        if (configuration == null) {
            System.out.println("No configuration to save. Please set up configuration first.");
            return;
        }
        try {
            configuration.saveToFile("ticket_config.json");
            System.out.println("Configuration saved successfully.");
        } catch (Exception e) {
            System.out.println("Error saving configuration: " + e.getMessage());
        }
    }

    private static void loadConfiguration() {
        try {
            Configuration loadedConfig = Configuration.loadFromFile("ticket_config.json");
            if (loadedConfig != null) {
                configuration = loadedConfig;
                ticketPool = new TicketPool(configuration.getMaxTicketCapacity());
                System.out.println("Configuration loaded successfully.");
            } else {
                System.out.println("Failed to load configuration.");
            }
        } catch (Exception e) {
            System.out.println("Error loading configuration: " + e.getMessage());
        }
    }

    private static void viewConfiguration() {
        if (configuration == null) {
            System.out.println("No configuration available.");
        } else {
            System.out.println(configuration);
        }
    }

    private static int getValidatedInput(Scanner scanner, String errorMessage) {
        while (true) {
            try {
                int value = scanner.nextInt();
                if (value <= 0) {
                    System.out.print(errorMessage);
                } else {
                    return value;
                }
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a positive integer: ");
                scanner.next();
            }
        }
    }
}
