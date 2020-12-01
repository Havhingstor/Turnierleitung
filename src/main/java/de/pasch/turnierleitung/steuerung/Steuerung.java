package de.pasch.turnierleitung.steuerung;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.protagonisten.SpielerTeamConnector;
import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.spiele.Aufstellung;
import de.pasch.turnierleitung.spiele.Spiel;
import de.pasch.turnierleitung.spiele.Spielaktivitaet;
import de.pasch.turnierleitung.spiele.Strafe;
import de.pasch.turnierleitung.spiele.Tor;
import de.pasch.turnierleitung.spiele.Wechsel;
import de.pasch.turnierleitung.turnierelemente.KORunde;
import de.pasch.turnierleitung.turnierelemente.Liga;
import de.pasch.turnierleitung.turnierelemente.Runde;
import de.pasch.turnierleitung.turnierelemente.Rundensammlung;
import de.pasch.turnierleitung.turnierelemente.Spieltag;
import de.pasch.turnierleitung.turnierelemente.Turnierelement;
import javafx.util.Pair;

public class Steuerung {
	private final ArraySpeicher as = new ArraySpeicher();
	private final IDCreator idc = new IDCreator();
	private String name = "";
	private final ArrayList<String> torarten = new ArrayList<>();
	private final ArrayList<String> strafenarten = new ArrayList<>();
	public final String version = "0.7";

	public Steuerung() {
		torarten.add("Rechtsschuss");
		torarten.add("Linksschuss");
		torarten.add("Kopfball");
		torarten.add("Elfmeter");
		torarten.add("Freistoß");
		torarten.add("Sonstiges");
		strafenarten.add("Gelbe Karte");
		strafenarten.add("Rote Karte");
		strafenarten.add("Gelb-Rote Karte");
	}

	public void setLetzteID(long ID) {
		idc.setLetzteID(ID);
	}

