package de.pasch.turnierleitung.uis;

import javafx.scene.shape.Box;

public class VGap extends Box {
	
	public VGap(double height) {
		super();
		prefHeight(height);
	}
	
	public void setPref(double height) {
		prefHeight(height);
	}
}
