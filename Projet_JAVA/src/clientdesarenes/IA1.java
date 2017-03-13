package clientdesarenes;

import java.awt.Point;
import java.util.HashMap;

import jeu.Joueur;
import jeu.Plateau;
import jeu.Joueur.Action;

public class IA1 extends jeu.Joueur implements reseau.JoueurReseauInterface {
	String key;
    
	IA1(String id, String cle) {
        super(id);
        key = cle;
    }
    
    @Override
    public Joueur.Action faitUneAction(Plateau t) {   
        Action a = super.faitUneAction(t);
        
        
        Point positionJoueur = this.donnePosition();
        
        Point destinationDroite = new Point((int) positionJoueur.getX() + 1, (int) positionJoueur.getY());
        Point destinationGauche = new Point((int) positionJoueur.getX() - 1, (int) positionJoueur.getY());
        Point destinationHaut = new Point((int) positionJoueur.getX(), (int) positionJoueur.getY() - 1);
        Point destinationBas = new Point((int) positionJoueur.getX(), (int) positionJoueur.getY() + 1);
        
        HashMap listeLivre = t.cherche(positionJoueur, 3, t.CHERCHE_LIVRE);
        if(!listeLivre.isEmpty()){
        	t.
        }
        Action.
        
        
        int nbrEsprit = this.donneEsprit();
        if(nbrEsprit > 80) {
        	
        } else if(nbrEsprit > 50) {
        	
        }
        else if(nbrEsprit > 30) {
        	
        } else {
        	
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
    
    
    boolean estUnObstacle (int position, Plateau t){
    	
		return false;
    }
    boolean estUnSpawn (int position, Plateau t){
		return false;
    }
    boolean estUnLivre (int position, Plateau t){
		return false;
    }
    boolean estUnLit (int position, Plateau t){
		return false;
    }
    boolean estUnPersonnage (int position, Plateau t){
		return false;
    }

}
