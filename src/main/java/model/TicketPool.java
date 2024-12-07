package model;

import entities.Ticket;
import java.util.LinkedList;
import java.util.Queue;

public class TicketPool {
    private final Queue<Ticket> tickets;
    private final int maxCapacity;
    private int addTicketCount;
    private int removeTicketCount;

    public TicketPool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.tickets = new LinkedList<>();
    }

    // Synchronized method to add tickets
    public synchronized void addTickets(Ticket ticket) {
        while (tickets.size() >= maxCapacity) {
            try {
                System.out.println("Ticket pool full. Vendor is waiting...");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        this.tickets.add(ticket);
        addTicketCount++;
        System.out.println("Ticket " + addTicketCount + " added by " + Thread.currentThread().getName() + " | Total Tickets: " + tickets.size());
        notifyAll();
    }

    // Synchronized method to remove tickets
    public synchronized void removeTicket() {
        while (tickets.isEmpty()) {
            try {
                System.out.println("No tickets available. Customer is waiting...");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        Ticket ticket = tickets.poll();
        removeTicketCount++;
        System.out.println("Ticket " + removeTicketCount + " bought by " + Thread.currentThread().getName() + " | Remaining Tickets: " + tickets.size());
        notifyAll();
    }
}
