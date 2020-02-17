package de.pasch.turnierleitung.turnierelemente;

import de.pasch.turnierleitung.steuerung.Pickable;

public abstract class Turnierelement implements Comparable<Turnierelement>,Pickable {
	private long ID;
	private String name;
	
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
}
