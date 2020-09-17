package de.pasch.turnierleitung.turnierelemente;

import java.util.ArrayList;

import org.jdom2.Element;

import de.pasch.turnierleitung.protagonisten.Link;
import de.pasch.turnierleitung.spiele.Spiel;
import de.pasch.turnierleitung.steuerung.ArraySpeicher;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Pickable;
import de.pasch.turnierleitung.steuerung.Steuerung;

public class Runde implements Pickable{
	private long heimteamID=0;
	private long auswaertsteamID=0;
	private long gewinnerID=0;
	private final ArrayList<Long>spieleIDs=new ArrayList<>();
	private final long ID;
	private ArraySpeicher as=null;
	private int kriteriumEins=0;
	private int kriteriumZwei=0;
	private int spielanzahl=1;
	private String stadionHeim;
	private String stadionAuswaerts;
	
	public static final int kriteriumEinsTore=0;
	public static final int kriteriumEinsSpiele=1;
	public static final int kriteriumZweiATore=0;
	public static final int kriteriumZweiElfmeter=1;
		
	public Runde(long heimteamID,long auswaertsteamID,long ID,ArraySpeicher as,int kriteriumEins,
			int kriteriumZwei, int spielanzahl,String stadionHeim, String stadionAuswaerts) {
		this.heimteamID=heimteamID;
		this.auswaertsteamID=auswaertsteamID;
		this.ID=ID;
		this.as=as;
		this.kriteriumEins=kriteriumEins;
		this.kriteriumZwei=kriteriumZwei;
		this.spielanzahl=spielanzahl;
		this.stadionHeim=stadionHeim;
		this.stadionAuswaerts=stadionAuswaerts;
	}
	
	public void createXMLElements(Element parEl) {
		Element runde=new Element("Runde");
		parEl.addContent(runde);
		
		Element heimEl=new Element("Heimteam");
		heimEl.addContent(""+heimteamID);
		parEl.addContent(heimEl);
		
		Element auswEl=new Element("Auswaertsteam");
		heimEl.addContent(""+auswaertsteamID);
		parEl.addContent(auswEl);
		
		Element gewinnerEl=new Element("Gewinner");
		gewinnerEl.addContent(""+gewinnerID);
		parEl.addContent(gewinnerEl);
		
		Element spieleEl=new Element("Spiele");
		Steuerung.createALElements(spieleEl,spieleIDs,"Spiel");
		parEl.addContent(spieleEl);
		
		Element IDEl=new Element("ID");
		IDEl.addContent(""+ID);
		parEl.addContent(IDEl);
		
		Element kriteriumEinsEl=new Element("KriteriumEins");
		kriteriumEinsEl.addContent(""+kriteriumEins);
		parEl.addContent(kriteriumEinsEl);
		
		Element kriteriumZweiEl=new Element("KriteriumZwei");
		kriteriumZweiEl.addContent(""+kriteriumZwei);
		parEl.addContent(kriteriumZweiEl);
		
		Element spielanzahlEl=new Element("Spielanzahl");
		spielanzahlEl.addContent(""+spielanzahl);
		parEl.addContent(spielanzahlEl);
		
		Element stadionHeimEl=new Element("Heimstadion");
		stadionHeimEl.addContent(""+stadionHeim);
		parEl.addContent(stadionHeimEl);
		
		Element stadionAuswEl=new Element("Auswaertsstadion");
		stadionAuswEl.addContent(""+stadionAuswaerts);
		parEl.addContent(stadionAuswEl);
	}
	
	/**
	 * @return the stadionHeim
	 */
	public String getStadionHeim() {
		return stadionHeim;
	}

	/**
	 * @return the stadionAuswaerts
	 */
	public String getStadionAuswaerts() {
		return stadionAuswaerts;
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
		String string="<Runde>\n<Heimteam>"+heimteamID+"</Heimteam>\n<Auswaertsteam>"+auswaertsteamID+"</Auswaertsteam>\n<Gewinner>"+gewinnerID+"</Gewinner>\n<ID>"+ID+"</ID>\n<Spiele>";
		for(long spiel:spieleIDs) {
			string+=spiel+"\n";
		}
		string+="</Spiele>\n</Runde>\n";
		
		return string;
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
			if(kriteriumEins==1) {
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
				if(kriteriumZwei==0) {
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
			link=IDPicker.pick(as.links,heimteamID);
			heimteam=true;
		}else if(IDPicker.pick(as.teams,auswaertsteamID)==null) {
			link=IDPicker.pick(as.links,auswaertsteamID);
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

