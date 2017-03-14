package fr.insalyon.dasi.collectif.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import fr.insalyon.dasi.collectif.business.model.Evenement;

public class EvenementDAO {
    
    public Evenement findById(long id) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Evenement evenement = null;
        try {
            evenement = em.find(Evenement.class, id);
        }
        catch(Exception e) {
            throw e;
        }
        return evenement;
    }
    
    public List<Evenement> findAll() throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Evenement> evenements = null;
        try {
            Query q = em.createQuery("SELECT e FROM Evenement e");
            evenements = (List<Evenement>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return evenements;
    }
    
    public void add(Evenement evenement) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        
        JpaUtil.ouvrirTransaction();
        try {
            em.persist(evenement);
            JpaUtil.validerTransaction();
        }
        catch(Exception e) {
            JpaUtil.annulerTransaction();
            throw e;
        }
    }
    
    public void update(Evenement evenement) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        
        JpaUtil.ouvrirTransaction();
        try {
            em.merge(evenement);
            JpaUtil.validerTransaction();
        }
        catch(Exception e) {
            JpaUtil.annulerTransaction();
            throw e;
        }
    }
}
