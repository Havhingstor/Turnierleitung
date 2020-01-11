/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pasch.turnierleitung.uis;

import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.steuerung.Steuerung;
import java.util.Arrays;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
public class HFTeamuebersicht {
    Stage stage;
    Steuerung steuerung;
    Button[]teams;
    GridPane gp;
    Aktualisierer akt;
    
    public HFTeamuebersicht(Stage stage,MenuBar mb,Steuerung steuerung,Aktualisierer akt){
        this.akt=akt;
        this.stage=stage;
        this.steuerung=steuerung;
        BorderPane bp=new BorderPane();
        bp.setTop(mb);
        gp=new GridPane();
        bp.setCenter(gp);
        gp.setPadding(new Insets(5,5,5,5));
        gp.setHgap(5);
        gp.setVgap(5);
        VBox box=new VBox();
        ScrollPane sp=new ScrollPane(box);
        sp.setPrefSize(400,650);
        teams=new Button[steuerung.getTeams().size()];
        for(int i=0;i<teams.length;i++){
            teams[i]=new Button(steuerung.getTeams().get(i).toString());
            teams[i].setPrefWidth(395);
            Team team=steuerung.getTeams().get(i);
            teams[i].setOnAction((var e)->{
               new TeamBearbeitung(steuerung,stage,team,akt); 
            });
        }
       
        box.getChildren().addAll(Arrays.asList(teams));
        gp.add(sp,1,0,1,2);
        
        Button neu=new Button("Neues Team");
        neu.setPrefSize(100,100);
        gp.add(neu,2,0);
        neu.setOnAction((e)->{
            new TeamHinzufuegen(stage,akt,steuerung);
        });
        
        VBox boxl=new VBox();
        boxl.setPrefSize(400,50);
        gp.add(boxl, 0,1);
       
        Scene scene=new Scene(bp,1200,725);
        stage.setScene(scene);
        stage.show();
    }
    
    public void aktualisiere(){
         teams=new Button[steuerung.getTeams().size()];
        VBox box=new VBox();
        ScrollPane sp=new ScrollPane(box);
        sp.setPrefSize(400,650);
        teams=new Button[steuerung.getTeams().size()];
        for(int i=0;i<teams.length;i++){
            teams[i]=new Button(steuerung.getTeams().get(i).toString());
            teams[i].setPrefWidth(395);
            Team team=steuerung.getTeams().get(i);
            teams[i].setOnAction((var e)->{
               new TeamBearbeitung(steuerung,stage,team,akt); 
            });
        }
       
        box.getChildren().addAll(Arrays.asList(teams));
        gp.add(sp,1,0,1,2);
        stage.show();
    }
}
