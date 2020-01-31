package de.pasch.turnierleitung.uis;

import javax.swing.JOptionPane;

import de.pasch.turnierleitung.steuerung.Steuerung;
import de.pasch.turnierleitung.turnierelemente.KORunde;
import de.pasch.turnierleitung.turnierelemente.Liga;
import de.pasch.turnierleitung.turnierelemente.Spieltag;
import de.pasch.turnierleitung.turnierelemente.Turnierelement;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HFSpieltag {
	Steuerung steuerung;
	BorderPane bp;
	Stage stage;
	Aktualisierer akt;
	GridPane gp;	
	
	public HFSpieltag(Stage stage,BorderPane bp,Steuerung steuerung,Aktualisierer akt) {
		this.steuerung=steuerung;
		this.bp=bp;
		this.stage=stage;
		this.akt=akt;
		
		gp=new GridPane();
		gp.setPadding(new Insets(5));
		gp.setVgap(5);
		gp.setHgap(5);
		
		try {
			bp.getChildren().remove(1);
		}catch(IndexOutOfBoundsException ioobe) {}
		bp.setCenter(gp);
		
		aktualisiere();
	}
	
	public void aktualisiere() {
		if(steuerung.getLigen().size()+steuerung.getKORunden().size()>0) {
			GridPane sptAuswahl=new GridPane();
			sptAuswahl.setPadding(new Insets(5));
			sptAuswahl.setHgap(5);
			sptAuswahl.setVgap(5);
			gp.add(sptAuswahl, 0, 0);
			
			ComboBox<Turnierelement> TEAuswahl=new ComboBox<Turnierelement>();
			TEAuswahl.getItems().addAll(steuerung.getLigen());
			TEAuswahl.getItems().addAll(steuerung.getKORunden());
			TEAuswahl.setValue(TEAuswahl.getItems().get(0));
			sptAuswahl.add(TEAuswahl, 0, 0);
			
			createSpieltagslisteRest(TEAuswahl, sptAuswahl);
		}
	}
	
	public void createSpieltagslisteRest(ComboBox<Turnierelement> teAuswahl,GridPane sptAuswahl) {
		VBox spieltage =new VBox();
		ScrollPane spieltageSP=new ScrollPane(spieltage);
		spieltageSP.setPrefSize(200,500);
		sptAuswahl.add(spieltageSP, 0, 1);
		
		if(teAuswahl.getValue().getClass().getName().equals("de.pasch.turnierleitung"
				+ ".turnierelemente.Liga")) {
			Liga liga=(Liga)teAuswahl.getValue();
			for(Spieltag spt:liga.getSpieltage()) {
				Button name=new Button(spt.getName());
				name.setFont(Font.font(15));
				name.setPrefWidth(198);
				spieltage.getChildren().add(name);
				//name.setOnAction();
			}
			
			Button neu=new Button("Neuer Spieltag");
			neu.setFont(Font.font(15));
			sptAuswahl.add(neu, 0, 2);
			neu.setOnAction((e)->{
				new SpieltagHinzufuegen(liga,stage, steuerung, akt);
			});
			
			Button loe=new Button("Spieltag löschen");
			loe.setFont(Font.font(15));
			sptAuswahl.add(loe, 0, 3);
			loe.setOnAction((e)->{
				new ListDialog<Spieltag>(liga.getSpieltage(), stage, "Welcher Spieltag soll gelöscht werden",
						"Spieltag löschen",(f)->{
					try {
						liga.removeSpieltag(f.getID());
						akt.aktualisieren();
					}catch(IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(null,iae.getMessage(),"FEHLER!",0);
					}
				});
			});
		}else {
			KORunde kor =(KORunde)teAuswahl.getValue();
		}
	}
}
