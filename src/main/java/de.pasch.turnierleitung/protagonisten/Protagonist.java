package de.pasch.turnierleitung.protagonisten;

import org.jdom2.Element;

import de.pasch.turnierleitung.steuerung.Pickable;

public class Protagonist implements Pickable {
	
	protected long ID=0;
	protected String name="";
	
	protected Protagonist(String name,long ID){
		this.ID=ID;
		this.name=name;
	}
	
	protected Protagonist(Element parEl) {
		for(Element el:parEl.getChildren()) {
			if(el.getName().equals("ID")) {
				ID=Long.parseLong(el.getValue());
			}else if(el.getName().equals("Name")) {
				name=el.getValue();
			}
		}
	}
	
	public long getID() {
		return ID;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return (name+" (ID: "+ID+")");
	}
	
	public void setID(long ID) {
		this.ID=ID;
	}
	
	public void setName(String name) {
		this.name=name;
	}

	public String getDateiString() {
		String string="<ID>"+ID+"</ID>\n<Name>"+name+"</Name>\n";		
		return string;
	}
	
	public void createXMLElements(Element parEl) {
		Element id=new Element("ID");
		id.addContent(""+ID);
		parEl.addContent(id);
		Element nameEl=new Element("Name");
		nameEl.addContent(name);
		parEl.addContent(nameEl);
	}
}
