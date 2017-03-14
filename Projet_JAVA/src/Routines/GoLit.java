package Routines;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import Routines.Routine;
import clientdesarenes.Bot;
import jeu.Joueur;
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
		
		
		ArrayList <Node> deplacementVersLit = litLePlusProche(bot, plateau);
		
		Node n = joueursProches.get(joueursProches.size()-1);
		int posX = n.getPosX();
		int posY = n.getPosY();
		Joueur j = plateau.donneJoueurEnPosition(posX,posY);

		/**
		 * CAS 1 : Standard
		 * Aucun joueurs a proximite immediate du bot, on poursuit notre objectif initial.
		 */
		if(joueursProches.size() > 3) {
			bot.setAction(super.direction(bot, deplacementVersLit.get(0)));
		} 
		/**
		 * CAS 2 : Joueur dans le perimetre 
		 * Au moins un joueur dans le perimetre de 3 cases, on analyse l'etat de ce joueur.
		 */
		else if (joueursProches.size() == 3) {
			if(bot.getActions().peek() == Action.RIEN) {
				bot.setAction(super.direction(bot, deplacementVersLit.get(0)));
			} else {
				if(bot.donneEsprit() > 15){
					bot.setAction(Action.RIEN);
				} else {
					bot.setAction(super.direction(bot, deplacementVersLit.get(0)));
				}
			}	
		} 
		/**
		 * CAS 3 : Joueur a proximite
		 * Au moins un joueur a 2 cases, on analyse l'etat de ce joueur.
		 */
		else if (joueursProches.size() == 2) {
			if(j.donneEsprit() <= 20) {
				bot.setAction(super.direction(bot, joueursProches.get(0)));
			} else {
				bot.setAction(super.direction(bot, deplacementVersLit.get(0)));
			}
		} 
		/**
		 * CAS 4 : Joueur a cote
		 * Au moins un joueur a cote de nous, on analyse l'etat de ce joueur.
		 */
		else {
			bot.setAction(super.direction(bot, deplacementVersLit.get(0)));
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