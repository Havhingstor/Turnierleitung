package de.pasch.turnierleitung.uis;

import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.spiele.Spiel;
import de.pasch.turnierleitung.steuerung.Steuerung;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AufstellungSetzenFenster {
	Stage stage;
	GridPane gp;
	Steuerung steuerung;
	Spiel spiel;
	boolean heim;
	Team team;
	Aktualisierer akt;
	
	public AufstellungSetzenFenster(Stage primStage,Steuerung steuerung,Spiel spiel,boolean heim,Aktualisierer akt) {
		this.steuerung=steuerung;
		this.spiel=spiel;
		this.heim=heim;
		this.akt=akt;
		
		if(heim) {
			team=spiel.getHeimteam();
		}else {
			team=spiel.getAuswaertsteam();
		}
		
		stage=new Stage();
		stage.setTitle("Aufstellung bearbeiten");
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(primStage);
		
		gp=new GridPane();
		
		Scene scene=new Scene(gp);
		stage.setScene(scene);
		stage.show();
		
	}
}
