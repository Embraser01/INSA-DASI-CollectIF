package fr.insalyon.dasi.collectif.dao;

import fr.insalyon.dasi.collectif.business.model.Activite;
import fr.insalyon.dasi.collectif.business.model.Adherent;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import fr.insalyon.dasi.collectif.business.model.Demande;
import java.util.Date;

public class DemandeDAO {
    
    public Demande findById(long id) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Demande demande = null;
        try {
            demande = em.find(Demande.class, id);
        }
        catch(Exception e) {
            throw e;
        }
        return demande;
    }
    
    public Demande find(Adherent adherent, Date date, String moment, Activite activite) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        
        Query jpqlQuery = em.createQuery("SELECT d FROM Demande d WHERE d.adherent = :adherent AND d.wantedDate = :wantedDate AND d.moment = :moment AND d.activite = :activite");
        List results = jpqlQuery.setParameter("adherent", adherent)
                                .setParameter("wantedDate", date)
                                .setParameter("moment", moment)
                                .setParameter("activite", activite)
                                .getResultList();
            
        if (results.isEmpty()) {
            return null;
        }
        
        return (Demande)results.get(0);
    }
    
    public List<Demande> findAll() throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Demande> demandes = null;
        try {
            Query q = em.createQuery("SELECT d FROM Demande d");
            demandes = (List<Demande>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return demandes;
    }
    
    public void add(Demande demande) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        
        JpaUtil.ouvrirTransaction();
        try {
            em.persist(demande);
            JpaUtil.validerTransaction();
        } catch(Exception e) {
            throw e;
        }
    }
}
