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
    	Action a = null;
    	if(donneEsprit() < DistanceLitPlusProche(t) + 40) {
    		a = direction(litLePlusProche(t).get(0));
    	} else {
    		a = direction(livreLePlusProche(t).get(0));
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
    
    public ArrayList<Node> litLePlusProche(Plateau t){
    	HashMap listeLit;
    	
    	Point positionLit;
    	
    	for(int i = 1;;++i){
    		listeLit = t.cherche(this.donnePosition(), i, t.CHERCHE_LIT);
    		
    		if (!listeLit.isEmpty()) {
				ArrayList<Point> arrayPointLit = (ArrayList<Point>) listeLit.get(1);
    			for (Point p : arrayPointLit){
    				positionLit = p;
	     	     	ArrayList<Node> arrayPointChemin = t.donneCheminEntre(this.donnePosition(), positionLit);
	     			return arrayPointChemin;
    	     	}
    		}
    	}
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
    
    public ArrayList<Node> livreLePlusProche(Plateau t){
    	HashMap listeLivre;
    	
    	Point positionLivre;
    	
    	for(int i = 1;;++i){
    		listeLivre = t.cherche(this.donnePosition(), i, t.CHERCHE_LIVRE);
    		if (!listeLivre.isEmpty()) {
				ArrayList<Point> arrayPointLivres = (ArrayList<Point>) listeLivre.get(2);
    			for (Point p : arrayPointLivres){
    				
    	     		if(t.contientUnLivreQuiNeLuiAppartientPas(this, t.donneContenuCellule(p))){
    	     			positionLivre = p;
    	     	     	ArrayList<Node> arrayPointChemin = t.donneCheminEntre(this.donnePosition(), positionLivre);
    	     			return arrayPointChemin;
    	     		}
    	     	}
    		}
    	}
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
