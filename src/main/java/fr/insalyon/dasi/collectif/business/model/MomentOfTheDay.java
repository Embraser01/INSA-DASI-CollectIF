package fr.insalyon.dasi.collectif.business.model;


import java.util.EnumSet;

@SuppressWarnings("unused")
public enum MomentOfTheDay {
    morning("Matin"),
    afternoon("Après-midi"),
    evening("Soir");

    private final String name;
    public static EnumSet<MomentOfTheDay> all = EnumSet.allOf(MomentOfTheDay.class);

    MomentOfTheDay(String s) {
        name = s;
    }

    public static MomentOfTheDay find(String moment) {
        for (MomentOfTheDay m :
                MomentOfTheDay.all) {
            if (m.toString().equals(moment)) {
                return m;
            }
        }
        return null;
    }

    public String toString() {
        return this.name;
    }
}
