package clientdesarenes;

import java.awt.Desktop.Action;

import Routines.Routine;
import jeu.Joueur;
import jeu.Plateau;

public class BotJD extends jeu.Joueur implements reseau.JoueurReseauInterface {

    String key;
    Routine routine;
    Action a;
    
    BotJD(String id, String cle) {
        super(id);
        key = cle;
    }
    
    @Override
    public Joueur.Action faitUneAction(Plateau t) {   
        
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
    
    public void setActionHaut() {
    	this.a = Action.HAUT;
    }
    
    /* -- ROUTINES WORK -- */
    public Routine getRoutine() {
        return routine;
    }

    public void setRoutine(Routine routine) {
        this.routine = routine;
    }
}
