package taches;

import java.time.Duration;
import java.awt.Color;

public class Tache {
    private String nomDeTache;
    private Duration durée;
    private Priorité priorité;
    private Catégorie cat;
    private Color couleur;
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
    public void setCat(Catégorie cat) {
        this.cat = cat;
    }
    public Catégorie getCat() {
        return cat;
    }
    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }
    public Color getCouleur() {
        return couleur;
    }
    public void setColor(){
        Color color;
        switch (this.getCat()) {
            case Studies :
                color = Color.PINK;
                break;
            case Work :
                color = Color.GREEN;
                break;
            case Hobby :
                color = Color.BLUE;
                break;
            case Sport:
                color = Color.YELLOW;
                break;
            case Health:
                color = Color.ORANGE;
                break;
            default:
                color = Color.WHITE;
                break;
        }
        setCouleur(color);
    }
}
