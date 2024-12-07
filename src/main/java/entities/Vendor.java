package entities;

import model.TicketPool;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int ticketReleaseRate;
    private final int totalTickets;

    public Vendor(TicketPool ticketPool, int ticketReleaseRate, int totalTickets) {
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
        this.totalTickets = totalTickets;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= totalTickets; i++) {
                Ticket ticket = new Ticket(i);
                ticketPool.addTickets(ticket);
                Thread.sleep(ticketReleaseRate);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
