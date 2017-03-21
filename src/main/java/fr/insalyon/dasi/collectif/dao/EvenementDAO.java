package fr.insalyon.dasi.collectif.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import fr.insalyon.dasi.collectif.business.model.Evenement;

public class EvenementDAO {
    
    public Evenement findById(long id) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Evenement evenement = null;
        
        evenement = em.find(Evenement.class, id);

        // Load adhérents
        evenement.getAdherents().size();
        return evenement;
    }
    
    @SuppressWarnings("unchecked")
    public List<Evenement> findAll() throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Evenement> evenements = null;
        
        Query q = em.createQuery("SELECT e FROM Evenement e ORDER BY e.eventDate DESC");
        evenements = (List<Evenement>) q.getResultList();

        return evenements;
    }
    
    public void add(Evenement evenement) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.persist(evenement);
    }
    
    public void update(Evenement evenement) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();

        em.merge(evenement);
    }
}
