package com.example.testybite;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class ViewBooking extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private JComboBox<String> filterType, filterStatus;

    public ViewBooking() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Header ---
        JLabel title = new JLabel("📅 View All Bookings", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setOpaque(true);
        title.setBackground(new Color(0, 102, 204));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(100, 50));
        add(title, BorderLayout.NORTH);

        // --- Table Columns ---
        String[] columns = {"Booking ID", "Customer Name", "Type", "Date", "Time/Room No", "Status"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setRowHeight(25);
        table.setSelectionBackground(new Color(200, 230, 255));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Booking Details"));
        add(scrollPane, BorderLayout.CENTER);

        // --- Top Filter Panel ---
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(Color.WHITE);

        filterPanel.add(new JLabel("Search:"));
        searchField = new JTextField(15);
        filterPanel.add(searchField);

        filterPanel.add(new JLabel("Type:"));
        filterType = new JComboBox<>(new String[]{"All", "Room", "Dining"});
        filterPanel.add(filterType);

        filterPanel.add(new JLabel("Status:"));
        filterStatus = new JComboBox<>(new String[]{"All", "Pending", "Confirmed", "Checked-in", "Completed", "Cancelled"});
        filterPanel.add(filterStatus);

        JButton btnSearch = new JButton("🔍 Search");
        JButton btnRefresh = new JButton("↻ Refresh");
        styleButton(btnSearch);
        styleButton(btnRefresh);

        filterPanel.add(btnSearch);
        filterPanel.add(btnRefresh);

        add(filterPanel, BorderLayout.SOUTH);

        // --- Load Bookings from DB ---
        loadBookingsFromDB();

        // --- Button Functionality ---
        btnSearch.addActionListener(e -> applyFilters());
        btnRefresh.addActionListener(e -> {
            resetFilters();
            loadBookingsFromDB();
        });

        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                applyFilters(); // live search
            }
        });
    }

    // --- Load bookings from both tables ---
    private void loadBookingsFromDB() {
        model.setRowCount(0); // clear existing rows
        int bookingCounter = 1;

        // --- Load Room Bookings ---
        String sqlRoom = "SELECT id, guest_name, room_number, check_in, check_out FROM BookRoom";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlRoom)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String guest = rs.getString("guest_name");
                String roomNo = rs.getString("room_number");
                String checkIn = rs.getString("check_in");
                String checkOut = rs.getString("check_out");
                String status = "Confirmed"; // default, can update later if you add status column

                model.addRow(new Object[]{"R" + id, guest, "Room", checkIn, roomNo, status});
                bookingCounter++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load room bookings!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // --- Load Dining Bookings ---
        String sqlDining = "SELECT Customer_name, dateOfBooking, timing, noOfPersons FROM bookDinning";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlDining)) {

            while (rs.next()) {
                String customer = rs.getString("Customer_name");
                String date = rs.getString("dateOfBooking");
                String time = rs.getString("timing");
                int numPersons = rs.getInt("noOfPersons");
                String status = "Confirmed"; // default

                model.addRow(new Object[]{"D" + bookingCounter, customer, "Dining", date, time + " (" + numPersons + " pax)", status});
                bookingCounter++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load dining bookings!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Apply Search and Filter Logic ---
    private void applyFilters() {
        String keyword = searchField.getText().trim().toLowerCase();
        String selectedType = (String) filterType.getSelectedItem();
        String selectedStatus = (String) filterStatus.getSelectedItem();

        DefaultTableModel filteredModel = new DefaultTableModel(new String[]{
                "Booking ID", "Customer Name", "Type", "Date", "Time/Room No", "Status"
        }, 0);

        for (int i = 0; i < model.getRowCount(); i++) {
            Vector<?> row = model.getDataVector().elementAt(i);
            String bookingId = row.get(0).toString().toLowerCase();
            String customerName = row.get(1).toString().toLowerCase();
            String type = row.get(2).toString();
            String status = row.get(5).toString();

            boolean matchesSearch = bookingId.contains(keyword) || customerName.contains(keyword);
            boolean matchesType = selectedType.equals("All") || type.equals(selectedType);
            boolean matchesStatus = selectedStatus.equals("All") || status.equals(selectedStatus);

            if (matchesSearch && matchesType && matchesStatus) {
                filteredModel.addRow(row);
            }
        }

        table.setModel(filteredModel);
    }

    // --- Reset Filters ---
    private void resetFilters() {
        searchField.setText("");
        filterType.setSelectedIndex(0);
        filterStatus.setSelectedIndex(0);
        table.setModel(model);
    }

    // --- Helper Method: Style Buttons ---
    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(110, 32));
    }

    // --- Standalone Testing ---
    public static void main(String[] args) {
        JFrame frame = new JFrame("View Bookings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.add(new ViewBooking());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
