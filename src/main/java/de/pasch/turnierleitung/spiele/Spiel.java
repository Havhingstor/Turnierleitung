package de.pasch.turnierleitung.spiele;

import java.util.ArrayList;

import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.steuerung.ArraySpeicher;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Pickable;

public class Spiel implements Pickable {
	private long ID = 0;
	private long heimID = 0;
	private long auswaertsID = 0;
	private boolean enthaeltLink=false;
	private boolean neutralerPlatz = false;
	private String stadion = "";
	private ArrayList<Long> heimtore = new ArrayList<>();
	private ArrayList<Long> auswaertstore = new ArrayList<>();
	private ArrayList<Long> heimstrafen = new ArrayList<>();
	private ArrayList<Long> auswaertsstrafen = new ArrayList<>();
	private long gewinnerID = 0;
	private boolean unentschieden = false;
	private Elfmeterschießen elfmeterschießen = null;
	private boolean elfWahr = false;
	private boolean nachspielzeit;
	private boolean seperaterPlatzname=false;
	private ArraySpeicher as=null;

	public Spiel(Team heimteam, Team auswaertsteam, long ID, boolean neutralerPlatz,ArraySpeicher as) {
		this.ID = ID;
		this.heimID = heimteam.getID();
		this.auswaertsID = auswaertsteam.getID();
		this.neutralerPlatz = neutralerPlatz;
		this.stadion = heimteam.getHeimstadion();
		this.as=as;
	}
	
