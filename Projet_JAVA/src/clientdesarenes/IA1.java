package clientdesarenes;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import jeu.Joueur;
import jeu.Plateau;
import jeu.astar.Node;
import jeu.Joueur.Action;

public class IA1 extends jeu.Joueur implements reseau.JoueurReseauInterface {
	String key;
    
	IA1(String id, String cle) {
        super(id);
        key = cle;
    }
    
	@Override
    public Joueur.Action faitUneAction(Plateau t) {           
        Action a = null;
        
        /*int nbrEsprit = this.donneEsprit();
        if(nbrEsprit > 80) {
        	
        } else if(nbrEsprit > 50) {
        	
        }
        else if(nbrEsprit > 30) {
        	
        } else {
        	
        }*/
        
        a = direction(livreLePlusProche(t));
        
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
    
    public Node livreLePlusProche(Plateau t){
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
    	     			return arrayPointChemin.get(0);
    	     		}
    	     	}
    		}
    	}
    }
    
    public Node joueurLePlusProche(Plateau t){
    	HashMap listeJoueur;
    	
    	Point positionJoueur;
    	
    	for(int i = 1;;++i){
    		listeJoueur = t.cherche(this.donnePosition(), i, t.CHERCHE_JOUEUR);
    		
    		if (!listeLivre.isEmpty()) {
				ArrayList<Point> arrayPointLivres = (ArrayList<Point>) listeLivre.get(2);
    			for (Point p : arrayPointLivres){
    				
    	     		if(t.contientUnLivreQuiNeLuiAppartientPas(this, t.donneContenuCellule(p))){
    	     			positionLivre = p;
    	     	     	ArrayList<Node> arrayPointChemin = t.donneCheminEntre(this.donnePosition(), positionLivre);
    	     			return arrayPointChemin.get(0);
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
