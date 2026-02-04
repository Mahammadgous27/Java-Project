package com.example.testybite;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SettingsPanel extends JPanel {

    public SettingsPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 242, 245)); // Light gray background

        // Main content card
        JPanel mainContentCard = new JPanel();
        mainContentCard.setLayout(new BoxLayout(mainContentCard, BoxLayout.Y_AXIS));
        mainContentCard.setBackground(Color.WHITE);
        mainContentCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(30, 40, 30, 40)
        ));

        // Top Profile Section
        JPanel profilePanel = createProfilePanel();
        profilePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentCard.add(profilePanel);
        mainContentCard.add(Box.createRigidArea(new Dimension(0, 30)));

        // Personal Information
        mainContentCard.add(createPersonalInfoPanel());
        mainContentCard.add(Box.createRigidArea(new Dimension(0, 40)));

        // Notifications Section
        mainContentCard.add(createNotificationsPanel());
        mainContentCard.add(Box.createRigidArea(new Dimension(0, 50)));

        // Save Button
        mainContentCard.add(createSaveButtonPanel());

        JScrollPane scrollPane = new JScrollPane(mainContentCard);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    // === PROFILE PANEL ===
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 0));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(700, 80));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel iconLabel = new JLabel(getProfileIcon(60));
        panel.add(iconLabel, BorderLayout.WEST);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel("User");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(nameLabel);

        JLabel emailLabel = new JLabel("user27@gmail.com");
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        emailLabel.setForeground(Color.GRAY);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(emailLabel);

        panel.add(infoPanel, BorderLayout.CENTER);

        JButton updateButton = createStyledButton(" Update Profile", new Color(0, 123, 255), Color.WHITE, 12);
        updateButton.setPreferredSize(new Dimension(150, 40));

        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonWrapper.setBackground(Color.WHITE);
        buttonWrapper.add(updateButton);
        panel.add(buttonWrapper, BorderLayout.EAST);

        return panel;
    }

    private ImageIcon getProfileIcon(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.WHITE);
        g2.fillOval(0, 0, size, size);

        g2.setColor(new Color(0, 123, 255));
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(1, 1, size - 2, size - 2);

        g2.setColor(new Color(180, 180, 180));
        int center = size / 2;
        int headRadius = (int) (size / 5.5);
        g2.fillOval(center - headRadius, size / 4 + 2, headRadius * 2, headRadius * 2);

        int bodyWidth = (int) (size * 0.55);
        int bodyHeight = (int) (size * 0.3);
        g2.fillRoundRect(center - bodyWidth / 2, (int)(size / 2.2) + headRadius, bodyWidth, bodyHeight, 10, 10);

        g2.dispose();
        return new ImageIcon(image);
    }

    // === PERSONAL INFO PANEL ===
    private JPanel createPersonalInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Personal Information");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        gbc.gridy = row++;
        addFormField(formPanel, gbc, "Full Name:", "USer", false);
        gbc.gridy = row++;
        addFormField(formPanel, gbc, "Email Address:", "user27@gmail.com", false);
        gbc.gridy = row++;
        addFormField(formPanel, gbc, "Phone Number:", "+91 9876543210", false);

        panel.add(formPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        JButton changePassword = createLinkButton("Change Password");
        changePassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(changePassword);

        return panel;
    }

    private void addFormField(JPanel formPanel, GridBagConstraints gbc, String labelText, String defaultValue, boolean isPasswordField) {
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(Color.DARK_GRAY);
        formPanel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField textField = isPasswordField ? new JPasswordField(defaultValue) : new JTextField(defaultValue);
        textField.setPreferredSize(new Dimension(280, 35));
        textField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        textField.setBackground(new Color(248, 248, 248));
        formPanel.add(textField, gbc);
    }

    private JButton createLinkButton(String text) {
        JButton button = new JButton("<html><u>" + text + "</u></html>");
        button.setForeground(new Color(0, 102, 204));
        button.setFont(new Font("SansSerif", Font.BOLD, 14));

        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // 🔥 These stop movement on click
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setRolloverEnabled(false);

        return button;
    }


    // === NOTIFICATIONS PANEL ===
    private JPanel createNotificationsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Notifications");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel togglesPanel = new JPanel(new GridBagLayout());
        togglesPanel.setBackground(Color.WHITE);
        togglesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridy = 0; addNotificationToggle(togglesPanel, "Email Notifications", true, gbc);
        gbc.gridy = 1; addNotificationToggle(togglesPanel, "SMS Notifications", true, gbc);
        gbc.gridy = 2; addNotificationToggle(togglesPanel, "App Alerts", false, gbc);

        panel.add(togglesPanel);
        return panel;
    }

    private void addNotificationToggle(JPanel container, String labelText, boolean initialValue, GridBagConstraints gbc) {
        JPanel rowPanel = new JPanel(new BorderLayout(20, 0));
        rowPanel.setBackground(Color.WHITE);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(Color.DARK_GRAY);
        rowPanel.add(label, BorderLayout.WEST);

        // Correct toggle using JCheckBox
        JCheckBox toggle = new JCheckBox();
        toggle.setSelected(initialValue);
        toggle.setBackground(Color.WHITE);

        JPanel toggleWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        toggleWrapper.setBackground(Color.WHITE);
        toggleWrapper.add(toggle);
        rowPanel.add(toggleWrapper, BorderLayout.EAST);

        gbc.gridx = 0;
        gbc.weightx = 1.0;
        container.add(rowPanel, gbc);
    }

    private JPanel createSaveButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton saveButton = createStyledButton("Save Changes", new Color(0, 123, 255), Color.WHITE, 14);
        saveButton.setPreferredSize(new Dimension(180, 45));
        panel.add(saveButton);
        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor, int fontSize) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                AbstractButton b = (AbstractButton) c;
                ButtonModel model = b.getModel();
                g2.setColor(model.isPressed() ? bgColor.darker().darker() : model.isRollover() ? bgColor.darker() : bgColor);
                g2.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 10, 10);
                FontMetrics fm = g2.getFontMetrics();
                int x = (b.getWidth() - fm.stringWidth(b.getText())) / 2;
                int y = (b.getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.setColor(fgColor);
                g2.setFont(b.getFont());
                g2.drawString(b.getText(), x, y);
                g2.dispose();
            }
        });
        return button;
    }
}
