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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Real-Time Event Ticketing System");
//        setupConfiguration(scanner);
//        startSystem();
        boolean isValid = true;
        while (isValid) {
            System.out.println("\nMenu:");
            System.out.println("1. Setup Configuration");
            System.out.println("2. Save Configuration");
            System.out.println("3. Load Configuration");
            System.out.println("4. View Configuration");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            try {
                Scanner scanner1 = new Scanner(System.in);
                int choice = scanner1.nextInt();
//                scanner.nextLine(); // Consume newline
                switch (choice) {
                    case 1:
                        setupConfiguration(scanner);
                        startSystem();
                        break;
                    case 2:
                        saveConfiguration(scanner);
                        break;
                    case 3:
                        loadConfiguration(scanner);
                        break;
                    case 4:
                        viewConfiguration();
                        break;
                    case 5:
                        System.out.println("Exiting program.....");
                        isValid = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number from 1 to 5.");
                        break;
                }
            }catch (InputMismatchException ex) {
                System.out.println("WARNING!..... Please enter a valid integer.");
            }
        }
    }

    private static void setupConfiguration(Scanner scanner) {
        int maxTicketCapacity;

        System.out.print("Enter total tickets: ");
        int totalTickets = getValidatedInput(scanner, "Total tickets must be a non-negative integer: ");

        System.out.print("Enter ticket release rate: "); // The ticketReleaseRate defines how many tickets a vendor (producer) adds to the ticket pool in a single operation (typically per unit of time, e.g., per second).
        int ticketReleaseRate = getValidatedInput(scanner, "Ticket release rate must be a non-negative integer: ");

        System.out.print("Enter customer retrieval rate: "); // customerRetrievalRate defines how many tickets a customer (consumer) attempts to remove (purchase) from the ticket pool in a single operation (typically per unit of time, e.g., per second).
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
        // Start vendor threads
        Vendor vendor = new Vendor(ticketPool, configuration.getTicketReleaseRate(),configuration.getTotalTickets());
        Thread vendorThread1 = new Thread(vendor);
        Thread vendorThread2 = new Thread(vendor);
        vendorThread1.start();
        vendorThread2.start();

        // Start customer threads
        Customer customer = new Customer(ticketPool, configuration.getCustomerRetrievalRate());
        Thread customerThread1 = new Thread(customer);
        Thread customerThread2 = new Thread(customer);
        Thread customerThread3 = new Thread(customer);
        customerThread1.start();
        customerThread2.start();
        customerThread3.start();
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
//        System.out.print("Enter filename to save configuration: ");
//        String filename = scanner.nextLine();
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
