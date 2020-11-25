package de.pasch.turnierleitung.spiele;

import java.util.ArrayList;

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
