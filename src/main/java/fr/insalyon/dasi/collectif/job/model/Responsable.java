package fr.insalyon.dasi.collectif.job.model;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class Responsable extends Adherent implements Serializable {

    private String role;

    public Responsable() {
    }

    public Responsable(String nom, String prenom, String mail, String adresse, String password, String role) {
        super(nom, prenom, mail, adresse, password);
        this.role = role;
    }


}
