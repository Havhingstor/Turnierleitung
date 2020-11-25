package de.pasch.turnierleitung.uis;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.spiele.Spiel;
import de.pasch.turnierleitung.steuerung.Steuerung;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SpielereignisHinzufuegen {
	Stage stage;
	Aktualisierer akt;
	Steuerung steuerung;
	Stage primstage;
	Spiel spiel;
	Spieler letzterAusfuehrer;
	Spieler letzterAndererSpieler;
	boolean pausiert=false;

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
		typCB.getItems().addAll("Tor", "Strafe", "Wechsel");
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
		zeitFeld.setText("" + 0);
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
		
		Button speichern = new Button("Speichern");
		speichern.setFont(Font.font(20));
		
		typCB.setOnAction((e) -> {
			if(!pausiert) {
				pausiert=true;
				if (typCB.getValue().equals("Wechsel")&&(teamCB.getValue().equals(spiel.getHeimteam())
						? spiel.getAufstHeim().getAllespieler(steuerung.getSpieler()).size()<2
						: spiel.getAufstAuswaerts().getAllespieler(steuerung.getSpieler()).size()<2)) {
					speichern.setDisable(true);
				}else {
					speichern.setDisable(false);
				}
				addArtauswahl(typCB, gp, artCB);
				addAnderenSpieler(typCB, gp, andererSpielerCB, andererSpielerLab, teamCB);
				pausiert=false;
			}
		});
		teamCB.setOnAction((e) -> {
			if(!pausiert) {
				pausiert=true;
				if (typCB.getValue().equals("Wechsel")&&(teamCB.getValue().equals(spiel.getHeimteam())
						? spiel.getAufstHeim().getAllespieler(steuerung.getSpieler()).size()<2
						: spiel.getAufstAuswaerts().getAllespieler(steuerung.getSpieler()).size()<2)) {
					speichern.setDisable(true);
				}else {
					speichern.setDisable(false);
				}
				addSpielerauswahl(teamCB, gp, spielerCB);
				addAnderenSpieler(typCB, gp, andererSpielerCB, andererSpielerLab, teamCB);
				pausiert=false;
			}
		});

		letzterAusfuehrer = spielerCB.getValue();
		letzterAndererSpieler = andererSpielerCB.getValue();
		spielerCB.setOnAction((e) -> {
			if(!pausiert) {
				pausiert=true;
				if(andererSpielerCB.getValue()!=null) {
					if (spielerCB.getValue().equals(andererSpielerCB.getValue())) {
						andererSpielerCB.setValue(letzterAusfuehrer);
					}
				}
				letzterAusfuehrer = spielerCB.getValue();
				pausiert=false;
			}
		});
		andererSpielerCB.setOnAction((e) -> {
			if(!pausiert) {
				pausiert=true;
				if (spielerCB.getValue().equals(andererSpielerCB.getValue())) {
					if (letzterAndererSpieler != null) {
						spielerCB.setValue(letzterAndererSpieler);
					} else {
						int zahl = 0;
						for (int i = spielerCB.getItems().size() - 1; i >= 0; --i) {
							if (!spielerCB.getItems().get(i).equals(andererSpielerCB.getValue())) {
								spielerCB.setValue(spielerCB.getItems().get(i));
								++zahl;
							}
						}
						if (zahl < 1) {
							andererSpielerCB.setValue(letzterAndererSpieler);
							return;
						}
					}
	
				}
				letzterAndererSpieler = andererSpielerCB.getValue();
				pausiert=false;
			}
		});

		
		speichern.setOnAction((e) -> {
			if (typCB.getValue().equals("Tor")) {
				try {
					steuerung.addTor(teamCB.getValue().equals(spiel.getHeimteam()), spielerCB.getValue().getID(),
							(andererSpielerCB.getValue() != null) ? andererSpielerCB.getValue().getID() : 0,
							spiel.getID(), Integer.parseInt(zeitFeld.getText()), Integer.parseInt(nZeitFeld.getText()),
							artCB.getValue());
					stage.hide();
					akt.aktualisieren();
				} catch (IllegalArgumentException iae) {
					Alert warnung = new Alert(AlertType.ERROR);
					warnung.initModality(Modality.WINDOW_MODAL);
					warnung.initOwner(stage);
					warnung.setTitle("Tor hinzufügen nicht möglich!");
					warnung.setHeaderText(null);
					warnung.setContentText(iae.getMessage());
					warnung.showAndWait();
				}
			} else if (typCB.getValue().equals("Wechsel")) {
				try {
					steuerung.addWechsel(teamCB.getValue().equals(spiel.getHeimteam()),
							Integer.parseInt(zeitFeld.getText()), Integer.parseInt(nZeitFeld.getText()),
							spielerCB.getValue(), andererSpielerCB.getValue(), spiel);
					stage.hide();
					akt.aktualisieren();
				} catch (IllegalArgumentException iae) {
					Alert warnung = new Alert(AlertType.ERROR);
					warnung.initModality(Modality.WINDOW_MODAL);
					warnung.initOwner(stage);
					warnung.setTitle("Wechsel hinzufügen nicht möglich!");
					warnung.setHeaderText(null);
					warnung.setContentText(iae.getMessage());
					warnung.showAndWait();
				}
			} else {
				try {
					steuerung.addStrafe(teamCB.getValue().equals(spiel.getHeimteam()), spielerCB.getValue().getID(),
							(andererSpielerCB.getValue() != null) ? andererSpielerCB.getValue().getID() : 0,
							spiel.getID(), Integer.parseInt(zeitFeld.getText()), Integer.parseInt(nZeitFeld.getText()),
							artCB.getValue());
					stage.hide();
					akt.aktualisieren();
				} catch (IllegalArgumentException iae) {
					Alert warnung = new Alert(AlertType.ERROR);
					warnung.initModality(Modality.WINDOW_MODAL);
					warnung.initOwner(stage);
					warnung.setTitle("Strafe hinzufügen nicht möglich!");
					warnung.setHeaderText(null);
					warnung.setContentText(iae.getMessage());
					warnung.showAndWait();
				}
			}
		});
		gp.add(speichern, 0, 5, 2, 1);

		Scene scene = new Scene(gp);
		stage.setScene(scene);
		stage.show();
	}

	private void addArtauswahl(ComboBox<String> typCB, GridPane gp, ComboBox<String> artCB) {
		if (typCB.getValue().equals("Tor")) {
			artCB.getItems().clear();
			artCB.getItems().addAll(FXCollections.observableArrayList(steuerung.getTorarten()));
		} else if (typCB.getValue().equals("Wechsel")) {
			artCB.getItems().clear();
			artCB.getItems().add("Eingewechselt");
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
		} else if (typCB.getValue().equals("Wechsel")) {
			andererSpielerLab.setText("Ausgewechselt");
			andererSpieler.getItems().clear();
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
		if(andererSpieler.getItems().size()>0) {
			andererSpieler.setValue(andererSpieler.getItems().get(0));
		}
	}
}
