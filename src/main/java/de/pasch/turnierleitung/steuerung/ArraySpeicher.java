package de.pasch.turnierleitung.steuerung;

import java.util.ArrayList;

import org.jdom2.Element;

import de.pasch.turnierleitung.protagonisten.Link;
import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.protagonisten.SpielerTeamConnector;
import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.spiele.Aufstellung;
import de.pasch.turnierleitung.spiele.Spiel;
import de.pasch.turnierleitung.spiele.Strafe;
import de.pasch.turnierleitung.spiele.Tor;
import de.pasch.turnierleitung.turnierelemente.KORunde;
import de.pasch.turnierleitung.turnierelemente.Liga;
import de.pasch.turnierleitung.turnierelemente.Runde;
import de.pasch.turnierleitung.turnierelemente.Rundensammlung;
import de.pasch.turnierleitung.turnierelemente.Spieltag;

public class ArraySpeicher {
	
	public ArrayList<Team> teams=new ArrayList<Team>();
	public ArrayList<Spieler> spieler=new ArrayList<Spieler>();
	public ArrayList<Tor> tore=new ArrayList<Tor>();
	public ArrayList<Spiel>spiele=new ArrayList<Spiel>();
	public ArrayList<Strafe>strafen=new ArrayList<Strafe>();
	public ArrayList<SpielerTeamConnector>connectoren=new ArrayList<SpielerTeamConnector>();
	public ArrayList<Aufstellung>aufstellungen=new ArrayList<Aufstellung>();
	public ArrayList<Runde>runden=new ArrayList<Runde>();
	public ArrayList<Rundensammlung>rs=new ArrayList<Rundensammlung>();
	public ArrayList<Spieltag>spt=new ArrayList<Spieltag>();
	public ArrayList<Liga>ligen=new ArrayList<Liga>();
	public ArrayList<Link>link=new ArrayList<Link>();
	public ArrayList<KORunde>koRunden=new ArrayList<KORunde>();
	
	public void createXMLElements(Element parElement) {
		Element teamEl=new Element("Teams");
		for(Team team:teams) {
			team.createXMLElements(teamEl);
		}
		parElement.addContent(teamEl);
		
		Element spielerEl=new Element("Spieler");
		for(Spieler eSpieler:spieler) {
			eSpieler.createXMLElements(spielerEl);
		}
		parElement.addContent(spielerEl);
		
		Element torEl=new Element("Tore");
		for(Tor tor:tore) {
			tor.createXMLElements(torEl);
		}
		parElement.addContent(torEl);
		
		Element spieleEl=new Element("Spiele");
		for(Spiel spiel:spiele) {
			spiel.createXMLElements(spieleEl);
		}
		parElement.addContent(spieleEl);
		
		Element strafenEl=new Element("Strafen");
		for(Strafe strafe:strafen) {
			strafe.createXMLElements(strafenEl);
		}
		parElement.addContent(strafenEl);
		
		Element connectorenEl=new Element("Connectoren");
		for(SpielerTeamConnector stc:connectoren) {
			stc.createXMLElements(strafenEl);
		}
		parElement.addContent(connectorenEl);
		
		Element aufstellungenEl=new Element("Aufstellungen");
		for(Aufstellung auf:aufstellungen) {
			auf.createXMLElements(strafenEl);
		}
		parElement.addContent(aufstellungenEl);
	}
}