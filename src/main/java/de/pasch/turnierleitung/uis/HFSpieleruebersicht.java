/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pasch.turnierleitung.uis;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.steuerung.Steuerung;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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
    SpielerDetail spd=null;
    String letztesTeam="";
    
    public HFSpieleruebersicht(Stage stage,MenuBar mb,Steuerung steuerung,Aktualisierer akt){
        this.akt=akt;
        this.stage=stage;
        this.steuerung=steuerung;
        gp=new GridPane();
        gp.setAlignment(Pos.TOP_CENTER);
        BorderPane bp=new BorderPane();
        bp.setTop(mb);
        bp.setCenter(gp);
        gp.setPadding(new Insets(5,5,5,5));
        gp.setHgap(5);
        gp.setVgap(5);
        letztesTeam=steuerung.getTeams().get(0).getName();
        aktualisiere();
        Scene scene=new Scene(bp,1200,725);
        stage.setScene(scene);
        stage.show();
    }
    
    public void aktualisiere(){
    	boolean erlaubt=true;
    	if(steuerung.getTeams().size()==0) {
    		erlaubt=false;
    	}
        
    	if(erlaubt) {
	        VBox schaltungen=new VBox();
	        
	        ComboBox<String> team=new ComboBox<String>();
	        for(Team t:steuerung.getTeams()){
	          team.getItems().add(t.getName());  
	        }
	        team.setValue(letztesTeam);
	        schaltungen.getChildren().add(team);
	        
	        VBox gap=new VBox();
	        gap.setPrefHeight(20);
	        schaltungen.getChildren().add(gap);
	                 
	        VBox box=new VBox();
	        ScrollPane sp=new ScrollPane(box);
	        sp.setPrefSize(400,650);
            String s=(String)team.getValue();
            ArrayList<Spieler>spieler;
            Team t = null;
            for(Team team2:steuerung.getTeams()){
                if(team2.getName().equals(s)){
                    t=team2;
                }
            }
            spieler=steuerung.getAktiveSpielerEinesTeams(t.getID());
	        spielerGes=new Button[spieler.size()];
            for(int i=0;i<spielerGes.length;i++){
                spielerGes[i]=new Button(spieler.get(i).toString());
                spielerGes[i].setPrefWidth(395);
                Spieler spielerEinz=steuerung.getSpieler().get(i);
                spielerGes[i].setOnAction((var f)->{
                   spd=new SpielerDetail(steuerung,stage,spielerEinz,akt); 
                });
            }
	       
	        box.getChildren().addAll(Arrays.asList(spielerGes));
	        gp.add(sp,1,0,1,2);
	        
	        team.setOnAction((e)->{
	            String s2=(String)team.getValue();
	            letztesTeam=s2;
	            ArrayList<Spieler>spieler2;
	            Team t2 = null;
                for(Team team2:steuerung.getTeams()){
                    if(team2.getName().equals(s2)){
                        t2=team2;
                    }
                }
                spieler2=steuerung.getAktiveSpielerEinesTeams(t2.getID());
	            VBox box2=new VBox();
	            sp.setContent(box2);
	            spielerGes=new Button[spieler2.size()];
	            for(int i=0;i<spielerGes.length;i++){
	                spielerGes[i]=new Button(spieler2.get(i).toString());
	                spielerGes[i].setPrefWidth(395);
	                Spieler spielerEinz=steuerung.getSpieler().get(i);
	                spielerGes[i].setOnAction((var f)->{
	                   spd=new SpielerDetail(steuerung,stage,spielerEinz,akt); 
	                });
	            }
	            box2.getChildren().addAll(Arrays.asList(spielerGes));
	        });
	                      
	        Button neu=new Button("Neuer Spieler");
	        neu.setPrefSize(100,100);
	        schaltungen.getChildren().add(neu);
	        gp.add(schaltungen,2,0);
	        neu.setOnAction((e)->{
	            String s2=(String)team.getValue();
	            Team t2 = null;
	                for(Team team2:steuerung.getTeams()){
	                    if(team2.getName().equals(s2)){
	                        t2=team2;
	                    }
	                }
	                new SpielerHinzufuegen(stage,t2.getID(),akt,steuerung);
	            
	        });
	        
	        
	        VBox gap2=new VBox();
	        gap2.setPrefHeight(20);
	        schaltungen.getChildren().add(gap2);
	        
	        Button loe=new Button("Spieler löschen");
	        loe.setPrefSize(100,100);
	        schaltungen.getChildren().add(loe);
	        loe.setOnAction((e)->{
	            String s2=(String)team.getValue();
	            ArrayList<Spieler>als=new ArrayList<Spieler>();
	                Team t2 = null;
	                for(Team team2:steuerung.getTeams()){
	                    if(team2.getName().equals(s2)){
	                        t2=team2;
	                    }
	                }
	                als.addAll(steuerung.getSpielerEinesTeams(t2.getID()));
	            
	            if(als.size()>0) {
		            new ListDialog<Spieler>(als, stage,
		            		"Welchen Spieler möchten sie löschen?","Spieler löschen",(f)->{
		            	try{
		            		steuerung.removeSpieler(f.getID());
		            		akt.aktualisieren();
		            	}catch(IllegalArgumentException iae) {
		            		JOptionPane.showMessageDialog(null, iae.getMessage(),"FEHLER!",JOptionPane.ERROR_MESSAGE);
		            	}
		            });
	            }else {
	            	JOptionPane.showMessageDialog(null, "Kein Spieler vorhanden!","FEHLER!",JOptionPane.ERROR_MESSAGE);
	            }
	        });
	        
	        VBox boxl=new VBox();
	        boxl.setPrefSize(400,50);
	        gp.add(boxl, 0,1);
	        if(spd!=null) {
	        	spd.aktualisiere();
	        }
    	}else {
    		Text text1=new Text("Es ist kein Team vorhanden, deshalb sind bis jetzt auch keine Spieler möglich!");
    		Text text2=new Text("Sobald es ein Team gibt, können diesem auch Spieler hinzugefügt werden.");
    		text1.setFont(Font.font("Verdana",FontWeight.BOLD, 25));
    		text2.setFont(Font.font("Verdana",FontWeight.BOLD, 25));
    		gp.setAlignment(Pos.CENTER);
    		gp.add(text1,0,0);
    		gp.add(text2,0,1);
    	}
    }
}
