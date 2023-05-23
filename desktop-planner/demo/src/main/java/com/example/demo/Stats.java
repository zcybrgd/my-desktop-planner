package com.example.demo;

import com.example.demo.user.Badge;
import com.example.demo.user.Jour;
import com.example.demo.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class Stats {
    private User user;

    public void setUser(User user) {
        this.user = user;
        try{
            gererLesEncouragements();
            List<Badge> badges = this.user.getPlanning().getBadges();
            // Count the occurrences of each badge type
            int goodCount = 0;
            int vGoodCount = 0;
            int excellentCount = 0;
            for (Badge badge : badges) {
                System.out.println("on est dans la boucle pour afficher les badges: " + badge.getBadgeLabel());
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
        }catch(NullPointerException e){e.getMessage();}

    }

    void gererLesEncouragements(){
        List<Jour> jours = user.getPlanning().getJours();
        int consecutiveDays = 0; // Compteur de jours consécutifs avec 1 encouragement

        for (Jour jour : jours) {
            System.out.println("encouragement: " + jour.getEncouragement() + " de " + jour.getDateDuJour());
            if (jour.getEncouragement() == 1) {
                consecutiveDays++;
                if (consecutiveDays == 2) { //5
                    System.out.println("est ce que on add un good la?");
                    Badge badgeGood = new Badge("Good");
                    List<Badge> badges = user.getPlanning().getBadges();
                    badges.add(badgeGood);
                    user.getPlanning().setBadges(badges);
                }
            } else {
                consecutiveDays = 0;
            }
        }

        int goodBadgeCount = 0; // Compteur de badges "Good" obtenus
        if(user.getPlanning().getBadges()==null){
            user.getPlanning().setBadges(new ArrayList<>());
        }
        for (Badge badge : user.getPlanning().getBadges()) {
            if (badge.getBadgeLabel().equals("Good")) {
                goodBadgeCount++;
                if (goodBadgeCount == 2) { // 3
                    Badge badgeVGood = new Badge("VeryGood");
                    List<Badge> badges = user.getPlanning().getBadges();
                    badges.add(badgeVGood);
                    user.getPlanning().setBadges(badges);
                } else if (goodBadgeCount == 4) { // 6
                    Badge badgeE = new Badge("Excellent");
                    List<Badge> badges = user.getPlanning().getBadges();
                    badges.add(badgeE);
                    user.getPlanning().setBadges(badges);
                }
            }
        }
    }

    @FXML
    private Label Excellent;

    @FXML
    private Label Good;

    @FXML
    private Label VGood;
}
