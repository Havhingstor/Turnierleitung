package de.pasch.turnierleitung.uis;

import javax.swing.JOptionPane;

import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.steuerung.Steuerung;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HFProtagonisten {
	Stage stage;
	BorderPane bp;
	Steuerung steuerung;
	Aktualisierer akt;
	TabPane inhalt;
	Tab teamTab;
	Tab spielerTab;
	GridPane teamDetailGP;
	
	public HFProtagonisten(Stage stage,BorderPane bp,Steuerung steuerung,Aktualisierer akt) {
		this.stage=stage;
		this.bp=bp;
		this.steuerung=steuerung;
		this.akt=akt;
		inhalt=new TabPane();
		inhalt.setPadding(new Insets(5,5,5,5));
		bp.setCenter(inhalt);
		aktualisiere();
	}
	
	public void aktualisiere() {
        GridPane teamGP=createTeamInhalt();
        //GridPane spielerGP=createSpielerInhalt();
        
        inhalt.getTabs().clear();
        teamTab=new Tab("Teams",teamGP);
        teamTab.setClosable(false);
        spielerTab=new Tab("Spieler",new Label("Spieler"));
        inhalt.getTabs().addAll(teamTab,spielerTab);
        spielerTab.setClosable(false);
        
		stage.show();
	}
	
	private GridPane createTeamInhalt() {
		GridPane teamGP=new GridPane();
		teamGP.setAlignment(Pos.TOP_LEFT);
		teamGP.setPadding(new Insets(5,5,5,5));
		teamGP.setHgap(5);
		teamGP.setVgap(5);
        
    	ListView<Team> teamListe=new ListView<Team>();
        ScrollPane teamListeSP=new ScrollPane(teamListe);
        teamListe.setPrefSize(550,550);
        for(Team t:steuerung.getTeams()){
            teamListe.getItems().add(t);
        }
        teamListe.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        teamListe.getSelectionModel().selectedItemProperty().addListener((e)->{
        	aktualisiereTeamDetails(teamListe.getSelectionModel().getSelectedItem(), teamGP);
        });
		        
        HBox schaltungen=new HBox();
        Button neu=new Button("Neues Team");
        neu.setPrefSize(100,30);
        schaltungen.getChildren().add(neu);
        neu.setOnAction((e)->{
            new TeamHinzufuegen(stage,akt,steuerung);
        });
        Button loe=new Button("Team löschen");
        loe.setPrefSize(100,30);
        schaltungen.getChildren().add(loe);
        loe.setOnAction((e)->{
        	if(steuerung.getTeams().size()>0) {
        		new ListDialog<Team>(steuerung.getTeams(), stage,"Welches Team soll gelöscht werden",
        				"Team löschen", (f)->{
		            int best=JOptionPane.showConfirmDialog(null,"Wollen sie dieses Team wirklich löschen?","ACHTUNG!",2,2);
		            if (best==0){
		                try{
		                    steuerung.removeTeam(f.getID());
		                }catch(IllegalArgumentException iae){
		                   JOptionPane.showMessageDialog(null,iae.getMessage(),"Fehler",0);
		                }
		            }
		            akt.aktualisieren();
				});
        	}else {
        		JOptionPane.showMessageDialog(null, "Es ist kein Team vorhanden!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
        	}
        });
        
        teamGP.add(teamListeSP, 0, 2);
		teamGP.add(schaltungen,0,3);
		
		aktualisiereTeamDetails(null,teamGP);
		
		return teamGP;		
	}
	
	private void aktualisiereTeamDetails(Team team,GridPane teamGP) {
		teamGP.getChildren().remove(teamDetailGP);
		teamDetailGP=new GridPane();
		teamDetailGP.setPadding(new Insets(10));
		teamDetailGP.setVgap(50);
		teamDetailGP.setHgap(25);
		
		String[]beschriftungen=aktualisiereTeamDetailsBeschr(team);
		
		Label teamDetailNameLab=new Label("Name:");
		teamDetailNameLab.setFont(Font.font(25));
		Text teamDetailNameText=new Text(beschriftungen[0]);
		teamDetailNameText.setFont(Font.font(25));
		teamDetailGP.add(teamDetailNameLab, 0, 0);
		teamDetailGP.add(teamDetailNameText, 1,0);
		
		Label teamDetailKurznameLab=new Label("Kurzname:");
		teamDetailKurznameLab.setFont(Font.font(25));
		Text teamDetailKurznameText=new Text(beschriftungen[1]);
		teamDetailKurznameText.setFont(Font.font(25));
		teamDetailGP.add(teamDetailKurznameLab, 0,1);
		teamDetailGP.add(teamDetailKurznameText, 1,1);
		
		Label teamDetailStadionLab=new Label("Heimstadion:");
		teamDetailStadionLab.setFont(Font.font(25));
		Text teamDetailStadionText=new Text(beschriftungen[2]);
		teamDetailStadionText.setFont(Font.font(25));
		teamDetailGP.add(teamDetailStadionLab, 0, 2);
		teamDetailGP.add(teamDetailStadionText, 1,2);
		
		Label teamDetailKapitaenLab=new Label("Kapitän:");
		teamDetailKapitaenLab.setFont(Font.font(25));
		Text teamDetailKapitaenText=new Text(beschriftungen[3]);
		teamDetailKapitaenText.setFont(Font.font(25));
		teamDetailGP.add(teamDetailKapitaenLab, 0, 3);
		teamDetailGP.add(teamDetailKapitaenText, 1,3);
		
		Label teamDetailVizeLab=new Label("Vizekapitän:");
		teamDetailVizeLab.setFont(Font.font(25));
		Text teamDetailVizeText=new Text(beschriftungen[4]);
		teamDetailVizeText.setFont(Font.font(25));
		teamDetailGP.add(teamDetailVizeLab, 0, 4);
		teamDetailGP.add(teamDetailVizeText, 1,4);
		
		teamGP.add(teamDetailGP, 1,2);
	}
	
	private String[] aktualisiereTeamDetailsBeschr(Team team) {
		String []strings=new String[5];
		
		if(team==null) {
			for(String s:strings) {
				s="";
			}
		}else {
			strings[0]=team.getName();
			strings[1]=team.getKurzname();
			strings[2]=team.getHeimstadion();
			
			if(team.getKapitaen()!=null) {
				strings[3]=team.getKapitaen().getName();
			}else {
				strings[3]="Nicht festgelegt";
			}
			
			if(team.getVizekapitaen()!=null) {
				strings[4]=team.getVizekapitaen().getName();
			}else {
				strings[4]="Nicht festgelegt";
			}
		}
		
		return strings;
	}
}
