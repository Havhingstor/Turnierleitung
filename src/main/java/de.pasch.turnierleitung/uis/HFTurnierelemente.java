package de.pasch.turnierleitung.uis;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JOptionPane;

import de.pasch.turnierleitung.protagonisten.Team;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Steuerung;
import de.pasch.turnierleitung.turnierelemente.KORunde;
import de.pasch.turnierleitung.turnierelemente.Liga;
import de.pasch.turnierleitung.turnierelemente.Turnierelement;
import de.pasch.turnierleitung.turnierelemente.VereinInTabelle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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

public class HFTurnierelemente {
	Stage stage;
	BorderPane bp;
	Steuerung steuerung;
	Aktualisierer akt;
	GridPane gp;
	long letztesTE;
	GridPane detGP;
	TabPane inhalt;
	HFProtagonisten hfp;
	Tab uebersichtTab,tabelleTab;
	int letzterTab=0;
	HFSpiele hfs;
	HFSpieltag hfst;
	
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
		
		aktualisiere(steuerung);
	}
	
	public HFTurnierelemente(Turnierelement te,Stage stage,BorderPane bp,Steuerung steuerung,Aktualisierer akt) {
		this.steuerung=steuerung;
		this.bp=bp;
		this.stage=stage;
		this.akt=akt;
		
		letztesTE=te.getID();
		if(te.isLiga()) {
			letzterTab=1;
		}
		
		gp=new GridPane();
		gp.setPadding(new Insets(5));
		gp.setVgap(5);
		gp.setHgap(5);
		
		try {
			bp.getChildren().remove(1);
		}catch(IndexOutOfBoundsException ioobe) {}
		bp.setCenter(gp);
		
		aktualisiere(steuerung);
	}
	
	public void aktualisiere(Steuerung steuerung) {
		this.steuerung=steuerung;
		
		if(hfs!=null) {
			hfs.aktualisiere(steuerung);
		}
		if(hfst!=null) {
			hfst.aktualisiere(steuerung);
		}
		
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
        HBox schaltungen=new HBox();
        Button neu=new Button("Turnierelement hinzufügen");
        schaltungen.getChildren().add(neu);
        neu.setOnAction((e)->{
        	new TEHinzufuegen(steuerung, stage,akt);
        });
        
        Button loe=new Button("Turnierelement löschen");
        schaltungen.getChildren().add(loe);
        loe.setOnAction((e)->{
        	try {
	        	new ListDialog<Turnierelement>(tes, stage,300,"Welches Turnierelement soll gelöscht werden?",
	        			"Turnierelement löschen", (f)->{
					int best=JOptionPane.showConfirmDialog(null, "Soll das Turnierelement wirklich gelöscht werden?",
	    					"Bestätigung",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
	    			if(best==JOptionPane.OK_OPTION) {
	    				if(f.getClass().getName().equals("de.pasch.turnierleitung.turnierelemente.Liga")) {
	    					try {
	    						steuerung.removeLiga(f.getID());
	    						akt.aktualisieren();
	    					}catch(IllegalArgumentException iae) {
	    						JOptionPane.showMessageDialog(null,iae.getMessage(), "FEHLER!",JOptionPane.ERROR_MESSAGE);
	    					}
	            		}else {
	            			try {
	    						steuerung.removeKORunde(f.getID());
	    						akt.aktualisieren();
	    					}catch(IllegalArgumentException iae) {
	    						JOptionPane.showMessageDialog(null,iae.getMessage(), "FEHLER!",JOptionPane.ERROR_MESSAGE);
	    					}
	            		}
	    			}
	        	});
        	}catch(IllegalArgumentException iae) {
        		JOptionPane.showMessageDialog(null,iae.getMessage(),"FEHLER!",0);
        	}
        });
        
        gp.add(listeSP, 0, 0);
        gp.add(schaltungen, 0, 1);
        
        Turnierelement te=IDPicker.pick(tes,letztesTE);
        if(te==null) {
        	try {
        		te=tes.get(0);
        	}catch(IndexOutOfBoundsException ioobe) {}
        }
        aktualisiereTEDetails(te,gp);
		
        if(hfp!=null) {
        	hfp.aktualisiere(steuerung);
        }
        
		stage.show();
	}
	
	private void aktualisiereTEDetails(Turnierelement t,GridPane gp) {
		String[]beschriftungen=TEDetailsBeschriftungen(t);
				
		gp.getChildren().removeAll(inhalt,detGP);
		
		if(t!=null) {
			detGP=new GridPane();
			detGP.setPadding(new Insets(5));
			detGP.setVgap(5);
			detGP.setHgap(5);
			
			Label typName=new Label("Typ");
			typName.setFont(Font.font(20));
			detGP.add(typName, 0, 0);
			Label typText=new Label(beschriftungen[0]);
			typText.setFont(Font.font(20));
			detGP.add(typText, 1, 0);
			
			Label nameName=new Label("Name");
			nameName.setFont(Font.font(20));
			detGP.add(nameName, 0, 1);
			Label nameText=new Label(beschriftungen[1]);
			nameText.setFont(Font.font(20));
			detGP.add(nameText, 1, 1);
			
			int bearbeitenY=0;
					
			if(t.isLiga()) {
				bearbeitenY=5;
				inhalt=new TabPane();
				inhalt.setPrefWidth(630);
				int letzterTabZwischen=letzterTab;
				inhalt.getSelectionModel().selectedItemProperty().addListener((e)->{
					letzterTab=inhalt.getSelectionModel().getSelectedIndex();
				});
				
				Tab uebersichtTab=new Tab("Übersicht",detGP);
				uebersichtTab.setClosable(false);
				inhalt.getTabs().add(uebersichtTab);
				letzterTab=letzterTabZwischen;
				
				Liga liga=(Liga)t;
				createZusaetzlichenInhalt(inhalt,liga);
				
				Label ppsName=new Label("Punkte pro Sieg");
				ppsName.setFont(Font.font(20));
				detGP.add(ppsName, 0, 2);
				Label ppsText=new Label(beschriftungen[2]);
				ppsText.setFont(Font.font(20));
				detGP.add(ppsText, 1,2);
				
				Label ppuName=new Label("Punkte pro Unentschieden");
				ppuName.setFont(Font.font(20));
				detGP.add(ppuName, 0, 3);
				Label ppuText=new Label(beschriftungen[3]);
				ppuText.setFont(Font.font(20));
				detGP.add(ppuText, 1,3);
				
				Label ppnName=new Label("Punkte pro Niederlage");
				ppnName.setFont(Font.font(20));
				detGP.add(ppnName, 0, 4);
				Label ppnText=new Label(beschriftungen[4]);
				ppnText.setFont(Font.font(20));
				detGP.add(ppnText, 1,4);
	
				Button teams=new Button("Teams auswählen");
				teams.setFont(Font.font(15));
				teams.setOnAction((e)->{
					if(liga.getSpieltage().size()==0) {
						new TETeams(steuerung, liga, stage, akt);
					}else {
						JOptionPane.showMessageDialog(null,"Die Teams können nicht bearbeitet werden, "
								+ "da schon Spieltage vorhanden sind!","FEHLER!",0);
					}
				});
				detGP.add(teams, 1, 5);
							
				gp.add(inhalt, 1, 0);
			}else if(!t.isLiga()) {
				letzterTab=0;
				bearbeitenY=6;
				
				KORunde kor=(KORunde) t;
				
				Label ekLab=new Label("Erstes Kriterium");
				ekLab.setFont(Font.font(20));
				detGP.add(ekLab, 0,2);
				String ekString="Tore";
				if(kor.getKriterium1()==KORunde.kriteriumEinsSpiele) {
					ekString="Spiele";
				}
				Text ekText=new Text(ekString);
				ekText.setFont(Font.font(20));
				detGP.add(ekText, 1,2);
				
				Label zkLab=new Label("Zweites Kriterium");
				zkLab.setFont(Font.font(20));
				detGP.add(zkLab, 0,3);
				String zkString="Auswärtstore";
				if(kor.getKriterium2()==KORunde.kriteriumZweiElfmeter) {
					zkString="Elfmeterschießen";
				}
				Text zkText=new Text(zkString);
				zkText.setFont(Font.font(20));
				detGP.add(zkText, 1,3);
				
				Label spLab=new Label("Spielanzahl");
				spLab.setFont(Font.font(20));
				detGP.add(spLab, 0,4);
				Text spText=new Text(""+kor.getSpielanzahl());
				spText.setFont(Font.font(20));
				detGP.add(spText, 1,4);
				
				gp.add(detGP, 1,0);
			}
			
			Button bearbeiten=new Button("Bearbeiten");
			bearbeiten.setFont(Font.font(15));
			bearbeiten.setOnAction((e)->{
				new TurnierelementBearbeiten(akt, steuerung, stage, t);
			});
			detGP.add(bearbeiten, 0,bearbeitenY);
		}
		
	}


	private String[] TEDetailsBeschriftungen(Turnierelement t) {
		String[]beschr=null;
		if(t!=null) {
			if(t.getClass().getName().equals("de.pasch.turnierleitung.turnierelemente.Liga")) {
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
				beschr[0]="KO-Runde";
				beschr[1]=t.getName();
			}
		}else {
			beschr=new String[2];
			for(int i=0;i<beschr.length;i++) {
				beschr[i]="";
			}
		}
		
		return beschr;
	}
	
	private void createZusaetzlichenInhalt(TabPane inhalt,Liga liga) {
		GridPane gp=new GridPane();
		gp.setPadding(new Insets(10));
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setAlignment(Pos.TOP_CENTER);
		
		Label tabLab=new Label("Tabelle: "+liga.getName());
		tabLab.setFont(Font.font(20));
		tabLab.setOnMouseClicked((e)->{
			hfst=new HFSpieltag(liga, null, null, stage, bp, steuerung, akt);
		});
		gp.add(tabLab, 0, 0);
		
		GridPane tabellePane=new GridPane();
		tabellePane.setPadding(new Insets(10));
		tabellePane.setHgap(10);
		tabellePane.setVgap(10);
		ScrollPane tabelleSP=new ScrollPane(tabellePane);
		gp.add(tabelleSP, 0, 1);
		
		ArrayList<VereinInTabelle>tabelleVIT=liga.berechneGetAktuelleTabelle();
		
		Label platz=new Label("Platz");
		platz.setFont(Font.font(15));
		tabellePane.add(platz, 0, 0);
		Label name=new Label("Team");
		name.setFont(Font.font(15));
		tabellePane.add(name, 1, 0);
		Label punkte=new Label("Pkt");
		punkte.setFont(Font.font(15));
		tabellePane.add(punkte, 3, 0);
		Label spiele=new Label("Sp");
		spiele.setFont(Font.font(15));
		tabellePane.add(spiele,2, 0);
		Label siege=new Label("S");
		siege.setFont(Font.font(15));
		tabellePane.add(siege, 4, 0);
		Label unentschieden=new Label("U");
		unentschieden.setFont(Font.font(15));
		tabellePane.add(unentschieden, 5, 0);
		Label niederlagen=new Label("N");
		niederlagen.setFont(Font.font(15));
		tabellePane.add(niederlagen, 6, 0);
		Label tore=new Label("+");
		tore.setFont(Font.font(15));
		tabellePane.add(tore, 7, 0);
		Label gegentore=new Label("-");
		gegentore.setFont(Font.font(15));
		tabellePane.add(gegentore, 8, 0);
		Label tordifferenz=new Label("TD");
		tordifferenz.setFont(Font.font(15));
		tabellePane.add(tordifferenz, 9, 0);
		
		
		for(int i=0;i<tabelleVIT.size();++i) {
			VereinInTabelle vit=tabelleVIT.get(i);
			Team team=IDPicker.pick(steuerung.getTeams(),vit.getTeamID());
			
			Label teamPlatz=new Label(""+(i+1));
			teamPlatz.setFont(Font.font(15));
			teamPlatz.setPrefWidth(75);
			teamPlatz.setOnMouseClicked((e)->{
				hfp=new HFProtagonisten(team,stage, bp, steuerung, akt);
			});
			tabellePane.add(teamPlatz, 0, i+1);
			
			Label teamName=new Label(team.getMoeglichKN());
			teamName.setFont(Font.font(15));
			teamName.setPrefWidth(75);
			teamName.setOnMouseClicked((e)->{
				hfp=new HFProtagonisten(team,stage, bp, steuerung, akt);
			});
			tabellePane.add(teamName, 1, i+1);
			
			Label teamPunkte=new Label(""+vit.getPunkte());
			teamPunkte.setFont(Font.font(15));
			teamPunkte.setOnMouseClicked((e)->{
				hfp=new HFProtagonisten(team,stage, bp, steuerung, akt);
			});
			tabellePane.add(teamPunkte, 3, i+1);
			
			Label teamSpiele=new Label(""+vit.getSpiele());
			teamSpiele.setFont(Font.font(15));
			teamSpiele.setOnMouseClicked((e)->{
				hfp=new HFProtagonisten(team,stage, bp, steuerung, akt);
			});
			tabellePane.add(teamSpiele, 2, i+1);
			
			Label teamSiege=new Label(""+vit.getSiege());
			teamSiege.setFont(Font.font(15));
			teamSiege.setOnMouseClicked((e)->{
				hfp=new HFProtagonisten(team,stage, bp, steuerung, akt);
			});
			tabellePane.add(teamSiege, 4, i+1);
			
			Label teamUnentsch=new Label(""+vit.getUnentschieden());
			teamUnentsch.setFont(Font.font(15));
			teamUnentsch.setOnMouseClicked((e)->{
				hfp=new HFProtagonisten(team,stage, bp, steuerung, akt);
			});
			tabellePane.add(teamUnentsch, 5, i+1);
			
			Label teamNiederl=new Label(""+vit.getNiederlagen());
			teamNiederl.setFont(Font.font(15));
			teamNiederl.setOnMouseClicked((e)->{
				hfp=new HFProtagonisten(team,stage, bp, steuerung, akt);
			});
			tabellePane.add(teamNiederl, 6, i+1);
			
			Label teamTore=new Label(""+vit.getTore());
			teamTore.setFont(Font.font(15));
			teamTore.setOnMouseClicked((e)->{
				hfp=new HFProtagonisten(team,stage, bp, steuerung, akt);
			});
			tabellePane.add(teamTore, 7, i+1);
			
			Label teamGegentore=new Label(""+vit.getGegentore());
			teamGegentore.setFont(Font.font(15));
			teamGegentore.setOnMouseClicked((e)->{
				hfp=new HFProtagonisten(team,stage, bp, steuerung, akt);
			});
			tabellePane.add(teamGegentore, 8, i+1);
			
			Label teamTD=new Label(""+vit.getTD());
			teamTD.setFont(Font.font(15));
			teamTD.setOnMouseClicked((e)->{
				hfp=new HFProtagonisten(team,stage, bp, steuerung, akt);
			});
			tabellePane.add(teamTD, 9, i+1);
		}
		
		HBox buttons=new HBox();
		buttons.setSpacing(10);
		gp.add(buttons,0,2);
		
		Button rk=new Button("Sortierkriterien bearbeiten");
		rk.setOnAction((e)->{
			new TERKSetzer(liga, stage, akt);
		});
		rk.setFont(Font.font(15));
		
		Button spi=new Button("Spiele anzeigen");
		spi.setOnAction((e)->{
			hfs=new HFSpiele(stage, bp, steuerung, akt);
		});
		spi.setFont(Font.font(15));
		
		buttons.getChildren().addAll(rk,spi);
		
		Tab tabelleTab=new Tab("Tabelle",gp);
		tabelleTab.setClosable(false);
		int letzterTabZwischen=letzterTab;
		inhalt.getTabs().add(tabelleTab);
		letzterTab=letzterTabZwischen;
		inhalt.getSelectionModel().select(letzterTab);
	}
}
