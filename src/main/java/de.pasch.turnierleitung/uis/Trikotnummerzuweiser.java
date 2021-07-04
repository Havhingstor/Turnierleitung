package de.pasch.turnierleitung.uis;

import java.util.ArrayList;
import java.util.Arrays;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.steuerung.Steuerung;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Trikotnummerzuweiser {
	Team team;
	Steuerung steuerung;
	Aktualisierer akt;
	Stage stage;
	GridPane gp;
	
	public Trikotnummerzuweiser(Steuerung steuerung,Team team,Stage primStage, Aktualisierer akt) {
		this.steuerung=steuerung;
		this.team=team;
		this.akt=akt;
		
		stage=new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(primStage);
		stage.setTitle("Trikotnummern ändern");
		gp=new GridPane();
		gp.setAlignment(Pos.TOP_CENTER);
		gp.setPadding(new Insets(5));
		gp.setHgap(5);
		gp.setVgap(5);
		
		aktualisiere();
		
		Scene scene=new Scene(gp,400,300);
		stage.setScene(scene);
		stage.show();
	}
	
	private void aktualisiere() {
		gp.getChildren().clear();
		int zahlenmenge;
		ArrayList<Spieler>spieler=steuerung.getAktiveSpielerEinesTeams(team.getID());
		if(spieler.size()<100) {
			zahlenmenge=99;
		}else {
			zahlenmenge=999;
		}
		TKNummerString[]nummern=new TKNummerString[zahlenmenge];
		nummernSetzen(nummern,spieler);
		VBox spielerNummern=new VBox();
		spielerNummern.setPrefSize(350,250);
		for(Spieler s:spieler) {
			HBox box=new HBox();
			Button name=new Button(s.getName());
			name.setPrefWidth(300);
			box.getChildren().add(name);
			Label nummer=new Label("           "+
			steuerung.getTrikotnummerEinesSpielersString(s.getID()));
			nummer.setPrefWidth(50);
			box.getChildren().add(nummer);
			spielerNummern.getChildren().add(box);
			name.setOnAction((e)->{
				eingabeDialog(nummern,s,spieler);
			});
		}
		
		Text ueberschrift=new Text("Wählen Sie einen Spieler aus.");
		ueberschrift.setFont(Font.font(20));
		gp.add(ueberschrift, 0, 0);
				
		ScrollPane sp=new ScrollPane(spielerNummern);
		gp.add(sp, 0, 1);
	}
	
	public void nummernSetzen(TKNummerString[]nummern,ArrayList<Spieler>spieler) {
		for(int i=0;i<nummern.length;i++) {
			nummern[i]=new TKNummerString();
			nummern[i].nummer=(i+1);
			nummern[i].botschaft="Frei";
			for(Spieler s:spieler) {
				if(steuerung.getTrikotnummerEinesSpielers(s.getID())==(i+1)) {
					nummern[i].botschaft="Belegt von "+s.getName();
				}
			}
		}
	}
	
	public void eingabeDialog(TKNummerString[]nummern,Spieler s,ArrayList<Spieler>spieler) {
		new ListDialog<TKNummerString>(Arrays.asList(nummern),stage,350,"Welche Nummer soll der Spieler bekommen?",
				"Nummer zuweisen", (e)->{
					if(!e.botschaft.equals("Frei")) {
						for(Spieler sp:spieler ) {
							if(steuerung.getTrikotnummerEinesSpielers(sp.getID())==e.nummer) {
								steuerung.editSpieler(sp.getVorname(), sp.getNachname(), 0, steuerung.getAktivesTeamEinesSpielers(sp.getID()).getID(), sp.getID());
							}
						}
					}
					steuerung.editSpieler(s.getVorname(), s.getNachname(), e.nummer, steuerung.getAktivesTeamEinesSpielers(s.getID()).getID()
							, s.getID());
					aktualisiere();
					akt.aktualisieren();
		});
	}
}

class TKNummerString{
	int nummer;
	String botschaft;
	
	@Override
	public String toString() {
		return nummer+" "+botschaft;
	}
}
