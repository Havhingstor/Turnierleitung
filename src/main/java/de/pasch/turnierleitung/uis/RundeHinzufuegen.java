package de.pasch.turnierleitung.uis;

import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.steuerung.Steuerung;
import de.pasch.turnierleitung.turnierelemente.Rundensammlung;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
			Rundensammlung rs) {
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
				if(heimTeam.getValue().equals(auswaertsTeam.getValue())&&erlaubt.getText().length()==0) {
					heimTeam.setValue(altHeim.getTeam());
				}else {
					altHeim.setTeam(heimTeam.getValue());
				}
			});
			
			auswaertsTeam.setOnAction((e)->{
				if(auswaertsTeam.getValue().equals(heimTeam.getValue())&&erlaubt.getText().length()==0) {
					auswaertsTeam.setValue(altAuswaerts.getTeam());
				}else {
					altAuswaerts.setTeam(auswaertsTeam.getValue());
				}
			});
			
			CheckBox neutral=new CheckBox("Neutraler Platz");
			neutral.setFont(Font.font(15));
			gp.add(neutral, 0, 2);
						
			
			
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
			
			Button speichern=new Button("Speichern");
			speichern.setFont(Font.font(20));
			gp.add(speichern, 1,4);
			speichern.setOnAction((e)->{
				steuerung.addRunde(rs.getID(), false,
						heimTeam.getValue().getID(), auswaertsTeam.getValue().getID());
				akt.aktualisieren();
				stage.hide();
			});
			
			Scene scene=new Scene(gp,500,350);
			stage.setScene(scene);
			stage.show();
		}
	}
}
