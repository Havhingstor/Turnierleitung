package de.pasch.turnierleitung.turnierelemente;

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
