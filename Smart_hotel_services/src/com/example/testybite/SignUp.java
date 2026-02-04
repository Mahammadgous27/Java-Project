package com.example.testybite;

import javax.swing.*;
import java.awt.*;

public class SignUp extends JFrame {

    public SignUp() {
        Container contentPane = this.getContentPane();
        final Color PRIMARY_BLUE = new Color(0, 150, 199);
        contentPane.setBackground(PRIMARY_BLUE);

        setTitle("TastyBites - Sign Up");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel title = new JLabel("TastyBites - Create Account", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(50, 20, 300, 30);
        add(title);

        JTextField name = new JTextField();
        name.setBorder(BorderFactory.createTitledBorder("Full Name"));
        name.setBounds(50, 70, 300, 50);
        add(name);

        JTextField email = new JTextField();
        email.setBorder(BorderFactory.createTitledBorder("Email Address"));
        email.setBounds(50, 130, 300, 50);
        add(email);

        JTextField phone = new JTextField();
        phone.setBorder(BorderFactory.createTitledBorder("Phone Number"));
        phone.setBounds(50, 190, 300, 50);
        add(phone);

        JPasswordField password = new JPasswordField();
        password.setBorder(BorderFactory.createTitledBorder("Password"));
        password.setBounds(50, 250, 300, 50);
        add(password);

        JPasswordField confirmPassword = new JPasswordField();
        confirmPassword.setBorder(BorderFactory.createTitledBorder("Confirm Password"));
        confirmPassword.setBounds(50, 310, 300, 50);
        add(confirmPassword);

        // Role selection: Customer or Owner
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Customer", "Owner"});
        roleBox.setBounds(50, 370, 300, 50);
        roleBox.setBorder(BorderFactory.createTitledBorder("Select Role"));
        add(roleBox);

        JButton createAccountBtn = new JButton("Create Account");
        createAccountBtn.setBounds(50, 440, 300, 40);
        add(createAccountBtn);

        createAccountBtn.addActionListener(e -> {
            String fullName = name.getText().trim();
            String emailId = email.getText().trim();
            String phoneNo = phone.getText().trim();
            String pass = new String(password.getPassword()).trim();
            String confirmPass = new String(confirmPassword.getPassword()).trim();
            String role = (String) roleBox.getSelectedItem();

            if (fullName.isEmpty() || emailId.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠ Please fill all fields.");
                return;
            }
            if (!pass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this, "❌ Passwords do not match!");
                return;
            }

            boolean inserted = false;
            if ("Owner".equals(role)) {
                inserted = DatabaseConnector.insertOwner(fullName, emailId, phoneNo, pass);
            } else {
                inserted = DatabaseConnector.insertCustomer(fullName, emailId, phoneNo, pass);
            }

            if (inserted) {
                JOptionPane.showMessageDialog(this, "✅ Account Created Successfully!");
                dispose();
                new Login().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Account Creation Failed (Email might exist).");
            }
        });

        JButton goToLoginBtn = new JButton("Already have an account? Login");
        goToLoginBtn.setBounds(90, 500, 220, 30);
        goToLoginBtn.setBorderPainted(false);
        goToLoginBtn.setContentAreaFilled(false);
        goToLoginBtn.setForeground(Color.BLUE);
        goToLoginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(goToLoginBtn);

        goToLoginBtn.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignUp::new);
    }
}
