package de.pasch.turnierleitung.protagonisten;

import de.pasch.turnierleitung.steuerung.Pickable;

public class Link implements Linkface,Pickable{
	private long rundeID;
	private long ID;
	private boolean gewinner;
	
	public Link(long ID,long rundeID, boolean gewinner) {
		this.gewinner=gewinner;
		this.rundeID=rundeID;
		this.ID=ID;
	}
	
	public String getDateiString() {
		return "<Link>\n<ID>"+ID+"</ID>\n<Runde>"+rundeID+"</Runde>\n"
				+ "<Gewinner>"+gewinner+"</Gewinner>\n</Link>\n";
	}
	
	public void setRunde(long ID, boolean gewinner) {
		this.rundeID=ID;
		this.gewinner=gewinner;
	}
	
	public long getID() {
		return ID;
	}
	
	public long getRundeID() {
		return rundeID;
	}
	
	public boolean getGewinner() {
		return gewinner;
	}
}
