package com.example.demo;

import com.example.demo.user.Badge;
import com.example.demo.user.Jour;
import com.example.demo.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class Stats {
    private User user;

    public void setUser(User user) {
        this.user = user;
        try{
            List<Badge> badges = this.user.getPlanning().getBadges();
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
            Good.setText(goodCount + " Good Badges");
            VGood.setText(vGoodCount + " Very Good Badges");
            Excellent.setText(excellentCount + " Excellent Badges");

            List<Jour> jours = this.user.getPlanning().getJours();
            int totalEncouragement = 0;

            for (Jour jour : jours) {
                totalEncouragement += jour.getEncouragement();
            }

            nbrdesEncouragements.setText(totalEncouragement + "");
        }catch(NullPointerException e){e.getMessage();}

    }

    @FXML
    private Label nbrdesEncouragements;

    @FXML
    private Label Excellent;

    @FXML
    private Label Good;

    @FXML
    private Label VGood;
}
