package de.pasch.turnierleitung.turnierelemente;

import java.util.ArrayList;

import de.pasch.turnierleitung.steuerung.ArraySpeicher;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Pickable;

public class KORunde extends Turnierelement implements Pickable{
	private final ArrayList<Long>rundensammlungen=new ArrayList<>();
	private final ArrayList<PlatzierungInKORunde>platzierungen=new ArrayList<>();
	private long ID=0;
	private String name="";
	private final ArraySpeicher as;
	private int kriteriumEins=0;
	private int kriteriumZwei=0;
	private int spielanzahl=1;

	public static final int kriteriumEinsTore=0;
	public static final int kriteriumEinsSpiele=1;
	public static final int kriteriumZweiATore=0;
	public static final int kriteriumZweiElfmeter=1;
	
	public String getDateiString() {
		String string="<KORunde>\n<ID>"+ID+"</ID>\n<Name>"+name+"</Name>\n<KriteriumEins>"+kriteriumEins+"</KriteriumEins>\n"
				+ "<KriteriumZwei>"+kriteriumZwei+"</KriteriumZwei>\n<Spielanzahl>"+spielanzahl+"</Spielanzahl>\n<Rundensammlungen>\n";
		for(long rs:rundensammlungen) {
			string+=rs+"\n";
		}
		string+="</Rundensammlungen>\n<Platzierungen>\n";
		for(PlatzierungInKORunde piko: platzierungen) {
			string+=piko.getDateiString();
		}
		string+="</Platzierungen>\n</KORunde>\n";
		return string;
	}
	
	public KORunde(long ID,ArraySpeicher as,String name,int k1,int k2,int spielanzahl) {
		this.ID=ID;
		this.as=as;
		this.name=name;
		this.kriteriumEins=k1;
		this.kriteriumZwei=k2;
		this.spielanzahl=spielanzahl;
	}
	
	
	public ArrayList<Long>getRundensammlungenIDs(){
		return rundensammlungen;
	}
	
	public void addRundensammlung(long ID) {
		rundensammlungen.add(ID);
	}
	
	public ArrayList<Rundensammlung>getRundensammlungen(){
		ArrayList<Rundensammlung>rückgabeRunden=new ArrayList<>();
		rundensammlungen.forEach((l) -> {
			rückgabeRunden.add(IDPicker.pick(as.rs, l));
		});
		return rückgabeRunden;
	}

	@Override
	public long getID() {
		return ID;
	}

	@Override
	public String toString() {
		return name;
	}    

	public String getName() {
		return name;
	}

	public void removeRundensammlung(long ID) {
		if(rundensammlungen.contains(ID)) {
			rundensammlungen.remove(ID);
		}
	}

	public void setzePlatzierungen() {
		platzierungen.forEach((piko) -> {
			long team;
			if(piko.getGewinner()) {
				team=IDPicker.pick(as.runden,piko.getSpielID()).getSetGewinner();
			}else {
				team=IDPicker.pick(as.runden,piko.getSpielID()).getSetVerlierer();
			}
			piko.setTeam(team);
		});
	}

	public ArrayList<PlatzierungInKORunde>getPlatzierungen(){
		return platzierungen;
	}

	public void addPlatzierung(PlatzierungInKORunde p) {
		if(p.getPlatz()==platzierungen.size()+1) {
			platzierungen.add(p);
		}
	}

	public void removeLetztePlatzierung() {
		int hoechstPlatz=0;
		PlatzierungInKORunde piko=null;
		for(PlatzierungInKORunde piko2:platzierungen) {
			if(piko2.getPlatz()>hoechstPlatz) {
				hoechstPlatz=piko2.getPlatz();
				piko=piko2;
			}
		}
		platzierungen.remove(piko);
	}

	@Override
	public boolean isLiga() {
		return false;
	}
	
	public void setKriterium1(int k1) {
		this.kriteriumEins=k1;
	}
	
	public void setKriterium2(int k2) {
		this.kriteriumZwei=k2;
	}
	
	public int getKriterium1() {
		return kriteriumEins;
	}
	
	public int getKriterium2() {
		return kriteriumZwei;
	}
	
	public void setSpielanzahl(int anzahl) {
		this.spielanzahl=anzahl;
	}
	
	public int getSpielanzahl() {
		return spielanzahl;
	}
}
