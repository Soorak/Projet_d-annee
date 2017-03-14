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
	
	IA1(String id, String cle) {
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
        System.out.println("Bot: On est dÃ©connectÃ© du serveur.");
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
			if(deplacementVersLivre.size() <= 3){
				return this.direction(deplacementVersLivre.get(0));
			} else {
				if(this.getActions().peek() == Action.RIEN) {
					if (j.donnePointsCulture() > this.donnePointsCulture() - 20 
							&& j.donneEsprit() <= 20 && this.donneEsprit() > 50) {
						this.livreChasse = null;
						return this.direction(deplacementVersJoueur.get(0));
					} else {
						return this.direction(deplacementVersLivre.get(0));
					}
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
    
    /*
     * Permet de récupérer le livre le plus proche suivant certains critères
     */
    public ArrayList<Node> livreLePlusProche(Plateau t){
    	int flag;
    	
    	HashMap listeLivre;
    	
    	this.litChasse = null;
    	
    	Point positionLivre;
    	Point positionLivreNonAdjacent = null;
    	Point positionLivrePossede = null;
    	
    	//Si on a deja un livre en chasse
    	if(this.livreChasse != null) {
    		ArrayList<Node> arrayPointChemin = t.donneCheminEntre(this.donnePosition(), this.livreChasse);
    		//Si le livre en chasse est adjacent, ça veut dire que nous allons l'obtenir au prochain tour,
    		//On réinitialise donc la variable livreChasse
    		if(adjacent(this.livreChasse)) {
    			this.livreChasse = null;
    		}
    		return arrayPointChemin;
    	} else {
    		//On parcours les alentours de la map pour trouver des livres
    		for(int i = 1;;++i){
        		listeLivre = t.cherche(this.donnePosition(), i, t.CHERCHE_LIVRE);
        		//Si on a trouvé des livres aux alentours
        		if (!listeLivre.isEmpty()) {
    				ArrayList<Point> arrayPointLivres = (ArrayList<Point>) listeLivre.get(2);
    				//On parcourt toutes les positions des livres trouvé
        			for (Point p : arrayPointLivres) {
        				//Si le livre ne nous appartient pas déjà
        	     		if(t.contientUnLivreQuiNeLuiAppartientPas(this, t.donneContenuCellule(p))){
        	     			positionLivre = p;
        	     			//Si le livre est un livre adjacent, on le prend en priorité
        	     			if (adjacent(positionLivre)){
        	     				ArrayList<Node> arrayPointChemin = t.donneCheminEntre(this.donnePosition(), positionLivre);
        	     				this.livreChasse = null;
            	     			return arrayPointChemin;
        	     			} else {
        	     				//Si le le livre est un livre possédé par quelqu'un, on le prend en priorité
        	     				if(t.donneProprietaireDuLivre(t.donneContenuCellule(positionLivre)) != 0) {
        	     					positionLivrePossede = p;
        	     				} else {
        	     					positionLivreNonAdjacent = p;
        	     				}
        	     			}
        	     		}
        	     	}
        			//A la fin du parcourt des livres on vérifie vers quel livre on se déplace
        			//Si on a trouvé un livre possédé par quelqu'un
        			if (positionLivrePossede != null) {
        				ArrayList<Node> arrayPointChemin = t.donneCheminEntre(this.donnePosition(), positionLivrePossede);
        				this.livreChasse = positionLivrePossede;
        				return arrayPointChemin;
        			//Sinon si on a trouvé un livre Non adjacent
        			} else if(positionLivreNonAdjacent != null) {
        				ArrayList<Node> arrayPointChemin = t.donneCheminEntre(this.donnePosition(), positionLivreNonAdjacent);
        				this.livreChasse = positionLivreNonAdjacent;
        				return arrayPointChemin;
        			}
        		}
        	}//end for
    	}
    }
    
    public int rechercheEnvironnement(int type, int taille, Point position, Plateau t){
    	HashMap liste;
    	liste = t.cherche(position, taille, type);
    	return liste.size();
    }
    
    public ArrayList<Node> litLePlusProche(Plateau t){
    	HashMap listeLit;
    	
    	this.livreChasse = null;
    	
    	Point positionLit;
    	Point positionLitNonAdjacent = null;
    	if(this.litChasse != null) {
    		ArrayList<Node> arrayPointChemin = t.donneCheminEntre(this.donnePosition(), this.litChasse);
    		if(adjacent(this.litChasse)) {
    			this.litChasse = null;
    		}
    		return arrayPointChemin;
    	}
    	for(int i = 1;;++i){
    		listeLit = t.cherche(this.donnePosition(), i, t.CHERCHE_LIT);
    		
    		if (!listeLit.isEmpty()) {
				ArrayList<Point> arrayPointLit = (ArrayList<Point>) listeLit.get(1);
    			for (Point p : arrayPointLit){
    				positionLit = p;
    				if(adjacent(positionLit)){
    					ArrayList<Node> arrayPointChemin = t.donneCheminEntre(this.donnePosition(), positionLit);
    					this.litChasse = null;
    	     			return arrayPointChemin;
    				} else {
    					positionLitNonAdjacent = p;
    				}
    	     	}
    			if(positionLitNonAdjacent != null) {
    				ArrayList<Node> arrayPointChemin = t.donneCheminEntre(this.donnePosition(), positionLitNonAdjacent);
    				this.litChasse = positionLitNonAdjacent;
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
