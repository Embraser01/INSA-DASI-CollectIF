/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.collectif.view;

import fr.insalyon.dasi.collectif.business.model.*;
import fr.insalyon.dasi.collectif.business.service.BusinessService;
import fr.insalyon.dasi.collectif.dao.JpaUtil;
import fr.insalyon.dasi.collectif.util.Saisie;

import java.util.*;

/**
 * To start the JavaDB on Windows : "C:\Program Files\Java\jdk1.8.0_77\db\bin\startNetworkServer" -noSecurityManager
 */
public class Main {

    public static void main(String[] args) {
        init();

        BusinessService businessService = new BusinessService();

        // View start
        doMain(businessService);


        stop();
    }

    private static void init() {
        JpaUtil.init();
    }

    private static void stop() {
        JpaUtil.destroy();
    }


    private static void doMain(BusinessService businessService) {
        int menuSelection;
        Adherent currentUser;

        while (true) {

            menuSelection = menuAuth();

            switch (menuSelection) {
                case 1:

                    String email = Saisie.readString("Entre votre mail : ");

                    // FIXME readPassword() ?
                    String password = Saisie.readString("Entrez votre mot de passe : ");

                    currentUser = businessService.authLogin(email, password);
                    // On supprime de la mémoire le mot de passe...
                    password = "";
                    if (currentUser != null) {
                        System.out.println("Vous êtes bien connecté");
                        if (currentUser instanceof Responsable) {
                            doResp((Responsable) currentUser, businessService);
                        } else {
                            doAdher(currentUser, businessService);
                        }
                    } else {
                        System.out.println("Mauvaise combinaison mail/mot de passe");
                    }
                    break;
                case 2:
                    String nom = Saisie.readString("Entrez votre nom : ");
                    String prenom = Saisie.readString("Entrez votre prénom : ");
                    String mail = Saisie.readString("Entrez votre mail : ");
                    String adresse = Saisie.readString("Entrez votre adresse : ");

                    String signupPassword = "1";
                    String passwordConfirmation = "2";


                    while (!Objects.equals(signupPassword, passwordConfirmation)) {
                        signupPassword = Saisie.readString("Entrez votre mot de passe :");
                        passwordConfirmation = Saisie.readString("Re-entrez votre mot de passe :");
                    }

                    currentUser = new Adherent(nom, prenom, mail, adresse, signupPassword);
                    currentUser = businessService.authSignup(currentUser);
                    if (currentUser != null) {
                        System.out.println("Vous êtes bien inscrits");
                        if (currentUser instanceof Responsable) {
                            doResp((Responsable) currentUser, businessService);
                        } else {
                            doAdher(currentUser, businessService);
                        }
                    } else {
                        System.out.println("Erreur lors de l'inscription");
                    }
                    break;
                case 3:
                    stop();
                    return;
                default:
                    break;
            }
        }
    }

    private static void doResp(Responsable responsable, BusinessService businessService) {
        List<Evenement> evenements;
        Evenement ev;
        int menuSelection;

        while (responsable != null) {
            menuSelection = menuResponsable();
            switch (menuSelection) {
                case 1:
                    evenements = businessService.consulterEvenements();

                    System.out.println("Liste des évènements :");
                    for (Evenement evenement :
                            evenements) {
                        System.out.println(evenement);
                    }
                    System.out.println("---FIN---");

                    break;
                case 2:
                    long wantedID = Saisie.readLong("ID de l'évènement à compléter :");

                    ev = null;
                    evenements = businessService.consulterEvenements();

                    for (Evenement evenement :
                            evenements) {
                        if (evenement.getId() == wantedID) {
                            ev = evenement;
                            break;
                        }
                    }

                    if (ev != null) {

                        System.out.println("Id : " + ev.getId());
                        System.out.println("Activité le : " + ev.getEventDate() + " - " + ev.getMoment());
                        System.out.println("Activité : " + ev.getActivite().getDenomination());
                        System.out.println("-------------- A remplir :");

                        List<Lieu> lieux = businessService.consulterLieux();
                        // TODO Cac

                        System.out.println("Liste des lieux disponibles :");
                        for (Lieu lieu1 : lieux) {
                            System.out.println(lieu1);
                        }
                        System.out.println("---FIN---");

                        boolean found = false;

                        while (!found) {
                            Long lieuId = Saisie.readLong("Lieu (id) : ");

                            for (Lieu lieu1 : lieux) {
                                if (Objects.equals(lieu1.getId(), lieuId)) {
                                    found = true;
                                    ev.setLieu(lieu1);
                                    break;
                                }
                            }
                        }

                        if (ev.getActivite().getPayant()) {
                            ((EvenementPayant) ev).setPaf(Saisie.readInteger("PAF : "));
                        }

                        if (businessService.completerEvenement(ev)) {
                            System.out.println("L'évènement a bien été complété");
                        } else {
                            System.out.println("Erreur lors de la completion de l'évènement");
                        }
                    } else {
                        System.out.println("Cet évenement n'existe pas !");
                    }
                    break;
                case 3:
                    responsable = null;
                    break;
                default:
                    break;
            }
        }
    }

