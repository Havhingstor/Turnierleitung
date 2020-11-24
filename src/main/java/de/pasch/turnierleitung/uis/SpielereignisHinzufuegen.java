package de.pasch.turnierleitung.uis;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.spiele.Spiel;
import de.pasch.turnierleitung.steuerung.Steuerung;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SpielereignisHinzufuegen {
	Stage stage;
	Aktualisierer akt;
	Steuerung steuerung;
	Stage primstage;
	Spiel spiel;

	public SpielereignisHinzufuegen(Stage primstage, Aktualisierer akt, Steuerung steuerung, Spiel spiel) {
		this.primstage = primstage;
		this.akt = akt;
		this.steuerung = steuerung;
		this.spiel = spiel;

		stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(primstage);
		stage.setTitle("Spielereignis hinzufügen");

		GridPane gp = new GridPane();
		gp.setPadding(new Insets(5));
		gp.setHgap(5);
		gp.setVgap(5);
		gp.setAlignment(Pos.TOP_CENTER);

		ComboBox<String> typCB = new ComboBox<String>();
		typCB.getItems().addAll("Tor", "Strafe");
		typCB.setValue("Tor");
		gp.add(typCB, 0, 0);

		ComboBox<String> artCB = new ComboBox<String>();
		gp.add(artCB, 0, 1);
		addArtauswahl(typCB, gp, artCB);

		ComboBox<Team> teamCB = new ComboBox<Team>();
		teamCB.getItems().addAll(spiel.getHeimteam(), spiel.getAuswaertsteam());
		teamCB.setValue(spiel.getHeimteam());
		gp.add(teamCB, 1, 0);

		ComboBox<Spieler> spielerCB = new ComboBox<Spieler>();
		gp.add(spielerCB, 1, 1);
		addSpielerauswahl(teamCB, gp, spielerCB);

		Label zeitLbl = new Label("Zeit");
		gp.add(zeitLbl, 0, 2);

		NumberTextField zeitFeld = new NumberTextField();
		gp.add(zeitFeld, 1, 2);

		Label nZeitLbl = new Label("Nachspielzeit");
		gp.add(nZeitLbl, 0, 3);

		NumberTextField nZeitFeld = new NumberTextField();
		nZeitFeld.setText("" + 0);
		gp.add(nZeitFeld, 1, 3);

		ComboBox<Spieler> andererSpielerCB = new ComboBox<Spieler>();
		gp.add(andererSpielerCB, 1, 4);
		Label andererSpielerLab = new Label();
		gp.add(andererSpielerLab, 0, 4);
		addAnderenSpieler(typCB, gp, andererSpielerCB, andererSpielerLab, teamCB);
		typCB.setOnAction((e) -> {
			addArtauswahl(typCB, gp, artCB);
			addAnderenSpieler(typCB, gp, andererSpielerCB, andererSpielerLab, teamCB);
		});
		teamCB.setOnAction((e) -> {
			addSpielerauswahl(teamCB, gp, spielerCB);
			addAnderenSpieler(typCB, gp, andererSpielerCB, andererSpielerLab, teamCB);
		});

		Scene scene = new Scene(gp);
		stage.setScene(scene);
		stage.show();
	}

	private void addArtauswahl(ComboBox<String> typCB, GridPane gp, ComboBox<String> artCB) {
		if (typCB.getValue().equals("Tor")) {
			artCB.getItems().clear();
			artCB.getItems().addAll(FXCollections.observableArrayList(steuerung.getTorarten()));
		} else {
			artCB.getItems().clear();
			artCB.getItems().addAll(FXCollections.observableArrayList(steuerung.getStrafenarten()));
		}
		if (artCB.getItems().size() > 0) {
			artCB.setValue(artCB.getItems().get(0));
		}
	}

	private void addSpielerauswahl(ComboBox<Team> teamCB, GridPane gp, ComboBox<Spieler> spielerCB) {
		if (teamCB.getValue().equals(spiel.getHeimteam())) {
			spielerCB.getItems().clear();
			spielerCB.getItems().addAll(
					FXCollections.observableArrayList(spiel.getAufstHeim().getAllespieler(steuerung.getSpieler())));
		} else {
			spielerCB.getItems().clear();
			spielerCB.getItems().addAll(FXCollections
					.observableArrayList(spiel.getAufstAuswaerts().getAllespieler(steuerung.getSpieler())));
		}
		if (spielerCB.getItems().size() > 0) {
			spielerCB.setValue(spielerCB.getItems().get(0));
		}
	}

	private void addAnderenSpieler(ComboBox<String> typCB, GridPane gp, ComboBox<Spieler> andererSpieler,
			Label andererSpielerLab, ComboBox<Team> teamCB) {
		boolean heimteam = teamCB.getValue().equals(spiel.getHeimteam());
		if (typCB.getValue().equals("Tor")) {
			andererSpielerLab.setText("Vorbereiter");
			andererSpieler.getItems().clear();
			andererSpieler.getItems().add(null);
			if (heimteam) {
				andererSpieler.getItems().addAll(
						FXCollections.observableArrayList(spiel.getAufstHeim().getAllespieler(steuerung.getSpieler())));
			} else {
				andererSpieler.getItems().addAll(FXCollections
						.observableArrayList(spiel.getAufstAuswaerts().getAllespieler(steuerung.getSpieler())));
			}
		} else {
			andererSpielerLab.setText("Gefoulter");
			andererSpieler.getItems().clear();
			andererSpieler.getItems().add(null);
			if (!heimteam) {
				andererSpieler.getItems().addAll(
						FXCollections.observableArrayList(spiel.getAufstHeim().getAllespieler(steuerung.getSpieler())));
			} else {
				andererSpieler.getItems().addAll(FXCollections
						.observableArrayList(spiel.getAufstAuswaerts().getAllespieler(steuerung.getSpieler())));
			}
		}
		andererSpieler.setValue(andererSpieler.getItems().get(0));
	}
}
