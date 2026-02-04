package com.example.testybite;

public class CartItem {
    private String name;
    private int quantity;
    private double unitPrice; // Renamed 'price' to 'unitPrice' for clarity
    private String variant;   // New field for item specification (e.g., 'Veg - Spicy', '350ml')

    // --- Constructor Updated ---
    public CartItem(String name, int quantity, double unitPrice, String variant) {
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        // Ensure variant is not null, default to an empty string if needed
        this.variant = (variant != null) ? variant : "";
    }

    // --- Getters ---
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    // Renamed to getUnitPrice to reflect that it's the price for one item
    public double getUnitPrice() { return unitPrice; } 

    // ✅ Implemented Getter for CartPanel/Cart logic
    public String getVariant() {
        return variant;
    }

    public double getTotalPrice() { return unitPrice * quantity; }

    // --- Setter added for Quantity Control in CartPanel ---
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
}