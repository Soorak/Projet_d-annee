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
* Cette classe a pour but de realiser l'objectif : aller vers un lit.
* Elle prends en compte la proximite avec les autres joueurs.
*
* @author  Benjamin Bertrand
* @author  Valerian Cuq
* @author  Jeremie Doche
* @author  Leo Folcher
* @author  Guillaume Tricaud
* @author  Geoffrey Vigneau
* @see     Routine
*/
public class GoLit extends Routine {

	/**
	 * 
	 * @param bot Instance de notre joueur
	 * @param plateau Instance du plateau de jeu
	 */
	public GoLit(Bot bot, Plateau plateau) {
		super(bot, plateau);
	}

	/**
	 * Methode qui definit l'action necessaire pour atteindre l'objectif 
	 * en s'adaptant en cas de joueurs proches.
	 */
	@Override
	public void act() {
		if(super.joueurLePlusProche(bot, plateau).size() > 2) {
			
		} else if (super.joueurLePlusProche(bot, plateau).size() == 2) {
			// Joueur a 2 cases de nous : decision avance ou repli
		} else {
			// Joueur a cote de nous : decision attaque ou repli
		}
	}

	/**
	 * Retourne un tableau des points constituants le chemin vers le lit le plus proche
	 * @param bot Instance de notre joueur
	 * @param t Instance du plateau de jeu
	 * @return ArrayList<Node> Tableau des points
	 */
	public ArrayList<Node> litLePlusProche(Bot bot, Plateau t) {
		
    	HashMap listeLit;
    	Point positionLit;
    	
    	for(int i = 1;;++i){
    		listeLit = t.cherche(bot.donnePosition(), i, t.CHERCHE_LIT);
    		
    		if (!listeLit.isEmpty()) {
				ArrayList<Point> arrayPointLit = (ArrayList<Point>) listeLit.get(1);
    			for (Point p : arrayPointLit){
    				positionLit = p;
	     	     	ArrayList<Node> arrayPointChemin = t.donneCheminEntre(bot.donnePosition(), positionLit);
	     			return arrayPointChemin;
    	     	}
    		}
    	}
    }
}