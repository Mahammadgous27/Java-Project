package com.example.testybite;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

public class CartPanel extends JPanel {
    private Cart cart;
    private final Color PANEL_BACKGROUND = Color.WHITE;
    private final Color PRIMARY_BLUE = new Color(0, 102, 204); 
    private final Color BLUE_BACKGROUND = new Color(240, 248, 255); 

    // UI Components for dynamic update
    private JPanel itemsListPanel;
    private JLabel subtotalValueLabel;
    private JLabel gstValueLabel;      
    private JLabel payableValueLabel;  
    
    // Constants for calculation
    private static final double GST_RATE = 0.05; // 5% GST
    private final DecimalFormat df = new DecimalFormat("₹0.00");

    public CartPanel(Cart cart) {
        this.cart = cart;

        setLayout(new GridLayout(1, 2, 20, 0)); 
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 
        setBackground(BLUE_BACKGROUND);

        // --- 1. Left Panel: Items in Cart ---
        JPanel itemsContainer = createItemsInCartPanel();
        add(itemsContainer);

        // --- 2. Right Panel: Order Summary ---
        JPanel summaryContainer = createOrderSummaryPanel();
        add(summaryContainer);

        // Initial refresh
        refreshCart();
    }

    // 🚨 CRITICAL FIX: The method HomePage calls to refresh the display
    public void refreshCart() {
        // 1. Clear existing items
        itemsListPanel.removeAll();
        
        List<CartItem> items = cart.getItems();

        // ✅ Debug: print current items
        System.out.println("Refreshing Cart Panel. Current items in cart:");
        for (CartItem item : items) {
            System.out.println(" - " + item.getName() + " | Qty: " + item.getQuantity() + " | Total: " + item.getTotalPrice());
        }

        // 2. Repopulate the list
        if (items.isEmpty()) {
            JLabel emptyLabel = new JLabel("Your cart is empty. Add some delicious food!", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            itemsListPanel.add(emptyLabel);
        } else {
            for (CartItem item : items) {
                itemsListPanel.add(createCartItemRow(item));
            }
        }
        
        // 3. Recalculate and update totals
        updateSummaryTotals();

        // 4. Validate and repaint the panel
        itemsListPanel.revalidate();
        itemsListPanel.repaint();
        System.out.println("Cart has " + items.size() + " items:");
        for (CartItem ci : items) {
            System.out.println(" - " + ci.getName() + " | Qty: " + ci.getQuantity());
        }
    }

    
    private void updateSummaryTotals() {
        double subtotal = cart.getTotalPrice();
        double gst = subtotal * GST_RATE;
        // Assuming no service charge or discount for this calculation
        double payable = subtotal + gst; 
        
        subtotalValueLabel.setText(df.format(subtotal));
        gstValueLabel.setText(df.format(gst));
        payableValueLabel.setText(df.format(payable));
    }


    // =========================================================================
    // UI COMPONENT CREATION
    // =========================================================================

    private JPanel createItemsInCartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("🛒 Items in Cart");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(title, BorderLayout.NORTH);

        itemsListPanel = new JPanel();
        itemsListPanel.setLayout(new BoxLayout(itemsListPanel, BoxLayout.Y_AXIS));
        itemsListPanel.setBackground(PANEL_BACKGROUND);

        JScrollPane scrollPane = new JScrollPane(itemsListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); 
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
    
    private JPanel createCartItemRow(CartItem item) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        row.setBackground(PANEL_BACKGROUND);
        
        // 1. Item Name
        JLabel nameLabel = new JLabel("<html><b>" + item.getName() + "</b><br><small>" + item.getVariant() + "</small></html>");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        row.add(nameLabel, BorderLayout.WEST);

        // 2. Quantity, Price, and Remove link
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        controlPanel.setBackground(PANEL_BACKGROUND);
        
        // Quantity Spinner
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(item.getQuantity(), 1, 99, 1));
        quantitySpinner.setPreferredSize(new Dimension(50, 25));
        quantitySpinner.addChangeListener(e -> {
            int newQuantity = (int) quantitySpinner.getValue();
            item.setQuantity(newQuantity);
            refreshCart(); // Refresh the entire panel to update totals
        });
        controlPanel.add(quantitySpinner);

        // Total Price
        JLabel priceLabel = new JLabel(df.format(item.getTotalPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 13));
        controlPanel.add(priceLabel);
        
