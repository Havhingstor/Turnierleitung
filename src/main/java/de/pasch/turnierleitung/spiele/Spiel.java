package de.pasch.turnierleitung.spiele;

import java.util.ArrayList;
import java.util.Collections;

import org.jdom2.Element;

import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.steuerung.ArraySpeicher;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Pickable;
import de.pasch.turnierleitung.steuerung.Steuerung;

public class Spiel implements Pickable {
	private long ID = 0;
	private long heimID = 0;
	private long auswaertsID = 0;
	private boolean enthaeltLink = false;
	private boolean neutralerPlatz = false;
	private String stadion = "";
	private ArrayList<Long> heimtore = new ArrayList<>();
	private ArrayList<Long> auswaertstore = new ArrayList<>();
	private ArrayList<Long>heimwechsel=new ArrayList<>();
	private ArrayList<Long>auswaertswechsel=new ArrayList<>();
	private int alternativeHeimtore = 0;
	private int alternativeAuswaertstore = 0;
	private ArrayList<Long> heimstrafen = new ArrayList<>();
	private ArrayList<Long> auswaertsstrafen = new ArrayList<>();
	private long gewinnerID = 0;
	private boolean ergebnis = false;;
	private boolean unentschieden = false;
//	private Elfmeterschießen elfmeterschießen = null;
//	private boolean elfWahr = false;
	private boolean nachspielzeit = false;
	private boolean seperaterPlatzname = false;
	private Aufstellung aufstHeim;
	private Aufstellung aufstAuswaerts;

	private ArraySpeicher as = null;
	public Spiel(Team heimteam, Team auswaertsteam, long ID, boolean neutralerPlatz, ArraySpeicher as, Aufstellung heim,
			Aufstellung auswaerts) {
		this.ID = ID;
		this.heimID = heimteam.getID();
		this.auswaertsID = auswaertsteam.getID();
		this.neutralerPlatz = neutralerPlatz;
		this.stadion = heimteam.getHeimstadion();
		this.as = as;
		aufstHeim = heim;
		aufstAuswaerts = auswaerts;
	}

	public Spiel(Element parEl,ArraySpeicher as) {
		for(Element el:parEl.getChildren()) {
			if(el.getName().equals("ID")) {
				ID=Long.parseLong(el.getValue());
			}else if(el.getName().equals("Heim")) {
				heimID=Long.parseLong(el.getValue());
			}else if(el.getName().equals("Auswaerts")) {
				auswaertsID=Long.parseLong(el.getValue());
			}else if(el.getName().equals("Link")) {
				enthaeltLink=Boolean.parseBoolean(el.getValue());
			}else if(el.getName().equals("Neutral")) {
				neutralerPlatz=Boolean.parseBoolean(el.getValue());
			}else if(el.getName().equals("Stadion")) {
				stadion=el.getValue();
			}else if(el.getName().equals("Gewinner")) {
				gewinnerID=Long.parseLong(el.getValue());
			}else if(el.getName().equals("Ergebnis")) {
				ergebnis=Boolean.parseBoolean(el.getValue());
			}else if(el.getName().equals("Unentschieden")) {
				unentschieden=Boolean.parseBoolean(el.getValue());
			}else if(el.getName().equals("Nachspielzeit")) {
				nachspielzeit=Boolean.parseBoolean(el.getValue());
			}else if(el.getName().equals("Seperat")) {
				seperaterPlatzname=Boolean.parseBoolean(el.getValue());
			}else if(el.getName().equals("Heimtore")) {
				for(Element childEl:el.getChildren()) {
					heimtore.add(Long.parseLong(childEl.getValue()));
				}
			}else if(el.getName().equals("Auswaertstore")) {
				for(Element childEl:el.getChildren()) {
					auswaertstore.add(Long.parseLong(childEl.getValue()));
				}
			}else if(el.getName().equals("Heimwechsel")) {
				for(Element childEl:el.getChildren()) {
					heimwechsel.add(Long.parseLong(childEl.getValue()));
				}
			}else if(el.getName().equals("Auswaertswechsel")) {
				for(Element childEl:el.getChildren()) {
					auswaertswechsel.add(Long.parseLong(childEl.getValue()));
				}
			}else if(el.getName().equals("Heimstrafen")) {
				for(Element childEl:el.getChildren()) {
					heimstrafen.add(Long.parseLong(childEl.getValue()));
				}
			}else if(el.getName().equals("Auswaertsstrafen")) {
				for(Element childEl:el.getChildren()) {
					auswaertsstrafen.add(Long.parseLong(childEl.getValue()));
				}
			}else if(el.getName().equals("AlternativeHeimtore")) {
				alternativeHeimtore=Integer.parseInt(el.getValue());
			}else if(el.getName().equals("AlternativeAuswaertstore")) {
				alternativeAuswaertstore=Integer.parseInt(el.getValue());
			}else if(el.getName().equals("Heimaufstellung")) {
				aufstHeim=new Aufstellung(el, as);
			}else if(el.getName().equals("Auswaertsaufstellung")) {
				aufstAuswaerts=new Aufstellung(el,as);
			}
		}
		this.as=as;
	}
	
