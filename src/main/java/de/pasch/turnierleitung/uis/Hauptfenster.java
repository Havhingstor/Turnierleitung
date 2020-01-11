/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pasch.turnierleitung.uis;

import de.pasch.turnierleitung.steuerung.Steuerung;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import javafx.scene.control.*;

/**
 *
 * @author pasch
 */
public class Hauptfenster {
    HFStartbereich hfs;
    Steuerung steuerung;
    Stage stage;
    HFTeamuebersicht hft;
    HFSpieleruebersicht hfsp;
    
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
        menuBar.getMenus().addAll(datei,ansichten);
        MenuItem zuHauptfenster=new MenuItem("Hauptfenster");
        MenuItem zuTeamuebersicht=new MenuItem("Teams");
        MenuItem zuSpieleruebersicht=new MenuItem("Spieler");
        MenuItem zuKORundenuebersicht=new MenuItem("KO-Runden");
        MenuItem zuLigenuebersicht=new MenuItem("Ligen");
        ansichten.getItems().addAll(zuHauptfenster,zuTeamuebersicht,
                zuSpieleruebersicht,zuKORundenuebersicht,zuLigenuebersicht);
        Aktualisierer akt=new Aktualisierer(this);
        zuHauptfenster.setOnAction((e)->{
            hfs=new HFStartbereich(stage,menuBar,steuerung,akt);
        });
        zuTeamuebersicht.setOnAction((e)->{
           hft= new HFTeamuebersicht(stage,menuBar,steuerung,akt);
        });
        zuSpieleruebersicht.setOnAction((e)->{
           hfsp= new HFSpieleruebersicht(stage,menuBar,steuerung,akt);
        });
        hfs=new HFStartbereich(stage,menuBar,steuerung,akt);
    }
    
    public void aktualisieren(){
        if((stage!=null)&&(steuerung!=null)){
            stage.setTitle(steuerung.getName());
        }
        if(hfs!=null){
            hfs.aktualisiere();
        }
        if(hfsp!=null){
            hfsp.aktualisiere();
        }
        if(hft!=null){
            hft.aktualisiere();
        }
    }
}
