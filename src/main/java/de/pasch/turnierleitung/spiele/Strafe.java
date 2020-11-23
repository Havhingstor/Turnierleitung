package de.pasch.turnierleitung.spiele;

import java.util.ArrayList;

import org.jdom2.Element;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.steuerung.ArraySpeicher;
import de.pasch.turnierleitung.steuerung.IDPicker;

public class Strafe extends Spielaktivitaet {
	
	private long gefoulterID;
	
	public Strafe(int zeit,int nachspielzeit,Spieler foulender, Spieler gefoulter,
			Team team,long ID,ArrayList<String>typen,ArraySpeicher as) {
		super(zeit,nachspielzeit,foulender,ID,typen,team.getID(),as);
		if(gefoulter!=null) {
			this.gefoulterID=gefoulter.getID();
		}
	}
	
	public String getDateiString() {
		return "<Strafe>\n"+super.getDateiString()+"<Team>"+teamID+"</Team>\n<Gefoulter>"+gefoulterID+"</Gefoulter>\n</Strafe>\n";
	}
	
	public void createXMLElements(Element parEl) {
		Element strafe=new Element("Strafe");
		parEl.addContent(strafe);
		super.createXMLElements(strafe);
		Element teamEl=new Element("Team");
		strafe.addContent(teamEl);
		teamEl.addContent(""+teamID);
		Element gefoulterEl=new Element("Gefoulter");
		strafe.addContent(gefoulterEl);
		teamEl.addContent(""+gefoulterID);
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
	
	public long getGefoultenID() {
		return gefoulterID;
	}
	
	public Spieler getGefoulten() {
		return IDPicker.pick(as.spieler,gefoulterID);
	}
	
	public void setGefoultenID(long ID) {
		this.gefoulterID=ID;
	}
	
	public void setGefoulten(Spieler spieler) {
		this.gefoulterID=spieler.getID();
	}

	@Override
	public boolean isTor() {
		return false;
	}
}


