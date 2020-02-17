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
	
	public String getDateiString() {
		String string="<Team>\n"+super.getDateiString()+"<Kurzname>\n"
		+kurzname+"\n</Kurzname>\n<Heimstadion>\n"+heimstadion+"\n</Heimstadion>\n<Kapitaen>\n"
		+kapitaenID+"\n</Kapitaen>\n<Vizekapitaen>\n"+vizekapitaenID+"\n</Vizekapitaen\n</Team>\n";
		return string;
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
	
	public String getMoeglichKN() {
		if(!kurzname.equals("")) {
			return kurzname;
		}else {
			return name;
		}
	}
}