	public long getLetzteID() {
		return idc.getLetzteID();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public ArrayList<String> getTorarten() {
		return torarten;
	}

	public ArrayList<String> getStrafenarten() {
		return strafenarten;
	}

	public void addSpieler(String vorname, String nachname, long teamID, int trikotnummer) {
		Spieler s = new Spieler(vorname, nachname, idc.createID());
		if (s.getName().equals("")) {
			throw new IllegalArgumentException("Dieser Spieler hat keinen Namen!");
		}
		boolean erlaubt = true;
		for (Spieler s2 : as.spieler) {
			if (s2.getName().equals(s.getName())) {
				erlaubt = false;
			}
		}
		if (erlaubt) {
			as.spieler.add(s);
			if (teamID > 0) {
				SpielerTeamConnector stc = new SpielerTeamConnector(s.getID(), teamID, trikotnummer, idc.createID(),
						as);
				as.connectoren.add(stc);
			}
		} else {
			throw new IllegalArgumentException("Dieser Spieler ist schon vorhanden!");
		}
	}

	public int getTrikotnummerEinesSpielers(long ID) {
		for (SpielerTeamConnector stc : as.connectoren) {
			if (stc.getSpielerID() == ID && !stc.getAusgetreten()) {
				return stc.getTrikotnummer();
			}
		}
		return 0;
	}

	public String getTrikotnummerEinesSpielersString(long ID) {
		for (SpielerTeamConnector stc : as.connectoren) {
			if (stc.getSpielerID() == ID && !stc.getAusgetreten()) {
				if (stc.getTrikotnummer() > 0) {
					return "" + stc.getTrikotnummer();
				} else {
					return "-";
				}
			}
		}
		return "-";
	}

	public void editSpieler(String vorname, String nachname, int trikotnummer, long teamID, long spielerID) {
		as.spieler.stream().filter((s) -> (s.getID() == spielerID)).forEachOrdered((s) -> {
			s.setName(vorname, nachname);
		});
		boolean wechsel = false;
		boolean schonSTC = false;
		for (int i = 0; i < as.connectoren.size(); i++) {
			SpielerTeamConnector stc = as.connectoren.get(i);
			if (spielerID == stc.getSpielerID()) {
				if ((teamID != stc.getTeamID()) && (!stc.getAusgetreten())) {
					boolean erlaubt = true;
					for (Aufstellung aufst : getAlleAufstellungen()) {
						if (aufst.getAllespieler(as.spieler).contains(IDPicker.pick(as.spieler, spielerID))
								&& !aufst.isBeendet()) {
							throw new IllegalArgumentException("A" + aufst.getSpielID());
						}

					}
					if (erlaubt) {
						stc.setAusgetreten(true);
						wechsel = true;
					}
				} else if (teamID == stc.getTeamID()) {
					stc.setAusgetreten(false);
					stc.setTrikotnummer(trikotnummer);
					schonSTC = true;
				}
			}
		}
		if (wechsel && !schonSTC) {
			SpielerTeamConnector stc2 = new SpielerTeamConnector(spielerID, teamID, trikotnummer, idc.createID(), as);
			as.connectoren.add(stc2);
		}
	}

	public void removeSpieler(long ID) {
		boolean erlaubt = true;
		for (Tor t : as.tore) {
			if (t.getAusfuehrerID() == ID) {
				erlaubt = false;
			}
			if (t.getVorbereiterID() == ID) {
				erlaubt = false;
			}
		}
		for (Strafe s : as.strafen) {
			if (s.getAusfuehrerID() == ID) {
				erlaubt = false;
			}
			if (s.getGefoultenID() == ID) {
				erlaubt = false;
			}
		}
		if (erlaubt) {
			as.connectoren.removeIf((e) -> (e.getSpielerID() == ID));
			Spieler spieler = IDPicker.pick(as.spieler, ID);
			as.spieler.remove(spieler);
		} else {
			throw new IllegalArgumentException(
					"Dieser Spieler wurde bereits bei einem Tor oder einer Strafe erfasst und kann nicht entfernt werden!");
		}
	}

	public ArrayList<Spieler> getSpieler() {
		return as.spieler;
	}

	public ArrayList<Spieler> getSpielerEinesTeams(long ID) {
		return IDPicker.pickSpielerEinesTeams(as.spieler, as.connectoren, IDPicker.pick(as.teams, ID));
	}

	public ArrayList<Spieler> getAktiveSpielerEinesTeams(long ID) {
		return IDPicker.pickAktiveSpielerEinesTeams(as.spieler, as.connectoren, IDPicker.pick(as.teams, ID));
	}

	public ArrayList<SpielerTeamConnector> getSTC() {
		return as.connectoren;
	}

	public ArrayList<Long> getSpielerIDsEinesTeams(long ID) {
		ArrayList<Long> spieler = new ArrayList<>();
		getSpielerEinesTeams(ID).forEach((s) -> {
			spieler.add(s.getID());
		});
		return spieler;
	}

	public void addTeam(String name, String kurzname, String heimstadion) {
		Team team = new Team(kurzname, name, idc.createID(), as);
		team.setHeimstadion(heimstadion);
		boolean erlaubt = true;
		for (Team t : as.teams) {
			if (t.getName().equals(team.getName())) {
				erlaubt = false;
			}
		}
		if (erlaubt) {
			as.teams.add(team);
		} else {
			throw new IllegalArgumentException("Dieses Team ist schon vorhanden!");
		}
	}

	public void editTeam(String kurzname, String name, String heimstadion, long teamID) {
		for (Team t : as.teams) {
			if (t.getID() == teamID) {
				t.setName(name);
				t.setKurzname(kurzname);
				t.setHeimstadion(heimstadion);
				for (Spiel sp : as.spiele) {
					if (sp.getHeimID() == t.getID()) {
						editSpiel(sp.getNeutralerPlatz(), sp.getSeperaterPlatzname(), sp.getStadion(), sp.getID());
					}
				}
			}
		}
	}

	public void setKapitaen(long teamID, long kapitaenID) {
		as.teams.stream().filter((t) -> (t.getID() == teamID)).map((t) -> {
			if (kapitaenID != 0) {
				if (getSpielerIDsEinesTeams(teamID).contains(kapitaenID)) {
					t.setKapitaen(IDPicker.pick(as.spieler, kapitaenID));
				} else {
					throw new IllegalArgumentException("Dieser Spieler ist nicht bei diesem Verein!");
				}
			}
			return t;
		});
	}

	public void setVizekapitaen(long teamID, long vizekapitaenID) {
		as.teams.stream().filter((t) -> (t.getID() == teamID)).map((t) -> {
			if (vizekapitaenID != 0) {
				if (getSpielerIDsEinesTeams(teamID).contains(vizekapitaenID)) {
					t.setVizekapitaen(IDPicker.pick(as.spieler, vizekapitaenID));
				} else {
					throw new IllegalArgumentException("Dieser Spieler ist nicht bei diesem Verein!");
				}
			}
			return t;
		});
	}

	public void removeTeam(long ID) {
		boolean erlaubt = true;
		for (SpielerTeamConnector stc : as.connectoren) {
			if (stc.getTeamID() == ID) {
				erlaubt = false;
				throw new IllegalArgumentException(
						"Dieses Team besitzt schon Spieler und kann deshalb nicht entfernt werden!");
			}
		}
		for (Liga liga : as.ligen) {
			if (liga.getTeamIDs().contains(ID)) {
				erlaubt = false;
				throw new IllegalArgumentException(
						"Dises Team ist schon in einer Liga aufgeführt und kann deshalb nicht entfernt werden!");
			}
		}
		for (Spiel s : as.spiele) {
			if (s.getHeimID() == ID) {
				erlaubt = false;
				throw new IllegalArgumentException(
						"Team kommt schon in einem Spiel vor und kann deshalb nicht entfernt werden!");
			} else if (s.getAuswaertsID() == ID) {
				erlaubt = false;
				throw new IllegalArgumentException(
						"Team kommt schon in einem Spiel vor und kann deshalb nicht entfernt werden!");
			}
		}
		if (erlaubt) {
			Team team = IDPicker.pick(as.teams, ID);
			as.teams.remove(team);
		}
	}

	public ArrayList<Team> getTeams() {
		return as.teams;
	}

	public ArrayList<Team> getTeamsEinesSpielers(long ID) {
		return IDPicker.pickTeamsEinesSpielers(as.teams, as.connectoren, IDPicker.pick(as.spieler, ID));
	}

	public Team getAktivesTeamEinesSpielers(long ID) {
		return IDPicker.pickAktivesTeamEinesSpielers(as.teams, as.connectoren, IDPicker.pick(as.spieler, ID));
	}

	public void addRundensammlung(String name, long korundenID) {
		KORunde kor = IDPicker.pick(as.koRunden, korundenID);
		boolean erlaubt = true;
		for (Rundensammlung rs : kor.getRundensammlungen()) {
			if (rs.getName().equals(name)) {
				erlaubt = false;
			}
		}
		if (erlaubt) {
			Rundensammlung rs = new Rundensammlung(name, idc.createID(), as, kor.getKriterium1(), kor.getKriterium2(),
					kor.getSpielanzahl());
			as.rs.add(rs);
			kor.addRundensammlung(rs.getID());
		} else {
			throw new IllegalArgumentException("In dieser KO-Runde gibt es schon eine Runde mit diesem Namen!");
		}
	}

	public void editRundensammlung(long ID, String name) {
		as.rs.stream().filter((ruS) -> (ruS.getID() == ID)).forEachOrdered((ruS) -> {
			ruS.setName(name);
		});
	}

	public void addRunde(long RSID, KORunde kor, boolean auslosung, long heimID, long auswaertsID,
			boolean neutralePlaetze, String besPlnHeim, String besPlnAuswaerts) {
		// Besonderer Platzname deaktiviert, wenn String leer
		if (auslosung) {
			long zw = 0;
			int rd = (int) (Math.random() * 100);
			if (rd > 50) {
				zw = heimID;
				heimID = auswaertsID;
				auswaertsID = zw;
			}
		}
		String heimstadion, auswaertsstadion;
		boolean besPlnHeimbl = false, besPlnAuswaertsbl = false;
		if (besPlnHeim.length() > 0) {
			besPlnHeimbl = true;
			heimstadion = besPlnHeim;
		} else {
			heimstadion = IDPicker.pick(as.teams, heimID).getHeimstadion();
		}
		if (besPlnAuswaerts.length() > 0) {
			besPlnAuswaertsbl = true;
			auswaertsstadion = besPlnAuswaerts;
		} else {
			auswaertsstadion = IDPicker.pick(as.teams, auswaertsID).getHeimstadion();
		}
		Rundensammlung rs = IDPicker.pick(as.rs, RSID);
		Runde runde = new Runde(heimID, auswaertsID, idc.createID(), as, rs.getKriteriumEins(), rs.getKriteriumZwei(),
				rs.getSpielanzahl(), heimstadion, auswaertsstadion);
		rs.addRunde(runde.getID());
		as.runden.add(runde);
		boolean erstHeim = true;
		for (int i = 0; i < rs.getSpielanzahl(); ++i) {
			if (erstHeim) {
				this.addSpiel(heimID, auswaertsID, neutralePlaetze, besPlnHeimbl, besPlnHeim, runde.getID(), kor);
			} else {
				this.addSpiel(auswaertsID, heimID, neutralePlaetze, besPlnAuswaertsbl, besPlnAuswaerts, runde.getID(),
						kor);
			}
			erstHeim = !erstHeim;
		}
	}

	public void removeRundensammlung(long ID) {
		as.rs.stream().filter((rs) -> (rs.getID() == ID)).map((rs) -> {
			rs.getRunden().forEach((r) -> {
				removeRunde(r.getID());
			});
			return rs;
		}).forEachOrdered((rs) -> {
			as.rs.remove(rs);
		});
	}

	public void removeRunde(long ID) {
		as.runden.stream().map((r) -> {
			if (r.getID() == ID) {
				r.getSpiele().forEach((l) -> {
					removeSpiel(l);
				});
			}
			return r;
		}).forEachOrdered((_item) -> {
			as.spiele.remove(IDPicker.pick(as.spiele, ID));
		});
	}

	public ArrayList<Rundensammlung> getRundensammlungen() {
		return as.rs;
	}

	public ArrayList<Runde> getRunden() {
		return as.runden;
	}

	public ArrayList<Runde> getRundenEinerRS(long ID) {
		ArrayList<Runde> runden = new ArrayList<>();
		as.rs.stream().filter((rs) -> (rs.getID() == ID)).forEachOrdered((rs) -> {
			rs.getRunden().forEach((r) -> {
				runden.add(r);
			});
		});
		return runden;
	}

	public void addSpiel(long heimID, long auswaertsID, boolean neutralerPlatz, boolean seperaterPlatzname,
			String seperaterPlatznameName, long rundenOderSpieltagID, Turnierelement te) {
		long neueID = idc.createID();
		Aufstellung heim = new Aufstellung(heimID, neueID, te.getHoechstStartelf(), te.getHoechstAuswechselspieler(),
				te.getHoechstAuswechslung(), te.getHoechstAuswechslungNachspielzeit(), idc.createID(), as);
		Aufstellung auswaerts = new Aufstellung(auswaertsID, neueID, te.getHoechstStartelf(),
				te.getHoechstAuswechselspieler(), te.getHoechstAuswechslung(), te.getHoechstAuswechslungNachspielzeit(),
				idc.createID(), as);
		Spiel spiel = new Spiel(IDPicker.pick(as.teams, heimID), IDPicker.pick(as.teams, auswaertsID), neueID,
				neutralerPlatz, as, heim, auswaerts);
		if (seperaterPlatzname) {
			spiel.setStadion(seperaterPlatznameName);
		} else {
			spiel.setStadion(IDPicker.pick(as.teams, heimID).getHeimstadion());
		}
		as.spiele.add(spiel);
		as.runden.stream().filter((runde) -> (runde.getID() == rundenOderSpieltagID)).forEachOrdered((runde) -> {
			if ((runde.getHeimteamID() == heimID && runde.getAuswaertsteamID() == auswaertsID)
					|| (runde.getHeimteamID() == auswaertsID && runde.getAuswaertsteamID() == heimID)) {
				runde.addSpiel(neueID);
			} else {
				throw new IllegalArgumentException("Das Spiel passt nicht zu der Runde!");
			}
		});
		as.spt.stream().filter((st) -> (st.getID() == rundenOderSpieltagID)).forEach((st) -> {
			st.addSpiel(neueID);
		});
	}

	public ArrayList<Spiel> getSpiele() {
		return as.spiele;
	}

	public void editSpiel(boolean neutralerPlatz, boolean seperaterPlatzname, String seperaterPlatznameName, long ID) {
		as.spiele.stream().filter((spiel) -> (spiel.getID() == ID)).map((spiel) -> {
			spiel.setNeutralerPlatz(neutralerPlatz);
			return spiel;
		}).forEachOrdered((spiel) -> {
			if (seperaterPlatzname) {
				spiel.setStadion(seperaterPlatznameName);
			} else {
				spiel.setStadion(IDPicker.pick(as.teams, spiel.getHeimID()));
			}
		});
	}

	public void removeSpiel(long ID) {
		Spiel spiel = IDPicker.pick(as.spiele, ID);
		as.spiele.remove(IDPicker.pick(as.spiele, ID));
		as.runden.stream().filter((r) -> (r.getSpiele().contains(ID))).forEach((r) -> {
			r.getSpiele().remove(ID);
		});
		as.spt.stream().filter((spt) -> (spt.getSpieleIDs().contains(ID))).forEach((spt) -> {
			spt.getSpieleIDs().remove(ID);
		});
		for (Tor t : spiel.getHeimtoreDirekt()) {
			as.tore.remove(t);
		}
		for (Tor t : spiel.getAuswaertstoreDirekt()) {
			as.tore.remove(t);
		}
		for (Strafe s : spiel.getHeimstrafen()) {
			as.strafen.remove(s);
		}
		for (Strafe s : spiel.getAuswaertsstrafen()) {
			as.strafen.remove(s);
		}
	}

	public ArrayList<Aufstellung> getAlleAufstellungen() {
		ArrayList<Aufstellung> aufst = new ArrayList<Aufstellung>();
		for (Spiel sp : as.spiele) {
			aufst.add(sp.getAufstHeim());
			aufst.add(sp.getAufstAuswaerts());
		}
		return aufst;
	}

	public void removeSpieltag(long ID) {
		Spieltag sp = IDPicker.pick(as.spt, ID);
		sp.getSpiele().stream().forEach((spiel) -> {
			removeSpiel(spiel.getID());
		});
		for (Liga lg : as.ligen) {
			lg.getSpieltageID().remove(ID);
		}
		as.spt.remove(sp);
	}

	public void addTor(boolean heimteam, long torschuetzeID, long vorlagengeberID, long spielID, int spielminute,
			int nachspielzeit, int torartIndex) {
		long teamID = 0;
		if (heimteam) {
			teamID = IDPicker.pick(as.spiele, spielID).getHeimID();
		} else {
			teamID = IDPicker.pick(as.spiele, spielID).getAuswaertsID();
		}
		Tor tor = new Tor(spielminute, nachspielzeit, IDPicker.pick(as.spieler, torschuetzeID),
				IDPicker.pick(as.spieler, vorlagengeberID), IDPicker.pick(as.teams, teamID), idc.createID(), torarten,
				as);
		tor.setTyp(torartIndex);
		as.spiele.stream().filter((spiel) -> (spiel.getID() == spielID)).forEach((spiel) -> {
			spiel.addTor(tor);
		});
		as.tore.add(tor);
	}

	public void addTor(boolean heimteam, long torschuetzeID, long vorlagengeberID, long spielID, int spielminute,
			int nachspielzeit, String torart) {
		int index = 0;
		for (int i = 0; i < torarten.size(); ++i) {
			if (torart.equals(torarten.get(i))) {
				index = i;
			}
		}
		addTor(heimteam, torschuetzeID, vorlagengeberID, spielID, spielminute, nachspielzeit, index);
	}

	public void editTor(long torschuetzeID, long vorlagengeberID, int spielminute, int nachspielzeit, int torartIndex,
			long ID) {
		as.tore.stream().filter((tor) -> (tor.getID() == ID)).map((tor) -> {
			tor.setTyp(torartIndex);
			return tor;
		}).map((tor) -> {
			tor.setAusfuehrerID(torschuetzeID);
			return tor;
		}).map((tor) -> {
			tor.setVorbereiterID(vorlagengeberID);
			return tor;
		}).forEachOrdered((tor) -> {
			tor.setZeit(spielminute, nachspielzeit);
		});
	}

	public void removeTor(long ID) {
		as.tore.stream().filter((tor) -> (tor.getID() == ID)).forEachOrdered((tor) -> {
			as.spiele.stream().filter(
					(spiel) -> (spiel.getHeimtoreIDs().contains(ID) || spiel.getAuswaertstoreIDs().contains(ID)))
					.forEachOrdered((spiel) -> {
						spiel.removeTor(ID);
					});
			// as.tore.remove(tor);
		});
		as.tore.removeIf((t) -> (t.getID() == ID));
	}

	public ArrayList<Tor> getTore() {
		return as.tore;
	}

	public void addStrafe(boolean heimteam, long foulenderID, long gefoulterID, long spielID, int spielminute,
			int nachspielzeit, int strafenartIndex) {
		long teamID = 0;
		if (heimteam) {
			teamID = IDPicker.pick(as.spiele, spielID).getHeimID();
		} else {
			teamID = IDPicker.pick(as.spiele, spielID).getAuswaertsID();
		}
		Strafe strafe = new Strafe(spielminute, nachspielzeit, IDPicker.pick(as.spieler, foulenderID),
				IDPicker.pick(as.spieler, gefoulterID), IDPicker.pick(as.teams, teamID), idc.createID(), strafenarten,
				as);
		strafe.setTyp(strafenartIndex);
		IDPicker.pick(as.spiele, spielID).addStrafe(strafe);
		as.strafen.add(strafe);
	}

	public void addStrafe(boolean heimteam, long foulenderID, long gefoulterID, long spielID, int spielminute,
			int nachspielzeit, String strafenart) {
		int index = 0;
		for (int i = 0; i < strafenarten.size(); ++i) {
			if (strafenart.equals(torarten.get(i))) {
				index = i;
			}
		}
		addStrafe(heimteam, foulenderID, gefoulterID, spielID, spielminute, nachspielzeit, index);
	}

	public void editStrafe(long foulenderID, long gefoulterID, int spielminute, int nachspielzeit, int strafenartIndex,
			long ID) {
		as.strafen.stream().filter((strafe) -> (strafe.getID() == ID)).map((strafe) -> {
			strafe.setTyp(strafenartIndex);
			return strafe;
		}).map((strafe) -> {
			strafe.setAusfuehrerID(foulenderID);
			return strafe;
		}).map((strafe) -> {
			strafe.setGefoultenID(gefoulterID);
			return strafe;
		}).map((strafe) -> {
			return strafe;
		}).forEachOrdered((strafe) -> {
			strafe.setZeit(spielminute, nachspielzeit);
		});
	}

	public void removeStrafe(long ID) {
		as.strafen.stream().filter((strafe) -> (strafe.getID() == ID)).forEachOrdered((strafe) -> {
			as.spiele.stream().filter(
					(spiel) -> (spiel.getHeimstrafenIDs().contains(ID) || spiel.getAuswaertsstrafenIDs().contains(ID)))
					.forEachOrdered((spiel) -> {
						spiel.removeStrafe(ID);
					});
			// as.strafen.remove(strafe);
		});
		as.strafen.removeIf((s) -> (s.getID() == ID));
	}

	public ArrayList<Strafe> getStrafen() {
		return as.strafen;
	}

	public void addWechsel(boolean heimteam, int zeit, int nachspielzeit, Spieler eingewechselt, Spieler ausgewechselt,
			Spiel spiel) {
		Wechsel neuerW = new Wechsel(zeit, nachspielzeit, eingewechselt, ausgewechselt, idc.createID(),
				heimteam ? spiel.getHeimID() : spiel.getAuswaertsID(), as);
		if (!ereignisseSpaeter(zeit, nachspielzeit, ausgewechselt, spiel, false, null)) {
			if (heimteam) {
				spiel.getAufstHeim().addWechsel(zeit, nachspielzeit, ausgewechselt.getID(), eingewechselt.getID());
				spiel.addWechsel(neuerW);
			} else {
				spiel.getAufstAuswaerts().addWechsel(zeit, nachspielzeit, ausgewechselt.getID(), eingewechselt.getID());
				spiel.addWechsel(neuerW);
			}
			as.wechsel.add(neuerW);
		} else {
			throw new IllegalArgumentException("Ausgewechselter Spieler war danach noch in Ereignisse involviert");
		}

	}

	public void removeWechsel(long ID, Spiel spiel) {
		Wechsel wechsel = IDPicker.pick(as.wechsel, ID);
		Spieler eingewechselt = wechsel.getAusfuehrer();
		Spieler ausgewechselt = wechsel.getAusgewechselt();
		if (!ereignisseSpaeter(wechsel.getZeit(), wechsel.getNachspielzeit(), eingewechselt, spiel, true, wechsel)) {
			spiel.removeWechsel(ID);
			spiel.getAufstHeim().removeWechsel(ausgewechselt.getID(), eingewechselt.getID());
			spiel.getAufstAuswaerts().removeWechsel(ausgewechselt.getID(), eingewechselt.getID());
			as.wechsel.remove(wechsel);
		} else {
			throw new IllegalArgumentException("Eingewechselter Spieler war danach noch in Ereignisse involviert");
		}
	}

	public boolean ereignisseSpaeter(int zeit, int nachspielzeit, Spieler spieler, Spiel spiel, boolean mitAktuell,
			Spielaktivitaet ereignisLoeschen) {
		ArrayList<Spielaktivitaet> ereignisse = spiel.getEreignisseSortiert();
		for (int i = ereignisse.size() - 1; i >= 0; --i) {
			Spielaktivitaet ereignis = ereignisse.get(i);
			if ((ereignis.getZeit() > zeit)
					|| (ereignis.getZeit() == zeit && ((mitAktuell && ereignis.getNachspielzeit() >= nachspielzeit)
							|| (ereignis.getNachspielzeit() > nachspielzeit)))) {
				if (ereignis.isTor()&&!ereignis.equals(ereignisLoeschen)) {
					Tor tor = (Tor) ereignis;
					if (tor.getAusfuehrerID() == spieler.getID() || tor.getVorbereiterID() == spieler.getID()) {
						return true;
					}
				} else if (ereignis.isWechsel()&&!ereignis.equals(ereignisLoeschen)) {
					Wechsel wechsel = (Wechsel) ereignis;
					if (wechsel.getAusfuehrerID() == spieler.getID()
							|| wechsel.getAusgewechselt().getID() == spieler.getID()) {
						return true;
					}
				}
			} else {
				return false;
			}

		}
		return false;
	}

	public void addElfmeterschießen(long ID) {
		as.spiele.stream().filter((spiel) -> (spiel.getID() == ID)).forEachOrdered((spiel) -> {
			spiel.addBenoetigtesElfmeterschießen();
		});
	}

	public void addElfmeterschießenSchuss(long spielID, boolean heim, boolean getroffen, long spielerID) {
		as.spiele.stream().filter((spiel) -> (spiel.getID() == spielID))
				.filter((spiel) -> (spiel.istElfmeterschießen())).forEachOrdered((spiel) -> {
					spiel.addElfmeterSchuss(getroffen, heim, spielerID);
				});
	}

	public void addLiga(int pps, int ppu, int ppn, String name, int[] rk) {
		Liga liga = new Liga(pps, ppu, ppn, name, idc.createID(), as, rk);
		as.ligen.add(liga);
	}

	public ArrayList<Liga> getLigen() {
		return as.ligen;
	}

	public ArrayList<Turnierelement> getTurnierelemente() {
		ArrayList<Turnierelement> tes = new ArrayList<Turnierelement>();
		tes.addAll(as.ligen);
		tes.addAll(as.koRunden);
		return tes;
	}

	public void removeLiga(long ID) {
		Liga liga = IDPicker.pick(as.ligen, ID);
		for (Spieltag spt : liga.getSpieltage()) {
			this.removeSpieltag(spt.getID());
		}
		as.ligen.remove(liga);
	}

	public void addTeamZuLiga(long teamID, long ligaID) {
		IDPicker.pick(as.ligen, ligaID).addTeam(teamID);
	}

	public Spieltag addSpieltag(long ligaID, String name) {
		boolean erlaubt = true;
		for (Spieltag spt : IDPicker.pick(as.ligen, ligaID).getSpieltage()) {
			if (spt.getName().equals(name)) {
				erlaubt = false;
			}
		}
		if (erlaubt) {
			Spieltag spt = new Spieltag(idc.createID(), name, as);
			as.ligen.stream().filter((l) -> (l.getID() == ligaID)).forEachOrdered((l) -> {
				l.addSpieltag(spt);
			});
			as.spt.add(spt);
			return spt;
		} else {
			throw new IllegalArgumentException(
					"In diesem Turnierelement gibt es schon einen Spieltag mit diesem Namen!");
		}
	}

	public ArrayList<Spieltag> getSpieltage() {
		return as.spt;
	}

	public void addAusgelosterSpieltag(long ligaID) {
		addSpieltag(ligaID, IDPicker.pick(as.ligen, ligaID).getSpieltage().size() + 1 + ". Spieltag");
		IDPicker.pick(as.ligen, ligaID).auslosen(idc).stream().map((s) -> {
			as.spiele.add(s);
			return s;
		}).forEachOrdered((s) -> {
			IDPicker.pick(as.ligen, ligaID).getSpieltage()
					.get(IDPicker.pick(as.ligen, ligaID).getSpieltage().size() - 1).addSpiel(s.getID());
		});
	}

	public void addAusgelosterSpieltag(long ligaID, String name) {
		addSpieltag(ligaID, name);
		IDPicker.pick(as.ligen, ligaID).auslosen(idc).stream().map((s) -> {
			as.spiele.add(s);
			return s;
		}).forEachOrdered((s) -> {
			IDPicker.pick(as.ligen, ligaID).getSpieltage()
					.get(IDPicker.pick(as.ligen, ligaID).getSpieltage().size() - 1).addSpiel(s.getID());
		});
	}

	public void ligaspielplanBerechnen(long ligaID, boolean rückrunde) {
		Liga liga = IDPicker.pick(as.ligen, ligaID);
		int spieltaganzahl = 0;
		if (liga.getTeams().size() % 2 == 0) {
			spieltaganzahl = (liga.getTeams().size() - 1) * 2;
		} else {
			spieltaganzahl = liga.getTeams().size() * 2;
		}
		if (liga.getSpieltage().size() != 1) {
			throw new IllegalArgumentException("Die Liga muss genau einen Spieltag enthalten!");
		}
		for (int i = 0; i < spieltaganzahl / 2 - 1; i++) {
			liga.getSpieltage().get(0).setName("1. Spieltag");
			ArrayList<Spiel> spiele = liga.fortfuehrenNaechsterSpieltag(idc);
			Spieltag spieltag = new Spieltag(idc.createID(), i + 2 + ". Spieltag", as);
			liga.addSpieltag(spieltag);
			as.spt.add(spieltag);
			for (Spiel spiel : spiele) {
				as.spiele.add(spiel);
				spieltag.addSpiel(spiel.getID());
			}
		}
		if (rückrunde) {
			createRueckrunde(ligaID);
		}
	}

	public void createRueckrunde(long ligaID) {
		Liga liga = IDPicker.pick(as.ligen, ligaID);
		ArrayList<Spieltag> spieltage = liga.getSpieltage();
		spieltage.forEach((st) -> {
			Spieltag spieltag = new Spieltag(idc.createID(), liga.getSpieltage().size() + 1 + ". Spieltag", as);
			liga.addSpieltag(spieltag);
			as.spt.add(spieltag);
			st.getSpiele().stream().map((sp) -> {
				long heimID = sp.getAuswaertsID();
				long auswaertsID = sp.getHeimID();
				long neueID = idc.createID();
				Liga te = IDPicker.pick(as.ligen, ligaID);
				Aufstellung heim = new Aufstellung(heimID, neueID, te.getHoechstStartelf(),
						te.getHoechstAuswechselspieler(), te.getHoechstAuswechslung(),
						te.getHoechstAuswechslungNachspielzeit(), idc.createID(), as);
				Aufstellung auswaerts = new Aufstellung(auswaertsID, neueID, 0, 0, 0, 0, idc.createID(), as);
				Spiel spiel = new Spiel(IDPicker.pick(as.teams, heimID), IDPicker.pick(as.teams, auswaertsID), neueID,
						false, as, heim, auswaerts);
				return spiel;
			}).map((spiel) -> {
				as.spiele.add(spiel);
				return spiel;
			}).forEachOrdered((spiel) -> {
				spieltag.addSpiel(spiel.getID());
			});
		});
	}

	public void addKORunde(String name, int k1, int k2, int spielanzahl) {
		boolean erlaubt = true;
		for (KORunde kor : as.koRunden) {
			if (kor.getName().equals(name)) {
				erlaubt = false;
			}
		}
		for (Liga liga : as.ligen) {
			if (liga.getName() == name) {
				erlaubt = false;
			}
		}
		if (erlaubt && !name.equals("")) {
			KORunde kor = new KORunde(idc.createID(), as, name, k1, k2, spielanzahl);
			as.koRunden.add(kor);
		} else {
			if (erlaubt) {
				throw new IllegalArgumentException("Es wurde kein Name eingetragen");
			} else {
				throw new IllegalArgumentException("Dieser Name ist schon für eine Turnierelement vergeben");
			}
		}
	}

	public KORunde getKORunde(long ID) {
		return IDPicker.pick(as.koRunden, ID);
	}

	public void removeKORunde(long ID) {
		if (IDPicker.pick(as.koRunden, ID).getRundensammlungen().size() > 0) {
			throw new IllegalArgumentException("Diese KO-Runde enthält bereits Runden!");
		} else {
			as.koRunden.remove(IDPicker.pick(as.koRunden, ID));
		}
	}

	public ArrayList<KORunde> getKORunden() {
		return as.koRunden;
	}

	public Liga getLiga(long ID) {
		return IDPicker.pick(as.ligen, ID);
	}

	public void editKORunde(long ID, String name) {
		KORunde kor = IDPicker.pick(as.koRunden, ID);
		kor.setName(name);
	}

	public void editLiga(long ID, String name, int pps, int ppu, int ppn, int[] rk) {
		Liga liga = IDPicker.pick(as.ligen, ID);
		liga.setName(name);
		liga.setPunkteProSieg(pps);
		liga.setPunkteProUnentschieden(ppu);
		liga.setPunkteProNiederlage(ppn);
		liga.setReihenfolgeKriterien(rk);
	}

	public void regeneriereAusDatei(File datei) throws IOException, JDOMException {
		Document doc = new SAXBuilder().build(datei);
		Element re = doc.getRootElement();
		name = re.getAttributeValue("Name");
		if (!version.equals(re.getAttributeValue("Version"))) {
			JOptionPane.showMessageDialog(null,
					"Diese Datei stammt aus einer anderen Version, möglicherweise werden nicht alle Daten richtig eingelesen!",
					"ACHTUNG!", 2);
		}
		for (Element e : re.getChildren()) {
			if (e.getName().equals("Torarten")) {
				torarten.clear();
				for (Element child : e.getChildren()) {
					torarten.add(child.getValue());
				}
			} else if (e.getName().equals("Strafenarten")) {
				strafenarten.clear();
				for (Element child : e.getChildren()) {
					strafenarten.add(child.getValue());
				}
			}
		}
	}

	/*
	 * FileInputStream fis=new FileInputStream(datei); InputStreamReader isr=new
	 * InputStreamReader(fis); BufferedReader br=new BufferedReader(isr);
	 * 
	 * XMLTagSystem xml=new XMLTagSystem(br); XMLTag tag=xml.getTagSystem();
	 * XMLChildTag aktTag; boolean erlaubt=true; try {
	 * aktTag=tag.getChildTags().get(0); }catch(IndexOutOfBoundsException ioobe) {
	 * erlaubt=false; throw new
	 * XMLException("Diese Datei kann nicht entschlüsselt werden!"); } if(erlaubt) {
	 * name=aktTag.getName(); for(XMLChildTag chTag:aktTag.getChildTags()) {
	 * System.out.println(chTag.getName()); if(chTag.getName().equals("IDCreator"))
	 * { idc.setLetzteID(Long.parseLong(chTag.getAttributes().get(0))); }else
	 * if(chTag.getName().equals("Torarten")) { torarten.clear();
	 * torarten.addAll(chTag.getAttributes()); } } }
	 */

	public Element getRootElement() {
		Element el = new Element("Turnier");
		el.setAttribute(new Attribute("Name", name));
		el.setAttribute(new Attribute("Version", version));
		Element torartenElement = new Element("Torarten");
		el.addContent(torartenElement);
		for (String torart : torarten) {
			Element torartEl = new Element("Torart");
			torartEl.addContent(torart);
			torartenElement.addContent(torartEl);
		}
		Element strafenartenElement = new Element("Strafenarten");
		el.addContent(strafenartenElement);
		for (String strafenart : strafenarten) {
			Element strafenartEl = new Element("Strafenart");
			strafenartEl.addContent(strafenart);
			strafenartenElement.addContent(strafenartEl);
		}
		as.createXMLElements(el);
		return el;
	}

	public static void createALElements(Element parEl, ArrayList<? extends Object> list, String bez) {
		for (Object o : list) {
			Element el = new Element(bez);
			parEl.addContent(el);
			el.addContent(o.toString());
		}
	}

	public Pair<Liga, Spieltag> getLSVonSpiel(long ID) {
		Spieltag rSpt = null;
		for (Spieltag spt : as.spt) {
			if (spt.getSpieleIDs().contains(ID)) {
				rSpt = spt;
			}
		}
		Liga rLig = null;
		for (Liga lig : as.ligen) {
			if (lig.getSpieltageID().contains(rSpt.getID())) {
				rLig = lig;
			}
		}
		return new Pair<Liga, Spieltag>(rLig, rSpt);
	}
}
