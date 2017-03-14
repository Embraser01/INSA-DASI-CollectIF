package fr.insalyon.dasi.collectif.business.model;


import java.util.EnumSet;

@SuppressWarnings("unused")
public enum MomentOfTheDay {
    morning("Matin"),
    afternoon("Après-midi"),
    evening("Soir"),
    night("Nuit");

    private final String name;
    public static EnumSet<MomentOfTheDay> all = EnumSet.allOf(MomentOfTheDay.class);

    MomentOfTheDay(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
