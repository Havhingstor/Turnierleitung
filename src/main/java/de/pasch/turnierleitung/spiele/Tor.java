package de.pasch.turnierleitung.spiele;

import java.util.ArrayList;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.steuerung.ArraySpeicher;
import de.pasch.turnierleitung.steuerung.IDPicker;

public class Tor extends Spielaktivitaet {
	
	private long teamID;
	private long vorbereiterID;
	
	public Tor(int zeit,int nachspielzeit,Spieler torschuetze,Spieler vorbereiter,Team team,long ID,
			ArrayList<String>typen,ArraySpeicher as) {
		super(zeit,nachspielzeit,torschuetze,ID,typen,as);
		if(vorbereiter!=null) {
			vorbereiterID=vorbereiter.getID();
		}
		teamID=team.getID();
	}
	
	public void setTyp(int index) {
		typ=typen.get(index);
	}
	
	public String getTyp() {
		return typ;
	}
	
	public void setTyp(String typ) {
		if (typen.contains(typ)) {
			this.typ=typ;
		}else {
			throw new IllegalArgumentException("Dieses Element ist nicht vorhanden!");
		}
	}
	
	public long getVorbereiterID() {
		return vorbereiterID;
	}
	
	public Spieler getVorbereiter() {
		return IDPicker.pick(as.spieler,vorbereiterID);
	}
	
	public void setVorbereiterID(long ID) {
		this.vorbereiterID=ID;
	}
	
	public void setVorbereiter(Spieler spieler) {
		this.vorbereiterID=spieler.getID();
	}
	
	public void setTeam(Team team) {
		teamID=team.getID();
	}
	
	public void setTeam(long teamID) {
		this.teamID=teamID;
	}
	
	public long getTeamID() {
		return teamID;
	}
	
	public Team getTeam() {
		return IDPicker.pick(as.teams, teamID);
	}
}
