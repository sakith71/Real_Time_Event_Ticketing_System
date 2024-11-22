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
    public synchronized void addTickets(int count, String vendorId) {
        while (tickets.size() + count > maxCapacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        for (int i = 0; i < count; i++) {
            tickets.add("Ticket-" + (tickets.size() + 1));
        }
        System.out.println(count + " tickets added. Current tickets available: " + tickets.size());
        notifyAll();
    }


    // Synchronized method to remove tickets
    public synchronized void removeTicket(String customerId) {
        while (tickets.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
//                return null;
            }
        }
        String ticket = tickets.poll();
        System.out.println("Ticket sold: " + ticket + ". Tickets left: " + tickets.size());
        notifyAll();
//        return ticket;
    }
}
