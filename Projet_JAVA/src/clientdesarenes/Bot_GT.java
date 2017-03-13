package clientdesarenes;

import java.awt.Point;
import java.util.ArrayList;

import jeu.Joueur;
import jeu.Plateau;

public class Bot_GT extends jeu.Joueur implements reseau.JoueurReseauInterface {

    String key;
    
    Bot_GT(String id, String cle) {
        super(id);
        key = cle;
    }
    
    @Override
    public Joueur.Action faitUneAction(Plateau t) {
    	if(donneEsprit() < 30) {
    		System.err.println("J'ai " + donneEsprit() + "points d'esprit, je vais au lit !");
    		ArrayList<Point> lits = t.cherche(this.donnePosition(), 10, Plateau.CHERCHE_LIT).get(1);
    		t.donneCheminEntre(this.donnePosition(), lits.get(0));
    	}
        Action a = super.faitUneAction(t);
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
}
