package fr.insalyon.dasi.collectif.job.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

@Entity
public class Demande implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date wantedDate;
    private String moment;
    
    @ManyToOne
    private Activite activite; 

    protected Demande() {
    }

    public Demande(Date date, String moment, Activite activite) {
        this.wantedDate = date;
        this.moment = moment;
        this.activite = activite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getWantedDate() {
        return wantedDate;
    }

    public void setWantedDate(Date wantedDate) {
        this.wantedDate = wantedDate;
    }

    public String getMoment() {
        return moment;
    }

    public void setMoment(String moment) {
        this.moment = moment;
    }

    public Activite getActivite() {
        return activite;
    }

    public void setActivite(Activite activite) {
        this.activite = activite;
    }

    @Override
    public String toString() {
        return "Demande{" + "id=" + id + ", wantedDate=" + wantedDate + ", moment=" + moment + ", activite=" + activite + '}';
    }
}
