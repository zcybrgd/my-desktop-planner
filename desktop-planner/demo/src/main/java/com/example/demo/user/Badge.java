package com.example.demo.user;

import java.io.Serializable;

public class Badge implements Serializable {
    private String badgeLabel;
    public Badge (String badgeLabel){
        this.badgeLabel = badgeLabel;
    }

    public String getBadgeLabel() {
        return badgeLabel;
    }
}
