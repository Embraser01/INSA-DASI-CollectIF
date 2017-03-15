/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.collectif.business.service;

import com.google.maps.model.LatLng;
import fr.insalyon.dasi.collectif.dao.ActiviteDAO;
import fr.insalyon.dasi.collectif.dao.AdherentDAO;
import fr.insalyon.dasi.collectif.dao.DemandeDAO;
import fr.insalyon.dasi.collectif.dao.EvenementDAO;
import fr.insalyon.dasi.collectif.business.model.Activite;
import fr.insalyon.dasi.collectif.business.model.Adherent;
import fr.insalyon.dasi.collectif.business.model.Demande;
import fr.insalyon.dasi.collectif.business.model.Evenement;
import fr.insalyon.dasi.collectif.business.model.EvenementGratuit;
import fr.insalyon.dasi.collectif.business.model.EvenementPayant;
import fr.insalyon.dasi.collectif.business.model.Lieu;
import fr.insalyon.dasi.collectif.dao.JpaUtil;
import fr.insalyon.dasi.collectif.dao.LieuDAO;
import fr.insalyon.dasi.collectif.util.GeoTest;
import fr.insalyon.dasi.collectif.util.MailFactory;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.OptimisticLockException;

/**
 * @author tbourvon
 */
public class BusinessService {
    private AdherentDAO adherentDAO = new AdherentDAO();
    private DemandeDAO demandeDAO = new DemandeDAO();
    private ActiviteDAO activiteDAO = new ActiviteDAO();
    private EvenementDAO evenementDAO = new EvenementDAO();
    private LieuDAO lieuDAO = new LieuDAO();
    private TechnicalService technicalService = new TechnicalService();

    public Adherent authSignup(Adherent adherent) {
        JpaUtil.creerEntityManager();

        Adherent ret;
        try {
            JpaUtil.ouvrirTransaction();
            if (adherentDAO.findByEmail(adherent.getMail()) != null) {
                return null;
            }

            LatLng latlng = GeoTest.getLatLng(adherent.getAdresse());
            if (latlng != null) {
                adherent.setLatitudeLongitude(latlng.lat, latlng.lng);
            }

            adherentDAO.add(adherent);

            JpaUtil.validerTransaction();

            ret = adherent;

            technicalService.sendMail(
                    adherent.getMail(),
                    "Bienvenue chez Collect'IF",
                    MailFactory.makeMailFromSignup(adherent)
            );

        } catch (Exception e) {
            JpaUtil.annulerTransaction();
            e.printStackTrace();
            ret = null;
        }

        JpaUtil.fermerEntityManager();
        return ret;
    }

    public Adherent authLogin(String email, String password) {
        JpaUtil.creerEntityManager();

        Adherent adherent;
        try {
            adherent = adherentDAO.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            adherent = null;
        }

        if (adherent != null) {
            if (!adherent.getPassword().equals(password)) {
                adherent = null;
            }
        }

        JpaUtil.fermerEntityManager();
        return adherent;
    }

    public List<Lieu> consulterLieux() {
        JpaUtil.creerEntityManager();
        List<Lieu> ret = null;
        try {
            ret = lieuDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            ret = null;
        }

        JpaUtil.fermerEntityManager();
        return ret;
    }

    public List<Activite> consulterActivites() {
        JpaUtil.creerEntityManager();
        List<Activite> ret = null;
        try {
            ret = activiteDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            ret = null;
        }

        JpaUtil.fermerEntityManager();
        return ret;
    }

    public boolean posterDemande(Demande demande) {
        JpaUtil.creerEntityManager();

        // TODO Vérifier qu'il n'a pas déjà posté la demande et que la date > now()
        boolean ret = false;
        try {
            JpaUtil.ouvrirTransaction();
            if (demandeDAO.existsByValue(demande)) {
                JpaUtil.annulerTransaction();
                ret = false;
            } else {
                demandeDAO.add(demande);
                JpaUtil.validerTransaction();

                Evenement newEvenement;
                boolean retry;
                do {
                    newEvenement = null;
                    JpaUtil.ouvrirTransaction();
                    List<Demande> demandesCandidates = demandeDAO.findCandidatesForEvent(demande.getWantedDate(), demande.getMoment(), demande.getActivite());
                    if (demandesCandidates.size() >= demande.getActivite().getNbParticipants()) {

                        List<Adherent> participants = new ArrayList<>(demandesCandidates.size());

                        for (Demande d :
                                demandesCandidates) {
                            participants.add(d.getAdherent());
                        }

                        if (demande.getActivite().getPayant()) {
                            newEvenement = new EvenementPayant(
                                    demande.getWantedDate(),
                                    demande.getMoment(),
                                    participants,
                                    demande.getActivite(),
                                    null,
                                    null
                            );
                        } else {
                            newEvenement = new EvenementGratuit(
                                    demande.getWantedDate(),
                                    demande.getMoment(),
                                    participants,
                                    demande.getActivite(),
                                    null
                            );
                        }

                        for (Demande d : demandesCandidates) {
                            d.setEvenement(newEvenement);
                        }

                        evenementDAO.add(newEvenement);
                    }
                    try {
                        JpaUtil.validerTransaction();
                        retry = false;
                    } catch (OptimisticLockException e) {
                        JpaUtil.annulerTransaction();
                        retry = true;
                    }
                } while (retry);


                // Send mail to each user

                if (newEvenement != null) {
                    for (Adherent ad : newEvenement.getAdherents()) {
                        technicalService.sendMail(
                                ad.getMail(),
                                "Nouvel Evènement Collect'IF",
                                MailFactory.makeMailFromEvent(newEvenement, ad)
                        );
                    }
                }

                ret = true;
            }
        } catch (Exception e) {
            JpaUtil.annulerTransaction();
            e.printStackTrace();
            ret = false;
        }

        JpaUtil.fermerEntityManager();
        return ret;
    }

    public List<Demande> consulterHistorique(Adherent adherent) {
        JpaUtil.creerEntityManager();
        List<Demande> ret = null;
        try {
            ret = demandeDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            ret = null;
        }

        JpaUtil.fermerEntityManager();
        return ret;
    }

    public List<Evenement> consulterEvenements() {
        JpaUtil.creerEntityManager();
        List<Evenement> ret = null;
        try {
            ret = evenementDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            ret = null;
        }

        JpaUtil.fermerEntityManager();
        return ret;
    }

    public boolean completerEvenement(Evenement evenement) {
        JpaUtil.creerEntityManager();

        JpaUtil.ouvrirTransaction();
        boolean ret = false;
        try {
            evenementDAO.update(evenement);
            JpaUtil.validerTransaction();
            ret = true;
        } catch (Exception e) {
            JpaUtil.annulerTransaction();
            e.printStackTrace();
            ret = false;
        }

        JpaUtil.fermerEntityManager();
        return ret;
    }
}
