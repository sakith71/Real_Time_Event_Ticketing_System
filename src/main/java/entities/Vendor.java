package entities;

import model.TicketPool;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int ticketReleaseRate;
    private final int totalTickets;
    private int ticketCount = 0;

    public Vendor(TicketPool ticketPool, int ticketReleaseRate, int totalTickets) {
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
        this.totalTickets = totalTickets;
    }

    @Override
    public void run() {
        try {
            while (ticketCount < totalTickets) {
                String ticket = "Ticket-" + (++ticketCount);
                ticketPool.addTickets(ticket);
                Thread.sleep(ticketReleaseRate);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
