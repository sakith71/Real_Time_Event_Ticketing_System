package config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

public class Configuration implements Serializable {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    public Configuration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getTotalTickets() {
        return totalTickets;
    }
    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }
    public int getTicketReleaseRate() {
        return ticketReleaseRate;

    }
    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;

    }
    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }
    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;

    }
    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;

    }

    public void saveToFile(String filename) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(this, writer);
            System.out.println("Configuration saved successfully to " + filename);
        } catch (IOException e) {
            System.out.println("Error: Unable to save configuration to file '" + filename + "'. Please check the file path and permissions.");
            e.printStackTrace(); // Log detailed error for developers/administrators
        }
    }

    public static Configuration loadFromFile(String filename) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filename)) {
            return gson.fromJson(reader, Configuration.class);
        } catch (IOException e) {
            System.out.println("Error: Unable to load configuration from file '" + filename + "'. Ensure the file exists and is readable.");
            e.printStackTrace(); // Log detailed error for developers/administrators
        } catch (Exception e) {
            System.out.println("Error: Configuration file is corrupted or in an invalid format. Please verify the content of '" + filename + "'.");
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public String toString() {
        return "Configuration{" +
                "totalTickets=" + totalTickets +
                ", ticketReleaseRate=" + ticketReleaseRate +
                ", customerRetrievalRate=" + customerRetrievalRate +
                ", maxTicketCapacity=" + maxTicketCapacity +
                '}';
    }
}
