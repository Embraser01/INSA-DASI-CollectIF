package fr.insalyon.dasi.collectif.business.model;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class EvenementGratuit extends Evenement implements Serializable {

    public EvenementGratuit(Date eventDate, MomentOfTheDay moment, List<Adherent> adherents, Activite activite, Lieu lieu) {
        super(eventDate, moment, adherents, activite, lieu);
    }

    public EvenementGratuit() {
    }
}
