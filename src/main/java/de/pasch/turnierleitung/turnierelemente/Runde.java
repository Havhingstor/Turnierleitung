package de.pasch.turnierleitung.turnierelemente;

import java.util.ArrayList;

import de.pasch.turnierleitung.protagonisten.Link;
import de.pasch.turnierleitung.spiele.Spiel;
import de.pasch.turnierleitung.steuerung.ArraySpeicher;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Pickable;

public class Runde implements Pickable{
	private long heimteamID=0;
	private long auswaertsteamID=0;
	private long gewinnerID=0;
	private final ArrayList<Long>spieleIDs=new ArrayList<>();
	private final long ID;
	private ArraySpeicher as=null;
	
	/*
	 * erstesKriterium =Spiele/Tore
	 * zweitesKriterium=Auswärtstore/Elfmeterschie�en
	 */
	private int erstesKriteriumIndex=1;
	private int zweitesKriteriumIndex=0;
	
	public Runde(long heimteamID,long auswaertsteamID,long ID,ArraySpeicher as) {
		this.heimteamID=heimteamID;
		this.auswaertsteamID=auswaertsteamID;
		this.ID=ID;
		this.as=as;
	}
	
	public void addSpiel(Spiel spiel) {
		if(((spiel.getHeimID()==heimteamID)&&(spiel.getAuswaertsID()==auswaertsteamID))
				||((spiel.getHeimID()==heimteamID)&&(spiel.getAuswaertsID()==auswaertsteamID))) {
			spieleIDs.add(spiel.getID());
		}else {
			throw new IllegalArgumentException("Dieses Spiel passt nicht ins Schema");
		}
	}
	
	public void addSpiel(long spielID) {
		this.spieleIDs.add(spielID);
	}
	
	public void removeSpiel(long ID) {
		if(spieleIDs.contains(ID)) {
			spieleIDs.remove(ID);
		}else {
			throw new IllegalArgumentException("Dieses Spiel ist nicht vorhanden!");
		}
	}
	
	public ArrayList<Long>getSpiele(){
		return spieleIDs;
	}
	
	public long getSetGewinner() {
		int werteHeimSpieler=0;
		int werteAuswaertsSpieler=0;
		int zusatzwertungHeim=0;
		int zusatzwertungAuswaerts=0;
		boolean erlaubt=true;
		for(long l:spieleIDs) {
			if(IDPicker.pick(as.spiele,l).isEnthaeltLink()) {
				erlaubt=false;
			}
		}
		if(erlaubt) {
			if(erstesKriteriumIndex==0) {
				for(long s:spieleIDs) {
					for(Spiel spiel:as.spiele) {
						if(spiel.getID()==s) {
							if(spiel.abschließenUndGewinner()) {
								if(spiel.getGewinnerID()==heimteamID) {
									werteHeimSpieler++;
								}else {
									werteAuswaertsSpieler++;
								}
							}
						}
					}
				}
			}else {
				for(long s:spieleIDs) {
					for(Spiel spiel:as.spiele) {
						if(spiel.getID()==s) {
							if(spiel.abschließenUndGewinner()) {
								if(spiel.getHeimID()==heimteamID) {
									werteHeimSpieler+=spiel.getMengeHeimtore();
									werteAuswaertsSpieler+=spiel.getMengeAuswaertstore();
								}else {
									werteAuswaertsSpieler+=spiel.getMengeHeimtore();
									werteHeimSpieler+=spiel.getMengeAuswaertstore();
								}
							}
						}
					}
				}
			}
			if(werteHeimSpieler>werteAuswaertsSpieler) {
				gewinnerID=heimteamID;
				return heimteamID;
			}else if(werteHeimSpieler<werteAuswaertsSpieler) {
				gewinnerID=auswaertsteamID;
				return auswaertsteamID;
			}else {
				if(zweitesKriteriumIndex==0) {
					for(long s:spieleIDs) {
						for(Spiel spiel:as.spiele) {
							if(spiel.getID()==s) {
								if(spiel.abschließenUndGewinner()) {
									if(spiel.getHeimID()==heimteamID) {
										zusatzwertungAuswaerts+=spiel.getMengeAuswaertstore();
									}else {
										zusatzwertungHeim+=spiel.getMengeAuswaertstore();
									}
								}
							}
						}
					}
				}
				if(zusatzwertungAuswaerts>zusatzwertungHeim) {
					gewinnerID=auswaertsteamID;
					return auswaertsteamID;
				}else if(zusatzwertungAuswaerts<zusatzwertungHeim) {
					gewinnerID=heimteamID;
					return heimteamID;
				}else {
					gewinnerID=0;
					return 0;
				}
			}
		}else {
			return 0;
		}
	}
	
	public void setErstesKriterium(int index) {
		erstesKriteriumIndex=index;
	}
	
	public void setZweitesKriterium(int index) {
		zweitesKriteriumIndex=index;
	}
	
        @Override
	public long getID() {
		return ID;
	}
	
	public long getGewinner() {
		return gewinnerID;
	}
	
	public long getHeimteamID() {
		return heimteamID;
	}
	
	public long getAuswaertsteamID(){
		return auswaertsteamID;
	}
	
	public void loeseLinksAuf() {
		Link link=null;
		boolean heimteam =true;
		if(IDPicker.pick(as.teams,heimteamID)==null) {
			link=IDPicker.pick(as.link,heimteamID);
			heimteam=true;
		}else if(IDPicker.pick(as.teams,auswaertsteamID)==null) {
			link=IDPicker.pick(as.link,auswaertsteamID);
			heimteam=false;
		}
		if(link!=null) {
			Runde runde=IDPicker.pick(as.runden,link.getRundeID());
			long teamID=0;
			if(link.getGewinner()) {
				teamID=runde.getSetGewinner();
			}else {
				teamID=runde.getSetVerlierer();
			}
			if(heimteam) {
				heimteamID=teamID;
			}else {
				auswaertsteamID=teamID;
			}
			for(long l:spieleIDs) {
				Spiel s=IDPicker.pick(as.spiele,l);
				if(s.getHeimID()==link.getID()) {
					s.setHeimteam(IDPicker.pick(as.teams,teamID));
				}else {
						s.setAuswaertsteam(IDPicker.pick(as.teams,teamID));
				}
			}
		}
	}
	
	public long getVerlierer() {
		if(heimteamID==gewinnerID) {
			return auswaertsteamID;
		}else if(auswaertsteamID==gewinnerID) {
			return heimteamID;
		}else {
			return 0;
		}
	}
	
	public long getSetVerlierer() {
		this.getSetGewinner();
		return this.getVerlierer();
	}
	
	public String toString() {
		return IDPicker.pick(as.teams,heimteamID).getKurzname()+" : "
	+IDPicker.pick(as.teams,auswaertsteamID).getKurzname();
	}
}

