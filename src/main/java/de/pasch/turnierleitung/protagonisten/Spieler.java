package de.pasch.turnierleitung.protagonisten;

import org.jdom2.Element;

public class Spieler extends Protagonist implements Linkface {
	
	private String vorname="";
	private String nachname="";
	
	public Spieler(String vorname, String nachname, long ID){
		super(vorname+" "+nachname,ID);
		this.vorname=vorname;
		this.nachname=nachname;
	}
	
	public Spieler(Element parEl) {
		super(parEl);
		
		for(Element el:parEl.getChildren()) {
			if(el.getName().equals("Vorname")) {
				vorname=el.getValue();
			}else if(el.getName().equals("Nachname")) {
				nachname=el.getValue();
			}
		}
	}
	
	public void setName(String vorname,String nachname) {
		this.setName(vorname+" "+nachname);
		this.vorname=vorname;
		this.nachname=nachname;
	}
	
	public String getDateiString() {
		String string="<Spieler>\n"+super.getDateiString()+"<Vorname>"+vorname
		+"</Vorname\n<Nachname>"+nachname+"</Nachname>\n</Spieler>\n";
		return string;
	}
	
	public void createXMLElements(Element parEl) {
		Element element=new Element("EinSpieler");
		parEl.addContent(element);
		
		super.createXMLElements(element);
		

		Element vornameEl=new Element("Vorname");
		vornameEl.addContent(vorname);
		element.addContent(vornameEl);
		
		Element nachnameEl=new Element("Nachname");
		nachnameEl.addContent(nachname);
		element.addContent(nachnameEl);
	}
	
	public String getVorname() {
		return vorname;
	}
	
	public String getNachname() {
		return nachname;
	}
        
        @Override
        public String toString(){
           return vorname+" "+nachname; 
        }
}
