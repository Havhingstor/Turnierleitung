package de.pasch.turnierleitung.uis;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MaximalTorEingabe {	
	public static void getMTA(Stage par,Handler<Integer> h) {
		Stage stage=new Stage();
		stage.setTitle("Tore eingeben");
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(par);
		
		GridPane gp=new GridPane();
		gp.setAlignment(Pos.TOP_CENTER);
		gp.setPadding(new Insets(5));
		gp.setHgap(5);
		gp.setVgap(5);
		
		Label nachricht=new Label("Bitte Toranzahl eingeben!");
		nachricht.setFont(Font.font("Verdana"));
		gp.add(nachricht,0,0,2,1);
		
		NumberTextField ntf=new NumberTextField();
		ntf.setFont(Font.font("Verdana"));
		gp.add(ntf,0,1,2,1);
		
		Button okBtn=new Button("OK");
		okBtn.setFont(Font.font("Verdana"));
		okBtn.setOnAction((e)->{
			if(ntf.getText().length()>0) {
				h.handle(Integer.parseInt(ntf.getText()));
			}else {
				h.handle(0);
			}
			stage.hide();
		});
		gp.add(okBtn, 0, 2);
		
		Button abbrBtn=new Button("Abbrechen");
		abbrBtn.setFont(Font.font("Verdana"));
		abbrBtn.setOnAction((e)->{
			stage.hide();
		});
		gp.add(abbrBtn, 1, 2);
		
		Scene scene=new Scene(gp,260,100);
		stage.setScene(scene);
		stage.show();
	}
}
