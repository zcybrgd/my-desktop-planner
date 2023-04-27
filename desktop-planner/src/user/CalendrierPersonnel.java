package user;


import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import planification.Creneau;
import taches.Tache;

public class CalendrierPersonnel {
    private List<Creneau> lesCrenaux; // les créneaux 
    private List<Tache> taches; // la liste des taches

    public CalendrierPersonnel(List<Creneau> lesCrenaux) {
        this.lesCrenaux = lesCrenaux;
        this.taches = new ArrayList<>();
    }

   /*  public void addTache(Tache Tache, boolean manuallyScheduled) throws MinimumDurationException {
        if (manuallyScheduled) {
            // manually schedule the Tache
            LocalTime start = Tache.getStartTime();
            LocalTime end = Tache.getEndTime();
            Duration TacheDuration = Tache.getDurée();

            boolean isTimeSlotFound = false;
            for (Creneau timeSlot : lesCrenaux) {
                if (timeSlot.contains(start) && timeSlot.contains(end)) {
                    // found a time slot that fully contains the Tache duration
                    isTimeSlotFound = true;

                    // check if time slot can be decomposed
                    Duration slotDuration = Duration.between(timeSlot.getStartTime(), timeSlot.getEndTime());
                    if (slotDuration.compareTo(TacheDuration) <= 0) {
                        // add Tache to time slot
                        timeSlot.addTache(Tache);
                        taches.add(Tache);

                        // remove time slot if it has been fully scheduled
                        if (timeSlot.isFullyScheduled()) {
                            lesCrenaux.remove(timeSlot);
                        }
                        break;
                    } else if (slotDuration.compareTo(Tache.getMinimumDuration()) >= 0) {
                        // decompose time slot and add Tache to first part
                        TimeSlot firstSlot = new TimeSlot(timeSlot.getStartTime(), end);
                        timeSlot.setStartTime(start);
                        timeSlot.addTache(Tache);
                        Taches.add(Tache);
                        lesCrenaux.add(firstSlot);
                        break;
                    } else {
                        throw new MinimumDurationException("Time slot cannot be decomposed to schedule Tache");
                    }
                }
            }
            if (!isTimeSlotFound) {
                throw new IllegalArgumentException("No free time slot available for scheduling Tache");
            }
        } else {
            // automatically schedule the Tache
            // implementation of automatic scheduling is beyond the scope of this example
            // it could involve machine learning algorithms, optimization techniques, etc.
            // for the purpose of this example, we simply add the Tache to the list of scheduled Taches
            Taches.add(Tache);
        }
    }*/

    public List<Creneau> getlesCrenaux() {
        return lesCrenaux;
    }

    public List<Tache> getTaches() {
        return taches;
    }
}

class MinimumDurationException extends Exception {}