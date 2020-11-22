package de.pasch.turnierleitung.uis;

import java.util.ArrayList;
import java.util.Optional;

import javax.swing.JOptionPane;

import de.pasch.turnierleitung.protagonisten.Spieler;
import de.pasch.turnierleitung.protagonisten.SpielerTeamConnector;
import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.spiele.Spiel;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Steuerung;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
	long letztesTeamTeams=0;
	long letzterSpielerTeams=0;
	long letztesTeamSpieler=0;
	int letzterTab=1;
	ComboBox<Team>spielerTeamAuswahl;
	GridPane spielerGP;
	GridPane std;
	GridPane teamGP;
	HFSpiele hfs;
	
	public HFProtagonisten(Stage stage,BorderPane bp,Steuerung steuerung,Aktualisierer akt) {
		this.stage=stage;
		this.bp=bp;
		this.steuerung=steuerung;
		this.akt=akt;
		inhalt=new TabPane();
		inhalt.setPadding(new Insets(5,5,5,5));
		try {
			bp.getChildren().remove(1);
		}catch(IndexOutOfBoundsException ioobe) {}
		bp.setCenter(inhalt);
		aktualisiere(steuerung,false);
	}
	
	public void aktualisiere(Steuerung steuerung) {
		aktualisiere(steuerung,false);
	}
	
	public void aktualisiere(Steuerung steuerung,boolean other) {
		this.steuerung=steuerung;
		
		if(hfs!=null){
			hfs.aktualisiere(steuerung);
		}
		
		Tab letzterTabTab=inhalt.getSelectionModel().getSelectedItem();
		if(!other&&letzterTabTab==teamTab) {
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
		teamGP=new GridPane();
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
        		letztesTeamTeams=t.getID();
        		aktualisiereTeamDetails(t,teamGP);
        	});
        }
		        
        HBox schaltungen=new HBox();
        Button neu=new Button("Neues Team");
        neu.setPrefSize(100,30);
        schaltungen.getChildren().add(neu);
        /*
        neu.setOnAction((e)->{
            	new TeamHinzufuegen(stage, akt, steuerung,new Handler<Team>() {
            		public void handle(Team t) {
            			letztesTeamTeams=t.getID();
            		}
            	});
            });
            */
        neu.setOnAction((e)->{
        	new TeamHinzufuegen(stage, akt, steuerung,(t)->{
    			letztesTeamTeams=t.getID();
        	});
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
		if(letztesTeamTeams!=0) {
			t=IDPicker.pick(steuerung.getTeams(),letztesTeamTeams);
		}else if(steuerung.getTeams().size()>0) {
			t=steuerung.getTeams().get(0);
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
		teamDetailGP.add(teamBearbeiten, 0,6);
		teamBearbeiten.setOnAction((e)->{
			if(team!=null) {
				new TeamBearbeiten(steuerung, stage, team, akt);
			}
		});
		
		Button teamSpieler=new Button("Spieler");
		teamSpieler.setFont(Font.font(15));
		teamSpieler.setPrefSize(100,50);
		teamDetailGP.add(teamSpieler, 1,6);
		teamSpieler.setOnAction((e)->{
				if(team!=null) {
				inhalt.getSelectionModel().select(1);
				spielerTeamAuswahl.setValue(team);
				aktualisiereSpielerTeamDetails(team,spielerGP);
			}
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
		if(letztesTeamSpieler==0) {
			spielerTeamAuswahl.setValue(steuerung.getTeams().get(0));
		}else {
			spielerTeamAuswahl.setValue(IDPicker.pick
					(steuerung.getTeams(),letztesTeamSpieler));
		}
		
    	VBox spielerListeBox=new VBox();
        ScrollPane spielerListeSP=new ScrollPane(spielerListeBox);
        spielerListeBox.setPrefSize(370,500);
        aktualisiereSpielerListe(spielerTeamAuswahl.getValue(), spielerListeBox, spielerGP);

        spielerTeamAuswahl.setOnAction((e)->{
        	letztesTeamSpieler=spielerTeamAuswahl.getValue().getID();
			aktualisiereSpielerListe(spielerTeamAuswahl.getValue(), spielerListeBox, spielerGP);
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
        spielerGP.add(spielerListeSP, 1, 1);
        spielerGP.add(schaltungen,1,2);
		
		Spieler s=null;
		long team=spielerTeamAuswahl.getValue().getID();
		if(letzterSpielerTeams!=0) {
			s=IDPicker.pick(steuerung.getSpieler(),letzterSpielerTeams);
		}else if(steuerung.getAktiveSpielerEinesTeams(team).size()>0){
			s=steuerung.getAktiveSpielerEinesTeams(team).get(0);
		}
		aktualisiereSpielerDetails(s,spielerGP); 
		
		return spielerGP;		
	}
	
	private void aktualisiereSpielerListe(Team team,VBox spielerListeBox,
			GridPane spielerGP) {
		spielerListeBox.getChildren().clear();
		 for(Spieler s:steuerung.getAktiveSpielerEinesTeams(team.getID()
				 )){
	        	Button spielerButton=new Button(s.getName());
	        	spielerButton.setPrefWidth(370);
	        	spielerListeBox.getChildren().add(spielerButton);
	        	spielerButton.setOnAction((e)->{
	        		letzterSpielerTeams=s.getID();
	        		aktualisiereSpielerDetails(s,spielerGP);
	        	});
	     }
		 aktualisiereSpielerTeamDetails(team, spielerGP);
	}
	
	private void aktualisiereSpielerDetails(Spieler spieler,GridPane spielerGP) {
		spielerGP.getChildren().remove(spielerDetailGP);
		spielerDetailGP=new GridPane();
		spielerDetailGP.setPadding(new Insets(10));
		spielerDetailGP.setVgap(50);
		spielerDetailGP.setHgap(25);
		spielerDetailGP.setPrefWidth(370);
		
		String[]beschriftungen=aktualisiereSpielerDetailsBeschr(spieler);
		
		Label spielerDetailVornameLab=new Label("Vorname:");
		spielerDetailVornameLab.setFont(Font.font(20));
		Text spielerDetailVornameText=new Text(beschriftungen[0]);
		spielerDetailVornameText.setFont(Font.font(20));
		spielerDetailGP.add(spielerDetailVornameLab, 0, 0);
		spielerDetailGP.add(spielerDetailVornameText, 1,0);
		
		Label spielerDetailNachnameLab=new Label("Nachname:");
		spielerDetailNachnameLab.setFont(Font.font(20));
		Text teamDetailKurznameText=new Text(beschriftungen[1]);
		teamDetailKurznameText.setFont(Font.font(20));
		spielerDetailGP.add(spielerDetailNachnameLab, 0,1);
		spielerDetailGP.add(teamDetailKurznameText, 1,1);
		
		Label spielerDetailTeamLab=new Label("Team:");
		spielerDetailTeamLab.setFont(Font.font(20));
		Text spielerDetailTeamText=new Text(beschriftungen[2]);
		spielerDetailTeamText.setFont(Font.font(20));
		spielerDetailGP.add(spielerDetailTeamLab, 0, 2);
		spielerDetailGP.add(spielerDetailTeamText, 1,2);
		
		Label spielerDetailKapitaenLab=new Label("Kapitän:");
		spielerDetailKapitaenLab.setFont(Font.font(20));
		Text spielerDetailKapitaenText=new Text(beschriftungen[3]);
		spielerDetailKapitaenText.setFont(Font.font(20));
		spielerDetailGP.add(spielerDetailKapitaenLab, 0, 3);
		spielerDetailGP.add(spielerDetailKapitaenText, 1,3);
		
		Label spielerDetailTrikotNLab=new Label("Trikotnummer:");
		spielerDetailTrikotNLab.setFont(Font.font(20));
		Text spielerDetailTrikotNText=new Text(beschriftungen[4]);
		spielerDetailTrikotNText.setFont(Font.font(20));
		spielerDetailGP.add(spielerDetailTrikotNLab, 0, 4);
		spielerDetailGP.add(spielerDetailTrikotNText, 1,4);
				
		Button spielerBearbeiten=new Button("Bearbeiten");
		spielerBearbeiten.setFont(Font.font(15));
		spielerBearbeiten.setPrefSize(100,50);
		spielerDetailGP.add(spielerBearbeiten, 0,5);
		spielerBearbeiten.setOnAction((e)->{
			if(spieler!=null) {
				new SpielerBearbeiten(steuerung, stage, spieler, akt);
			}
		});
		
		Button spielerWechsel=new Button("Wechsel");
		spielerWechsel.setFont(Font.font(15));
		spielerWechsel.setPrefSize(100,50);
		spielerDetailGP.add(spielerWechsel,1,5);
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
    	                	if(iae.getMessage().charAt(0)=='A') {
    	                		ButtonType zuSp=new ButtonType("Spiel anzeigen");
    	                		Alert al=new Alert(AlertType.ERROR);
    	                		al.getButtonTypes().add(zuSp);
    	                		al.setTitle("Fehler");
    	                		al.setHeaderText(null);
    	                		al.setContentText("Der Spieler ist in einem Spiel gelistet, welches noch nicht abgeschlossen ist."
    	                				+ " Deshalb kann er gerade nicht gewechselt werden!");
    	                		al.setHeight(200);
    	                		Optional<ButtonType>btn=al.showAndWait();
    	                		if(btn.get().equals(zuSp)) {
    	                			Spiel sp=IDPicker.pick(steuerung.getSpiele(),Long.valueOf(iae.getMessage().substring(1)));
    	                			hfs=new HFSpiele(steuerung.getLSVonSpiel(sp.getID()).getKey(),steuerung.getLSVonSpiel(sp.getID()).getValue(),sp, stage, bp, steuerung, akt);
    	                		}
    	                	}else {
    	                		JOptionPane.showMessageDialog(null,iae.getMessage(),"Fehler",0);
    	                	}
    	                }
    	            }
        	});
        });
		
		spielerGP.add(spielerDetailGP, 2,1);
	}
	
	private String[] aktualisiereSpielerDetailsBeschr(Spieler spieler){
		 String[]strings=new String[5];
		 
		 if(spieler==null) {
			 strings[0]="";
			 strings[1]="";
			 strings[1]="";
			 strings[3]="";
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
			 int trikotnummer=steuerung.getTrikotnummerEinesSpielers(spieler.getID());
			 if(trikotnummer>0) {
				 strings[4]=""+trikotnummer;
			 }else {
				 strings[4]="-------";
			 }
		 }
		 
		 return strings;
	}
	
	private void aktualisiereSpielerTeamDetails(Team team,GridPane spielerGP) {
		spielerGP.getChildren().remove(std);
		std=new GridPane();
		std.setPadding(new Insets(10));
		std.setVgap(50);
		std.setHgap(25);
		std.setPrefWidth(370);
		
		String teamName;
		if(team.getKurzname().length()>0) {
			teamName=team.getKurzname();
		}else {
			teamName=team.getName();
		}
		Text titel=new Text(teamName+"-Spieler");
		titel.setFont(Font.font(25));
		titel.setOnMouseClicked((e)->{
			inhalt.getSelectionModel().select(0);
			aktualisiereTeamDetails(team,teamGP);
		});
		std.add(titel, 0, 0);
		Label kapitaenLab=new Label("Kapitän");
		kapitaenLab.setFont(Font.font(20));
		std.add(kapitaenLab, 0, 1);
		String kaptString="Nicht gewählt";
		if(team.getKapitaen()!=null) {
			kaptString=team.getKapitaen().getName();
		}
		Text kapitaenText=new Text(kaptString);
		kapitaenText.setFont(Font.font(20));
		std.add(kapitaenText, 1, 1);
		
		Label vizekapitaenLab=new Label("Vizekapitän");
		vizekapitaenLab.setFont(Font.font(20));
		std.add(vizekapitaenLab, 0, 2);
		String vizekaptString="Nicht gewählt";
		if(team.getVizekapitaen()!=null) {
			vizekaptString=team.getVizekapitaen().getName();
		}
		Text vizekapitaenText=new Text(vizekaptString);
		vizekapitaenText.setFont(Font.font(20));
		std.add(vizekapitaenText, 1, 2);
		
		Button kapitaenSetzen=new Button("Kapitän bestimmen");
		kapitaenSetzen.setFont(Font.font(15));
		kapitaenSetzen.setOnAction((e)->{
			if(steuerung.getAktiveSpielerEinesTeams(team.getID()).size()>0) {
				new ListDialog<Spieler>(steuerung.getAktiveSpielerEinesTeams(team.getID()),stage,
						300,"Welcher Spieler soll zum Kapitän werden?",
						"Kapitän bestimmen",(f)->{
							team.setKapitaen(f);
							if(team.getVizekapitaen()==f) {
								team.setVizekapitaen(0);
							}
							akt.aktualisieren();
						});
			}else {
				JOptionPane.showMessageDialog(null, "In diesem Team ist kein Spieler eingetragen!","FEHLER!",0);
			}
		});
		std.add(kapitaenSetzen,0,3,2,1);
		
		Button vizekapitaenSetzen=new Button("Vizekapitän bestimmen");
		vizekapitaenSetzen.setFont(Font.font(15));
		vizekapitaenSetzen.setOnAction((e)->{
			if(steuerung.getAktiveSpielerEinesTeams(team.getID()).size()>0) {
				new ListDialog<Spieler>(steuerung.getAktiveSpielerEinesTeams(team.getID()),
						stage,300,"Welcher Spieler soll zum Vizekapitän werden?",
						"Vizekapitän bestimmen",(f)->{
							team.setVizekapitaen(f);
							if(team.getKapitaen()==f) {
								team.setKapitaen(0);
							}
							akt.aktualisieren();
						});
			}else {
				JOptionPane.showMessageDialog(null, "In diesem Team ist kein Spieler eingetragen!","FEHLER!",0);
			}
		});
		std.add(vizekapitaenSetzen,0,4,2,1);
		
		Button trikotnummern=new Button("Trinkotnummern");
		trikotnummern.setFont(Font.font(15));
		trikotnummern.setOnAction((e)->{
			new Trikotnummerzuweiser(steuerung, team, stage, akt);	
		});
		std.add(trikotnummern, 0,5,2,1);
		
		spielerGP.add(std,0, 1);
	}
	
	public HFProtagonisten(Team team,Stage stage,BorderPane bp,Steuerung steuerung,Aktualisierer akt) {
		this.stage=stage;
		this.bp=bp;
		this.steuerung=steuerung;
		this.akt=akt;
		inhalt=new TabPane();
		inhalt.setPadding(new Insets(5,5,5,5));
		try {
			bp.getChildren().remove(1);
		}catch(IndexOutOfBoundsException ioobe) {}
		bp.setCenter(inhalt);
		letztesTeamTeams=team.getID();
		aktualisiere(steuerung,false);
	}
	
	public HFProtagonisten(Spieler spieler,Stage stage,BorderPane bp,Steuerung steuerung,Aktualisierer akt) {
		this.stage=stage;
		this.bp=bp;
		this.steuerung=steuerung;
		this.akt=akt;
		inhalt=new TabPane();
		inhalt.setPadding(new Insets(5,5,5,5));
		try {
			bp.getChildren().remove(1);
		}catch(IndexOutOfBoundsException ioobe) {}
		bp.setCenter(inhalt);
		letzterSpielerTeams=spieler.getID();
		letztesTeamSpieler=steuerung.getAktivesTeamEinesSpielers(spieler.getID()).getID();
		aktualisiere(steuerung,true);
	}
}
