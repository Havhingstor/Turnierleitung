/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pasch.turnierleitung.uis;

/**
 *
 * @author pasch
 */
public class Aktualisierer {
    private Hauptfenster hf;
    
    public Aktualisierer(Hauptfenster hf){
        this.hf=hf;
    }
    public void aktualisieren(){
        hf.aktualisieren();
    }
}
