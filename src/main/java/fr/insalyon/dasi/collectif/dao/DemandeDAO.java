package fr.insalyon.dasi.collectif.dao;

import fr.insalyon.dasi.collectif.business.model.Activite;
import fr.insalyon.dasi.collectif.business.model.Adherent;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import fr.insalyon.dasi.collectif.business.model.Demande;
import fr.insalyon.dasi.collectif.business.model.MomentOfTheDay;

import java.util.Date;

public class DemandeDAO {
    
    public Demande findById(long id) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Demande demande = null;
        
        demande = em.find(Demande.class, id);

        return demande;
    }
    
    public boolean existsByValue(Demande demande) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        
        Query jpqlQuery = em.createQuery("SELECT d FROM Demande d WHERE d.adherent = :adherent AND d.wantedDate = :wantedDate AND d.moment = :moment AND d.activite = :activite");
        List results = jpqlQuery.setParameter("adherent", demande.getAdherent())
                                .setParameter("wantedDate", demande.getWantedDate())
                                .setParameter("moment", demande.getMoment())
                                .setParameter("activite", demande.getActivite())
                                .getResultList();
            
        return !results.isEmpty();
    }
    
    public List<Demande> findCandidatesForEvent(Date wantedDate, MomentOfTheDay moment, Activite activite) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        
        List<Demande> demandes = null;
        
        Query jpqlQuery = em.createQuery("SELECT d FROM Demande d WHERE d.wantedDate = :wantedDate AND d.moment = :moment AND d.activite = :activite AND d.evenement IS NULL");
        demandes = jpqlQuery.setParameter("wantedDate", wantedDate)
                            .setParameter("moment", moment)
                            .setParameter("activite", activite)
                            .getResultList();
        
        return demandes;
    }
    
    public List<Demande> findAll() throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Demande> demandes = null;
        
        Query q = em.createQuery("SELECT d FROM Demande d");
        demandes = (List<Demande>) q.getResultList();
        
        return demandes;
    }
    
    public void add(Demande demande) throws Exception {
        EntityManager em = JpaUtil.obtenirEntityManager();

        em.persist(demande);
    }
}
