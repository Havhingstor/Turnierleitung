package de.pasch.turnierleitung.steuerung;

import java.util.ArrayList;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.protagonisten.SpielerTeamConnector;
import de.pasch.turnierleitung.protagonisten.Team;

public class IDPicker {
	
	static public <T extends Pickable> T pick(ArrayList<T> liste,long ID) {
		for(int i=0;i<liste.size();i++) {
			if(liste.get(i).getID()==ID) {
				return liste.get(i);
			}
		}
		return null;
	}
		
	static public ArrayList<Spieler> pickSpielerEinesTeams(ArrayList<Spieler>insgSpieler,
			ArrayList<SpielerTeamConnector>connectoren,Team team){
		ArrayList<Spieler>spieler=new ArrayList<Spieler>();
		for(int i=0;i<connectoren.size();i++) {
			if(connectoren.get(i).getTeamID()==team.getID()) {
				spieler.add(connectoren.get(i).getSpieler());
			}
		}
		return spieler;
	}
	
	static public ArrayList<Spieler> pickAktiveSpielerEinesTeams(ArrayList<Spieler>insgSpieler,
			ArrayList<SpielerTeamConnector>connectoren,Team team){
		ArrayList<Spieler>spieler=new ArrayList<Spieler>();
		for(SpielerTeamConnector stc:connectoren) {
			if(stc.getTeamID()==team.getID()&&!stc.getAusgetreten()) {
				spieler.add(stc.getSpieler());
			}
		}
		return spieler;
	}
	
	static public ArrayList<Team> pickTeamsEinesSpielers(ArrayList<Team>insgTeams,
			ArrayList<SpielerTeamConnector>connectoren,Spieler spieler){
		ArrayList<Team>teams=new ArrayList<Team>();
		for(int i=0;i<connectoren.size();i++) {
			if(connectoren.get(i).getSpielerID()==spieler.getID()) {
				teams.add(connectoren.get(i).getTeam());
			}
		}
		return teams;
	}
}
