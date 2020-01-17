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
	
	public KORunde(long ID,ArraySpeicher as,String name) {
		this.ID=ID;
		this.as=as;
		this.name=name;
	}
	
	public ArrayList<Long>getRundensammlungenIDs(){
		return rundensammlungen;
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
}
