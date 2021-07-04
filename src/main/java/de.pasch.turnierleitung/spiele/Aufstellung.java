package de.pasch.turnierleitung.spiele;

import java.util.ArrayList;

import org.jdom2.Element;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.steuerung.ArraySpeicher;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Pickable;
import de.pasch.turnierleitung.steuerung.Steuerung;

public class Aufstellung implements Pickable {
	private long ID;
	private long vereinsID = 0;
	private long spielID;
	private ArrayList<Long> startelf = new ArrayList<Long>();
	private ArrayList<Long> auswechselspieler = new ArrayList<Long>();
	private int auswechslungen = 0;
	private ArrayList<SpielerInAufstellung> SIAs = new ArrayList<SpielerInAufstellung>();
	private long kapitaenID;
	// private boolean beendet=false;
	private int hoechstStartelf = 11;
	private int hoechstAuswechselspieler = 9;
	private int hoechstAuswechslung = 5;
	private boolean verlaengerung;
	private int hoechstAuswechslungVerlaengerung = 6;
	private ArraySpeicher as;

	public Aufstellung(long vereinsID, long spielID, int hoechstStartelf, int hoechstAuswechselspieler,
			int hoechstAuswechslung, int hoechstAuswechslungNachspielzeit, long ID, ArraySpeicher as) {
		this.vereinsID = vereinsID;
		this.hoechstStartelf = hoechstStartelf;
		this.hoechstAuswechselspieler = hoechstAuswechselspieler;
		this.hoechstAuswechslung = hoechstAuswechslung;
		this.hoechstAuswechslungVerlaengerung = hoechstAuswechslungNachspielzeit;
		this.spielID = spielID;
		this.as = as;
	}

	public Aufstellung(Element parEl, ArraySpeicher as) {
		for (Element el : parEl.getChildren()) {
			if (el.getName().equals("ID")) {
				ID = Long.parseLong(el.getValue());
			} else if (el.getName().equals("Verein")) {
				vereinsID = Long.parseLong(el.getValue());
			} else if (el.getName().equals("Spiel")) {
				spielID = Long.parseLong(el.getValue());
			} else if (el.getName().equals("Auswechslungen")) {
				auswechslungen = Integer.parseInt(el.getValue());
			} else if (el.getName().equals("hoechstStartelf")) {
				hoechstStartelf = Integer.parseInt(el.getValue());
			} else if (el.getName().equals("hoechstAuswechselspieler")) {
				hoechstAuswechselspieler = Integer.parseInt(el.getValue());
			} else if (el.getName().equals("hoechstAuswechselungen")) {
				hoechstAuswechslung = Integer.parseInt(el.getValue());
			} else if (el.getName().equals("Verlängerung")) {
				verlaengerung = Boolean.parseBoolean(el.getValue());
			} else if (el.getName().equals("AuswechslungenVerlängerung")) {
				hoechstAuswechslungVerlaengerung = Integer.parseInt(el.getValue());
			} else if (el.getName().equals("Startelf")) {
				for (Element childEl : el.getChildren()) {
					startelf.add(Long.parseLong(childEl.getValue()));
				}
			} else if (el.getName().equals("Auswechselspieler")) {
				for (Element childEl : el.getChildren()) {
					auswechselspieler.add(Long.parseLong(childEl.getValue()));
				}
			} else if (el.getName().equals("SIAs")) {
				for (Element childEl : el.getChildren()) {
					SIAs.add(new SpielerInAufstellung(childEl));
				}
			} else if (el.getName().equals("Kapitaen")) {
				kapitaenID = Long.parseLong(el.getValue());
			}
		}
		this.as = as;
	}

