package de.pasch.turnierleitung.uis;

import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.steuerung.Steuerung;
import de.pasch.turnierleitung.turnierelemente.KORunde;
import de.pasch.turnierleitung.turnierelemente.Rundensammlung;
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

public class RundeHinzufuegen {
	Stage stage;
	Aktualisierer akt;
	Steuerung steuerung;
	Rundensammlung rs;
	
	public RundeHinzufuegen(Stage primstage,Aktualisierer akt,Steuerung steuerung,
			Rundensammlung rs,KORunde kor) {
		this.akt=akt;
		this.steuerung=steuerung;
		this.rs=rs;
		
		stage=new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(primstage);
		
		if(steuerung.getTeams().size()>1) {
			GridPane gp=new GridPane();
			gp.setPadding(new Insets(5));
			gp.setHgap(5);
			gp.setVgap(5);
			gp.setAlignment(Pos.TOP_CENTER);
			
			Label heimLab=new Label("Anfangs-Heimteam");
			heimLab.setFont(Font.font(20));
			gp.add(heimLab, 0, 0);
			
			Label auswaertsLab=new Label("Anfangs-Auswärtsteam");
			auswaertsLab.setFont(Font.font(20));
			gp.add(auswaertsLab, 1, 0);
			
			ComboBox<Team>heimTeam=new ComboBox<Team>();
			heimTeam.getItems().addAll(steuerung.getTeams());
			Team letztesHeimteam=heimTeam.getItems().get(0);
			ZWTeam altHeim=new ZWTeam(letztesHeimteam);
			heimTeam.setValue(letztesHeimteam);
			gp.add(heimTeam, 0, 1);
			
			ComboBox<Team>auswaertsTeam=new ComboBox<Team>();
			auswaertsTeam.getItems().addAll(steuerung.getTeams());
			Team letztesAuswaertsteam=auswaertsTeam.getItems().get(1);
			ZWTeam altAuswaerts=new ZWTeam(letztesAuswaertsteam);
			auswaertsTeam.setValue(letztesAuswaertsteam);
			gp.add(auswaertsTeam, 1, 1);
			
			ZWText erlaubt=new ZWText();
			erlaubt.setText("");
			
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
			
			CheckBox besPlatzHeim=new CheckBox("Besonderer Platzname");
			besPlatzHeim.setFont(Font.font(15));
			gp.add(besPlatzHeim, 0, 2);
			
			TextField stadionTextHeim=new TextField();
			stadionTextHeim.setFont(Font.font(20));
			stadionTextHeim.setEditable(false);
			gp.add(stadionTextHeim, 0,3);
			
			ZWText altNameHeim=new ZWText();
			besPlatzHeim.setOnAction((e)->{
				if(!besPlatzHeim.isSelected()) {
					altNameHeim.setText(stadionTextHeim.getText());
					stadionTextHeim.setText("");
					stadionTextHeim.setEditable(false);
				}else {
					stadionTextHeim.setEditable(true);
					stadionTextHeim.setText(altNameHeim.getText());
				}
			});
			
			CheckBox besPlatzAuswaerts=new CheckBox("Besonderer Platzname");
			besPlatzAuswaerts.setFont(Font.font(15));
			gp.add(besPlatzAuswaerts, 1, 2);
			
			TextField stadionTextAuswaerts=new TextField();
			stadionTextAuswaerts.setFont(Font.font(20));
			stadionTextAuswaerts.setEditable(false);
			gp.add(stadionTextAuswaerts, 1,3);
			
			ZWText altNameAuswaerts=new ZWText();
			besPlatzAuswaerts.setOnAction((e)->{
				if(!besPlatzAuswaerts.isSelected()) {
					altNameAuswaerts.setText(stadionTextAuswaerts.getText());
					stadionTextAuswaerts.setText("");
					stadionTextAuswaerts.setEditable(false);
				}else {
					stadionTextAuswaerts.setEditable(true);
					stadionTextAuswaerts.setText(altNameAuswaerts.getText());
				}
			});
			
			CheckBox neutral=new CheckBox("Neutraler Platz");
			neutral.setFont(Font.font(15));
			gp.add(neutral, 0, 4);
						
			CheckBox auslosung=new CheckBox("Erstes Heimteam Auslosen");
			auslosung.setFont(Font.font(15));
			gp.add(auslosung, 1, 4);
			
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
			gp.add(speichern, 0,5);
			speichern.setOnAction((e)->{
				steuerung.addRunde(rs.getID(), kor,auslosung.isSelected(),
						heimTeam.getValue().getID(), auswaertsTeam.getValue().getID(), 
						neutral.isSelected(),stadionTextHeim.getText() , stadionTextAuswaerts.getText());
				akt.aktualisieren();
				stage.hide();
			});
			
			Scene scene=new Scene(gp,500,350);
			stage.setScene(scene);
			stage.show();
		}
	}
}
