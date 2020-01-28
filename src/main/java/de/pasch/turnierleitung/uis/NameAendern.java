package de.pasch.turnierleitung.uis;

import de.pasch.turnierleitung.steuerung.Steuerung;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NameAendern {
	Steuerung steuerung;
	Aktualisierer akt;
	
	public NameAendern(Steuerung steuerung,Stage primStage,Aktualisierer akt) {
		this.akt=akt;
		this.steuerung=steuerung;
		
		Stage stage=new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(primStage);
		
		GridPane gp=new GridPane();
		gp.setPadding(new Insets(5));
		gp.setHgap(5);
		gp.setVgap(5);
		
		TextField nameFeld=new TextField(steuerung.getName());
		nameFeld.setFont(Font.font(15));
		gp.add(nameFeld, 0, 0);
		
		Button speichern=new Button("Speichern");
		speichern.setFont(Font.font(15));
		gp.add(speichern, 0, 1);
		speichern.setOnAction((e)->{
			steuerung.setName(nameFeld.getText());
			akt.aktualisieren();
			stage.hide();
		});
		
		Scene scene=new Scene(gp,250,250);
		stage.setScene(scene);
		stage.show();
	}
}
