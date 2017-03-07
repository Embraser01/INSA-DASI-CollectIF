/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.collectif.view;

import fr.insalyon.dasi.collectif.dao.ActiviteDAO;
import fr.insalyon.dasi.collectif.dao.AdherentDAO;
import fr.insalyon.dasi.collectif.dao.JpaUtil;
import fr.insalyon.dasi.collectif.dao.LieuDAO;
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
        ActiviteDAO activiteDAO = new ActiviteDAO();
        LieuDAO lieuDAO = new LieuDAO();
        
        try {
            System.out.println(adherentDAO.findAll());
            System.out.println(activiteDAO.findAll());
            System.out.println(lieuDAO.findAll());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        
        JpaUtil.fermerEntityManager();
        
        JpaUtil.destroy();
    }
}
