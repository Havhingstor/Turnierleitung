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
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author pasch
 */
public class TeamBearbeitung {
    Steuerung steuerung;
    long ID;
    
    public TeamBearbeitung(Steuerung steuerung,Stage primStage,Team team,Aktualisierer akt){
        this.ID=team.getID();
        Stage stage=new Stage();
        stage.setTitle("Team bearbeiten");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primStage);
        GridPane gp=new GridPane();
        gp.setPadding(new Insets(5,5,5,5));
        gp.setHgap(5);
        gp.setVgap(5);
        Label nameLabel=new Label("Name");
        nameLabel.setPrefWidth(145);
        Label nameAlt=new Label(team.getName());
        nameAlt.setPrefWidth(145);
        nameAlt.setFont(Font.font("Verdana",FontWeight.MEDIUM,FontPosture.ITALIC,15));
        TextField nameFeld=new TextField(nameAlt.getText());
        nameFeld.setPrefWidth(145);
        gp.add(nameLabel, 0, 0);
        gp.add(nameAlt,1,0);
        gp.add(nameFeld,2, 0);
        Label kurznLabel=new Label("Kurzname");
        kurznLabel.setPrefWidth(145);
        Label kurznAlt=new Label(team.getKurzname());
        kurznAlt.setPrefWidth(145);
        kurznAlt.setFont(Font.font("Verdana",FontWeight.MEDIUM,FontPosture.ITALIC,15));
        TextField kurznFeld=new TextField(kurznAlt.getText());
        kurznFeld.setPrefWidth(145);
        gp.add(kurznLabel,0,1);
        gp.add(kurznAlt,1,1);
        gp.add(kurznFeld,2,1);
        Label stadionLabel=new Label("Heimstadion");
        stadionLabel.setPrefWidth(145);
        Label stadionAlt=new Label(team.getHeimstadion());
        stadionAlt.setPrefWidth(145);
        stadionAlt.setFont(Font.font("Verdana",FontWeight.MEDIUM,FontPosture.ITALIC,15));
        TextField stadionFeld=new TextField(stadionAlt.getText());
        stadionFeld.setPrefWidth(145);
        gp.add(stadionLabel,0,2);
        gp.add(stadionAlt,1,2);
        gp.add(stadionFeld,2,2);
        Button speichern=new Button("Speichern");
        speichern.setPrefWidth(145);
        gp.add(speichern,2,3);
        Button loeschen=new Button("Löschen");
        loeschen.setPrefWidth(145);
        gp.add(loeschen,1,3);
        Scene scene=new Scene(gp,450,200);
        stage.setScene(scene);
        stage.show();
        speichern.setOnAction((e)->{
            if(!nameFeld.getText().equals("")){
                steuerung.editTeam(kurznFeld.getText(),nameFeld.getText(),
                stadionFeld.getText(),ID);
                System.out.println(steuerung.getTeams().get(0).toString());
                akt.aktualisieren();
                stage.hide();
            }else{
                JOptionPane.showMessageDialog(null,"Kein Name eingegeben!","FEHLER!",0);
            }
        });
        loeschen.setOnAction((e)->{
            int best=JOptionPane.showConfirmDialog(null,"Wollen sie dieses Team wirklich löschen?","ACHTUNG!",2,2);
            if (best==0){
                try{
                    steuerung.removeTeam(ID);
                }catch(IllegalArgumentException iae){
                   JOptionPane.showMessageDialog(null,iae.getMessage(),"Fehler",0);
                }
                
                akt.aktualisieren();
                stage.hide();
            }
        });
    }
}
