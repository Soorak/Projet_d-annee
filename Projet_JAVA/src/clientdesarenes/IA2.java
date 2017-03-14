package clientdesarenes;

import java.awt.Point;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import jeu.Joueur;
import jeu.Plateau;
import jeu.astar.Node;
import jeu.Joueur.Action;

public class IA2 extends jeu.Joueur implements reseau.JoueurReseauInterface {
	String key;
	Queue<Action> actions = new LinkedList<Action>();
	Point livreChasse;
	Point litChasse;
	
	IA2(String id, String cle) {
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
        System.out.println("Bot: La partie est finie.");
    }

    @Override
    public void deconnecte() {
        System.out.println("Bot: On est déconnecté du serveur.");
    }
    
    public Action chercheLivre(Plateau t){
    	ArrayList <Node> deplacementVersJoueur = this.joueurLePlusProche(t);
		ArrayList <Node> deplacementVersLivre = livreLePlusProche(t);
        ArrayList <Node> deplacementVersJoueurFort = JoueurFortProche(t);
        ArrayList <Node> deplacementVersJoueurRiche = JoueurRicheProche(t);
		
		Node n = deplacementVersJoueur.get(deplacementVersJoueur.size()-1);
		int posX = n.getPosX();
		int posY = n.getPosY();
		Joueur j = t.donneJoueurEnPosition(posX,posY);
		
		Point pFort = new Point(deplacementVersJoueurFort.get(deplacementVersJoueurFort.size()-1).getPosX(),deplacementVersJoueurFort.get(deplacementVersJoueurFort.size()-1).getPosY());
		Point pRiche = new Point(deplacementVersJoueurRiche.get(deplacementVersJoueurRiche.size()-1).getPosX(),deplacementVersJoueurRiche.get(deplacementVersJoueurRiche.size()-1).getPosY());
		
		if(deplacementVersJoueurFort.size()<4&&!chercheLitProche(t, pFort)&&t.donneJoueurEnPosition(pFort).donneEsprit()+20<this.donneEsprit())
		{
			return this.direction(deplacementVersJoueurFort.get(0));
		}
		
		if(chercheRicheProche(t)||!chercheLitProche(t, pRiche))
		{
			return this.direction(deplacementVersJoueurRiche.get(0));
		}

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
					return this.direction(deplacementVersJoueur.get(0));
				}
			}
			
		/* -------------- CASE 4 : Player next to bot -------------- */	
		} else {
			return this.direction(deplacementVersLivre.get(0));
		}
		return this.direction(deplacementVersLivre.get(0));
    }
    
    public boolean chercheLitProche(Plateau t,Point p)
    {
    	HashMap listeLitProche;
    	listeLitProche = t.cherche(p, 3, t.CHERCHE_LIT);
    	if(!listeLitProche.isEmpty())
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    	
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
    	
    	this.litChasse = null;
    	
    	Point positionLivre;
    	Point positionLivreNonAdjacent = null;
    	Point positionLivrePossede = null;
    	
    	if(this.livreChasse != null) {
    		ArrayList<Node> arrayPointChemin = t.donneCheminEntre(this.donnePosition(), this.livreChasse);
    		if(adjacent(this.livreChasse)) {
    			this.livreChasse = null;
    		}
    		return arrayPointChemin;
    	} else {
    		for(int i = 1;;++i){
        		listeLivre = t.cherche(this.donnePosition(), i, t.CHERCHE_LIVRE);
        		if (!listeLivre.isEmpty()) {
    				ArrayList<Point> arrayPointLivres = (ArrayList<Point>) listeLivre.get(2);
        			for (Point p : arrayPointLivres){    				
        	     		if(t.contientUnLivreQuiNeLuiAppartientPas(this, t.donneContenuCellule(p))){
        	     			positionLivre = p;
        	     			if (adjacent(positionLivre)){
        	     				ArrayList<Node> arrayPointChemin = t.donneCheminEntre(this.donnePosition(), positionLivre);
        	     				this.livreChasse = null;
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
        				ArrayList<Node> arrayPointChemin = t.donneCheminEntre(this.donnePosition(), positionLivrePossede);
        				this.livreChasse = positionLivrePossede;
        				return arrayPointChemin;
        			} else if(positionLivreNonAdjacent != null) {
        				ArrayList<Node> arrayPointChemin = t.donneCheminEntre(this.donnePosition(), positionLivreNonAdjacent);
        				this.livreChasse = positionLivreNonAdjacent;
        				return arrayPointChemin;
        			}
        		}
        	}
    	}
    	
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
    
    public ArrayList<Node> JoueurFortProche(Plateau t)
	{
		HashMap listeTousJoueur;
		Joueur joueurLePlusFort = new Joueur();
		Point positionJoueurLePlusFort = new Point(0,0);

		listeTousJoueur = t.cherche(positionJoueurLePlusFort, t.donneTaille(), t.CHERCHE_JOUEUR);
		ArrayList<Point> arrayTousJoueur = (ArrayList<Point>) listeTousJoueur.get(4);
		for (Point p : arrayTousJoueur) {
			Joueur temp = t.donneJoueurEnPosition(p);
			if(joueurLePlusFort.donnePointsCulture()<temp.donnePointsCulture()&&p!=this.donnePosition())
			{
				joueurLePlusFort=temp;
				positionJoueurLePlusFort = p;
			}

		}
		ArrayList<Node> arrayPointChemin = t.donneCheminEntre(this.donnePosition(), positionJoueurLePlusFort);
		return arrayPointChemin;
	}
    
    public ArrayList<Node> JoueurRicheProche(Plateau t)
    {
    	HashMap listeTousJoueur;
		Joueur joueurLePlusRiche = new Joueur();
		Point positionJoueurLePlusRiche = new Point(0,0);
		int numeroJoueur = 0;
		int nbrLivrePlusRiche = 5;
		int distanceActuelleAvecFort = 5;

		listeTousJoueur = t.cherche(positionJoueurLePlusRiche, t.donneTaille(), t.CHERCHE_JOUEUR);
		ArrayList<Point> arrayTousJoueur = (ArrayList<Point>) listeTousJoueur.get(4);
		
		
		for (Point p : arrayTousJoueur) {
			Joueur temp = t.donneJoueurEnPosition(p);
			numeroJoueur = temp.donneCouleurNumerique();
			if((t.nombreDeLivresJoueur(numeroJoueur)>nbrLivrePlusRiche&&p!=this.donnePosition()&&t.donneCheminEntre(this.donnePosition(), p).size()<5)||(this.donnePosition()!=p&&((t.donneCheminEntre(this.donnePosition(), positionJoueurLePlusRiche).size())-(t.donneCheminEntre(this.donnePosition(), p).size()))<((nbrLivrePlusRiche)-(t.nombreDeLivresJoueur(numeroJoueur)))))
			{
				nbrLivrePlusRiche = t.nombreDeLivresJoueur(numeroJoueur);
				joueurLePlusRiche=temp;
				positionJoueurLePlusRiche = p;
			}

		}
		ArrayList<Node> arrayPointChemin = t.donneCheminEntre(this.donnePosition(), positionJoueurLePlusRiche);
		return arrayPointChemin;
    }
    
    public boolean chercheRicheProche(Plateau t)
    {
    	HashMap listeJoueurProche;
    	int numeroJoueur = 0;
    	
    	listeJoueurProche = t.cherche(this.donnePosition(), 5, t.CHERCHE_JOUEUR);
    	if(listeJoueurProche.isEmpty())
    	{
    		return false;
    	}
    	else
    	{
    		ArrayList<Point> ArrayJoueurRiche = (ArrayList<Point>) listeJoueurProche.get(4);
    		
    		for (Point p : ArrayJoueurRiche)
    		{
    			Joueur temp = t.donneJoueurEnPosition(p);
    			numeroJoueur = temp.donneCouleurNumerique();
    			
    			if(t.nombreDeLivresJoueur(numeroJoueur)>5)
    			{
    				return true;
    			}
    			
    		}
    	return false;	
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
