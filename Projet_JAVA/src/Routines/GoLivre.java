package Routines;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import clientdesarenes.Bot;
import jeu.Plateau;
import jeu.Joueur.Action;
import jeu.astar.Node;

/**
 * 
 * @author Jeremie Doche
 *
 *
 */
public class GoLivre extends Routine {

	public GoLivre() {}

	@Override
	public void act(Bot bot, Plateau plateau) {
		if(super.joueurLePlusProche(bot, plateau).size() > 2) {
			bot.setAction(super.direction(bot, livreLePlusProche(bot, plateau)));
		}
	}

	/**
	 * 
	 * @param t Le plateau de jeu
	 * @return
	 */
	public Node livreLePlusProche(Bot bot, Plateau t){
		ArrayList<Point> livre;
		int taille_recherche = 1;
		do {
			livre = t.cherche(bot.donnePosition(), taille_recherche++, Plateau.CHERCHE_LIVRE).get(2);
		} while (livre == null || livre.isEmpty());
		System.err.println("Je vais m'instruire !");
		return t.donneCheminEntre(bot.donnePosition(), livre.get(0)).get(0);
	}
	
}