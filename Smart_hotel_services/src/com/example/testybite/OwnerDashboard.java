package com.example.testybite;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class OwnerDashboard extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;

    public OwnerDashboard() {
        setTitle("Owner Dashboard - Hotel Management");
        setSize(1700, 820);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- Main Panel ---
        JPanel mainPanel = new JPanel(new BorderLayout());

        // --- Top Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setPreferredSize(new Dimension(900, 80));

        JLabel headerLabel = new JLabel("🏨 Owner Dashboard", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // --- Sidebar Menu ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(220, 220, 220));
        sidebar.setPreferredSize(new Dimension(220, 600));

        JButton btnViewBookings = new JButton("View Bookings");
        btnViewBookings.setContentAreaFilled(true);
        btnViewBookings.setOpaque(true);

        JButton btnManageRooms = new JButton("Manage Rooms");
        JButton btnViewOrders = new JButton("View Orders");
        JButton btnManageStaff = new JButton("Manage Staff");
        JButton btnLogout = new JButton("Logout");

        for (JButton btn : new JButton[]{btnViewBookings, btnManageRooms, btnViewOrders, btnManageStaff, btnLogout}) {
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setFocusPainted(false);

            btn.setBackground(new Color(0, 102, 204));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.PLAIN, 16));

            // 🔥 Important lines
            btn.setOpaque(true);  
            btn.setBorderPainted(false);  
            btn.setContentAreaFilled(true);

            sidebar.add(Box.createVerticalStrut(15));
            sidebar.add(btn);
        }


        mainPanel.add(sidebar, BorderLayout.WEST);

     // --- Center Content Area ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Welcome panel stays the same
        contentPanel.add(createWelcomePanel(), "welcome");

        // Embed updated panels
        contentPanel.add(new ViewBooking(), "bookings");   // JPanel version of ViewBooking
        contentPanel.add(new ManageRooms(), "rooms");      // JPanel version of ManageRooms

        // For ViewOrders, convert it to JPanel inline here (so it fits dashboard)
        // Minimal conversion
        contentPanel.add(new ViewOrders(), "orders");  

        // Staff panel remains unchanged
        contentPanel.add(createStaffPanel(), "staff");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // --- Sidebar button actions ---
        btnViewBookings.addActionListener(e -> switchPanel("bookings"));
        btnManageRooms.addActionListener(e -> switchPanel("rooms"));
        btnViewOrders.addActionListener(e -> switchPanel("orders"));
        btnManageStaff.addActionListener(e -> switchPanel("staff"));
        btnLogout.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });


        add(mainPanel);
        setVisible(true);
    }

    private void switchPanel(String name) {
        cardLayout.show(contentPanel, name);
    }

    // --- Welcome Panel ---
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Welcome, Owner!", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 28));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    // --- View Bookings ---
    private JPanel createBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("View Bookings", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"Booking ID", "Customer Name", "Type", "Date", "Time", "Status"};
        Object[][] data = {
                {"B001", "Rahul Sharma", "Room", "2025-10-18", "-", "Confirmed"},
                {"B002", "Sneha Patel", "Dining", "2025-10-18", "08:00 PM", "Pending"},
                {"B003", "Amit Kumar", "Room", "2025-10-20", "-", "Checked-in"},
                {"B004", "Priya Singh", "Dining", "2025-10-19", "07:30 PM", "Completed"}
        };

        JTable table = new JTable(new DefaultTableModel(data, columns));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    // --- Manage Rooms ---
    private JPanel createRoomsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Manage Rooms", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"Room No", "Type", "Price", "Availability"};
        Object[][] data = {
                {"101", "Single", "₹1500", "Available"},
                {"102", "Double", "₹2500", "Booked"},
                {"201", "Deluxe", "₹3500", "Available"},
                {"202", "Suite", "₹5000", "Under Maintenance"}
        };

        JTable table = new JTable(new DefaultTableModel(data, columns));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    // --- View Orders ---
    private JPanel createOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("View Orders", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"Order ID", "Customer", "Item", "Qty", "Total", "Status"};
        Object[][] data = {
                {"O001", "Rahul Sharma", "Paneer Butter Masala", "2", "₹500", "Preparing"},
                {"O002", "Sneha Patel", "Veg Biryani", "1", "₹250", "Served"},
                {"O003", "Amit Kumar", "Pizza Margherita", "3", "₹750", "Delivered"},
                {"O004", "Priya Singh", "Pasta Alfredo", "1", "₹300", "Pending"}
        };

        JTable table = new JTable(new DefaultTableModel(data, columns));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    // --- Manage Staff (with waiter assignment) ---
    private JPanel createStaffPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Assign Waiter to Tables", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setOpaque(true);
        title.setBackground(new Color(70, 130, 180));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(100, 40));
        panel.add(title, BorderLayout.NORTH);

        // Left: Waiter details
        String[] waiterCols = {"Waiter ID", "Name", "Contact", "Shift"};
        Object[][] waiterData = {
                {"W001", "Ravi Kumar", "9876543210", "Morning"},
                {"W002", "Asha Mehta", "9865321470", "Evening"},
                {"W003", "Karan Singh", "9812345678", "Full Day"}
        };
        JTable waiterTable = new JTable(new DefaultTableModel(waiterData, waiterCols));
        JScrollPane waiterScroll = new JScrollPane(waiterTable);
        waiterScroll.setBorder(BorderFactory.createTitledBorder("Waiter Details"));

        // Right: Current orders
        String[] orderCols = {"Table No", "Order ID", "Order Details", "Assigned Waiter"};
        Object[][] orderData = {
                {"T1", "O101", "2x Paneer Butter Masala", "Ravi Kumar"},
                {"T2", "O102", "1x Veg Biryani, 1x Roti", "Asha Mehta"},
                {"T3", "O103", "3x Tea, 1x Sandwich", "—"},
                {"T4", "O104", "2x Noodles, 2x Manchurian", "—"}
        };
        JTable orderTable = new JTable(new DefaultTableModel(orderData, orderCols));
        JScrollPane orderScroll = new JScrollPane(orderTable);
        orderScroll.setBorder(BorderFactory.createTitledBorder("Orders & Assigned Waiters"));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, waiterScroll, orderScroll);
        splitPane.setDividerLocation(400);
        panel.add(splitPane, BorderLayout.CENTER);

        // Bottom section for assigning waiter
        JPanel assignPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        assignPanel.setBorder(BorderFactory.createTitledBorder("Assign Waiter to Table"));
        assignPanel.setBackground(Color.WHITE);

        JComboBox<String> waiterCombo = new JComboBox<>(new String[]{"Ravi Kumar", "Asha Mehta", "Karan Singh"});
        JTextField tableField = new JTextField();

        assignPanel.add(new JLabel("Select Waiter:"));
        assignPanel.add(waiterCombo);
        assignPanel.add(new JLabel("Enter Table No:"));
        assignPanel.add(tableField);

        JButton assignBtn = new JButton("Assign Waiter");
        assignBtn.setBackground(new Color(70, 130, 180));
        assignBtn.setForeground(Color.WHITE);
        assignBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        assignBtn.addActionListener(e -> {
            String tableNo = tableField.getText().trim();
            String selectedWaiter = (String) waiterCombo.getSelectedItem();

            if (tableNo.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Enter a table number!");
                return;
            }

            DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
            boolean found = false;
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0).toString().equalsIgnoreCase(tableNo)) {
                    model.setValueAt(selectedWaiter, i, 3);
                    found = true;
                    break;
                }
            }

            if (found) {
                JOptionPane.showMessageDialog(panel, "Waiter assigned successfully!");
                tableField.setText("");
            } else {
                JOptionPane.showMessageDialog(panel, "Table not found!");
            }
        });

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(assignPanel, BorderLayout.CENTER);
        bottom.add(assignBtn, BorderLayout.SOUTH);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OwnerDashboard::new);
    }
}
