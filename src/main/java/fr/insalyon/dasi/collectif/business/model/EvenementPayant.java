package fr.insalyon.dasi.collectif.business.model;


import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class EvenementPayant extends Evenement implements Serializable {
    private Integer paf;

    public EvenementPayant(Date eventDate, MomentOfTheDay moment, List<Adherent> adherents, Activite activite, Lieu lieu, Integer paf) {
        super(eventDate, moment, adherents, activite, lieu);
        this.paf = paf;
    }

    public EvenementPayant() {
    }

    public Integer getPaf() {
        return paf;
    }

    public void setPaf(Integer paf) {
        this.paf = paf;
    }
}
