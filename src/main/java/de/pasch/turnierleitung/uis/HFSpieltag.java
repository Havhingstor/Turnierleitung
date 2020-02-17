package de.pasch.turnierleitung.uis;

import javax.swing.JOptionPane;

import de.pasch.turnierleitung.spiele.Spiel;
import de.pasch.turnierleitung.steuerung.IDPicker;
import de.pasch.turnierleitung.steuerung.Steuerung;
import de.pasch.turnierleitung.turnierelemente.KORunde;
import de.pasch.turnierleitung.turnierelemente.Liga;
import de.pasch.turnierleitung.turnierelemente.Runde;
import de.pasch.turnierleitung.turnierelemente.Rundensammlung;
import de.pasch.turnierleitung.turnierelemente.Spieltag;
import de.pasch.turnierleitung.turnierelemente.Turnierelement;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HFSpieltag {
	Steuerung steuerung;
	BorderPane bp;
	Stage stage;
	Aktualisierer akt;
	GridPane gp;
	GridPane detailGP;
	GridPane spielInfos;
	long letztesTE=0;
	
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
		gp.getChildren().clear();
		if(steuerung.getLigen().size()+steuerung.getKORunden().size()>0) {
			GridPane sptAuswahl=new GridPane();
			sptAuswahl.setPadding(new Insets(5));
			sptAuswahl.setHgap(10);
			sptAuswahl.setVgap(10);
			sptAuswahl.setPrefWidth(200);
			gp.add(sptAuswahl, 0, 0);
			
			
			Turnierelement te=IDPicker.pick(steuerung.getTurnierelemente(),letztesTE);
			
			ComboBox<Turnierelement> TEAuswahl=new ComboBox<Turnierelement>();
			TEAuswahl.getItems().addAll(steuerung.getLigen());
			TEAuswahl.getItems().addAll(steuerung.getKORunden());
			if(te==null) {
				te=TEAuswahl.getItems().get(0);
			}
			TEAuswahl.setValue(te);
			TEAuswahl.setOnAction((e)->{
				letztesTE=TEAuswahl.getValue().getID();
				sptAuswahl.getChildren().removeIf((f)->(f!=TEAuswahl));
				createSpieltagslisteRest(TEAuswahl, sptAuswahl);
			});
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
				name.setPrefWidth(170);
				spieltage.getChildren().add(name);
				name.setOnAction((e)->{
					createSptDetailBereich(spt, liga);
				});
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
			
			Spieltag spieltag=null;
			try {
				spieltag=liga.getSpieltage().get(0);
			}catch(IndexOutOfBoundsException ioobe) {}
			createSptDetailBereich(spieltag, liga);
		}else {
			KORunde kor =(KORunde)teAuswahl.getValue();
			
			for(Rundensammlung r:kor.getRundensammlungen()) {
				Button name=new Button(r.getName());
				name.setFont(Font.font(15));
				name.setPrefWidth(185);
				spieltage.getChildren().add(name);
				name.setOnAction((e)->{
					createRundenDetailBereich(r, kor);
				});
			}
			
			Button neu=new Button("Neue Runde");
			neu.setFont(Font.font(15));
			sptAuswahl.add(neu, 0, 2);
			neu.setOnAction((e)->{
				new RundensammlungHinzufuegen(kor, stage, steuerung, akt);
			});
			
			Button loe=new Button("Runde löschen"); 
			loe.setFont(Font.font(15));
			sptAuswahl.add(loe, 0, 3);
			loe.setOnAction((e)->{
				new ListDialog<Rundensammlung>(kor.getRundensammlungen(), stage, "Welche Runde soll gelöscht werden",
						"Runde löschen",(f)->{
					try {
						kor.removeRundensammlung(f.getID());
						akt.aktualisieren();
					}catch(IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(null,iae.getMessage(),"FEHLER!",0);
					}
				});
			});
			
			Rundensammlung rs=null;
			try {
				rs=kor.getRundensammlungen().get(0);
			}catch(IndexOutOfBoundsException ioobe) {}
			createRundenDetailBereich(rs, kor);
		}
	}
	
	public void createSptDetailBereich(Spieltag spieltag,Liga liga) {
		gp.getChildren().remove(detailGP);
		
		detailGP=new GridPane();
		detailGP.setPadding(new Insets(40,10,10,10));
		detailGP.setHgap(10);
		detailGP.setVgap(40);
		detailGP.setPrefWidth(400);
				
		String[]beschr=createBeschrSpt(spieltag);
		
		GridPane infos=new GridPane();
		infos.setPadding(new Insets(5));
		infos.setHgap(5);
		infos.setVgap(5);
		infos.setPrefHeight(250);
		detailGP.add(infos, 0, 0);
		
		Label nameLab=new Label("Name");
		nameLab.setFont(Font.font(20));
		infos.add(nameLab, 0, 0);
		
		Text nameText=new Text(beschr[0]);
		nameText.setFont(Font.font(20));
		infos.add(nameText, 1, 0);
		
		Label spieleLab=new Label("Spielmenge");
		spieleLab.setFont(Font.font(20));
		infos.add(spieleLab, 0,1);
		
		Text spieleText=new Text(beschr[1]);
		spieleText.setFont(Font.font(20));
		infos.add(spieleText, 1, 1);
		
		gp.add(detailGP, 1, 0);
		
		Button auslosen=new Button("Ausgelosten Spieltag hinzufügen");
		auslosen.setFont(Font.font(15));
		auslosen.setPrefWidth(370);
		auslosen.setOnAction((e)->{
			if(liga!=null) {
				String name=JOptionPane.showInputDialog(null,"Welchen Namen soll der Spieltag haben?",
						"Neuen Spieltag auslosen",JOptionPane.QUESTION_MESSAGE);
				if(name!=null) {
					if(name.equals("")){
						try {
						steuerung.addAusgelosterSpieltag(liga.getID());
						}catch(IllegalArgumentException iae) {
							fehlerbehandlungSpieltagsbenennung(liga,liga.getSpieltage().size()+2);
						}
					}else {
						steuerung.addAusgelosterSpieltag(liga.getID(),name);
					}
					akt.aktualisieren();
				}
			}
		});
		detailGP.add(auslosen, 0,1);
		
		Button spielplanBer=new Button("Restlichen Spielplan berechnen");
		spielplanBer.setFont(Font.font(15));
		spielplanBer.setPrefWidth(370);
		spielplanBer.setOnAction((e)->{
			int rueckrunde=JOptionPane.showConfirmDialog(null,"Soll auch die Rückrunde berechnet werden?","Spielplan berechnen",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
			if(rueckrunde==JOptionPane.YES_OPTION) {
				try {
					steuerung.ligaspielplanBerechnen(liga.getID(), true);
				}catch(IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(null,iae.getMessage(),"FEHLER!",0);
				}
			}else {
				try {
					steuerung.ligaspielplanBerechnen(liga.getID(), false);
				}catch(IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(null,iae.getMessage(),"FEHLER!",0);
				}
			}
			akt.aktualisieren();
		});
		detailGP.add(spielplanBer, 0,2);
		
		Button rueckrunde=new Button("Rückrunde hinzufügen");
		rueckrunde.setFont(Font.font(15));
		rueckrunde.setPrefWidth(370);
		rueckrunde.setOnAction((e)->{
			steuerung.createRueckrunde(liga.getID());
			akt.aktualisieren();
		});
		detailGP.add(rueckrunde, 0,3);
		
		createSpielLigaListe(spieltag, liga);
	}
	
	public String[] createBeschrSpt(Spieltag spieltag) {
		String[]beschr=new String[2];
		
		if(spieltag!=null) {
			beschr[0]=spieltag.getName();
			beschr[1]=""+spieltag.getSpiele().size();
		}else {
			beschr[0]="";
			beschr[1]="";
		}
		return beschr;
	}
	
	public void fehlerbehandlungSpieltagsbenennung(Liga liga,int neuerVersuch) {
		try {
			steuerung.addAusgelosterSpieltag(liga.getID(),neuerVersuch+". Spieltag");
		}catch(IllegalArgumentException iae) {
			fehlerbehandlungSpieltagsbenennung(liga,neuerVersuch+1);
		}
	}
	
	public void createRundenDetailBereich(Rundensammlung runde,KORunde kor) {
		gp.getChildren().remove(detailGP);
		
		detailGP=new GridPane();
		detailGP.setPadding(new Insets(40,10,10,10));
		detailGP.setHgap(10);
		detailGP.setVgap(40);
		detailGP.setPrefWidth(400);
				
		String[]beschr=createBeschrRunde(runde);
		
		GridPane infos=new GridPane();
		infos.setPadding(new Insets(5));
		infos.setHgap(5);
		infos.setVgap(5);
		infos.setPrefHeight(250);
		detailGP.add(infos, 0, 0);
		
		Label nameLab=new Label("Name");
		nameLab.setFont(Font.font(20));
		infos.add(nameLab, 0, 0);
		
		Text nameText=new Text(beschr[0]);
		nameText.setFont(Font.font(20));
		infos.add(nameText, 1, 0);
		
		Label spieleLab=new Label("Begegnungsmenge");
		spieleLab.setFont(Font.font(20));
		infos.add(spieleLab, 0, 1);
		
		Text spieleText=new Text(beschr[1]);
		spieleText.setFont(Font.font(20));
		infos.add(spieleText, 1, 1);
		
		gp.add(detailGP, 1, 0);
		
		Button auslosen=new Button("Diese Runde neu auslosen");
		auslosen.setFont(Font.font(15));
		auslosen.setPrefWidth(370);
		auslosen.setOnAction((e)->{
			if(kor!=null) {
				
			}
		});
		detailGP.add(auslosen, 0,1);
		
		createBegegnungKORListe(runde);
	}
	
	public String[] createBeschrRunde(Rundensammlung runde) {
		String[]beschr=new String[2];
		
		if(runde!=null) {
			beschr[0]=runde.getName();
			beschr[1]=""+runde.getRunden().size();
		}else {
			beschr[0]="";
			beschr[1]="";
		}
		return beschr;
	}
	
	public void createSpielLigaListe(Spieltag spieltag,Liga liga) {
		GridPane sAuswahl=new GridPane();
		sAuswahl.setPadding(new Insets(5));
		sAuswahl.setHgap(5);
		sAuswahl.setVgap(5);
		
		VBox spieltage =new VBox();
		ScrollPane spieltageSP=new ScrollPane(spieltage);
		spieltageSP.setPrefSize(200,500);
		sAuswahl.add(spieltageSP, 0, 1);
		
		if(spieltag!=null) {
			for(Spiel sp:spieltag.getSpiele()) {
				Button name=new Button(sp.toString());
				name.setFont(Font.font(15));
				name.setPrefWidth(185);
				spieltage.getChildren().add(name);
				name.setOnAction((e)->{
					createSpielLigaDetails(sp);
				});
			}
		}
		
		Button neu=new Button("Neues Spiel");
		neu.setFont(Font.font(15));
		sAuswahl.add(neu, 0, 2);
		neu.setOnAction((e)->{
			if(spieltag!=null&&liga!=null) {
				new SpielHinzufuegen(stage, akt, steuerung, spieltag,liga);
			}
		});
		
		Button loe=new Button("Spiel löschen");
		loe.setFont(Font.font(15));
		sAuswahl.add(loe, 0, 3);
		loe.setOnAction((e)->{
			if(spieltag.getSpiele().size()>0) {
				new ListDialog<Spiel>(spieltag.getSpiele(), stage, "Welches Spiel soll gelöscht werden",
						"Spiel löschen",(f)->{
					try {
						steuerung.removeSpiel(f.getID());
						spieltag.removeSpiel(f.getID());
						akt.aktualisieren();
					}catch(IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(null,iae.getMessage(),"FEHLER!",0);
					}
				});
			}
		});
		
		Spiel spiel=null;
		try {
			spiel=spieltag.getSpiele().get(0);
		}catch(IndexOutOfBoundsException ioobe) {}catch(NullPointerException npe) {}
		createSpielLigaDetails(spiel);
		
		gp.add(sAuswahl, 2,0);
	}
	
	public void createBegegnungKORListe(Rundensammlung rs) {
		GridPane sAuswahl=new GridPane();
		sAuswahl.setPadding(new Insets(5));
		sAuswahl.setHgap(5);
		sAuswahl.setVgap(5);
		
		VBox spieltage =new VBox();
		ScrollPane spieltageSP=new ScrollPane(spieltage);
		spieltageSP.setPrefSize(200,500);
		sAuswahl.add(spieltageSP, 0, 1);
		
		if(rs!=null) {
			for(Runde r:rs.getRunden()) {
				Button name=new Button(r.toString());
				name.setFont(Font.font(15));
				name.setPrefWidth(185);
				spieltage.getChildren().add(name);
				name.setOnAction((e)->{
					createBegegnungKORDetails(r);
				});
			}
		}
		
		Button neu=new Button("Neue Begegnung");
		neu.setFont(Font.font(15));
		sAuswahl.add(neu, 0, 2);
		neu.setOnAction((e)->{
			if(rs!=null) {
				new RundeHinzufuegen(stage, akt, steuerung, rs);
			}
		});
		
		Button loe=new Button("Begegnung löschen");
		loe.setFont(Font.font(15));
		sAuswahl.add(loe, 0, 3);
		loe.setOnAction((e)->{
			if(rs.getRunden().size()>0) {
				new ListDialog<Runde>(rs.getRunden(), stage, "Welche Begegnung soll gelöscht werden",
						"Begegnung löschen",(f)->{
					try {
						steuerung.removeRunde(f.getID());
						rs.removeRunde(f.getID());
						akt.aktualisieren();
					}catch(IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(null,iae.getMessage(),"FEHLER!",0);
					}
				});
			}
		});
		
		Runde r=null;
		try {
			r=rs.getRunden().get(0);
		}catch(IndexOutOfBoundsException ioobe) {}catch(NullPointerException npe) {}
		
		createBegegnungKORDetails(r);
		gp.add(sAuswahl, 2,0);
	}
	
	public void createSpielLigaDetails(Spiel spiel) {
		gp.getChildren().remove(spielInfos);
		
		spielInfos=new GridPane();
		spielInfos.setPadding(new Insets(40,10,10,10));
		spielInfos.setHgap(10);
		spielInfos.setVgap(40);
		spielInfos.setPrefWidth(400);
				
		String[]beschr=createBeschrSpiel(spiel);
		
		
		Label heimLabel=new Label("Heimteam");
		heimLabel.setFont(Font.font(20));
		spielInfos.add(heimLabel, 0, 0);
		
		Text heimText=new Text(beschr[0]);
		heimText.setFont(Font.font(20));
		spielInfos.add(heimText, 1, 0);
		
		Label auswLabel=new Label("Auswärtsteam");
		auswLabel.setFont(Font.font(20));
		spielInfos.add(auswLabel, 0, 1);
		
		Text auswText=new Text(beschr[1]);
		auswText.setFont(Font.font(20));
		spielInfos.add(auswText, 1, 1);
		
		Label ergebLabel=new Label("Ergebnis");
		ergebLabel.setFont(Font.font(20));
		spielInfos.add(ergebLabel, 0, 2);
		
		Text ergebText=new Text(beschr[2]);
		ergebText.setFont(Font.font(20));
		spielInfos.add(ergebText, 1,2);
		
		Label stadionLabel=new Label("Stadionname");
		stadionLabel.setFont(Font.font(20));
		spielInfos.add(stadionLabel, 0, 3);
		
		Text stadionText=new Text(beschr[3]);
		stadionText.setFont(Font.font(20));
		spielInfos.add(stadionText, 1,3);
		
		Label neutrLabel=new Label("Neutraler Platz");
		neutrLabel.setFont(Font.font(20));
		spielInfos.add(neutrLabel, 0, 4);
		
		Text neutrText=new Text(beschr[4]);
		neutrText.setFont(Font.font(20));
		spielInfos.add(neutrText, 1,4);
		
		gp.add(spielInfos, 4, 0);
		
	}

	private String[] createBeschrSpiel(Spiel spiel) {
		String[]beschr=new String[5];
		
		if(spiel!=null) {
			beschr[0]=spiel.getHeimteam().getName();
			beschr[1]=spiel.getAuswaertsteam().getName();
			beschr[2]=spiel.getHeimteam().getKurzname()+" "+spiel.getHeimtore().size()+":"
			+spiel.getAuswaertstore().size()+" "+spiel.getAuswaertsteam().getKurzname();
			beschr[3]=spiel.getStadion();
			if(spiel.getNeutralerPlatz()) {
				beschr[4]="Ja";
			}else {
				beschr[4]="Nein";
			}
		}else {
			for (int i = 0; i < beschr.length; i++) {
				beschr[i]="";
			}
		}
		
		return beschr;
	}
	
	public void createBegegnungKORDetails(Runde runde) {
		gp.getChildren().remove(spielInfos);
		
		spielInfos=new GridPane();
		spielInfos.setPadding(new Insets(40,10,10,10));
		spielInfos.setHgap(10);
		spielInfos.setVgap(40);
		spielInfos.setPrefWidth(400);
				
		String[]beschr=createBeschrBegegnung(runde);
		
		
		Label heimLabel=new Label("erstes Heimteam");
		heimLabel.setFont(Font.font(20));
		spielInfos.add(heimLabel, 0, 0);
		
		Text heimText=new Text(beschr[0]);
		heimText.setFont(Font.font(20));
		spielInfos.add(heimText, 1, 0);
		
		Label auswLabel=new Label("erstes Auswärtsteam");
		auswLabel.setFont(Font.font(20));
		spielInfos.add(auswLabel, 0, 1);
		
		Text auswText=new Text(beschr[1]);
		auswText.setFont(Font.font(20));
		spielInfos.add(auswText, 1, 1);
		
		Label stadienLabel=new Label("Stadien");
		stadienLabel.setFont(Font.font(20));
		spielInfos.add(stadienLabel, 0, 2);
		
		Text stadienText=new Text(beschr[2]);
		stadienText.setFont(Font.font(20));
		spielInfos.add(stadienText, 1,2);
		
		Label neutralLabel=new Label("Neutrale Plätze");
		neutralLabel.setFont(Font.font(20));
		spielInfos.add(neutralLabel, 0, 3);
		
		Text neutralText=new Text(beschr[3]);
		neutralText.setFont(Font.font(20));
		spielInfos.add(neutralText, 1,2);
		
		gp.add(spielInfos, 4, 0);
		
	}

	private String[] createBeschrBegegnung(Runde runde) {
		if(runde!=null) {
			String[]beschr=new String[4+runde.getSpiele().size()];
			
			beschr[0]=IDPicker.pick(steuerung.getTeams(),runde.getHeimteamID()).getName();
			beschr[1]=IDPicker.pick(steuerung.getTeams(),runde.getAuswaertsteamID()).getName();
			beschr[2]=IDPicker.pick(steuerung.getTeams(),runde.getHeimteamID()).getHeimstadion()+
					IDPicker.pick(steuerung.getTeams(),runde.getAuswaertsteamID()).getHeimstadion();
			
			if(IDPicker.pick(steuerung.getSpiele(),runde.getSpiele().get(0)).getNeutralerPlatz()) {
				beschr[3]="Ja";
			}else {
				beschr[3]="Nein";
			}
			
			for(int i=0;i<runde.getSpiele().size();i++) {
				Spiel spiel=IDPicker.pick(steuerung.getSpiele(),runde.getSpiele().get(i));
				beschr[4+i]=spiel.getHeimteam().getKurzname()+" "+spiel.getHeimtore().size()+":"
						+spiel.getAuswaertstore().size()+" "+spiel.getAuswaertsteam().getKurzname();
			}
			
			return beschr;
		}else {
			String[]beschr=new String[4];
			
			for(int i=0;i<4;i++) {
				beschr[i]="";
			}
			
			return beschr;
		}
	}
}
