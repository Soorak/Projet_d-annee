package clientdesarenes;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;

import Routines.GoLit;
import Routines.GoLivre;
import Routines.Routine;
import jeu.Joueur;
import jeu.Plateau;
import jeu.Joueur.Action;
import jeu.astar.Node;

public class Bot extends jeu.Joueur implements reseau.JoueurReseauInterface {

	String key;
	Routine routine;
	Action a;
	StopWatch s;
	Queue<Action> actions = new LinkedList<Action>();
	Point livreChasse = null;
    Point litChasse = null;

	public Bot(String id, String cle) {
		super(id);
		key = cle;
		s = new StopWatch();
		livreChasse = null;
	    litChasse = null;
	}

	@Override
	public Joueur.Action faitUneAction(Plateau t) {
		this.s.reset();
		this.s.start();
		
		ArrayList <Node> deplacementVersLit = litLePlusProche(this, t);
        ArrayList <Node> deplacementVersLivre = livreLePlusProche(this, t);
		
		if(this.donneEsprit() < 50 && deplacementVersLit.size() <= 3 && deplacementVersLivre.size() > deplacementVersLit.size()) {
			this.routine = new GoLit(this, t);
			routine.act();
		} else if(this.donneEsprit() > 20 + deplacementVersLit.size()) {
			this.routine = new GoLivre(this, t);
			routine.act();
		} else {
			this.routine = new GoLit(this, t);
			routine.act();
		}
		
		this.s.stop();
		System.out.println("Bot.faitUneAction: Je joue " + a);
		System.out.println("Temps décision : " + s.getTime(TimeUnit.MILLISECONDS) + "ms");
		actions.add(a);
		return a;
	}

	public int DistanceLitPlusProche(Plateau t){
    	ArrayList<Point> lits;
		int taille_recherche = 1;
		do {
			lits = t.cherche(this.donnePosition(), taille_recherche++, Plateau.CHERCHE_LIT).get(1);
		} while (lits == null || lits.isEmpty());
		return t.donneCheminEntre(this.donnePosition(), lits.get(0)).size();
    }
	
	@Override
	public String donneID() {
		return donneNom();
	}

	@Override
	public String donneCle() {
		return key;
	}

	@Override
	public void debutNouvellePartie() {
		System.out.println("Bot: La partie commence.");
	}

	@Override
	public void finDeLaPartie(Plateau t) {
		System.out.println("Bot: La partie est finie.");
	}

	@Override
	public void deconnecte() {
		System.out.println("Bot: On est déconnecté du serveur.");
	}

	/**
	 * @param a the action to set
	 */
	public void setAction(Action a) {
		this.a = a;
	}

	/**
	 * @param routine the routine to set
	 */
	public void setRoutine(Routine routine) {
		this.routine = routine;
	}

	public void addAction(Action a) {
        actions.add(a);
    }
	
	
	
	/**
	 * @return the actions
	 */
	public Queue<Action> getActions() {
		return actions;
	}

	/**
	 * @return the s
	 */
	public StopWatch getS() {
		return s;
	}
	
	public ArrayList<Node> joueurLePlusProche(Plateau t){
    	HashMap listeJoueur;
    	
    	Point positionJoueur;
    	
    	for(int i = 1;;++i){
    		listeJoueur = t.cherche(this.donnePosition(), i, t.CHERCHE_JOUEUR);
    		if (!listeJoueur.isEmpty()) {
    			
				ArrayList<Point> arrayPointJoueur = (ArrayList<Point>) listeJoueur.get(4);
    			for (Point p : arrayPointJoueur){
					positionJoueur = p;
					if(!positionJoueur.equals(this.donnePosition())){
						ArrayList<Node> arrayPointChemin = t.donneCheminEntre(this.donnePosition(), positionJoueur);
		     			return arrayPointChemin;
					}
    	     	}
    		}
    	}
    }
	
