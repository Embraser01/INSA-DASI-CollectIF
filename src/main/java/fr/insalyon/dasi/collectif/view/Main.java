/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.collectif.view;

import fr.insalyon.dasi.collectif.dao.AdherentDAO;
import fr.insalyon.dasi.collectif.dao.JpaUtil;
import fr.insalyon.dasi.collectif.job.model.Adherent;
import java.util.Arrays;

/**
 *
 * @author tbourvon
 */
public class Main {
    public static void main(String[] args) {
        JpaUtil.init();
        
        JpaUtil.creerEntityManager();
    
        AdherentDAO adherentDAO = new AdherentDAO();
        Adherent adherent = new Adherent("Bourvon", "Tristan", "tristan.bourvon@insa-lyon.fr", "blablabla");
        
        try {
            adherentDAO.add(adherent);

            System.out.println(adherentDAO.findAll());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        
        JpaUtil.fermerEntityManager();
        
        JpaUtil.destroy();
    }
}
