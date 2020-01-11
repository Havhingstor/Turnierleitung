package de.pasch.turnierleitung.probierUndTestklassen;

import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.steuerung.Steuerung;

public class TK2 {

	public static void main(String[] args) {
		Steuerung s=new Steuerung();
		testmethode(s);
		for(Team t:s.getTeams()) {
			System.out.println(t.toString());
		}
	}
	
	public static void testmethode(Steuerung s) {
		s.addTeam("Test","TT","TTField");
	}

}