	/**
	 * @return the ergebnis
	 */
	public boolean isErgebnis() {
		return ergebnis;
	}

	/**
	 * @param ergebnis the ergebnis to set
	 */
	public void setErgebnis(boolean ergebnis) {
		this.ergebnis = ergebnis;

	}

	public void createXMLElements(Element parEl) {
		Element spiel = new Element("Spiel");
		parEl.addContent(spiel);

		Element idEl = new Element("ID");
		idEl.addContent("" + ID);
		spiel.addContent(idEl);

		Element heimEl = new Element("Heim");
		heimEl.addContent("" + heimID);
		spiel.addContent(heimEl);

		Element auswaertsEl = new Element("Auswaerts");
		auswaertsEl.addContent("" + auswaertsID);
		spiel.addContent(auswaertsEl);

		Element linkEl = new Element("Link");
		linkEl.addContent("" + enthaeltLink);
		spiel.addContent(linkEl);

		Element neutralEl = new Element("Neutral");
		neutralEl.addContent("" + neutralerPlatz);
		spiel.addContent(neutralEl);

		Element stadionEl = new Element("Stadion");
		stadionEl.addContent(stadion);
		spiel.addContent(stadionEl);

		Element gewinnerEl = new Element("Gewinner");
		gewinnerEl.addContent("" + gewinnerID);
		spiel.addContent(gewinnerEl);

		Element ergebnisEl = new Element("Ergebnis");
		ergebnisEl.addContent("" + ergebnis);
		spiel.addContent(ergebnisEl);

		Element unentschiedenEl = new Element("Unentschieden");
		unentschiedenEl.addContent("" + unentschieden);
		spiel.addContent(unentschiedenEl);

//		Element elfBoolEl = new Element("ElfBool");
//		elfBoolEl.addContent("" + elfWahr);
//		spiel.addContent(elfBoolEl);

		Element nachEl = new Element("Nachspielzeit");
		nachEl.addContent("" + nachspielzeit);
		spiel.addContent(nachEl);

		Element seperatEl = new Element("Seperat");
		seperatEl.addContent("" + seperaterPlatzname);
		spiel.addContent(seperatEl);

		Element heimtoreEl = new Element("Heimtore");
		spiel.addContent(heimtoreEl);
		Steuerung.createALElements(heimtoreEl, heimtore, "Tor");
		/*
		 * for(long tor: heimtore) { Element torEl=new Element("Tor");
		 * heimtoreEl.addContent(torEl); torEl.addContent(""+tor); }
		 */

		Element auswaertstoreEl = new Element("Auswaertstore");
		spiel.addContent(auswaertstoreEl);
		for (long tor : auswaertstore) {
			Element torEl = new Element("Tor");
			auswaertstoreEl.addContent(torEl);
			torEl.addContent("" + tor);
		}

		Element heimwechselEl=new Element("Heimwechsel");
		Steuerung.createALElements(heimwechselEl, heimwechsel, "Wechsel");
		spiel.addContent(heimwechselEl);
		
		Element auswaertswechselEl=new Element("Auswaertswechsel");
		Steuerung.createALElements(auswaertswechselEl, auswaertswechsel, "Wechsel");
		spiel.addContent(auswaertswechselEl);
		
		Element heimstrafenEl = new Element("Heimstrafen");
		spiel.addContent(heimstrafenEl);
		for (long strafe : heimstrafen) {
			Element strafeEl = new Element("Strafe");
			heimstrafenEl.addContent(strafeEl);
			strafeEl.addContent("" + strafe);
		}

		Element auswaertstrafenEl = new Element("Auswaertsstrafen");
		spiel.addContent(auswaertstrafenEl);
		for (long strafe : auswaertsstrafen) {
			Element strafeEl = new Element("Strafe");
			auswaertstrafenEl.addContent(strafeEl);
			strafeEl.addContent("" + strafe);
		}
		
		Element alternHeimtoreEl=new Element("AlternativeHeimtore");
		alternHeimtoreEl.addContent(""+alternativeHeimtore);
		spiel.addContent(alternHeimtoreEl);
		
		Element alternAuswaertstoreEl=new Element("AlternativeAuswaertstore");
		alternAuswaertstoreEl.addContent(""+alternativeAuswaertstore);
		spiel.addContent(alternAuswaertstoreEl);

		Element heimaufstellungEl=new Element("Heimaufstellung");
		aufstHeim.createXMLElements(heimaufstellungEl);
		spiel.addContent(heimaufstellungEl);
		
		Element auswaertsaufstellungEl=new Element("Auswaertsaufstellung");
		aufstAuswaerts.createXMLElements(auswaertsaufstellungEl);
		spiel.addContent(auswaertsaufstellungEl);
		
//		if (elfmeterschießen != null) {
//			Element elfEl = new Element("Elfmeterschießen");
//			spiel.addContent(elfEl);
//
//			Element heimGetroffenEl = new Element("Heimtreffer");
//			spiel.addContent(heimGetroffenEl);
//			for (boolean treffer : elfmeterschießen.heimGetroffen) {
//				Element trefferEl = new Element("Treffer");
//				heimGetroffenEl.addContent(trefferEl);
//				trefferEl.addContent("" + treffer);
//			}
//
//			Element auswaertsGetroffenEl = new Element("Auswaertstreffer");
//			spiel.addContent(auswaertsGetroffenEl);
//			for (boolean treffer : elfmeterschießen.auswaertsGetroffen) {
//				Element trefferEl = new Element("Treffer");
//				auswaertsGetroffenEl.addContent(trefferEl);
//				trefferEl.addContent("" + treffer);
//			}
//
//			Element heimSpielerEl = new Element("Heimspieler");
//			spiel.addContent(heimSpielerEl);
//			for (boolean spieler : elfmeterschießen.heimGetroffen) {
//				Element spielerEl = new Element("Spieler");
//				heimSpielerEl.addContent(spielerEl);
//				spielerEl.addContent("" + spieler);
//			}
//
//			Element auswaertsSpielerEl = new Element("Auswaertsspieler");
//			spiel.addContent(auswaertsSpielerEl);
//			for (boolean spieler : elfmeterschießen.auswaertsGetroffen) {
//				Element spielerEl = new Element("Spieler");
//				auswaertsSpielerEl.addContent(spielerEl);
//				spielerEl.addContent("" + spieler);
//			}
//		}
	}

