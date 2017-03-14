package clientdesarenes;

import java.awt.Point;
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
	Point livreChasse;
	Point litChasse;
	
	public IA1(String id, String cle) {
        super(id);
        key = cle;
        livreChasse = null;
        litChasse = null;
    }
    
	@Override
    public Joueur.Action faitUneAction(Plateau t) {           
        Action a = null;
        
        ArrayList <Node> deplacementVersLit = litLePlusProche(t);
        ArrayList <Node> deplacementVersLivre = livreLePlusProche(t);
        
        if(this.donneEsprit() < 50 && deplacementVersLit.size() <= 3 && deplacementVersLivre.size() > deplacementVersLit.size()){
        	a = chercheLit(t);
        }
        else if(this.donneEsprit() > 20 + deplacementVersLit.size()) {
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
    	Queue<Action> actions = new LinkedList<Action>();
    	actions = null;
    	livreChasse = null;
        litChasse = null;
        System.out.println("Bot: La partie est finie.");
    }

    @Override
    public void deconnecte() {
        System.out.println("Bot: On est déconnecté du serveur.");
    }
    
    /**
     * Le joueur se met dans une position de chercheur de livre
     * @param t plateau
     * @return Action � faire
     */
    public Action chercheLivre(Plateau t){
    	ArrayList <Node> deplacementVersJoueur = this.joueurLePlusProche(t);
		ArrayList <Node> deplacementVersLivre = livreLePlusProche(t);
		
		Node n = deplacementVersJoueur.get(deplacementVersJoueur.size()-1);
		int posX = n.getPosX();
		int posY = n.getPosY();
		Joueur j = t.donneJoueurEnPosition(posX,posY);

		/* -------------------- CASE 1 : Clear --------------------- */
		/* Si il n'y a pas de joueur � moins de 3 cases, on se d�place vers le livre le plus proche */
		if(deplacementVersJoueur.size() > 3) {
			return this.direction(deplacementVersLivre.get(0));
			
		/* ---------------- CASE 2 : Player 3 cells ---------------- */	
		/* Sinon si il y a un joueur � 3 cases */
		} else if (deplacementVersJoueur.size() == 3) {
			/* Si un livre se trouve � moins de 3 cases on va le chercher */
			if(deplacementVersLivre.size() <= 3){
				return this.direction(deplacementVersLivre.get(0));
			} else {
				/* Si la derni�re action �t� de rien faire */
				if(this.getActions().peek() == Action.RIEN) {
					/* Si le joueur adverse � plus de points de culture que nous et 
					 * que son esprit est inf�rieur � 20 et que notre esprit est 
					 * sup�rieur � 50, on s'approche du joueur */
					if (j.donnePointsCulture() > this.donnePointsCulture() - 20 
							&& j.donneEsprit() <= 20 && this.donneEsprit() > 50) {
						this.livreChasse = null;
						return this.direction(deplacementVersJoueur.get(0));
					/* Sinon on se d�place vers le livre le plus proche */
					} else {
						return this.direction(deplacementVersLivre.get(0));
					}
				/* Sinon on ne fait rien */
				} else {
					return Action.RIEN;
				}
			}
		
		/* ---------------- CASE 3 : Player 2 cells ---------------- */	
		} else if (deplacementVersJoueur.size() == 2) {
			if(deplacementVersLivre.size() <= 2) {
				return this.direction(deplacementVersLivre.get(0));
			} else {
				if(
					(j.donnePointsCulture() > this.donnePointsCulture() - 20 
								&& j.donneEsprit() < this.donneEsprit()) 
						|| j.donneEsprit() <= 20) {
					this.livreChasse = null;
					return this.direction(deplacementVersJoueur.get(0));
				}
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
			if(j.donneEsprit() <= 20 && this.donneEsprit() > 20) {
				this.litChasse = null;
				return this.direction(deplacementVersJoueur.get(0));
			} else {
				return this.direction(deplacementVersLit.get(0));
			}

		/* -------------- CASE 4 : Player next to bot -------------- */	
		} else {
			return this.direction(deplacementVersLit.get(0));
		}
    }
    
    /**
     * Permet de r�cup�rer le livre le plus proche suivant certains crit�res
     */
    public ArrayList<Node> livreLePlusProche(Plateau t){
    	int flag;
    	
    	HashMap listeLivre;
    	
    	this.litChasse = null;
    	
    	Point positionLivre;
    	Point positionLivreNonAdjacent = null;
    	Point positionLivrePossede = null;
    	
    	/** Si on a deja un livre en chasse */
    	if(this.livreChasse != null) {
    		ArrayList<Node> arrayPointChemin = t.donneCheminEntre(this.donnePosition(), this.livreChasse);
    		/** Si le livre en chasse est adjacent, �a veut dire que nous allons l'obtenir au prochain tour,
    		   On r�initialise donc la variable livreChasse */
    		if(adjacent(this.livreChasse)) {
    			this.livreChasse = null;
    		}
    		return arrayPointChemin;
    	} else {
    		/** On parcours les alentours de la map pour trouver des livres */
    		for(int i = 1;;++i) {
        		listeLivre = t.cherche(this.donnePosition(), i, t.CHERCHE_LIVRE);
        		/** Si on a trouv� des livres aux alentours */
        		if (!listeLivre.isEmpty()) {
    				ArrayList<Point> arrayPointLivres = (ArrayList<Point>) listeLivre.get(2);
    				/** On parcourt toutes les positions des livres trouv� */
        			for (Point p : arrayPointLivres) {
        				/** Si le livre ne nous appartient pas d�j� */
        	     		if(t.contientUnLivreQuiNeLuiAppartientPas(this, t.donneContenuCellule(p))){
        	     			positionLivre = p;
        	     			/** Si le livre est un livre adjacent, on le prend en priorit� */
        	     			if (adjacent(positionLivre)){
        	     				ArrayList<Node> arrayPointChemin = t.donneCheminEntre(this.donnePosition(), positionLivre);
        	     				this.livreChasse = null;
            	     			return arrayPointChemin;
        	     			} else {
        	     				/** Si le le livre est un livre poss�d� par quelqu'un, on le prend en priorit� */
        	     				if(t.donneProprietaireDuLivre(t.donneContenuCellule(positionLivre)) != 0) {
        	     					positionLivrePossede = p;
        	     				} else {
        	     					positionLivreNonAdjacent = p;
        	     				}
        	     			}
        	     		}
        	     	}
        			/** A la fin du parcourt des livres on v�rifie vers quel livre on se d�place
        			   Si on a trouv� un livre poss�d� par quelqu'un */
        			if (positionLivrePossede != null) {
        				ArrayList<Node> arrayPointChemin = t.donneCheminEntre(this.donnePosition(), positionLivrePossede);
        				this.livreChasse = positionLivrePossede;
        				return arrayPointChemin;
        			/** Sinon si on a trouv� un livre Non adjacent */
        			} else if(positionLivreNonAdjacent != null) {
        				ArrayList<Node> arrayPointChemin = t.donneCheminEntre(this.donnePosition(), positionLivreNonAdjacent);
        				this.livreChasse = positionLivreNonAdjacent;
        				return arrayPointChemin;
        			}
        		}
        	}//end for
    	}
    }
    
    /**
     * Permet de r�cup�rer le lit le plus proche suivant certains crit�res
     */
    public ArrayList<Node> litLePlusProche(Plateau t){
    	HashMap listeLit;
    	
    	this.livreChasse = null;
    	
    	Point positionLit;
    	Point positionLitNonAdjacent = null;
    	
    	/** Si on a deja un lit en chasse */
    	if(this.litChasse != null) {
    		ArrayList<Node> arrayPointChemin = t.donneCheminEntre(this.donnePosition(), this.litChasse);
    		/** Si le livre en chasse est adjacent, �a veut dire que nous allons l'obtenir au prochain tour,
    		   On r�initialise donc la variable livreChasse */
    		if(adjacent(this.litChasse)) {
    			this.litChasse = null;
    		}
    		return arrayPointChemin;
    	}
    	/** On parcours les alentours de la map pour trouver des lits */
    	for(int i = 1;;++i){
    		listeLit = t.cherche(this.donnePosition(), i, t.CHERCHE_LIT);
    		/** Si on a trouv� des lits aux alentours */
    		if (!listeLit.isEmpty()) {
				ArrayList<Point> arrayPointLit = (ArrayList<Point>) listeLit.get(1);
				/** On parcourt toutes les positions des lits trouv�s */
    			for (Point p : arrayPointLit){
    				positionLit = p;
    				/** Si le lit est un lit adjacent, on le prend en priorit� */
    				if(adjacent(positionLit)){
    					ArrayList<Node> arrayPointChemin = t.donneCheminEntre(this.donnePosition(), positionLit);
    					this.litChasse = null;
    	     			return arrayPointChemin;
    				} else {
    					positionLitNonAdjacent = p;
    				}
    	     	}
    			/** Si on a trouv� un lit Non adjacent */
    			if(positionLitNonAdjacent != null) {
    				ArrayList<Node> arrayPointChemin = t.donneCheminEntre(this.donnePosition(), positionLitNonAdjacent);
    				this.litChasse = positionLitNonAdjacent;
    				return arrayPointChemin;
    			}
    		}
    	}
    }
    
    /**
     * Permet de r�cup�rer le joueur le plus proche suivant certains crit�res
     */
    public ArrayList<Node> joueurLePlusProche(Plateau t){
    	HashMap listeJoueur;
    	
    	Point positionJoueur;
    	
    	/** On parcours les alentours de la map pour trouver des joueurs */
    	for(int i = 1;;++i){
    		listeJoueur = t.cherche(this.donnePosition(), i, t.CHERCHE_JOUEUR);
    		/** Si on a trouv� des joueurs aux alentours */
    		if (!listeJoueur.isEmpty()) {
				ArrayList<Point> arrayPointJoueur = (ArrayList<Point>) listeJoueur.get(4);
				/** On parcourt toutes les positions des joueurs trouv�s */
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
     * Permet de savoir si la position envoy� est une position adjacente
     */
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
     * Permet de retourner l'action permettant d'acceder � la case adjacente
     */
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
    
    /**
     * Ajoute une action � notre Queue
     */
	public void addAction(Action a) {
        actions.add(a);
    }
	
	/**
	 * R�cup�re la Queue
	 * @return the actions
	 */
	public Queue<Action> getActions() {
		return actions;
	}

}
