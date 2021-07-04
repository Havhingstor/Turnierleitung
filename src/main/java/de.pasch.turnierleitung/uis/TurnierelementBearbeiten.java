package de.pasch.turnierleitung.uis;

import javax.swing.JOptionPane;

import de.pasch.turnierleitung.steuerung.Steuerung;
import de.pasch.turnierleitung.turnierelemente.Liga;
import de.pasch.turnierleitung.turnierelemente.Turnierelement;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TurnierelementBearbeiten {
	Stage stage;
	Aktualisierer akt;
	Steuerung steuerung;
	Turnierelement te;
	
	public TurnierelementBearbeiten(Aktualisierer akt,Steuerung steuerung,Stage primstage,Turnierelement te) {
		this.akt=akt;
		this.steuerung=steuerung;
		stage=new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(primstage);
		stage.setTitle("Turnierelement bearbeiten");
		
		GridPane gp=new GridPane();
		gp.setHgap(5);
		gp.setVgap(5);
		gp.setPadding(new Insets(5));
		
		if(te.isLiga()) {
			Liga liga=(Liga)te;
			Label nameLab=new Label("Name");
			TextField nameFeld=new TextField();
			nameFeld.setText(liga.getName());
			gp.add(nameLab, 0, 1);
			gp.add(nameFeld, 1,1);
			Label ppsLab=new Label("Punkte pro Sieg");
			NumberTextField ppsFeld=new NumberTextField();
			ppsFeld.setText(""+liga.getPunkteProSieg());
			gp.add(ppsLab, 0, 2);
			gp.add(ppsFeld,1,2);
			Label ppuLab=new Label("Punkte pro Unentschieden");
			NumberTextField ppuFeld=new NumberTextField();
			ppuFeld.setText(""+liga.getPunkteProUnentschieden());
			gp.add(ppuLab, 0,3);
			gp.add(ppuFeld,1,3);
			Label ppnLab=new Label("Punkte pro Niederlage");
			NumberTextField ppnFeld=new NumberTextField();
			ppnFeld.setText(""+liga.getPunkteProNiederlage());
			gp.add(ppnLab, 0,4);
			gp.add(ppnFeld,1,4);
			
			Button speichern=new Button("Speichern");
			gp.add(speichern, 0, 5);	
			speichern.setOnAction((e)->{
				try {
					int[]rk= {1,2,3,4,5};
					steuerung.editLiga(liga.getID(),nameFeld.getText(),Integer.parseInt(ppsFeld.getText()),Integer.parseInt(ppuFeld.getText()),Integer.parseInt(ppnFeld.getText()),
							rk);
					stage.hide();
					akt.aktualisieren();
				}catch(IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(null, iae.getMessage(), "FEHLER!", 0);
				}
			});
		}else {
			Label nameLab=new Label("Name");
			TextField nameFeld=new TextField();
			nameFeld.setText(te.getName());
			gp.add(nameLab, 0,0);
			gp.add(nameFeld, 1,0);
			
			Button speichern=new Button("Speichern");
			gp.add(speichern, 0,1);	
			speichern.setOnAction((e)->{
				try {
					steuerung.editKORunde(te.getID(),nameFeld.getText());
					stage.hide();
					akt.aktualisieren();
				}catch(IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(null, iae.getMessage(), "FEHLER!", 0);
				}
			});
		}
		
		Scene scene =new Scene(gp,320,200);
		stage.setScene(scene);
		stage.show();
	}
	
}
