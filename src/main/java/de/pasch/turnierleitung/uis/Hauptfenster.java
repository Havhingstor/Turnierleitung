/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pasch.turnierleitung.uis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import javax.swing.JOptionPane;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import de.pasch.turnierleitung.steuerung.Steuerung;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author pasch
 */
public class Hauptfenster {
    HFProtagonisten hfp;
    Steuerung steuerung;
    Stage stage;
    HFTurnierelemente hft;
    HFSpieltag hfst;
    HFSpiele hfs;
    Einstellungsfenster einst;
    File aktSpeicherung=null;
    boolean spNoetig;
    
    public  Hauptfenster(Startfenster startfenster) {
        Startfenster sf=startfenster;
        steuerung=new Steuerung();
        if(sf.getDateiLadenBool()){
            try {
				steuerung.regeneriereAusDatei(sf.getDateiLadenFile());
				aktSpeicherung=sf.getDateiLadenFile();
				spNoetig=false;
			} catch (IOException | JDOMException e1) {
				JOptionPane.showMessageDialog(null,e1.getMessage(),"FEHLER!",0);
			}
        }else if(sf.getBeispielBool()) {
        	steuerung.setName("Beispielturner");
        	aktSpeicherung=null;
        	beispielSetup();
        	spNoetig=false;
        }else{
        	/*
            boolean erlaubt=false;
            String eingabe="";
            while(!erlaubt){
                eingabe=JOptionPane.showInputDialog("Welchen Namen soll es haben?");
                if(eingabe!=null) {
                    erlaubt=true;
	                if(eingabe.equals("")){
	                	eingabe="Neues Turnier";
	                }
                }else {
                    JOptionPane.showMessageDialog(null,"Es wurde kein Name eingegeben!");
                }
            }
            */
            steuerung.setName(startfenster.getTurniername());
            aktSpeicherung=null;
            spNoetig=false;
        }
        stage=new Stage();
        if(aktSpeicherung==null) {
        	stage.setTitle(steuerung.getName());
        }else {
        	stage.setTitle(steuerung.getName()+" - "+aktSpeicherung.getName());
        }
        
        MenuBar menuBar=new MenuBar();
        Menu datei=new Menu("Datei");
        Menu ansichten=new Menu("Ansichten");
        Menu hilfe=new Menu("Hilfe");
        menuBar.getMenus().addAll(datei,ansichten,hilfe);
        MenuItem dateiLaden=new MenuItem("Laden");
        MenuItem neu=new MenuItem("Neu");
        MenuItem spUnter=new MenuItem("Speichern unter");
        MenuItem speichern=new MenuItem("Speichern");
        MenuItem zuProtuebersichtMenu=new MenuItem("Protagonisten");
        MenuItem zuTurnierelementeuebersichtMenu=new MenuItem("Turnierelemente");
        MenuItem zuSpieltagsuebersichtMenu=new MenuItem("Spieltage");
        MenuItem zuSpieluebersichtMenu=new MenuItem("Spiele");
        MenuItem zuEinstellungen=new MenuItem("Einstellungen");
        MenuItem ueber=new MenuItem("Über");
        hilfe.getItems().add(ueber);
        ansichten.getItems().addAll(zuProtuebersichtMenu,
        		zuTurnierelementeuebersichtMenu,zuSpieltagsuebersichtMenu,zuSpieluebersichtMenu);
        datei.getItems().addAll(dateiLaden,neu,speichern,spUnter,zuEinstellungen);
        Aktualisierer akt=new Aktualisierer(this);
       
        
        
        ToolBar schnellwechsel=new ToolBar();
        Button zuProtuebersichtbSchn=new Button("Protagonisten");
        Button zuTurnuebersichtbSchn=new Button("Turnierelemente");
        Button zuSpieltaguebersichtSchn=new Button("Spieltage");
        Button zuSpieluebersichtSchn=new Button("Spiele");
       
        schnellwechsel.getItems().addAll(zuProtuebersichtbSchn,zuTurnuebersichtbSchn,zuSpieltaguebersichtSchn,zuSpieluebersichtSchn);
        BorderPane bp=new BorderPane();
        VBox menus=new VBox();
        menus.getChildren().addAll(menuBar,schnellwechsel);
        bp.setTop(menus);
        
        stage.setOnCloseRequest((e)->{
        	if(spNoetig) {
        	ButtonType ja=new ButtonType("Ja");
        	ButtonType nein=new ButtonType("Nein");
        	ButtonType abbr=new ButtonType("Abbrechen");
        	
        	Alert schließen=new Alert(Alert.AlertType.CONFIRMATION,
        			"Soll die Datei gespeichert werden?",ja,nein,abbr) ;
        	schließen.setHeaderText(null);
        	schließen.initModality(Modality.APPLICATION_MODAL);
        	
        	Optional<ButtonType> antwort=schließen.showAndWait();
        	if(antwort.get().equals(ja)) {
	        		boolean getan=false;
		        	if(aktSpeicherung!=null) {
		    				Document doc=new Document();
		            		doc.setRootElement(steuerung.getRootElement());
		                    Format format = Format.getPrettyFormat();
		                    format.setIndent("    ");
		                    try (FileOutputStream fos = new FileOutputStream(aktSpeicherung)) {
		                        XMLOutputter op = new XMLOutputter(format);
		                        op.output(doc, fos);
		                    } catch (IOException ioe) {
		                		JOptionPane.showMessageDialog(null,"Fehler beim Schreiben in die Datei!","FEHLER!",0);
		                		e.consume();
		                    } 
		            		getan=true;
		        		}
		        	if(!getan) {
		        		FileChooser fc=new FileChooser();
		            	fc.getExtensionFilters().addAll(
		                		new FileChooser.ExtensionFilter("Turnierleitungsdatei", "*.tul"),
		                		new FileChooser.ExtensionFilter("XML-Datei", "*.xml")
		                    );
		            	File neuDatei=null;
		            	neuDatei=fc.showSaveDialog(stage);
		        		if(neuDatei!=null) {
		    	        	Document doc=new Document();
		    	    		doc.setRootElement(steuerung.getRootElement());
		    	            Format format = Format.getPrettyFormat();
		    	            format.setIndent("    ");
		    	            try (FileOutputStream fos = new FileOutputStream(neuDatei)) {
		    	                XMLOutputter op = new XMLOutputter(format);
		    	                op.output(doc, fos);
		    	            } catch (IOException ioe) {
		    	        		JOptionPane.showMessageDialog(null,"Fehler beim Schreiben in die Datei!","FEHLER!",0);
		    	        		e.consume();
		    	            }
		    	    		aktSpeicherung=neuDatei;
		        		}
		        	}
	        	}else if(antwort.get().equals(abbr)) {
	        		e.consume();
	        	}
        	}
        });
        
        dateiLaden.setOnAction((e)->{
        	FileChooser fc=new FileChooser();
            fc.getExtensionFilters().addAll(
        		new FileChooser.ExtensionFilter("Turnierleitungsdatei", "*.tul"),
        		new FileChooser.ExtensionFilter("XML-Datei", "*.xml")
            );
            File file=fc.showOpenDialog(this.stage);
            if(file!=null) {
            	try {
    				steuerung.regeneriereAusDatei(file);
    				aktSpeicherung=file;
    				akt.aktualisieren();
    				spNoetig=false;
    			} catch (IOException | JDOMException e1) {
    				JOptionPane.showMessageDialog(null,e1.getMessage(),"FEHLER!",0);
    			}
            }
        	/*
        	FileChooser fc=new FileChooser();
            fc.getExtensionFilters().addAll(
        		new FileChooser.ExtensionFilter("Turnierleitungsdatei", "*.tul"),
        		new FileChooser.ExtensionFilter("XML-Datei", "*.xml")
            );
            File file=fc.showOpenDialog(stage);
            try {
				steuerung.regeneriereAusDatei(file);
				aktSpeicherung=file;
				akt.aktualisieren();
			} catch (IOException | JDOMException e1) {
				JOptionPane.showMessageDialog(null,e1.getMessage(),"FEHLER!",0);
			}
			*/
        });
        neu.setOnAction((e)->{
            	String eingabe=JOptionPane.showInputDialog("Welchen Namen soll es haben?");
        	if(eingabe!=null) {
                if(eingabe.equals("")){
                	eingabe="Neues Turnier";
                }
                steuerung=new Steuerung();
            	steuerung.setName(eingabe);
            	aktSpeicherung=null;
            	akt.aktualisieren();
            	spNoetig=false;
            }
        });
        speichern.setOnAction((e)->{
        	boolean getan=false;
        	if(aktSpeicherung!=null) {
    				Document doc=new Document();
            		doc.setRootElement(steuerung.getRootElement());
                    Format format = Format.getPrettyFormat();
                    format.setIndent("    ");
                    try (FileOutputStream fos = new FileOutputStream(aktSpeicherung)) {
                        XMLOutputter op = new XMLOutputter(format);
                        op.output(doc, fos);
                    } catch (IOException ioe) {
                		JOptionPane.showMessageDialog(null,"Fehler beim Schreiben in die Datei!","FEHLER!",0);
                    } 
            		akt.aktualisieren();
            		getan=true;
        		}
        	if(!getan) {
        		FileChooser fc=new FileChooser();
            	fc.getExtensionFilters().addAll(
                		new FileChooser.ExtensionFilter("Turnierleitungsdatei", "*.tul"),
                		new FileChooser.ExtensionFilter("XML-Datei", "*.xml")
                    );
            	File neuDatei=null;
            	neuDatei=fc.showSaveDialog(stage);
        		if(neuDatei!=null) {
    	        	Document doc=new Document();
    	    		doc.setRootElement(steuerung.getRootElement());
    	            Format format = Format.getPrettyFormat();
    	            format.setIndent("    ");
    	            try (FileOutputStream fos = new FileOutputStream(neuDatei)) {
    	                XMLOutputter op = new XMLOutputter(format);
    	                op.output(doc, fos);
    	            } catch (IOException ioe) {
    	        		JOptionPane.showMessageDialog(null,"Fehler beim Schreiben in die Datei!","FEHLER!",0);
    	            }
    	    		aktSpeicherung=neuDatei;
    	    		akt.aktualisieren();
        		}
        	}
        	spNoetig=false;
        });
        spUnter.setOnAction((e)->{
        	FileChooser fc=new FileChooser();
        	fc.getExtensionFilters().addAll(
            		new FileChooser.ExtensionFilter("Turnierleitungsdatei", "*.tul"),
            		new FileChooser.ExtensionFilter("XML-Datei", "*.xml")
                );
        	File neuDatei=null;
    		neuDatei=fc.showSaveDialog(stage);
    		if(neuDatei!=null) {
	        	Document doc=new Document();
	    		doc.setRootElement(steuerung.getRootElement());
	            Format format = Format.getPrettyFormat();
	            format.setIndent("    ");
	            try (FileOutputStream fos = new FileOutputStream(neuDatei)) {
	                XMLOutputter op = new XMLOutputter(format);
	                op.output(doc, fos);
	            } catch (IOException ioe) {
	        		JOptionPane.showMessageDialog(null,"Fehler beim Schreiben in die Datei!","FEHLER!",0);
	            }
	    		aktSpeicherung=neuDatei;
	    		akt.aktualisieren();
	    		spNoetig=false;
    		}
        });
        zuProtuebersichtMenu.setOnAction((e)->{
            hfp= new HFProtagonisten(stage,bp,steuerung,akt);
         });
        zuProtuebersichtbSchn.setOnAction((e)->{
        	 hfp= new HFProtagonisten(stage,bp,steuerung,akt);
        });
        ueber.setOnAction((e)->{
        	new UeberFenster(steuerung, stage);
        });
        zuEinstellungen.setOnAction((e)->{
        	einst=new Einstellungsfenster(stage, steuerung, akt);
        });  
        zuTurnierelementeuebersichtMenu.setOnAction((e)->{
        	hft=new HFTurnierelemente(stage, bp, steuerung, akt);
        });
        zuTurnuebersichtbSchn.setOnAction((e)->{
        	hft=new HFTurnierelemente(stage, bp, steuerung, akt);
        });
        zuSpieltagsuebersichtMenu.setOnAction((e)->{
        	hfst=new HFSpieltag(stage, bp, steuerung, akt);
        });
        zuSpieltaguebersichtSchn.setOnAction((e)->{
        	hfst=new HFSpieltag(stage, bp, steuerung, akt);
        });
        zuSpieluebersichtMenu.setOnAction((e)->{
        	hfs=new HFSpiele(stage,bp,steuerung,akt);
        });
        zuSpieluebersichtSchn.setOnAction((e)->{
        	hfs=new HFSpiele(stage,bp,steuerung,akt);        	
        });
                
        stage.setMaximized(true);
        Scene scene=new Scene(bp,1200,800);
        stage.setScene(scene);
        hfp=new HFProtagonisten(stage, bp, steuerung, akt);
    }
    
