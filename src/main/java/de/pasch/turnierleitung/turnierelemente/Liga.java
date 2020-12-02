package de.pasch.turnierleitung.turnierelemente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.jdom2.Element;

import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.spiele.Aufstellung;
import de.pasch.turnierleitung.spiele.Spiel;
import de.pasch.turnierleitung.steuerung.ArraySpeicher;
import de.pasch.turnierleitung.steuerung.IDCreator;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Steuerung;

public class Liga extends Turnierelement {
	private long ID=0;
	private final ArrayList<Long>spt=new ArrayList<>();
	private final ArrayList<Long>teams=new ArrayList<>();
	private ArraySpeicher as=null;
	private String name="";
	private int punkteProSieg=3;
	private int punkteProUnentschieden=1;
	private int punkteProNiederlage=0;
	private final ArrayList<VereinInTabelle>vit=new ArrayList<>();
	/*
	 * 1=Punkte
	 * 2=TD
	 * 3=Tore
	 * 4=Gegentore
	 * 5=Spiele
	 */
	private int[]reihenfolgeKriterien= {1,2,3,4,5};
	
	public Liga(int pps,int ppu,int ppn,String name,long ID,ArraySpeicher as,int[]rk) {
		this.setName(name);
		this.ID=ID;
		this.as=as;
		this.setPunkteProSieg(pps);
		this.setPunkteProUnentschieden(ppu);
		this.setPunkteProNiederlage(ppn);
		this.reihenfolgeKriterien=rk;
	}
	
	public Liga(Element parEl,ArraySpeicher as) {
		for(Element el:parEl.getChildren()) {
			if(el.getName().equals("ID")) {
				ID=Long.parseLong(el.getValue());
			}else if(el.getName().equals("Spieltage")) {
				for(Element childEl:el.getChildren()) {
					spt.add(Long.parseLong(childEl.getValue()));
				}
			}else if(el.getName().equals("Teams")) {
				for(Element childEl:el.getChildren()) {
					teams.add(Long.parseLong(childEl.getValue()));
				}
			}else if(el.getName().equals("Name")) {
				name=el.getValue();
			}else if(el.getName().equals("PPS")) {
				punkteProSieg=Integer.parseInt(el.getValue());
			}else if(el.getName().equals("PPU")) {
				punkteProUnentschieden=Integer.parseInt(el.getValue());
			}else if(el.getName().equals("PPN")) {
				punkteProNiederlage=Integer.parseInt(el.getValue());
			}else if(el.getName().equals("RK")) {
				for(int i=0;i<5;++i) {
					reihenfolgeKriterien[i]=Integer.parseInt(el.getChildren().get(i).getValue());
				}
			}
		}
		this.as=as;
	}
	
	public void createXMLElements(Element parEl) {
		Element ligaEl=new Element("Liga");
		parEl.addContent(ligaEl);
		
		Element IDEl=new Element("ID");
		IDEl.addContent(""+ID);
		ligaEl.addContent(IDEl);
		
		Element sptEl=new Element("Spieltage");
		Steuerung.createALElements(sptEl, spt, "Spieltag");
		ligaEl.addContent(sptEl);
		
		Element teamsEl=new Element("Teams");
		Steuerung.createALElements(teamsEl, teams, "Team");
		ligaEl.addContent(teamsEl);
		
		Element nameEl=new Element("Name");
		nameEl.addContent(name);
		ligaEl.addContent(nameEl);
		
		Element ppsEl=new Element("PPS");
		ppsEl.addContent(""+punkteProSieg);
		ligaEl.addContent(ppsEl);
		
		Element ppuEl=new Element("PPU");
		ppuEl.addContent(""+punkteProUnentschieden);
		ligaEl.addContent(ppuEl);
		
		Element ppnEl=new Element("PPN");
		ppnEl.addContent(""+punkteProNiederlage);
		ligaEl.addContent(ppnEl);
		
		/*Element vitEl=new Element("VIT");//Unbenötigt
		Steuerung.createALElements(vitEl, vit, "Verein");
		ligaEl.addContent(vitEl);*/
		
		Element rkEl=new Element("RK");
		for(int i:reihenfolgeKriterien) {
			Element el=new Element("Kriterium");
			el.addContent(""+i);
			rkEl.addContent(el);
		}
		ligaEl.addContent(rkEl);
	}
	
