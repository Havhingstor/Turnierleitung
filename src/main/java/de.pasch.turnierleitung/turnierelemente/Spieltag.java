package de.pasch.turnierleitung.turnierelemente;

import java.util.ArrayList;

import org.jdom2.Element;

import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.spiele.Spiel;
import de.pasch.turnierleitung.steuerung.ArraySpeicher;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Pickable;
import de.pasch.turnierleitung.steuerung.Steuerung;

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
	
	public Spieltag(Element parEl, ArraySpeicher as) {
		for(Element el:parEl.getChildren()) {
			if(el.getName().equals("Teams")) {
				for(Element childEl:el.getChildren()) {
					teamIDs.add(Long.parseLong(childEl.getValue()));
				}
			}else if(el.getName().equals("Spiele")) {
				for(Element childEl:el.getChildren()) {
					spielIDs.add(Long.parseLong(childEl.getValue()));
				}
			}else if(el.getName().equals("ID")) {
				ID=Long.parseLong(el.getValue());
			}else if(el.getName().equals("Name")) {
				name=el.getValue();
			}
		}
		this.as=as;
	}
	
	public void createXMLElements(Element parEl) {
		Element spieltagEl=new Element("Spieltag");
		parEl.addContent(spieltagEl);
		
		Element teamsEl=new Element("Teams");
		Steuerung.createALElements(teamsEl, teamIDs, "Team");
		spieltagEl.addContent(teamsEl);
		
		Element spieleEl=new Element("Spiele");
		Steuerung.createALElements(spieleEl, spielIDs, "Spiel");
		spieltagEl.addContent(spieleEl);
		
		Element IDEl=new Element("ID");
		IDEl.addContent(""+ID);
		spieltagEl.addContent(IDEl);
		
		Element nameEl=new Element("Name");
		nameEl.addContent(name);
		spieltagEl.addContent(nameEl);
	}
	
	public String getDateiString() {
		String string="<Spieltag>\n<ID>"+ID+"</ID>\n<Name>"+name+"</Name>\n<Teams>\n";
		for(long team:teamIDs) {
			string+=team+"\n";
		}
		string+="</Teams>\n<Spiele>\n";
		for(long spiel:spielIDs) {
			string+=spiel+"\n";
		}
		string+="</Spiele>\n</Spieltag>\n";
		return string;
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
	
	public ArrayList<Long>getSpieleIDs(){
		return spielIDs;
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
	
	public ArrayList<Long>getTeamIDs(){
		return teamIDs;
	}
	
	public ArrayList<Team>getTeams(){
		ArrayList<Team>teams=new ArrayList<>();
		for(long id:teamIDs) {
			teams.add(IDPicker.pick(as.teams, id));
		}
		return teams;
	}
	
	public Team getEinzelnesTeam(){
		boolean[]teamsBool=new boolean[teamIDs.size()];
		for(int i=0;i<teamIDs.size();i++) {
			teamsBool[i]=false;
		}
		ArrayList<Spiel>alleSpiele=this.getSpiele();
		ArrayList<Team>alleTeams=this.getTeams();
		for(Spiel sp:alleSpiele) {
			for(int i=0;i<alleTeams.size();++i) {
				Team t=alleTeams.get(i);
				if(sp.getHeimID()==t.getID()||
						sp.getAuswaertsID()==t.getID()) {
					teamsBool[i]=true;
				}
			}
			for(int i=0;i<teamsBool.length;i++) {
				if(!teamsBool[i]) {
					return alleTeams.get(i);
				}
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public boolean istAbgeschlossen() {
		for(long spID:spielIDs) {
			if(!IDPicker.pick(as.spiele, spID).isErgebnis()) {
				return false;
			}
		}
		return true;
	}
}
