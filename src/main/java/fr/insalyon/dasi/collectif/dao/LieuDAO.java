package fr.insalyon.dasi.collectif.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import fr.insalyon.dasi.collectif.business.model.Lieu;

public class LieuDAO {
    
    public Lieu findById(long id) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Lieu lieu = null;

        lieu = em.find(Lieu.class, id);

        return lieu;
    }
    
    public List<Lieu> findAll() throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Lieu> lieux = null;
        
        Query q = em.createQuery("SELECT l FROM Lieu l");
        lieux = (List<Lieu>) q.getResultList();
        
        return lieux;
    }
}