	public void createXMLElements(Element aufstellung) {
		Element idEl = new Element("ID");
		idEl.addContent("" + ID);
		aufstellung.addContent(idEl);

		Element vereinsEl = new Element("Verein");
		vereinsEl.addContent("" + vereinsID);
		aufstellung.addContent(vereinsEl);

		Element spielEl = new Element("Spiel");
		spielEl.addContent("" + spielID);
		aufstellung.addContent(spielEl);

		Element auswEl = new Element("Auswechslungen");
		auswEl.addContent("" + auswechslungen);
		aufstellung.addContent(auswEl);

		/*
		 * Element beendetEl=new Element("Beendet"); beendetEl.addContent(""+beendet);
		 * aufstellung.addContent(beendetEl);
		 */

		Element hoechstStartelfEl = new Element("hoechstStartelf");
		hoechstStartelfEl.addContent("" + hoechstStartelf);
		aufstellung.addContent(hoechstStartelfEl);

		Element hoechstAuswechselspielerEl = new Element("hoechstAuswechselspieler");
		hoechstAuswechselspielerEl.addContent("" + hoechstAuswechselspieler);
		aufstellung.addContent(hoechstAuswechselspielerEl);

		Element hoechstAuswechslungenEl = new Element("hoechstAuswechselungen");
		hoechstAuswechslungenEl.addContent("" + hoechstAuswechslung);
		aufstellung.addContent(hoechstAuswechslungenEl);

		Element verlaengerungEl = new Element("Verlängerung");
		verlaengerungEl.addContent("" + verlaengerung);
		aufstellung.addContent(verlaengerungEl);

		Element auswechselVerlaengerungEl = new Element("AuswechslungenVerlängerung");
		auswechselVerlaengerungEl.addContent("" + hoechstAuswechslungVerlaengerung);
		aufstellung.addContent(auswechselVerlaengerungEl);

		Element startelfEl = new Element("Startelf");
		Steuerung.createALElements(startelfEl, startelf, "Startspieler");
		aufstellung.addContent(startelfEl);

		Element auswechselspielerEl = new Element("Auswechselspieler");
		Steuerung.createALElements(auswechselspielerEl, auswechselspieler, "EinAuswechselspieler");
		aufstellung.addContent(auswechselspielerEl);

		Element siaEl = new Element("SIAs");
		aufstellung.addContent(siaEl);
		for (SpielerInAufstellung sia : SIAs) {
			sia.createXMLElements(siaEl);
		}

		Element kapitaenEl = new Element("Kapitaen");
		kapitaenEl.addContent("" + kapitaenID);
		aufstellung.addContent(kapitaenEl);
	}

	public void addSpielerStartelf(Spieler spieler) {
		if (!IDPicker.pick(as.spiele, spielID).isErgebnis()) {
			if (startelf.size() < hoechstStartelf) {
				startelf.add(spieler.getID());
				SpielerInAufstellung sia = new SpielerInAufstellung(spieler.getID(), true);
				sia.setEingewechseltZeit(0);
				sia.setAusgewechseltZeit(90);
				sia.setEingewechselt(true);
				SIAs.add(sia);
			} else {
				throw new IllegalArgumentException("Die Aufstellung ist bereits voll!");
			}
		} else {
			throw new IllegalArgumentException("Die Aufstellung ist bereits beendet!");
		}
	}

	public void removeSpielerStartelf(Spieler spieler) {
		if (!IDPicker.pick(as.spiele, spielID).isErgebnis()) {
			if (startelf.size() > 0) {
				startelf.remove(spieler.getID());
				ArrayList<SpielerInAufstellung> SIAsn = new ArrayList<SpielerInAufstellung>(SIAs);
				for (SpielerInAufstellung sia : SIAsn) {
					if (sia.getSpielerID() == spieler.getID()) {
						SIAs.remove(sia);
					}
				}
			}
		} else {
			throw new IllegalArgumentException("Die Aufstellung ist bereits beendet!");
		}
	}

	public void addSpielerBank(Spieler spieler) {
		if (!IDPicker.pick(as.spiele, spielID).isErgebnis()) {
			if (auswechselspieler.size() < hoechstAuswechselspieler) {
				auswechselspieler.add(spieler.getID());
				SpielerInAufstellung sia = new SpielerInAufstellung(spieler.getID(), false);
				SIAs.add(sia);
			} else {
				throw new IllegalArgumentException("Die Bank ist bereits voll!");
			}
		} else {
			throw new IllegalArgumentException("Die Aufstellung ist bereits beendet!");
		}
	}

