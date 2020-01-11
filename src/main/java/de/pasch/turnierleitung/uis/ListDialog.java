/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pasch.turnierleitung.uis;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author pasch
 */
public class ListDialog<T> {
    public ListDialog(ArrayList<T> al,Stage primStage,
            String messageString,String title,LDHandler<T> ldh){
        Stage stage=new Stage();
        if(primStage!=null){
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primStage);
        }
        stage.setTitle(title);
        GridPane gp=new GridPane();
        gp.setPadding(new Insets(5,5,5,5));
        gp.setVgap(5);
        gp.setHgap(5);
        
        Text messageText=new Text(messageString);
        gp.add(messageText, 0, 0,2,1);
        Button ok=new Button("OK");
        gp.add(ok,1,1);
        ComboBox<T> listTaker=new ComboBox();
        listTaker.getItems().addAll(al);
        gp.add(listTaker,0,1);
        
        Scene scene=new Scene(gp,300,200);
        stage.setScene(scene);
        stage.show();
        ok.setOnAction((e)->{
            stage.hide();
            ldh.handle(listTaker.getValue());
        });
    }
}
