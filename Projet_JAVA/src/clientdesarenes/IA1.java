package clientdesarenes;

import java.awt.Point;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import jeu.Joueur;
import jeu.Plateau;
import jeu.astar.Node;
import jeu.Joueur.Action;

public class IA1 extends jeu.Joueur implements reseau.JoueurReseauInterface {
	String key;
	Queue<Action> actions = new LinkedList<Action>();
	
	IA1(String id, String cle) {
        super(id);
        key = cle;
    }
    
	@Override
    public Joueur.Action faitUneAction(Plateau t) {           
        Action a = null;
        
        if(this.donneEsprit() > 30) {
        	a = chercheLivre(t);
        } else {
        	a = chercheLit(t);
        }
        
        System.out.println("Bot.faitUneAction: Je joue " + a);
        this.addAction(a);
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
    
    public Action chercheLivre(Plateau t){
    	ArrayList <Node> deplacementVersJoueur = this.joueurLePlusProche(t);
		ArrayList <Node> deplacementVersLivre = livreLePlusProche(t);
		
		Node n = deplacementVersJoueur.get(deplacementVersJoueur.size()-1);
		int posX = n.getPosX();
		int posY = n.getPosY();
		Joueur j = t.donneJoueurEnPosition(posX,posY);

		/* -------------------- CASE 1 : Clear --------------------- */
		if(deplacementVersJoueur.size() > 3) {
			return this.direction(deplacementVersLivre.get(0));
			
		/* ---------------- CASE 2 : Player 3 cells ---------------- */	
		} else if (deplacementVersJoueur.size() == 3) {
			if(this.getActions().peek() == Action.RIEN) {
				if (j.donnePointsCulture() > this.donnePointsCulture() - 20 
						&& j.donneEsprit() <= 20 && this.donneEsprit() > 50) {
					return this.direction(deplacementVersJoueur.get(0));
				} else {
					return this.direction(deplacementVersLivre.get(0));
				}
			} else {
				return Action.RIEN;
			}
		
		/* ---------------- CASE 3 : Player 2 cells ---------------- */	
		} else if (deplacementVersJoueur.size() == 2) {
			if(
					(j.donnePointsCulture() > this.donnePointsCulture() - 20 
							&& j.donneEsprit() < this.donneEsprit()) 
					|| j.donneEsprit() <= 20) {
				return this.direction(deplacementVersJoueur.get(0));
			}

		/* -------------- CASE 4 : Player next to bot -------------- */	
		} else {
			return this.direction(deplacementVersLivre.get(0));
		}
		return this.direction(deplacementVersLivre.get(0));
    }
    
    public Action chercheLit(Plateau t){
    	ArrayList <Node> deplacementVersJoueur = this.joueurLePlusProche(t);
		ArrayList <Node> deplacementVersLit = litLePlusProche(t);
		
		Node n = deplacementVersJoueur.get(deplacementVersJoueur.size()-1);
		int posX = n.getPosX();
		int posY = n.getPosY();
		Joueur j = t.donneJoueurEnPosition(posX,posY);

		/* -------------------- CASE 1 : Clear --------------------- */
		if(deplacementVersJoueur.size() > 3) {
			return this.direction(deplacementVersLit.get(0));
			
		/* ---------------- CASE 2 : Player 3 cells ---------------- */	
		} else if (deplacementVersJoueur.size() == 3) {
			if(this.getActions().peek() == Action.RIEN) {
				return this.direction(deplacementVersLit.get(0));
			} else {
				if(this.donneEsprit() > 15){
					return Action.RIEN;
				} else {
					return this.direction(deplacementVersLit.get(0));
				}
			}
		
		/* ---------------- CASE 3 : Player 2 cells ---------------- */	
		} else if (deplacementVersJoueur.size() == 2) {
			if(j.donneEsprit() <= 20) {
				return this.direction(deplacementVersJoueur.get(0));
			} else {
				return this.direction(deplacementVersLit.get(0));
			}

		/* -------------- CASE 4 : Player next to bot -------------- */	
		} else {
			return this.direction(deplacementVersLit.get(0));
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
    	return Action.RIEN;
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

}
