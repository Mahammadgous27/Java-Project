package com.example.testybite;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ManageRooms extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtRoomNo, txtType, txtPrice;
    private JComboBox<String> cmbAvailability;

    public ManageRooms() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Header ---
        JLabel title = new JLabel("🏨 Manage Rooms", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setOpaque(true);
        title.setBackground(new Color(0, 102, 204));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(100, 50));
        add(title, BorderLayout.NORTH);

        // --- Table Section ---
        String[] columns = {"Room No", "Type", "Price per Night", "Availability"};
        model = new DefaultTableModel(columns, 0); // start empty
        table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setSelectionBackground(new Color(200, 230, 255));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Room Details"));
        add(scrollPane, BorderLayout.CENTER);

        // --- Form Section (Bottom) ---
        JPanel formPanel = new JPanel(new GridLayout(2, 5, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Room Information"));
        formPanel.setBackground(Color.WHITE);

        txtRoomNo = new JTextField();
        txtType = new JTextField();
        txtPrice = new JTextField();
        cmbAvailability = new JComboBox<>(new String[]{"Available", "Booked", "Under Maintenance"});

        formPanel.add(new JLabel("Room No:"));
        formPanel.add(txtRoomNo);
        formPanel.add(new JLabel("Type:"));
        formPanel.add(txtType);
        formPanel.add(new JLabel("Price per Night:"));
        formPanel.add(txtPrice);
        formPanel.add(new JLabel("Availability:"));
        formPanel.add(cmbAvailability);

        // --- Button Section ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnAdd = new JButton("Add Room");
        JButton btnEdit = new JButton("Edit Selected");
        JButton btnDelete = new JButton("Delete");
        JButton btnUpdateStatus = new JButton("Update Status");

        for (JButton btn : new JButton[]{btnAdd, btnEdit, btnDelete, btnUpdateStatus}) {
            btn.setBackground(new Color(0, 102, 204));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(150, 35));
            buttonPanel.add(btn);
        }

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- Load Rooms from Database ---
        loadRoomsFromDB();

        // --- Button Functionalities ---
        btnAdd.addActionListener(e -> addRoom());
        btnEdit.addActionListener(e -> editRoom());
        btnDelete.addActionListener(e -> deleteRoom());
        btnUpdateStatus.addActionListener(e -> updateRoomStatus());

        table.getSelectionModel().addListSelectionListener(e -> fillFieldsFromSelectedRow());
    }

    private void loadRoomsFromDB() {
        model.setRowCount(0); // clear existing rows
        String sql = "SELECT guest_name, room_number, check_in, check_out FROM BookRoom";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String guest = rs.getString("guest_name");
                String roomNo = rs.getString("room_number");
                String checkIn = rs.getString("check_in");
                String checkOut = rs.getString("check_out");

                model.addRow(new Object[]{roomNo, guest, checkIn, checkOut});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load booked rooms from database!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void addRoom() {
        String roomNo = txtRoomNo.getText().trim();
        String type = txtType.getText().trim();
        String price = txtPrice.getText().trim();
        String avail = (String) cmbAvailability.getSelectedItem();

        if (roomNo.isEmpty() || type.isEmpty() || price.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Insert into DB
        String sql = "INSERT INTO rooms (room_no, type, price_per_night, availability) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, roomNo);
            pst.setString(2, type);
            pst.setInt(3, Integer.parseInt(price));
            pst.setString(4, avail);

            if (pst.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Room added successfully!");
                loadRoomsFromDB();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add room!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editRoom() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a room to edit!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String roomNo = txtRoomNo.getText().trim();
        String type = txtType.getText().trim();
        String price = txtPrice.getText().trim();
        String avail = (String) cmbAvailability.getSelectedItem();

        String sql = "UPDATE rooms SET type=?, price_per_night=?, availability=? WHERE room_no=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, type);
            pst.setInt(2, Integer.parseInt(price));
            pst.setString(3, avail);
            pst.setString(4, roomNo);

            if (pst.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Room updated successfully!");
                loadRoomsFromDB();
                clearFields();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRoom() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a room to delete!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String roomNo = model.getValueAt(selectedRow, 0).toString();
        String sql = "DELETE FROM rooms WHERE room_no=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, roomNo);
            if (pst.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Room deleted successfully!");
                loadRoomsFromDB();
                clearFields();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRoomStatus() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a room to update status!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String roomNo = model.getValueAt(selectedRow, 0).toString();
        String[] statuses = {"Available", "Booked", "Under Maintenance"};
        String newStatus = (String) JOptionPane.showInputDialog(this, "Select new status:", "Update Room Status",
                JOptionPane.PLAIN_MESSAGE, null, statuses, statuses[0]);
        if (newStatus != null) {
            String sql = "UPDATE rooms SET availability=? WHERE room_no=?";
            try (Connection conn = DatabaseConnector.getConnection();
                 PreparedStatement pst = conn.prepareStatement(sql)) {

                pst.setString(1, newStatus);
                pst.setString(2, roomNo);

                if (pst.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(this, "Room status updated successfully!");
                    loadRoomsFromDB();
                    clearFields();
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void fillFieldsFromSelectedRow() {
        int selected = table.getSelectedRow();
        if (selected >= 0) {
            txtRoomNo.setText(model.getValueAt(selected, 0).toString());
            txtType.setText(model.getValueAt(selected, 1).toString());
            txtPrice.setText(model.getValueAt(selected, 2).toString().replace("₹", ""));
            cmbAvailability.setSelectedItem(model.getValueAt(selected, 3).toString());
        }
    }

    private void clearFields() {
        txtRoomNo.setText("");
        txtType.setText("");
        txtPrice.setText("");
        cmbAvailability.setSelectedIndex(0);
    }

    // --- Standalone Testing ---
    public static void main(String[] args) {
        JFrame frame = new JFrame("Manage Rooms");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new ManageRooms());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
