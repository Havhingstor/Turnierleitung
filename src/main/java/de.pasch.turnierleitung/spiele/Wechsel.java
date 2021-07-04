package de.pasch.turnierleitung.spiele;

import java.util.ArrayList;

import org.jdom2.Element;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.steuerung.ArraySpeicher;
import de.pasch.turnierleitung.steuerung.IDPicker;

public class Wechsel extends Spielaktivitaet {

	private long ausgewechselt;

	public Wechsel(int zeit, int nachspielzeit, Spieler eingewechselt, Spieler ausgewechselt, long ID, long teamID,
			ArraySpeicher as) {
		super(zeit, nachspielzeit, eingewechselt, ID, new ArrayList<String>(), teamID, as);
		this.ausgewechselt = ausgewechselt.getID();
		typen.add("Wechsel");
		super.setTyp(0);
	}

	public Wechsel(Element parEl,ArraySpeicher as) {
		super(parEl,as);
		for(Element el:parEl.getChildren()) {
			if(el.getName().equals("Ausgewechselt")) {
				ausgewechselt=Long.parseLong(el.getValue());
			}
		}
	}
	
	public void createXMLElements(Element parEl) {
		Element element=new Element("EinWechsel");
		parEl.addContent(element);
		super.createXMLElements(element);
		Element ausgewechseltEl=new Element("Ausgewechselt");
		ausgewechseltEl.addContent(""+ausgewechselt);
		element.addContent(ausgewechseltEl);
	}
	
	@Override
	public boolean isTor() {
		return false;
	}

	@Override
	public boolean isWechsel() {
		return true;
	}

	@Override
	public String toString() {
		return "Wechsel: " + getZeitUndNachspielzeit() + super.getAusfuehrer().toString() + " --> "
				+ getAusgewechselt().toString();
	}
	
	@Override
	public String toStringGebrochen() {
		return "Wechsel: "+getZeitUndNachspielzeit()+"\nEin: "+super.getAusfuehrer().toString()+"\nAus: "+getAusgewechselt().toString();
	}

	public Spieler getAusgewechselt() {
		return IDPicker.pick(super.as.spieler, ausgewechselt);
	}
}
