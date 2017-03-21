package fr.insalyon.dasi.collectif.dao;

import fr.insalyon.dasi.collectif.business.model.Responsable;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class ResponsableDAO {


    public List<Responsable> findAll() throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();

        List<Responsable> responsables = null;
        Query q = em.createQuery("SELECT a FROM Responsable a");
        responsables = (List<Responsable>) q.getResultList();

        return responsables;
    }
}
