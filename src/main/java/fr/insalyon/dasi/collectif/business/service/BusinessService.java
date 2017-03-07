/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.collectif.business.service;

import fr.insalyon.dasi.collectif.dao.AdherentDAO;
import fr.insalyon.dasi.collectif.job.model.Adherent;

/**
 *
 * @author tbourvon
 */
public class BusinessService {
    private AdherentDAO adherentDAO = new AdherentDAO();
    
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
    
    public boolean authLogin(String email, String password) {
        Adherent adherent = null;
        try {
            adherent = adherentDAO.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        if (adherent == null) {
            return false;
        }
        
        if (!adherent.getPassword().equals(password)) {
            return false;
        }
        
        return true;
    }
    
    public boolean posterDemande(String activite, Date date, String moment) {
        return false;
    }
    
    public List<Demande> consulterHistorique() {
        
    }
    
    public List<Evenement> consulterEvenements() {
        
    }
    
    public List<Geo> localiserParticipants() {
        
    }
    
    public boolean completerEvenement() {
        
    }
}
