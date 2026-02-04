package com.example.testybite;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class ViewOrders extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private JComboBox<String> filterStatus;

    public ViewOrders() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Header ---
        JLabel title = new JLabel("🍽 View All Orders", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setOpaque(true);
        title.setBackground(new Color(0, 102, 204));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(100, 50));
        add(title, BorderLayout.NORTH);

        // --- Table Columns (empty data - will load from DB)
        String[] columns = {"Order ID", "Customer", "Item", "Quantity", "Total Price", "Prep Time (mins)", "Status"};
        Object[][] data = {}; // no dummy data

        model = new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setRowHeight(25);
        table.setSelectionBackground(new Color(200, 230, 255));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Order Details"));
        add(scrollPane, BorderLayout.CENTER);

        // --- Filter Panel ---
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(Color.WHITE);

        filterPanel.add(new JLabel("Search:"));
        searchField = new JTextField(15);
        filterPanel.add(searchField);

        filterPanel.add(new JLabel("Status:"));
        filterStatus = new JComboBox<>(new String[]{"All", "Pending", "Preparing", "Served", "Delivered"});
        filterPanel.add(filterStatus);

        JButton btnSearch = new JButton("🔍 Search");
        JButton btnRefresh = new JButton("↻ Refresh");
        JButton btnUpdateStatus = new JButton("Update Status");

        styleButton(btnSearch);
        styleButton(btnRefresh);
        styleButton(btnUpdateStatus);

        filterPanel.add(btnSearch);
        filterPanel.add(btnRefresh);
        filterPanel.add(btnUpdateStatus);

        add(filterPanel, BorderLayout.SOUTH);

        // --- Button Actions ---
        btnSearch.addActionListener(e -> applyFilters());
        btnRefresh.addActionListener(e -> {
            resetFilters();
            loadOrdersFromDatabase();
        });

        btnUpdateStatus.addActionListener(e -> updateOrderStatus());

        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                applyFilters();
            }
        });

        // --- Load data from the database ---
        loadOrdersFromDatabase();
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(130, 32));
    }

    // ------------------------------------------------------------
    //        LOAD ORDER LIST FROM DATABASE
    // ------------------------------------------------------------
    private void loadOrdersFromDatabase() {
        model.setRowCount(0); // clear old rows

        try {
            DatabaseConnector db = new DatabaseConnector();
            Connection conn = db.getConnection();

            String sql = "SELECT order_id, reservation_id, item_name, quantity, " +
                    "(item_price * quantity) AS total_price, " +
                    "TIMESTAMPDIFF(MINUTE, order_time, NOW()) AS prep_time, status " +
                    "FROM Orders";

            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("order_id"),
                        rs.getInt("reservation_id"),  // Will show customer ID for now
                        rs.getString("item_name"),
                        rs.getInt("quantity"),
                        "₹" + rs.getDouble("total_price"),
                        rs.getInt("prep_time"),
                        rs.getString("status")
                };
                model.addRow(row);
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Failed to load orders: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyFilters() {
        String keyword = searchField.getText().trim().toLowerCase();
        String selectedStatus = (String) filterStatus.getSelectedItem();

        DefaultTableModel filteredModel = new DefaultTableModel(
                new String[]{"Order ID", "Customer", "Item", "Quantity", "Total Price", "Prep Time (mins)", "Status"}, 0);

        for (int i = 0; i < model.getRowCount(); i++) {
            Vector<?> row = model.getDataVector().elementAt(i);

            String orderId = row.get(0).toString().toLowerCase();
            String customer = row.get(1).toString().toLowerCase();
            String status = row.get(6).toString();

            boolean matchSearch = orderId.contains(keyword) || customer.contains(keyword);
            boolean matchStatus = selectedStatus.equals("All") || status.equals(selectedStatus);

            if (matchSearch && matchStatus) {
                filteredModel.addRow(row);
            }
        }

        table.setModel(filteredModel);
    }

    private void resetFilters() {
        searchField.setText("");
        filterStatus.setSelectedIndex(0);
        table.setModel(model);
    }

    private void updateOrderStatus() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select an order to update!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] statuses = {"Pending", "Preparing", "Served", "Delivered"};

        String newStatus = (String) JOptionPane.showInputDialog(
                this,
                "Select new status:",
                "Update Order Status",
                JOptionPane.PLAIN_MESSAGE,
                null,
                statuses,
                statuses[0]
        );

        if (newStatus != null) {
            table.setValueAt(newStatus, selectedRow, 6);
            JOptionPane.showMessageDialog(this,
                    "Status updated (UI only).\nDatabase update can be added if needed!");
        }
    }

    // Standalone testing
    public static void main(String[] args) {
        JFrame frame = new JFrame("View Orders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.add(new ViewOrders());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
