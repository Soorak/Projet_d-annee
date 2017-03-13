package Routines;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import clientdesarenes.Bot;
import jeu.Plateau;
import jeu.Joueur.Action;
import jeu.astar.Node;

public abstract class Routine {
	
    public enum RoutineState {
        Success,
        Failure,
        Running
    }

    protected RoutineState state;

    protected Routine() { }

    public void start() {
        System.out.println(">>> Starting routine: " + this.getClass().getSimpleName());
        this.state = RoutineState.Running;
    }

    public abstract void act(Bot droid, Plateau board);

    public Action direction(Bot bot, Node node){
    	if (node.getPosX() < bot.donnePosition().getX() && node.getPosY() == bot.donnePosition().getY()){
    		return Action.GAUCHE;
    	} else if (node.getPosX() > bot.donnePosition().getX() && node.getPosY() == bot.donnePosition().getY()){
    		return Action.DROITE;
    	} else if (node.getPosX() == bot.donnePosition().getX() && node.getPosY() > bot.donnePosition().getY()){
    		return Action.BAS;
    	} else if (node.getPosX() == bot.donnePosition().getX() && node.getPosY() < bot.donnePosition().getY()){
    		return Action.HAUT;
    	}
    	return Action.RIEN;
    }
    
    protected void succeed() {
        System.out.println(">>> Routine: " + this.getClass().getSimpleName() + " SUCCEEDED");
        this.state = RoutineState.Success;
    }

    protected void fail() {
        System.out.println(">>> Routine: " + this.getClass().getSimpleName() + " FAILED");
        this.state = RoutineState.Failure;
    }

    public boolean isSuccess() {
        return state.equals(RoutineState.Success);
    }

    public boolean isFailure() {
        return state.equals(RoutineState.Failure);
    }

    public boolean isRunning() {
        return state.equals(RoutineState.Running);
    }

    public RoutineState getState() {
        return state;
    }
    
    public ArrayList<Node> joueurLePlusProche(Bot bot, Plateau t){
    	HashMap listeJoueur;
    	
    	Point positionJoueur;
    	
    	for(int i = 1;;++i){
    		listeJoueur = t.cherche(bot.donnePosition(), i, t.CHERCHE_JOUEUR);
    		if (!listeJoueur.isEmpty()) {
    			
				ArrayList<Point> arrayPointJoueur = (ArrayList<Point>) listeJoueur.get(4);
    			for (Point p : arrayPointJoueur){
					positionJoueur = p;
					if(!positionJoueur.equals(bot.donnePosition())){
						ArrayList<Node> arrayPointChemin = t.donneCheminEntre(bot.donnePosition(), positionJoueur);
		     			return arrayPointChemin;
					}
    	     	}
    		}
    	}
    }
}