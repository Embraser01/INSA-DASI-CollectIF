package fr.insalyon.dasi.collectif.business.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Inheritance( strategy = InheritanceType.JOINED)
public abstract class Evenement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date eventDate;
    private String moment;

    @ManyToMany(mappedBy = "evenements")
    private List<Adherent> adherents;

    @ManyToOne
    private Activite activite;

    @ManyToOne
    private Lieu lieu;

    protected Evenement(Date eventDate, String moment) {
        this.eventDate = eventDate;
        this.moment = moment;
    }

    public Evenement() {
    }

    public Long getId() {
        return id;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getMoment() {
        return moment;
    }

    public void setMoment(String moment) {
        this.moment = moment;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Adherent> getAdherents() {
        return adherents;
    }

    public void setAdherents(List<Adherent> adherents) {
        this.adherents = adherents;
    }

    public Activite getActivite() {
        return activite;
    }

    public void setActivite(Activite activite) {
        this.activite = activite;
    }

    public Lieu getLieu() {
        return lieu;
    }

    public void setLieu(Lieu lieu) {
        this.lieu = lieu;
    }
}
