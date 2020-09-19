package de.pasch.turnierleitung.uis;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import de.pasch.turnierleitung.spiele.Spiel;
import de.pasch.turnierleitung.spiele.Tor;
import de.pasch.turnierleitung.steuerung.Steuerung;
import de.pasch.turnierleitung.turnierelemente.Liga;
import de.pasch.turnierleitung.turnierelemente.Spieltag;
import de.pasch.turnierleitung.turnierelemente.Turnierelement;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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
	HFProtagonisten hfp;
	HFTurnierelemente hft;
	HFSpieltag hfst;
	
	public HFSpiele(Stage stage,BorderPane bp,Steuerung steuerung, Aktualisierer akt) {
		this.stage=stage;
		this.bp=bp;
		this.steuerung=steuerung;
		this.akt=akt;
				
		gp=new GridPane();
		gp.setPadding(new Insets(5));
		gp.setVgap(5);
		gp.setHgap(5);
		gp.setAlignment(Pos.TOP_LEFT);
		
		try {
			bp.getChildren().remove(1);
		}catch(IndexOutOfBoundsException ioobe) {}
		bp.setCenter(gp);
		
		aktualisiere(steuerung);
	}
	
	public HFSpiele(Liga liga,Spieltag spt,Spiel sp,Stage stage,BorderPane bp,Steuerung steuerung, Aktualisierer akt) {
		this.stage=stage;
		this.bp=bp;
		this.steuerung=steuerung;
		this.akt=akt;
		
		letztesTE=liga;
		letzterSpt=spt;
		letztesSpiel=sp;
		
		gp=new GridPane();
		gp.setPadding(new Insets(5));
		gp.setVgap(5);
		gp.setHgap(5);
		gp.setAlignment(Pos.TOP_LEFT);
		
		try {
			bp.getChildren().remove(1);
		}catch(IndexOutOfBoundsException ioobe) {}
		bp.setCenter(gp);
		
		aktualisiere(steuerung);
	}
	
	public void aktualisiere(Steuerung steuerung) {
		this.steuerung=steuerung;
		if(hfp!=null) {
			hfp.aktualisiere(steuerung);
		}
		if(hft!=null) {
			hft.aktualisiere(steuerung);
		}
		if(hfst!=null) {
			hfst.aktualisiere(steuerung);
		}
		
		gp.getChildren().clear();
		
		if(steuerung.getTurnierelemente().size()>0) {
			createElUSptSelector();
		}else {
			Text fehlerText=new Text("Es sind keine Turnierelemente vorhanden! Bitte fügen "
					+ "Sie eines hinzu.");
	        fehlerText.setFont(Font.font(20));
	        gp.add(fehlerText, 0, 0);
		}
		
		stage.show();
	}
	
	private void createElUSptSelector() {
		HBox turnierBox=new HBox();
		gp.add(turnierBox, 0, 0);
		ComboBox<Turnierelement> teAuswahl=new ComboBox<Turnierelement>();
		turnierBox.getChildren().add(teAuswahl);
		Button zuTurnierelement=new Button("<-Tabelle zeigen");
		zuTurnierelement.setOnAction((e)->{
			hft=new HFTurnierelemente(teAuswahl.getValue(), stage, bp, steuerung, akt);
		});
		turnierBox.getChildren().add(zuTurnierelement);
		for(Turnierelement te: steuerung.getTurnierelemente()) {
			teAuswahl.getItems().add(te);
		}
		if(steuerung.getTurnierelemente().contains(letztesTE)) {
			teAuswahl.getSelectionModel().select(letztesTE);
		}else{
			teAuswahl.getSelectionModel().select(steuerung.getTurnierelemente().get(0));
		}
		teAuswahl.setOnAction((e)->{
			letztesTE=teAuswahl.getSelectionModel().getSelectedItem();
			aktualisiere(steuerung);
		});
		
		Turnierelement aktuell=teAuswahl.getValue();
		if(aktuell.isLiga()) {
			Liga aktLiga=(Liga)aktuell;
			HBox sptBox=new HBox();
			gp.add(sptBox, 1, 0);
			ComboBox<Spieltag>sptAuswahl=new ComboBox<Spieltag>();
			Button zuSpt=new Button("<-Details zeigen");
			zuSpt.setOnAction((e)->{
				hfst=new HFSpieltag(aktLiga, sptAuswahl.getValue(), null, stage, bp, steuerung, akt);
			});
			sptBox.getChildren().addAll(sptAuswahl,zuSpt);
			for(Spieltag spt:aktLiga.getSpieltage()) {
				sptAuswahl.getItems().add(spt);
			}
			if(aktLiga.getSpieltage().contains(letzterSpt)) {
				sptAuswahl.getSelectionModel().select(letzterSpt);
				createSpiellisteSpt(letzterSpt);
			}else if(aktLiga.getSpieltage().size()>0) {
				Spieltag neuerSpt=aktLiga.getSpieltage().get(0);
				for( int i=1;i<aktLiga.getSpieltage().size()&&neuerSpt.istAbgeschlossen();++i){
				neuerSpt=aktLiga.getSpieltage().get(i);
			}
				sptAuswahl.setValue(neuerSpt);
				letzterSpt=neuerSpt;
				createSpiellisteSpt(sptAuswahl.getValue());
			}else {
				createSpiellisteSpt(null);
			}
			
			sptAuswahl.setOnAction((e)->{
				letzterSpt=sptAuswahl.getSelectionModel().getSelectedItem();
				aktualisiere(steuerung);
			});
		}
	}
	
	/**
	 * @param selected der ausgewählte spieltag, null ist erlaubt
	 */
	private void createSpiellisteSpt(Spieltag selected) {
		VBox spieleBox=new VBox();
		spieleBox.setPrefSize(200,500);
		ScrollPane spieleScroll=new ScrollPane(spieleBox);
		gp.add(spieleScroll, 0, 1);
		if(selected!=null) {
			for(Spiel sp:selected.getSpiele()) {
				Button btn=new Button(sp.toString());
				btn.setPrefWidth(190);
				spieleBox.getChildren().add(btn);
				btn.setOnAction((e)->{
					letztesSpiel=sp;
					aktualisiere(steuerung);
				});
			}
			if(selected.getSpiele().contains(letztesSpiel)) {
				createSpielAnsicht(letztesSpiel);
			}else if(selected.getSpiele().size()>0) {
				createSpielAnsicht(selected.getSpiele().get(0));
			}else {
				createSpielAnsicht(null);
			}
			
		}
	}
	
	/**
	 * @param sp das ausgewählte spiel, null ist erlaubt
	 */
	private void createSpielAnsicht(Spiel sp) {
		GridPane spielInfos=new GridPane();
		spielInfos.setPadding(new Insets(10,10,10,10));
		spielInfos.setHgap(10);
		spielInfos.setVgap(10);
		gp.add(spielInfos, 1,1);
		String[]beschr = createBeschrSpiel(sp);
		
		int verschiebung=4;
		
		Label heimLabel=new Label("Heimteam");
		heimLabel.setFont(Font.font(20));
		spielInfos.add(heimLabel, 0, 0+verschiebung);
		
		Text heimText=new Text(beschr[0]);
		heimText.setFont(Font.font(20));
		if(sp!=null) {
			heimText.setOnMouseClicked((e)->{
				hfp=new HFProtagonisten(sp.getHeimteam(), stage, bp, steuerung, akt);
			});
		}
		spielInfos.add(heimText, 1, 0+verschiebung);
		
		Label auswLabel=new Label("Auswärtsteam");
		auswLabel.setFont(Font.font(20));
		spielInfos.add(auswLabel, 0, 1+verschiebung);
		
		Text auswText=new Text(beschr[1]);
		auswText.setFont(Font.font(20));
		if(sp!=null) {
			auswText.setOnMouseClicked((e)->{
				hfp=new HFProtagonisten(sp.getAuswaertsteam(), stage, bp, steuerung, akt);
			});
		}
		spielInfos.add(auswText, 1, 1+verschiebung);
				
		Label stadionLabel=new Label("Stadionname");
		stadionLabel.setFont(Font.font(20));
		spielInfos.add(stadionLabel, 0, 2+verschiebung);
		
		Text stadionText=new Text(beschr[2]);
		stadionText.setFont(Font.font(20));
		spielInfos.add(stadionText, 1,2+verschiebung);
		
		Label neutrLabel=new Label("Neutraler Platz");
		neutrLabel.setFont(Font.font(20));
		spielInfos.add(neutrLabel, 0, 3+verschiebung);
		
		Text neutrText=new Text(beschr[3]);
		neutrText.setFont(Font.font(20));
		spielInfos.add(neutrText, 1,3+verschiebung);
		
		createMinimaltorEingabe(sp,spielInfos);
	}
	
	private void createMinimaltorEingabe(Spiel sp,GridPane spielInfo) {		
		Label teamnamenHeim=new Label("");
		teamnamenHeim.setFont(Font.font("Verdana",FontWeight.BOLD, 20));
		teamnamenHeim.setAlignment(Pos.CENTER);
		if(sp!=null) {
			teamnamenHeim.setOnMouseClicked((e)->{
				hfp=new HFProtagonisten(sp.getHeimteam(), stage, bp, steuerung, akt);
			});
		}
		spielInfo.add(teamnamenHeim, 0,0);
		
		Label teamnamenAuswaerts=new Label("");
		teamnamenAuswaerts.setFont(Font.font("Verdana",FontWeight.BOLD, 20));
		teamnamenAuswaerts.setAlignment(Pos.CENTER);
		if(sp!=null) {
			teamnamenAuswaerts.setOnMouseClicked((e)->{
				hfp=new HFProtagonisten(sp.getAuswaertsteam(), stage, bp, steuerung, akt);
			});
		}
		spielInfo.add(teamnamenAuswaerts,1,0);
		
		Label heimtore=new Label();
		heimtore.setFont(Font.font("Verdana",FontWeight.BOLD,20));
		heimtore.setAlignment(Pos.CENTER);
		spielInfo.add(heimtore,0,1);
		
		Label auswaertstore=new Label();
		auswaertstore.setFont(Font.font("Verdana",FontWeight.BOLD,20));
		auswaertstore.setAlignment(Pos.CENTER);
		spielInfo.add(auswaertstore,1,1);
		
		
		VBox schalterHeim=new VBox();
		spielInfo.add(schalterHeim, 0, 2);
		VBox schalterAuswaerts=new VBox();
		spielInfo.add(schalterAuswaerts, 1, 2);
		
		int prefWidth=100;
		
		Button plusHeim=new Button("+");
		plusHeim.setFont(Font.font("Verdana"));
		plusHeim.setPrefWidth(prefWidth);
		plusHeim.setDisable(true);
		plusHeim.setOnAction((e)->{
			sp.setMinimaleHeimtore(sp.getHeimtoreZahl()+1);
			akt.aktualisieren();
		});
		schalterHeim.getChildren().add(plusHeim);
		
		Button eingHeim=new Button("Eingeben");
		eingHeim.setFont(Font.font("Verdana"));
		eingHeim.setPrefWidth(prefWidth);
		eingHeim.setDisable(true);
		eingHeim.setOnAction((e)->{
			MaximalTorEingabe.getMTA(stage, (t)->{
				if(t>=sp.getMengeHeimtoreDirekt()) {
					sp.setMinimaleHeimtore(t);
					akt.aktualisieren();
				}else {
					JOptionPane.showMessageDialog(null, "Es sind zu viele Tore direkt eingetragen worden,"
							+ " bitte diese entfernen!","FEHLER!",JOptionPane.ERROR_MESSAGE);
				}
			});
		});
		schalterHeim.getChildren().add(eingHeim);
		
		Button minusHeim=new Button("-");
		minusHeim.setFont(Font.font("Verdana"));
		minusHeim.setPrefWidth(prefWidth);
		minusHeim.setDisable(true);
		minusHeim.setOnAction((e)->{
			if(sp.getHeimtoreZahl()>sp.getMengeHeimtoreDirekt()) {
				sp.setMinimaleHeimtore(sp.getHeimtoreZahl()-1);
				akt.aktualisieren();
			}else {
				JOptionPane.showMessageDialog(null, "Es sind zu viele Tore direkt eingetragen worden,"
						+ " bitte diese entfernen!","FEHLER!",JOptionPane.ERROR_MESSAGE);
			}
		});
		schalterHeim.getChildren().add(minusHeim);
		
		
		Button plusAuswaerts=new Button("+");
		plusAuswaerts.setFont(Font.font("Verdana"));
		plusAuswaerts.setPrefWidth(prefWidth);
		plusAuswaerts.setDisable(true);
		plusAuswaerts.setOnAction((e)->{
			sp.setMinimaleAuswaertstore(sp.getAuswaertstoreZahl()+1);
			akt.aktualisieren();
		});
		schalterAuswaerts.getChildren().add(plusAuswaerts);
		
		Button eingAuswaerts=new Button("Eingeben");
		eingAuswaerts.setFont(Font.font("Verdana"));
		eingAuswaerts.setPrefWidth(prefWidth);
		eingAuswaerts.setDisable(true);
		eingAuswaerts.setOnAction((e)->{
			MaximalTorEingabe.getMTA(stage, (t)->{
				if(t>=sp.getMengeHeimtoreDirekt()) {
					sp.setMinimaleHeimtore(t);
					akt.aktualisieren();
				}else {
					JOptionPane.showMessageDialog(null, "Es sind zu viele Tore direkt eingetragen worden,"
							+ " bitte diese entfernen!","FEHLER!",JOptionPane.ERROR_MESSAGE);
				}
			});
		});
		schalterAuswaerts.getChildren().add(eingAuswaerts);
		
		Button minusAuswaerts=new Button("-");
		minusAuswaerts.setFont(Font.font("Verdana"));
		minusAuswaerts.setPrefWidth(prefWidth);
		minusAuswaerts.setDisable(true);
		minusAuswaerts.setOnAction((e)->{
			if(sp.getAuswaertstoreZahl()>sp.getMengeAuswaertstoreDirekt()) {
				sp.setMinimaleAuswaertstore(sp.getAuswaertstoreZahl()-1);
				akt.aktualisieren();
			}else {
				JOptionPane.showMessageDialog(null, "Es sind zu viele Tore direkt eingetragen worden,"
						+ " bitte diese entfernen!","FEHLER!",JOptionPane.ERROR_MESSAGE);
			}
		});
		schalterAuswaerts.getChildren().add(minusAuswaerts);
		
		Button aktivierer=new Button("als 0:0 zählen");
		aktivierer.setFont(Font.font("Verdana"));
		aktivierer.setPrefWidth(prefWidth*2+40);
		aktivierer.setDisable(true);
		spielInfo.add(aktivierer,0,3,2,1);
		
		
		if(sp!=null) {
			teamnamenHeim.setText(sp.getHeimteam().getMoeglichKN());
			teamnamenAuswaerts.setText(sp.getAuswaertsteam().getMoeglichKN());
			if(sp.isErgebnis()) {
				heimtore.setText(""+sp.getHeimtoreZahl());
				auswaertstore.setText(""+sp.getAuswaertstoreZahl());
				aktivierer.setText("Ergebnis löschen");
				aktivierer.setOnAction((e)->{
					int best=JOptionPane.showConfirmDialog(null, "Sollen wirklich alle Tore "
							+ "(samt der vollständig eingegebenen) gelöscht werden?",
							"ACHTUNG!", JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
					if(best==JOptionPane.OK_OPTION) {
						ArrayList<Tor> tore=new ArrayList<Tor>(sp.getHeimtoreDirekt());
						tore.addAll(sp.getAuswaertstoreDirekt());
						for(Tor t:tore) {
							steuerung.removeTor(t.getID());
						}
						sp.setMinimaleAuswaertstore(0);
						sp.setMinimaleHeimtore(0);
						sp.setErgebnis(false);
						akt.aktualisieren();
					}
				});
			}else {
				heimtore.setText("-");
				auswaertstore.setText("-");
				aktivierer.setOnAction((e)->{
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
		String[]beschr=new String[4];
		
		if(spiel!=null) {
			beschr[0]=spiel.getHeimteam().getName();
			beschr[1]=spiel.getAuswaertsteam().getName();
			beschr[2]=spiel.getStadion();
			if(spiel.getNeutralerPlatz()) {
				beschr[3]="Ja";
			}else {
				beschr[3]="Nein";
			}
			
		}else {
			for (int i = 0; i < beschr.length; i++) {
				beschr[i]="";
			}
		}
		
		return beschr;
	}
	
	
}
