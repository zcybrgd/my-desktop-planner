package com.example.demo;

import com.example.demo.user.Badge;
import com.example.demo.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class Stats {
    private User user;

    public void setUser(User user) {
        this.user = user;
        try{
            List<Badge> badges = user.getPlanning().getBadges();
            badges.add(new Badge("Good"));
            // Count the occurrences of each badge type
            int goodCount = 0;
            int vGoodCount = 0;
            int excellentCount = 0;
            for (Badge badge : badges) {
                String badgeType = badge.getBadgeLabel();
                if (badgeType.equals("Good")) {
                    goodCount++;
                } else if (badgeType.equals("VeryGood")) {
                    vGoodCount++;
                } else if (badgeType.equals("Excellent")) {
                    excellentCount++;
                }
            }
           // Update the label texts with the badge counts
            Good.setText("You have " + goodCount + " Good Badges");
            VGood.setText("You have " + vGoodCount + " VeryGood Badges");
            Excellent.setText("You have " + excellentCount + " Excellent Badges");
        }catch(NullPointerException e){e.getMessage();}

    }

    @FXML
    private Label Excellent;

    @FXML
    private Label Good;

    @FXML
    private Label VGood;
}
