package Routines;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import clientdesarenes.Bot;
import jeu.Plateau;
import jeu.Joueur;
import jeu.Joueur.Action;
import jeu.astar.Node;

/**
 *
 * Cette classe a pour but de realiser l'objectif : aller vers un livre.
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
public class GoLivre extends Routine {

	/**
	 * 
	 * @param bot Instance de notre joueur
	 * @param plateau Instance du plateau de jeu
	 */
	public GoLivre(Bot bot, Plateau plateau) {
		super(bot, plateau);
	}

	/**
	 * Methode qui definit l'action necessaire pour atteindre l'objectif 
	 * en s'adaptant en cas de joueurs proches.
	 */
	@Override
	public void act() {
		
		ArrayList <Node> deplacementVersLivre = livreLePlusProche(bot, plateau);

		Node n = joueursProches.get(joueursProches.size()-1);
		int posX = n.getPosX();
		int posY = n.getPosY();
		Joueur j = plateau.donneJoueurEnPosition(posX,posY);

		/**
		 * CAS 1 : Standard
		 * Aucun joueurs a proximite immediate du bot, on poursuit notre objectif initial.
		 */
		if(joueursProches.size() > 3) {
			bot.setAction(super.direction(bot, deplacementVersLivre.get(0)));
		} 
		/**
		 * CAS 2 : Joueur dans le perimetre 
		 * Au moins un joueur dans le perimetre de 3 cases, on analyse l'etat de ce joueur.
		 */
		else if (super.joueursProches.size() == 3) {
			System.out.println(bot.getActions().peek());
			if(bot.getActions().peek() == Action.RIEN) {
				if (j.donnePointsCulture() > bot.donnePointsCulture() - 20 
						&& j.donneEsprit() <= 20 && bot.donneEsprit() > 50) {
					bot.setAction(super.direction(bot, joueursProches.get(0)));
				} else {
					bot.setAction(super.direction(bot, joueursProches.get(0)));
				}
			} else {
				bot.setAction(Action.RIEN);
			}
		} 
		/**
		 * CAS 3 : Joueur a proximite
		 * Au moins un joueur a 2 cases, on analyse l'etat de ce joueur.
		 */
		else if (joueursProches.size() == 2) {
			if(
					(j.donnePointsCulture() > bot.donnePointsCulture() - 20 
							&& j.donneEsprit() < bot.donneEsprit()) 
					|| j.donneEsprit() <= 20) {
				bot.setAction(super.direction(bot, joueursProches.get(0)));
			}
		} 
		/**
		 * CAS 4 : Joueur a cote
		 * Au moins un joueur a cote de nous, on analyse l'etat de ce joueur.
		 */
		else {
			bot.setAction(super.direction(bot, deplacementVersLivre.get(0)));
		}
	}

	/**
	 * Retourne un tableau des points constituants le chemin vers le livre le plus proche
	 * @param bot Instance de notre joueur
	 * @param t Instance du plateau de jeu
	 * @return ArrayList<Node> Tableau des points
	 */
	@Override
	public ArrayList<Node> livreLePlusProche(Bot bot, Plateau t){
    	HashMap listeLivre;
    	bot.setLitChasse(null);
    	Point positionLivre;
    	Point positionLivreNonAdjacent = null;
    	Point positionLivrePossede = null;

    	/** Si on a deja un livre en chasse */
    	if(bot.getLivreChasse() != null) {
    		ArrayList<Node> arrayPointChemin = t.donneCheminEntre(bot.donnePosition(), bot.getLivreChasse());
    		/** Si le livre en chasse est adjacent, ça veut dire que nous allons l'obtenir au prochain tour,
 		   		On reinitialise donc la variable livreChasse */
    		if(adjacent(bot.getLivreChasse())) {
    			bot.setLivreChasse(null);
    		}
    		return arrayPointChemin;
    	} else {
    		/** On parcours les alentours de la map pour trouver des livres */
    		for(int i = 1;;++i){
        		listeLivre = t.cherche(bot.donnePosition(), i, t.CHERCHE_LIVRE);
        		/** Si on a trouve des livres aux alentours */
        		if (!listeLivre.isEmpty()) {
    				ArrayList<Point> arrayPointLivres = (ArrayList<Point>) listeLivre.get(2);
    				/** On parcourt toutes les positions des livres trouve */
        			for (Point p : arrayPointLivres) {
        				/** Si le livre ne nous appartient pas deja */
        	     		if(t.contientUnLivreQuiNeLuiAppartientPas(bot, t.donneContenuCellule(p))){
        	     			positionLivre = p;
        	     			/** Si le livre est un livre adjacent, on le prend en priorite */
        	     			if (adjacent(positionLivre)){
        	     				ArrayList<Node> arrayPointChemin = t.donneCheminEntre(bot.donnePosition(), positionLivre);
        	     				bot.setLivreChasse(null);
            	     			return arrayPointChemin;
        	     			} else {
        	     				/** Si le le livre est un livre possédé par quelqu'un, on le prend en priorité */
        	     				if(t.donneProprietaireDuLivre(t.donneContenuCellule(positionLivre)) != 0) {
        	     					positionLivrePossede = p;
        	     				} else {
        	     					positionLivreNonAdjacent = p;
        	     				}
        	     			}
        	     		}
        	     	}
        			/** A la fin du parcourt des livres on vérifie vers quel livre on se déplace
     			   		Si on a trouvé un livre possédé par quelqu'un */
        			if (positionLivrePossede != null) {
        				ArrayList<Node> arrayPointChemin = t.donneCheminEntre(bot.donnePosition(), positionLivrePossede);
        				bot.setLivreChasse(positionLivrePossede);
        				return arrayPointChemin;
        			/** Sinon si on a trouvé un livre Non adjacent */
        			} else if(positionLivreNonAdjacent != null) {
        				ArrayList<Node> arrayPointChemin = t.donneCheminEntre(bot.donnePosition(), positionLivreNonAdjacent);
        				bot.setLivreChasse(positionLivreNonAdjacent);
        				return arrayPointChemin;
        			}
        		}
        	}
    	}
    }

	@Override
	public ArrayList<Node> litLePlusProche(Bot bot, Plateau t) {
		return null;
	}
}