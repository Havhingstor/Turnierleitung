package de.pasch.turnierleitung.turnierelemente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.spiele.Spiel;
import de.pasch.turnierleitung.steuerung.ArraySpeicher;
import de.pasch.turnierleitung.steuerung.IDCreator;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Pickable;

public class Liga implements Pickable {
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
	
        @Override
	public long getID() {
		return ID;
	}

	public String getName() {
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
		if(spt.contains(ID)&&IDPicker.pick(as.spt, ID).getSpiele().size()>0) {
			spt.remove(ID);
		}else {
			throw new IllegalArgumentException("Dieser Spieltag enth�lt bereits Spiele");
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
				if(s.getHeimID()==ID||s.getAuswaertsID()==ID) {
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
				if(s.getHeimID()==ID) {
					tore+=s.getMengeHeimtore();
				}else if(s.getAuswaertsID()==ID) {
					tore+=s.getMengeAuswaertstore();
				}
			}
		}
		return tore;
	}
	
	public int getGegentoreEinesTeams(long ID) {
		int tore=0;
		for(Spieltag st:getSpieltage()) {
			for(Spiel s:st.getSpiele()) {
				if(s.getHeimID()==ID) {
					tore+=s.getMengeAuswaertstore();
				}else if(s.getAuswaertsID()==ID) {
					tore+=s.getMengeHeimtore();
				}
			}
		}
		return tore;
	}
	
	public int getSpielemengeEinesTeams(long ID) {
		int spiele=0;
		for(Spieltag st:getSpieltage()) {
			for(Spiel s:st.getSpiele()) {
				if(s.getHeimID()==ID||s.getAuswaertsID()==ID) {
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
				if(s.getHeimID()==ID||s.getAuswaertsID()==ID) {
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
				if(s.getHeimID()==ID||s.getAuswaertsID()==ID) {
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
				if(s.getHeimID()==ID||s.getAuswaertsID()==ID) {
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
			spiele.add(new Spiel(IDPicker.pick(as.teams,heimID),IDPicker.pick(as.teams, auswaertsID),idc.createID(),false,as));
		}
		return spiele;
	}
	
	public ArrayList<Spiel>fortfuehrenNaechsterSpieltag(IDCreator idc){
		Random zufall=new Random();
		Spieltag alterS =IDPicker.pick(as.spt,spt.get(spt.size()-1));
		ArrayList<Spiel>neuerS=new ArrayList<>();
		for(int i=0;i<teams.size();i++) {
			long heimteam=alterS.getSpiele().get(i).getHeimID();
			long auswaertsteam=0;
			if(i<teams.size()-1) {
				auswaertsteam=alterS.getSpiele().get(i+1).getAuswaertsID();
			}else {
				if(teams.size()%2==1) {
					auswaertsteam=alterS.getEinzelnesTeam().getID();
				}else {
					auswaertsteam=alterS.getSpiele().get(1).getAuswaertsID();
				}
			}
			if(zufall.nextBoolean()) {
				long zw=heimteam;
				heimteam=auswaertsteam;
				auswaertsteam=zw;
			}
			neuerS.add(new Spiel(IDPicker.pick(as.teams,heimteam),IDPicker.pick(as.teams,auswaertsteam),idc.createID(),false,as));
		}
		return neuerS;
	}
}