	public boolean isEnthaeltLink() {
		return enthaeltLink;
	}

	public void setEnthaeltLink(boolean enthaeltLink) {
		this.enthaeltLink = enthaeltLink;
	}

	public long getID() {
		return ID;
	}

	public void setHeimteam(Team heimteam) {
		heimID = heimteam.getID();
	}

	public void setAuswaertsteam(Team auswaertsteam) {
		auswaertsID = auswaertsteam.getID();
	}

	public void setStadion(Team heimteam) {
		stadion = heimteam.getHeimstadion();
	}

	public void setStadion(String name) {
		stadion = name;
	}

	public void setSeperaterPlatzname(boolean wahr) {
		this.seperaterPlatzname = wahr;
	}

	public boolean getSeperaterPlatzname() {
		return seperaterPlatzname;
	}

	public void setNeutralerPlatz(boolean neutral) {
		neutralerPlatz = neutral;
	}

	public Team getHeimteam() {
		if (!enthaeltLink) {
			return IDPicker.pick(as.teams, heimID);
		} else {
			return null;
		}
	}

	public Team getAuswaertsteam() {
		if (!enthaeltLink) {
			return IDPicker.pick(as.teams, auswaertsID);
		} else {
			return null;
		}
	}

	public long getAuswaertsID() {
		return auswaertsID;
	}

