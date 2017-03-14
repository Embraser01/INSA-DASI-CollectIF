package fr.insalyon.dasi.collectif.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import fr.insalyon.dasi.collectif.business.model.Activite;

public class ActiviteDAO {
    
    public Activite findById(long id) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Activite activite = null;
        try {
            activite = em.find(Activite.class, id);
        }
        catch(Exception e) {
            throw e;
        }
        return activite;
    }
    
    public Activite findByDenomination(String denomination) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            Query jpqlQuery = em.createQuery("SELECT a FROM Activite a WHERE a.denomination = :denomination");
            List results = jpqlQuery.setParameter("denomination", denomination).getResultList();
            
            if (results.isEmpty()) {
                return null;
            }
            
            return (Activite)results.get(0);
        }
        catch(Exception e) {
            throw e;
        }
    }
    
    public List<Activite> findAll() throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Activite> activites = null;
        try {
            Query q = em.createQuery("SELECT a FROM Activite a");
            activites = (List<Activite>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return activites;
    }
}
