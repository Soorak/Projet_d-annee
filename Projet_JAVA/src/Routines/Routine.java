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
* C'est une classe abstraite qui permet d'implementer un comportement 
*
* @author  Benjamin Bertrand
* @author  Valerian Cuq
* @author  Jeremie Doche
* @author  Leo Folcher
* @author  Guillaume Tricaud
* @author  Geoffrey Vigneau
* @see     Routine
*/
public abstract class Routine {

    protected Bot bot;
    protected Plateau plateau;
    protected ArrayList <Node> joueursProches;
    
	protected Routine(Bot bot, Plateau plateau) {
    	this.bot = bot;
    	this.plateau = plateau;
		this.joueursProches = joueurLePlusProche(bot, plateau);
    }

	/**
	 * Methode qui definit l'action necessaire pour atteindre l'objectif 
	 * en s'adaptant en cas de joueurs proches.
	 */
    public abstract void act();

    /**
     * Traduction du point a atteindre en deplacement primitif.
     * 
     * @param bot Instance de notre joueur
     * @param node Point adjacent a atteindre
     * @return Action deplacement a effectuer par le joueur
     */
    public Action direction(Bot bot, Node node){
    	if (node.getPosX() < bot.donnePosition().getX() && node.getPosY() == bot.donnePosition().getY()){
    		return Action.GAUCHE;
    	} else if (node.getPosX() > bot.donnePosition().getX() && node.getPosY() == bot.donnePosition().getY()){
    		return Action.DROITE;
    	} else if (node.getPosX() == bot.donnePosition().getX() && node.getPosY() > bot.donnePosition().getY()){
    		return Action.BAS;
    	} else if (node.getPosX() == bot.donnePosition().getX() && node.getPosY() < bot.donnePosition().getY()){
    		return Action.HAUT;
    	}
    	return Action.RIEN;
    }
    
    /**
	 * Retourne un tableau des points constituants le chemin vers le joueur le plus proche
	 * @param bot Instance de notre joueur
	 * @param t Instance du plateau de jeu
	 * @return ArrayList<Node> Tableau des points
	 */
    public ArrayList<Node> joueurLePlusProche(Bot bot, Plateau t){
    	
    	HashMap listeJoueur;
    	Point positionJoueur;
    	
    	for(int i = 1;;++i){
    		listeJoueur = t.cherche(bot.donnePosition(), i, t.CHERCHE_JOUEUR);
    		if (!listeJoueur.isEmpty()) {
    			
				ArrayList<Point> arrayPointJoueur = (ArrayList<Point>) listeJoueur.get(4);
    			for (Point p : arrayPointJoueur){
					positionJoueur = p;
					if(!positionJoueur.equals(bot.donnePosition())){
						ArrayList<Node> arrayPointChemin = t.donneCheminEntre(bot.donnePosition(), positionJoueur);
		     			return arrayPointChemin;
					}
    	     	}
    		}
    	}
    }
    
    public abstract ArrayList<Node> litLePlusProche(Bot bot, Plateau t);
    public abstract ArrayList<Node> livreLePlusProche(Bot bot, Plateau t);
    /**
     * Permet de savoir si la position envoye est une position adjacente
     */
    public boolean adjacent(Point position){
    	if ((position.getX() == bot.donnePosition().getX() + 1 && position.getY() == bot.donnePosition().getY())
    			|| (position.getX() == bot.donnePosition().getX()-1 && position.getY() == bot.donnePosition().getY()) 
    			|| (position.getX() == bot.donnePosition().getX() && position.getY() == bot.donnePosition().getY() + 1)
    			|| (position.getX() == bot.donnePosition().getX() && position.getY() == bot.donnePosition().getY() - 1)){
    		return true;
    	}
    	else {
    		return false;
    	}
    }
}