	public String getDateiString() {
		String string="<Liga>\n<ID>"+ID+"</ID>\n<Name>"+name+"</Name>\n<PPS>"+punkteProSieg+"</PPS>\n<PPU>"+punkteProUnentschieden+"</PPU>\n<PPN>"+punkteProNiederlage+"</PPN>\n<Teams>\n";
		for(long team:teams) {
			string+=team+"\n";
		}
		string+="</Teams>\n<Spieltage>\n";
		for(long spieltag:spt) {
			string+=spieltag+"\n";
		}
		string+="</Spieltage>\n<Tabelle>\n";
		for(VereinInTabelle v:vit) {
			string+=v.getDateiString();
		}
		string+="</Tabelle>\n</Liga>\n";
		return string;
	}
	
        @Override
	public long getID() {
		return ID;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}
	
	public int[] getReihenfolgeKriterien() {
		return reihenfolgeKriterien;
	}
	
	public int getPunkteProSieg() {
		return punkteProSieg;
	}

	public final void setPunkteProSieg(int punkteProSieg) {
		this.punkteProSieg = punkteProSieg;
	}

	public int getPunkteProUnentschieden() {
		return punkteProUnentschieden;
	}

	public final void setPunkteProUnentschieden(int punkteProUnentschieden) {
		this.punkteProUnentschieden = punkteProUnentschieden;
	}

	public int getPunkteProNiederlage() {
		return punkteProNiederlage;
	}

	public final void setPunkteProNiederlage(int punkteProNiederlage) {
		this.punkteProNiederlage = punkteProNiederlage;
	}

	public void addTeam(long ID) {
		this.teams.add(ID);
                spt.forEach((l) -> {
                    IDPicker.pick(as.spt,l).addTeam(ID);
            });
	}
		
	public ArrayList<Team>getTeams(){
		ArrayList<Team>t=new ArrayList<>();
                teams.forEach((l) -> {
                    t.add(IDPicker.pick(as.teams, l));
            });
		return t;
	}
	
	public ArrayList<Long>getTeamIDs(){
		return teams;
	}
	
	public void removeTeam(long ID) {
		if(teams.contains(ID)) {
			teams.remove(ID);
		}else {
			throw new IllegalArgumentException("Team nicht vorhanden!");
		}
	}
	
	public void addSpieltag(Spieltag s) {
		boolean erlaubt=true;
		for(Spiel spiel:s.getSpiele()) {
			if(teams.contains(spiel.getHeimID())&&
					teams.contains(spiel.getAuswaertsID())){
			}else {
				erlaubt=false;
			}
		}
		if(erlaubt) {
			spt.add(s.getID());
                        teams.forEach((l) -> {
                            try {
                                s.addTeam(l);
                            }catch(IllegalArgumentException e) {}
                    });
		}else {
			throw new IllegalArgumentException("In diesem Spieltage kommt ein nicht erlaubtes Team vor!");
		}
	}
	
	public void removeSpieltag(long ID) {
		if(spt.contains(ID)) {
			spt.remove(ID);
		}
	}
	
	public ArrayList<Spieltag>getSpieltage(){
		ArrayList<Spieltag>spieltage=new ArrayList<>();
        spt.forEach((l) -> {
        	spieltage.add(IDPicker.pick(as.spt, l));
        });
		return spieltage;
	}
	
	public ArrayList<Long>getSpieltageID(){
		return spt;
	}
	
	public void setReihenfolgeKriterien(int[]rk) {
		this.reihenfolgeKriterien=rk;
                vit.forEach((vereinIT) -> {
                    vereinIT.setReihenfolgeKriterien(rk);
            });
	}
	
	public int getPunkteEinesTeams(long ID) {
		int punkte=0;
		for(Spieltag st:getSpieltage()) {
			for(Spiel s:st.getSpiele()) {
				if((s.getHeimID()==ID||s.getAuswaertsID()==ID)&&s.isErgebnis()) {
					if(s.abschließenUndGewinner()) {
						if(s.getGewinnerID()==ID) {
							punkte+=punkteProSieg;
						}else {
							punkte+=punkteProNiederlage;
						}
					}else {
						punkte+=punkteProUnentschieden;
					}
				}
			}
		}
		return punkte;
	}
	
	
	public int getToreEinesTeams(long ID) {
		int tore=0;
		for(Spieltag st:getSpieltage()) {
			for(Spiel s:st.getSpiele()) {
				if(s.isErgebnis()) {
					if(s.getHeimID()==ID) {
						tore+=s.getHeimtoreZahl();
					}else if(s.getAuswaertsID()==ID) {
						tore+=s.getAuswaertstoreZahl();
					}
				}
			}
		}
		return tore;
	}
	
