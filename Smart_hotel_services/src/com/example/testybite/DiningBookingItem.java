package com.example.testybite;

public class DiningBookingItem {
    private String customerName;
    private String hotelName;
    private String date;
    private String time;
    private int numPersons;

    public DiningBookingItem(String customerName, String hotelName, String date, String time, int numPersons) {
        this.customerName = customerName;
        this.hotelName = hotelName;
        this.date = date;
        this.time = time;
        this.numPersons = numPersons;
    }

    // Getters
    public String getCustomerName() { return customerName; }
    public String getHotelName() { return hotelName; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public int getNumPersons() { return numPersons; }
}
