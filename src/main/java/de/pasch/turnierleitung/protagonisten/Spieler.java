package de.pasch.turnierleitung.protagonisten;

public class Spieler extends Protagonist implements Linkface {
	
	private String vorname="";
	private String nachname="";
	
	public Spieler(String vorname, String nachname, long ID){
		super(vorname+" "+nachname,ID);
		this.vorname=vorname;
		this.nachname=nachname;
	}

	public void setName(String vorname,String nachname) {
		this.setName(vorname+" "+nachname);
		this.vorname=vorname;
		this.nachname=nachname;
	}
	
	public String getDateiString() {
		String string="<Spieler>\n"+super.getDateiString()+"<Vorname>\n"+vorname
		+"\n</Vorname\n<Nachname>\n"+nachname+"\n</Nachname>\n</Spieler>\n";
		return string;
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