	public long getHeimID() {
		return heimID;
	}

	public String getStadion() {
		return stadion;
	}

	public boolean getNeutralerPlatz() {
		return neutralerPlatz;
	}

	public int getMengeHeimtoreDirekt() {
		return heimtore.size();
	}

	public int getMengeAuswaertstoreDirekt() {
		return auswaertstore.size();
	}

	public void setMinimaleHeimtore(int alternativeHeimtore) {
		if (alternativeHeimtore >= heimtore.size()) {
			this.alternativeHeimtore = alternativeHeimtore;
			abschließenUndGewinner();
		}
		if (getHeimtoreZahl() + getAuswaertstoreZahl() > 0) {
			ergebnis = true;
		}
	}

	public void setMinimaleAuswaertstore(int alternativeAuswaertstore) {
		if (alternativeAuswaertstore >= auswaertstore.size()) {
			this.alternativeAuswaertstore = alternativeAuswaertstore;
			abschließenUndGewinner();
		}
		if (getHeimtoreZahl() + getAuswaertstoreZahl() > 0) {
			ergebnis = true;
		}
	}

	public void addTor(Tor tor) {
		if (tor.getTeamID() == heimID) {
			if (isInTime(tor, true)) {
				heimtore.add(tor.getID());
				ergebnis = true;
				if (heimtore.size() > alternativeHeimtore) {
					alternativeHeimtore = heimtore.size();
				}
			} else {
				throw new IllegalArgumentException("Spieler ist zu dieser Zeit nicht auf dem Platz!");
			}
		} else if (tor.getTeamID() == auswaertsID) {
			if (isInTime(tor, false)) {
				auswaertstore.add(tor.getID());
				ergebnis = true;
				if (auswaertstore.size() > alternativeAuswaertstore) {
					alternativeAuswaertstore = auswaertstore.size();
				}
			} else {
				throw new IllegalArgumentException("Spieler ist zu dieser Zeit nicht auf dem Platz!");
			}
		} else {
			throw new IllegalArgumentException("Tor kann keinem Team zugeordnet werden!");
		}
		abschließenUndGewinner();
	}

	public void removeTor(long ID) {
		if (heimtore.contains(ID)) {
			heimtore.remove(ID);
		} else if (auswaertstore.contains(ID)) {
			auswaertstore.remove(ID);
		} else {
			throw new IllegalArgumentException("Dieses Tor ist weder als Heim- noch als " + "Ausw�rtstor registriert!");
		}
		abschließenUndGewinner();
	}

	public int getHeimtoreZahl() {
		return alternativeHeimtore;
	}

	public int getAuswaertstoreZahl() {
		return alternativeAuswaertstore;
	}

	public ArrayList<Tor> getHeimtoreDirekt() {
		ArrayList<Tor> tore = new ArrayList<>();
		heimtore.forEach((l) -> {
			tore.add(IDPicker.pick(as.tore, l));
		});
		return tore;
	}

