/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pasch.turnierleitung.uis;

import de.pasch.turnierleitung.steuerung.Steuerung;
import java.util.Arrays;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author pasch
 */
public class HFStartbereich {
        private Stage stage;
        private Steuerung steuerung;
        VBox spielerBox2,teamBox2,korBox2,ligBox2,sptBox2,rsBox2;
        Text[] teamTexte,spielerTexte,korTexte,ligTexte,sptTexte,rsTexte;
        HFTeamuebersicht hft;
        Einstellungsfenster ef;
        HFSpieleruebersicht hfs;
        
        public HFStartbereich(Stage stage,MenuBar mb,Steuerung steuerung,Aktualisierer akt){
            this.steuerung=steuerung;
            this.stage=stage;
            BorderPane bp=new BorderPane();
            GridPane grid=new GridPane();
            bp.setTop(mb);
            bp.setCenter(grid);
            grid.setAlignment(Pos.CENTER);
            grid.setPadding(new Insets(5,5,5,5));
            grid.setHgap(5);
            grid.setVgap(5);
            Text titel=new Text("Turnierleitung");
            titel.setFont(Font.font("Verdana",FontWeight.BOLD,25));
            grid.add(titel,0,0);
            Text turnierName =new Text(steuerung.getName());
            turnierName.setFont(Font.font("Verdana",FontWeight.MEDIUM,20));
            grid.add(turnierName,0,1);

            Button einst=new Button("Einstellungen");
            grid.add(einst,1,0);
            
            teamTexte=new Text[steuerung.getTeams().size()];
            BorderPane teamBox=new BorderPane();
            Button teamName=new Button("Teams");
            teamBox.setTop(teamName);
            for(int i=0;i<teamTexte.length;i++){
                teamTexte[i]=new Text(steuerung.getTeams().get(i).toString());
            }
             teamBox2=new VBox();
            teamBox2.getChildren().addAll(Arrays.asList(teamTexte));
            teamBox2.setPadding(new Insets(5,5,5,5));
            ScrollPane spTE=new ScrollPane(teamBox2);
            teamBox.setCenter(spTE);
            spTE.setPrefSize(300,200);

            grid.add(teamBox,0,2);

            spielerTexte=new Text[steuerung.getSpieler().size()];
            BorderPane spielerBox=new BorderPane();
            Button spielerName=new Button("Spieler");
            spielerBox.setTop(spielerName);
            for(int i=0;i<spielerTexte.length;i++){
                spielerTexte[i]=new Text(steuerung.getSpieler().get(i).toString());
            }
            spielerBox2=new VBox();
            spielerBox2.getChildren().addAll(Arrays.asList(spielerTexte));
            spielerBox2.setPadding(new Insets(5,5,5,5));
            ScrollPane spSP=new ScrollPane(spielerBox2);
            spielerBox.setCenter(spSP);
            spSP.setPrefSize(300,200);

            grid.add(spielerBox,0,3);

            korTexte=new Text[steuerung.getLigen().size()];
            BorderPane korBox=new BorderPane();
            Button korName=new Button("KO-Runden");
            korBox.setTop(korName);
            for(int i=0;i<korTexte.length;i++){
                korTexte[i]=new Text(steuerung.getKORunden().get(i).getName());
            }
            korBox2=new VBox();
            korBox2.getChildren().addAll(Arrays.asList(korTexte));
            korBox2.setPadding(new Insets(5,5,5,5));
            ScrollPane spKO=new ScrollPane(korBox2);
            korBox.setCenter(spKO);
            spKO.setPrefSize(300,200);

            grid.add(korBox,2,2);

            ligTexte=new Text[steuerung.getLigen().size()];
            BorderPane ligBox=new BorderPane();
            Button ligName=new Button("Ligen");
            ligBox.setTop(ligName);
            for(int i=0;i<ligTexte.length;i++){
                ligTexte[i]=new Text(steuerung.getLigen().get(i).getName());
            }
            ligBox2=new VBox();
            ligBox2.getChildren().addAll(Arrays.asList(ligTexte));
            ligBox2.setPadding(new Insets(5,5,5,5));
            ScrollPane spLIG=new ScrollPane(ligBox2);
            ligBox.setCenter(spLIG);
            spLIG.setPrefSize(300,200);

            grid.add(ligBox, 1,2);


            sptTexte=new Text[steuerung.getSpieltage().size()];
            BorderPane sptBox=new BorderPane();
            Label sptName=new Label("Spieltage in Ligen");
            sptBox.setTop(sptName);
            for(int i=0;i<sptTexte.length;i++){
                sptTexte[i]=new Text(steuerung.getSpieltage().get(i).getName());
            }
            sptBox2=new VBox();
            sptBox2.getChildren().addAll(Arrays.asList(sptTexte));
            sptBox2.setPadding(new Insets(5,5,5,5));
            ScrollPane spSPT=new ScrollPane(sptBox2);
            sptBox.setCenter(spSPT);
            spSPT.setPrefSize(300,200);

            grid.add(sptBox, 2,3);



            rsTexte=new Text[steuerung.getRundensammlungen().size()];
            BorderPane rsBox=new BorderPane();
            Label rsName=new Label("Spieltage in KO-Runden");
            rsBox.setTop(rsName);
            for(int i=0;i<rsTexte.length;i++){
                rsTexte[i]=new Text(steuerung.getRundensammlungen().get(i).getName());
            }
            rsBox2=new VBox();
            rsBox2.getChildren().addAll(Arrays.asList(rsTexte));
            rsBox2.setPadding(new Insets(5,5,5,5));
            ScrollPane spRS=new ScrollPane(rsBox2);
            rsBox.setCenter(spRS);
            spRS.setPrefSize(300,200);

            grid.add(rsBox, 1,3);

            teamName.setOnAction((e)->{
               hft=new HFTeamuebersicht(stage,mb,steuerung,akt);
            });
            
            einst.setOnAction((e)->{
                ef=new Einstellungsfenster(stage,steuerung,akt);
            });
            
            spielerName.setOnAction((e)->{
               hfs=new HFSpieleruebersicht(stage,mb,steuerung,akt);
            });
            
            Scene scene=new Scene(bp,1200,725);
            stage.setTitle("Turnierleitung");
            stage.setScene(scene);
            stage.show();
        }
        
