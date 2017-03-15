package fr.insalyon.dasi.collectif.util;

import com.google.maps.model.LatLng;
import fr.insalyon.dasi.collectif.business.model.Adherent;
import fr.insalyon.dasi.collectif.business.model.Evenement;
import fr.insalyon.dasi.collectif.business.model.EvenementPayant;

import java.text.DateFormat;
import java.util.Locale;


public class MailFactory {

    public static String makeMailFromEvent(Evenement evenement, Adherent sendTo) {

        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.FRANCE);

        StringBuilder content = new StringBuilder("Bonjour %s,\n\n " +
                "        Comme vous l'aviez souhaité, COLLECT'IF organise un évènement de %s le %s." +
                " Vous trouverez ci-dessous les détails de cet évènement.\n\n" +
                "        Associativement vôtre,\n\n" +
                "                Le Reponsable de l'Association\n\n\n" +
                "Evènement : %s\n" +
                "Date : %s\n" +
                "Lieu : %s, %s\n" +
                "(à %f km de chez vous)\n");

        if (evenement.getActivite().getPayant()) {
            content
                    .append("Prix : ")
                    .append(((EvenementPayant) evenement).getPaf())
                    .append(" €\n");
        }

        content.append("\nVous jouerez avec :\n");

        for (Adherent adh :
                evenement.getAdherents()) {
            if (adh != sendTo) {
                content
                        .append("        ")
                        .append(adh.getPrenom())
                        .append(" ")
                        .append(adh.getNom())
                        .append("\n");
            }
        }

        return String.format(content.toString(),
                sendTo.getPrenom(),
                evenement.getActivite().getDenomination(),
                df.format(evenement.getEventDate()),
                evenement.getActivite().getDenomination(),
                df.format(evenement.getEventDate()),
                evenement.getLieu().getDenomination(),
                evenement.getLieu().getAdresse(),
                GeoTest.getFlightDistanceInKm(
                        new LatLng(sendTo.getLatitude(), sendTo.getLongitude()),
                        new LatLng(evenement.getLieu().getLatitude(), evenement.getLieu().getLongitude())
                )
        );
    }

    public static String makeMailFromSignup(Adherent newUser) {
        String content = "Bonjour %s,\n\n " +
                "        Nous vous confirmons votre adhésion à l'association COLLECT'IF. Votre numéro d'adhérent est : %d.";

        return String.format(content, newUser.getPrenom(), newUser.getId());
    }
}
