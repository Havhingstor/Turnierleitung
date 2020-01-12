/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pasch.turnierleitung.uis;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.protagonisten.SpielerTeamConnector;
import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Steuerung;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author pasch
 */
public class TeamDetail {
    
    public TeamDetail(Steuerung steuerung,Stage primStage,Spieler spieler,Aktualisierer akt){
        Stage stage=new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primStage);
        GridPane gp=new GridPane();
        gp.setAlignment(Pos.TOP_CENTER);
        gp.setPadding(new Insets(5,5,5,5));
        gp.setVgap(5);
        gp.setHgap(5);
        Label vornameLabelL=new Label("Vorname:");
        Label vornameLabelR=new Label(spieler.getVorname());
        gp.add(vornameLabelL, 0, 0);
        gp.add(vornameLabelR, 1, 0);
        Label nachnameLabelL=new Label("Nachname:");
        Label nachnameLabelR=new Label(spieler.getNachname());
        gp.add(nachnameLabelL, 0, 1);
        gp.add(nachnameLabelR, 1, 1);
        Label teamLabelL=new Label("Aktuelles Team:");
        Team t = null;
        for(SpielerTeamConnector stc:steuerung.getSTC()){
            if(stc.getSpielerID()==spieler.getID()&&(!stc.getAusgetreten())){
             t=IDPicker.pick(steuerung.getTeams(),stc.getTeamID());   
            }
        }
        Label teamLabelR;
        if(t!=null){
            teamLabelR=new Label(t.getName());
        }else{
            teamLabelR=new Label("Nicht vorhanden");
        }
        gp.add(teamLabelL, 0,2);
        gp.add(teamLabelR, 1,2);
        Scene scene=new Scene(gp,500,500);
        stage.setScene(scene);
        stage.show();
        
    }
}
