package de.pasch.turnierleitung.protagonisten;

import de.pasch.turnierleitung.steuerung.ArraySpeicher;
import de.pasch.turnierleitung.steuerung.IDPicker;

public class Team extends Protagonist {
	
	private String kurzname="";
	private String heimstadion="";
	private long kapitaenID=0;
	private long vizekapitaenID=0;
	private ArraySpeicher as=null;
	
	public Team(String kurzname,String name,long ID,ArraySpeicher as) {
		super(name,ID);
		this.kurzname=kurzname;
		this.as=as;
	}
	
	public String getKurzname() {
		return kurzname;
	}
	
        @Override
	public String toString() {
            if(!kurzname.equals("")){
		return (kurzname+": "+name);
            }else{
                return name;
            }
	}
	
    public void setKurzname(String kurzname) {
		this.kurzname=kurzname;
	}
	
        @Override
	public void setName(String name) {
		this.name=name;
	}
	
	public void setHeimstadion(String heimstadion) {
		this.heimstadion=heimstadion;
	}
	
	public String getHeimstadion() {
		return heimstadion;
	}
	
	public void setKapitaen(Spieler kapitaen) {
		this.kapitaenID=kapitaen.getID();
	}
	
	public void setVizekapitaen(Spieler vizekapitaen) {
		this.vizekapitaenID=vizekapitaen.getID();
	}
	
	public Spieler getKapitaen() {
		return IDPicker.pick(as.spieler,kapitaenID);
	}
	
	public Spieler getVizekapitaen() {
		return IDPicker.pick(as.spieler,vizekapitaenID);
	}

	public void setVizekapitaen(int ID) {
		this.vizekapitaenID=ID;
		
	}

	public void setKapitaen(int ID) {
		this.kapitaenID=ID;
		
	}
}
