package de.pasch.turnierleitung.turnierelemente;

import de.pasch.turnierleitung.steuerung.Pickable;

public abstract class Turnierelement implements Comparable<Turnierelement>,Pickable {
	private long ID;
	private String name;
	private int hoechstStartelf=11;
	private int hoechstAuswechselspieler=9;
	private int hoechstAuswechslung=5;
	private int hoechstAuswechslungNachspielzeit=6;
	
	public long getID() {
		return ID;
	}
	
	public int compareTo(Turnierelement o) {
		if(ID<o.getID()) {
			return 1;
		}else if(ID>o.getID()) {
			return -1;
		}else {
			return 0;
		}
			
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public abstract boolean isLiga();

	public int getHoechstStartelf() {
		return hoechstStartelf;
	}

	public void setHoechstStartelf(int hoechstStartelf) {
		this.hoechstStartelf = hoechstStartelf;
	}

	public int getHoechstAuswechselspieler() {
		return hoechstAuswechselspieler;
	}

	public void setHoechstAuswechselspieler(int hoechstAuswechselspieler) {
		this.hoechstAuswechselspieler = hoechstAuswechselspieler;
	}

	public int getHoechstAuswechslung() {
		return hoechstAuswechslung;
	}

	public void setHoechstAuswechslung(int hoechstAuswechslung) {
		this.hoechstAuswechslung = hoechstAuswechslung;
	}

	public int getHoechstAuswechslungNachspielzeit() {
		return hoechstAuswechslungNachspielzeit;
	}

	public void setHoechstAuswechslungNachspielzeit(int hoechstAuswechslungNachspielzeit) {
		this.hoechstAuswechslungNachspielzeit = hoechstAuswechslungNachspielzeit;
	}
}
