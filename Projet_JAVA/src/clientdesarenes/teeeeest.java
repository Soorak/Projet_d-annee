package clientdesarenes;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import jeu.Joueur;
import jeu.Plateau;
import jeu.astar.Node;
import jeu.Joueur.Action;

public class teeeeest extends jeu.Joueur implements reseau.JoueurReseauInterface
{

	Plateau t;
	Point positionJoueur = this.donnePosition();
	ArrayList<Point> enn_proches = (ArrayList<Point>) t.cherche(positionJoueur, 3, t.CHERCHE_JOUEUR).get(0);
	Point lit_proche;
	ArrayList<Point> livre_tres_proche = (ArrayList<Point>) t.cherche(positionJoueur, 3, t.CHERCHE_LIVRE).get(2);
	Point lit_proche_destination;
	boolean chemin_lit_ok;
	boolean pts_vie_ennemis_sup;
	boolean enn_faible_proche;
	boolean enn_fragile_proche;
	int pts_esprit;
	Point livre_proche = null;
	int nbr_enn_proches;

	
	public void play()
	{
		Point positionJoueur = this.donnePosition();
        
        Point destinationDroite = new Point((int) positionJoueur.getX() + 1, (int) positionJoueur.getY());
        Point destinationGauche = new Point((int) positionJoueur.getX() - 1, (int) positionJoueur.getY());
        Point destinationHaut = new Point((int) positionJoueur.getX(), (int) positionJoueur.getY() - 1);
        Point destinationBas = new Point((int) positionJoueur.getX(), (int) positionJoueur.getY() + 1);
        
        HashMap listeLivre = t.cherche(positionJoueur, 3, t.CHERCHE_LIVRE);
        HashMap listeJoueur = t.cherche(positionJoueur, 3, t.CHERCHE_JOUEUR);
        HashMap listeLit = t.cherche(positionJoueur, 3, t.CHERCHE_LIT);
		
        if(!listeLivre.isEmpty())
        {
        	ArrayList<Point> arrayPointLivres = (ArrayList<Point>) listeLivre.get(2);	
        }
        
        if(!listeJoueur.isEmpty())
        {
        	ArrayList<Point> arrayPointJoueurs = (ArrayList<Point>) listeJoueur.get(0);
        }
        
        if(!listeLit.isEmpty())
        {
        	ArrayList<Point> arrayPointLits = (ArrayList<Point>) listeLit.get(1);
        }
        
        
        //CALCUL DE LA SITUATION
		if(pts_esprit>80)
		{
			situation_avantageuse();
		}
		else if(pts_esprit>20)
		{
			situation_moyenne();
		}
		else if(pts_esprit<=20)
		{
			situation_dangereuse();
		}
	}

	
	public int pts_de_vie_enn_zone(Point p)
    {
		HashMap ListeEnnZone = t.cherche(p, 3, t.CHERCHE_JOUEUR);
		
		if(!ListeEnnZone.isEmpty())
        {
        	ArrayList<Point> arrayPointJoueurs = (ArrayList<Point>) ListeEnnZone.get(0);
        	int sommePdv = 0;
        	
        	for (Point pointTest : arrayPointJoueurs) 
        	{
        		Joueur JoueurEnCours = t.donneJoueurEnPosition(pointTest);
        		sommePdv += JoueurEnCours.donneEsprit();			
			}
        	return sommePdv;
        }
    }
	

	public void situation_dangereuse()
	{

		if(!chemin_lit_ok)
		{
			prio_harakiri();
		}
		else
		{
			if(!enn_proches)
			{
				prio_lit();
			}
			else
			{
				/*if(pts_vie_ennemis_sup)
				{
					prio_harakiri();
				}*/
				//CODER KAMIKAZE
				
				prio_harakiri();
					
			}
		}
	}
	
	public void situation_moyenne()
	{
		ArrayList<Point> enn_proches = (ArrayList<Point>) t.cherche(positionJoueur, 3, t.CHERCHE_JOUEUR).get(0);
		
		if(livre_tres_proche != null)
		{
			prio_livre(livre_tres_proche);
		}
		else if(enn_proches != null)
		{
			prio_combat(enn_proches);
		}
		else 
		{
			prio_livre(livre_proche);
		}
	}
	
	public void situation_avantageuse()
	{
		if(enn_fragile_proche)
		{
			prio_enn_fragile(enn_fragile_proche);
		}
		else if(enn_faible_proche)
		{
			prio_enn_faible(enn_faible_proche);
		}
		else if(livre_proche != null)
		{
			prio_livre(livre_proche);
		}
		
	}
	
	public void prio_harakiri()
	{
		if(livre_proche != null)
		{
			direction(livre_proche);
		}
		else
		{
			Point point_de_fuite = pointFuite();
			direction(point_de_fuite);
		}
		
	}
	
	/*public void prio_kamikaze(Point enn_proches)
	{
		direction(enn_proches);
	}*/
	
	public void prio_lit(Node lit_proche)
	{
		direction(lit_proche);
	}
	
	public void prio_enn_fragile(Node enn_fragile_proche)
	{
		direction(enn_fragile_proche);
	}
	
	public void prio_enn_faible(Node enn_faible_proche)
	{
		direction(enn_faible_proche);
	}
	
	public void prio_livre(Node livre_proche)
	{
		direction(livre_proche);
	}
	
	public void prio_combat(Node enn_proches)
	{
		direction(enn_proches);
	}
	
	public void pointFuite()
	{
		
	}


	@Override
	public void debutNouvellePartie() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void deconnecte() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String donneCle() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String donneID() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void finDeLaPartie(Plateau arg0) {
		// TODO Auto-generated method stub
		
	}
}