	public void removeSpielerBank(Spieler spieler) {
		if (!IDPicker.pick(as.spiele, spielID).isErgebnis()) {
			if (auswechselspieler.size() > 0) {
				auswechselspieler.remove(spieler.getID());
				ArrayList<SpielerInAufstellung> SIAsn = new ArrayList<SpielerInAufstellung>(SIAs);
				for (SpielerInAufstellung sia : SIAsn) {
					if (sia.getSpielerID() == spieler.getID()) {
						SIAs.remove(sia);
					}
				}
			}
		} else {
			throw new IllegalArgumentException("Die Aufstellung ist bereits beendet!");
		}
	}

	public int getStartelfNummer() {
		return startelf.size();
	}

	public int getAuswechselspielerNummer() {
		return auswechselspieler.size();
	}

	public ArrayList<Spieler> getStartelf(ArrayList<Spieler> liste) {
		ArrayList<Spieler> spieler = new ArrayList<Spieler>();
		for (Long s : startelf) {
			spieler.add(IDPicker.pick(liste, s));
		}
		return spieler;
	}

	public ArrayList<Spieler> getAuswechselspieler(ArrayList<Spieler> liste) {
		ArrayList<Spieler> spieler = new ArrayList<Spieler>();
		for (Long s : auswechselspieler) {
			spieler.add(IDPicker.pick(liste, s));
		}
		return spieler;
	}

	public ArrayList<Spieler> getAllespieler(ArrayList<Spieler> liste) {
		ArrayList<Spieler> spieler = new ArrayList<Spieler>();
		spieler.addAll(getAuswechselspieler(liste));
		spieler.addAll(getStartelf(liste));
		return spieler;
	}

	public void setHoechstStartelf(int anzahl) {
		if (!IDPicker.pick(as.spiele, spielID).isErgebnis()) {
			if (startelf.size() <= anzahl) {
				hoechstStartelf = anzahl;
			} else {
				throw new IllegalArgumentException("Es sind schon zu viele Spieler vorhanden");
			}
		} else {
			throw new IllegalArgumentException("Die Aufstellung ist bereits beendet!");
		}
	}

	public void setHoechstAuswechselspieler(int anzahl) {
		if (!IDPicker.pick(as.spiele, spielID).isErgebnis()) {
			if (auswechselspieler.size() <= anzahl) {
				hoechstAuswechselspieler = anzahl;
			} else {
				throw new IllegalArgumentException("Es sind schon zu viele Spieler vorhanden");
			}
		} else {
			throw new IllegalArgumentException("Die Aufstellung ist bereits beendet!");
		}
	}

	public void setHoechstAuswechslungen(int anzahl) {
		if (!IDPicker.pick(as.spiele, spielID).isErgebnis()) {
			if (!verlaengerung) {
				if (auswechslungen <= anzahl) {
					hoechstAuswechslung = anzahl;
				} else {
					throw new IllegalArgumentException("Es sind schon zu viele Auswechslungen vorhanden");
				}
			}
		} else {
			throw new IllegalArgumentException("Die Aufstellung ist bereits beendet!");
		}
	}

