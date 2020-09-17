package de.pasch.turnierleitung.protagonisten;

import org.jdom2.Element;

import de.pasch.turnierleitung.steuerung.Pickable;

public class Link implements Linkface,Pickable{
	private long rundeID;
	private long ID;
	private boolean gewinner;
	
	public Link(long ID,long rundeID, boolean gewinner) {
		this.gewinner=gewinner;
		this.rundeID=rundeID;
		this.ID=ID;
	}
	
	public void createXMLElements(Element parEl) {
		Element linkEl=new Element("Link");
		parEl.addContent(linkEl);
		
		Element rundeEl=new Element("Runde");
		rundeEl.addContent(""+rundeID);
		linkEl.addContent(rundeEl);
		
		Element IDEl=new Element("ID");
		IDEl.addContent(""+ID);
		linkEl.addContent(IDEl);
		
		Element gewinnerEl=new Element("Gewinner");
		gewinnerEl.addContent(""+gewinner);
		linkEl.addContent(gewinnerEl);
	}
	
	public String getDateiString() {
		return "<Link>\n<ID>"+ID+"</ID>\n<Runde>"+rundeID+"</Runde>\n"
				+ "<Gewinner>"+gewinner+"</Gewinner>\n</Link>\n";
	}
	
	public void setRunde(long ID, boolean gewinner) {
		this.rundeID=ID;
		this.gewinner=gewinner;
	}
	
	public long getID() {
		return ID;
	}
	
	public long getRundeID() {
		return rundeID;
	}
	
	public boolean getGewinner() {
		return gewinner;
	}
}
