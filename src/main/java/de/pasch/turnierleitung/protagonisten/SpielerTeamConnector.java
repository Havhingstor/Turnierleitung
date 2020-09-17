package de.pasch.turnierleitung.protagonisten;

import org.jdom2.Element;

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
		this.ID=ID;
		this.spielerID=spielerID;
		this.teamID=teamID;
		this.trikotnummer=trikotnummer;
		this.as=as;
	}
	
	public String getDateiString() {
		return "<Connector>\n<ID>"+ID+"</ID>\n<Team>"+teamID+"</Team>\n<Spieler>"+spielerID+"</Spieler>\n<Trikotnummer>"+trikotnummer+"</Trikotnummer>\n<Ausgetreten>"+ausgetreten+"</Ausgetreten>\n</Connector>\n";
	}
	
	public void createXMLElements(Element parEl) {
		Element stc=new Element("Connector");
		parEl.addContent(stc);
		
		Element idEl=new Element("ID");
		idEl.addContent(""+ID);
		stc.addContent(idEl);
		
		Element teamEl=new Element("Team");
		teamEl.addContent(""+teamID);
		stc.addContent(teamEl);
		
		Element spielerEl=new Element("Spieler");
		spielerEl.addContent(""+spielerID);
		stc.addContent(spielerEl);
		
		Element trikotnummerEl=new Element("Trikotnummer");
		trikotnummerEl.addContent(""+trikotnummer);
		stc.addContent(trikotnummerEl);
		
		Element ausgetretenEl=new Element("Ausgetreten");
		ausgetretenEl.addContent(""+ausgetreten);
		stc.addContent(ausgetretenEl);
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
