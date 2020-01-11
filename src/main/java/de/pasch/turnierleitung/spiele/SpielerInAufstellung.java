package de.pasch.turnierleitung.spiele;

import java.util.ArrayList;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.steuerung.IDPicker;

public class SpielerInAufstellung {
	private long spielerID;
	private boolean eingewechselt=false;
	private boolean ausgewechselt=false;	
	private int eingewechseltZeit=0;
	private int ausgewechseltZeit=0;
	
	protected SpielerInAufstellung(long spielerID) {
		this.spielerID=spielerID;
	}
	
	public long getSpielerID() {
		return spielerID;
	}
	
	public Spieler getSpieler(ArrayList<Spieler>liste) {
		return IDPicker.pick(liste,spielerID);
	}
	
	public int getSpielzeit() {
		return(ausgewechseltZeit-eingewechseltZeit);
	}
	
	public void setEingewechseltZeit(int zeit) {
		this.eingewechseltZeit=zeit;
	}
	
	public void setAusgewechseltZeit(int zeit) {
		this.ausgewechseltZeit=zeit;
	}
	
	public int getEingewechseltZeit() {
		return eingewechseltZeit;
	}
	
	public int getAusgewechseltZeit() {
		return ausgewechseltZeit;
	}
	
	public void setEingewechselt(boolean wahr) {
		eingewechselt=wahr;
	}
	
	public void setAusgewechselt(boolean wahr) {
		ausgewechselt=wahr;
	}
	
	public boolean getAusgewechselt() {
		return ausgewechselt;
	}
	
	public boolean getEingewechselt() {
		return eingewechselt;
	}
}
