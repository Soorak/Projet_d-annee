package clientdesarenes;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import jeu.Joueur;
import jeu.Plateau;
import jeu.astar.Node;
import jeu.Joueur.Action;

public class teeeeest 
{

	Plateau t;
	boolean enn_proches;
	Point lit_proche;
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
		if(pts_esprit>20)
		{
			situation_moyenne();
		}
		if(pts_esprit<=20)
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
				if(pts_vie_ennemis_sup)
				{
					prio_harakiri();
				}
				else
				{
					prio_kamikaze();
				}	
			}
		}
	}
	
	public void situation_moyenne()
	{
		
	}
	
	public void situation_avantageuse()
	{
		if(enn_fragile_proche)
		{
			prio_enn_fragile();
		}
		if(enn_faible_proche)
		{
			prio_enn_faible();
		}
		if(livre_proche != null)
		{
			prio_livre();
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
	
	public void prio_kamikaze()
	{
		direction(enn_proches);
	}
	
	public void prio_lit()
	{
		direction(lit_proche);
	}
	
	public void prio_enn_fragile()
	{
		direction(enn_fragile_proche);
	}
	
	public void prio_enn_faible()
	{
		direction(enn_faible_proche);
	}
	
	public void prio_livre()
	{
		direction(livre_proche);
	}
	
}
