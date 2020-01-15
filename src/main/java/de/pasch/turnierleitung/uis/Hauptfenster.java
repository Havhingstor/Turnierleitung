/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pasch.turnierleitung.uis;

import javax.swing.JOptionPane;

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
        MenuItem zuKORundenuebersicht=new MenuItem("Turnierelemente");
        MenuItem zuEinstellungen=new MenuItem("Einstellungen");
        MenuItem ueber=new MenuItem("Über");
        hilfe.getItems().add(ueber);
        ansichten.getItems().addAll(zuProtuebersichtMenu,
        		zuKORundenuebersicht);
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
    }
}
