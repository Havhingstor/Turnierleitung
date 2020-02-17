package de.pasch.turnierleitung.uis;

import javax.swing.JOptionPane;

import de.pasch.turnierleitung.steuerung.Steuerung;
import de.pasch.turnierleitung.turnierelemente.KORunde;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TEHinzufuegen {
	Steuerung steuerung;	
	Stage stage;
	Aktualisierer akt;
	
	public TEHinzufuegen(Steuerung steuerung,Stage primstage,Aktualisierer akt) {
		this.akt=akt;
		this.steuerung=steuerung;
		stage=new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(primstage);
		stage.setTitle("Turnierelement hinzufügen");
		
		GridPane gp=new GridPane();
		gp.setHgap(5);
		gp.setVgap(5);
		gp.setPadding(new Insets(5));
		
		ComboBox<String> artauswahl=new ComboBox<String>();
		artauswahl.getItems().addAll("Liga","KO-Runde");
		artauswahl.setValue("Liga");
		gp.add(artauswahl, 0, 0);
		artauswahl.setOnAction((e)->{
			if(artauswahl.getValue().equals("KO-Runde")) {
				gp.getChildren().removeIf((f)->(!f.equals(artauswahl)));
				createKORInh(gp);
			}else {
				gp.getChildren().removeIf((f)->(!f.equals(artauswahl)));
				createLigaInh(gp);
			}
		});
		
		createLigaInh(gp);
		
		Scene scene =new Scene(gp,320,200);
		stage.setScene(scene);
		stage.show();
	}
	
	public void createKORInh(GridPane gp) {
		Label nameLab=new Label("Name");
		TextField nameFeld=new TextField();
		gp.add(nameLab, 0, 1);
		gp.add(nameFeld, 1,1);
		
		Label eKLab=new Label("Erstes Krierium");
		gp.add(eKLab, 0, 2);
		ComboBox<String> eKBox=new ComboBox<String>();
		eKBox.getItems().addAll("Tore","Spiele");
		eKBox.setValue("Tore");
		gp.add(eKBox, 1,2);
		
		Label zKLab=new Label("Zweites Krierium");
		gp.add(zKLab, 0, 3);
		ComboBox<String> zKBox=new ComboBox<String>();
		zKBox.getItems().addAll("Auswärtstore","Elfmeterschießen");
		zKBox.setValue("Auswärtstore");
		gp.add(zKBox, 1,3);
		
		Label spielLab=new Label("Spielanzahl");
		gp.add(spielLab, 0, 4);
		
		NumberTextField spielFeld=new NumberTextField();
		spielFeld.setText("1");
		gp.add(spielFeld, 1,4);
		
		Button speichern=new Button("Speichern");
		gp.add(speichern, 0, 5);	
		speichern.setOnAction((e)->{
			try {
				int k1=KORunde.kriteriumEinsSpiele;
				int k2=KORunde.kriteriumZweiElfmeter;
				if(eKBox.getValue().equals("Tore")) {
					k1=KORunde.kriteriumEinsTore;
				}
				if(zKBox.getValue().equals("Auswärtstore")) {
					k2=KORunde.kriteriumZweiATore;
				}
				int nummer=0;
				try {
					nummer=Integer.parseInt(spielFeld.getText());
				}catch(NumberFormatException e1) {				}
				if(nummer>0) {
					steuerung.addKORunde(nameFeld.getText(),k1,k2,nummer);
					stage.hide();
					akt.aktualisieren();
				}else {
					JOptionPane.showMessageDialog(null,"Die Spielmenge darf nicht 0 sein!","FEHLER!",0);
				}
			}catch(IllegalArgumentException iae) {
				JOptionPane.showMessageDialog(null, iae.getMessage(), "FEHLER!", 0);
			}
		});
		
	}
	
	public void createLigaInh(GridPane gp) {
		Label nameLab=new Label("Name");
		TextField nameFeld=new TextField();
		gp.add(nameLab, 0, 1);
		gp.add(nameFeld, 1,1);
		Label ppsLab=new Label("Punkte pro Sieg");
		NumberTextField ppsFeld=new NumberTextField();
		ppsFeld.replaceSelection("3");
		gp.add(ppsLab, 0, 2);
		gp.add(ppsFeld,1,2);
		Label ppuLab=new Label("Punkte pro Unentschieden");
		NumberTextField ppuFeld=new NumberTextField();
		ppuFeld.replaceSelection("1");
		gp.add(ppuLab, 0,3);
		gp.add(ppuFeld,1,3);
		Label ppnLab=new Label("Punkte pro Niederlage");
		NumberTextField ppnFeld=new NumberTextField();
		ppnFeld.replaceSelection("0");
		gp.add(ppnLab, 0,4);
		gp.add(ppnFeld,1,4);
		
		Button speichern=new Button("Speichern");
		gp.add(speichern, 0, 5);	
		speichern.setOnAction((e)->{
			try {
				int[]rk= {1,2,3,4,5};
				steuerung.addLiga(Integer.parseInt(ppsFeld.getText()),Integer.parseInt(ppuFeld.getText()),Integer.parseInt(ppnFeld.getText()),
						nameFeld.getText(),rk);
				stage.hide();
				akt.aktualisieren();
			}catch(IllegalArgumentException iae) {
				JOptionPane.showMessageDialog(null, iae.getMessage(), "FEHLER!", 0);
			}
		});
	}
}
