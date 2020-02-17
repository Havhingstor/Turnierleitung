package de.pasch.turnierleitung.protagonisten;

import de.pasch.turnierleitung.steuerung.Pickable;

public class Protagonist implements Pickable {
	
	protected long ID=0;
	protected String name="";
	
	protected Protagonist(String name,long ID){
		this.ID=ID;
		this.name=name;
	}
	
	public long getID() {
		return ID;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return (name+" (ID: "+ID+")");
	}
	
	public void setID(long ID) {
		this.ID=ID;
	}
	
	public void setName(String name) {
		this.name=name;
	}

	public String getDateiString() {
		String string="<ID>\n"+ID+"\n</ID>\n<Name>\n"+name+"\n</Name>\n";		
		return string;
	}
}
