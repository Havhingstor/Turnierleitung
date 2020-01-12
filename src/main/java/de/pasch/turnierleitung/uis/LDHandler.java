package de.pasch.turnierleitung.uis;

@FunctionalInterface
public interface LDHandler<T> {
	public void handle(T t);
}
