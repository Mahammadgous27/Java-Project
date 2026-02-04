package com.example.testybite;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class HotelDetailsPanel extends JPanel {
    private JTextArea itemDisplayArea;
    private JButton bookDiningBtn;
    private List<JCheckBox> menuCheckboxes;

    private HomePage homePage;
    private String currentHotelName;
    private Cart cart;
    private JButton addToCartBtn;

    public HotelDetailsPanel(HomePage homePage, String hotelName, Cart cart) {
        this.homePage = homePage;
        this.currentHotelName = hotelName;
        this.cart = cart;

        menuCheckboxes = new ArrayList<>();
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        // add LEFT INFO PANEL
        add(createHotelInfoPanel(), BorderLayout.WEST);

        JScrollPane scrollPane = new JScrollPane(createContent());
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    // ------------------------------------------------------------
    // LEFT SIDE HOTEL INFO PANEL
    // ------------------------------------------------------------
    private JPanel createHotelInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setPreferredSize(new Dimension(250, 600));
        infoPanel.setBackground(new Color(224, 235, 255));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel name = new JLabel(currentHotelName);
        name.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel location = new JLabel("📍 Banjara Hills, Hyderabad");
        location.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel rating = new JLabel("⭐ Rating: 4.5 / 5");
        rating.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel website = new JLabel("<html><a href=''>🌐Visit Website</a></html>");
        website.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        website.setFont(new Font("Arial", Font.PLAIN, 16));

        infoPanel.add(name);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(location);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(rating);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(website);
        infoPanel.add(Box.createVerticalGlue());

        return infoPanel;
    }

    // ------------------------------------------------------------
    // MAIN CONTENT PANEL (Right side)
    // ------------------------------------------------------------
    private JPanel createContent() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        contentPanel.setBackground(new Color(240, 248, 255));

        // ===========================
        // HEADER PANEL (FULL WIDTH)
        // ===========================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(224, 235, 255));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // LEFT SIDE → HOTEL DETAILS
        JPanel leftHeader = new JPanel();
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.Y_AXIS));
        leftHeader.setBackground(new Color(224, 235, 255));

        JLabel hotelNameLabel = new JLabel(currentHotelName);
        hotelNameLabel.setFont(new Font("Arial", Font.BOLD, 26));

        

       

        leftHeader.add(hotelNameLabel);
        leftHeader.add(Box.createVerticalStrut(5));


        // RIGHT SIDE → BOOKING BUTTONS
        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightHeader.setBackground(new Color(224, 235, 255));

        // Book Dining Button
        bookDiningBtn = new JButton("Book Dining");
        bookDiningBtn.setBackground(new Color(0, 51, 153));
        bookDiningBtn.setForeground(Color.WHITE);
        bookDiningBtn.setFocusPainted(false);
        bookDiningBtn.setOpaque(true);
        bookDiningBtn.setBorderPainted(false);
        bookDiningBtn.addActionListener(e -> {
            new BookDining(currentHotelName, bookingItem -> {
                toggleBookDiningStatus();  // callback from child window
            });
        });


        // Book Room Button
        JButton bookRoomBtn = new JButton("Book Room");
        bookRoomBtn.setBackground(new Color(102, 0, 204));
        bookRoomBtn.setForeground(Color.WHITE);
        bookRoomBtn.setFocusPainted(false);
        bookRoomBtn.setOpaque(true);
        bookRoomBtn.setBorderPainted(false);
        bookRoomBtn.addActionListener(e -> new BookRoom(currentHotelName));

        rightHeader.add(bookDiningBtn);
        rightHeader.add(Box.createHorizontalStrut(10));
        rightHeader.add(bookRoomBtn);

        // Add left + right to header
        header.add(leftHeader, BorderLayout.WEST);
        header.add(rightHeader, BorderLayout.EAST);

        contentPanel.add(header);
        contentPanel.add(Box.createVerticalStrut(20));

        // MENU TITLE
        JLabel menuTitle = new JLabel("🍴 Our Delicious Menu");
        menuTitle.setFont(new Font("Arial", Font.BOLD, 22));
        menuTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(menuTitle);
        contentPanel.add(Box.createVerticalStrut(10));

        // MENU UI PANEL
        JPanel menuPanel = createMenuPanel();
        contentPanel.add(menuPanel);
        contentPanel.add(Box.createVerticalStrut(30));

        // CART PANEL
        JPanel cartActions = new JPanel(new BorderLayout());
        cartActions.setAlignmentX(Component.LEFT_ALIGNMENT);
        cartActions.setBackground(new Color(240, 248, 255));

        itemDisplayArea = new JTextArea("Selected Items will appear here...", 5, 20);
        itemDisplayArea.setEditable(false);
        itemDisplayArea.setBorder(BorderFactory.createTitledBorder("Review Order"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(240, 248, 255));

        addToCartBtn = new JButton("Add Selected Items to Cart");
        addToCartBtn.setBackground(new Color(0, 100, 0));
        addToCartBtn.setForeground(Color.WHITE);
        addToCartBtn.setFocusPainted(false);
        addToCartBtn.setOpaque(true);
        addToCartBtn.setBorderPainted(false);
        addToCartBtn.addActionListener(e -> addSelectedItemsToCart());

        buttonPanel.add(addToCartBtn);

        cartActions.add(new JScrollPane(itemDisplayArea), BorderLayout.CENTER);
        cartActions.add(buttonPanel, BorderLayout.SOUTH);

        contentPanel.add(cartActions);
        contentPanel.add(Box.createVerticalGlue());

        return contentPanel;
    }


    // ------------------------------------------------------------
    // MENU PANEL
    // ------------------------------------------------------------
    private JPanel createMenuPanel() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(Color.WHITE);
        menu.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        menu.setAlignmentX(Component.LEFT_ALIGNMENT);

        LinkedHashMap<String, LinkedHashMap<String, Double>> menuSections = new LinkedHashMap<>();

        // Snacks
        LinkedHashMap<String, Double> snacks = new LinkedHashMap<>();
        snacks.put("French Fries", 120.0);
        snacks.put("Cheese Balls", 150.0);
        snacks.put("Samosa", 80.0);
        snacks.put("Veg Sandwich", 100.0);
        menuSections.put("🍟 Snacks", snacks);

        // Starters
        LinkedHashMap<String, Double> starters = new LinkedHashMap<>();
        starters.put("Veg Spring Roll", 180.0);
        starters.put("Chicken Kebab", 320.0);
        starters.put("Paneer Tikka", 250.0);
        starters.put("Crispy Corn", 190.0);
        menuSections.put("🥗 Starters", starters);

        // Main Course
        LinkedHashMap<String, Double> mains = new LinkedHashMap<>();
        mains.put("Veg Biryani", 250.0);
        mains.put("Butter Chicken", 380.0);
        mains.put("Paneer Butter Masala", 300.0);
        mains.put("Dal Tadka", 220.0);
        mains.put("Jeera Rice", 180.0);
        menuSections.put("🍛 Main Course", mains);

        // Desserts
        LinkedHashMap<String, Double> desserts = new LinkedHashMap<>();
        desserts.put("Chocolate Cake", 120.0);
        desserts.put("Ice Cream", 80.0);
        desserts.put("Gulab Jamun", 90.0);
        desserts.put("Brownie with Ice Cream", 150.0);
        menuSections.put("🍰 Desserts", desserts);

        // Drinks
        LinkedHashMap<String, Double> drinks = new LinkedHashMap<>();
        drinks.put("Coffee (Latte)", 150.0);
        drinks.put("Fresh Juice", 100.0);
        drinks.put("Cold Drink", 60.0);
        drinks.put("Masala Chai", 50.0);
        menuSections.put("🥤 Drinks", drinks);

        // Build Menu UI
        for (Map.Entry<String, LinkedHashMap<String, Double>> s : menuSections.entrySet()) {
            JLabel sectionLabel = new JLabel(s.getKey());
            sectionLabel.setFont(new Font("Arial", Font.BOLD, 18));
            sectionLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
            menu.add(sectionLabel);

            JSeparator separator = new JSeparator();
            separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            menu.add(separator);

            JPanel itemsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
            itemsPanel.setBackground(Color.WHITE);
            itemsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 10, 20));

            for (Map.Entry<String, Double> item : s.getValue().entrySet()) {
                JCheckBox cb = new JCheckBox(item.getKey() + "  ₹" + String.format("%.2f", item.getValue()));
                cb.setBackground(Color.WHITE);
                cb.putClientProperty("price", item.getValue());
                cb.putClientProperty("variant", item.getKey());

                menuCheckboxes.add(cb);
                itemsPanel.add(cb);

                cb.addActionListener(e -> getSelectedItems());
            }

            menu.add(itemsPanel);
            menu.add(Box.createVerticalStrut(10));
        }

        return menu;
    }

    // ------------------------------------------------------------
    // ADD TO CART
    // ------------------------------------------------------------
    private void addSelectedItemsToCart() {
        List<CartItem> cartItems = new ArrayList<>();

        for (JCheckBox cb : menuCheckboxes) {
            if (cb.isSelected()) {
                String itemName = (String) cb.getClientProperty("variant");
                double price = (double) cb.getClientProperty("price");
                int quantity = 1; // default quantity; can be enhanced to select quantity
                cartItems.add(new CartItem(itemName, quantity, price, itemName));
            }
        }

        // Add to cart for UI
        for (CartItem item : cartItems) {
            cart.addItem(item);
        }
        homePage.refreshCartPanel();

        // Insert selected items into Orders table
        insertOrdersToDatabase(cartItems);
    }

    private void insertOrdersToDatabase(List<CartItem> items) {
        int reservationId = 1; // replace with actual reservation_id
        int staffId = 1;       // replace with actual staff_id
        java.sql.Timestamp orderTime = new java.sql.Timestamp(System.currentTimeMillis());

        for (CartItem item : items) {
            String itemName = item.getName();
            double price = item.getUnitPrice();
            int quantity = item.getQuantity();

            boolean success = DatabaseConnector.insertOrder(reservationId, staffId, itemName, price, quantity, orderTime, "New");
            if (!success) {
                JOptionPane.showMessageDialog(this, "Failed to insert " + itemName + " into Orders table!", "DB Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        JOptionPane.showMessageDialog(this, "✅ Selected items added to Orders successfully!");
    }

    private void toggleBookDiningStatus() {
        bookDiningBtn.setText("Dining Booked");
        bookDiningBtn.setBackground(new Color(0, 200, 0));
    }

    private List<String> getSelectedItems() {
        List<String> selected = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        double total = 0;

        for (JCheckBox cb : menuCheckboxes) {
            if (cb.isSelected()) {
                String item = (String) cb.getClientProperty("variant");
                double price = (double) cb.getClientProperty("price");
                sb.append("• ").append(item).append("  Rs.").append(String.format("%.2f", price)).append("\n");
                total += price;
                selected.add(item);
            }
        }

        sb.append("\nTotal: Rs. ").append(String.format("%.2f", total));
        itemDisplayArea.setText(sb.toString());
        return selected;
    }
}
