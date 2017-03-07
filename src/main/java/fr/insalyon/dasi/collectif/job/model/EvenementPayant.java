package fr.insalyon.dasi.collectif.job.model;


import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

@Entity
public class EvenementPayant extends Evenement implements Serializable {
    private Integer paf;

    public EvenementPayant(Date date, String moment, Integer paf) {
        super(date, moment);
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
