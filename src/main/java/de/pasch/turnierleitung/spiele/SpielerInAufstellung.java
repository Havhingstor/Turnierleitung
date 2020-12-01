package de.pasch.turnierleitung.spiele;

import java.util.ArrayList;

import org.jdom2.Element;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.steuerung.IDPicker;

public class SpielerInAufstellung {
	private long spielerID;
	private boolean eingewechselt=false;
	private boolean ausgewechselt=false;	
	private boolean inStartaufstellung;
	private int eingewechseltZeit=0;
	private int eingewechseltNZeit=0;
	private int ausgewechseltZeit=0;
	private int ausgewechseltNZeit=0;
	
	protected SpielerInAufstellung(long spielerID,boolean inStartaufstellung) {
		this.spielerID=spielerID;
		this.inStartaufstellung=inStartaufstellung;
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
		
		Element startelfEl=new Element("Startelf");
		sia.addContent(startelfEl);
		startelfEl.addContent(""+inStartaufstellung);
		
		Element eingewZeitEl=new Element("EingewechseltZeit");
		sia.addContent(eingewZeitEl);
		eingewZeitEl.addContent(""+eingewechseltZeit);
		
		Element eingewNZeitEl=new Element("EingewechseltNZeit");
		sia.addContent(eingewNZeitEl);
		eingewNZeitEl.addContent(""+eingewechseltNZeit);
		
		Element ausgewZeitEl=new Element("AusgewechseltZeit");
		sia.addContent(ausgewZeitEl);
		ausgewZeitEl.addContent(""+ausgewechseltZeit);
		
		Element ausgewNZeitEl=new Element("AusgewechseltNZeit");
		sia.addContent(ausgewNZeitEl);
		ausgewNZeitEl.addContent(""+ausgewechseltNZeit);
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
	
	public void setEingewechseltNZeit(int zeit) {
		this.eingewechseltNZeit=zeit;
	}
	
	public void setAusgewechseltNZeit(int zeit) {
		this.ausgewechseltNZeit=zeit;
	}
	
	public int getEingewechseltNZeit() {
		return eingewechseltNZeit;
	}
	
	public int getAusgewechseltNZeit() {
		return ausgewechseltNZeit;
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
	
	public boolean istAufPlatz(int zeit, int nachspielzeit) {
		boolean vorherEingewechselt=false;
		boolean nichtVorherAusgewechselt=true;
		
		if(eingewechselt) {
			vorherEingewechselt=true;
			if(eingewechseltZeit>zeit) {
				vorherEingewechselt=false;
			}else if(eingewechseltZeit==zeit&&eingewechseltNZeit>nachspielzeit) {
				vorherEingewechselt=false;
			}
		}
		if(ausgewechselt) {
			nichtVorherAusgewechselt=false;
			if(ausgewechseltZeit>zeit) {
				nichtVorherAusgewechselt=true;
			}else if(ausgewechseltZeit==zeit&&ausgewechseltNZeit>nachspielzeit) {
				nichtVorherAusgewechselt=true;
			}
		}
		
		return (inStartaufstellung||vorherEingewechselt)&&nichtVorherAusgewechselt;
	}
	
	public boolean istAufBank(int zeit, int nachspielzeit) {
		boolean nichtVorherEingewechselt=true;
		boolean vorherAusgewechselt=false;
		
		if(eingewechselt) {
			nichtVorherEingewechselt=false;
			if(eingewechseltZeit>zeit) {
				nichtVorherEingewechselt=true;
			}else if(eingewechseltZeit==zeit&&eingewechseltNZeit>nachspielzeit) {
				nichtVorherEingewechselt=true;
			}
		}
		if(ausgewechselt) {
			vorherAusgewechselt=true;
			if(ausgewechseltZeit>zeit) {
				vorherAusgewechselt=false;
			}else if(ausgewechseltZeit==zeit&&ausgewechseltNZeit>nachspielzeit) {
				vorherAusgewechselt=false;
			}
		}
		
		return (!inStartaufstellung&&nichtVorherEingewechselt)||vorherAusgewechselt;
	}
}
