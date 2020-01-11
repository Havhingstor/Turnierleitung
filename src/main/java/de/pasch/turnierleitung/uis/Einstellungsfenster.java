/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pasch.turnierleitung.uis;

import de.pasch.turnierleitung.steuerung.Steuerung;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author pasch
 */
public class Einstellungsfenster {
    Stage stage;
    Steuerung steuerung;
    Aktualisierer akt;
    GridPane gp;
    Button tNeu,tLoe,sNeu,sLoe;
    
    public Einstellungsfenster(Stage primStage,Steuerung steuerung,Aktualisierer akt){
        this.akt=akt;
        this.steuerung=steuerung;
        stage=new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primStage);
        gp=new GridPane();
        gp.setPadding(new Insets(5,5,5,5));
        gp.setHgap(5);
        gp.setVgap(5);
        aktualisiere();
        Scene scene=new Scene(gp,300,300);
        stage.setScene(scene);
        stage.show();
    }
    
    public void aktualisiere(){
        VBox tB1=new VBox();
        VBox tB2=new VBox();
        ScrollPane tSp=new ScrollPane(tB2);
        Label toreName=new Label("Torarten");
        tB1.getChildren().add(toreName);
        tB1.getChildren().add(tSp);
        steuerung.getTorarten().forEach((s) -> {
            tB2.getChildren().add(new Label(s));
        });
        gp.add(tB1,0,0,1,2);
        tSp.setPrefSize(100,300);
       tNeu=new Button("Torart hinzufügen");
        tNeu.setPrefSize(150,50);
        gp.add(tNeu,1,0);
        tLoe=new Button("Torart entfernen");
        tLoe.setPrefSize(150,50);
        gp.add(tLoe,1,1);
        
        VBox sB1=new VBox();
        VBox sB2=new VBox();
        ScrollPane sSp=new ScrollPane(sB2);
        Label strafenName=new Label("Strafenarten");
        sB1.getChildren().add(strafenName);
        sB1.getChildren().add(sSp);
        steuerung.getStrafenarten().forEach((s) -> {
            sB2.getChildren().add(new Label(s));
        });
        gp.add(sB1,0,3,1,2);
        sSp.setPrefSize(100,300);
        sNeu=new Button("Strafenart hinzufügen");
        sNeu.setPrefSize(150,50);
        gp.add(sNeu,1,3);
        sLoe=new Button("Strafenart entfernen");
        sLoe.setPrefSize(150,50);
        gp.add(sLoe,1,4);
        
        tNeu.setOnAction((e)->{
            String eingabe=JOptionPane.showInputDialog(null,"Wie soll die Torart heißen?");
            if(!eingabe.equals("")){
                if(!steuerung.getTorarten().contains(eingabe)){
                    steuerung.getTorarten().add(eingabe);
                    akt.aktualisieren();
                }else{
                    JOptionPane.showMessageDialog(null,"Diese Torart existiert schon!","FEHLER!",0);
                }
            }else{
                 JOptionPane.showMessageDialog(null,"Es wurde nichts eingegeben!","FEHLER!",0);
            }        
        });
        
        tLoe.setOnAction((e)->{
            new ListDialog<String>(steuerung.getTorarten(),stage,"Welche Torart soll gelöscht werden?",
            "Torart löschen",(f)->{
                steuerung.getTorarten().remove(f);
                akt.aktualisieren();
            });
        });
        
        sNeu.setOnAction((e)->{
            String eingabe=JOptionPane.showInputDialog(null,"Wie soll die Strafenart heißen?");
            if(!eingabe.equals("")){
                if(!steuerung.getStrafenarten().contains(eingabe)){
                    steuerung.getStrafenarten().add(eingabe);
                    akt.aktualisieren();
                }else{
                    JOptionPane.showMessageDialog(null,"Diese Strafenart existiert schon!","FEHLER!",0);
                }
            }else{
                 JOptionPane.showMessageDialog(null,"Es wurde nichts eingegeben!","FEHLER!",0);
            }        
        });
        
        sLoe.setOnAction((e)->{
            String eingabe=JOptionPane.showInputDialog(null,"Welche Strafenart soll gelöscht werden?");
            if(!eingabe.equals("")){
                if(steuerung.getStrafenarten().contains(eingabe)){
                    steuerung.getStrafenarten().remove(eingabe);
                    akt.aktualisieren();
                }else{
                    JOptionPane.showMessageDialog(null,"Diese Strafenart existiert nicht!","FEHLER!",0);
                }
            }else{
                 JOptionPane.showMessageDialog(null,"Es wurde nichts eingegeben!","FEHLER!",0);
            } 
        });
    }
}
