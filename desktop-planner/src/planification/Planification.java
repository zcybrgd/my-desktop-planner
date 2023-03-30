package planification;

public interface Planification {
    // manuellement : choisir le créneau libre d'une journée et planifier les taches
    // système : l'user spécifie ses créneaux libres et la durée minimale de chaque créneau
    public void choisirUnCréneau();
    public void planifieruneTache();
}