	public ArrayList<Tor> getAuswaertstoreDirekt() {
		ArrayList<Tor> tore = new ArrayList<>();
		auswaertstore.forEach((l) -> {
			tore.add(IDPicker.pick(as.tore, l));
		});
		return tore;
	}

	public ArrayList<Long> getHeimtoreIDs() {
		return heimtore;
	}

	public ArrayList<Long> getAuswaertstoreIDs() {
		return auswaertstore;
	}

	public int getMengeHeimstrafen() {
		return heimstrafen.size();
	}

	public int getMengeAuswaertsfouls() {
		return auswaertsstrafen.size();
	}

	public void addStrafe(Strafe strafe) {
		if (strafe.getTeamID() == heimID) {
			heimstrafen.add(strafe.getID());
			ergebnis = true;
		} else if (strafe.getTeamID() == auswaertsID) {
			auswaertsstrafen.add(strafe.getID());
			ergebnis = true;
		} else {
			throw new IllegalArgumentException("Strafe kann keinem Team zugeordnet werden!");
		}
	}

	public void removeStrafe(long ID) {
		if (heimstrafen.contains(ID)) {
			heimstrafen.remove(ID);
		} else if (auswaertsstrafen.contains(ID)) {
			auswaertsstrafen.remove(ID);
		} else {
			throw new IllegalArgumentException(
					"Diese Strafe ist weder als Heim- noch als " + "Auswärtsstrafe registriert!");
		}
	}

	public ArrayList<Strafe> getHeimstrafen() {
		ArrayList<Strafe> tore = new ArrayList<>();
		heimstrafen.forEach((l) -> {
			tore.add(IDPicker.pick(as.strafen, l));
		});
		return tore;
	}

	public ArrayList<Strafe> getAuswaertsstrafen() {
		ArrayList<Strafe> tore = new ArrayList<>();
		auswaertsstrafen.forEach((l) -> {
			tore.add(IDPicker.pick(as.strafen, l));
		});
		return tore;
	}

	public ArrayList<Long> getHeimstrafenIDs() {
		return heimstrafen;
	}

	public ArrayList<Long> getAuswaertsstrafenIDs() {
		return auswaertsstrafen;
	}
	
	public void addWechsel(Wechsel wechsel) {
		if(wechsel.getTeamID()==heimID) {
			heimwechsel.add(wechsel.getID());
			ergebnis=true;
		}else if(wechsel.getTeamID()==auswaertsID) {
			auswaertswechsel.add(wechsel.getID());
			ergebnis=true;
		}else {
			throw new IllegalArgumentException("Wechsel kann keinem Team zugeordnet werden!");
		}
	}

	public void removeWechsel(long ID) {
		if (heimwechsel.contains(ID)) {
			heimwechsel.remove(ID);
		} else if (auswaertswechsel.contains(ID)) {
			auswaertswechsel.remove(ID);
		} else {
			throw new IllegalArgumentException(
					"Dieser Wechsel ist weder als Heim- noch als " + "Auswärtswechsel registriert!");
		}
	}
	
	public ArrayList<Wechsel> getHeimwechsel() {
		ArrayList<Wechsel> wechsel = new ArrayList<>();
		heimwechsel.forEach((l) -> {
			wechsel.add(IDPicker.pick(as.wechsel, l));
		});
		return wechsel;
	}

	public ArrayList<Wechsel> getAuswaertswechsel() {
		ArrayList<Wechsel> wechsel = new ArrayList<>();
		auswaertswechsel.forEach((l) -> {
			wechsel.add(IDPicker.pick(as.wechsel, l));
		});
		return wechsel;
	}
	