    public void aktualisieren(){
    	spNoetig=true;
        if((stage!=null)&&(steuerung!=null)){
            if(aktSpeicherung==null) {
            	stage.setTitle(steuerung.getName());
            }else {
            	stage.setTitle(steuerung.getName()+" - "+aktSpeicherung.getName());
            }
        }
        if(hfp!=null){
            hfp.aktualisiere(steuerung);
        }
        if(hft!=null){
            hft.aktualisiere(steuerung);
        }
        if(einst!=null) {
        	einst.aktualisiere(steuerung);
        }
        if(hfst!=null) {
        	hfst.aktualisiere(steuerung);
        }
        if(hfs!=null) {
        	hfs.aktualisiere(steuerung);
        }
    }
    
   public void beispielSetup() {
	   steuerung.addTeam("FC Bayern München","FCB","Allianz-Arena");
	   steuerung.addTeam("SV Werder Bremen","SVW","wohninvest Weserstadion");
		int[]ar= {1,2,3,4,5};
		steuerung.addLiga(3,1,0,"Testliga",ar);
		steuerung.addTeamZuLiga(1000001, 1000003);
		steuerung.addTeamZuLiga(1000002,1000003);
		steuerung.addSpieltag(steuerung.getLigen().get(0).getID(),"TestST");
		
		steuerung.addSpiel(1000001l, 1000002l, false, false, "", steuerung.getSpieltage().get(0).getID(), steuerung.getLigen().get(0));
		steuerung.addTeam("1. FC Nürnberg","FCN","Max-Morlock-Stadion");
		steuerung.addTeamZuLiga(steuerung.getTeams().get(2).getID(), 1000003);
		steuerung.addSpieler("Manuel", "Neuer",1000001 , 1);
		steuerung.addSpieler("Thomas", "Müller",1000001 , 25);
		steuerung.addSpieler("Joshua", "Kimmich",1000001 ,6);
		steuerung.addSpieler("Joshua", "Zirkzee",1000001 ,14);
		steuerung.getTeams().get(0).setKapitaen(steuerung.getSpieler().get(0));
		steuerung.getTeams().get(0).setVizekapitaen(steuerung.getSpieler().get(1));
		steuerung.getSpiele().get(0).getAufstHeim().addSpielerStartelf(steuerung.getSpieler().get(0));
		steuerung.getSpiele().get(0).getAufstHeim().addSpielerStartelf(steuerung.getSpieler().get(1));
		steuerung.getSpiele().get(0).getAufstHeim().addSpielerStartelf(steuerung.getSpieler().get(2));
		steuerung.getSpiele().get(0).getAufstHeim().addSpielerBank(steuerung.getSpieler().get(3));
		steuerung.getSpiele().get(0).getAufstHeim().setKapitaenID(steuerung.getSpieler().get(0).getID());
        steuerung.addTor(true, steuerung.getSpieler().get(1).getID(), 0,steuerung.getSpiele().get(0).getID(), 17, 0, 2);
        steuerung.addStrafe(true, steuerung.getSpieler().get(2).getID(), 0,steuerung.getSpiele().get(0).getID(), 35, 0, 0);
   }
   
   public static void setLink(Text text,Handler<Text> h) {
	   text.setUnderline(true);
	   text.setOnMouseEntered((e)->{
		   text.setFill(Color.BLUE);
	   });
	   text.setOnMouseExited((e)->{
		   text.setFill(Color.BLACK);
	   });
	   text.setOnMouseClicked((e)->{
		   h.handle(text);
	   });
   }
}
