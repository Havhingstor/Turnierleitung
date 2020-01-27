/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pasch.turnierleitung.uis;

import javax.swing.JOptionPane;

import de.pasch.turnierleitung.probierUndTestklassen.Testklasse;
import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Steuerung;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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
    
    public  Hauptfenster(Startfenster startfenster) {
        Startfenster sf=startfenster;
        steuerung=new Steuerung();
        if(sf.getDateiLadenBool()){
            //steuerung.ladeDatei(sf.getDateiLadenFIle)
        }else{
            boolean erlaubt=false;
            String eingabe="";
            while(!erlaubt){
                eingabe=JOptionPane.showInputDialog("Welchen Namen soll es haben?");
                if(!eingabe.equals("")){
                    erlaubt=true;
                }else{
                    JOptionPane.showMessageDialog(null,"Es wurde kein Name eingegeben!");
                }
            }
            steuerung.setName(eingabe);
        }
        stage=new Stage();
        stage.setTitle(steuerung.getName());
        MenuBar menuBar=new MenuBar();
        Menu datei=new Menu("Datei");
        Menu ansichten=new Menu("Ansichten");
        Menu hilfe=new Menu("Hilfe");
        menuBar.getMenus().addAll(datei,ansichten,hilfe);
        MenuItem zuProtuebersichtMenu=new MenuItem("Protagonisten");
        MenuItem zuTurnierelementeuebersichtMenu=new MenuItem("Turnierelemente");
        MenuItem zuEinstellungen=new MenuItem("Einstellungen");
        MenuItem ueber=new MenuItem("Über");
        hilfe.getItems().add(ueber);
        ansichten.getItems().addAll(zuProtuebersichtMenu,
        		zuTurnierelementeuebersichtMenu);
        datei.getItems().add(zuEinstellungen);
        Aktualisierer akt=new Aktualisierer(this);
       
        
        
        ToolBar schnellwechsel=new ToolBar();
        Button zuProtuebersichtbSchn=new Button("Protagonisten");
        Button zuTurnuebersichtbSchn=new Button("Turnierelemente");
       
        schnellwechsel.getItems().addAll(zuProtuebersichtbSchn,zuTurnuebersichtbSchn);
        BorderPane bp=new BorderPane();
        VBox menus=new VBox();
        menus.getChildren().addAll(menuBar,schnellwechsel);
        bp.setTop(menus);
        
        zuProtuebersichtMenu.setOnAction((e)->{
            hfp= new HFProtagonisten(stage,bp,steuerung,akt);
         });
        zuProtuebersichtbSchn.setOnAction((e)->{
        	 hfp= new HFProtagonisten(stage,bp,steuerung,akt);
        });
        ueber.setOnAction((e)->{
        	new UeberFenster(steuerung);
        });
        zuEinstellungen.setOnAction((e)->{
        	new Einstellungsfenster(stage, steuerung, akt);
        });  
        zuTurnierelementeuebersichtMenu.setOnAction((e)->{
        	hft=new HFTurnierelemente(stage, bp, steuerung, akt);
        });
        zuTurnuebersichtbSchn.setOnAction((e)->{
        	hft=new HFTurnierelemente(stage, bp, steuerung, akt);
        });
        
        
        steuerung.addTeam("FC Bayern München","FCB","Allianz-Arena");
		steuerung.addTeam("SV Werder Bremen","SVW","wohninvest Weserstadion");
                steuerung.getTeams().stream().map((Team team) -> {
                    System.out.println(team.toString());
                    return team;
                }).forEachOrdered((team) -> {
                System.out.println(team.getHeimstadion());
            });
		int[]ar= {1,2,3,4,5};
		steuerung.addLiga(3,1,0,"Testliga",ar);
		steuerung.addTeamZuLiga(1000001, 1000003);
		steuerung.addTeamZuLiga(1000002,1000003);
		steuerung.addSpieltag(steuerung.getLigen().get(0).getID(),"TestST");
		
		steuerung.addSpiel(1000001l, 1000002l, false, false, "", steuerung.getSpieltage().get(0).getID());
                steuerung.getSpiele().stream().map((spiel) -> {
                    System.out.println(spiel.abschließenUndGewinner());
                return spiel;
            }).map((spiel) -> {
                System.out.println(spiel.getStadion());
                return spiel;
            }).forEachOrdered((spiel) -> {
                System.out.println(spiel.getID());
            });
		steuerung.addTor(true, 0, 0,steuerung.getSpiele().get(0).getID(), 17, 0, 2);
                steuerung.getSpiele().stream().map((spiel) -> {
                    System.out.println(spiel.abschließenUndGewinner());
                return spiel;
            }).map((spiel) -> {
                System.out.println(spiel.getStadion());
                return spiel;
            }).map((spiel) -> {
                System.out.println(spiel.getID());
                return spiel;
            }).map((spiel) -> {
                System.out.println(spiel.getHeimtore().get(0).getZeit());
                return spiel;
            }).forEachOrdered((spiel) -> {
                System.out.println(IDPicker.pick(steuerung.getTeams(),spiel.getGewinnerID()));
            });
		System.out.println(steuerung.getLigen().get(0).berechneGetAktuelleTabelle());
		steuerung.addTeam("1. FC Nürnberg","FCN","Max-Morlock-Stadion");
		steuerung.addTeamZuLiga(steuerung.getTeams().get(2).getID(), 1000003);
		System.out.println(steuerung.getLigen().get(0).berechneGetAktuelleTabelle());
                steuerung.getTeams().stream().map((team) -> {
                    System.out.println(team.toString());
                return team;
            }).forEachOrdered((team) -> {
                System.out.println(team.getHeimstadion());
            });
	
        
        
        stage.setMaximized(true);
        Scene scene=new Scene(bp,1200,800);
        stage.setScene(scene);
        hfp=new HFProtagonisten(stage, bp, steuerung, akt);
    }
    
    public void aktualisieren(){
        if((stage!=null)&&(steuerung!=null)){
            stage.setTitle(steuerung.getName());
        }
        if(hfp!=null){
            hfp.aktualisiere();
        }
        if(hft!=null){
            hft.aktualisiere();
        }
    }
}
