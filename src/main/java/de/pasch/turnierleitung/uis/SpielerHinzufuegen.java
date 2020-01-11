/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pasch.turnierleitung.uis;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.steuerung.Steuerung;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author pasch
 */
public class SpielerHinzufuegen {

    private ToggleGroup kaptGruppe;
    private RadioButton vize;
    private RadioButton kapt;
    
    public SpielerHinzufuegen(Stage primStage,long teamID,Aktualisierer akt,Steuerung steuerung){
        int sondernummer;
        if(teamID==0){
            sondernummer=4;
        }else{
            sondernummer=3;
        }
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
        
        if(teamID!=0){
            kaptGruppe=new ToggleGroup();
            RadioButton nichts=new RadioButton("Nein");
            nichts.setSelected(true);
            kapt=new RadioButton("Kapitän");
            vize=new RadioButton("Vizekapitän");
            kaptGruppe.getToggles().addAll(nichts,kapt,vize);
            VBox kaptBox=new VBox();
            kaptBox.getChildren().addAll(nichts,vize,kapt);
            gp.add(kaptBox,1, 2);
            Label kaptLabel=new Label("Kapitän");
            gp.add(kaptLabel,0,2);
        }
        
        Button speichern=new Button("Speichern");
        speichern.setPrefWidth(145);
        gp.add(speichern,1,sondernummer);
        Scene scene=new Scene(gp,300,200);
        stage.setScene(scene);
        stage.show();
        speichern.setOnAction((ActionEvent e) -> {
            boolean erlaubt=true;
            for(Spieler s:steuerung.getSpieler()){
                if(s.getNachname().equals(nachnameFeld.getText())&&
                        s.getVorname().equals(vornameFeld.getText())){
                    erlaubt=false;
                }
            }
            if(erlaubt){
                steuerung.addSpieler(vornameFeld.getText(), nachnameFeld.getText(),teamID, 0);
                if(teamID!=0){
                    RadioButton rb=(RadioButton)kaptGruppe.getSelectedToggle();
                    if(rb==kapt||rb==vize){
                        if(rb==kapt){
                            steuerung.getTeams().stream().filter((t) -> (t.getID()==teamID)).forEachOrdered((t) -> {
                                t.setKapitaen(steuerung.getSpieler().get(steuerung.getSpieler().size()-1));
                            });
                        }
                        if(rb==vize){
                           steuerung.getTeams().stream().filter((t)->(t.getID()==teamID)).forEachOrdered((t)->{
                               t.setVizekapitaen(steuerung.getSpieler().get(steuerung.getSpieler().size()-1));
                           });
                        }    
                    }
                }
                akt.aktualisieren();
                stage.hide();
                
            }
        });
    }
}
