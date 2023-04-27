package user;

import javafx.scene.control.PasswordField;

public class User {
    private String pseudo;
    private PasswordField pass;
    private CalendrierPersonnel calendar;
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
    public String getPseudo() {
        return pseudo;
    }
    public PasswordField getPass() {
        return pass;
    }
    public void setPass(PasswordField pass) {
        this.pass = pass;
    }
    // valider ou refuser la proposition de planification, il peut changer le nom des sous taches
}
