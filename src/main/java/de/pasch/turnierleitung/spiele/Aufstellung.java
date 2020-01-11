package de.pasch.turnierleitung.spiele;

import java.util.ArrayList;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Pickable;

public class Aufstellung implements Pickable{
	private long ID;
	private long vereinsID=0;
	private long spielID;
	private ArrayList <Long>startelf=new ArrayList<Long>();
	private ArrayList<Long>auswechselspieler=new ArrayList<Long>();
	private int auswechslungen=0;
	private ArrayList<SpielerInAufstellung>SIAs=new ArrayList<SpielerInAufstellung>();
	private boolean beendet=false;
	private int hoechstStartelf=11;
	private int hoechstAuswechselspieler=5;
	private int hoechstAuswechslung=3;
	private boolean nachspielzeit;
	private int hoechstAuswechslungNachspielzeit=4;
	
	public Aufstellung(long vereinsID,long spielID,int hoechstStartelf,int hoechstAuswechselspieler,int hoechstAuswechslung,int hoechstAuswechslungNachspielzeit,long ID) {
		this.vereinsID=vereinsID;
		this.hoechstStartelf=hoechstStartelf;
		this.hoechstAuswechselspieler=hoechstAuswechselspieler;
		this.hoechstAuswechslung=hoechstAuswechslung;
		this.hoechstAuswechslungNachspielzeit=hoechstAuswechslungNachspielzeit;
		this.spielID=spielID;
	}
	public void addSpielerStartelf(Spieler spieler) {
		if(!beendet) {
			if(startelf.size()<hoechstStartelf) {
				startelf.add(spieler.getID());
				SpielerInAufstellung sia=new SpielerInAufstellung(spieler.getID());
				sia.setEingewechseltZeit(0);
				sia.setAusgewechseltZeit(90);
				sia.setEingewechselt(true);
				SIAs.add(sia);
			}else {
				throw new IllegalArgumentException("Die Aufstellung ist bereits voll!");
			}
		}else {
			throw new IllegalArgumentException("Die Aufstellung ist bereits beendet!");
		}
	}
	
	public void addSpielerBank(Spieler spieler) {
		if(!beendet) {
			if(auswechselspieler.size()<hoechstAuswechselspieler) {
				auswechselspieler.add(spieler.getID());
				SpielerInAufstellung sia=new SpielerInAufstellung(spieler.getID());
				SIAs.add(sia);
			}else {
				throw new IllegalArgumentException("Die Bank ist bereits voll!");
			}
		}else {
			throw new IllegalArgumentException("Die Aufstellung ist bereits beendet!");
		}
	}
	
	public int getStartelfNummer() {
		return startelf.size();
	}
	
	public int getAuswechselspielerNummer() {
		return auswechselspieler.size();
	}
	
	public ArrayList<Spieler> getStartelf(ArrayList<Spieler>liste){
		ArrayList<Spieler>spieler=new ArrayList<Spieler>();
		for(Long s:startelf) {
			spieler.add(IDPicker.pick(liste,s));
		}
		return spieler;
	}
	
	public ArrayList<Spieler> getAuswechselspieler(ArrayList<Spieler>liste){
		ArrayList<Spieler>spieler=new ArrayList<Spieler>();
		for(Long s:auswechselspieler) {
			spieler.add(IDPicker.pick(liste,s));
		}
		return spieler;
	}
	
	public void setHoechstStartelf(int anzahl) {
		if(!beendet) {
			if(startelf.size()<=anzahl) {
				hoechstStartelf=anzahl;
			}else {
				throw new IllegalArgumentException("Es sind schon zu viele Spieler vorhanden");
			}
		}else {
			throw new IllegalArgumentException("Die Aufstellung ist bereits beendet!");
		}
	}
	
	public void setHoechstAuswechselspieler(int anzahl) {
		if(!beendet) {
			if(auswechselspieler.size()<=anzahl) {
				hoechstAuswechselspieler=anzahl;
			}else {
				throw new IllegalArgumentException("Es sind schon zu viele Spieler vorhanden");
			}
		}else {
			throw new IllegalArgumentException("Die Aufstellung ist bereits beendet!");
		}
	}
	
	public void setHoechstAuswechslungen(int anzahl) {
		if(!beendet) {
			if(!nachspielzeit) {
				if(auswechslungen<=anzahl) {
					hoechstAuswechslung=anzahl;
				}else {
					throw new IllegalArgumentException("Es sind schon zu viele Auswechslungen vorhanden");
				}
			}
		}else {
			throw new IllegalArgumentException("Die Aufstellung ist bereits beendet!");
		}
	}
	
