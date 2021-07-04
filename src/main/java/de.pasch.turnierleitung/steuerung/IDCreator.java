package de.pasch.turnierleitung.steuerung;

public class IDCreator {

	private long letzteID=0;
	
	public IDCreator() {
		letzteID=1000000;
	}
	
	public long createID() {
		letzteID++;
		return letzteID;
	}
	
	public long getLetzteID() {
		return letzteID;
	}
	
	public void setLetzteID(long ID) {
		this.letzteID=ID;
	}
}
