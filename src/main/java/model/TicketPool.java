package model;

import java.util.LinkedList;
import java.util.Queue;

public class TicketPool {
    private final Queue<String> tickets = new LinkedList<>();
    private final int maxCapacity;

    public TicketPool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    // Synchronized method to add tickets
    public synchronized void addTickets(String ticket) {
        while (tickets.size() >= maxCapacity) {
            try {
                System.out.println("Ticket pool full. Vendor is waiting...");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        tickets.offer(ticket);
        System.out.println("Ticket added: " + ticket + " | Total Tickets: " + tickets.size());
        notifyAll();
    }

    // Synchronized method to remove tickets
    public synchronized String removeTicket() {
        while (tickets.isEmpty()) {
            try {
                System.out.println("Not enough tickets. Customer is waiting...");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        String ticket = tickets.poll();
        System.out.println("Ticket purchased: " + ticket + " | Remaining Tickets: " + tickets.size());
        notifyAll();
        return ticket;
    }
}