	public void setHoechstNachspielzeitAuswechslungen(int anzahl) {
		if(!beendet) {
			if(auswechslungen<=anzahl) {
				hoechstAuswechslungNachspielzeit=anzahl;
			}else {
				throw new IllegalArgumentException("Es sind schon zu viele Auswechslungen vorhanden");
			}
		}else {
			throw new IllegalArgumentException("Die Aufstellung ist bereits beendet!");
		}
	}
	
	public int getHoechstStartelf() {
		return hoechstStartelf;
	}
	
	public int getHoechstAuswechselspieler() {
		return hoechstAuswechselspieler;
	}
	
	public int getHoechstAuswechslungen() {
		return hoechstAuswechslung;
	}
	
	public int getHoechstNachspielzeitAuswechslungen() {
		return hoechstAuswechslungNachspielzeit;
	}
	
	public ArrayList<Long> getAktuelleAufstellung(int zeit){
		ArrayList<Long> spieler=new ArrayList<Long>();
		for(SpielerInAufstellung s:SIAs) {
			if((s.getEingewechseltZeit()<=zeit)&&(s.getAusgewechseltZeit()>zeit)) {
				spieler.add(s.getSpielerID());
			}
		}
		return spieler;
	}
	
	public ArrayList<Long>getAktuelleBank(int zeit){
		ArrayList<Long>spieler=new ArrayList<Long>();
		for(SpielerInAufstellung s:SIAs) {
			if((s.getEingewechseltZeit()>zeit)||(s.getAusgewechseltZeit()<=zeit)) {
				spieler.add(s.getSpielerID());
			}
		}
		return spieler;
	}
	
	public boolean wechselMoeglich() {
		if(nachspielzeit) {
			return (hoechstAuswechslungNachspielzeit>auswechslungen);
		}else {
			return (hoechstAuswechslung>auswechslungen);
		}
	}
	
	public void addWechsel(int zeit,long ausgewechseltID,long eingewechseltID) {
		if(!beendet) {
			if(wechselMoeglich()) {
				boolean eingewechselt=false;
				boolean ausgewechselt=false;
				for(SpielerInAufstellung s:SIAs) {
					if(s.getSpielerID()==eingewechseltID) {
						eingewechselt=s.getEingewechselt();
					}else if(s.getSpielerID()==ausgewechseltID) {
						ausgewechselt=s.getAusgewechselt();
					}
				}
				if((getAktuelleAufstellung(zeit).contains(ausgewechseltID))
						&&(getAktuelleBank(zeit).contains(eingewechseltID))
						&&!ausgewechselt&&!eingewechselt) {
					auswechslungen++;
					for(SpielerInAufstellung s:SIAs) {
						if(s.getSpielerID()==eingewechseltID) {
							s.setEingewechseltZeit(zeit);
							s.setEingewechselt(true);
						}else if(s.getSpielerID()==ausgewechseltID) {
							s.setAusgewechseltZeit(zeit);
							s.setAusgewechselt(true);
						}
					}
				}else {
					throw new IllegalArgumentException("Diese Spieler sind aktuell nicht für einen Wechsel verfügbar!");
				}
			}else {
				throw new IllegalArgumentException("Wechselmöglichkeiten ausgeschöpft!");
			}
		}else {
			throw new IllegalArgumentException("Die Aufstellung ist bereits abgeschlossen!");
		}
	}
	
	public void removeWechsel(long ausgewechseltID,long eingewechseltID) {
		if(!beendet) {
			
			boolean eingewechselt=false;
			boolean ausgewechselt=false;
			for(SpielerInAufstellung s:SIAs) {
				if(s.getSpielerID()==eingewechseltID) {
					eingewechselt=s.getEingewechselt();
				}else if(s.getSpielerID()==ausgewechseltID) {
					ausgewechselt=s.getAusgewechselt();
				}
			}
			if(ausgewechselt&&eingewechselt) {
				auswechslungen--;
				for(SpielerInAufstellung s:SIAs) {
					if(s.getSpielerID()==eingewechseltID) {
						s.setEingewechseltZeit(0);
						s.setEingewechselt(false);
					}else if(s.getSpielerID()==ausgewechseltID) {
						s.setAusgewechseltZeit(0);
						s.setAusgewechselt(false);
					}
				}
			}else {
				throw new IllegalArgumentException("Dieser Wechsel wurde so nicht volstreckt!");
			}
		}else {
			throw new IllegalArgumentException("Die Aufstellung ist bereits abgeschlossen!");
		}
	}
	
	public void setSpielID(long ID) {
		spielID=ID;
	}
	
	public long getSpielID() {
		return spielID;
	}
	
	public long getID() {
		return ID;
	}
	
	public long getVereinsID() {
		return vereinsID;
	}
}