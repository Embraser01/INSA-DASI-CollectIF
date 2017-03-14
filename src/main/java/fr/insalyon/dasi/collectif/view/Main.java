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
 * @author tbourvon
 */
public class Main {

    public static void main(String[] args) {
        init();

        BusinessService businessService = new BusinessService();
        Adherent currentUser = null;

        // View start
        int menuSelection;
        Scanner input = new Scanner(System.in);

        // Connexion de l'utilisateur

        while (currentUser == null) {

            menuSelection = menuAuth();

            switch (menuSelection) {
                case 1:

                    String password;
                    String email;

                    System.out.println("Entrez votre nom :");
                    email = input.nextLine();

                    System.out.println("Entrez votre mot de passe :");
                    password = String.valueOf(System.console().readPassword());

                    currentUser = businessService.authLogin(email, password);
                    if (currentUser != null) {
                        System.out.println("Vous êtes bien connecté");
                    }
                    break;
                case 2:
                    currentUser = businessService.authSignup(signup());
                    if (currentUser != null) {
                        System.out.println("Vous êtes bien inscrits");
                    }
                    break;
                case 3:
                    stop();
                    return;
                default:
                    break;
            }
        }

        // A partir d'ici on est connecté

        if (currentUser instanceof Responsable) {
            // TODO Responsable
            List<Evenement> evenements;
            Evenement ev;

            while (currentUser != null) {
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
                        System.out.println("ID de l'évènement à compléter :");

                        long wantedID = input.nextLong();
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

                            // TODO Catalogue de lieu
                            List<String> lieux = new ArrayList<>();

                            boolean found = false;

                            while (!found) {
                                System.out.println("Lieu :");
                                String lieu = input.nextLine();

                                for (String lieu1 : lieux) {
                                    if (lieu1.equals(lieu)) {
                                        found = true;
                                        break;
                                    }
                                }
                            }

                            if (ev.getActivite().getPayant()) {
                                System.out.println("PAF :");
                                ((EvenementPayant) ev).setPaf(input.nextInt());
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
                        currentUser = null;
                        break;
                    default:
                        break;
                }
            }
        } else {
            // TODO Adhérent

            List<Demande> demandes;
            while (currentUser != null) {
                menuSelection = menuAdherent();
                switch (menuSelection) {
                    case 1:
                        demandes = businessService.consulterHistorique(currentUser);

                        System.out.println("Liste des demandes :");
                        for (Demande demande :
                                demandes) {
                            System.out.println(demande);
                        }
                        System.out.println("---FIN---");
                        break;
                    case 2:
                        Activite activite = null;
                        Date date;
                        MomentOfTheDay momentOfTheDay = null;


                        System.out.println("-------------- A remplir :");

                        // TODO Catalogue d'activité
                        List<Activite> activites = new ArrayList<>();

                        boolean found = false;
                        String acti;
                        while (!found) {
                            System.out.println("Activité :");
                            acti = input.nextLine();

                            for (Activite activite1 : activites) {
                                if (activite1.getDenomination().equals(acti)) {
                                    found = true;
                                    activite = activite1;
                                    break;
                                }
                            }
                        }

                        System.out.println("Date de l'activité :");
                        date = Saisie.readDate("dd/MM/yyyy");

                        while (momentOfTheDay == null) {
                            System.out.println("Moment de la journée " + MomentOfTheDay.all.toString() + " :");
                            momentOfTheDay = MomentOfTheDay.valueOf(input.nextLine());
                        }
                        Demande demande = new Demande(currentUser, date, momentOfTheDay, activite);
                        if (businessService.posterDemande(demande)) {
                            System.out.println("La demande a bien été postée");
                        } else {
                            System.out.println("Erreur lors de l'ajout de la demande");
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

        // View end
        stop();
    }

    private static void init() {
        JpaUtil.init();
        JpaUtil.creerEntityManager();
    }

    private static void stop() {
        JpaUtil.fermerEntityManager();
        JpaUtil.destroy();
    }


    private static int menuAuth() {

        int selection = -1;
        Scanner input = new Scanner(System.in);

        while (selection < 1 || selection > 3) {

            System.out.println("Menu d'accueil : ");
            System.out.println("-------------------------\n");
            System.out.println("1 - Se connecter");
            System.out.println("2 - S'inscrire");
            System.out.println("3 - Quitter");

            selection = input.nextInt();
        }
        return selection;
    }

    private static Adherent signup() {

        String nom;
        String prenom;
        String mail;
        String adresse;
        String password = "1";
        String passwordConfirmation = "2";
        Scanner input = new Scanner(System.in);

        System.out.println("Entrez votre nom :");
        nom = input.nextLine();

        System.out.println("Entrez votre prénom :");
        prenom = input.nextLine();

        System.out.println("Entrez votre mail :");
        mail = input.nextLine();

        System.out.println("Entrez votre adresse :");
        adresse = input.nextLine();


        while (!Objects.equals(password, passwordConfirmation)) {
            System.out.println("Entrez votre mot de passe :");
            password = String.valueOf(System.console().readPassword());

            System.out.println("Re-entrez votre mot de passe :");
            passwordConfirmation = String.valueOf(System.console().readPassword());
        }

        return new Adherent(nom, prenom, mail, adresse, password);
    }

    private static int menuAdherent() {

        int selection = -1;
        Scanner input = new Scanner(System.in);

        while (selection < 1 || selection > 3) {

            System.out.println("Menu Adhérent : ");
            System.out.println("-------------------------\n");
            System.out.println("1 - Consulter les demandes");
            System.out.println("2 - Poster une demande");
            System.out.println("3 - Se déconnecter et quitter");

            selection = input.nextInt();
        }
        return selection;
    }

    private static int menuResponsable() {


        int selection = -1;
        Scanner input = new Scanner(System.in);

        while (selection < 1 || selection > 3) {

            System.out.println("Menu Responsable : ");
            System.out.println("-------------------------\n");
            System.out.println("1 - Consulter les évènements");
            System.out.println("2 - Valider un évènement");
            System.out.println("3 - Se déconnecter et quitter");

            selection = input.nextInt();
        }
        return selection;
    }
}
