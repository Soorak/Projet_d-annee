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
	Routine routineLit;
	Routine routineLivre;
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
		
		this.routineLit = new GoLit(this, t);
		this.routineLivre = new GoLivre(this, t);
		
		ArrayList <Node> deplacementVersLit = routineLit.litLePlusProche(this, t);
        ArrayList <Node> deplacementVersLivre = routineLivre.livreLePlusProche(this, t);
		
		if(this.donneEsprit() < 50 && deplacementVersLit.size() <= 3 && deplacementVersLivre.size() > deplacementVersLit.size()) {
			routineLit.act();
		} else if(this.donneEsprit() > 20 + deplacementVersLit.size()) {
			routineLivre.act();
		} else {
			routineLit.act();
		}
		
		this.s.stop();
		System.out.println("Bot.faitUneAction: Je joue " + a);
		System.out.println("Temps décision : " + s.getTime(TimeUnit.MILLISECONDS) + "ms");
		actions.add(a);
		return a;
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
