/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.collectif.business.service;

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
import java.util.Date;
import java.util.List;

/**
 *
 * @author tbourvon
 */
public class BusinessService {
    private AdherentDAO adherentDAO = new AdherentDAO();
    private DemandeDAO demandeDAO = new DemandeDAO();
    private ActiviteDAO activiteDAO = new ActiviteDAO();
    private EvenementDAO evenementDAO = new EvenementDAO();
    
    public Adherent authSignup(Adherent adherent) {
        try {
            if (adherentDAO.findByEmail(adherent.getMail()) != null) {
                return null;
            }
            
            adherentDAO.add(adherent);
            return adherent;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Adherent authLogin(String email, String password) {
        Adherent adherent = null;
        try {
            adherent = adherentDAO.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        if (adherent == null) {
            return null;
        }
        
        if (!adherent.getPassword().equals(password)) {
            return null;
        }
        
        return adherent;
    }
    
    public boolean posterDemande(Demande demande) {
        try {
            if (demandeDAO.existsByValue(demande)) {
                return false;
            }
        
            demandeDAO.add(demande);
            
            List<Demande> demandesCandidates = demandeDAO.findCandidatesForEvent(demande.getWantedDate(), demande.getMoment(), demande.getActivite());
            if (demandesCandidates.size() >= demande.getActivite().getNbParticipants()) {
                Evenement newEvenement;
                if (demande.getActivite().getPayant()) {
                    newEvenement = new EvenementPayant(demande.getWantedDate(), demande.getMoment());
                } else {
                    newEvenement = new EvenementGratuit(demande.getWantedDate(), demande.getMoment());
                }
                
                evenementDAO.add(newEvenement);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Demande> consulterHistorique(Adherent adherent) {
        try {
            return demandeDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Evenement> consulterEvenements() {
        try {
            return evenementDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean completerEvenement(Evenement evenement) {
        try {
            evenementDAO.update(evenement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
