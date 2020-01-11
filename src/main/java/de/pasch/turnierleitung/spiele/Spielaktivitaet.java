package de.pasch.turnierleitung.spiele;

import java.util.ArrayList;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.steuerung.ArraySpeicher;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Pickable;

public class Spielaktivitaet implements Pickable {
	
	protected long ID=0;
	protected int zeit=0;
	protected int nachspielzeit=0;
	protected String typ="";
	protected ArrayList<String>typen=new ArrayList<String>();
	protected long ausfuehrerID=0;
	protected ArraySpeicher as=null;
	
	protected Spielaktivitaet(int zeit,int nachspielzeit,Spieler ausfuehrer,
			long ID,ArrayList<String>typen,ArraySpeicher as) {
		this.zeit=zeit;
		this.nachspielzeit=nachspielzeit;
		if(ausfuehrer!=null) {
			this.ausfuehrerID=ausfuehrer.getID();
		}
		this.typen.addAll(typen);
		this.as=as;
	}
	
	public long getAusfuehrerID() {
		return ausfuehrerID;
	}
	
	public Spieler getAusfuehrer() {
		return IDPicker.pick(as.spieler,ausfuehrerID);
	}
	
	public void setAusfuehrerID(long ID) {
		this.ausfuehrerID=ID;
	}
	
	public void setAusfuehrer(Spieler spieler) {
		this.ausfuehrerID=spieler.getID();
	}
	
	public long getID() {
		return ID;
	}
	
	public ArrayList<String>getTypen(){
		return typen;
	}
	
	public void addTyp(String typ) {
		typen.add(typ);
	}
	
	public void removeTyp(String typ) {
		this.typen.remove(typ);
	}
	
	public void setZeit(int zeit, int nachspielzeit) {
		this.zeit=zeit;
		this.nachspielzeit=nachspielzeit;
	}
	
	public int getZeit() {
		return zeit;
	}
	
	public int getNachspielzeit() {
		return nachspielzeit;
	}
}
