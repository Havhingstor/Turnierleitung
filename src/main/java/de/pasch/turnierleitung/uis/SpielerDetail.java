/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pasch.turnierleitung.uis;

import java.util.ArrayList;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.protagonisten.SpielerTeamConnector;
import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Steuerung;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author pasch
 */
public class SpielerDetail {
    Stage stage=null;
    private Spieler spieler;
    private Steuerung steuerung;
    private Aktualisierer akt;
	
    public SpielerDetail(Steuerung steuerung,Stage primStage,Spieler spieler,Aktualisierer akt){
        stage=new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primStage);  
        this.spieler=spieler;
        this.steuerung=steuerung;
        this.akt=akt;
        aktualisiere();
    }
    
    public void aktualisiere() {
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
        
        Button wechsel=new Button("Wechsel");
        gp.add(wechsel,1,3);
        wechsel.setOnAction((e)->{
        	ArrayList<Team>teams=new ArrayList<Team>();
        	Team team = null;
            for(SpielerTeamConnector stc:steuerung.getSTC()){
                if(stc.getSpielerID()==spieler.getID()&&(!stc.getAusgetreten())){
                 team=IDPicker.pick(steuerung.getTeams(),stc.getTeamID());   
                }
            }

        	for(Team team1:steuerung.getTeams()) {
        		if(!team1.equals(team)) {
        			teams.add(team1);
        		}
        	}
        	new ListDialog<Team>(teams, stage, "Wohin soll der Spieler wechseln?",
        			"Wechsel",(f)->{
	        		steuerung.editSpieler(spieler.getVorname(), spieler.getNachname(),
	        				0, f.getID(),spieler.getID());
	        		akt.aktualisieren();
        	});
        });
        
        Button bearbeiten=new Button("Bearbeiten");
        gp.add(bearbeiten,0,3);
        
        Scene scene=new Scene(gp,400,200);
        stage.setScene(scene);
        stage.show();
    }
}
