package de.pasch.turnierleitung.uis;

import javax.swing.JOptionPane;

import de.pasch.turnierleitung.steuerung.Steuerung;
import de.pasch.turnierleitung.turnierelemente.KORunde;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RundensammlungHinzufuegen {
	Stage stage;
	Steuerung steuerung;
	Aktualisierer akt;
	
	public RundensammlungHinzufuegen(KORunde kor,Stage primStage,Steuerung steuerung,Aktualisierer akt) {
		this.steuerung=steuerung;
		this.akt=akt;
		stage=new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(primStage);
		stage.setTitle("Runde hinzufügen");
		
		GridPane gp=new GridPane();
		gp.setPadding(new Insets(5));
		gp.setHgap(5);
		gp.setVgap(5);
		
		Text ueberschrift=new Text("Runde hinzufügen");
		ueberschrift.setFont(Font.font(20));
		gp.add(ueberschrift, 0, 0);
		
		Label nameLab=new Label("Name");
		nameLab.setFont(Font.font(15));
		gp.add(nameLab, 0, 1);
		
		TextField nameFeld=new TextField();
		nameFeld.setFont(Font.font(15));
		gp.add(nameFeld, 1, 1);
		
		Button speichern=new Button("Speichern");
		speichern.setFont(Font.font(15));
		gp.add(speichern, 0, 2);
		speichern.setOnAction((e)->{
			try {
				steuerung.addRundensammlung(nameFeld.getText(),kor.getID());
				akt.aktualisieren();
				stage.hide();
			}catch(IllegalArgumentException iae) {
				JOptionPane.showMessageDialog(null, iae.getMessage(),"FEHLER!",0);
			}
		});
		
		Scene scene=new Scene(gp,400,170);
		stage.setScene(scene);
		stage.show();
	}
}
