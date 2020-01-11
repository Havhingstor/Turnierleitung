/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pasch.turnierleitung.uis;

import java.util.ArrayList;

/**
 *
 * @author pasch
 */
public class ListDialog {
    public <T> ListDialog(ArrayList<T> al,LDHandler<T> ldh){
        T endElement=null;
        
        ldh.handle(endElement);
    }
}
