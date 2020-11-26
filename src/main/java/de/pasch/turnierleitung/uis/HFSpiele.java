package de.pasch.turnierleitung.uis;

import java.util.ArrayList;
import java.util.Optional;

import javax.swing.JOptionPane;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.spiele.Aufstellung;
import de.pasch.turnierleitung.spiele.Spiel;
import de.pasch.turnierleitung.spiele.Spielaktivitaet;
import de.pasch.turnierleitung.spiele.Strafe;
import de.pasch.turnierleitung.spiele.Tor;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Steuerung;
import de.pasch.turnierleitung.turnierelemente.Liga;
import de.pasch.turnierleitung.turnierelemente.Spieltag;
import de.pasch.turnierleitung.turnierelemente.Turnierelement;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HFSpiele {
	Stage stage;
	BorderPane bp;
	Steuerung steuerung;
	Aktualisierer akt;
	GridPane gp;
	Turnierelement letztesTE;
	Spieltag letzterSpt;
	Spiel letztesSpiel;
	Spielaktivitaet letztesEreignis;
	HFProtagonisten hfp;
	HFTurnierelemente hft;
	HFSpieltag hfst;

	public HFSpiele(Stage stage, BorderPane bp, Steuerung steuerung, Aktualisierer akt) {
		this.stage = stage;
		this.bp = bp;
		this.steuerung = steuerung;
		this.akt = akt;

		gp = new GridPane();
		gp.setPadding(new Insets(5));
		gp.setVgap(5);
		gp.setHgap(5);
		gp.setAlignment(Pos.TOP_LEFT);

		try {
			bp.getChildren().remove(1);
		} catch (IndexOutOfBoundsException ioobe) {
		}
		bp.setCenter(gp);

		aktualisiere(steuerung);
	}

	public HFSpiele(Liga liga, Spieltag spt, Spiel sp, Stage stage, BorderPane bp, Steuerung steuerung,
			Aktualisierer akt) {
		this.stage = stage;
		this.bp = bp;
		this.steuerung = steuerung;
		this.akt = akt;

		letztesTE = liga;
		letzterSpt = spt;
		letztesSpiel = sp;

		gp = new GridPane();
		gp.setPadding(new Insets(5));
		gp.setVgap(5);
		gp.setHgap(5);
		gp.setAlignment(Pos.TOP_LEFT);

		try {
			bp.getChildren().remove(1);
		} catch (IndexOutOfBoundsException ioobe) {
		}
		bp.setCenter(gp);

		aktualisiere(steuerung);
	}

	public void aktualisiere(Steuerung steuerung) {
		this.steuerung = steuerung;
		if (hfp != null) {
			hfp.aktualisiere(steuerung);
		}
		if (hft != null) {
			hft.aktualisiere(steuerung);
		}
		if (hfst != null) {
			hfst.aktualisiere(steuerung);
		}

		gp.getChildren().clear();

		if (steuerung.getTurnierelemente().size() > 0) {
			createElUSptSelector();
		} else {
			Text fehlerText = new Text("Es sind keine Turnierelemente vorhanden! Bitte fügen " + "Sie eines hinzu.");
			fehlerText.setFont(Font.font(20));
			gp.add(fehlerText, 0, 0);
		}

		stage.show();
	}

	private void createElUSptSelector() {
		HBox turnierBox = new HBox();
		gp.add(turnierBox, 0, 0);
		ComboBox<Turnierelement> teAuswahl = new ComboBox<Turnierelement>();
		turnierBox.getChildren().add(teAuswahl);
		Button zuTurnierelement = new Button("<-Tabelle zeigen");
		zuTurnierelement.setOnAction((e) -> {
			hft = new HFTurnierelemente(teAuswahl.getValue(), stage, bp, steuerung, akt);
		});
		turnierBox.getChildren().add(zuTurnierelement);
		for (Turnierelement te : steuerung.getTurnierelemente()) {
			teAuswahl.getItems().add(te);
		}
		if (steuerung.getTurnierelemente().contains(letztesTE)) {
			teAuswahl.getSelectionModel().select(letztesTE);
		} else {
			teAuswahl.getSelectionModel().select(steuerung.getTurnierelemente().get(0));
		}
		teAuswahl.setOnAction((e) -> {
			letztesTE = teAuswahl.getSelectionModel().getSelectedItem();
			aktualisiere(steuerung);
		});

		Turnierelement aktuell = teAuswahl.getValue();
		if (aktuell.isLiga()) {
			Liga aktLiga = (Liga) aktuell;
			HBox sptBox = new HBox();
			gp.add(sptBox, 1, 0);
			ComboBox<Spieltag> sptAuswahl = new ComboBox<Spieltag>();
			Button zuSpt = new Button("<-Details zeigen");
			zuSpt.setOnAction((e) -> {
				hfst = new HFSpieltag(aktLiga, sptAuswahl.getValue(), null, stage, bp, steuerung, akt);
			});
			sptBox.getChildren().addAll(sptAuswahl, zuSpt);
			for (Spieltag spt : aktLiga.getSpieltage()) {
				sptAuswahl.getItems().add(spt);
			}
			if (aktLiga.getSpieltage().contains(letzterSpt)) {
				sptAuswahl.getSelectionModel().select(letzterSpt);
				createSpiellisteSpt(letzterSpt);
			} else if (aktLiga.getSpieltage().size() > 0) {
				Spieltag neuerSpt = aktLiga.getSpieltage().get(0);
				for (int i = 1; i < aktLiga.getSpieltage().size() && neuerSpt.istAbgeschlossen(); ++i) {
					neuerSpt = aktLiga.getSpieltage().get(i);
				}
				sptAuswahl.setValue(neuerSpt);
				letzterSpt = neuerSpt;
				createSpiellisteSpt(sptAuswahl.getValue());
			} else {
				createSpiellisteSpt(null);
			}

			sptAuswahl.setOnAction((e) -> {
				letzterSpt = sptAuswahl.getSelectionModel().getSelectedItem();
				aktualisiere(steuerung);
			});
		}
	}

	/**
	 * @param selected der ausgewählte spieltag, null ist erlaubt
	 */
	private void createSpiellisteSpt(Spieltag selected) {
		VBox spieleBox = new VBox();
		spieleBox.setPrefSize(200, 500);
		ScrollPane spieleScroll = new ScrollPane(spieleBox);
		gp.add(spieleScroll, 0, 1);
		if (selected != null) {
			for (Spiel sp : selected.getSpiele()) {
				Button btn = new Button(sp.toString());
				btn.setPrefWidth(190);
				spieleBox.getChildren().add(btn);
				btn.setOnAction((e) -> {
					letztesSpiel = sp;
					aktualisiere(steuerung);
				});
			}
			if (selected.getSpiele().contains(letztesSpiel)) {
				createSpielAnsicht(letztesSpiel);
			} else if (selected.getSpiele().size() > 0) {
				createSpielAnsicht(selected.getSpiele().get(0));
			} else {
				createSpielAnsicht(null);
			}

		}
	}

	/**
	 * @param sp das ausgewählte spiel, null ist erlaubt
	 */
	private void createSpielAnsicht(Spiel sp) {
		GridPane spielInfos = new GridPane();
		spielInfos.setPadding(new Insets(10));
		spielInfos.setHgap(10);
		spielInfos.setVgap(10);
		gp.add(spielInfos, 1, 1);
		String[] beschr = createBeschrSpiel(sp);

		int verschiebung = 4;

		Label heimLabel = new Label("Heimteam");
		heimLabel.setFont(Font.font(20));
		spielInfos.add(heimLabel, 0, 0 + verschiebung);

		Text heimText = new Text(beschr[0]);
		heimText.setFont(Font.font(20));
		if (sp != null) {
			heimText.setOnMouseClicked((e) -> {
				hfp = new HFProtagonisten(sp.getHeimteam(), stage, bp, steuerung, akt);
			});
		}
		spielInfos.add(heimText, 1, 0 + verschiebung);

		Label auswLabel = new Label("Auswärtsteam");
		auswLabel.setFont(Font.font(20));
		spielInfos.add(auswLabel, 0, 1 + verschiebung);

		Text auswText = new Text(beschr[1]);
		auswText.setFont(Font.font(20));
		if (sp != null) {
			auswText.setOnMouseClicked((e) -> {
				hfp = new HFProtagonisten(sp.getAuswaertsteam(), stage, bp, steuerung, akt);
			});
		}
		spielInfos.add(auswText, 1, 1 + verschiebung);

		Label stadionLabel = new Label("Stadionname");
		stadionLabel.setFont(Font.font(20));
		spielInfos.add(stadionLabel, 0, 2 + verschiebung);

		Text stadionText = new Text(beschr[2]);
		stadionText.setFont(Font.font(20));
		spielInfos.add(stadionText, 1, 2 + verschiebung);

		Label neutrLabel = new Label("Neutraler Platz");
		neutrLabel.setFont(Font.font(20));
		spielInfos.add(neutrLabel, 0, 3 + verschiebung);

		Text neutrText = new Text(beschr[3]);
		neutrText.setFont(Font.font(20));
		spielInfos.add(neutrText, 1, 3 + verschiebung);

		createMinimaltorEingabe(sp, spielInfos);
		createSpielereignisFeld(gp, sp);
		if (sp != null) {
			createAufstellungen(sp, spielInfos, verschiebung);
		}
	}

	private void createMinimaltorEingabe(Spiel sp, GridPane spielInfo) {
		Label teamnamenHeim = new Label("");
		teamnamenHeim.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		teamnamenHeim.setAlignment(Pos.CENTER);
		if (sp != null) {
			teamnamenHeim.setOnMouseClicked((e) -> {
				hfp = new HFProtagonisten(sp.getHeimteam(), stage, bp, steuerung, akt);
			});
		}
		spielInfo.add(teamnamenHeim, 0, 0);

		Label teamnamenAuswaerts = new Label("");
		teamnamenAuswaerts.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		teamnamenAuswaerts.setAlignment(Pos.CENTER);
		if (sp != null) {
			teamnamenAuswaerts.setOnMouseClicked((e) -> {
				hfp = new HFProtagonisten(sp.getAuswaertsteam(), stage, bp, steuerung, akt);
			});
		}
		spielInfo.add(teamnamenAuswaerts, 1, 0);

		Label heimtore = new Label();
		heimtore.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		heimtore.setAlignment(Pos.CENTER);
		spielInfo.add(heimtore, 0, 1);

		Label auswaertstore = new Label();
		auswaertstore.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		auswaertstore.setAlignment(Pos.CENTER);
		spielInfo.add(auswaertstore, 1, 1);

		VBox schalterHeim = new VBox();
		spielInfo.add(schalterHeim, 0, 2);
		VBox schalterAuswaerts = new VBox();
		spielInfo.add(schalterAuswaerts, 1, 2);

		int prefWidth = 100;

		Button plusHeim = new Button("+");
		plusHeim.setFont(Font.font("Verdana"));
		plusHeim.setPrefWidth(prefWidth);
		plusHeim.setDisable(true);
		plusHeim.setOnAction((e) -> {
			sp.setMinimaleHeimtore(sp.getHeimtoreZahl() + 1);
			akt.aktualisieren();
		});
		schalterHeim.getChildren().add(plusHeim);

		Button eingHeim = new Button("Eingeben");
		eingHeim.setFont(Font.font("Verdana"));
		eingHeim.setPrefWidth(prefWidth);
		eingHeim.setDisable(true);
		eingHeim.setOnAction((e) -> {
			MaximalTorEingabe.getMTA(stage, (t) -> {
				if (t >= sp.getMengeHeimtoreDirekt()) {
					sp.setMinimaleHeimtore(t);
					akt.aktualisieren();
				} else {
					JOptionPane.showMessageDialog(null,
							"Es sind zu viele Tore direkt eingetragen worden," + " bitte diese entfernen!", "FEHLER!",
							JOptionPane.ERROR_MESSAGE);
				}
			});
		});
		schalterHeim.getChildren().add(eingHeim);

		Button minusHeim = new Button("-");
		minusHeim.setFont(Font.font("Verdana"));
		minusHeim.setPrefWidth(prefWidth);
		minusHeim.setDisable(true);
		minusHeim.setOnAction((e) -> {
			if (sp.getHeimtoreZahl() > sp.getMengeHeimtoreDirekt()) {
				sp.setMinimaleHeimtore(sp.getHeimtoreZahl() - 1);
				akt.aktualisieren();
			} else {
				JOptionPane.showMessageDialog(null,
						"Es sind zu viele Tore direkt eingetragen worden," + " bitte diese entfernen!", "FEHLER!",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		schalterHeim.getChildren().add(minusHeim);

		Button plusAuswaerts = new Button("+");
		plusAuswaerts.setFont(Font.font("Verdana"));
		plusAuswaerts.setPrefWidth(prefWidth);
		plusAuswaerts.setDisable(true);
		plusAuswaerts.setOnAction((e) -> {
			sp.setMinimaleAuswaertstore(sp.getAuswaertstoreZahl() + 1);
			akt.aktualisieren();
		});
		schalterAuswaerts.getChildren().add(plusAuswaerts);

		Button eingAuswaerts = new Button("Eingeben");
		eingAuswaerts.setFont(Font.font("Verdana"));
		eingAuswaerts.setPrefWidth(prefWidth);
		eingAuswaerts.setDisable(true);
		eingAuswaerts.setOnAction((e) -> {
			MaximalTorEingabe.getMTA(stage, (t) -> {
				if (t >= sp.getMengeHeimtoreDirekt()) {
					sp.setMinimaleHeimtore(t);
					akt.aktualisieren();
				} else {
					JOptionPane.showMessageDialog(null,
							"Es sind zu viele Tore direkt eingetragen worden," + " bitte diese entfernen!", "FEHLER!",
							JOptionPane.ERROR_MESSAGE);
				}
			});
		});
		schalterAuswaerts.getChildren().add(eingAuswaerts);

		Button minusAuswaerts = new Button("-");
		minusAuswaerts.setFont(Font.font("Verdana"));
		minusAuswaerts.setPrefWidth(prefWidth);
		minusAuswaerts.setDisable(true);
		minusAuswaerts.setOnAction((e) -> {
			if (sp.getAuswaertstoreZahl() > sp.getMengeAuswaertstoreDirekt()) {
				sp.setMinimaleAuswaertstore(sp.getAuswaertstoreZahl() - 1);
				akt.aktualisieren();
			} else {
				JOptionPane.showMessageDialog(null,
						"Es sind zu viele Tore direkt eingetragen worden," + " bitte diese entfernen!", "FEHLER!",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		schalterAuswaerts.getChildren().add(minusAuswaerts);

		Button aktivierer = new Button("als 0:0 zählen");
		aktivierer.setFont(Font.font("Verdana"));
		aktivierer.setPrefWidth(prefWidth * 2 + 40);
		aktivierer.setDisable(true);
		spielInfo.add(aktivierer, 0, 3, 2, 1);

		if (sp != null) {
			teamnamenHeim.setText(sp.getHeimteam().getMoeglichKN());
			teamnamenAuswaerts.setText(sp.getAuswaertsteam().getMoeglichKN());
			if (sp.isErgebnis()) {
				heimtore.setText("" + sp.getHeimtoreZahl());
				auswaertstore.setText("" + sp.getAuswaertstoreZahl());
				aktivierer.setText("Ergebnis löschen");
				aktivierer.setOnAction((e) -> {
					boolean erlaubt = true;
					Spieler fS = null;
					for (Spieler spieler : sp.getAufstHeim().getAllespieler(steuerung.getSpieler())) {
						if (!steuerung.getAktivesTeamEinesSpielers(spieler.getID()).equals(sp.getHeimteam())) {
							erlaubt = false;
							fS = spieler;
							break;
						}
					}
					if (erlaubt) {
						int best = JOptionPane.showConfirmDialog(null,
								"Sollen wirklich alle Tore "
										+ "(samt der vollständig eingegebenen) und Strafen gelöscht werden?",
								"ACHTUNG!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
						if (best == JOptionPane.OK_OPTION) {
							ArrayList<Tor> tore = new ArrayList<Tor>(sp.getHeimtoreDirekt());
							tore.addAll(sp.getAuswaertstoreDirekt());
							for (Tor t : tore) {
								steuerung.removeTor(t.getID());
							}
							ArrayList<Strafe> strafen = new ArrayList<Strafe>(sp.getHeimstrafen());
							strafen.addAll(sp.getAuswaertsstrafen());
							for (Strafe s : strafen) {
								steuerung.removeStrafe(s.getID());
							}
							sp.setMinimaleAuswaertstore(0);
							sp.setMinimaleHeimtore(0);
							sp.setErgebnis(false);
							akt.aktualisieren();
						}
					} else {
						ButtonType sA = new ButtonType("Spieler anzeigen");
						Alert al = new Alert(AlertType.ERROR);
						al.getButtonTypes().add(sA);
						al.setTitle("Fehler");
						al.setHeaderText(null);
						al.setContentText(fS.getName()
								+ " spielt nicht mehr bei seinem Verein. Deshalb kann das Ergebnis nicht mehr entfernt werden!");
						Optional<ButtonType> btn = al.showAndWait();
						if (btn.get().equals(sA)) {
							hfp = new HFProtagonisten(fS, stage, bp, steuerung, akt);
						}
					}
				});
			} else {
				heimtore.setText("-");
				auswaertstore.setText("-");
				aktivierer.setOnAction((e) -> {
					sp.setErgebnis(true);
					akt.aktualisieren();
				});
			}

			aktivierer.setDisable(false);
			plusHeim.setDisable(false);
			eingHeim.setDisable(false);
			minusHeim.setDisable(false);

			plusAuswaerts.setDisable(false);
			eingAuswaerts.setDisable(false);
			minusAuswaerts.setDisable(false);
		}
	}

	/**
	 * @param spiel kann null sein
	 */
	private String[] createBeschrSpiel(Spiel spiel) {
		String[] beschr = new String[4];

		if (spiel != null) {
			beschr[0] = spiel.getHeimteam().getName();
			beschr[1] = spiel.getAuswaertsteam().getName();
			beschr[2] = spiel.getStadion();
			if (spiel.getNeutralerPlatz()) {
				beschr[3] = "Ja";
			} else {
				beschr[3] = "Nein";
			}

		} else {
			for (int i = 0; i < beschr.length; i++) {
				beschr[i] = "";
			}
		}

		return beschr;
	}

	private void createAufstellungen(Spiel spiel, GridPane spielInfo, int verschiebung) {
		TabPane aufstellungen = new TabPane();
		aufstellungen.setPadding(new Insets(5));
		spielInfo.add(aufstellungen, 0, 4 + verschiebung, 2, 1);

		Tab heim = new Tab("Aufstellung-Heimteam");
		heim.setClosable(false);
		aufstellungen.getTabs().add(heim);

		Tab auswaerts = new Tab("Aufstellung-Auswärtsteam");
		auswaerts.setClosable(false);
		aufstellungen.getTabs().add(auswaerts);
		aufstellungenTabs(heim, spiel, true);
		aufstellungenTabs(auswaerts, spiel, false);
	}

	private void aufstellungenTabs(Tab tab, Spiel spiel, boolean heim) {
		Aufstellung aufst;
		if (heim) {
			aufst = spiel.getAufstHeim();
		} else {
			aufst = spiel.getAufstAuswaerts();
		}

		Team team;
		if (heim) {
			team = spiel.getHeimteam();
		} else {
			team = spiel.getAuswaertsteam();
		}

		GridPane aufstellung = new GridPane();
		aufstellung.setPadding(new Insets(5));
		aufstellung.setVgap(5);
		aufstellung.setHgap(5);

		GridPane start = new GridPane();
		start.setPadding(new Insets(5));
		start.setVgap(5);
		start.setHgap(5);

		ScrollPane spStart = new ScrollPane(start);
		spStart.setPrefWidth(160);

		aufstellung.add(spStart, 0, 1);

		Label startelfLabel = new Label("Startelf");
		aufstellung.add(startelfLabel, 0, 0);

		int zaehler = 1;
		// for(int i=0;i<6;++i)
		for (Spieler s : aufst.getStartelf(steuerung.getSpieler())) {
			Text trikotnummer = new Text(steuerung.getTrikotnummerEinesSpielersString(s.getID()));
			trikotnummer.setFont(Font.font("Verdana"));
			start.add(trikotnummer, 0, zaehler);

			Text name = new Text(s.getName());
			name.setFont(Font.font("Verdana"));
			start.add(name, 1, zaehler);

			if (aufst.getKapitaenID() == s.getID()) {
				Text kapt = new Text("C");
				name.setFont(Font.font("Verdana"));
				start.add(kapt, 2, zaehler);
			}
			++zaehler;
		}

		GridPane bank = new GridPane();
		bank.setPadding(new Insets(5));
		bank.setVgap(5);
		bank.setHgap(5);

		ScrollPane spBank = new ScrollPane(bank);
		spBank.setPrefWidth(160);

		aufstellung.add(spBank, 1, 1);

		Label bankLabel = new Label("Auswechselbank");
		aufstellung.add(bankLabel, 1, 0);

		zaehler = 1;
		for (Spieler s : aufst.getAuswechselspieler(steuerung.getSpieler())) {
			Text trikotnummer = new Text(steuerung.getTrikotnummerEinesSpielersString(s.getID()));
			trikotnummer.setFont(Font.font("Verdana"));
			bank.add(trikotnummer, 0, zaehler);

			Text name = new Text(s.getName());
			name.setFont(Font.font("Verdana"));
			bank.add(name, 1, zaehler);
			++zaehler;
		}

		VBox btns = new VBox();
		btns.setSpacing(5);
		aufstellung.add(btns, 2, 1);

		Button setzen = new Button("Aufstellung eingeben");
		setzen.setFont(Font.font("Verdana"));
		setzen.setOnAction((e) -> {
			if (!spiel.isErgebnis()) {
				Stage aufstStage = new Stage();
				aufstStage.setTitle("Aufstellung bearbeiten");
				aufstStage.initModality(Modality.WINDOW_MODAL);
				aufstStage.initOwner(stage);

				GridPane aufstGp = new GridPane();
				aufstGp.setPadding(new Insets(5));
				aufstGp.setVgap(5);
				aufstGp.setHgap(5);

				Label startBearb = new Label("Startaufstellung");
				aufstGp.add(startBearb, 0, 0);

				ZwLong[] zwl = new ZwLong[aufst.getHoechstAuswechselspieler() + aufst.getHoechstStartelf()];

				ArrayList<ComboBox<Spieler>> cBoxen = new ArrayList<ComboBox<Spieler>>();
				for (int i = 0; i < aufst.getHoechstStartelf(); ++i) {
					ComboBox<Spieler> box = new ComboBox<Spieler>();
					box.getItems().add(null);
					box.getItems().addAll(steuerung.getAktiveSpielerEinesTeams(team.getID()));
					cBoxen.add(box);
					if (aufst.getStartelfNummer() > i) {
						box.setValue(aufst.getStartelf(steuerung.getSpieler()).get(i));
					}
					if (box.getValue() != null) {
						zwl[i] = new ZwLong(box.getValue().getID());
					} else {
						zwl[i] = new ZwLong(0l);
					}
					final int iE = i;
					box.setOnAction((f) -> {
						if (box.getValue() != null) {
							for (int h = 0; h < cBoxen.size(); ++h) {
								ComboBox<Spieler> cB = cBoxen.get(h);
								if (cB.getValue() != null && !box.equals(cB) && box.getValue().equals(cB.getValue())) {
									cB.setValue(IDPicker.pick(steuerung.getSpieler(), zwl[iE].getWert()));
									if(cB.getValue()!=null) {
										zwl[h].setWert(cB.getValue().getID());
									}else {
										zwl[h].setWert(0l);
									}
								}
							}
						}
						if (box.getValue() != null) {
							zwl[iE].setWert(box.getValue().getID());
						}else {
							zwl[iE].setWert(0l);
						}
					});
					aufstGp.add(box, 0, i + 1);
				}

				Label bankBearb = new Label("Auswechselbank");
				aufstGp.add(bankBearb, 1, 0);

				for (int i = 0; i < aufst.getHoechstAuswechselspieler(); ++i) {
					ComboBox<Spieler> box = new ComboBox<Spieler>();
					box.getItems().add(null);
					box.getItems().addAll(steuerung.getAktiveSpielerEinesTeams(team.getID()));
					cBoxen.add(box);
					if (aufst.getAuswechselspielerNummer() > i) {
						box.setValue(aufst.getAuswechselspieler(steuerung.getSpieler()).get(i));
					}
					if (box.getValue() != null) {
						zwl[i + aufst.getHoechstStartelf()] = new ZwLong(box.getValue().getID());
					} else {
						zwl[i + aufst.getHoechstStartelf()] = new ZwLong(0l);
					}
					final int iE = i;
					box.setOnAction((f) -> {
						if (box.getValue() != null) {
							for (int h = 0; h < cBoxen.size(); ++h) {
								ComboBox<Spieler> cB = cBoxen.get(h);
								if (cB.getValue() != null && !box.equals(cB) && box.getValue().equals(cB.getValue())) {
									cB.setValue(IDPicker.pick(steuerung.getSpieler(),
											zwl[iE + aufst.getHoechstStartelf()].getWert()));
									if(cB.getValue()!=null) {
										zwl[h].setWert(cB.getValue().getID());
									}else {
										zwl[h].setWert(0l);
									}
								}
							}
						}
						if (box.getValue() != null) {
							zwl[iE + aufst.getHoechstStartelf()].setWert(box.getValue().getID());
						}else {
							zwl[iE + aufst.getHoechstStartelf()].setWert(0l);
						}
					});
					aufstGp.add(box, 1, i + 1);
				}

				Button ok = new Button("OK");
				ok.setOnAction((f) -> {
					for (Spieler sp : aufst.getStartelf(steuerung.getSpieler())) {
						aufst.removeSpielerStartelf(sp);
					}
					for (Spieler sp : aufst.getAuswechselspieler(steuerung.getSpieler())) {
						aufst.removeSpielerBank(sp);
					}
					for (int i = 0; i < aufst.getHoechstStartelf(); ++i) {
						if (cBoxen.get(i).getValue() != null) {
							aufst.addSpielerStartelf(cBoxen.get(i).getValue());
						}
					}
					for (int i = aufst.getHoechstStartelf(); i < cBoxen.size(); ++i) {
						if (cBoxen.get(i).getValue() != null) {
							aufst.addSpielerBank(cBoxen.get(i).getValue());
						}
					}
					aufstStage.hide();
					akt.aktualisieren();
				});
				aufstGp.add(ok, 2, 1);

				aufstGp.setPrefSize(350, 410);

				Scene aufstScene = new Scene(aufstGp);
				aufstStage.setScene(aufstScene);
				aufstStage.show();
			} else {
				Alert warnung = new Alert(AlertType.INFORMATION);
				warnung.initModality(Modality.WINDOW_MODAL);
				warnung.initOwner(stage);
				warnung.setTitle("Bearbeiten der Aufstellung nicht möglich!");
				warnung.setHeaderText(null);
				warnung.setContentText("Da das Spiel schon ein Ergebnis hat, "
						+ "kann die Aufstellung nicht bearbeiten werden.\n" + "Zum Bearbeiten Ergebnis entfernen.");
				warnung.showAndWait();
			}
		});
		btns.getChildren().add(setzen);

		Button kapitaen = new Button("Kapitän bestimmen");
		kapitaen.setFont(Font.font("Verdana"));
		kapitaen.setOnAction((e) -> {
			if (!spiel.isErgebnis()) {
				Stage kapStage = new Stage();
				kapStage.setTitle("Kapitän bestimmen");
				kapStage.initModality(Modality.WINDOW_MODAL);
				kapStage.initOwner(stage);

				GridPane gp = new GridPane();
				gp.setPadding(new Insets(5));
				gp.setHgap(5);
				gp.setVgap(5);

				Scene scene = new Scene(gp);
				kapStage.setScene(scene);
				kapStage.show();
			} else {
				Alert warnung = new Alert(AlertType.INFORMATION);
				warnung.initModality(Modality.WINDOW_MODAL);
				warnung.initOwner(stage);
				warnung.setTitle("Bestimmen des Kapitäns nicht möglich!");
				warnung.setHeaderText(null);
				warnung.setContentText("Da das Spiel schon ein Ergebnis hat, "
						+ "kann der Kapitän nicht bestimmt werden.\n" + "Zum Bearbeiten Ergebnis entfernen.");
				warnung.showAndWait();
			}
		});
		kapitaen.setDisable(true);
		btns.getChildren().add(kapitaen);

		tab.setContent(aufstellung);
	}

	private void createSpielereignisFeld(GridPane gp, Spiel sp) {
		GridPane ereignisGP = new GridPane();
		ereignisGP.setPadding(new Insets(5));
		ereignisGP.setVgap(5);
		ereignisGP.setHgap(5);
		ereignisGP.setAlignment(Pos.CENTER);

		gp.add(ereignisGP, 2, 0, 1, 2);

		// Spielaktivitaet ereignis=null;

		Label ueberschrift = new Label("Spielereignisse");
		ueberschrift.setFont(Font.font(20));
		ereignisGP.add(ueberschrift, 0, 0, 2, 1);

		GridPane ereignisListeGP = new GridPane(); // Die Container für die Auflistung der Spielereignisse
		ereignisListeGP.setPrefSize(280, 500);
		// ereignisListeGP.setAlignment(Pos.TOP_CENTER);
		ScrollPane ereignisListeSP = new ScrollPane(ereignisListeGP);
		ereignisGP.add(ereignisListeSP, 0, 1, 2, 1);

		if (sp != null) {
			Label heimUeberschrift = new Label(sp.getHeimteam().getMoeglichKN());
			heimUeberschrift.setFont(Font.font(20));
			heimUeberschrift.setPrefWidth(140);
			heimUeberschrift.setAlignment(Pos.CENTER);
			ereignisListeGP.add(heimUeberschrift, 0, 0);

			Label auswaertsUeberschrift = new Label(sp.getAuswaertsteam().getMoeglichKN());
			auswaertsUeberschrift.setFont(Font.font(20));
			auswaertsUeberschrift.setPrefWidth(140);
			auswaertsUeberschrift.setAlignment(Pos.CENTER);
			ereignisListeGP.add(auswaertsUeberschrift, 1, 0);

			ArrayList<Spielaktivitaet> ereignisse = sp.getEreignisseSortiert();
			for (int i = 0; i < ereignisse.size(); ++i) {
				Button ereignisBtn = new Button(ereignisse.get(i).toStringGebrochen());
				ereignisBtn.setPrefWidth(140);
				final int j = i;
				ereignisBtn.setOnAction((e) -> {
					letztesEreignis = ereignisse.get(j);
					akt.aktualisieren();
				});
				if (ereignisse.get(i).getTeamID() == sp.getHeimID()) {
					ereignisBtn.setAlignment(Pos.CENTER_LEFT);
					ereignisListeGP.add(ereignisBtn, 0, i + 1);
				} else {
					ereignisListeGP.add(ereignisBtn, 1, i + 1);
					ereignisBtn.setAlignment(Pos.CENTER_RIGHT);
				}
			}
		}

		Button hinzufuegen = new Button("Spielereignis hinzufügen"); // Die Buttons zum hinzufügen und entfernen von
																		// Ereignissen
		ereignisGP.add(hinzufuegen, 0, 2);
		hinzufuegen.setOnAction((e) -> {
			if (sp != null) {
				new SpielereignisHinzufuegen(stage, akt, steuerung, sp);
			}
		});

		Button entfernen = new Button("Spielereignis entfernen");
		entfernen.setOnAction((e) -> {
			if (sp.getEreignisseSortiert().size() > 0) {
				new ListDialog<Spielaktivitaet>(sp.getEreignisseSortiert(), stage,350,
						"Welches Spielereigniss soll gelöscht werden?", "Spielereignis löschen", (f) -> {
							try {
								if (f.isTor()) {
									steuerung.removeTor(f.getID());
								} else if (f.isWechsel()) {
									steuerung.removeWechsel(f.getID(), sp);
								} else {
									steuerung.removeStrafe(f.getID());
								}
								akt.aktualisieren();
							}catch (IllegalArgumentException iae) {
								Alert warnung = new Alert(AlertType.ERROR);
								warnung.initModality(Modality.WINDOW_MODAL);
								warnung.initOwner(stage);
								warnung.setTitle("Entfernen des Spielereignisses nicht möglich");
								warnung.setHeaderText(null);
								warnung.setContentText(iae.getMessage());
								warnung.showAndWait();
							}
						});
			}
		});
		ereignisGP.add(entfernen, 1, 2);

		if (sp == null) {
			hinzufuegen.setDisable(true);
			entfernen.setDisable(true);
		}

		GridPane ereignisDetailsGP = new GridPane(); // Container für die Detaildaten eines Ereignisses
		ereignisDetailsGP.setPadding(new Insets(5));
		ereignisDetailsGP.setVgap(5);
		ereignisDetailsGP.setHgap(5);
		ereignisDetailsGP.setAlignment(Pos.CENTER_LEFT);

		ereignisGP.add(ereignisDetailsGP, 0, 3, 2, 1);

		if (sp != null) {
			if (!sp.getEreignisseSortiert().contains(letztesEreignis) && sp.getEreignisseSortiert().size() > 0) {
				letztesEreignis = sp.getEreignisseSortiert().get(0); // Test für die Details eines Ereignisses
			} else if (sp.getEreignisseSortiert().size() == 0) {
				letztesEreignis = null;
			}
		}

		createEreignisBeschreibung(ereignisDetailsGP, letztesEreignis);

		// ereignisGP.add(new Label(sp.getHeimtoreDirekt().get(0).toString()), 0, 0);
		// //Test für das Ausgeben von einem Ereignis
	}

	private void createEreignisBeschreibung(GridPane ereignisDetailsGP, Spielaktivitaet ereignis) {
		Label spielerLabel = new Label("Spieler");
		spielerLabel.setFont(Font.font(20));
		ereignisDetailsGP.add(spielerLabel, 0, 1);

		Label zeitLabel = new Label("Zeit");
		zeitLabel.setFont(Font.font(20));
		ereignisDetailsGP.add(zeitLabel, 0, 2);

		Label artLabel = new Label("Art");
		artLabel.setFont(Font.font(20));
		ereignisDetailsGP.add(artLabel, 0, 3);

		Label teamLabel = new Label("Team");
		teamLabel.setFont(Font.font(20));
		ereignisDetailsGP.add(teamLabel, 0, 4);

		if (ereignis == null) {
			Label ueberschriftLabel = new Label("Nichts ausgewählt");
			ueberschriftLabel.setFont(Font.font(20));
			ereignisDetailsGP.add(ueberschriftLabel, 0, 0);
		} else if (ereignis.isTor()) {
			Label ueberschriftLabel = new Label("Tor");
			ueberschriftLabel.setFont(Font.font(20));
			ereignisDetailsGP.add(ueberschriftLabel, 0, 0);

			Label vorlagengeberLabel = new Label("Vorlagengeber");
			vorlagengeberLabel.setFont(Font.font(20));
			ereignisDetailsGP.add(vorlagengeberLabel, 0, 5);

			String[] beschriftungen = getDetailsTor((Tor) ereignis);

			Text spielerText = new Text(beschriftungen[0]);
			spielerText.setFont(Font.font(20));
			ereignisDetailsGP.add(spielerText, 1, 1);

			Text zeitText = new Text(beschriftungen[1]);
			zeitText.setFont(Font.font(20));
			ereignisDetailsGP.add(zeitText, 1, 2);

			Text artText = new Text(beschriftungen[2]);
			artText.setFont(Font.font(20));
			ereignisDetailsGP.add(artText, 1, 3);

			Text teamText = new Text(beschriftungen[3]);
			teamText.setFont(Font.font(20));
			ereignisDetailsGP.add(teamText, 1, 4);

			Text vorlagengeberText = new Text(beschriftungen[4]);
			vorlagengeberText.setFont(Font.font(20));
			ereignisDetailsGP.add(vorlagengeberText, 1, 5);
		} else if(ereignis.isWechsel()) {
			
		}else{
		Label ueberschriftLabel = new Label("Strafe");
		ueberschriftLabel.setFont(Font.font(20));
		ereignisDetailsGP.add(ueberschriftLabel, 0, 0);

		Label gefoulterLabel = new Label("Gefoulter");
		gefoulterLabel.setFont(Font.font(20));
		ereignisDetailsGP.add(gefoulterLabel, 0, 5);

		String[] beschriftungen = getDetailsStrafe((Strafe) ereignis);

		Text spielerText = new Text(beschriftungen[0]);
		spielerText.setFont(Font.font(20));
		ereignisDetailsGP.add(spielerText, 1, 1);

		Text zeitText = new Text(beschriftungen[1]);
		zeitText.setFont(Font.font(20));
		ereignisDetailsGP.add(zeitText, 1, 2);

		Text artText = new Text(beschriftungen[2]);
		artText.setFont(Font.font(20));
		ereignisDetailsGP.add(artText, 1, 3);

		Text teamText = new Text(beschriftungen[3]);
		teamText.setFont(Font.font(20));
		ereignisDetailsGP.add(teamText, 1, 4);

		Text vorlagengeberText = new Text(beschriftungen[4]);
		vorlagengeberText.setFont(Font.font(20));
		ereignisDetailsGP.add(vorlagengeberText, 1, 5);
	}
	}

	private String[] getDetailsTor(Tor tor) {
		String[] returner = new String[5];
		returner[0] = tor.getAusfuehrer().getName();
		returner[1] = tor.getZeitUndNachspielzeit();
		returner[2] = tor.getTyp();
		returner[3] = tor.getTeam().getMoeglichKN();
		if (tor.getVorbereiter() != null) {
			returner[4] = tor.getVorbereiter().getName();
		} else {
			returner[4] = "Nicht angegeben";
		}
		return returner;
	}

	private String[] getDetailsStrafe(Strafe strafe) {
		String[] returner = new String[5];
		returner[0] = strafe.getAusfuehrer().getName();
		returner[1] = strafe.getZeitUndNachspielzeit();
		returner[2] = strafe.getTyp();
		returner[3] = strafe.getTeam().getMoeglichKN();
		if (strafe.getGefoulten() != null) {
			returner[4] = strafe.getGefoulten().getName();
		} else {
			returner[4] = "Nicht angegeben";
		}
		return returner;
	}

	private class ZwLong {
		private long wert = 0l;

		private ZwLong(long l) {
			wert = l;
		}

		public long getWert() {
			return wert;
		}

		public void setWert(long wert) {
			this.wert = wert;
		}
	}
}
