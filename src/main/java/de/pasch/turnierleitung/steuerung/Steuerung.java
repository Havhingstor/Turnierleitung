package de.pasch.turnierleitung.steuerung;

import java.util.ArrayList;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.protagonisten.SpielerTeamConnector;
import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.spiele.Spiel;
import de.pasch.turnierleitung.spiele.Strafe;
import de.pasch.turnierleitung.spiele.Tor;
import de.pasch.turnierleitung.turnierelemente.KORunde;
import de.pasch.turnierleitung.turnierelemente.Liga;
import de.pasch.turnierleitung.turnierelemente.Runde;
import de.pasch.turnierleitung.turnierelemente.Rundensammlung;
import de.pasch.turnierleitung.turnierelemente.Spieltag;

public class Steuerung {
	private final ArraySpeicher as=new ArraySpeicher();
	private final IDCreator idc=new IDCreator();
	private String name="";
	private final ArrayList<String>torarten=new ArrayList<>();
	private final ArrayList<String>strafenarten=new ArrayList<>();
	public final String version="1.0";
	
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
		this.name=name;
	}
	
	public String getName() {
		return name;
	}
	
        public ArrayList<String>getTorarten(){
            return torarten;
        }

        public ArrayList<String>getStrafenarten(){
            return strafenarten;
        }
        
	public void addSpieler(String vorname,String nachname,long teamID,int trikotnummer) {
		Spieler s=new Spieler(vorname,nachname,idc.createID());
		boolean erlaubt=true;
		for(Spieler s2:as.spieler) {
			if(s2.getName().equals(s.getName())) {
				erlaubt=false;
			}
		}
		if(erlaubt) {
			as.spieler.add(s);
			if(teamID>0) {
				SpielerTeamConnector stc=new SpielerTeamConnector(s.getID(),teamID,
						trikotnummer,idc.createID(),as);
				as.connectoren.add(stc);
			}
		}else {
			throw new IllegalArgumentException("Dieser Spieler ist schon vorhanden!");
		}
	}
	
	public void editSpieler(String vorname,String nachname,int trikotnummer,long teamID,long spielerID) {
        as.spieler.stream().filter((s) -> (s.getID()==spielerID)).forEachOrdered((s) -> {
            s.setName(vorname, nachname);
        });
        boolean wechsel=false;
        for(int i=0;i<as.connectoren.size();i++) {
        	SpielerTeamConnector stc=as.connectoren.get(i);
        	if(spielerID==stc.getSpielerID()) {
	            if(teamID!=stc.getTeamID()) {
	                stc.setAusgetreten(true);
	                wechsel=true;
	            }else {
	                stc.setTrikotnummer(trikotnummer);
	            }
        	}
        } 
        if(wechsel) {
	        SpielerTeamConnector stc2=new SpielerTeamConnector(spielerID, teamID, trikotnummer, spielerID,as);
	        as.connectoren.add(stc2);
		}
	}
	
	public void removeSpieler(long ID) {
		boolean erlaubt=true;
		for(Tor t:as.tore) {
			if(t.getAusfuehrerID()==ID) {
				erlaubt=false;
			}
			if(t.getVorbereiterID()==ID) {
				erlaubt=false;
			}
		}
		for(Strafe s:as.strafen) {
			if(s.getAusfuehrerID()==ID) {
				erlaubt=false;
			}
			if(s.getGefoultenID()==ID) {
				erlaubt=false;
			}
		}
		if(erlaubt) {
			as.connectoren.removeIf((e)->(e.getSpielerID()==ID));
			Spieler spieler=IDPicker.pick(as.spieler, ID);
			as.spieler.remove(spieler);
		}else {
			throw new IllegalArgumentException("Dieser Spieler wurde bereits bei einem Tor oder einer Strafe erfasst und kann nicht entfernt werden!");
		}
	}
	
	public ArrayList<Spieler>getSpieler(){
		return as.spieler;
	}
	
	public ArrayList<Spieler>getSpielerEinesTeams(long ID){
		return IDPicker.pickSpielerEinesTeams(as.spieler,as.connectoren,IDPicker.pick(as.teams,ID));
	}
	
	public ArrayList<Spieler>getAktiveSpielerEinesTeams(long ID){
		return IDPicker.pickAktiveSpielerEinesTeams(as.spieler,as.connectoren,IDPicker.pick(as.teams,ID));
	}
	
        public ArrayList<SpielerTeamConnector>getSTC(){
            return as.connectoren;
        }
        
	public ArrayList<Long>getSpielerIDsEinesTeams(long ID){
		ArrayList<Long>spieler=new ArrayList<>();
                getSpielerEinesTeams(ID).forEach((s) -> {
                    spieler.add(s.getID());
            });
		return spieler;
	}
	
	public void addTeam(String name,String kurzname,String heimstadion) {
		Team team=new Team(kurzname,name,idc.createID(),as);
		team.setHeimstadion(heimstadion);
		boolean erlaubt=true;
		for(Team t:as.teams) {
			if(t.getName().equals(team.getName())) {
				erlaubt=false;
			}
		}
		if(erlaubt) {
			as.teams.add(team);
		}else {
			throw new IllegalArgumentException("Dieses Team ist schon vorhanden!");
		}
	}
	
	public void editTeam(String kurzname,String name,String heimstadion,long teamID) {
            for(Team t:as.teams){
                if(t.getID()==teamID){
                    t.setName(name);
                    t.setKurzname(kurzname);
                    t.setHeimstadion(heimstadion);
                }
            }
	}
	
        public void setKapitaen(long teamID,long kapitaenID){
            as.teams.stream().filter((t)->(t.getID()==teamID))
                    .map((t) -> {
                if(kapitaenID!=0) {
                    if(getSpielerIDsEinesTeams(teamID).contains(kapitaenID)) {
                        t.setKapitaen(IDPicker.pick(as.spieler,kapitaenID));
                    }else {
                        throw new IllegalArgumentException("Dieser Spieler ist nicht bei diesem Verein!");
                    }
                }
                return t;
            });
        }
        
        public void setVizekapitaen(long teamID,long vizekapitaenID){
            as.teams.stream().filter((t)->(t.getID()==teamID))
                    .map((t) -> {
                if(vizekapitaenID!=0) {
                    if(getSpielerIDsEinesTeams(teamID).contains(vizekapitaenID)) {
                        t.setVizekapitaen(IDPicker.pick(as.spieler,vizekapitaenID));
                    }else {
                        throw new IllegalArgumentException("Dieser Spieler ist nicht bei diesem Verein!");
                    }
                }
                return t;
            });
        }
        
	public void removeTeam(long ID) {
		boolean erlaubt=true;
		for(SpielerTeamConnector stc:as.connectoren) {
			if(stc.getTeamID()==ID) {
				erlaubt=false;
                                throw new IllegalArgumentException("Dises Team besitzt schon Spieler und kann deshalb nicht entfernt werden!");
			}
		}
                for(Spiel s:as.spiele) {
			if(s.getHeimID()==ID) {
				erlaubt=false;
                                throw new IllegalArgumentException("Team kommt schon in einem Spiel vor und kann deshalb nicht entfernt werden!");
			}else if(s.getAuswaertsID()==ID){
                            erlaubt=false;
                            throw new IllegalArgumentException("Team kommt schon in einem Spiel vor und kann deshalb nicht entfernt werden!");
                        }
		}
		if(erlaubt) {
			Team team=IDPicker.pick(as.teams, ID);
			as.teams.remove(team);
		}
	}
	
	public ArrayList<Team>getTeams(){
		return as.teams;
	}
	
	public ArrayList<Team>getTeamsEinesSpielers(long ID){
		return IDPicker.pickTeamsEinesSpielers(as.teams,as.connectoren,IDPicker.pick(as.spieler,ID));
	}
	
	public Team getAktivesTeamEinesSpielers(long ID){
		return IDPicker.pickAktivesTeamEinesSpielers(as.teams,as.connectoren,IDPicker.pick(as.spieler,ID));
	}
	
	public void createRundensammlung(String name) {
		Rundensammlung rs=new Rundensammlung(name,idc.createID(),as);
		as.rs.add(rs);
	}
	
	public void editRundensammlung(long ID,String name) {
            as.rs.stream().filter((ruS) -> (ruS.getID()==ID)).forEachOrdered((ruS) -> {
                ruS.setName(name);
            });
	}
	
	public void createRunde(long RSID,boolean auslosung,long heimID,long auswaertsID) {
		if(auslosung) {
			long zw=0;
			int rd=(int)(Math.random()*100);
			if(rd>50) {
				zw=heimID;
				heimID=auswaertsID;
				auswaertsID=zw;
			}
		}
		Runde runde=new Runde(heimID,auswaertsID,idc.createID(),as); 
                as.rs.stream().filter((rs) -> (rs.getID()==RSID)).forEachOrdered((rs) -> {
                    rs.addRunde(runde.getID());
            });
		as.runden.add(runde);
	}
	
	public void removeRundensammlung(long ID) {
            as.rs.stream().filter((rs) -> (rs.getID()==ID)).map((rs) -> {
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
                if(r.getID()==ID) {
                    r.getSpiele().forEach((l) -> {
                        removeSpiel(l);
                    });
                }
                return r;
            }).forEachOrdered((_item) -> {
                as.spiele.remove(IDPicker.pick(as.spiele, ID));
            });
	}
	
	public ArrayList<Rundensammlung> getRundensammlungen(){
		return as.rs;
	}
	
	public ArrayList<Runde>getRunden(){
		return as.runden;
	}
	
	public ArrayList<Runde>getRundenEinerRS(long ID){
		ArrayList<Runde> runden=new ArrayList<>();
                as.rs.stream().filter((rs) -> (rs.getID()==ID)).forEachOrdered((rs) -> {
                    rs.getRunden().forEach((r) -> {
                        runden.add(r);
                    });
            });
		return runden;
	}
	
	public void addSpiel(long heimID,long auswaertsID,boolean neutralerPlatz,boolean seperaterPlatzname,String seperaterPlatznameName,
			long rundenOderSpieltagID) {
		long neueID=idc.createID();
		Spiel spiel=new Spiel(IDPicker.pick(as.teams,heimID),IDPicker.pick(as.teams,auswaertsID),neueID, neutralerPlatz,as);
		if(seperaterPlatzname) {
			spiel.setStadion(seperaterPlatznameName);
		}else {
			spiel.setStadion(IDPicker.pick(as.teams,heimID).getHeimstadion());
		}
		as.spiele.add(spiel);
                as.runden.stream().filter((runde) -> (runde.getID()==rundenOderSpieltagID)).forEachOrdered((runde) -> {
                    if((runde.getHeimteamID()==heimID&&runde.getAuswaertsteamID()==auswaertsID)||
                            (runde.getHeimteamID()==auswaertsID&&runde.getAuswaertsteamID()==auswaertsID)) {
                        runde.addSpiel(neueID);
                    }else {
                        throw new IllegalArgumentException("Das Spiel passt nicht zu der Runde!");
                    }
            });
                as.spt.stream().filter((st) -> (st.getID()==rundenOderSpieltagID)).forEachOrdered((st) -> {
                    st.addSpiel(neueID);
            });
	}
	
	public ArrayList<Spiel> getSpiele() {
		return as.spiele;
	}
	
	public void editSpiel(boolean neutralerPlatz,boolean seperaterPlatzname,String seperaterPlatznameName,long ID) {
            as.spiele.stream().filter((spiel) -> (spiel.getID()==ID)).map((spiel) -> {
                spiel.setNeutralerPlatz(neutralerPlatz);
                return spiel;
            }).forEachOrdered((spiel) -> {
                if(seperaterPlatzname) {
                    spiel.setStadion(seperaterPlatznameName);
                }else {
                    spiel.setStadion(IDPicker.pick(as.teams,spiel.getHeimID()));
                }
            });
	}
	
	public void removeSpiel(long ID) {
		as.spiele.remove(IDPicker.pick(as.spiele,ID));
                as.aufstellungen.stream().filter((a) -> (a.getSpielID()==ID)).forEachOrdered((a) -> {
                    as.aufstellungen.remove(a);
            });
                as.runden.stream().filter((r) -> (r.getSpiele().contains(ID))).forEachOrdered((r) -> {
                    r.getSpiele().remove(ID);
            });
	}
	
	public void addTor(boolean heimteam,long torschuetzeID,long vorlagengeberID,long spielID, int spielminute,
			int nachspielzeit,int torartIndex) {
		long teamID=0;
		if(heimteam) {
			teamID=IDPicker.pick(as.spiele,spielID).getHeimID();
		}else {
			teamID=IDPicker.pick(as.spiele,spielID).getAuswaertsID();
		}
		Tor tor=new Tor(spielminute,nachspielzeit,IDPicker.pick(as.spieler,torschuetzeID),IDPicker.pick(as.spieler,vorlagengeberID),
				IDPicker.pick(as.teams,teamID),idc.createID(),torarten,as);
		tor.setTyp(torartIndex);
                as.spiele.stream().filter((spiel) -> (spiel.getID()==spielID)).forEachOrdered((spiel) -> {
                    spiel.addTor(tor);
            });
		as.tore.add(tor);		
	}
	
	public void editTor(long torschuetzeID,long vorlagengeberID,int spielminute,
			int nachspielzeit,int torartIndex,long ID) {
            as.tore.stream().filter((tor) -> (tor.getID()==ID)).map((tor) -> {
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
            as.tore.stream().filter((tor) -> (tor.getID()==ID)).forEachOrdered((tor) -> {
                as.spiele.stream().filter((spiel) -> (spiel.getHeimtoreIDs().contains(ID)||spiel.getAuswaertstoreIDs().contains(ID))).forEachOrdered((spiel) -> {
                    spiel.removeTor(ID);
                });
                as.tore.remove(tor);
            });
	}
	
	public ArrayList<Tor>getTore(){
		return as.tore;
	}
	
	public void addStrafe(boolean heimteam,long foulenderID,long gefoulterID,long spielID, int spielminute,
			int nachspielzeit,int strafenartIndex) {
		long teamID=0;
		if(heimteam) {
			teamID=IDPicker.pick(as.spiele,spielID).getHeimID();
		}else {
			teamID=IDPicker.pick(as.spiele,spielID).getAuswaertsID();
		}
		Strafe strafe=new Strafe(spielminute,nachspielzeit,IDPicker.pick(as.spieler,foulenderID),IDPicker.pick(as.spieler,gefoulterID),
				IDPicker.pick(as.teams,teamID),idc.createID(),strafenarten,as);
		strafe.setTyp(strafenartIndex);
                as.spiele.stream().filter((spiel) -> (spiel.getID()==spielID)).forEachOrdered((spiel) -> {
                    spiel.addStrafe(strafe);
            });
		as.strafen.add(strafe);
	}
	
	public void editStrafe(long foulenderID,long gefoulterID,int spielminute,
			int nachspielzeit,int strafenartIndex,long ID) {
            as.strafen.stream().filter((strafe) -> (strafe.getID()==ID)).map((strafe) -> {
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
            as.strafen.stream().filter((strafe) -> (strafe.getID()==ID)).forEachOrdered((strafe) -> {
                as.spiele.stream().filter((spiel) -> (spiel.getHeimstrafenIDs().contains(ID)||spiel.getAuswaertsstrafenIDs().contains(ID))).forEachOrdered((spiel) -> {
                    spiel.removeStrafe(ID);
                });
                as.strafen.remove(strafe);
            });
	}
	
	public ArrayList<Strafe>getStrafen(){
		return as.strafen;
	}
	
	public void addElfmeterschießen(long ID) {
            as.spiele.stream().filter((spiel) -> (spiel.getID()==ID)).forEachOrdered((spiel) -> {
                spiel.addBenoetigtesElfmeterschießen();
            });
	}
	
	public void addElfmeterschießenSchuss(long spielID,boolean heim, boolean getroffen,long spielerID) {
            as.spiele.stream().filter((spiel) -> (spiel.getID()==spielID)).filter((spiel) -> (spiel.istElfmeterschießen())).forEachOrdered((spiel) -> {
                spiel.addElfmeterSchuss(getroffen, heim, spielerID);
            });
	}
	
	public void addLiga(int pps,int ppu,int ppn,String name,int[]rk) {
		Liga liga=new Liga(pps,ppu,ppn,name,idc.createID(),as,rk);
		as.ligen.add(liga);
	}
	
	public ArrayList<Liga>getLigen(){
		return as.ligen;
	}
	
	public void removeLiga(long ID) {
		if(IDPicker.pick(as.ligen, ID).getSpieltage().size()>0) {
			throw new IllegalArgumentException("Diese Liga enth�lt bereits Spieltage!");
		}
	}
	
	public void addTeamZuLiga(long teamID,long ligaID) {
		IDPicker.pick(as.ligen,ligaID).addTeam(teamID);
	}
	
	public void addSpieltag(long ligaID,String name) {
		Spieltag spt=new Spieltag(idc.createID(),name,as);
                as.ligen.stream().filter((l) -> (l.getID()==ligaID)).forEachOrdered((l) -> {
                    l.addSpieltag(spt);
            });
		as.spt.add(spt);
	}
	
	public ArrayList<Spieltag>getSpieltage(){
		return as.spt;
	}
	
	public void addAusgelosterSpieltag(long ligaID) {
		addSpieltag(ligaID,IDPicker.pick(as.ligen,ligaID).getSpieltage().size()+1+". Spieltag");
                IDPicker.pick(as.ligen,ligaID).auslosen(idc).stream().map((s) -> {
                    as.spiele.add(s);
                return s;
            }).forEachOrdered((s) -> {
                IDPicker.pick(as.ligen,ligaID).getSpieltage().get(IDPicker.pick(as.ligen,ligaID).getSpieltage().size()).addSpiel(s.getID());
            });
	}
	
	public void ligaspielplanBerechnen(long ligaID,boolean rückrunde) {
		Liga liga=IDPicker.pick(as.ligen,ligaID);
		int spieltaganzahl=0;
		if(liga.getTeams().size()%2==0) {
			spieltaganzahl=(liga.getTeams().size()-1)*2;
		}else {
			spieltaganzahl=liga.getTeams().size()*2;
		}
		if(liga.getSpieltage().size()!=1) {
			throw new IllegalArgumentException("Die Liga muss genau einen Spieltag enthalten!");
		}
		for(int i=0;i<spieltaganzahl/2-1;i++) {
			Spieltag spieltag=new Spieltag(idc.createID(),i+2+". Spieltag", as);
			liga.addSpieltag(spieltag);
			ArrayList<Spiel>spiele=liga.fortfuehrenNaechsterSpieltag(idc);
                        spiele.stream().map((spiel) -> {
                            as.spiele.add(spiel);
                        return spiel;
                    }).forEachOrdered((spiel) -> {
                        spieltag.addSpiel(spiel.getID());
                    });
		}
		if(rückrunde) {
			createRueckrunde(ligaID);
		}
	}

	public void createRueckrunde(long ligaID) {
		Liga liga=IDPicker.pick(as.ligen,ligaID);
		ArrayList<Spieltag>spieltage=liga.getSpieltage();
                spieltage.forEach((st) -> {
                    Spieltag spieltag=new Spieltag(idc.createID(),liga.getSpieltage().size()+1+". Spieltag", as);
                    liga.addSpieltag(spieltag);
                    st.getSpiele().stream().map((sp) -> {
                        long heimID=sp.getAuswaertsID();
                        long auswaertsID=sp.getHeimID();
                        Spiel spiel=new Spiel(IDPicker.pick(as.teams,heimID),IDPicker.pick(as.teams,auswaertsID),idc.createID(),false,as);
                        return spiel;
                    }).map((spiel) -> {
                        as.spiele.add(spiel);
                        return spiel;
                    }).forEachOrdered((spiel) -> {
                        spieltag.addSpiel(spiel.getID());
                    });
            });
	}
	
	public void createKORunde(String name) {
		KORunde kor=new KORunde(idc.createID(),as,name);
		as.koRunden.add(kor);
	}
	
	public KORunde getKORunde(long ID) {
		return IDPicker.pick(as.koRunden,ID);
	}
        
        public ArrayList<KORunde> getKORunden(){
            return as.koRunden;
        }
        
        public Liga getLiga(long ID){
            return IDPicker.pick(as.ligen,ID);
        }
}
