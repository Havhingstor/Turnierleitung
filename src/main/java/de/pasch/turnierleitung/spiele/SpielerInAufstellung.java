package de.pasch.turnierleitung.spiele;

import java.util.ArrayList;

import org.jdom2.Element;

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
	
	public void createXMLElements(Element parEl) {
		Element sia=new Element("SIA");
		parEl.addContent(sia);
		
		Element spielerEl=new Element("Spieler");
		sia.addContent(spielerEl);
		spielerEl.addContent(""+spielerID);
		
		Element eingewEl=new Element("Eingewechselt");
		sia.addContent(eingewEl);
		eingewEl.addContent(""+eingewechselt);
		
		Element ausgewEl=new Element("Ausgewechselt");
		sia.addContent(ausgewEl);
		ausgewEl.addContent(""+ausgewechselt);
		
		Element eingewZeitEl=new Element("EingewechseltZeit");
		sia.addContent(eingewZeitEl);
		eingewZeitEl.addContent(""+eingewechseltZeit);
		
		Element ausgewZeitEl=new Element("AusgewechseltZeit");
		sia.addContent(ausgewZeitEl);
		ausgewZeitEl.addContent(""+ausgewechseltZeit);
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
