package de.pasch.turnierleitung.turnierelemente;

import org.jdom2.Element;

import de.pasch.turnierleitung.steuerung.Steuerung;

public class PlatzierungInKORunde {
	private int platz=0;
	private long spielID=0;
	private boolean gewinner=true;
	private long teamID=0;
	private boolean platziert=false;
	
	
	public PlatzierungInKORunde(int platz, long spielID,boolean gewinner) {
		this.platz=platz;
		this.spielID=spielID;
		this.gewinner=gewinner;
	}
	
	public void createXMLElements(Element parEl) {
		Element pikEl=new Element("PIK");
		parEl.addContent(pikEl);
		
		Element platzEl=new Element("Platz");
		platzEl.addContent(""+platz);
		pikEl.addContent(platzEl);
		
		Element spielEl=new Element("Spiel");
		spielEl.addContent(""+spielID);
		pikEl.addContent(spielEl);
		
		Element gewinnerEl=new Element("Gewinner");
		gewinnerEl.addContent(""+gewinner);
		pikEl.addContent(gewinnerEl);
		
		Element teamEl=new Element("Team");
		teamEl.addContent(""+teamID);
		pikEl.addContent(teamEl);
		
		Element platziertEl=new Element("Platzier");
		platziertEl.addContent(""+platziert);
		pikEl.addContent(platziertEl);
	}
	
	public String getDateiString() {
		return "<Platzierung>\n<Platz>"+platz+"</Platz>\n<Spiel>"+spielID+"</Spiel>\n<Gewinner>"+gewinner+"</Gewinner>"
				+ "\n<Team>"+teamID+"</Team>\n<Platziert>"+platziert+"</Platziert>\n</Platzierung>\n";
	}
	
	public void setTeam(long teamID) {
		this.teamID=teamID;
		platziert=true;
	}
	
	public void removeTeam() {
		platziert=false;
	}
	
	public long getTeam() {
		return teamID;
	}
	
	public boolean istPlatziert() {
		return platziert;
	}
	
	public int getPlatz() {
		return platz;
	}
	
	public void setSpiel(long ID,boolean gewinner) {
		this.spielID=ID;
		this.gewinner=gewinner;
	}
	
	public long getSpielID(){
		return spielID;
	}
	
	public boolean getGewinner() {
		return gewinner;
	}
}