        public void aktualisiere(){
            teamTexte=new Text[steuerung.getTeams().size()];
            for(int i=0;i<teamTexte.length;i++){
                teamTexte[i]=new Text(steuerung.getTeams().get(i).toString());
            }
            teamBox2.getChildren().clear();
            teamBox2.getChildren().addAll(Arrays.asList(teamTexte));
            
            spielerTexte=new Text[steuerung.getSpieler().size()];
             for(int i=0;i<spielerTexte.length;i++){
                spielerTexte[i]=new Text(steuerung.getSpieler().get(i).toString());
            }
            spielerBox2.getChildren().clear();
            spielerBox2.getChildren().addAll(Arrays.asList(spielerTexte));
            
            korTexte=new Text[steuerung.getKORunden().size()];
            for(int i=0;i<korTexte.length;i++){
                korTexte[i]=new Text(steuerung.getKORunden().get(i).getName());
            }
            korBox2.getChildren().clear();
            korBox2.getChildren().addAll(Arrays.asList(korTexte));
            
             ligTexte=new Text[steuerung.getLigen().size()];
            for(int i=0;i<ligTexte.length;i++){
                ligTexte[i]=new Text(steuerung.getLigen().get(i).getName());
            }
            ligBox2.getChildren().clear();
            ligBox2.getChildren().addAll(Arrays.asList(ligTexte));
            
             sptTexte=new Text[steuerung.getSpieltage().size()];
            for(int i=0;i<sptTexte.length;i++){
                sptTexte[i]=new Text(steuerung.getSpieltage().get(i).getName());
            }
            sptBox2.getChildren().clear();
            sptBox2.getChildren().addAll(Arrays.asList(sptTexte));
            
             rsTexte=new Text[steuerung.getRundensammlungen().size()];
            for(int i=0;i<rsTexte.length;i++){
                rsTexte[i]=new Text(steuerung.getRundensammlungen().get(i).getName());
            }
            rsBox2.getChildren().clear();
            rsBox2.getChildren().addAll(Arrays.asList(rsTexte));
            
            if(hft!=null){
                hft.aktualisiere();
            }
            if(ef!=null){
                ef.aktualisiere();
            }
            if(hfs!=null){
                hfs.aktualisiere();
            }
        }
}
