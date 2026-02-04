package com.example.testybite;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import org.jdatepicker.impl.UtilDateModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel; // optional
import org.jdatepicker.impl.UtilDateModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;

import java.util.Properties;
import java.util.Date;


import org.jdatepicker.impl.*;

public class BookRoom extends JFrame {

    private DefaultTableModel tableModel;

    // Custom Colors
    private final Color BUTTON_BLUE = new Color(0, 102, 204);
    private final Color BUTTON_GREEN = new Color(76, 175, 80);

    public BookRoom(String currentHotelName) {
        setTitle("Hotel Management - Book Room");
        setSize(600, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BUTTON_GREEN);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel mainTitle = new JLabel("Hotel Management System", SwingConstants.CENTER);
        mainTitle.setFont(new Font("Arial", Font.BOLD, 24));
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTitle = new JLabel("Book a Room for: " + currentHotelName, SwingConstants.CENTER);
        subTitle.setFont(new Font("Arial", Font.BOLD, 16));
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Input Fields
        JTextField guestName = createStyledTextField("Guest Name");
        JTextField roomNumber = createStyledTextField("Room Number");

        UtilDateModel checkInModel = new UtilDateModel();
        Properties checkInProps = new Properties();
        checkInProps.put("text.today", "Today");
        checkInProps.put("text.month", "Month");
        checkInProps.put("text.year", "Year");
        JDatePanelImpl checkInPanel = new JDatePanelImpl(checkInModel, checkInProps);
        JDatePickerImpl checkInPicker = new JDatePickerImpl(checkInPanel, null);
        checkInPicker.setPreferredSize(new Dimension(300, 50));

        // --- Check-out Date Picker (optional) ---
        UtilDateModel checkOutModel = new UtilDateModel();
        Properties checkOutProps = new Properties();
        checkOutProps.put("text.today", "Today");
        checkOutProps.put("text.month", "Month");
        checkOutProps.put("text.year", "Year");
        JDatePanelImpl checkOutPanel = new JDatePanelImpl(checkOutModel, checkOutProps);
        JDatePickerImpl checkOutPicker = new JDatePickerImpl(checkOutPanel, null);
        checkOutPicker.setPreferredSize(new Dimension(300, 50));

        // Button
        JButton bookBtn = new JButton("Book Room");
        styleButton(bookBtn, BUTTON_BLUE);
        bookBtn.setBackground(new Color(30, 144, 255));
        bookBtn.setForeground(Color.WHITE);
        bookBtn.setBorderPainted(false);
        bookBtn.setFocusPainted(false);
        bookBtn.setOpaque(true);

        // Table
        String[] columnNames = {"Guest Name", "Room No.", "Check-In", "Check-Out"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable bookingTable = new JTable(tableModel);
        bookingTable.setFillsViewportHeight(true);
        bookingTable.setRowHeight(30);
        JScrollPane tableScrollPane = new JScrollPane(bookingTable);
        tableScrollPane.setPreferredSize(new Dimension(500, 200));

        // ✅ Database Insertion Logic
        bookBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (guestName.getText().trim().isEmpty() || roomNumber.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(BookRoom.this,
                            "Please fill Guest Name and Room Number.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Date inDateValue = (Date) checkInPicker.getModel().getValue();
                Date outDateValue = (Date) checkOutPicker.getModel().getValue();

                if (inDateValue == null) {
                    JOptionPane.showMessageDialog(BookRoom.this,
                            "Please select a check-in date.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String guest = guestName.getText().trim();
                String room = roomNumber.getText().trim();
                String inDate = new SimpleDateFormat("dd/MM/yyyy").format(inDateValue);
                String outDate = (outDateValue != null) ? new SimpleDateFormat("dd/MM/yyyy").format(outDateValue) : "";

                // Insert into DB
                try (Connection conn = DatabaseConnector.getConnection()) {
                    if (conn == null) {
                        JOptionPane.showMessageDialog(BookRoom.this,
                                "Database connection failed!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String sql = "INSERT INTO `BookRoom` (`guest_name`, `room_number`, `check_in`) VALUES (?, ?, ?)";
                    try (PreparedStatement pst = conn.prepareStatement(sql)) {
                        pst.setString(1, guest);
                        pst.setString(2, room);
                        pst.setString(3, inDate);

                        int rows = pst.executeUpdate();
                        if (rows > 0) {
                            tableModel.addRow(new Object[]{guest, room, inDate, outDate});
                            JOptionPane.showMessageDialog(BookRoom.this,
                                    "✅ Room Booked Successfully!",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);

                            guestName.setText("");
                            roomNumber.setText("");
                            checkInPicker.getModel().setValue(null);
                            checkOutPicker.getModel().setValue(null);

                            bookBtn.setText("Room Booked!");
                            bookBtn.setBackground(BUTTON_GREEN);
                            bookBtn.setEnabled(false);
                        } else {
                            JOptionPane.showMessageDialog(BookRoom.this,
                                    "Booking failed. Try again!",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(BookRoom.this,
                            "Database Error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Layout assembly
        mainPanel.add(mainTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(subTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        mainPanel.add(guestName);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(roomNumber);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(checkInPicker);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(checkOutPicker);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        mainPanel.add(bookBtn);
        bookBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        JLabel currentBookingsLabel = new JLabel("Current Bookings:");
        currentBookingsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(currentBookingsLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(tableScrollPane);

        add(new JScrollPane(mainPanel));
        setVisible(true);
    }

    // Helper method
    private JTextField createStyledTextField(String title) {
        JTextField textField = new JTextField();
        textField.setBorder(BorderFactory.createTitledBorder(title));
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        return textField;
    }

    private void styleButton(JButton button, Color background) {
        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