	/*
	public String getDateiString() {
		String string="<Spiel>\n<ID";
		
		return string;
	}
	*/
	
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
		this.seperaterPlatzname=wahr;
	}

	public boolean getSeperaterPlatzname() {
		return seperaterPlatzname;
	}

	public void setNeutralerPlatz(boolean neutral) {
		neutralerPlatz = neutral;
	}

	public Team getHeimteam() {
		if(!enthaeltLink) {
			return IDPicker.pick(as.teams, heimID);
		}else {
			return null;
		}
	}

	public Team getAuswaertsteam() {
		if(!enthaeltLink) {
			return IDPicker.pick(as.teams, auswaertsID);
		}else {
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

	public int getMengeHeimtore() {
		return heimtore.size();
	}

	public int getMengeAuswaertstore() {
		return auswaertstore.size();
	}

	public void addTor(Tor tor) {
		if (tor.getTeamID() == heimID) {
			heimtore.add(tor.getID());
		} else if (tor.getTeamID() == auswaertsID) {
			auswaertstore.add(tor.getID());
		} else {
			throw new IllegalArgumentException("Tor kann keinem Team zugeordnet werden!");
		}
	}

	public void removeTor(long ID) {
		if (heimtore.contains(ID)) {
			heimtore.remove(ID);
		} else if (auswaertstore.contains(ID)) {
			auswaertstore.remove(ID);
		} else {
			throw new IllegalArgumentException(
					"Dieses Tor ist weder als Heim- noch als " + "Ausw�rtstor registriert!");
		}

	}

	public ArrayList<Tor> getHeimtore() {
		ArrayList<Tor> tore = new ArrayList<>();
                heimtore.forEach((l) -> {
                    tore.add(IDPicker.pick(as.tore, l));
            });
		return tore;
	}

	public ArrayList<Tor> getAuswaertstore() {
		ArrayList<Tor> tore = new ArrayList<>();
                auswaertstore.forEach((l) -> {
                    tore.add(IDPicker.pick(as.tore, l));
            });
		return tore;
	}
	
	public ArrayList<Long>getHeimtoreIDs(){
		return heimtore;
	}
	
	public ArrayList<Long>getAuswaertstoreIDs(){
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
		} else if (strafe.getTeamID() == auswaertsID) {
			auswaertsstrafen.add(strafe.getID());
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
					"Diese Strafe ist weder als Heim- noch als " + "Ausw�rtsstrafe registriert!");
		}
	}

	public ArrayList<Strafe> getHeimstrafen() {
		ArrayList<Strafe> tore = new ArrayList<>();
                heimstrafen.forEach((l) -> {
                    tore.add(IDPicker.pick(as.strafen, l));
            });
		return tore;
	}

	public ArrayList<Strafe> getAuswaertsstarfen() {
		ArrayList<Strafe> tore = new ArrayList<>();
                auswaertsstrafen.forEach((l) -> {
                    tore.add(IDPicker.pick(as.strafen, l));
            });
		return tore;
	}
	

	public ArrayList<Long>getHeimstrafenIDs(){
		return heimstrafen;
	}
	
	public ArrayList<Long>getAuswaertsstrafenIDs(){
		return auswaertsstrafen;
	}

	public boolean abschließenUndGewinner() {
		if (heimtore.size() > auswaertstore.size()) {
			gewinnerID = heimID;
			unentschieden = false;
		} else if (heimtore.size() < auswaertstore.size()) {
			gewinnerID = auswaertsID;
			unentschieden = false;
		} else {
			gewinnerID = 0;
			unentschieden = true;
		}
		return !unentschieden;
	}

	public Team getGewinner() {
		if(!enthaeltLink) {
			if (unentschieden) {
				throw new IllegalArgumentException("Das Spiel ist unentschieden augegeangen!");
			} else {
				return IDPicker.pick(as.teams, gewinnerID);
			}
		}else {
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
		if(!enthaeltLink) {
			if (unentschieden) {
				throw new IllegalArgumentException("Das Spiel ist unentschieden augegangen!");
			} else {
				return IDPicker.pick(as.teams,getVerliererID());
			}
		}else {
			return null;
		}
	}

	public long getVerliererID() {
		if (unentschieden) {
			throw new IllegalArgumentException("Das Spiel ist unentschieden augegangen!");
		} else {
			if(gewinnerID==heimID) {
				return auswaertsID;
			}else if(gewinnerID==auswaertsID){
				return auswaertsID;
			}else {
				return 0;
			}
		}

	}

	public boolean elfmeterschießenNoetig() {
		return unentschieden;
	}

	public void addElfmeterschießen() {
		if (!elfWahr) {
			elfmeterschießen = new Elfmeterschießen();
			elfWahr = true;
		} else {
			throw new IllegalArgumentException("Es ist bereits ein Elfmeterschießen vorhanden!");
		}
	}

	public void removeElfmeterschießen() {
		elfmeterschießen = null;
	}

	public boolean istElfmeterschießen() {
		return elfWahr;
	}

	public void addBenoetigtesElfmeterschießen() {
		if (elfmeterschießenNoetig()) {
			addElfmeterschießen();
		}
	}

	public void addElfmeterSchuss(boolean getroffen, boolean heim, long spielerID) {
		if (heim) {
			elfmeterschießen.heimGetroffen.add(getroffen);
			elfmeterschießen.heimSpieler.add(spielerID);
		} else {
			elfmeterschießen.auswaertsGetroffen.add(getroffen);
			elfmeterschießen.auswaertsSpieler.add(spielerID);
		}
	}

	public void removeElfmeterschuss(int index, boolean heim) {
		if (heim) {
			elfmeterschießen.heimGetroffen.remove(index);
			elfmeterschießen.heimSpieler.remove(index);
		} else {
			elfmeterschießen.auswaertsGetroffen.remove(index);
			elfmeterschießen.auswaertsSpieler.remove(index);
		}
	}

	public ArrayList<Boolean> getElfmeterschießen(boolean heim) {
		if (heim) {
			return elfmeterschießen.heimGetroffen;
		} else {
			return elfmeterschießen.auswaertsGetroffen;
		}
	}

	public ArrayList<Long>getElfmeterspieler(boolean heim){
		if(heim) {
			return elfmeterschießen.heimSpieler;
		}else {
			return elfmeterschießen.auswaertsSpieler;
		}
	}

	public void setNachspielzeit(boolean wahr) {
		this.nachspielzeit=wahr;
	}

	public boolean getNachspielzeit() {
		return nachspielzeit;
	}
	
        @Override
        public String toString(){
            if(IDPicker.pick(as.teams,heimID)!=null&&
                    IDPicker.pick(as.teams,auswaertsID)!=null){
                return IDPicker.pick(as.teams,heimID).getKurzname()+" : "+
                        IDPicker.pick(as.teams,auswaertsID).getKurzname();
            } else {
                if(IDPicker.pick(as.teams,auswaertsID)!=null){
                    return "? : "+IDPicker.pick(as.teams,auswaertsID).getKurzname();
                }else if(IDPicker.pick(as.teams,heimID)!=null){
                    return "? : "+IDPicker.pick(as.teams,heimID).getKurzname();                    
                }else{
                    return "? : ?";
                }
            }
        }
	
	private class Elfmeterschießen {
		private ArrayList<Boolean>heimGetroffen=new ArrayList<>();
		private ArrayList<Boolean>auswaertsGetroffen=new ArrayList<>();
		private ArrayList<Long>heimSpieler=new ArrayList<>();
		private ArrayList<Long>auswaertsSpieler=new ArrayList<>();

	}

}
