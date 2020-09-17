package de.pasch.turnierleitung.turnierelemente;

import java.util.ArrayList;

import org.jdom2.Element;

import de.pasch.turnierleitung.steuerung.ArraySpeicher;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Pickable;
import de.pasch.turnierleitung.steuerung.Steuerung;

public class Rundensammlung implements Pickable{
	private ArrayList<Long>runden=new ArrayList<Long>();
	private String name="";
	private long ID=0;
	private ArraySpeicher as=null;
	private int kriteriumEins=0;
	private int kriteriumZwei=0;
	private int spielanzahl=1;
	public static final int kriteriumEinsTore=0;
	public static final int kriteriumEinsSpiele=1;
	public static final int kriteriumZweiATore=0;
	public static final int kriteriumZweiElfmeter=1;
	
	public Rundensammlung(String name,long ID,ArraySpeicher as,int kriteriumEins,
			int kriteriumZwei,int spielanzahl) {
		this.setKriteriumEins(kriteriumEins);
		this.setKriteriumZwei(kriteriumZwei);
		this.setSpielanzahl(spielanzahl);
		this.name=name;
		this.ID=ID;
		this.as=as;
	}
	
	public void createXMLElements(Element parEl) {
		Element rundensammlungEl=new Element("Rundensammlung");
		parEl.addContent(rundensammlungEl);
		
		Element rundenEl=new Element("Runden");
		Steuerung.createALElements(rundenEl, runden, "Runde");
		rundensammlungEl.addContent(rundenEl);
		
		Element nameEl=new Element("Name");
		nameEl.addContent(name);
		rundensammlungEl.addContent(nameEl);
		
		Element IDEl=new Element("ID");
		IDEl.addContent(""+ID);
		rundensammlungEl.addContent(IDEl);
		
		Element kritEinsEl=new Element("KriteriumEins");
		kritEinsEl.addContent(""+kriteriumEins);
		rundensammlungEl.addContent(kritEinsEl);
		
		Element kritZweiEl=new Element("KriteriumZwei");
		kritZweiEl.addContent(""+kriteriumZwei);
		rundensammlungEl.addContent(kritZweiEl);
		
		Element spieleEl=new Element("Spielanzahl");
		spieleEl.addContent(""+spielanzahl);
		rundensammlungEl.addContent(spieleEl);
	}
	
	/**
	 * @return the kriteriumEins
	 */
	public int getKriteriumEins() {
		return kriteriumEins;
	}

	/**
	 * @param kriteriumEins the kriteriumEins to set
	 */
	public void setKriteriumEins(int kriteriumEins) {
		this.kriteriumEins = kriteriumEins;
	}

	/**
	 * @return the kriteriumZwei
	 */
	public int getKriteriumZwei() {
		return kriteriumZwei;
	}

	/**
	 * @param kriteriumZwei the kriteriumZwei to set
	 */
	public void setKriteriumZwei(int kriteriumZwei) {
		this.kriteriumZwei = kriteriumZwei;
	}

	/**
	 * @return the spielanzahl
	 */
	public int getSpielanzahl() {
		return spielanzahl;
	}

	/**
	 * @param spielanzahl the spielanzahl to set
	 */
	public void setSpielanzahl(int spielanzahl) {
		this.spielanzahl = spielanzahl;
	}

	public String getDateiString() {
		String string="<Rundensammlung>\n<Name>"+name+"</Name>\n<ID>"+ID+"</ID>\n<Runden>\n";
		for(long runde:runden) {
			string+=runde+"\n";
		}
		string+="</Runden>\n</Rundensammlung>";
		return string;
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
