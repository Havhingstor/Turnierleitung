/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pasch.turnierleitung.uis;

import javax.swing.JOptionPane;

import de.pasch.turnierleitung.steuerung.Steuerung;
import javafx.event.ActionEvent;
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
public class SpielerHinzufuegen {
    
    public SpielerHinzufuegen(Stage primStage,long teamID,Aktualisierer akt,Steuerung steuerung){
        Stage stage=new Stage();
        stage.setTitle("Neuer Spieler");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primStage);
        GridPane gp=new GridPane();
        gp.setPadding(new Insets(5,5,5,5));
        gp.setHgap(5);
        gp.setVgap(5);
        Label vornameLabel=new Label("Vorname");
        vornameLabel.setPrefWidth(145);
        TextField vornameFeld=new TextField();
        vornameFeld.setPrefWidth(145);
        gp.add(vornameLabel, 0, 0);
        gp.add(vornameFeld, 1, 0);
        Label nachnameLabel=new Label("Nachname");
        nachnameLabel.setPrefWidth(145);
        TextField nachnameFeld=new TextField();
        nachnameFeld.setPrefWidth(145);
        gp.add(nachnameLabel,0,1);
        gp.add(nachnameFeld,1,1);
        
        Button speichern=new Button("Speichern");
        speichern.setPrefWidth(145);
        gp.add(speichern,1,2);
        Scene scene=new Scene(gp,300,200);
        stage.setScene(scene);
        stage.show();
        speichern.setOnAction((ActionEvent e) -> {
            
        	try{
                steuerung.addSpieler(vornameFeld.getText(), nachnameFeld.getText(),teamID, 0);
                akt.aktualisieren();
                stage.hide();
                
            }catch(IllegalArgumentException iae) {
            	JOptionPane.showMessageDialog(null,iae.getMessage(),"Fehler",0);
            }
        });
    }
}
