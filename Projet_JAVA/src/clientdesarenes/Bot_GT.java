package clientdesarenes;

import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    	rÈcuperer_stats(t);
    	Action a = null;
    	try {
	    	int ptEsprit = donneEsprit();
	    	Point litProche = litLePlusProche(t);
	    	Point livreProche = livreLePlusProche_V2(t);
	    	if(possibleChercherLivre(livreProche, litProche, ptEsprit, t)) {
	    		//a = direction(t.donneCheminAvecObstacleSupplementaires(this.donnePosition(), livreProche, joueurLePlusProche(t)).get(0));
	    		a = direction(t.donneCheminEntre(this.donnePosition(), livreProche).get(0));
	    	} else {
	    		//a = direction(t.donneCheminAvecObstacleSupplementaires(this.donnePosition(), litProche, joueurLePlusProche(t)).get(0));
	    		a = direction(t.donneCheminEntre(this.donnePosition(), litProche).get(0));
	    	}
	        System.out.println("Bot.faitUneAction: Je joue " + a);
    	} catch (NullPointerException e) {
    		e.printStackTrace();
    	}
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
        System.out.println("Bot: On est d√©connect√© du serveur.");
    }
    
    public int DistanceLitPlusProche(Plateau t){
    	ArrayList<Point> lits;
		int taille_recherche = 1;
		do {
			lits = t.cherche(this.donnePosition(), taille_recherche++, Plateau.CHERCHE_LIT).get(1);
		} while (lits == null || lits.isEmpty());
		return t.donneCheminEntre(this.donnePosition(), lits.get(0)).size();
    }
    
    public Point litLePlusProche(Plateau t){
    	HashMap<Integer,ArrayList<Point>> listeLit;
    	
    	for(int i = 1;;++i){
    		listeLit = t.cherche(this.donnePosition(), i, Plateau.CHERCHE_LIT);
    		
    		if (!listeLit.isEmpty()) {
				ArrayList<Point> arrayPointLit = (ArrayList<Point>) listeLit.get(1);
    			for (Point p : arrayPointLit){
	     			return p;
    	     	}
    		}
    	}
    }
    
    public ArrayList<Node> joueurLePlusProche(Plateau t){
    	HashMap<Integer,ArrayList<Point>> listeJoueur;
    	
    	Point positionJoueur;
    	
    	for(int i = 1;;++i){
    		listeJoueur = t.cherche(this.donnePosition(), i, Plateau.CHERCHE_JOUEUR);
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
    	HashMap<Integer,ArrayList<Point>> listeLivre;
    	
    	Point positionLivre;
    	
    	for(int i = 1;;++i){
    		listeLivre = t.cherche(this.donnePosition(), i, t.CHERCHE_LIVRE);
    		if (!listeLivre.isEmpty()) {
				ArrayList<Point> arrayPointLivres = (ArrayList<Point>) listeLivre.get(2);
    			for (Point p : arrayPointLivres){
    				
    	     		if(Plateau.contientUnLivreQuiNeLuiAppartientPas(this, t.donneContenuCellule(p))){
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
	
	public Point livreLePlusProche_V2(Plateau t) {
		ArrayList<Point> listeLivre = t.cherche(new Point(0,0), t.donneTaille(), Plateau.CHERCHE_LIVRE).get(2);
		Point plusProche = listeLivre.get(0);
		for(Point p : listeLivre) {
			if(Plateau.contientUnLivreQuiNeLuiAppartientPas(this, t.donneContenuCellule(p))) {
				if(t.donneCheminEntre(this.donnePosition(), plusProche).size() > t.donneCheminEntre(this.donnePosition(), p).size()) {
					plusProche = p;
				}
			}
		}
		return plusProche;
	}
	
	public boolean possibleChercherLivre(Point livre, Point lit, int esprit, Plateau t) {
		System.err.println(joueurLePlusProche(t));
		t.donneGrillePourAstar();
		ArrayList<Node> cheminLivre = t.donneCheminAvecObstacleSupplementaires(this.donnePosition(), livre, joueurLePlusProche(t));
		int dstLitDepuisLit = t.donneCheminEntre(livre, lit).size();
		if(cheminLivre == null || cheminLivre.isEmpty()) {
			if( (t.donneCheminEntre(this.donnePosition(), livre).size() + dstLitDepuisLit + 20) < esprit)
				return true;
		} else {
			if((cheminLivre.size() + dstLitDepuisLit + 20) < esprit)
				return true;
		}
		return false;
	}
	
	public void rÈcuperer_stats (Plateau t) {
		File dossier = new File("./stats");
		File[] fichiers = dossier.listFiles();
		File file = new File("./stats/" + (fichiers.length-1));
		try {
			FileWriter writer = new FileWriter(file, true);
			String toWrite = t.donneTourCourant() + ";" + this.donnePointsCulture() +"\n";
			writer.write(toWrite);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
