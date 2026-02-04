package com.example.testybite;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cart {
    private List<CartItem> items;
	private String hotelName;

    public Cart() {
        items = new ArrayList<>();
    }

    /**
     * Adds an item to the cart. If an item with the same name and variant 
     * already exists, it updates the quantity instead of adding a new entry.
     */
    public void addItem(CartItem newItem) {
        // Look for an existing item that matches the new item (name and variant)
        Optional<CartItem> existingItem = items.stream()
            .filter(item -> 
                item.getName().equals(newItem.getName()) && 
                item.getVariant().equals(newItem.getVariant())
            )
            .findFirst();

        if (existingItem.isPresent()) {
            // If found, update the quantity
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + newItem.getQuantity());
        } else {
            // Otherwise, add the new item to the list
            items.add(newItem);
        }
    }
    
    /**
     * Removes a specific instance of a CartItem from the list.
     * Used when the 'Remove' link is clicked on an item row.
     */
    public void removeItem(CartItem itemToRemove) {
        // Use the exact object reference for removal since we are removing
        // the item selected in the UI.
        items.remove(itemToRemove); 
    }

    /**
     * Finds an item in the cart and reduces its quantity by a specified amount.
     * If the resulting quantity is 0 or less, the item is removed.
     */
    public void removeQuantity(String name, String variant, int quantityToRemove) {
        Optional<CartItem> existingItem = items.stream()
            .filter(item -> 
                item.getName().equals(name) && 
                item.getVariant().equals(variant)
            )
            .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() - quantityToRemove;
            
            if (newQuantity > 0) {
                item.setQuantity(newQuantity);
            } else {
                items.remove(item);
            }
        }
    }


    public void clear() {
        items.clear();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotalPrice() {
        // Ensures stream() is used on the instance field 'items'
        return items.stream()
            .mapToDouble(CartItem::getTotalPrice)
            .sum();
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelName() {
        return hotelName;
    }
}