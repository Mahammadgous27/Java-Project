package com.example.testybite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class Login extends JFrame {

    private JComboBox<String> emailBox; // Email input with autofill

    public Login() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Container contentPane = this.getContentPane();
        final Color PRIMARY_BLUE = new Color(0, 150, 199);
        contentPane.setBackground(PRIMARY_BLUE);

        setTitle("TastyBites - Login");
        setSize(400, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel title = new JLabel("TastyBites - Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(50, 30, 300, 30);
        add(title);

        // ✅ Email input as JComboBox for autofill
        emailBox = new JComboBox<>();
        emailBox.setEditable(true);
        emailBox.setBounds(50, 80, 300, 50);
        emailBox.setBorder(BorderFactory.createTitledBorder("Email Address"));
        add(emailBox);

        // Load emails from database
        loadEmailsFromDB();

        JPasswordField password = new JPasswordField();
        password.setBorder(BorderFactory.createTitledBorder("Password"));
        password.setBounds(50, 140, 300, 50);
        add(password);

        JButton loginBtnCustomer = new JButton("Login as Customer");
        loginBtnCustomer.setBounds(50, 220, 300, 40);
        add(loginBtnCustomer);

        JButton loginBtnOwner = new JButton("Login as Owner");
        loginBtnOwner.setBounds(50, 270, 300, 40); 
        add(loginBtnOwner);

        // Owner Login
        loginBtnOwner.addActionListener(e -> {
            String userEmail = ((String) emailBox.getSelectedItem()).trim();
            String userPass = new String(password.getPassword()).trim();

            if (DatabaseConnector.checkLogin(userEmail, userPass, "owner")) {
                JOptionPane.showMessageDialog(Login.this, "✅ Owner Login Successful!");
                dispose();
                new OwnerDashboard().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(Login.this, "❌ Invalid Owner Credentials!");
            }
        });


        // Customer Login
        loginBtnCustomer.addActionListener(e -> {
            String userEmail = ((String) emailBox.getSelectedItem()).trim();
            String userPass = new String(password.getPassword()).trim();

            if (DatabaseConnector.checkLogin(userEmail, userPass, "customer")) {
                JOptionPane.showMessageDialog(Login.this, "✅ Login Successful!");
                dispose();
                new HomePage().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(Login.this, "❌ Invalid Credentials!");
            }
        });

        JButton goToSignUpBtn = new JButton("Don't have an account? Sign up here");
        goToSignUpBtn.setBounds(70, 330, 260, 30);
        goToSignUpBtn.setBorderPainted(false);
        goToSignUpBtn.setContentAreaFilled(false);
        goToSignUpBtn.setForeground(Color.BLACK);
        goToSignUpBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(goToSignUpBtn);

        goToSignUpBtn.addActionListener(e -> {
            dispose();
            new SignUp().setVisible(true);
        });

        setVisible(true);
    }

    // ✅ Load existing emails from database into JComboBox
    private void loadEmailsFromDB() {
        try (Connection conn = DatabaseConnector.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.");
                return;
            }

            String sql = "SELECT email FROM Customer"; // Replace with Owner table if needed
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                emailBox.addItem(rs.getString("email"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading emails: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}
