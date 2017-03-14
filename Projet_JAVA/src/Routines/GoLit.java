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
			System.out.println(bot.getActions().peek());
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
		 * Au moins un joueur a cote de nous, on attaque le joueur.
		 */
		else {
			bot.setAction(super.direction(bot, joueursProches.get(0)));
		}
	}

	/**
	 * Retourne un tableau des points constituants le chemin vers le lit le plus proche
	 * @param bot Instance de notre joueur
	 * @param t Instance du plateau de jeu
	 * @return ArrayList<Node> Tableau des points
	 */
	@Override
    public ArrayList<Node> litLePlusProche(Bot bot, Plateau t){
    	
    	HashMap listeLit;
    	bot.setLivreChasse(null);
    	Point positionLit;
    	Point positionLitNonAdjacent = null;
    	
    	/** Si on a deja un lit en chasse */
    	if(bot.getLitChasse() != null) {
    		ArrayList<Node> arrayPointChemin = t.donneCheminEntre(bot.donnePosition(), bot.getLitChasse());
    		/** Si le livre en chasse est adjacent, ca veut dire que nous allons l'obtenir au prochain tour,
 		   		On reinitialise donc la variable livreChasse */
    		if(adjacent(bot.getLitChasse())) {
    			bot.setLitChasse(null);
    		}
    		return arrayPointChemin;
    	}
    	/** On parcours les alentours de la map pour trouver des lits */
    	for(int i = 1;;++i){
    		listeLit = t.cherche(bot.donnePosition(), i, t.CHERCHE_LIT);
    		/** Si on a trouve des lits aux alentours */
    		if (!listeLit.isEmpty()) {
				ArrayList<Point> arrayPointLit = (ArrayList<Point>) listeLit.get(1);
				/** On parcourt toutes les positions des lits trouves */
    			for (Point p : arrayPointLit){
    				positionLit = p;
    				/** Si le lit est un lit adjacent, on le prend en priorite */
    				if(adjacent(positionLit)){
    					ArrayList<Node> arrayPointChemin = t.donneCheminEntre(bot.donnePosition(), positionLit);
    					bot.setLitChasse(null);
    	     			return arrayPointChemin;
    	     		/** Sinon on prend le lit le plus proche */
    				} else {
    					positionLitNonAdjacent = p;
    				}
    	     	}
    			/** Si on a trouve un lit Non adjacent */
    			if(positionLitNonAdjacent != null) {
    				ArrayList<Node> arrayPointChemin = t.donneCheminEntre(bot.donnePosition(), positionLitNonAdjacent);
    				bot.setLitChasse(positionLitNonAdjacent);
    				return arrayPointChemin;
    			}
    		}
    	}
    }

	@Override
	public ArrayList<Node> livreLePlusProche(Bot bot, Plateau t) {
		// TODO Auto-generated method stub
		return null;
	}
}