package Routines;

import java.awt.Point;
import java.util.ArrayList;

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
public class GoLit extends Routine {

	public GoLit() {}

	@Override
	public void act(Bot bot, Plateau plateau) {
		if(super.joueurLePlusProche(bot, plateau).size() > 2) {
			bot.setAction(super.direction(bot, litLePlusProche(bot, plateau)));
		}
	}

	/**
	 * 
	 * @param bot Le bot qui va executer la routine
	 * @param t Le plateau de jeu
	 * @return
	 */
	public Node litLePlusProche(Bot bot, Plateau t){
		ArrayList<Point> lits;
		int taille_recherche = 1;
		do {
			lits = t.cherche(bot.donnePosition(), taille_recherche++, Plateau.CHERCHE_LIT).get(1);
		} while (lits == null || lits.isEmpty());
		System.err.println("J'ai " + bot.donneEsprit() + "points d'esprit, je vais au lit !");
		return t.donneCheminEntre(bot.donnePosition(), lits.get(0)).get(0);
	}
}