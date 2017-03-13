package clientdesarenes;

import java.util.HashMap;

import jeu.Plateau;

public class IA3 {
	private BotJD b;
	private Plateau p;
	
	
	public IA3(BotJD b, Plateau p) {
		this.b = b;
		this.p = p;
	}
	
	public void play() {
		HashMap<java.lang.Integer,java.util.ArrayList<java.awt.Point>> r = new HashMap<>();
		r = cherche(b.donnePosition(), 2, Plateau.CHERCHE_JOUEUR);
	}

}
