package de.pasch.turnierleitung.uis;

import javafx.scene.shape.Box;

public class HGap extends Box {
	
	public HGap(double width) {
		super();
		prefWidth(width);
	}
	
	public void setPref(double width) {
		prefWidth(width);
	}
}
