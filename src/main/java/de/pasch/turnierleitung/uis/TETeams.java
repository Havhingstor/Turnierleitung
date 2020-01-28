package de.pasch.turnierleitung.uis;

import java.util.ArrayList;

import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.steuerung.Steuerung;
import de.pasch.turnierleitung.turnierelemente.Liga;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TETeams {
	Liga liga;
	Stage stage;
	Steuerung steuerung;
	Aktualisierer akt;
	
	public TETeams(Steuerung steuerung,Liga liga,Stage primStage,Aktualisierer akt) {
		this.akt=akt;
		this.liga=liga;
		stage=new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(primStage);
		this.steuerung=steuerung;
		stage.setTitle("Teams auswählen");
		
		GridPane gp=new GridPane();
		gp.setPadding(new Insets(5));
		gp.setVgap(5);
		gp.setHgap(5);
		
		VBox teamsBox=new VBox();
		ScrollPane teamsSP=new ScrollPane(teamsBox);
		
		ArrayList<Team> teamsListe=steuerung.getTeams();
		CheckBox[] teamsChecks=new CheckBox[teamsListe.size()];
		for(int i=0;i<teamsListe.size();i++) {
			teamsChecks[i]=new CheckBox();
			if(liga.getTeams().contains(teamsListe.get(i))) {
				teamsChecks[i].setSelected(true);
			}else {
				teamsChecks[i].setSelected(false);
			}
			HBox box=new HBox();
			Label name=new Label(teamsListe.get(i).getName());
			name.setFont(Font.font(15));
			box.getChildren().addAll(teamsChecks[i],name);
			teamsBox.getChildren().add(box);
		}
		
		Text ueberschrift=new Text("Teams auswählen");
		ueberschrift.setFont(Font.font(20));
		gp.add(ueberschrift, 0, 0);
		
		gp.add(teamsSP, 0, 1);
		
		Button speichern=new Button("Speichern");
		speichern.setFont(Font.font(20));
		gp.add(speichern, 0, 2);
		speichern.setOnAction((e)->{
			for(int i=0;i<teamsChecks.length;i++) {
				CheckBox cb=teamsChecks[i];
				if(cb.isSelected()) {
					if(!liga.getTeams().contains(teamsListe.get(i))) {
						liga.addTeam(teamsListe.get(i).getID());
					}
				}else {
					if(liga.getTeams().contains(teamsListe.get(i))) {
						liga.removeTeam(teamsListe.get(i).getID());
					}
				}
			}
			akt.aktualisieren();
			stage.hide();
		});
		
		Scene scene=new Scene(gp,500,500);
		stage.setScene(scene);
		stage.show();
	}
}
