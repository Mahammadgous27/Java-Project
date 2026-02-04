package com.example.testybite;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class HomePage extends JFrame {

    // --- 1. FIELD DECLARATIONS ---
    public CardLayout cardLayout;
    public JPanel cardPanel;
    private final Color darkBlue = new Color(135, 206, 235);
    private Cart cart; // Shared Cart object
    private CartPanel cartPanelView; // Store reference to CartPanel
    private HotelDetailsPanel hotelDetailsPanelView;

    // --- CONSTRUCTOR ---
    public HomePage() {
        Container contentPane = this.getContentPane();
        contentPane.setBackground(darkBlue);
        setTitle("TastyBites - Home");
        setSize(1700, 820);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize shared Cart
        cart = new Cart();

        // 1. Setup Card Layout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(darkBlue);

        // 2. Add Content Views to CardLayout
        cardPanel.add(createMainContent().getViewport().getView(), "DASHBOARD");
        cardPanel.add(new ExploreHotels(this), "EXPLORE");
        cardPanel.add(new HotelDetailsPanel(this, "Default Hotel", cart), "HOTEL_DETAILS");

        // ✅ Add CartPanel view and assign to class field
        cartPanelView = new CartPanel(cart);
        cardPanel.add(cartPanelView, "CART");

        // 3. Root Layout
        setLayout(new BorderLayout());

        // 4. Header
        add(createHeader(), BorderLayout.NORTH);

        // 5. Sidebar
        add(createSidebar(), BorderLayout.WEST);

        // 6. Main Content
        add(cardPanel, BorderLayout.CENTER);

        // 7. Footer
        add(createFooter(), BorderLayout.SOUTH);

        // 8. Default View
        cardLayout.show(cardPanel, "DASHBOARD");

        setVisible(true);
    }

    // --- SIDEBAR IMPLEMENTATION ---
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(8, 1, 5, 5));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        sidebar.setBackground(darkBlue);

        String[] items = { "Home", "Hotels", "Rooms", "Cart", "Settings", "Logout"};
        for (String item : items) {
            JButton btn = new JButton(item);
            sidebar.add(btn);

            switch (item) {
                case "Logout":
                    btn.addActionListener(e -> {
                        dispose();
                        new Login().setVisible(true);
                    });
                    break;

//                case "Dashboard":
                case "Home":
                    btn.addActionListener(e -> cardLayout.show(cardPanel, "DASHBOARD"));
                    break;

                case "Hotels":
                    btn.addActionListener(e -> cardLayout.show(cardPanel, "EXPLORE"));
                    break;

                case "Cart":
                    btn.addActionListener(e -> cardLayout.show(cardPanel, "CART"));
                    break;

                case "Rooms":
                    btn.addActionListener(e -> JOptionPane.showMessageDialog(
                            this,
                            "Opening Generic Room Booking Window...",
                            "Book Room",
                            JOptionPane.INFORMATION_MESSAGE
                    ));
                    break;
                case "Settings":
                    btn.addActionListener(e -> {
                        Component existingSettings = findSettingsPanel();
                        if (existingSettings == null) {
                            SettingsPanel settingsPanel = new SettingsPanel();
                            cardPanel.add(settingsPanel, "SETTINGS");
                        }
                        cardLayout.show(cardPanel, "SETTINGS");
                    });
                    break;

            }
        }
        return sidebar;
    }

    // --- HOTEL DETAILS VIEW SWITCHER ---
    public void showHotelDetails(String hotelName) {
        Component existingPanel = findHotelDetailsPanel();
        if (existingPanel != null) cardPanel.remove(existingPanel);

        HotelDetailsPanel newDetailsPanel = new HotelDetailsPanel(this, hotelName, cart);
        cardPanel.add(newDetailsPanel, "HOTEL_DETAILS");
        cardLayout.show(cardPanel, "HOTEL_DETAILS");
    }

    private Component findHotelDetailsPanel() {
        for (Component comp : cardPanel.getComponents()) {
            if (comp instanceof HotelDetailsPanel) return comp;
        }
        return null;
    }
 
    // --- HEADER ---
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        header.setBackground(darkBlue);

        JLabel title = new JLabel("TastyBites", SwingConstants.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        JTextField search = new JTextField("Search...");
        JButton searchBtn = new JButton("Search");

        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.setOpaque(false);
        searchPanel.add(search, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);
        header.add(searchPanel, BorderLayout.CENTER);

        String userName = "Alex Johnson";
        String userEmail = "alex.j@tastybites.com";

        header.add(createProfileIconPanel(userName, userEmail), BorderLayout.EAST);

        return header;
    }

    private JPanel createProfileIconPanel(String userName, String userEmail) {
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        profilePanel.setOpaque(false);

        int iconSize = 40;
        Icon profileIcon = createCircularIcon(iconSize);

        JLabel iconLabel = new JLabel(profileIcon);
        iconLabel.setToolTipText("User Profile");

        iconLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showProfilePopup(iconLabel, userName, userEmail, 0, iconLabel.getHeight());
            }
        });

        profilePanel.add(iconLabel);
        profilePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        return profilePanel;
    }
    private Component findSettingsPanel() {
        for (Component comp : cardPanel.getComponents()) {
            if (comp instanceof SettingsPanel) return comp;
        }
        return null;
    }

    private Icon createCircularIcon(int size) {
        return new Icon() {
            private final int iconSize = size;

            @Override
            public int getIconWidth() { return iconSize; }

            @Override
            public int getIconHeight() { return iconSize; }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color topBlue = new Color(50, 150, 255);
                Color bottomBlue = new Color(0, 100, 200);

                GradientPaint gradient = new GradientPaint(x, y, topBlue, x, y + iconSize, bottomBlue);
                g2.setPaint(gradient);
                g2.fill(new Ellipse2D.Float(x, y, iconSize, iconSize));

                g2.setColor(Color.WHITE);

                int headSize = (int) (iconSize * 0.3);
                int headX = x + (iconSize - headSize) / 2;
                int headY = (int) (y + iconSize * 0.15);
                g2.fill(new Ellipse2D.Float(headX, headY, headSize, headSize));

                int bodyWidth = (int) (iconSize * 0.8);
                int bodyHeight = (int) (iconSize * 0.4);
                int bodyX = x + (iconSize - bodyWidth) / 2;
                int bodyY = (int) (y + iconSize * 0.65);
                g2.fill(new Ellipse2D.Float(bodyX, bodyY, bodyWidth, bodyHeight));

                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1));
                g2.draw(new Ellipse2D.Float(x, y, iconSize - 1, iconSize - 1));

                g2.dispose();
            }
        };
    }

    private void showProfilePopup(Component invoker, String userName, String userEmail, int x, int y) {
        JPopupMenu popup = new JPopupMenu();

        JPanel detailPanel = new JPanel(new BorderLayout(5, 5));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        detailPanel.setBackground(new Color(240, 240, 240));

        JLabel nameLabel = new JLabel("Hi, " + userName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel emailLabel = new JLabel(userEmail);
        emailLabel.setForeground(Color.GRAY);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        detailPanel.add(nameLabel, BorderLayout.NORTH);
        detailPanel.add(emailLabel, BorderLayout.CENTER);

        JMenuItem headerItem = new JMenuItem();
        headerItem.setLayout(new BorderLayout());
        headerItem.add(detailPanel, BorderLayout.CENTER);
        headerItem.setEnabled(false);

        popup.add(headerItem);
        popup.addSeparator();

        JMenuItem settings = new JMenuItem("Settings");
        settings.addActionListener(e -> {
            cardLayout.show(cardPanel, "SETTINGS");
            popup.setVisible(false);
        });

        JMenuItem viewProfile = new JMenuItem("View Profile");
        viewProfile.addActionListener(e -> {
            cardLayout.show(cardPanel, "SETTINGS");
            popup.setVisible(false);
        });

        JMenuItem logout = new JMenuItem("Logout", UIManager.getIcon("OptionPane.errorIcon"));
        logout.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(invoker, "Are you sure you want to log out?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                dispose();
                new Login().setVisible(true);
            }
        });

        popup.add(viewProfile);
        popup.add(settings);
        popup.addSeparator();
        popup.add(logout);

        popup.show(invoker, 0, invoker.getHeight());
    }

    // --- MAIN CONTENT ---
    private JScrollPane createMainContent() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(darkBlue);

        JLabel welcome = new JLabel("Welcome to SmartDining!", SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 24));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel stats = new JPanel(new GridLayout(1, 3, 5, 5));
        stats.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        stats.add(createStatCard("Active Orders", "03", "Currently in progress"));
        stats.add(createStatCard("Past Orders", "25", "Completed successfully"));
        stats.add(createStatCard("Saved Hotels", "10", "Your favorite restaurants"));

        JPanel hotels = new JPanel(new GridLayout(1, 2, 30, 30));
        hotels.setBorder(BorderFactory.createTitledBorder("Explore Hotels"));
        hotels.add(createHotelCard("Hotel Paradise", "4.5", "Indian, Chinese"));
        hotels.add(createHotelCard("Urban Bites", "4.2", "Italian, Continental"));

        mainPanel.add(welcome);
        mainPanel.add(stats);
        mainPanel.add(hotels);

        return new JScrollPane(mainPanel);
    }

    private JPanel createStatCard(String title, String count, String subtext) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        card.setBackground(new Color(245, 245, 245));

        JLabel countLabel = new JLabel(count, SwingConstants.CENTER);
        countLabel.setFont(new Font("Arial", Font.BOLD, 22));
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        JLabel subLabel = new JLabel(subtext, SwingConstants.CENTER);

        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titleLabel);
        card.add(countLabel);
        card.add(subLabel);

        return card;
    }

    private JPanel createHotelCard(String name, String rating, String cuisine) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        card.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel("🏨 " + name);
        JLabel ratingLabel = new JLabel("⭐ " + rating);
        JLabel cuisineLabel = new JLabel(cuisine);

        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ratingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cuisineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(nameLabel);
        card.add(ratingLabel);
        card.add(cuisineLabel);

        return card;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new GridLayout(1, 3, 20, 20));
        footer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        footer.setBackground(darkBlue);

        footer.add(createFooterSection("About Us", "SmartDining helps you order food..."));
        footer.add(createFooterSection("Quick Links", "Home | Hotels | Cart"));
        footer.add(createFooterSection("Follow Us", "Facebook | Twitter | Instagram"));

        return footer;
    }

    private JPanel createFooterSection(String title, String content) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(darkBlue);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel contentLabel = new JLabel("<html>" + content.replace("\n", "<br>") + "</html>");

        section.add(titleLabel);
        section.add(contentLabel);
        return section;
    }

    public void refreshCartPanel() {
        if (cartPanelView != null) {
            cartPanelView.refreshCart(); // refresh display after adding items
        }
    }

    public static void main(String[] args) {
        new HomePage();
    }
}
