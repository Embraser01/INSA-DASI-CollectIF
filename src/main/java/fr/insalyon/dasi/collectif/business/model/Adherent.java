package fr.insalyon.dasi.collectif.business.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Entity
@Inheritance( strategy = InheritanceType.JOINED)
public class Adherent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nom;
    private String prenom;
    private String mail;
    private String adresse;
    private String password;
    private Double longitude;
    private Double latitude;

    @OneToMany(mappedBy = "adherent")
    private List<Demande> demandes;

    @ManyToMany
    private List<Evenement> evenements;


    protected Adherent() {
    }
    
    public Adherent(String nom, String prenom, String mail, String adresse, String password) {
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.adresse = adresse;
        this.password = password;
        this.longitude = null;
        this.latitude = null;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getMail() {
        return mail;
    }

    public String getAdresse() {
        return adresse;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    

    public void setLatitudeLongitude(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public List<Demande> getDemandes() {
        return demandes;
    }

    public List<Evenement> getEvenements() {
        return evenements;
    }

    @Override
    public String toString() {
        return "Adherent{" + "id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", mail=" + mail + ", adresse=" + adresse + ", password=" + password + ", longitude=" + longitude + ", latitude=" + latitude + '}';
    }

    public void addEvent(Evenement newEvenement) {
        evenements.add(newEvenement);
    }
}
