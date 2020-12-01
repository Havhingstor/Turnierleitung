package de.pasch.turnierleitung.spiele;

import java.util.ArrayList;

import org.jdom2.Element;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.steuerung.ArraySpeicher;
import de.pasch.turnierleitung.steuerung.IDPicker;

public class Tor extends Spielaktivitaet {
	
	private long vorbereiterID;
	
	public Tor(int zeit,int nachspielzeit,Spieler torschuetze,Spieler vorbereiter,Team team,long ID,
			ArrayList<String>typen,ArraySpeicher as) {
		super(zeit,nachspielzeit,torschuetze,ID,typen,team.getID(), as);
		if(vorbereiter!=null) {
			vorbereiterID=vorbereiter.getID();
		}
	}
	
	public String getDateiString() {
		return "<Tor>\n"+super.getDateiString()+"<Team>"+teamID+"</Team>\n<Vorbereiter>"+vorbereiterID+"</Vorbereiter>\n</Tor>\n";
	}
	
	public void createXMLElements(Element parEl) {
		Element element=new Element("Tor");
		parEl.addContent(element);
		super.createXMLElements(element);
		Element vorbEl=new Element("Vorbereiter");
		vorbEl.addContent(""+vorbereiterID);
		element.addContent(vorbEl);
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
	

	@Override
	public boolean isTor() {
		return true;
	}

	@Override
	public boolean isWechsel() {
		// TODO Auto-generated method stub
		return false;
	}
}
