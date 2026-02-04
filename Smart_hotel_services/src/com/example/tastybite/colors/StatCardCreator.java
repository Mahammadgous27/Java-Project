package com.example.tastybite.colors;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

// This method should be a static utility or placed inside the class that uses it (e.g., HomePage)
public class StatCardCreator {
    public static JPanel createStatCard(String title, String count, String detail) {
        JPanel card = new JPanel();
        // Use BoxLayout for vertical stacking
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS)); 
        
        // --- Appearance ---
        card.setBackground(ColorPalette.CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(ColorPalette.BACKGROUND_GRAY, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15) // Internal padding
        ));
        
        // --- Title Label ---
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(ColorPalette.FONT_HEADER);
        titleLabel.setForeground(ColorPalette.TEXT_DARK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // --- Count Label (The large number) ---
        JLabel countLabel = new JLabel(count, SwingConstants.CENTER);
        countLabel.setFont(new Font("Arial", Font.BOLD, 30)); // Reduced size for better fit
        countLabel.setForeground(ColorPalette.PRIMARY_BLUE);
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // --- Detail Label ---
        JLabel detailLabel = new JLabel(detail, SwingConstants.CENTER);
        detailLabel.setFont(ColorPalette.FONT_BODY);
        detailLabel.setForeground(Color.GRAY);
        detailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components with vertical spacing (glue/rigid area)
        card.add(Box.createVerticalGlue());
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(countLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(detailLabel);
        card.add(Box.createVerticalGlue());
        
        return card;
    }
}


