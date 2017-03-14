/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.collectif.view;

import fr.insalyon.dasi.collectif.business.model.Adherent;
import fr.insalyon.dasi.collectif.business.model.Demande;
import fr.insalyon.dasi.collectif.business.model.Evenement;
import fr.insalyon.dasi.collectif.business.model.Responsable;
import fr.insalyon.dasi.collectif.business.service.BusinessService;
import fr.insalyon.dasi.collectif.dao.JpaUtil;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

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

        // Connexion de l'utilisateur

        while (currentUser == null) {

            menuSelection = menuAuth();

            switch (menuSelection) {
                case 1:

                    String password;
                    String email;
                    Scanner input = new Scanner(System.in);

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
            menuSelection = menuResponsable();
            switch (menuSelection) {
                case 1:
                    List<Evenement> evenements = businessService.consulterEvenements();

                    for (Evenement ev :
                            evenements) {
                        System.out.println(ev);
                    }

                    break;
                case 2:
                    break;
                case 3:
                    stop();
                    return;
                default:
                    break;
            }

        } else {
            // TODO Adhérent

            menuSelection = menuAdherent();
            switch (menuSelection) {
                case 1:
                    List<Demande> demandes = businessService.consulterHistorique(currentUser);

                    for (Demande demande :
                            demandes) {
                        System.out.println(demande);
                    }
                    break;
                case 2:
                    break;
                case 3:
                    stop();
                    return;
                default:
                    break;
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
