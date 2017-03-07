package fr.insalyon.dasi.collectif.job.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Inheritance( strategy = InheritanceType.JOINED)
public class Evenement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private Date date;
    private String moment;

    public Evenement(Date date, String moment) {
        this.date = date;
        this.moment = moment;
    }

    public Evenement() {
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
