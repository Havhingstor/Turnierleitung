package de.pasch.turnierleitung.turnierelemente;

import java.util.ArrayList;

import de.pasch.turnierleitung.steuerung.ArraySpeicher;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Pickable;

public class Rundensammlung implements Pickable{
	private ArrayList<Long>runden=new ArrayList<Long>();
	private String name="";
	private long ID=0;
	private ArraySpeicher as=null;
	
	public Rundensammlung(String name,long ID,ArraySpeicher as) {
		this.name=name;
		this.ID=ID;
		this.as=as;
	}
	
	public void addRunde(long ID) {
		if(!runden.contains(ID)) {
			runden.add(ID);
		}else {
			throw new IllegalArgumentException("Runde schon vorhanden!");
		}
	}
	
	public void removeRunde(long ID) {
		if(runden.contains(ID)) {
			runden.remove(ID);
		}else {
			throw new IllegalArgumentException("Runde nicht vorhanden!");
		}
	}
	
	public ArrayList<Runde>getRunden(){
		ArrayList<Runde>r=new ArrayList<Runde>();
		for(long l:runden) {
			r.add(IDPicker.pick(as.runden, l));
		}
		return r;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public String getName() {
		return name;
	}
	
	public long getID() {
		return ID;
	}
}
