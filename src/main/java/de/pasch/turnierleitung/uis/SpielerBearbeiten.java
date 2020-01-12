package de.pasch.turnierleitung.uis;

import javax.swing.JOptionPane;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.protagonisten.SpielerTeamConnector;
import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.steuerung.Steuerung;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SpielerBearbeiten {
	Steuerung steuerung;
    long ID;
    
    public SpielerBearbeiten(Steuerung steuerung,Stage primStage,Spieler spieler,Aktualisierer akt){
        this.ID=spieler.getID();
        Stage stage=new Stage();
        stage.setTitle("Spieler bearbeiten");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primStage);
        GridPane gp=new GridPane();
        gp.setPadding(new Insets(5,5,5,5));
        gp.setHgap(5);
        gp.setVgap(5);
        Label vornameLabel=new Label("Vorname");
        vornameLabel.setPrefWidth(145);
        Label vornameAlt=new Label(spieler.getVorname());
        vornameAlt.setPrefWidth(145);
        vornameAlt.setFont(Font.font("Verdana",FontWeight.MEDIUM,FontPosture.ITALIC,15));
        TextField vornameFeld=new TextField(vornameAlt.getText());
        vornameFeld.setPrefWidth(145);
        gp.add(vornameLabel, 0, 0);
        gp.add(vornameAlt,1,0);
        gp.add(vornameFeld,2, 0);
        Label nachnameLabel=new Label("Nachname");
        nachnameLabel.setPrefWidth(145);
        Label nachnameAlt=new Label(spieler.getNachname());
        nachnameAlt.setPrefWidth(145);
        nachnameAlt.setFont(Font.font("Verdana",FontWeight.MEDIUM,FontPosture.ITALIC,15));
        TextField nachnameFeld=new TextField(nachnameAlt.getText());
        nachnameFeld.setPrefWidth(145);
        gp.add(nachnameLabel,0,1);
        gp.add(nachnameAlt,1,1);
        gp.add(nachnameFeld,2,1);
        //Label kapitaenLabel=new Label("Kapitän");
        //kapitaenLabel.setPrefWidth(145);
        //String kapitaenAltStr;
        Team team=steuerung.getAktivesTeamEinesSpielers(ID);
        //if(team.getKapitaen().getID()==ID) {
        	//kapitaenAltStr="Kapitän";
        //}else if(team.getVizekapitaen().getID()==ID){
        	//kapitaenAltStr="Vizekapitän";
        //}else {
        	//kapitaenAltStr="Nichts";
        //}
       
        Button speichern=new Button("Speichern");
        speichern.setPrefWidth(145);
        gp.add(speichern,2,2);
        Button loeschen=new Button("Löschen");
        loeschen.setPrefWidth(145);
        gp.add(loeschen,1,2);
        Scene scene=new Scene(gp,450,200);
        stage.setScene(scene);
        stage.show();
        speichern.setOnAction((e)->{
        	int trikotnummer=0;
        	for(SpielerTeamConnector stc:steuerung.getSTC()) {
        		if(stc.getSpielerID()==ID&&(!stc.getAusgetreten())) {
        			trikotnummer=stc.getTrikotnummer();
        		}
        	}
            if(!(nachnameFeld.getText().equals("")||vornameFeld.getText().equals(""))){
                steuerung.editSpieler(vornameFeld.getText(), nachnameFeld.getText()
                		, trikotnummer, team.getID(),ID);
                akt.aktualisieren();
                stage.hide();
            }else{
                JOptionPane.showMessageDialog(null,"Kein Name eingegeben!","FEHLER!",0);
            }
        });
        loeschen.setOnAction((e)->{
            int best=JOptionPane.showConfirmDialog(null,"Wollen sie diesen Spieler wirklich löschen?","ACHTUNG!",2,2);
            if (best==0){
                try{
                    steuerung.removeSpieler(ID);
                }catch(IllegalArgumentException iae){
                   JOptionPane.showMessageDialog(null,iae.getMessage(),"Fehler",0);
                }
                
                akt.aktualisieren();
                stage.hide();
            }
        });
    }
}
