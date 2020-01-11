package de.pasch.turnierleitung.protagonisten;

import de.pasch.turnierleitung.steuerung.ArraySpeicher;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Pickable;

public class SpielerTeamConnector implements Pickable{
	
	public long ID=0;
	public long teamID=0;
	public long spielerID=0;
	private int trikotnummer=0;
	private boolean ausgetreten=false;
	private ArraySpeicher as=null;

	public SpielerTeamConnector(long spielerID,long teamID,int trikotnummer,long ID,ArraySpeicher as) {
		this.spielerID=spielerID;
		this.teamID=teamID;
		this.trikotnummer=trikotnummer;
		this.as=as;
	}
	
	public void setTrikotnummer(int trikotnummer) {
		this.trikotnummer=trikotnummer;
	}
		

	public Spieler getSpieler() {
		return IDPicker.pick(as.spieler,spielerID);
	}
	
	public Team getTeam() {
		return IDPicker.pick(as.teams,teamID);
	}
	
	public long getTeamID() {
		return teamID;
	}
	
	public long getSpielerID() {
		return spielerID;
	}
	
	public int getTrikotnummer() {
		return trikotnummer;
	}
		
	public long getID() {
		return ID;
	}
	
	public void setAusgetreten(boolean wahr) {
		ausgetreten=wahr;
	}
	
	public boolean getAusgetreten() {
		return ausgetreten;
	}
}
