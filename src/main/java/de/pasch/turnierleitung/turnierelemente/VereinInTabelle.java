package de.pasch.turnierleitung.turnierelemente;

import de.pasch.turnierleitung.steuerung.ArraySpeicher;
import de.pasch.turnierleitung.steuerung.IDPicker;

public class VereinInTabelle implements Comparable<VereinInTabelle> {
	private long teamID=0;
	private int spiele=0;
	private int tore=0;
	private int gegentore=0;
	private int punkte=0;
	private int siege=0;
	private int unentschieden=0;
	private int niederlagen=0;
	public int getSiege() {
		return siege;
	}

	public void setSiege(int siege) {
		this.siege = siege;
	}

	public int getUnentschieden() {
		return unentschieden;
	}

	public void setUnentschieden(int unentschieden) {
		this.unentschieden = unentschieden;
	}

	public int getNiederlagen() {
		return niederlagen;
	}

	public void setNiederlagen(int niederlagen) {
		this.niederlagen = niederlagen;
	}

	/*
	 * 1=Punkte
	 * 2=TD
	 * 3=Tore
	 * 4=Gegentore
	 * 5=Spiele
	 */
	private int[]reihenfolgeKriterien= {1,2,3,4,5};
	private ArraySpeicher as=null;
	
	public VereinInTabelle(long teamID,ArraySpeicher as) {
		this.teamID=teamID;
		this.as=as;
	}

	public long getTeamID() {
		return teamID;
	}

	public int getSpiele() {
		return spiele;
	}

	public void setSpiele(int spiele) {
		this.spiele = spiele;
	}

	public int getTore() {
		return tore;
	}

	public void setTore(int tore) {
		this.tore = tore;
	}

	public int getGegentore() {
		return gegentore;
	}

	public void setGegentore(int gegentore) {
		this.gegentore = gegentore;
	}

	public int getPunkte() {
		return punkte;
	}

	public void setPunkte(int punkte) {
		this.punkte = punkte;
	}
	
	public int getTD() {
		return tore-gegentore;
	}
	
	@Override
	public int compareTo(VereinInTabelle o) { 
		/*
		 * Achtung, da aufsteigend sortiert wird bekommt das
		 * bessere Team den "schlechteren" Wert.
		 */
		for(int i:reihenfolgeKriterien) {
                    switch (i) {
                        case 1:
                            if(this.punkte!=o.punkte) {
                                return o.punkte-this.punkte;
                            }
                            break;
                        case 2:
                            if(this.getTD()!=o.getTD()) {
                                return o.getTD()-this.getTD();
                            }
                            break;
                        case 3:
                            if(this.tore!=o.tore) {
                                return o.tore-this.tore;
                            }
                            break;
                        case 4:
                            if(this.gegentore!=o.gegentore) {
                                return this.gegentore-o.gegentore;
                            }
                            break;
                        case 5:
                            if(this.spiele!=o.spiele) {
                                return this.spiele-o.spiele;
                            }
                            break;
                        default:
                            break;
                    }
		}
		return 0;
	}
	
	public int[] getReihenfolgeKriterien() {
		return reihenfolgeKriterien;
	}

	public void setReihenfolgeKriterien(int[] reihenfolgeKriterien) {
		this.reihenfolgeKriterien = reihenfolgeKriterien;
	}
	
	@Override
	public String toString() {
		return IDPicker.pick(as.teams,this.getTeamID()).getKurzname()+" "+this.getPunkte()+" "+this.getSpiele()+" "+this.getSiege()+" "+this.getUnentschieden()+" "+this.getNiederlagen()+" "+this.getTore()+" "+this.getGegentore()+" "+this.getTD();
	}
}
