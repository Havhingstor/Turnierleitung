package de.pasch.turnierleitung.uis;

import de.pasch.turnierleitung.turnierelemente.Liga;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TERKSetzer {
	Liga liga;
	Stage stage;
	Aktualisierer akt;
	
	
	public TERKSetzer(Liga liga,Stage primStage,Aktualisierer akt) {
		this.liga=liga;
		this.akt=akt;
		
		stage=new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(primStage);
		stage.setTitle("Sortierkriterien bearbeiten");
		
		GridPane gp=new GridPane();
		gp.setPadding(new Insets(5));
		gp.setHgap(5);
		gp.setVgap(5);
		
		Text ueberschrift=new Text("Sortierkriterien bearbeiten");
		ueberschrift.setFont(Font.font(20));
		gp.add(ueberschrift, 0, 0);
		
		String[]rksNormal= {"Punkte","Tordifferenz","Tore","Gegentore","Spiele"};
		int[]rksAlt=liga.getReihenfolgeKriterien();
		
		for(int i=1;i<=5;i++) {
			Label lab=new Label(i+". Kriterium");
			lab.setFont(Font.font(15));
			gp.add(lab, 0, i);
		}
		
		ComboBox[] combos=new ComboBox[5];
		
		for(int i=0;i<5;i++) {
			combos[i]=new ComboBox<String>();
			combos[i].getItems().addAll(rksNormal);
			int vor=rksAlt[i]-1;
			combos[i].setValue(rksNormal[vor]);
			gp.add(combos[i], 1, i+1);
		}
		
		Button speichern =new Button("Speichern");
		speichern.setFont(Font.font(15));
		gp.add(speichern, 0, 6);
		speichern.setOnAction((e)->{
			int[]rksNeu=new int[5];
			for(int i=0;i<5;i++) {
				String str=(String)combos[i].getValue();
				switch(str) {
				case "Punkte":rksNeu[i]=1;
				break;
				case "Tordifferenz":rksNeu[i]=2;
				break;
				case "Tore":rksNeu[i]=3;
				break;
				case "Gegentore":rksNeu[i]=4;
				break;
				case "Spiele":rksNeu[i]=5;
				break;
				}
			}
			liga.setReihenfolgeKriterien(rksNeu);
			stage.hide();
			akt.aktualisieren();
		});
		
		Scene scene=new Scene(gp,400,300);
		stage.setScene(scene);
		stage.show();
	}
}