    private static void doAdher(Adherent currentUser, BusinessService businessService) {
        List<Demande> demandes;
        int menuSelection;

        while (currentUser != null) {
            menuSelection = menuAdherent();
            switch (menuSelection) {
                case 1:
                    demandes = businessService.consulterHistorique(currentUser);

                    System.out.println("Liste des demandes :");

                    if (demandes.size() > 0) {
                        for (Demande demande :
                                demandes) {
                            System.out.println(demande);
                        }
                    } else {
                        System.out.println("Aucune demande pour l'instant");
                    }
                    System.out.println("---FIN---");
                    break;
                case 2:
                    Activite activite = null;
                    Date date;
                    MomentOfTheDay momentOfTheDay = null;


                    System.out.println("-------------- A remplir :");

                    List<Activite> activites = businessService.consulterActivites();
                    for (Activite activite1 :
                            activites) {
                        System.out.println(activite1);
                    }

                    boolean found = false;
                    while (!found) {
                        String acti = Saisie.readString("Activité : ");

                        for (Activite activite1 : activites) {
                            if (activite1.getDenomination().equals(acti)) {
                                found = true;
                                activite = activite1;
                                break;
                            }
                        }
                    }
                    date = Saisie.readDate("Date de l'activité (dd/MM/yyyy) : ", "dd/MM/yyyy");

                    while (momentOfTheDay == null) {
                        momentOfTheDay = MomentOfTheDay.find(Saisie.readString("Moment de la journée " + MomentOfTheDay.all.toString() + " :"));
                    }

                    Demande demande = new Demande(currentUser, date, momentOfTheDay, activite);
                    if (businessService.posterDemande(demande)) {
                        System.out.println("La demande a bien été postée");
                    } else {
                        System.out.println("Erreur lors de l'ajout de la demande (la date est déjà passée ?)");
                    }
                    break;
                case 3:
                    currentUser = null;
                    break;
                default:
                    break;
            }
        }
    }


    private static int menuAuth() {

        System.out.println("Menu d'accueil : ");
        System.out.println("-------------------------\n");
        System.out.println("1 - Se connecter");
        System.out.println("2 - S'inscrire");
        System.out.println("3 - Quitter");

        return Saisie.readInteger("Selection :", 1, 2, 3);
    }

    private static int menuAdherent() {

        System.out.println("Menu Adhérent : ");
        System.out.println("-------------------------\n");
        System.out.println("1 - Consulter les demandes");
        System.out.println("2 - Poster une demande");
        System.out.println("3 - Se déconnecter");

        return Saisie.readInteger("Selection :", 1, 2, 3);
    }

    private static int menuResponsable() {

        System.out.println("Menu Responsable : ");
        System.out.println("-------------------------\n");
        System.out.println("1 - Consulter les évènements");
        System.out.println("2 - Valider/Modifier un évènement");
        System.out.println("3 - Se déconnecter");

        return Saisie.readInteger("Selection :", 1, 2, 3);
    }
}
