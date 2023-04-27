package planification;

public interface Planification {
    // manuellement : choisir le créneau libre d'une journée et planifier les taches
    // système : l'user spécifie ses créneaux libres et la durée minimale de chaque créneau, et ensemble des taches (interface décomposable)
    // fixer une période pour la planification, sinon les fixer avant la deadline
    public void choisirUnCréneau();
    public void planifieruneTache();
}
