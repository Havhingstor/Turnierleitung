package de.pasch.turnierleitung.steuerung;

import java.util.ArrayList;

import org.jdom2.Element;

import de.pasch.turnierleitung.protagonisten.Link;
import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.protagonisten.SpielerTeamConnector;
import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.spiele.Spiel;
import de.pasch.turnierleitung.spiele.Strafe;
import de.pasch.turnierleitung.spiele.Tor;
import de.pasch.turnierleitung.spiele.Wechsel;
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
	//public ArrayList<Aufstellung>aufstellungen=new ArrayList<Aufstellung>();
	public ArrayList<Runde>runden=new ArrayList<Runde>();
	public ArrayList<Rundensammlung>rs=new ArrayList<Rundensammlung>();
	public ArrayList<Spieltag>spt=new ArrayList<Spieltag>();
	public ArrayList<Liga>ligen=new ArrayList<Liga>();
	public ArrayList<Link>links=new ArrayList<Link>();
	public ArrayList<KORunde>koRunden=new ArrayList<KORunde>();
	public ArrayList<Wechsel>wechsel=new ArrayList<Wechsel>();
	
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
		
		Element connectorenEl=new Element("Connectoren");
		for(SpielerTeamConnector stc:connectoren) {
			stc.createXMLElements(connectorenEl);
		}
		parElement.addContent(connectorenEl);
		
		Element ligenEl=new Element("Ligen");
		for(Liga liga:ligen) {
			liga.createXMLElements(ligenEl);
		}
		parElement.addContent(ligenEl);
		
		Element spieltageEl=new Element("Spieltage");
		for(Spieltag sp:spt) {
			sp.createXMLElements(spieltageEl);
		}
		parElement.addContent(spieltageEl);
		
		Element spieleEl=new Element("Spiele");
		for(Spiel spiel:spiele) {
			spiel.createXMLElements(spieleEl);
		}
		parElement.addContent(spieleEl);
		
		Element torEl=new Element("Tore");
		for(Tor tor:tore) {
			tor.createXMLElements(torEl);
		}
		parElement.addContent(torEl);
		
		Element wechselEl=new Element("Wechsel");
		for(Wechsel w:wechsel) {
			w.createXMLElements(wechselEl);
		}
		parElement.addContent(wechselEl);
		
		Element strafenEl=new Element("Strafen");
		for(Strafe strafe:strafen) {
			strafe.createXMLElements(strafenEl);
		}
		parElement.addContent(strafenEl);
		
		/*Element aufstellungenEl=new Element("Aufstellungen");
		for(Aufstellung auf:aufstellungen) {
			auf.createXMLElements(aufstellungenEl);
		}
		parElement.addContent(aufstellungenEl);*/
		
		Element rsEl=new Element("Rundensammlungen");
		for(Rundensammlung rus:rs) {
			rus.createXMLElements(rsEl);
		}
		parElement.addContent(rsEl);
		
		Element linksEl=new Element("Links");
		for(Link link:links) {
			link.createXMLElements(linksEl);
		}
		parElement.addContent(linksEl);
		
		Element korEl=new Element("KORunden");
		for(KORunde kor:koRunden) {
			kor.createXMLElements(korEl);
		}
		parElement.addContent(korEl);
	}
	
	public void regeneriereAusXMLElements(Element rootElement) {
		löscheUndRegeneriereAlle();
		for(Element el:rootElement.getChildren()) {
			if(el.getName().equals("Teams")) {
				for(Element childEl:el.getChildren()) {
					teams.add(new Team(childEl,this));
				}
			}else if(el.getName().equals("Spieler")) {
				for(Element childEl:el.getChildren()) {
					spieler.add(new Spieler(childEl));
				}
			}else if(el.getName().equals("Connectoren")) {
				for(Element childEl:el.getChildren()) {
					connectoren.add(new SpielerTeamConnector(childEl,this));
				}
			}else if(el.getName().equals("Ligen")) {
				for(Element childEl:el.getChildren()) {
					ligen.add(new Liga(childEl,this));
				}
			}else if(el.getName().equals("Spieltage")) {
				for(Element childEl:el.getChildren()) {
					spt.add(new Spieltag(childEl,this));
				}
			}else if(el.getName().equals("Spiele")){
				for(Element childEl:el.getChildren()) {
					spiele.add(new Spiel(childEl,this));
				}
			}else if(el.getName().equals("Tore")) {
				for(Element childEl:el.getChildren()) {
					tore.add(new Tor(childEl,this));
				}
			}else if(el.getName().equals("Wechsel")) {
				for(Element childEl:el.getChildren()) {
					wechsel.add(new Wechsel(childEl,this));
				}
			}else if(el.getName().equals("Strafen")) {
				for(Element childEl:el.getChildren()) {
					strafen.add(new Strafe(childEl,this));
				}
			}
		}
	}
	
	private void löscheUndRegeneriereAlle() {
		teams=new ArrayList<Team>();
		spieler=new ArrayList<Spieler>();
		tore=new ArrayList<Tor>();
		spiele=new ArrayList<Spiel>();
		strafen=new ArrayList<Strafe>();
		connectoren=new ArrayList<SpielerTeamConnector>();
		runden=new ArrayList<Runde>();
		rs=new ArrayList<Rundensammlung>();
		spt=new ArrayList<Spieltag>();
		ligen=new ArrayList<Liga>();
		links=new ArrayList<Link>();
		koRunden=new ArrayList<KORunde>();
		wechsel=new ArrayList<Wechsel>();
	}
}