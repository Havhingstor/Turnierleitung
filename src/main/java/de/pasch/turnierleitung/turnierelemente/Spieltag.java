package de.pasch.turnierleitung.turnierelemente;

import java.util.ArrayList;

import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.spiele.Spiel;
import de.pasch.turnierleitung.steuerung.ArraySpeicher;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Pickable;

public class Spieltag implements Pickable {
	private ArrayList<Long>teamIDs=new ArrayList<Long>();
	private ArrayList<Long>spielIDs=new ArrayList<Long>();
	private long ID=0;
	private String name="";
	private ArraySpeicher as=null;
	
	public Spieltag(long ID,String name,ArraySpeicher as) {
		this.ID=ID;
		this.name=name;
		this.as=as;
	}
	
	public void addSpiel(long ID) {
		if(!spielIDs.contains(ID)) {
			if(teamIDs.contains(IDPicker.pick(as.spiele, ID).getHeimID())&&teamIDs.contains(IDPicker.pick(as.spiele, ID).getAuswaertsID())) {
				spielIDs.add(ID);
			}else {
				throw new IllegalArgumentException("Eines dieser Teams ist nicht eingetragen!");
			}
		}else {
			throw new IllegalArgumentException("Dieses Spiel ist schon vorhanden!");
		}
	}
	
	public void removeSpiel(long ID) {
		if(spielIDs.contains(ID)) {
			spielIDs.remove(ID);
		}else {
			throw new IllegalArgumentException("Dieses Spiel ist nicht vorhanden!");
		}
	}
	
	public ArrayList<Spiel>getSpiele(){
		ArrayList<Spiel>s=new ArrayList<Spiel>();
		for(long l:spielIDs) {
			s.add(IDPicker.pick(as.spiele, l));
		}
		return s;
	}
	
	public void addTeam(Team team) {
		this.teamIDs.add(team.getID());
	}
	
	public void addTeam(long ID) {
		this.teamIDs.add(ID);
	}
	
	public void removeTeam(long ID) {
		boolean erlaubt=true;
		for(long l:spielIDs) {
			if(IDPicker.pick(as.spiele,l).getHeimID()==ID||IDPicker.pick(as.spiele,l).getAuswaertsID()==ID) {
				erlaubt=false;
			}
		}
		if(erlaubt) {
			teamIDs.remove(ID);
		}else {
			throw new IllegalArgumentException("Dieses Team hat noch Spiele!");
		}
	}
	
	public long getID(){
		return ID;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public String getName() {
		return name;
	}
	
	public Team getEinzelnesTeam(){
		boolean[]teamsBool=new boolean[teamIDs.size()];
		for(int i=0;i<teamIDs.size();i++) {
			teamsBool[i]=false;
		}
		for(long l:spielIDs) {
			for(int i=0;i<spielIDs.size();i++) {
				if(IDPicker.pick(as.spiele,l).getHeimID()==teamIDs.get(i)||
						IDPicker.pick(as.spiele,l).getHeimID()==teamIDs.get(i)) {
					teamsBool[i]=true;
				}
			}
			for(int i=0;i<teamsBool.length;i++) {
				if(!teamsBool[i]) {
					return IDPicker.pick(as.teams,teamIDs.get(i));
				}
			}
		}
		return null;
	}
}
