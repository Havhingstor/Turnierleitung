/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pasch.turnierleitung.uis;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.steuerung.Steuerung;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author pasch
 */
public class HFSpieleruebersicht {
    Aktualisierer akt;
    Stage stage;
    Steuerung steuerung;
    GridPane gp;
    Button[]spielerGes;
    
    public HFSpieleruebersicht(Stage stage,MenuBar mb,Steuerung steuerung,Aktualisierer akt){
        this.akt=akt;
        this.stage=stage;
        this.steuerung=steuerung;
        gp=new GridPane();
        BorderPane bp=new BorderPane();
        bp.setTop(mb);
        bp.setCenter(gp);
        gp.setPadding(new Insets(5,5,5,5));
        gp.setHgap(5);
        gp.setVgap(5);
        aktualisiere();
        Scene scene=new Scene(bp,1200,725);
        stage.setScene(scene);
        stage.show();
    }
    
    public void aktualisiere(){
        
        VBox schaltungen=new VBox();
        
        ComboBox<String> team=new ComboBox<String>();
        team.getItems().add("Alle");
        team.setValue("Alle");
        for(Team t:steuerung.getTeams()){
          team.getItems().add(t.getName());  
        }
        schaltungen.getChildren().add(team);
        
        VBox gap=new VBox();
        gap.setPrefHeight(20);
        schaltungen.getChildren().add(gap);
                 
        VBox box=new VBox();
        ScrollPane sp=new ScrollPane(box);
        sp.setPrefSize(400,650);
        spielerGes=new Button[steuerung.getSpieler().size()];
        for(int i=0;i<spielerGes.length;i++){
            spielerGes[i]=new Button(steuerung.getSpieler().get(i).toString());
            spielerGes[i].setPrefWidth(395);
            Spieler spielerEinz=steuerung.getSpieler().get(i);
            spielerGes[i].setOnAction((var e)->{
               new TeamDetail(steuerung,stage,spielerEinz,akt);
            });
        }
       
        box.getChildren().addAll(Arrays.asList(spielerGes));
        gp.add(sp,1,0,1,2);
        
        team.setOnAction((e)->{
            String s=(String)team.getValue();
            ArrayList<Spieler>spieler; 
            if(s.equals("Alle")){
                spieler=steuerung.getSpieler();
            }else{
                Team t = null;
                for(Team team2:steuerung.getTeams()){
                    if(team2.getName().equals(s)){
                        t=team2;
                    }
                }
                spieler=steuerung.getSpielerEinesTeams(t.getID());
            }
            VBox box2=new VBox();
            sp.setContent(box2);
            spielerGes=new Button[spieler.size()];
            for(int i=0;i<spielerGes.length;i++){
                spielerGes[i]=new Button(spieler.get(i).toString());
                spielerGes[i].setPrefWidth(395);
                Spieler spielerEinz=steuerung.getSpieler().get(i);
                spielerGes[i].setOnAction((var f)->{
                    System.out.println("nvhbf");
                   new TeamDetail(steuerung,stage,spielerEinz,akt); 
                });
            }box2.getChildren().addAll(Arrays.asList(spielerGes));
        });
                      
        Button neu=new Button("Neuer Spieler");
        neu.setPrefSize(100,100);
        schaltungen.getChildren().add(neu);
        gp.add(schaltungen,2,0);
        neu.setOnAction((e)->{
            String s=(String)team.getValue();
            if(s.equals("Alle")){ 
                new SpielerHinzufuegen(stage,0,akt,steuerung);
            }else{
                Team t = null;
                for(Team team2:steuerung.getTeams()){
                    if(team2.getName().equals(s)){
                        t=team2;
                    }
                }
                new SpielerHinzufuegen(stage,t.getID(),akt,steuerung);
            }
        });
        
        VBox boxl=new VBox();
        boxl.setPrefSize(400,50);
        gp.add(boxl, 0,1);
    }
}
