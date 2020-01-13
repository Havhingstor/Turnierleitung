package de.pasch.turnierleitung.uis;

import javafx.scene.shape.Box;

public class DoubleGap extends Box {
	
	public DoubleGap(double height,double width) {
		super();
		prefHeight(height);
		prefWidth(width);
	}
	
	public void setPref(double height,double width) {
		prefHeight(height);
		prefWidth(width);
	}
}
