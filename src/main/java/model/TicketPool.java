package model;

import java.util.LinkedList;
import java.util.Queue;

public class TicketPool {
    private final Queue<String> tickets;
    private final int maxCapacity;

    public TicketPool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.tickets = new LinkedList<>();
    }

    // Synchronized method to add tickets
    public synchronized void addTickets(String ticket) {
        while (tickets.size() >= maxCapacity) {
            try {
                System.out.println("Ticket pool full. Vendor is waiting...");
                wait();
            } catch (InterruptedException e) {
                System.out.println("Add tickets interrupted.");
            }
        }
        this.tickets.add(ticket);
        System.out.println("Ticket added: " + ticket + " | Total Tickets: " + tickets.size());
        notifyAll();
    }

    // Synchronized method to remove tickets
    public synchronized void removeTicket() {
        while (tickets.isEmpty()) {
            try {
                System.out.println("Not enough tickets. Customer is waiting...");
                wait();
            } catch (InterruptedException e) {
                System.out.println("remove tickets interrupted.");
            }
        }
        String ticket = this.tickets.poll();
        System.out.println("Ticket purchased: " + ticket + " | Remaining Tickets: " + tickets.size());
        notifyAll();
    }
}
