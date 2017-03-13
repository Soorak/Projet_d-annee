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
 * @author Jeremie Doche
 *
 *
 */
public class GoLivre extends Routine {

	public GoLivre() {}

	@Override
	public void act(Bot bot, Plateau plateau) {

		ArrayList <Node> deplacementVersJoueur = super.joueurLePlusProche(bot, plateau);
		ArrayList <Node> deplacementVersLivre = livreLePlusProche(bot, plateau);
		
		Node n = deplacementVersJoueur.get(deplacementVersJoueur.size()-1);
		int posX = n.getPosX();
		int posY = n.getPosY();
		Joueur j = plateau.donneJoueurEnPosition(posX,posY);

		/* -------------------- CASE 1 : Clear --------------------- */
		if(deplacementVersJoueur.size() > 3) {
			bot.setAction(super.direction(bot, deplacementVersLivre.get(0)));
			
		/* ---------------- CASE 2 : Player 3 cells ---------------- */	
		} else if (deplacementVersJoueur.size() == 3) {
			if(bot.getActions().peek() == Action.RIEN) {
				if (j.donnePointsCulture() > bot.donnePointsCulture() - 20 
						&& j.donneEsprit() <= 20 && bot.donneEsprit() > 50) {
					bot.setAction(super.direction(bot, deplacementVersJoueur.get(0)));
				} else {
					bot.setAction(super.direction(bot, deplacementVersLivre.get(0)));
				}
			} else {
				bot.setAction(super.direction(bot, deplacementVersLivre.get(0)));
			}
		
		/* ---------------- CASE 3 : Player 2 cells ---------------- */	
		} else if (deplacementVersJoueur.size() == 2) {
			if(
					(j.donnePointsCulture() > bot.donnePointsCulture() - 20 
							&& j.donneEsprit() < bot.donneEsprit()) 
					|| j.donneEsprit() <= 20) {
				bot.setAction(super.direction(bot, deplacementVersJoueur.get(0)));
			}

		/* -------------- CASE 4 : Player next to bot -------------- */	
		} else {
			// t'es nické
		}
	}

	/**
	 * 
	 * @param t Le plateau de jeu
	 * @return
	 */
	public ArrayList<Node> livreLePlusProche(Bot bot, Plateau t){
    	HashMap listeLivre;
    	
    	Point positionLivre;
    	
    	for(int i = 1;;++i){
    		listeLivre = t.cherche(bot.donnePosition(), i, t.CHERCHE_LIVRE);
    		if (!listeLivre.isEmpty()) {
				ArrayList<Point> arrayPointLivres = (ArrayList<Point>) listeLivre.get(2);
    			for (Point p : arrayPointLivres){
    				
    	     		if(t.contientUnLivreQuiNeLuiAppartientPas(bot, t.donneContenuCellule(p))){
    	     			positionLivre = p;
    	     	     	ArrayList<Node> arrayPointChemin = t.donneCheminEntre(bot.donnePosition(), positionLivre);
    	     			return arrayPointChemin;
    	     		}
    	     	}
    		}
    	}
    }

}