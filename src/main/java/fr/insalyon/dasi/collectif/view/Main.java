/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.collectif.view;

import fr.insalyon.dasi.collectif.business.exception.*;
import fr.insalyon.dasi.collectif.business.model.*;
import fr.insalyon.dasi.collectif.business.service.BusinessService;
import fr.insalyon.dasi.collectif.dao.JpaUtil;
import fr.insalyon.dasi.collectif.util.Saisie;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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

                    try {
                        currentUser = businessService.authLogin(email, password);
                        System.out.println("Vous êtes bien connecté");
                        // On supprime de la mémoire le mot de passe...
                        password = "";

                        if (currentUser instanceof Responsable) {
                            doResp((Responsable) currentUser, businessService);
                        } else {
                            doAdher(currentUser, businessService);
                        }
                    } catch (ServiceException e) {
                        System.out.println("Erreur serveur... Re-essayez plus tard");
                    } catch (AdherentNotFoundException e) {
                        System.out.println("Mauvaise combinaison mail/mot de passe");
                    }
                    break;
                case 2:
                case 3:
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

                    if (menuSelection == 2) {
                        currentUser = new Adherent(nom, prenom, mail, adresse, signupPassword);
                    } else {
                        currentUser = new Responsable(nom, prenom, mail, adresse, signupPassword, "Responsable");
                    }

                    try {
                        if (menuSelection == 2) {
                            currentUser = businessService.authSignup(currentUser);
                            System.out.println("Vous êtes bien inscrits");
                            doAdher(currentUser, businessService);
                        } else {
                            currentUser = businessService.creerResponsable((Responsable) currentUser);
                            System.out.println("Un responsable a bien été créé");
                            doResp((Responsable) currentUser, businessService);
                        }
                        
                    } catch (ServiceException e) {
                        System.out.println("Erreur serveur... Re-essayez plus tard");
                    } catch (AdherentAlreadyExistsException e) {
                        System.out.println("Cet utilisateur existe déjà");
                    }
                    break;
                case 4:
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
                    try {
                        evenements = businessService.consulterEvenements();
                        System.out.println("Liste des évènements :");
                        for (Evenement evenement :
                                evenements) {
                            System.out.println(evenement);
                        }
                        System.out.println("---FIN---");

                    } catch (ServiceException e) {
                        System.out.println("Erreur serveur... Re-essayez plus tard");
                    }
                    break;
                case 2:
                    long wantedID = Saisie.readLong("ID de l'évènement à compléter :");

                    ev = null;
                    try {
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
                            // TODO Calcule moyenne

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

                            businessService.completerEvenement(ev);
                            System.out.println("L'évènement a bien été complété");
                        }

                    } catch (ServiceException e) {
                        System.out.println("Erreur serveur... Re-essayez plus tard");
                    } catch (EvenementNotFoundException e) {
                        System.out.println("Cet événement n'éxiste pas");
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
                    try {
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

                    } catch (ServiceException e) {
                        System.out.println("Erreur serveur... Re-essayez plus tard");
                    }
                    break;
                case 2:
                    try {
                        List<Activite> activites = businessService.consulterActivites();

                        Activite activite = null;
                        Date date;
                        MomentOfTheDay momentOfTheDay = null;

                        System.out.println("-------------- A remplir :");

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

                        businessService.posterDemande(demande);
                        System.out.println("La demande a bien été postée");

                    } catch (ServiceException e) {
                        System.out.println("Erreur serveur... Re-essayez plus tard");
                    } catch (DemandeBadDateException e) {
                        System.out.println("Erreur dans la demande, la date est déjà passée !");
                    } catch (DemandeAlreadyExistsException e) {
                        System.out.println("Vous avez déjà fait cette demande");
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
        System.out.println("3 - Créer un responsable");
        System.out.println("4 - Quitter");

        return Saisie.readInteger("Selection :", 1, 2, 3, 4);
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
