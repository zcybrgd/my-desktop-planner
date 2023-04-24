package taches;

import java.awt.Color;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Tache {
    private String nomDeTache;
    private Duration durée;
    private LocalDateTime deadline;
    private Priorité priorité;
    private Categorie cat;
    private Color couleur;
    private LocalTime startTime;
    private LocalTime endTime;
    public Tache(String name, Duration duration, Priorité priority, LocalDateTime deadline, Categorie cat, LocalTime s, LocalTime e){
        this.nomDeTache = name;
        this.durée = duration;
        this.priorité = priority;
        this.deadline = deadline;
        this.cat = cat;
        this.couleur = CategoryColorMap.getColorForCategorie(cat);
        this.startTime = s;
        this.endTime = e;
    }
    public LocalTime getStartTime() {
        return startTime;
    }
    public LocalTime getEndTime() {
        return endTime;
    }
    public void setNomDeTache(String nomDeTache) {
        this.nomDeTache = nomDeTache;
    }
    public void setDurée(Duration durée) {
        this.durée = durée;
    }
    public void setPriorité(Priorité priorité) {
        this.priorité = priorité;
    }
    public String getNomDeTache() {
        return nomDeTache;
    }
    public Duration getDurée() {
        return durée;
    }
    public Priorité getPriorité() {
        return priorité;
    }
    public void setCat(Categorie cat) {
        this.cat = cat;
        this.couleur = CategoryColorMap.getColorForCategorie(cat);
    }
    public Categorie getCat() {
        return cat;
    }
    public LocalDateTime getDeadline() {
        return deadline;
    }
}