	public boolean abschließenUndGewinner() {
		if (getHeimtoreZahl() > getAuswaertstoreZahl()) {
			gewinnerID = heimID;
			unentschieden = false;
		} else if (getHeimtoreZahl() < getAuswaertstoreZahl()) {
			gewinnerID = auswaertsID;
			unentschieden = false;
		} else {
			gewinnerID = 0;
			unentschieden = true;
		}
		return !unentschieden;
	}

	public Team getGewinner() {
		if (!enthaeltLink) {
			if (unentschieden) {
				throw new IllegalArgumentException("Das Spiel ist unentschieden augegeangen!");
			} else {
				return IDPicker.pick(as.teams, gewinnerID);
			}
		} else {
			return null;
		}
	}

	public long getGewinnerID() {
		if (unentschieden) {
			throw new IllegalArgumentException("Das Spiel ist unentschieden augegeangen!");
		} else {
			return gewinnerID;
		}
	}

	public Team getVerlierer() {
		if (!enthaeltLink) {
			if (unentschieden) {
				throw new IllegalArgumentException("Das Spiel ist unentschieden augegangen!");
			} else {
				return IDPicker.pick(as.teams, getVerliererID());
			}
		} else {
			return null;
		}
	}

	public long getVerliererID() {
		if (unentschieden) {
			throw new IllegalArgumentException("Das Spiel ist unentschieden augegangen!");
		} else {
			if (gewinnerID == heimID) {
				return auswaertsID;
			} else if (gewinnerID == auswaertsID) {
				return auswaertsID;
			} else {
				return 0;
			}
		}

	}

//	public boolean elfmeterschießenNoetig() {
//		return unentschieden;
//	}
//
//	public void addElfmeterschießen() {
//		ergebnis = true;
//		if (!elfWahr) {
//			elfmeterschießen = new Elfmeterschießen();
//			elfWahr = true;
//		} else {
//			throw new IllegalArgumentException("Es ist bereits ein Elfmeterschießen vorhanden!");
//		}
//	}
//
//	public void removeElfmeterschießen() {
//		elfmeterschießen = null;
//	}
//
//	public boolean istElfmeterschießen() {
//		return elfWahr;
//	}
//
//	public void addBenoetigtesElfmeterschießen() {
//		if (elfmeterschießenNoetig()) {
//			addElfmeterschießen();
//		}
//	}
//
//	public void addElfmeterSchuss(boolean getroffen, boolean heim, long spielerID) {
//		if (heim) {
//			elfmeterschießen.heimGetroffen.add(getroffen);
//			elfmeterschießen.heimSpieler.add(spielerID);
//		} else {
//			elfmeterschießen.auswaertsGetroffen.add(getroffen);
//			elfmeterschießen.auswaertsSpieler.add(spielerID);
//		}
//	}
//
//	public void removeElfmeterschuss(int index, boolean heim) {
//		if (heim) {
//			elfmeterschießen.heimGetroffen.remove(index);
//			elfmeterschießen.heimSpieler.remove(index);
//		} else {
//			elfmeterschießen.auswaertsGetroffen.remove(index);
//			elfmeterschießen.auswaertsSpieler.remove(index);
//		}
//	}
//
//	public ArrayList<Boolean> getElfmeterschießen(boolean heim) {
//		if (heim) {
//			return elfmeterschießen.heimGetroffen;
//		} else {
//			return elfmeterschießen.auswaertsGetroffen;
//		}
//	}
//
//	public ArrayList<Long> getElfmeterspieler(boolean heim) {
//		if (heim) {
//			return elfmeterschießen.heimSpieler;
//		} else {
//			return elfmeterschießen.auswaertsSpieler;
//		}
//	}

	public void setNachspielzeit(boolean wahr) {
		this.nachspielzeit = wahr;
	}

	public boolean getNachspielzeit() {
		return nachspielzeit;
	}

	public Aufstellung getAufstHeim() {
		return aufstHeim;
	}

	public Aufstellung getAufstAuswaerts() {
		return aufstAuswaerts;
	}

