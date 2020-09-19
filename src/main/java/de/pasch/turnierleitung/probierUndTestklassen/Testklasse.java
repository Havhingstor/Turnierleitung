package de.pasch.turnierleitung.probierUndTestklassen;

import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Steuerung;

public final class Testklasse {
		
	public static void main(String[] args) {
		Steuerung steuerung=new Steuerung();
		steuerung.addTeam("FC Bayern München","FCB","Allianz-Arena");
		steuerung.addTeam("SV Werder Bremen","SVW","wohninvest Weserstadion");
                steuerung.getTeams().stream().map((Team team) -> {
                    System.out.println(team.toString());
                    return team;
                }).forEachOrdered((team) -> {
                System.out.println(team.getHeimstadion());
            });
		int[]ar= {1,2,3,4,5};
		steuerung.addLiga(3,1,0,"Testliga",ar);
		steuerung.addTeamZuLiga(1000001, 1000003);
		steuerung.addTeamZuLiga(1000002,1000003);
		steuerung.addSpieltag(steuerung.getLigen().get(0).getID(),"TestST");
		
		steuerung.addSpiel(1000001l, 1000002l, false, false, "", steuerung.getSpieltage().get(0).getID());
                steuerung.getSpiele().stream().map((spiel) -> {
                    System.out.println(spiel.abschließenUndGewinner());
                return spiel;
            }).map((spiel) -> {
                System.out.println(spiel.getStadion());
                return spiel;
            }).forEachOrdered((spiel) -> {
                System.out.println(spiel.getID());
            });
		steuerung.addTor(true, 0, 0,steuerung.getSpiele().get(0).getID(), 17, 0, 2);
                steuerung.getSpiele().stream().map((spiel) -> {
                    System.out.println(spiel.abschließenUndGewinner());
                return spiel;
            }).map((spiel) -> {
                System.out.println(spiel.getStadion());
                return spiel;
            }).map((spiel) -> {
                System.out.println(spiel.getID());
                return spiel;
            }).map((spiel) -> {
                System.out.println(spiel.getHeimtoreDirekt().get(0).getZeit());
                return spiel;
            }).forEachOrdered((spiel) -> {
                System.out.println(IDPicker.pick(steuerung.getTeams(),spiel.getGewinnerID()));
            });
		System.out.println(steuerung.getLigen().get(0).berechneGetAktuelleTabelle());
		steuerung.addTeam("1. FC Nürnberg","FCN","Max-Morlock-Stadion");
		steuerung.addTeamZuLiga(steuerung.getTeams().get(2).getID(), 1000003);
		System.out.println(steuerung.getLigen().get(0).berechneGetAktuelleTabelle());
                steuerung.getTeams().stream().map((team) -> {
                    System.out.println(team.toString());
                return team;
            }).forEachOrdered((team) -> {
                System.out.println(team.getHeimstadion());
            });
	}
}