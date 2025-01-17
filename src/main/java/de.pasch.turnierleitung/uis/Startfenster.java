/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pasch.turnierleitung.uis;

import java.io.File;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author pasch
 */
public class Startfenster extends Application {
    private boolean dateiLadenBool=false;
    private boolean beispielBool=false;
    private File dateiLadenFile=null;
    private String turniername=null;
    Hauptfenster hf=null;
    
    public void start(Stage primaryStage){    
        GridPane grid=new GridPane();
        grid.setPadding(new Insets(5,5,5,5));
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        Text text=new Text("Wie wollen Sie die Turnierleitungssoftware starten?");
        text.setFont(Font.font("Verdana", FontWeight.THIN, 15));
        grid.add(text, 0, 0,3,1);
        Button neu=new Button("Neues Turnier erstellen");
        neu.setFont(Font.font("Verdana",FontWeight.THIN,12));
        Button laden=new Button("Turnier laden"); 
        laden.setFont(Font.font("Verdana",FontWeight.THIN,12));
        Button beispiel=new Button("Beispielturnier laden"); 
        beispiel.setFont(Font.font("Verdana",FontWeight.THIN,12));
        grid.add(neu,0,1);
        grid.add(laden,1,1);
        grid.add(beispiel, 2, 1);
        Scene scene = new Scene(grid,500,100);
        primaryStage.setScene(scene);
        primaryStage.show();
        neu.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                String eingabe=JOptionPane.showInputDialog("Welchen Namen soll es haben?");
                if(eingabe!=null) {
	                if(eingabe.equals("")){
	                	eingabe="Neues Turnier";
	                }
	                turniername=eingabe;
	                primaryStage.hide();
	                newHauptfenster();
                }
            }
        });
        laden.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                FileChooser fc=new FileChooser();
                fc.getExtensionFilters().addAll(
            		new FileChooser.ExtensionFilter("Turnierleitungsdatei", "*.tul"),
            		new FileChooser.ExtensionFilter("XML-Datei", "*.xml")
                );
                dateiLadenFile=fc.showOpenDialog(primaryStage);
                if(dateiLadenFile!=null) {
                	dateiLadenBool=true;
                    primaryStage.hide();
                    newHauptfenster();
                }
            }
        });
        beispiel.setOnAction((e)->{
        	beispielBool=true;
        	primaryStage.hide();
        	newHauptfenster();
        });
    }
    
    private void newHauptfenster(){
        hf=new Hauptfenster(this);
    }
    
    public boolean getBeispielBool() {
		return beispielBool;
	}

	public boolean getDateiLadenBool(){
        return dateiLadenBool;
    }
    
    public File getDateiLadenFile(){
        return dateiLadenFile;
    }
    
    public static void main(String[]args){
        launch();
    }
    
    public String getTurniername() {
    	return turniername;
    }
}
