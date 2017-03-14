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
    private String id;

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

    public String getId() {
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

    public void setId(String id) {
        this.id = id;
    }
}