	@Override
	public String toString() {
		if (IDPicker.pick(as.teams, heimID) != null && IDPicker.pick(as.teams, auswaertsID) != null) {
			return IDPicker.pick(as.teams, heimID).getMoeglichKN() + " : "
					+ IDPicker.pick(as.teams, auswaertsID).getMoeglichKN();
		} else {
			if (IDPicker.pick(as.teams, auswaertsID) != null) {
				return "? : " + IDPicker.pick(as.teams, auswaertsID).getMoeglichKN();
			} else if (IDPicker.pick(as.teams, heimID) != null) {
				return "? : " + IDPicker.pick(as.teams, heimID).getMoeglichKN();
			} else {
				return "? : ?";
			}
		}
	}

//	private class Elfmeterschießen {
//		private ArrayList<Boolean> heimGetroffen = new ArrayList<Boolean>();
//		private ArrayList<Boolean> auswaertsGetroffen = new ArrayList<Boolean>();
//		private ArrayList<Long> heimSpieler = new ArrayList<Long>();
//		private ArrayList<Long> auswaertsSpieler = new ArrayList<Long>();
//	}

	public <S extends Spielaktivitaet> boolean isInTime(S akt, boolean heim) {
		if (heim && aufstHeim.getAktuelleAufstellung(akt.zeit , akt.getNachspielzeit()).size() > 0) {
			if (akt.isTor()) {
				Tor t = (Tor) akt;
				if (t.getVorbereiterID() > 0 && !aufstHeim.getAktuelleAufstellung(t.getZeit() , t.getNachspielzeit())
						.contains(t.getVorbereiterID())) {
					return false;
				}
				if (t.getAusfuehrerID() > 0 && !aufstHeim.getAktuelleAufstellung(t.getZeit() , t.getNachspielzeit())
						.contains(t.getAusfuehrerID())) {
					return false;
				}
			} else if (akt.isWechsel()) {

			} else {
				Strafe s = (Strafe) akt;
				if (s.getAusfuehrerID() > 0 && !aufstHeim.getAktuelleAufstellung(s.getZeit() , s.getNachspielzeit())
						.contains(s.getAusfuehrerID())) {
					return false;
				}
				if (s.getGefoultenID() > 0 && !aufstHeim.getAktuelleAufstellung(s.getZeit() , s.getNachspielzeit())
						.contains(s.getGefoultenID())) {
					return false;
				}
			}
		} else if (!heim && aufstAuswaerts.getAktuelleAufstellung(akt.zeit , akt.getNachspielzeit()).size() > 0) {
			if (akt.getAusfuehrerID() > 0 && !aufstAuswaerts
					.getAktuelleAufstellung(akt.getZeit() , akt.getNachspielzeit()).contains(akt.getAusfuehrerID())) {
				return false;
			}
			if (akt.isTor()) {
				Tor t = (Tor) akt;
				if (t.getVorbereiterID() > 0 && !aufstAuswaerts
						.getAktuelleAufstellung(t.getZeit() , t.getNachspielzeit()).contains(t.getVorbereiterID())) {
					return false;
				}
			} else if (akt.isWechsel()) {

			} else {
				Strafe s = (Strafe) akt;
				if (s.getGefoultenID() > 0 && !aufstAuswaerts.getAktuelleAufstellung(s.getZeit() ,s.getNachspielzeit())
						.contains(s.getGefoultenID())) {
					return false;
				}
			}
		}
		return true;
	}

	public ArrayList<Spielaktivitaet> getEreignisseSortiert() {
		ArrayList<Spielaktivitaet> ereignisse = new ArrayList<Spielaktivitaet>();
		ereignisse.addAll(getHeimtoreDirekt());
		ereignisse.addAll(getAuswaertstoreDirekt());
		ereignisse.addAll(getHeimstrafen());
		ereignisse.addAll(getAuswaertsstrafen());
		ereignisse.addAll(getHeimwechsel());
		ereignisse.addAll(getAuswaertswechsel());
		Collections.sort(ereignisse);
		return ereignisse;
	}

}
