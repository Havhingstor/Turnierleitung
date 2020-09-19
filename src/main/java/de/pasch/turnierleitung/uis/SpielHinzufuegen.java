package de.pasch.turnierleitung.uis;

import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.spiele.Spiel;
import de.pasch.turnierleitung.steuerung.Steuerung;
import de.pasch.turnierleitung.turnierelemente.Liga;
import de.pasch.turnierleitung.turnierelemente.Spieltag;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SpielHinzufuegen {
	Stage stage;
	Aktualisierer akt;
	Steuerung steuerung;
	Liga liga;
	Spieltag spieltag;
	boolean bearbeiten=false;
	Spiel bearbeitenSpiel;
	
	public SpielHinzufuegen(Stage primstage,Aktualisierer akt,Steuerung steuerung,
			Spieltag spieltag,Liga liga) {
		this.akt=akt;
		this.steuerung=steuerung;
		this.spieltag=spieltag;
		this.liga=liga;
		aufbauen(primstage);
	}
	
	public SpielHinzufuegen(Stage primstage,Aktualisierer akt,Steuerung steuerung,Spieltag spieltag,Liga liga,Spiel bearbeiten) {
		this.liga=liga;
		this.spieltag=spieltag;
		this.akt=akt;
		this.steuerung=steuerung;
		this.bearbeitenSpiel=bearbeiten;
		this.bearbeiten=true;
		aufbauen(primstage);
	}
		
	private void aufbauen(Stage primstage) {	
		stage=new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(primstage);
		
		if(liga.getTeams().size()>1) {
			GridPane gp=new GridPane();
			gp.setPadding(new Insets(5));
			gp.setHgap(5);
			gp.setVgap(5);
			gp.setAlignment(Pos.TOP_CENTER);
			
			Label heimLab=new Label("Heimteam");
			heimLab.setFont(Font.font(20));
			gp.add(heimLab, 0, 0);
			
			Label auswaertsLab=new Label("Auswärtsteam");
			auswaertsLab.setFont(Font.font(20));
			gp.add(auswaertsLab, 1, 0);
			
			ComboBox<Team>heimTeam=new ComboBox<Team>();
			heimTeam.getItems().addAll(liga.getTeams());
			Team letztesHeimteam;
			if(bearbeiten) {
				letztesHeimteam=bearbeitenSpiel.getHeimteam();
			}else {
				letztesHeimteam=spieltag.getTeams().get(0);
			}
			ZWTeam altHeim=new ZWTeam(letztesHeimteam);
			heimTeam.setValue(letztesHeimteam);
			gp.add(heimTeam, 0, 1);
			
			ComboBox<Team>auswaertsTeam=new ComboBox<Team>();
			auswaertsTeam.getItems().addAll(liga.getTeams());
			Team letztesAuswaertsteam;
			if(bearbeiten) {
				letztesAuswaertsteam=bearbeitenSpiel.getAuswaertsteam();
			}else {
				letztesAuswaertsteam=spieltag.getTeams().get(1);
			}
			ZWTeam altAuswaerts=new ZWTeam(letztesAuswaertsteam);
			auswaertsTeam.setValue(letztesAuswaertsteam);
			gp.add(auswaertsTeam, 1, 1);
			
			ZWText erlaubt=new ZWText();
			erlaubt.setText("");
			
			if(bearbeiten) {
				heimTeam.setDisable(true);
				auswaertsTeam.setDisable(true);
			}
			
			heimTeam.setOnAction((e)->{
				if(heimTeam.getValue().equals(auswaertsTeam.getValue())) {
					auswaertsTeam.setValue(altHeim.getTeam());
				}
				altHeim.setTeam(heimTeam.getValue());
			});
			
			auswaertsTeam.setOnAction((e)->{
				if(auswaertsTeam.getValue().equals(heimTeam.getValue())) {
					heimTeam.setValue(altAuswaerts.getTeam());
				}
				altAuswaerts.setTeam(auswaertsTeam.getValue());
			});
			
			CheckBox neutral=new CheckBox("Neutraler Platz");
			neutral.setFont(Font.font(15));
			gp.add(neutral, 0, 2);
			
			CheckBox stadion=new CheckBox("Besonderer Platzname");
			stadion.setFont(Font.font(15));
			gp.add(stadion, 0, 3);
			
			TextField stadionText=new TextField();
			stadionText.setFont(Font.font(20));
			stadionText.setEditable(false);
			gp.add(stadionText, 1,3);
			
			ZWText altName=new ZWText();
			stadion.setOnAction((e)->{
				if(!stadion.isSelected()) {
					altName.setText(stadionText.getText());
					stadionText.setText("");
					stadionText.setEditable(false);
				}else {
					stadionText.setEditable(true);
					stadionText.setText(altName.getText());
				}
			});
			
			/*
			Button teamsTauschen=new Button("Teams tauschen");
			teamsTauschen.setFont(Font.font(20));
			gp.add(teamsTauschen, 0, 4);
			teamsTauschen.setOnAction((e)->{
				erlaubt.setText("a");
				Team zwTeam=heimTeam.getValue();
				heimTeam.setValue(auswaertsTeam.getValue());
				auswaertsTeam.setValue(zwTeam);
				erlaubt.setText("");
			});
			*/
			
			Button speichern=new Button("Speichern");
			speichern.setFont(Font.font(20));
			gp.add(speichern, 0,4);
			speichern.setOnAction((e)->{
				if(!bearbeiten) {
					steuerung.addSpiel(heimTeam.getValue().getID(), auswaertsTeam.getValue().getID(),neutral.isSelected(),
							stadion.isSelected(),stadionText.getText(), spieltag.getID());
					akt.aktualisieren();
					stage.hide();
				}else {
					steuerung.editSpiel(neutral.isSelected(), stadion.isSelected(), stadionText.getText(), bearbeitenSpiel.getID());
					akt.aktualisieren();
					stage.hide();
				}
			});
			
			Scene scene=new Scene(gp,500,350);
			stage.setScene(scene);
			stage.show();
		}
	}
}

class ZWText{
	private String text="";
	public void setText(String text) {
		this.text=text;
	}
	public String getText() {
		return text;
	}
}
class ZWTeam{
	private Team team;
	public ZWTeam(Team team) {
		this.team=team;
	}
	public void setTeam(Team team) {
		this.team=team;
	}
	public Team getTeam() {
		return team;
	}
}
