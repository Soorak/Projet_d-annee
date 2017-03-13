package clientdesarenes;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import Routines.GoLit;
import Routines.Routine;
import jeu.Joueur;
import jeu.Plateau;
import jeu.Joueur.Action;
import jeu.astar.Node;

public class Bot_GT extends jeu.Joueur implements reseau.JoueurReseauInterface {

    String key;
    
    Bot_GT(String id, String cle) {
        super(id);
        key = cle;
    }
    
    @Override
    public Joueur.Action faitUneAction(Plateau t) {
    	Action a = super.faitUneAction(t);
    	if(donneEsprit() < DistanceLitPlusProche(t) + 20) {
    		a = direction(litLePlusProche(t));
    	} else {
    		a = direction(LivreLePlusProche(t));
    	}
        System.out.println("Bot.faitUneAction: Je joue " + a);
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
    
    public int DistanceLitPlusProche(Plateau t){
    	ArrayList<Point> lits;
		int taille_recherche = 1;
		do {
			lits = t.cherche(this.donnePosition(), taille_recherche++, Plateau.CHERCHE_LIT).get(1);
		} while (lits == null || lits.isEmpty());
		return t.donneCheminEntre(this.donnePosition(), lits.get(0)).size();
    }
    
    /**
	 * 
	 * @param t Le plateau de jeu
	 * @return
	 */
    public Node litLePlusProche(Plateau t){
    	ArrayList<Point> lits;
		int taille_recherche = 1;
		do {
			lits = t.cherche(this.donnePosition(), taille_recherche++, Plateau.CHERCHE_LIT).get(1);
		} while (lits == null || lits.isEmpty());
		System.err.println("J'ai " + donneEsprit() + "points d'esprit, je vais au lit !");
		return t.donneCheminEntre(this.donnePosition(), lits.get(0)).get(0);
    }
    
    /**
	 * 
	 * @param t Le plateau de jeu
	 * @return
	 */
    public Node joueurLePlusProche(Plateau t){
    	ArrayList<Point> joueur;
		int taille_recherche = 1;
		do {
			joueur = t.cherche(this.donnePosition(), taille_recherche++, Plateau.CHERCHE_JOUEUR).get(4);
		} while (joueur == null || joueur.isEmpty());
		System.err.println("Je vais taper un joueur !");
		return t.donneCheminEntre(this.donnePosition(), joueur.get(0)).get(0);
    }
    
    /**
	 * 
	 * @param t Le plateau de jeu
	 * @return
	 */
    public Node LivreLePlusProche(Plateau t){
    	ArrayList<Point> livre;
		int taille_recherche = 1;
		do {
			livre = t.cherche(this.donnePosition(), taille_recherche++, Plateau.CHERCHE_LIVRE).get(2);
		} while (livre == null || livre.isEmpty());
		System.err.println("Je vais m'instruire !");
		return t.donneCheminEntre(this.donnePosition(), livre.get(0)).get(0);
    }
	
	public Action direction(Node node){
    	if (node.getPosX() < this.donnePosition().getX() && node.getPosY() == this.donnePosition().getY()){
    		return Action.GAUCHE;
    	} else if (node.getPosX() > this.donnePosition().getX() && node.getPosY() == this.donnePosition().getY()){
    		return Action.DROITE;
    	} else if (node.getPosX() == this.donnePosition().getX() && node.getPosY() > this.donnePosition().getY()){
    		return Action.BAS;
    	} else if (node.getPosX() == this.donnePosition().getX() && node.getPosY() < this.donnePosition().getY()){
    		return Action.HAUT;
    	}
    	return null;
    }
}
