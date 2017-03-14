package fr.insalyon.dasi.collectif.business.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Entity
public class Activite implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String denomination;
    private Boolean payant;
    private Integer nbParticipants;

    @OneToMany(mappedBy = "activite")
    private List<Evenement> evenements;

    @OneToMany(mappedBy = "activite")
    private List<Demande> demandes;

    protected Activite() {
    }

    public Activite(String denomination, Boolean payant, Integer nbParticipants) {
        this.denomination = denomination;
        this.payant = payant;
        this.nbParticipants = nbParticipants;
    }

    public Long getId() {
        return id;
    }

    public String getDenomination() {
        return denomination;
    }

    public Boolean getPayant() {
        return payant;
    }

    public Integer getNbParticipants() {
        return nbParticipants;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public void setPayant(Boolean payant) {
        this.payant = payant;
    }

    public void setNbParticipants(Integer nbParticipants) {
        this.nbParticipants = nbParticipants;
    }

    @Override
    public String toString() {
        return "Activite{" + "id=" + id + ", denomination=" + denomination + ", payant=" + payant + ", nbParticipants=" + nbParticipants + '}';
    }

}
