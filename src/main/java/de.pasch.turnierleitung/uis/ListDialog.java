/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pasch.turnierleitung.uis;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author pasch
 */
public class ListDialog<T> {
    public ListDialog(List<T> list,Stage primStage,double width,
            String messageString,String title,Handler<T> ldh){
    	if(list.size()>0) {
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
	        gp.setAlignment(Pos.CENTER);
	        
	        Label messageLabel=new Label(messageString);
	        gp.add(messageLabel, 0, 0,2,1);
	        Button ok=new Button("OK");
	        gp.add(ok,1,1);
	        ComboBox<T> listTaker=new ComboBox<T>();
	        listTaker.getItems().addAll(list);
	        listTaker.setValue(list.get(0));
	        gp.add(listTaker,0,1);
	        Scene scene=new Scene(gp,width,100);
	        stage.setScene(scene);
	        stage.show();
	        ok.setOnAction((e)->{
	            stage.hide();
	            ldh.handle(listTaker.getValue());
	        });
    	}else {
    		throw new IllegalArgumentException("List has no Object!");
    	}
    }
    
    public ListDialog(List<T> al,Stage primStage,
            String messageString,String title,Handler<T> ldh){
        new ListDialog<T>(al,primStage,250,messageString,title,ldh);
    }
}
