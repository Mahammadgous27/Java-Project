package com.example.testybite;

import javax.swing.*;
import org.jdatepicker.impl.*;
import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

public class BookDining extends JFrame {

    private JTextField customerNameField;
    private JDatePickerImpl datePicker;
    private JTextField timeField;
    private JSpinner numPersonsSpinner;
    private JButton confirmBtn;

    private String hotelName;
    private DiningBookingCallback callback; // callback to notify HotelDetailsPanel

    // Callback interface
    public interface DiningBookingCallback {
        void onBookingConfirmed(DiningBookingItem bookingItem);
    }

    public BookDining(String hotelName, DiningBookingCallback callback) {
        this.hotelName = hotelName;
        this.callback = callback;

        setTitle("Book Dining - " + hotelName);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));

        // --- Customer Name ---
        add(new JLabel("Customer Name:"));
        customerNameField = new JTextField();
        add(customerNameField);

        // --- Date Picker ---
        add(new JLabel("Date:"));
        UtilDateModel model = new UtilDateModel();
        model.setSelected(true); // default today
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        add(datePicker);

        // --- Time ---
        add(new JLabel("Time (HH:mm):"));
        timeField = new JTextField();
        add(timeField);

        // --- Number of Persons ---
        add(new JLabel("Number of Persons:"));
        numPersonsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        add(numPersonsSpinner);

        // --- Confirm Button ---
        add(new JLabel()); // empty cell
        confirmBtn = new JButton("Confirm Booking");
        add(confirmBtn);

        confirmBtn.addActionListener(e -> confirmBooking());

        setVisible(true);
    }

    private void confirmBooking() {
        String customerName = customerNameField.getText().trim();
        Object dateObj = datePicker.getModel().getValue();
        String time = timeField.getText().trim();
        int numPersons = (Integer) numPersonsSpinner.getValue();

        if (customerName.isEmpty() || dateObj == null || time.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Convert java.util.Date to java.sql.Date
        java.util.Date utilDate;
        if (dateObj instanceof Calendar) {
            utilDate = ((Calendar) dateObj).getTime();
        } else {
            utilDate = (java.util.Date) dateObj;
        }
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(utilDate);

        // Create booking item object
        DiningBookingItem bookingItem = new DiningBookingItem(
                customerName, hotelName, dateStr, time, numPersons
        );

        // Insert into database
        boolean success = DatabaseConnector.bookDining(
                customerName, sqlDate, time, numPersons
        );

        if (success) {
            JOptionPane.showMessageDialog(this, "✅ Dining booked successfully!");
            dispose(); // Close popup
            if (callback != null) {
                callback.onBookingConfirmed(bookingItem);
            }
        } else {
            JOptionPane.showMessageDialog(this, "❌ Failed to book dining. Try again!");
        }
    }

    // --- Formatter for JDatePicker ---
    public static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final String datePattern = "yyyy-MM-dd";
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) {
            try {
                return dateFormatter.parse(text);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public String valueToString(Object value) {
            if (value != null) {
                if (value instanceof Calendar) {
                    return dateFormatter.format(((Calendar) value).getTime());
                } else if (value instanceof java.util.Date) {
                    return dateFormatter.format((java.util.Date) value);
                }
            }
            return "";
        }
    }
}
