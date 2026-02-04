package com.example.testybite;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManageStaff extends JPanel {

    private JTable waiterTable;
    private JTable orderTable;
    private JComboBox<String> waiterCombo;
    private JTextField tableField;

    public ManageStaff() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // --- HEADER ---
        JLabel title = new JLabel("Assign Waiter to Tables", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setOpaque(true);
        title.setBackground(new Color(70, 130, 180));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(100, 40));
        add(title, BorderLayout.NORTH);

        // --- LEFT SIDE: WAITER DETAILS ---
        String[] waiterCols = {"Waiter ID", "Name", "Contact", "Shift"};
        Object[][] waiterData = {
                {"W001", "Ravi Kumar", "9876543210", "Morning"},
                {"W002", "Asha Mehta", "9865321470", "Evening"},
                {"W003", "Karan Singh", "9812345678", "Full Day"}
        };
        waiterTable = new JTable(new DefaultTableModel(waiterData, waiterCols));
        JScrollPane waiterScroll = new JScrollPane(waiterTable);
        waiterScroll.setBorder(BorderFactory.createTitledBorder("Waiter Details"));

        // --- RIGHT SIDE: CURRENT ORDERS / TABLE ASSIGNMENTS ---
        String[] orderCols = {"Table No", "Order ID", "Order Details", "Assigned Waiter"};
        Object[][] orderData = {
                {"T1", "O101", "2x Paneer Butter Masala", "Ravi Kumar"},
                {"T2", "O102", "1x Veg Biryani, 1x Roti", "Asha Mehta"},
                {"T3", "O103", "3x Tea, 1x Sandwich", "—"},
                {"T4", "O104", "2x Noodles, 2x Manchurian", "—"}
        };
        orderTable = new JTable(new DefaultTableModel(orderData, orderCols));
        JScrollPane orderScroll = new JScrollPane(orderTable);
        orderScroll.setBorder(BorderFactory.createTitledBorder("Current Orders & Assigned Waiters"));

        // Split layout for waiters and orders
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, waiterScroll, orderScroll);
        splitPane.setDividerLocation(250);
        add(splitPane, BorderLayout.CENTER);

        // --- BOTTOM PANEL FOR ASSIGNING WAITER ---
        JPanel assignPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        assignPanel.setBorder(BorderFactory.createTitledBorder("Assign Waiter to Table"));
        assignPanel.setBackground(Color.WHITE);

        waiterCombo = new JComboBox<>(new String[]{"Ravi Kumar", "Asha Mehta", "Karan Singh"});
        tableField = new JTextField();

        assignPanel.add(new JLabel("Select Waiter:"));
        assignPanel.add(waiterCombo);
        assignPanel.add(new JLabel("Enter Table No:"));
        assignPanel.add(tableField);

        JButton assignBtn = new JButton("Assign Waiter");
        assignBtn.setBackground(new Color(70, 130, 180));
        assignBtn.setForeground(Color.WHITE);
        assignBtn.setFocusPainted(false);
        assignBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Action to assign waiter to order table
        assignBtn.addActionListener(e -> assignWaiter());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(assignPanel, BorderLayout.CENTER);
        bottomPanel.add(assignBtn, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void assignWaiter() {
        String tableNo = tableField.getText().trim();
        String selectedWaiter = (String) waiterCombo.getSelectedItem();

        if (tableNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a table number!");
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

        if (!found) {
            JOptionPane.showMessageDialog(this, "Table not found!");
        } else {
            JOptionPane.showMessageDialog(this, "Waiter assigned successfully!");
            tableField.setText("");
        }
    }

    // For testing standalone
    public static void main(String[] args) {
        JFrame f = new JFrame("Owner Dashboard - Manage Staff");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(900, 500);
        f.setLayout(new BorderLayout());
        f.add(new ManageStaff(), BorderLayout.CENTER);
        f.setVisible(true);
    }
}
