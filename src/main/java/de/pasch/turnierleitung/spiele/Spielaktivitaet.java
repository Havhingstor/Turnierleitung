package de.pasch.turnierleitung.spiele;

import java.util.ArrayList;

import org.jdom2.Element;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.steuerung.ArraySpeicher;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Pickable;

public abstract class Spielaktivitaet implements Pickable {
	
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
		this.ID=ID;
		if(ausfuehrer!=null) {
			this.ausfuehrerID=ausfuehrer.getID();
		}
		this.typen.addAll(typen);
		this.as=as;
	}
	
	public abstract boolean isTor();
	
	public String getDateiString() {
		String string="<ID>"+ID+"</ID>\n<Zeit>"+zeit+"</Zeit>\n<Nachspielzeit>"+nachspielzeit+"</Nachspielzeit>\n"
				+ "<Ausfuehrer>"+ausfuehrerID+"</Ausfuehrer>\n<Typen>\n";
		for(String str:typen) {
			string+=str+"\n";
		}
		string+="</Typen>\n";
		return string;
	}
	
	public void createXMLElements(Element parEl) {
		Element idEl=new Element("ID");
		idEl.addContent(""+ID);
		parEl.addContent(idEl);
		
		Element zeitEl=new Element("Zeit");
		zeitEl.addContent(""+zeit);
		parEl.addContent(zeitEl);
		
		Element nzeitEl=new Element("Nachspielzeit");
		nzeitEl.addContent(""+nachspielzeit);
		parEl.addContent(nzeitEl);
		
		Element ausfEl=new Element("Ausfuehrer");
		ausfEl.addContent(""+ausfuehrerID);
		parEl.addContent(ausfEl);
		
		Element typenEl=new Element("Typen");
		parEl.addContent(typenEl);
		for(String typ:typen) {
			Element el=new Element("Typ");
			el.addContent(typ);
			typenEl.addContent(el);
		}
		
		Element typEl=new Element("Typ");
		parEl.addContent(typEl);
		typEl.addContent(typ);
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
	
	public String getZeitUndNachspielzeit() {
		if(nachspielzeit==0) {
			return zeit+"'";
		}else {
			return zeit+"+"+nachspielzeit+"'";
		}
	}
	
	public void setTyp(int index) {
		typ=typen.get(index);
	}
	
	public String getTyp() {
		return typ;
	}
	
	@Override
	public String toString() {
		return (isTor()?"Tor: ":"Strafe: ")+(IDPicker.pick(as.spieler,ausfuehrerID).toString())+(" ("+getZeitUndNachspielzeit()+")");
	}
}
