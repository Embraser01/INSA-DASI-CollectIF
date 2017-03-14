package fr.insalyon.dasi.collectif.business.model;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

@Entity
public class EvenementGratuit extends Evenement implements Serializable {

    public EvenementGratuit(Date date, MomentOfTheDay moment) {
        super(date, moment);
    }

    public EvenementGratuit() {
    }
}