        // Remove Link
        JLabel removeLink = new JLabel("Remove");
        removeLink.setForeground(Color.RED);
        removeLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        removeLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cart.removeItem(item);
                refreshCart(); // Refresh the entire panel
            }
        });
        controlPanel.add(removeLink);

        row.add(controlPanel, BorderLayout.EAST);
        return row;
    }


    private JPanel createOrderSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1), 
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // --- 1. Title ---
        JLabel title = new JLabel("Order Summary");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(title, BorderLayout.NORTH);

        // --- 2. Center Content (Summary Details, Promos, Delivery, Notes) ---
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(PANEL_BACKGROUND);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // A. Summary Totals Section 
        centerPanel.add(createSummaryTotalsSection());
        centerPanel.add(Box.createVerticalStrut(15));
        
        // B. Promo/Discount Section
        centerPanel.add(createPromoInput());
        centerPanel.add(Box.createVerticalStrut(15));
        
        // C. Delivery Section
        centerPanel.add(createDeliveryDetails());
        centerPanel.add(Box.createVerticalStrut(15));
        
        // D. Notes Section
        centerPanel.add(createNotesSection());
        centerPanel.add(Box.createVerticalGlue()); 

        // Add center content to scroll pane
        JScrollPane scroll = new JScrollPane(centerPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scroll, BorderLayout.CENTER);

        // --- 3. Bottom Buttons ---
        panel.add(createSummaryButtons(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSummaryTotalsSection() {
        JPanel totalsPanel = new JPanel(new GridBagLayout());
        totalsPanel.setBackground(PANEL_BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 0, 3, 0);

        // 1. Subtotal Row
        subtotalValueLabel = addSummaryRow(totalsPanel, gbc, "Subtotal", "₹0.00", false, 0);
        // 2. GST Row
        gstValueLabel = addSummaryRow(totalsPanel, gbc, "GST (" + (int)(GST_RATE * 100) + "%)", "₹0.00", false, 1);
        // 3. Service Charge Row
        addSummaryRow(totalsPanel, gbc, "Service Charge (optional)", "₹0.00", false, 2);
        // 4. Discount Row
        addSummaryRow(totalsPanel, gbc, "Discount", "-₹0.00", false, 3);
        
        // 5. Separator
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 0, 8, 0);
        totalsPanel.add(separator, gbc);
        
        // 6. Tip Row 
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(3, 0, 3, 0);
        totalsPanel.add(createTipRow(), gbc);
        
        // 7. Payable (Total) Row
        payableValueLabel = addSummaryRow(totalsPanel, gbc, "Payable", "₹0.00", true, 6);
        return totalsPanel;
    }
    
    private JLabel addSummaryRow(JPanel parent, GridBagConstraints gbc, String labelText, String valueText, boolean isTotal, int row) {
        // Label Column (Left)
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.5;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", isTotal ? Font.BOLD : Font.PLAIN, 12));
        label.setForeground(isTotal ? Color.BLACK : Color.GRAY);
        parent.add(label, gbc);

        // Value Column (Right)
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 0.5;
        JLabel value = new JLabel(valueText, SwingConstants.RIGHT);
        value.setFont(new Font("Arial", isTotal ? Font.BOLD : Font.PLAIN, 12));
        value.setForeground(isTotal ? new Color(0, 102, 0) : Color.BLACK);
        parent.add(value, gbc);
        return value;
    }
    
    private JPanel createTipRow() {
        JPanel row = new JPanel(new BorderLayout(5, 0));
        row.setBackground(PANEL_BACKGROUND);
        JLabel label = new JLabel("Tip (optional)");
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setForeground(Color.GRAY);
        row.add(label, BorderLayout.WEST);

        String[] tipOptions = {"No tip", "₹10", "₹20", "Custom"};
        JComboBox<String> tipDropdown = new JComboBox<>(tipOptions);
        tipDropdown.setFont(new Font("Arial", Font.PLAIN, 12));
        JPanel tipRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        tipRightPanel.setBackground(PANEL_BACKGROUND);
        tipRightPanel.add(tipDropdown);
        row.add(tipRightPanel, BorderLayout.EAST);
        return row;
    }
    
    private JPanel createPromoInput() {
        JPanel promoPanel = new JPanel(new BorderLayout(5, 0));
        promoPanel.setBackground(PANEL_BACKGROUND);
        promoPanel.setBorder(BorderFactory.createTitledBorder("Promo Code"));
        
        JTextField promoField = new JTextField();
        promoPanel.add(promoField, BorderLayout.CENTER);
        
        JButton applyBtn = new JButton("Apply");
        applyBtn.setBackground(PRIMARY_BLUE);    // Solid blue background
        applyBtn.setForeground(Color.WHITE);     // White text
        applyBtn.setFocusPainted(false);         // Remove focus border
        applyBtn.setOpaque(true);                // Ensure solid fill
        applyBtn.setBorderPainted(false);        // Remove default border
        promoPanel.add(applyBtn, BorderLayout.EAST);
        
        return promoPanel;
    }

    private void showBillPopup() {
        if (cart.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty. Add items first!", "No Items", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder bill = new StringBuilder();
        bill.append("🏨 Hotel: ").append(cart.getHotelName()).append("\n\n"); // Make sure Cart has hotel name

        double total = 0;
        for (CartItem item : cart.getItems()) {
            bill.append("• ").append(item.getName())
                .append(" x").append(item.getQuantity())
                .append(" = ₹").append(String.format("%.2f", item.getTotalPrice()))
                .append("\n");
            total += item.getTotalPrice();
        }

        bill.append("\n---------------------------------\n");
        bill.append("Total: ₹").append(String.format("%.2f", total)).append("\n");
        bill.append("Status: PAID ✅");

        JTextArea textArea = new JTextArea(bill.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Bill Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createDeliveryDetails() {
        JPanel deliveryPanel = new JPanel(new BorderLayout(5, 5));
        deliveryPanel.setBackground(PANEL_BACKGROUND);
        deliveryPanel.setBorder(BorderFactory.createTitledBorder("Delivery Details"));
        
        JLabel addressLabel = new JLabel("Delivery to: 123 Main St, City, 10001");
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        deliveryPanel.add(addressLabel, BorderLayout.NORTH);
        
        JLabel timeLabel = new JLabel("Estimated Time: 30-45 min");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        deliveryPanel.add(timeLabel, BorderLayout.SOUTH);
        
        return deliveryPanel;
    }
    
    private JPanel createNotesSection() {
        JPanel notesPanel = new JPanel(new BorderLayout());
        notesPanel.setBackground(PANEL_BACKGROUND);
        notesPanel.setBorder(BorderFactory.createTitledBorder("Special Instructions"));
        
        JTextArea notesArea = new JTextArea(3, 20);
        notesArea.setFont(new Font("Arial", Font.PLAIN, 12));
        notesPanel.add(new JScrollPane(notesArea), BorderLayout.CENTER);
        
        return notesPanel;
    }

    private JPanel createSummaryButtons() {
        JPanel buttonPanel = new JPanel(new BorderLayout(10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.setBackground(PANEL_BACKGROUND);
        
        // a. Checkout Button (main action)
        JButton checkoutBtn = new JButton("Proceed to Checkout");
        checkoutBtn.setBackground(new Color(0, 128, 0)); // Solid dark green fill
        checkoutBtn.setForeground(Color.WHITE);          // White text
        checkoutBtn.setFocusPainted(false);             // Remove focus border
        checkoutBtn.setOpaque(true);                     // Important! Makes background solid
        checkoutBtn.setBorderPainted(false);            // Remove default border
        checkoutBtn.setFont(new Font("Arial", Font.BOLD, 14));
        checkoutBtn.setPreferredSize(new Dimension(200, 45)); // You can adjust width/height

        // Optional: hover effect to slightly change color
        checkoutBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                checkoutBtn.setBackground(new Color(0, 153, 0)); // lighter green on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                checkoutBtn.setBackground(new Color(0, 128, 0)); // original dark green
            }
        });

        
        buttonPanel.add(checkoutBtn, BorderLayout.NORTH);
        
        // b. Utility Buttons (Print/QR)
        JPanel utilityRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        utilityRow.setBackground(PANEL_BACKGROUND);
        
     // --- Print Bill Button (Red) ---
        JButton printBillBtn = new JButton("🖨️ Print Bill");
        printBillBtn.setBackground(new Color(220, 50, 50)); // Red fill
        printBillBtn.setForeground(Color.WHITE);            // White text
        printBillBtn.setFocusPainted(false);
        printBillBtn.setOpaque(true);                       // Solid fill
        printBillBtn.setBorderPainted(false);              // Remove default border
        printBillBtn.setFont(new Font("Arial", Font.BOLD, 12));
        printBillBtn.setPreferredSize(new Dimension(150, 35));
        printBillBtn.addActionListener(e -> showBillPopup());


        // --- Show QR Code Button (Blue) ---
        JButton showQrCodeBtn = new JButton("📱 Show QR Code");
        showQrCodeBtn.setBackground(new Color(0, 102, 204)); // Blue fill
        showQrCodeBtn.setForeground(Color.WHITE);           // White text
        showQrCodeBtn.setFocusPainted(false);
        showQrCodeBtn.setOpaque(true);                      // Solid fill
        showQrCodeBtn.setBorderPainted(false);             // Remove default border
        showQrCodeBtn.setFont(new Font("Arial", Font.BOLD, 12));
        showQrCodeBtn.setPreferredSize(new Dimension(150, 35));
        showQrCodeBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Displaying payment QR code...", "Action", JOptionPane.INFORMATION_MESSAGE);
        });

        
        utilityRow.add(printBillBtn);
        utilityRow.add(showQrCodeBtn);
        
        buttonPanel.add(utilityRow, BorderLayout.CENTER);
        
        return buttonPanel;
    }
}