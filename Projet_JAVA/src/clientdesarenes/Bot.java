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

	public Bot(String id, String cle) {
		super(id);
		key = cle;
		s = new StopWatch();
	}

	@Override
	public Joueur.Action faitUneAction(Plateau t) {
		this.s.reset();
		this.s.start();
		
		if(donneEsprit() < DistanceLitPlusProche(t) + 20) {
			this.routine = new GoLit();
			routine.act(this, t);
		} else {
			this.routine = new GoLivre();
			routine.act(this, t);
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

}
