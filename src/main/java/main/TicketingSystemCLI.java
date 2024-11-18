package main;

import config.Configuration;
import java.util.Scanner;

public class TicketingSystemCLI {
    private static Configuration configuration;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Real-Time Event Ticketing System");

        System.out.println("Enter total tickets:");
        int totalTickets = scanner.nextInt();

        System.out.println("Enter ticket release rate:");
        int ticketReleaseRate = scanner.nextInt();

        System.out.println("Enter customer retrieval rate:");
        int customerRetrievalRate = scanner.nextInt();

        System.out.println("Enter maximum ticket capacity:");
        int maxTicketCapacity = scanner.nextInt();

        System.out.println("Configuration Completed!");

        configuration = new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
    }
}
