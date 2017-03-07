package fr.insalyon.dasi.collectif.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import fr.insalyon.dasi.collectif.job.model.Adherent;

public class AdherentDAO {
    
    public Adherent findById(long id) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Adherent adherent = null;
        try {
            adherent = em.find(Adherent.class, id);
        }
        catch(Exception e) {
            throw e;
        }
        return adherent;
    }
    
    public Adherent findByEmail(String email) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            Query jpqlQuery = em.createQuery("SELECT a FROM Adherent a WHERE a.mail = :email");
            List results = jpqlQuery.setParameter("email", email).getResultList();
            
            if (results.isEmpty()) {
                return null;
            }
            
            return (Adherent)results.get(0);
        }
        catch(Exception e) {
            throw e;
        }
    }
    
    public List<Adherent> findAll() throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Adherent> adherents = null;
        try {
            Query q = em.createQuery("SELECT a FROM Adherent a");
            adherents = (List<Adherent>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return adherents;
    }
    
    public void add(Adherent adherent) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        
        JpaUtil.ouvrirTransaction();
        try {
            em.persist(adherent);
            JpaUtil.validerTransaction();
        } catch(Exception e) {
            throw e;
        }
    }
}
