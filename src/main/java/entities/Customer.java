package entities;

import model.TicketPool;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int retrievalRate;
    private final String customerId;

    public Customer(TicketPool ticketPool, int retrievalRate, String customerId) {
        this.ticketPool = ticketPool;
        this.retrievalRate = retrievalRate;
        this.customerId = customerId;
    }

    @Override
    public void run() {
        for (int i = 0; i < retrievalRate; i++) {
            ticketPool.removeTicket(customerId);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