	public int getGegentoreEinesTeams(long ID) {
		int tore=0;
		for(Spieltag st:getSpieltage()) {
			for(Spiel s:st.getSpiele()) {if(s.isErgebnis()) {
					if(s.getHeimID()==ID) {
						tore+=s.getAuswaertstoreZahl();
					}else if(s.getAuswaertsID()==ID) {
						tore+=s.getHeimtoreZahl();
					}
				}
			}
		}
		return tore;
	}
	
	public int getSpielemengeEinesTeams(long ID) {
		int spiele=0;
		for(Spieltag st:getSpieltage()) {
			for(Spiel s:st.getSpiele()) {
				if((s.getHeimID()==ID||s.getAuswaertsID()==ID)&&s.isErgebnis()) {
					spiele++;
				}
			}
		}
		return spiele;
	}
	
	public int getSiegeEinesTeams(long ID) {
		int siege=0;
		for(Spieltag st:getSpieltage()) {
			for(Spiel s:st.getSpiele()) {
				if((s.getHeimID()==ID||s.getAuswaertsID()==ID)&&s.isErgebnis()) {
					if(s.abschließenUndGewinner()) {
						if(s.getGewinnerID()==ID) {
							siege++;
						}
					}
				}
			}
		}
		return siege;
	}
	
	public int getUnentschiedenEinesTeams(long ID) {
		int unentschieden=0;
		for(Spieltag st:getSpieltage()) {
			for(Spiel s:st.getSpiele()) {
				if((s.getHeimID()==ID||s.getAuswaertsID()==ID)&&s.isErgebnis()) {
					if(!s.abschließenUndGewinner()) {
						unentschieden++;
					}
				}
			}
		}
		return unentschieden;
	}
	
	public int getNiederlagenEinesTeams(long ID) {
		int niederlagen=0;
		for(Spieltag st:getSpieltage()) {
			for(Spiel s:st.getSpiele()) {
				if((s.getHeimID()==ID||s.getAuswaertsID()==ID)&&s.isErgebnis()) {
					if(s.abschließenUndGewinner()) {
						if(s.getGewinnerID()!=ID) {
							niederlagen++;
						}
					}
				}
			}
		}
		return niederlagen;

	}
	
	public ArrayList<VereinInTabelle>berechneGetAktuelleTabelle(){
		vit.clear();
                teams.stream().map((teamID) -> {
                    VereinInTabelle vereinIT=new VereinInTabelle(teamID,as);
                    vereinIT.setReihenfolgeKriterien(reihenfolgeKriterien);
                    vereinIT.setPunkte(getPunkteEinesTeams(teamID));
                    vereinIT.setTore(getToreEinesTeams(teamID));
                    vereinIT.setGegentore(getGegentoreEinesTeams(teamID));
                    vereinIT.setSpiele(getSpielemengeEinesTeams(teamID));
                    vereinIT.setSiege(getSiegeEinesTeams(teamID));
                    vereinIT.setUnentschieden(getUnentschiedenEinesTeams(teamID));
                    vereinIT.setNiederlagen(getNiederlagenEinesTeams(teamID));
                return vereinIT;
            }).forEachOrdered((vereinIT) -> {
                vit.add(vereinIT);
            });
		Collections.sort(vit);
		return vit;	
	}
	
	public ArrayList<VereinInTabelle>getLetzteBerechneteTabelle(){
		return vit;
	}
	
	public ArrayList<Spiel> auslosen(IDCreator idc) {
		ArrayList<Spiel>spiele=new ArrayList<>();
		Random zufall=new Random();
		boolean[]benutzt=new boolean[teams.size()];
		for(int i=0;i<benutzt.length/2;i++) {
			long heimID=0;
			long auswaertsID=0;
			int j=zufall.nextInt(benutzt.length);
			while(benutzt[j]) {
				j=zufall.nextInt(benutzt.length);
			}
			heimID=teams.get(j);
			benutzt[j]=true;
			while(benutzt[j]) {
				j=zufall.nextInt(benutzt.length);
			}
			auswaertsID=teams.get(j);
			benutzt[j]=true;
			if(zufall.nextBoolean()) {
				long zw=heimID;
				heimID=auswaertsID;
				auswaertsID=zw;
			}
			long neueID=idc.createID();
			Aufstellung heim=new Aufstellung(heimID, neueID, this.getHoechstStartelf(), this.getHoechstAuswechselspieler(),
					this.getHoechstAuswechslung(),this.getHoechstAuswechslungNachspielzeit(), idc.createID(), as);
			Aufstellung auswaerts=new Aufstellung(auswaertsID,neueID,this.getHoechstStartelf(), this.getHoechstAuswechselspieler(),
					this.getHoechstAuswechslung(),this.getHoechstAuswechslungNachspielzeit(),idc.createID(), as);
			spiele.add(new Spiel(IDPicker.pick(as.teams,heimID),IDPicker.pick(as.teams, auswaertsID),neueID,false,as,heim,auswaerts));
		}
		return spiele;
	}
	