	public void setHoechstNachspielzeitAuswechslungen(int anzahl) {
		if (!IDPicker.pick(as.spiele, spielID).isErgebnis()) {
			if (auswechslungen <= anzahl) {
				hoechstAuswechslungVerlaengerung = anzahl;
			} else {
				throw new IllegalArgumentException("Es sind schon zu viele Auswechslungen vorhanden");
			}
		} else {
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
		return hoechstAuswechslungVerlaengerung;
	}

	public ArrayList<Long> getAktuelleAufstellung(int zeit, int nachspielzeit) {
		ArrayList<Long> spieler = new ArrayList<Long>();
		for (SpielerInAufstellung s : SIAs) {
			if (s.istAufPlatz(zeit, nachspielzeit)) {
				spieler.add(s.getSpielerID());
			}
		}
		return spieler;
	}

	public ArrayList<Long> getAktuelleBank(int zeit, int nachspielzeit) {
		ArrayList<Long> spieler = new ArrayList<Long>();
		for (SpielerInAufstellung s : SIAs) {
			if (s.istAufBank(zeit, nachspielzeit)) {
				spieler.add(s.getSpielerID());
			}
		}
		return spieler;
	}

	public boolean wechselMoeglich() {
		if (verlaengerung) {
			return (hoechstAuswechslungVerlaengerung > auswechslungen);
		} else {
			return (hoechstAuswechslung > auswechslungen);
		}
	}

	public int getAuswechslungen() {
		return auswechslungen;
	}

	public int getHoechstAuswechslungenAktuell() {
		if (verlaengerung) {
			return (hoechstAuswechslungVerlaengerung);
		} else {
			return (hoechstAuswechslung);
		}
	}

	public void addWechsel(int zeit, int nachspielzeit, long ausgewechseltID, long eingewechseltID) {
		if (wechselMoeglich()) {
			boolean eingewechselt = false;
			boolean ausgewechselt = false;
			for (SpielerInAufstellung s : SIAs) {
				if (s.getSpielerID() == eingewechseltID) {
					eingewechselt = s.getEingewechselt();
				} else if (s.getSpielerID() == ausgewechseltID) {
					ausgewechselt = s.getAusgewechselt();
				}
			}
			if ((getAktuelleAufstellung(zeit, nachspielzeit).contains(ausgewechseltID))
					&& (getAktuelleBank(zeit, nachspielzeit).contains(eingewechseltID)) && !ausgewechselt
					&& !eingewechselt) {
				auswechslungen++;
				for (SpielerInAufstellung s : SIAs) {
					if (s.getSpielerID() == eingewechseltID) {
						s.setEingewechseltZeit(zeit);
						s.setEingewechseltNZeit(nachspielzeit);
						s.setEingewechselt(true);
					} else if (s.getSpielerID() == ausgewechseltID) {
						s.setAusgewechseltZeit(zeit);
						s.setAusgewechseltNZeit(nachspielzeit);
						s.setAusgewechselt(true);
					}
				}
			} else {
				throw new IllegalArgumentException("Diese Spieler sind aktuell nicht für einen Wechsel verfügbar!");
			}
		} else {
			throw new IllegalArgumentException("Wechselmöglichkeiten ausgeschöpft!");
		}
	}

	public void removeWechsel(long ausgewechseltID, long eingewechseltID) {
		boolean eingewechselt = false;
		boolean ausgewechselt = false;
		for (SpielerInAufstellung s : SIAs) {
			if (s.getSpielerID() == eingewechseltID) {
				eingewechselt = s.getEingewechselt();
			} else if (s.getSpielerID() == ausgewechseltID) {
				ausgewechselt = s.getAusgewechselt();
			}
		}
		if (ausgewechselt && eingewechselt) {
			auswechslungen--;
			for (SpielerInAufstellung s : SIAs) {
				if (s.getSpielerID() == eingewechseltID) {
					s.setEingewechseltZeit(0);
					s.setEingewechselt(false);
				} else if (s.getSpielerID() == ausgewechseltID) {
					s.setAusgewechseltZeit(0);
					s.setAusgewechselt(false);
				}
			}
		}
	}

	/*
	 * public void setSpielID(long ID) { spielID=ID; }
	 */

	public long getSpielID() {
		return spielID;
	}

	public long getID() {
		return ID;
	}

	public long getVereinsID() {
		return vereinsID;
	}

	public long getKapitaenID() {
		return kapitaenID;
	}

	public void setKapitaenID(long kapitaenID) {
		this.kapitaenID = kapitaenID;
	}

	public boolean setKapitaenAutomatisch() {
		Team team = IDPicker.pick(as.teams, vereinsID);
		boolean erfolgreich = false;
		if (team.getKapitaen() != null && startelf.contains(team.getKapitaen().getID())) {
			kapitaenID = team.getKapitaen().getID();
			erfolgreich = true;
		} else if (team.getVizekapitaen() != null && startelf.contains(team.getVizekapitaen().getID())) {
			kapitaenID = team.getVizekapitaen().getID();
			erfolgreich = true;
		}
		return erfolgreich;
	}

	public boolean isBeendet() {
		return IDPicker.pick(as.spiele, spielID).isErgebnis();
	}
}