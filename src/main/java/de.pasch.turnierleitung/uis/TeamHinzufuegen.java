/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pasch.turnierleitung.uis;

import javax.swing.JOptionPane;

import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.steuerung.Steuerung;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author pasch
 */
public class TeamHinzufuegen {
    
    public TeamHinzufuegen(Stage primStage,Aktualisierer akt,Steuerung steuerung,Handler<Team> h){
        Stage stage=new Stage();
        stage.setTitle("Neues Team");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primStage);
        GridPane gp=new GridPane();
        gp.setPadding(new Insets(5,5,5,5));
        gp.setHgap(5);
        gp.setVgap(5);
        Label nameLabel=new Label("Name");
        nameLabel.setPrefWidth(145);
        TextField nameFeld=new TextField();
        nameFeld.setPrefWidth(145);
        gp.add(nameLabel, 0, 0);
        gp.add(nameFeld, 1, 0);
        Label kurznLabel=new Label("Kurzname");
        kurznLabel.setPrefWidth(145);
        TextField kurznFeld=new TextField();
        kurznFeld.setPrefWidth(145);
        gp.add(kurznLabel,0,1);
        gp.add(kurznFeld,1,1);
        Label stadionLabel=new Label("Heimstadion");
        stadionLabel.setPrefWidth(145);
        TextField stadionFeld=new TextField();
        stadionFeld.setPrefWidth(145);
        gp.add(stadionLabel,0,2);
        gp.add(stadionFeld,1,2);
        Button speichern=new Button("Speichern");
        speichern.setPrefWidth(145);
        gp.add(speichern,1,3);
        Scene scene=new Scene(gp,300,200);
        stage.setScene(scene);
        stage.show();
        speichern.setOnAction((e)->{
            try{
                if(!nameFeld.getText().equals("")){
                    steuerung.addTeam(nameFeld.getText(),kurznFeld.getText(),stadionFeld.getText());
                    h.handle(steuerung.getTeams().get(steuerung.getTeams().size()-1));
                    akt.aktualisieren();
                    stage.hide();
                }else{
                    JOptionPane.showMessageDialog(null,"Kein Name eingegeben!","FEHLER!",0);
                }
            }catch(IllegalArgumentException iae){
                JOptionPane.showMessageDialog(null,iae.getMessage(),"Fehler",0);
            }
        });
    }
}
