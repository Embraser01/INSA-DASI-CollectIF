package fr.insalyon.dasi.collectif.business.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
public class Demande implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date wantedDate;

    @Enumerated(EnumType.STRING)
    private MomentOfTheDay moment;
    
    @ManyToOne
    private Activite activite;

    @ManyToOne
    private Adherent adherent;

    @ManyToOne
    private Evenement evenement;

    @Version
    private Long version;

    protected Demande() {
    }

    public Demande(Adherent adherent, Date date, MomentOfTheDay moment, Activite activite) {
        this.adherent = adherent;
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

    public Adherent getAdherent() {
        return adherent;
    }

    public void setAdherent(Adherent adherent) {
        this.adherent = adherent;
    }

    public Date getWantedDate() {
        return wantedDate;
    }

    public void setWantedDate(Date wantedDate) {
        this.wantedDate = wantedDate;
    }

    public MomentOfTheDay getMoment() {
        return moment;
    }

    public void setMoment(MomentOfTheDay moment) {
        this.moment = moment;
    }

    public Activite getActivite() {
        return activite;
    }

    public void setActivite(Activite activite) {
        this.activite = activite;
    }

    public Evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }
    
    @Override
    public String toString() {
        return "Demande{" + "id=" + id + ", wantedDate=" + wantedDate + ", moment=" + moment + ", activite=" + activite + '}';
    }
}
