/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.collectif.business.service;

import fr.insalyon.dasi.collectif.dao.ActiviteDAO;
import fr.insalyon.dasi.collectif.dao.AdherentDAO;
import fr.insalyon.dasi.collectif.dao.DemandeDAO;
import fr.insalyon.dasi.collectif.job.model.Activite;
import fr.insalyon.dasi.collectif.job.model.Adherent;
import fr.insalyon.dasi.collectif.job.model.Demande;
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
    
    public boolean authSignup(String name,
                              String firstname, 
                              String address, 
                              String email,
                              String password) {
        try {
            if (adherentDAO.findByEmail(email) != null) {
                return false;
            }
            
            adherentDAO.add(new Adherent(name, firstname, email, address, password));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
    
    public boolean posterDemande(Adherent adherent, Activite activite, Date date, String moment) {
        try {
            
            if (demandeDAO.find(adherent, date, moment, activite) != null) {
                return false;
            }
        
            demandeDAO.add(new Demande(adherent, date, moment, activite));
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
        
    }

    public boolean completerEvenement() {
        
    }
}
