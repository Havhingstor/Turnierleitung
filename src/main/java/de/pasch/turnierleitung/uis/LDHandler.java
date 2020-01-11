/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pasch.turnierleitung.uis;

/**
 *
 * @author pasch
 * @param <T>
 */
@FunctionalInterface
public interface LDHandler<T>{
    public void handle(T t);
}