	public ArrayList<Spiel>fortfuehrenNaechsterSpieltag(IDCreator idc){
		Spieltag alterS =IDPicker.pick(as.spt,spt.get(spt.size()-1));
		int teamNr=teams.size();
		ArrayList<Spiel>neuerS=new ArrayList<>();
		long[] teamsSpieltag=new long[teamNr];
		if(teamNr%2==0) {
			for(int i=0;i<(teamNr/2);++i) {
				teamsSpieltag[i]=alterS.getSpiele().get(i).getHeimID();
			}
			for(int i=teamNr/2;i<teamNr;++i) {
				teamsSpieltag[i]=alterS.getSpiele().get(i-teamNr/2).getAuswaertsID();
			}
		}else {
			for(int i=0;i<(teamNr/2);++i) {
				teamsSpieltag[i]=alterS.getSpiele().get(i).getHeimID();
			}
			teamsSpieltag[teamNr/2]=alterS.getEinzelnesTeam().getID();
			for(int i=teamNr/2+1;i<teamNr;++i) {
				teamsSpieltag[i]=
						alterS.getSpiele().get(i-(teamNr/2+1)).getAuswaertsID();
			}
		}
		Ausloser a=new Ausloser(teamsSpieltag);
		long[][]nsp=a.getAusgelost();
		for(int i=0;i<teamNr/2;++i) {
			long neueID=idc.createID();
			Aufstellung heim=new Aufstellung(nsp[1][i], neueID, this.getHoechstStartelf(), this.getHoechstAuswechselspieler(),
					this.getHoechstAuswechslung(),this.getHoechstAuswechslungNachspielzeit(), idc.createID(), as);
			Aufstellung auswaerts=new Aufstellung(nsp[1][teamNr-1-i],neueID,this.getHoechstStartelf(), this.getHoechstAuswechselspieler(),
					this.getHoechstAuswechslung(),this.getHoechstAuswechslungNachspielzeit(),idc.createID(), as);
			Spiel spiel=new Spiel(IDPicker.pick(as.teams,nsp[1][i]),IDPicker.pick(as.teams,nsp[1][teamNr-1-i]), neueID, false, as,heim,auswaerts);
			neuerS.add(spiel);
		}
		/*
		for(int i=0;i<alterS.getSpiele().size();i++) {
			long heimteam=alterS.getSpiele().get(i).getHeimID();
			long auswaertsteam=0;
			if(i<alterS.getSpiele().size()-1) {
				auswaertsteam=alterS.getSpiele().get(i+1).getAuswaertsID();
			}else {
				if(teams.size()%2==1) {
					auswaertsteam=alterS.getEinzelnesTeam().getID();
				}else {
					auswaertsteam=alterS.getSpiele().get(1).getAuswaertsID();
				}
			}
			neuerS.add(new Spiel(IDPicker.pick(as.teams,heimteam),IDPicker.pick(as.teams,auswaertsteam),idc.createID(),false,as));
		}
		*/
		return neuerS;
	}

	@Override
	public boolean isLiga() {
		return true;
	}
}

class Ausloser{
	private int teamNr;
	private long[][]positionen;	//Erste Stelle: Vorheriges Team + Neues Team, Zweite Stelle sind Positionen
	
	Ausloser(long [] teams) {
		this.teamNr=teams.length;
		positionen=new long[2][teamNr];
		for(int i=0; i<teams.length;++i) {
			positionen[0][i]=teams[i];
		}
		auslosen();
	}
	
	private void auslosen() {
		for(int i=0;i<teamNr-1;++i) {
			positionen[1][i+1]=positionen[0][i];
		}
		positionen[1][0]=positionen[0][teamNr-1];
	}
	
	public long [] [] getAusgelost() {
		return positionen;
	}
}