	/**
	 * Retourne un tableau des points constituants le chemin vers le lit le plus proche
	 * @param bot Instance de notre joueur
	 * @param t Instance du plateau de jeu
	 * @return ArrayList<Node> Tableau des points
	 */
    public ArrayList<Node> litLePlusProche(Bot bot, Plateau t){
    	
    	HashMap listeLit;
    	livreChasse = null;
    	Point positionLit;
    	Point positionLitNonAdjacent = null;
    	
    	if(litChasse != null) {
    		ArrayList<Node> arrayPointChemin = t.donneCheminEntre(bot.donnePosition(), litChasse);
    		if(adjacent(litChasse)) {
    			litChasse = null;
    		}
    		return arrayPointChemin;
    	}
    	for(int i = 1;;++i){
    		listeLit = t.cherche(bot.donnePosition(), i, t.CHERCHE_LIT);
    		
    		if (!listeLit.isEmpty()) {
				ArrayList<Point> arrayPointLit = (ArrayList<Point>) listeLit.get(1);
    			for (Point p : arrayPointLit){
    				positionLit = p;
    				if(adjacent(positionLit)){
    					ArrayList<Node> arrayPointChemin = t.donneCheminEntre(bot.donnePosition(), positionLit);
    					litChasse = null;
    	     			return arrayPointChemin;
    				} else {
    					positionLitNonAdjacent = p;
    				}
    	     	}
    			if(positionLitNonAdjacent != null) {
    				ArrayList<Node> arrayPointChemin = t.donneCheminEntre(bot.donnePosition(), positionLitNonAdjacent);
    				litChasse = positionLitNonAdjacent;
    				return arrayPointChemin;
    			}
    		}
    	}
    }
    
    /**
	 * Retourne un tableau des points constituants le chemin vers le livre le plus proche
	 * @param bot Instance de notre joueur
	 * @param t Instance du plateau de jeu
	 * @return ArrayList<Node> Tableau des points
	 */
	public ArrayList<Node> livreLePlusProche(Bot bot, Plateau t){
    	int flag;
    	HashMap listeLivre;
    	litChasse = null;
    	Point positionLivre;
    	Point positionLivreNonAdjacent = null;
    	Point positionLivrePossede = null;

    	if(livreChasse != null) {
    		ArrayList<Node> arrayPointChemin = t.donneCheminEntre(bot.donnePosition(), livreChasse);
    		if(adjacent(livreChasse)) {
    			livreChasse = null;
    		}
    		return arrayPointChemin;
    	} else {
    		for(int i = 1;;++i){
        		listeLivre = t.cherche(bot.donnePosition(), i, t.CHERCHE_LIVRE);
        		if (!listeLivre.isEmpty()) {
    				ArrayList<Point> arrayPointLivres = (ArrayList<Point>) listeLivre.get(2);
        			for (Point p : arrayPointLivres) {
        	     		if(t.contientUnLivreQuiNeLuiAppartientPas(bot, t.donneContenuCellule(p))){
        	     			positionLivre = p;
        	     			if (adjacent(positionLivre)){
        	     				ArrayList<Node> arrayPointChemin = t.donneCheminEntre(bot.donnePosition(), positionLivre);
        	     				livreChasse = null;
            	     			return arrayPointChemin;
        	     			} else {
        	     				if(t.donneProprietaireDuLivre(t.donneContenuCellule(positionLivre)) != 0) {
        	     					positionLivrePossede = p;
        	     				} else {
        	     					positionLivreNonAdjacent = p;
        	     				}
        	     			}
        	     		}
        	     	}
        			if (positionLivrePossede != null) {
        				ArrayList<Node> arrayPointChemin = t.donneCheminEntre(bot.donnePosition(), positionLivrePossede);
        				livreChasse = positionLivrePossede;
        				return arrayPointChemin;
        			} else if(positionLivreNonAdjacent != null) {
        				ArrayList<Node> arrayPointChemin = t.donneCheminEntre(bot.donnePosition(), positionLivreNonAdjacent);
        				livreChasse = positionLivreNonAdjacent;
        				return arrayPointChemin;
        			}
        		}
        	}
    	}
    }
	
	public boolean adjacent(Point position){
    	if ((position.getX() == this.donnePosition().getX() + 1 && position.getY() == this.donnePosition().getY())
    			|| (position.getX() == this.donnePosition().getX()-1 && position.getY() == this.donnePosition().getY()) 
    			|| (position.getX() == this.donnePosition().getX() && position.getY() == this.donnePosition().getY() + 1)
    			|| (position.getX() == this.donnePosition().getX() && position.getY() == this.donnePosition().getY() - 1)){
    		return true;
    	}
    	else {
    		return false;
    	}
    }

	/**
	 * @return the livreChasse
	 */
	public Point getLivreChasse() {
		return livreChasse;
	}

	/**
	 * @param livreChasse the livreChasse to set
	 */
	public void setLivreChasse(Point livreChasse) {
		this.livreChasse = livreChasse;
	}

	/**
	 * @return the litChasse
	 */
	public Point getLitChasse() {
		return litChasse;
	}

	/**
	 * @param litChasse the litChasse to set
	 */
	public void setLitChasse(Point litChasse) {
		this.litChasse = litChasse;
	}
	
	

}
