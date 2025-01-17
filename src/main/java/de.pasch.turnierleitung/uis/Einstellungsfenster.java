/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pasch.turnierleitung.uis;

import java.util.Optional;

import de.pasch.turnierleitung.steuerung.Steuerung;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author pasch
 */
public class Einstellungsfenster {
	Stage stage;
	Steuerung steuerung;
	Aktualisierer akt;
	GridPane gp;
	Button tNeu, tLoe, sNeu, sLoe;

	public Einstellungsfenster(Stage primStage, Steuerung steuerung, Aktualisierer akt) {
		this.akt = akt;
		this.steuerung = steuerung;
		stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(primStage);
		gp = new GridPane();
		gp.setPadding(new Insets(5, 5, 5, 5));
		gp.setHgap(5);
		gp.setVgap(5);
		aktualisiere(steuerung);
		Scene scene = new Scene(gp, 300, 300);
		stage.setScene(scene);
		stage.show();
	}

	public void aktualisiere(Steuerung steuerung) {
		this.steuerung = steuerung;
		VBox tB1 = new VBox();
		VBox tB2 = new VBox();
		ScrollPane tSp = new ScrollPane(tB2);
		Label toreName = new Label("Torarten");
		tB1.getChildren().add(toreName);
		tB1.getChildren().add(tSp);
		steuerung.getTorarten().forEach((s) -> {
			tB2.getChildren().add(new Label(s));
		});
		gp.add(tB1, 0, 0, 1, 2);
		tSp.setPrefSize(100, 300);
		tNeu = new Button("Torart hinzufügen");
		tNeu.setPrefSize(150, 50);
		gp.add(tNeu, 1, 0);
		tLoe = new Button("Torart entfernen");
		tLoe.setPrefSize(150, 50);
		gp.add(tLoe, 1, 1);

		VBox sB1 = new VBox();
		VBox sB2 = new VBox();
		ScrollPane sSp = new ScrollPane(sB2);
		Label strafenName = new Label("Strafenarten");
		sB1.getChildren().add(strafenName);
		sB1.getChildren().add(sSp);
		steuerung.getStrafenarten().forEach((s) -> {
			sB2.getChildren().add(new Label(s));
		});
		gp.add(sB1, 0, 3, 1, 2);
		sSp.setPrefSize(100, 300);
		sNeu = new Button("Strafenart hinzufügen");
		sNeu.setPrefSize(150, 50);
		gp.add(sNeu, 1, 3);
		sLoe = new Button("Strafenart entfernen");
		sLoe.setPrefSize(150, 50);
		gp.add(sLoe, 1, 4);

		Button name = new Button("Turniernamen ändern");
		gp.add(name, 0, 5);
		name.setOnAction((e) -> {
			new NameAendern(steuerung, stage, akt);
		});

		tNeu.setOnAction((e) -> {

//        	TextInputDialog tip=new TextInputDialog();
//        	tip.initModality(Modality.WINDOW_MODAL);
//        	tip.initOwner(stage);
//        	tip.setHeaderText("Wie soll die Torart heißen?");
//        	tip.setContentText("Name der Torart");
			Optional<String> result = Hauptfenster.getTIP(null, "Wie soll die Torart heißen?", "Name der Torart", stage)
					.showAndWait();
			result.ifPresent((eingabe) -> {
				if (!eingabe.equals("")) {
					if (!steuerung.getTorarten().contains(eingabe)) {
						steuerung.getTorarten().add(eingabe);
						akt.aktualisieren();
					} else {
						Hauptfenster.getAlert(AlertType.ERROR, "Fehler!", null, "Diese Torart existiert schon!", stage).show();
					}
				} else {
					Hauptfenster.getAlert(AlertType.ERROR, "Fehler!", null, "Es wurde nicht eingegeben!", stage).show();
				}
			});
		});

		tLoe.setOnAction((e) -> {
			if (steuerung.getTorarten().size() > 0) {
				new ListDialog<String>(steuerung.getTorarten(), stage, "Welche Torart soll gelöscht werden?",
						"Torart löschen", (f) -> {
							steuerung.getTorarten().remove(f);
							akt.aktualisieren();
						});
			}
		});

		sNeu.setOnAction((e) -> {
			Hauptfenster.getTIP(null, "Wie soll die Torart heißen?", "Name der Torart", stage)
			.showAndWait().ifPresent((eingabe)->{
				if (!eingabe.equals("")) {
					if (!steuerung.getStrafenarten().contains(eingabe)) {
						steuerung.getStrafenarten().add(eingabe);
						akt.aktualisieren();
					} else {
						Hauptfenster.getAlert(AlertType.ERROR, "Fehler!", null, "Diese Strafenart existiert schon!", stage).show();
					}
				} else {
					Hauptfenster.getAlert(AlertType.ERROR, "Fehler!", null, "Es wurde nicht eingegeben!", stage).show();
				}
			});
		});

		sLoe.setOnAction((e) -> {
			if (steuerung.getStrafenarten().size() > 0) {
				new ListDialog<String>(steuerung.getStrafenarten(), stage, 300,
						"Welche Strafenart soll gelöscht werden?", "Strafenart löschen", (f) -> {
							steuerung.getStrafenarten().remove(f);
							akt.aktualisieren();
						});
			}
		});
	}
}
