/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.collectif.business.service;

import com.google.maps.model.LatLng;
import fr.insalyon.dasi.collectif.business.exception.*;
import fr.insalyon.dasi.collectif.business.model.*;
import fr.insalyon.dasi.collectif.dao.*;
import fr.insalyon.dasi.collectif.util.GeoTest;
import fr.insalyon.dasi.collectif.util.MailFactory;
import fr.insalyon.dasi.collectif.util.PasswordStorage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.RollbackException;


public class BusinessService {
    private AdherentDAO adherentDAO = new AdherentDAO();
    private ResponsableDAO responsableDAO = new ResponsableDAO();
    private DemandeDAO demandeDAO = new DemandeDAO();
    private ActiviteDAO activiteDAO = new ActiviteDAO();
    private EvenementDAO evenementDAO = new EvenementDAO();
    private LieuDAO lieuDAO = new LieuDAO();
    private TechnicalService technicalService = new TechnicalService();

    public Adherent authSignup(Adherent adherent) throws ServiceException, AdherentAlreadyExistsException {
        JpaUtil.creerEntityManager();

        try {
            JpaUtil.ouvrirTransaction();
            if (adherentDAO.findByEmail(adherent.getMail()) != null) {
                throw new AdherentAlreadyExistsException();
            }

            LatLng latlng = GeoTest.getLatLng(adherent.getAdresse());
            if (latlng != null) {
                adherent.setLatitudeLongitude(latlng.lat, latlng.lng);
            }

            adherent.setPassword(PasswordStorage.newStorageString(adherent.getPassword()));

            adherentDAO.add(adherent);

            JpaUtil.validerTransaction();

            technicalService.sendMail(
                    adherent.getMail(),
                    "Bienvenue chez Collect'IF",
                    MailFactory.makeMailFromSignup(adherent)
            );

            List<Responsable> responsables = responsableDAO.findAll();
            for (Responsable resp : responsables) {
                technicalService.sendMail(
                        resp.getMail(),
                        "Nouvel adhérent chez Collect'IF",
                        MailFactory.makeMailFromSignupForResp(adherent, resp)
                );
            }

        } catch (AdherentAlreadyExistsException e) {
            // On renvoie cette erreur à la vue
            throw e;
        } catch (Exception e) {
            JpaUtil.annulerTransaction();
            e.printStackTrace();

            technicalService.sendMail(
                    adherent.getMail(),
                    "Erreur lors de l'inscription chez Collect'IF",
                    MailFactory.makeMailFromSignupFailure(adherent)
            );

            throw new ServiceException();
        }

        JpaUtil.fermerEntityManager();
        return adherent;
    }

    public Adherent authLogin(String email, String password) throws ServiceException, AdherentNotFoundException {
        JpaUtil.creerEntityManager();

        Adherent adherent;
        try {
            adherent = adherentDAO.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException();
        }

        if (adherent != null) {
            if (!PasswordStorage.checkPassword(password, adherent.getPassword())) {
                adherent = null;
            }
        }

        if (adherent == null) {
            throw new AdherentNotFoundException();
        }

        JpaUtil.fermerEntityManager();
        return adherent;
    }

    public List<Lieu> consulterLieux() throws ServiceException {
        JpaUtil.creerEntityManager();
        List<Lieu> ret = null;
        try {
            ret = lieuDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException();
        }

        JpaUtil.fermerEntityManager();
        return ret;
    }

    public List<Activite> consulterActivites() throws ServiceException {
        JpaUtil.creerEntityManager();
        List<Activite> ret = null;
        try {
            ret = activiteDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException();
        }

        JpaUtil.fermerEntityManager();
        return ret;
    }

    public void posterDemande(Demande demande) throws DemandeBadDateException, DemandeAlreadyExistsException, ServiceException {
        JpaUtil.creerEntityManager();

        int dateCompare = demande.getWantedDate().compareTo(new Date());
        if (dateCompare == -1) {
            // Si la date est déjà passé
            // FIXME Vérifier également le moment de la journée !
            throw new DemandeBadDateException();
        }

        try {

            JpaUtil.ouvrirTransaction();
            if (demandeDAO.existsByValue(demande)) {
                JpaUtil.annulerTransaction();
                throw new DemandeAlreadyExistsException();
            }

            demandeDAO.add(demande);
            JpaUtil.validerTransaction();

            Evenement newEvenement;
            boolean retry;
            do {
                JpaUtil.ouvrirTransaction();
                List<Demande> demandesCandidates = demandeDAO.findCandidatesForEvent(demande.getWantedDate(), demande.getMoment(), demande.getActivite());
                if (demandesCandidates.size() >= demande.getActivite().getNbParticipants()) {

                    // On enlève le surplus de demandes
                    demandesCandidates = demandesCandidates.subList(0, demande.getActivite().getNbParticipants());

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


                    for (Adherent adherent : participants) {
                        adherent.addEvent(newEvenement);
                    }
                }
                try {
                    JpaUtil.validerTransaction();
                    retry = false;
                } catch (RollbackException e) {
                    // OptimisticLockException
                    JpaUtil.annulerTransaction();
                    retry = true;
                }

            } while (retry);

        } catch (DemandeAlreadyExistsException e) {
            throw e;
        } catch (Exception e) {
            JpaUtil.annulerTransaction();
            e.printStackTrace();
            throw new ServiceException();
        }

        JpaUtil.fermerEntityManager();
    }

    public List<Demande> consulterHistorique(Adherent adherent) throws ServiceException {
        JpaUtil.creerEntityManager();
        List<Demande> ret;
        try {
            ret = demandeDAO.findAllByUser(adherent);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException();
        }

        JpaUtil.fermerEntityManager();
        return ret;
    }

    public List<Evenement> consulterEvenements() throws ServiceException {
        JpaUtil.creerEntityManager();
        List<Evenement> ret;
        try {
            ret = evenementDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException();
        }

        JpaUtil.fermerEntityManager();
        return ret;
    }

    public void completerEvenement(Evenement evenement) throws ServiceException, EvenementNotFoundException {
        JpaUtil.creerEntityManager();

        try {
            evenementDAO.findById(evenement.getId());
        } catch (Exception e) {
            throw new EvenementNotFoundException();
        }

        JpaUtil.ouvrirTransaction();
        try {
            evenementDAO.update(evenement);
            JpaUtil.validerTransaction();

            // Send mail to each user

            for (Adherent ad : evenement.getAdherents()) {
                technicalService.sendMail(
                        ad.getMail(),
                        "Nouvel Evènement Collect'IF",
                        MailFactory.makeMailFromEvent(evenement, ad)
                );
            }
        } catch (Exception e) {
            JpaUtil.annulerTransaction();
            e.printStackTrace();
            throw new ServiceException();
        }

        JpaUtil.fermerEntityManager();
    }
}
