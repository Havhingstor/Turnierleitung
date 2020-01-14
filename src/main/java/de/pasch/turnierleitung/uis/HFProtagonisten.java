package de.pasch.turnierleitung.uis;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.protagonisten.SpielerTeamConnector;
import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Steuerung;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
	GridPane spielerDetailGP;
	long letztesTeam=0;
	long letzterSpieler=0;
	int letzterTab=1;
	ComboBox<Team>spielerTeamAuswahl;
	GridPane spielerGP;
	
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
		Tab letzterTabTab=inhalt.getSelectionModel().getSelectedItem();
		if(letzterTabTab==teamTab) {
			letzterTab=0;
		}else {
			letzterTab=1;
		}
        GridPane teamGP=createTeamInhalt();
        GridPane spielerGP;
        if(steuerung.getTeams().size()>0) {
        	spielerGP=createSpielerInhalt();
        }else {
        	spielerGP=new GridPane();
        	spielerGP.setAlignment(Pos.CENTER);
	        Text fehlerText=new Text("Es ist kein Team eingetragen. Deshalb können keine Spieler "
	        		+ "hinzugefügt werden!");
	        fehlerText.setFont(Font.font(20));
	        spielerGP.add(fehlerText, 0, 0);
        }
        
        
        inhalt.getTabs().clear();
        teamTab=new Tab("Teams",teamGP);
        teamTab.setClosable(false);
        
        spielerTab=new Tab("Spieler",spielerGP);
        inhalt.getTabs().addAll(teamTab,spielerTab);
        spielerTab.setClosable(false);
        
        inhalt.getSelectionModel().select(letzterTab);
        
		stage.show();
	}
	
	private GridPane createTeamInhalt() {
		GridPane teamGP=new GridPane();
		teamGP.setAlignment(Pos.TOP_LEFT);
		teamGP.setPadding(new Insets(5,5,5,5));
		teamGP.setHgap(5);
		teamGP.setVgap(5);
        
    	VBox teamListeBox=new VBox();
        ScrollPane teamListeSP=new ScrollPane(teamListeBox);
        teamListeBox.setPrefSize(550,550);
        for(Team t:steuerung.getTeams()){
        	Button teamButton=new Button(t.getName());
        	teamButton.setPrefWidth(550);
        	teamListeBox.getChildren().add(teamButton);
        	teamButton.setOnAction((e)->{
        		letztesTeam=t.getID();
        		aktualisiereTeamDetails(t,teamGP);
        	});
        }
		        
        HBox schaltungen=new HBox();
        Button neu=new Button("Neues Team");
        neu.setPrefSize(100,30);
        schaltungen.getChildren().add(neu);
        neu.setOnAction((e)->{
            new TeamHinzufuegen(stage,akt,steuerung);
        });
        Button loe=new Button("Team löschen" );
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
        
        teamGP.add(teamListeSP, 0, 0);
		teamGP.add(schaltungen,0,1);
		
		Team t=null;
		if(letztesTeam!=0) {
			t=IDPicker.pick(steuerung.getTeams(),letztesTeam);
		}
		aktualisiereTeamDetails(t,teamGP);
		
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
		
		Button teamBearbeiten=new Button("Bearbeiten");
		teamBearbeiten.setFont(Font.font(15));
		teamBearbeiten.setPrefSize(100,50);
		teamDetailGP.add(teamBearbeiten, 0,5);
		teamBearbeiten.setOnAction((e)->{
			if(team!=null) {
				new TeamBearbeiten(steuerung, stage, team, akt);
			}
		});
		
		Button teamSpieler=new Button("Spieler");
		teamSpieler.setFont(Font.font(15));
		teamSpieler.setPrefSize(100,50);
		teamDetailGP.add(teamSpieler, 1,5);
		teamSpieler.setOnAction((e)->{
			inhalt.getSelectionModel().select(1);
			spielerTeamAuswahl.setValue(team);
			//aktualisiereSpielerTeamDetails(team,spielerGP);
		});
		
		teamGP.add(teamDetailGP, 1,0);
	}
	
	private String[] aktualisiereTeamDetailsBeschr(Team team) {
		String []strings=new String[5];
		
		if(team==null) {
			strings[0]="";
			strings[1]="";
			strings[2]="";
			strings[3]="";
			strings[4]="";
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
	
	private GridPane createSpielerInhalt() {
		spielerGP=new GridPane();
		spielerGP.setAlignment(Pos.TOP_LEFT);
		spielerGP.setPadding(new Insets(5,5,5,5));
		spielerGP.setHgap(5);
		spielerGP.setVgap(5);
        
		spielerTeamAuswahl=new ComboBox<Team>();
		for(Team t:steuerung.getTeams()) {
			spielerTeamAuswahl.getItems().add(t);
		}
		spielerTeamAuswahl.setValue(steuerung.getTeams().get(0));
		
    	VBox spielerListeBox=new VBox();
        ScrollPane spielerListeSP=new ScrollPane(spielerListeBox);
        spielerListeBox.setPrefSize(250,500);
        aktualisiereSpielerListe(spielerTeamAuswahl.getValue().getID(), spielerListeBox, spielerGP);

        spielerTeamAuswahl.setOnAction((e)->{
			aktualisiereSpielerListe(spielerTeamAuswahl.getValue().getID(), spielerListeBox, spielerGP);
		});
		        
        HBox schaltungen=new HBox();
        Button neu=new Button("Neuer Spieler");
        neu.setPrefSize(100,30);
        schaltungen.getChildren().add(neu);
        neu.setOnAction((e)->{
            new SpielerHinzufuegen(stage,spielerTeamAuswahl.getValue().getID(),akt,steuerung);
        });
        Button loe=new Button("Spieler löschen" );
        loe.setPrefSize(100,30);
        schaltungen.getChildren().add(loe);
        loe.setOnAction((e)->{
        	if(steuerung.getAktiveSpielerEinesTeams(spielerTeamAuswahl.getValue().getID()).size()>0) {
        		new ListDialog<Spieler>(steuerung.getAktiveSpielerEinesTeams(spielerTeamAuswahl.getValue().getID())
        				, stage,"Welcher Spieler soll gelöscht werden",
        				"Spieler löschen", (f)->{
		            int best=JOptionPane.showConfirmDialog(null,"Wollen sie diesen Spieler wirklich löschen?","ACHTUNG!",2,2);
		            if (best==0){
		                try{
		                    steuerung.removeSpieler(f.getID());
		                }catch(IllegalArgumentException iae){
		                   JOptionPane.showMessageDialog(null,iae.getMessage(),"Fehler",0);
		                }
		            }
		            akt.aktualisieren();
				});
        	}else {
        		JOptionPane.showMessageDialog(null, "Es ist kein Spieler vorhanden!", "FEHLER!", JOptionPane.ERROR_MESSAGE);
        	}
        });
        
        spielerGP.add(spielerTeamAuswahl, 0,0);
        spielerGP.add(spielerListeSP, 0, 1);
        spielerGP.add(schaltungen,0,2);
		
		Spieler s=null;
		if(letzterSpieler!=0) {
			s=IDPicker.pick(steuerung.getSpieler(),letzterSpieler);
		}else {
			
		}
		aktualisiereSpielerDetails(s,spielerGP); 
		
		return spielerGP;		
	}
	
	private void aktualisiereSpielerListe(long ID,VBox spielerListeBox,
			GridPane spielerGP) {
		spielerListeBox.getChildren().clear();
		 for(Spieler s:steuerung.getAktiveSpielerEinesTeams(ID)){
	        	Button spielerButton=new Button(s.getName());
	        	spielerButton.setPrefWidth(250);
	        	spielerListeBox.getChildren().add(spielerButton);
	        	spielerButton.setOnAction((e)->{
	        		letzterSpieler=s.getID();
	        		aktualisiereSpielerDetails(s,spielerGP);
	        	});
	        }
	}
	
	private void aktualisiereSpielerDetails(Spieler spieler,GridPane spielerGP) {
		spielerGP.getChildren().remove(spielerDetailGP);
		spielerDetailGP=new GridPane();
		spielerDetailGP.setPadding(new Insets(10));
		spielerDetailGP.setVgap(50);
		spielerDetailGP.setHgap(25);
		
		String[]beschriftungen=aktualisiereSpielerDetailsBeschr(spieler);
		
		Label spielerDetailVornameLab=new Label("Vorname:");
		spielerDetailVornameLab.setFont(Font.font(25));
		Text spielerDetailVornameText=new Text(beschriftungen[0]);
		spielerDetailVornameText.setFont(Font.font(25));
		spielerDetailGP.add(spielerDetailVornameLab, 0, 0);
		spielerDetailGP.add(spielerDetailVornameText, 1,0);
		
		Label spielerDetailNachnameLab=new Label("Nachname:");
		spielerDetailNachnameLab.setFont(Font.font(25));
		Text teamDetailKurznameText=new Text(beschriftungen[1]);
		teamDetailKurznameText.setFont(Font.font(25));
		spielerDetailGP.add(spielerDetailNachnameLab, 0,1);
		spielerDetailGP.add(teamDetailKurznameText, 1,1);
		
		Label spielerDetailTeamLab=new Label("Team:");
		spielerDetailTeamLab.setFont(Font.font(25));
		Text spielerDetailTeamText=new Text(beschriftungen[2]);
		spielerDetailTeamText.setFont(Font.font(25));
		spielerDetailGP.add(spielerDetailTeamLab, 0, 2);
		spielerDetailGP.add(spielerDetailTeamText, 1,2);
		
		Label spielerDetailKapitaenLab=new Label("Kapitän:");
		spielerDetailKapitaenLab.setFont(Font.font(25));
		Text spielerDetailKapitaenText=new Text(beschriftungen[3]);
		spielerDetailKapitaenText.setFont(Font.font(25));
		spielerDetailGP.add(spielerDetailKapitaenLab, 0, 3);
		spielerDetailGP.add(spielerDetailKapitaenText, 1,3);
				
		Button spielerBearbeiten=new Button("Bearbeiten");
		spielerBearbeiten.setFont(Font.font(15));
		spielerBearbeiten.setPrefSize(100,50);
		spielerDetailGP.add(spielerBearbeiten, 0,4);
		spielerBearbeiten.setOnAction((e)->{
			if(spieler!=null) {
				new SpielerBearbeiten(steuerung, stage, spieler, akt);
			}
		});
		
		Button spielerWechsel=new Button("Wechsel");
		spielerWechsel.setFont(Font.font(15));
		spielerWechsel.setPrefSize(100,50);
		spielerDetailGP.add(spielerWechsel,1,4);
        spielerWechsel.setOnAction((e)->{
        	ArrayList<Team>teams=new ArrayList<Team>();
        	Team team = null;
            for(SpielerTeamConnector stc:steuerung.getSTC()){
                if(stc.getSpielerID()==spieler.getID()&&(!stc.getAusgetreten())){
                 team=IDPicker.pick(steuerung.getTeams(),stc.getTeamID());   
                }
            }

        	for(Team team1:steuerung.getTeams()) {
        		if(!team1.equals(team)) {
        			teams.add(team1);
        		}
        	}
        	new ListDialog<Team>(teams, stage, "Wohin soll der Spieler wechseln?",
        			"Wechsel",(f)->{
    				int best=JOptionPane.showConfirmDialog(null,"Soll dieser Spieler wirklich wechseln?","ACHTUNG!",2,2);
    	            if (best==0){
    	                try{
    		        		steuerung.editSpieler(spieler.getVorname(), spieler.getNachname(),
    		        				0, f.getID(),spieler.getID());
    		        		akt.aktualisieren();
    	                }catch(IllegalArgumentException iae){
    	                   JOptionPane.showMessageDialog(null,iae.getMessage(),"Fehler",0);
    	                }
    	            }
        	});
        });
		
		spielerGP.add(spielerDetailGP, 2,1);
	}
	
	private String[] aktualisiereSpielerDetailsBeschr(Spieler spieler){
		 String[]strings=new String[4];
		 
		 if(spieler==null) {
			 strings[0]="";
			 strings[1]="";
			 strings[1]="";
			 strings[3]="";
		 }else {
			 strings[0]=spieler.getVorname();
			 strings[1]=spieler.getNachname();
			 Team team=steuerung.getAktivesTeamEinesSpielers(spieler.getID());
			 strings[2]=team.getName();
			 if(team.getKapitaen()==spieler) {
				 strings[3]="Kapitän";
			 }else if(team.getVizekapitaen()==spieler) {
				 strings[3]="Vizekapitän";
			 }else {
				 strings[3]="Nein";
			 }
		 }
		 
		 return strings;
	}
	
	private void createSpielerTeamDetails(Team team,GridPane spielerGP) {
		
	}
}
