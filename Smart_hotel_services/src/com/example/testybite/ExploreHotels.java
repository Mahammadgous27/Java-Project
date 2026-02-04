package com.example.testybite;

import javax.swing.*;
import javax.swing.border.LineBorder;
import com.example.tastybite.colors.ColorPalette;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ExploreHotels extends JPanel {
    private HomePage parentFrame;
    
    public ExploreHotels(HomePage parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        setBackground(ColorPalette.PRIMARY_BLUE);
        
        // Title Area
        JLabel title = new JLabel("Explore Hotels", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        add(title, BorderLayout.NORTH);
        
        // Cards Container (Scrollable)
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        cardsPanel.setBackground(ColorPalette.BACKGROUND_GRAY);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 50, 20));
        
        // Add hotel cards with images
        cardsPanel.add(createHotelCard("Hotel Paradise", "4.5", "Indian, Chinese", "image1.png"));
        cardsPanel.add(createHotelCard("Hotel Limra", "4.2", "Italian, Continental", "image2.png"));
        cardsPanel.add(createHotelCard("Royal Feast", "4.7", "Mughlai, North Indian", "image3.png"));
        cardsPanel.add(createHotelCard("Sea Breeze", "4.6", "Seafood, Continental", "image4.png"));
        
        // Make the whole thing scrollable
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createHotelCard(String name, String rating, String cuisine, String imagePath) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(250, 280));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(0, 0, 10, 0)
        ));

        // --- Load hotel image from absolute path ---
        ImageIcon hotelIcon = null;
        try {
            String fullPath = "E:/Smart_hotel_services/resources/hotels/"+ imagePath;
            java.io.File imgFile = new java.io.File(fullPath);
            if (imgFile.exists()) {
                hotelIcon = new ImageIcon(fullPath);
                Image scaledImage = hotelIcon.getImage().getScaledInstance(250, 180, Image.SCALE_SMOOTH);
                hotelIcon = new ImageIcon(scaledImage);
            } else {
                System.out.println("Image not found: " + fullPath);
                hotelIcon = new ImageIcon(); // empty placeholder
            }
        } catch (Exception ex) {
            System.out.println("Error loading image: " + imagePath);
            hotelIcon = new ImageIcon(); // empty placeholder
        }

        // --- Image Label ---
        JLabel imageLabel = new JLabel(hotelIcon);
        imageLabel.setPreferredSize(new Dimension(250, 180));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.LIGHT_GRAY);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(imageLabel, BorderLayout.NORTH);

        // --- Text Panel ---
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
        textPanel.setBackground(new Color(173, 216, 230)); // Faint Blue


        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        textPanel.add(nameLabel);

        JLabel ratingLabel = new JLabel("⭐ " + rating);
        ratingLabel.setForeground(new Color(255, 165, 0)); // Orange/Gold
        textPanel.add(ratingLabel);

        JLabel cuisineLabel = new JLabel(cuisine);
        cuisineLabel.setFont(ColorPalette.FONT_BODY);
        cuisineLabel.setForeground(Color.GRAY);
        textPanel.add(cuisineLabel);

        card.add(textPanel, BorderLayout.CENTER);

        // --- LINK: Navigate to Hotel Detail ---
        final String hotelName = name;
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (parentFrame != null) {
                    parentFrame.showHotelDetails(hotelName);
                }
            }
        });

        return card;
    }


}
