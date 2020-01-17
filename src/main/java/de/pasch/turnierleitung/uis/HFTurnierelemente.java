package de.pasch.turnierleitung.uis;

import java.util.ArrayList;
import java.util.Collections;

import de.pasch.turnierleitung.steuerung.Steuerung;
import de.pasch.turnierleitung.turnierelemente.KORunde;
import de.pasch.turnierleitung.turnierelemente.Liga;
import de.pasch.turnierleitung.turnierelemente.Turnierelement;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HFTurnierelemente {
	Stage stage;
	BorderPane bp;
	Steuerung steuerung;
	Aktualisierer akt;
	GridPane gp;
	long letztesTE;
	
	public HFTurnierelemente(Stage stage,BorderPane bp,Steuerung steuerung,Aktualisierer akt) {
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
		
		ArrayList<Turnierelement>tes=new ArrayList<Turnierelement>();
		tes.addAll(steuerung.getKORunden());
		tes.addAll(steuerung.getLigen());
		Collections.sort(tes);
		
		VBox listeBox=new VBox();
        ScrollPane listeSP=new ScrollPane(listeBox);
        listeBox.setPrefSize(550,550);
        for(Turnierelement t:tes){
        	Button teamButton=new Button(t.getName());
        	teamButton.setPrefWidth(550);
        	listeBox.getChildren().add(teamButton);
        	teamButton.setOnAction((e)->{
        		letztesTE=t.getID();
        		aktualisiereTEDetails(t,gp);
        	});
        }
        
        gp.add(listeSP, 0, 0);
		
		stage.show();
	}
	
	private void aktualisiereTEDetails(Turnierelement t,GridPane gp) {
		String[]beschriftungen=TEDetailsBeschriftungen(t);
		
		GridPane detGP=new GridPane();
		detGP.setPadding(new Insets(5));
		detGP.setVgap(5);
		detGP.setHgap(5);
		
		Label typName=new Label("Typ");
		detGP.add(typName, 0, 0);
		Label typText=new Label(beschriftungen[0]);
		detGP.add(typText, 0, 1);
	}
	
	private String[] TEDetailsBeschriftungen(Turnierelement t) {
		String[]beschr=null;
		if(t.getClass().getName().equals("Liga")) {
			beschr=new String[10];
			Liga liga=(Liga)t;
			beschr[0]="Liga";
			beschr[1]=liga.getName();
			beschr[2]=""+liga.getPunkteProSieg();
			beschr[3]=""+liga.getPunkteProUnentschieden();
			beschr[4]=""+liga.getPunkteProNiederlage();
			int[]rk=liga.getReihenfolgeKriterien();
			for(int i=0;i<4;i++) {
				int wahl=rk[i];
				switch(wahl) {
				case 1:
					beschr[i+5]="Punkte";
					break;
				case 2:
					beschr[i+5]="Tordifferenz";
					break;
				case 3:
					beschr[i+5]="Tore";
					break;
				case 4:
					beschr[i+5]="Gegentore";
					break;
				case 5:
					beschr[i+5]="Spiele";
					break;
				}
			}
		}else {
			beschr=new String[2];
			KORunde kor=(KORunde)t;
			beschr[0]="KO-Runde";
			beschr[1]=kor.getName();
		}
		
		return beschr;
	}
}
