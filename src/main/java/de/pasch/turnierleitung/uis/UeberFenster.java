package de.pasch.turnierleitung.uis;

import de.pasch.turnierleitung.steuerung.Steuerung;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UeberFenster {
	public UeberFenster(Steuerung st, Stage par) {
		Stage stage=new Stage();
		stage.setTitle("Über");
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(par);
		
		GridPane pane=new GridPane();
		pane.setPadding(new Insets(15));
		pane.setHgap(15);
		pane.setVgap(15);
		pane.setAlignment(Pos.CENTER);
		
		Label text1=new Label("Turnierleitung");
		text1.setAlignment(Pos.CENTER);
		text1.setFont(Font.font("Verdana",FontWeight.BOLD,25));
		pane.add(text1,0,0);
		
		Label text2=new Label("created and developed 2019 and 2020");
		text2.setAlignment(Pos.CENTER);
		text2.setFont(Font.font("Verdana",FontWeight.BOLD,25));
		pane.add(text2,0,1);

		Label text3=new Label("by Paul Schütz");
		text3.setAlignment(Pos.CENTER);
		text3.setFont(Font.font("Verdana",FontWeight.BOLD,25));
		pane.add(text3,0,2);
		
		Label text4=new Label("Version: "+st.version);
		text4.setAlignment(Pos.CENTER);
		text4.setFont(Font.font("Verdana",FontWeight.BOLD,25));
		pane.add(text4,0,3);
		
		Scene scene=new Scene(pane,600,300);
		stage.setScene(scene);
		stage.show();
	}
}
