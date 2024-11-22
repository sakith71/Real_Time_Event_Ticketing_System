package entities;

import model.TicketPool;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int ticketReleaseRate;
    private final String vendorId;

    public Vendor(TicketPool ticketPool, int ticketReleaseRate, String vendorId) {
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
        this.vendorId = vendorId;
    }

    @Override
    public void run() {
        for (int i = 0; i < ticketReleaseRate; i++) {
            ticketPool.addTickets(ticketReleaseRate, vendorId);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
