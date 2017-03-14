package clientdesarenes;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Queue;

import gui.FenetreDeJeu;
import jeu.Joueur;
import jeu.MaitreDuJeu;
import jeu.Plateau;
import jeu.astar.Node;
import jeu.Joueur.Action;

public class teeeeest extends jeu.Joueur implements reseau.JoueurReseauInterface
{

	String key;
	Queue<Action> actions = new LinkedList<Action>();
	Point livreChasse;
	Point litChasse;
	Plateau t;
	Point positionJoueur;
	ArrayList<Point> enn_proches;
	ArrayList<Point> lit_proche;
	Point lit_proche_destination;
	boolean chemin_lit_ok;
	boolean pts_vie_ennemis_sup;
	boolean enn_faible_proche;
	boolean enn_fragile_proche;
	int pts_esprit;
	ArrayList<Point> livre_proche = null;
	int nbr_enn_proches;

	boolean estFaisable;


	public teeeeest(String id, String cle) {
		super(id);
        key = cle;
        livreChasse = null;
        litChasse = null;
	}
	
	

	public teeeeest() {
		super();
	}


	//Faire un report sur le joueurleplusriche afin de savoir quand un joueur proche a plein de livre afin qu'on le tue meme si c'est le dernier
	//Il existe une methode qui permet d'avoir le nombre de livre que possede un joueur, cela permettrai d'anticiper quand un joueur risque de nous doubler ou de devenir trop fort
	//Il faut pouvoir se sucider vite en cas de danger
	//Il faut eviter d'aller sur les cases respawn
	//Quand 2 livres equidistance : prendre celui du plus fort

	//IL FAUT IMPERATIVEMENT TRIER LES LISTE AFIN QUE LE PREMIER ELEMENT RETOURNE SOIT LE PLUS PROCHE DE LA POSITION COURANTE

	    
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
	    
		
		
	    public Action chercheLivre(Plateau t){
	    	ArrayList <Node> deplacementVersJoueur = this.joueurLePlusProche(t);
			ArrayList <Node> deplacementVersLivre = livreLePlusProche(t);
	        ArrayList <Node> deplacementVersJoueurFort = JoueurFortProche(t);
			
			Node n = deplacementVersJoueur.get(deplacementVersJoueur.size()-1);
			int posX = n.getPosX();
			int posY = n.getPosY();
			Joueur j = t.donneJoueurEnPosition(posX,posY);
			
			Point pFort = new Point(deplacementVersJoueurFort.get(deplacementVersJoueurFort.size()-1).getPosX(),deplacementVersJoueurFort.get(deplacementVersJoueurFort.size()-1).getPosY());
			
			
			if(deplacementVersJoueurFort.size()<4&&chercheLitProche(pFort)==false&&t.donneJoueurEnPosition(pFort).donneEsprit()+20<this.donneEsprit())
			{
				return this.direction(deplacementVersJoueurFort.get(0));
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
	    
	    public boolean chercheLitProche(Point p)
	    {
	    	HashMap listeLitProche;
	    	listeLitProche = t.cherche(p, 3, t.CHERCHE_LIVRE);
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

	
	 
	
	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 

	




	public void play()
	{
		Point positionJoueur = this.donnePosition();

        Point destinationDroite = new Point((int) positionJoueur.getX() + 1, (int) positionJoueur.getY());
        Point destinationGauche = new Point((int) positionJoueur.getX() - 1, (int) positionJoueur.getY());
        Point destinationHaut = new Point((int) positionJoueur.getX(), (int) positionJoueur.getY() - 1);
        Point destinationBas = new Point((int) positionJoueur.getX(), (int) positionJoueur.getY() + 1);

        HashMap listeLivre = t.cherche(positionJoueur, 3, t.CHERCHE_LIVRE);
        HashMap listeLivreLoin = t.cherche(positionJoueur, 10, t.CHERCHE_LIVRE);
        HashMap listeJoueur = t.cherche(positionJoueur, 3, t.CHERCHE_JOUEUR);
        HashMap listeLit = t.cherche(positionJoueur, 3, t.CHERCHE_LIT);

        if(!listeLivre.isEmpty())
        {
        	ArrayList<Point> arrayPointLivres = (ArrayList<Point>) listeLivre.get(2);
        	ArrayList<Point> livre_tres_proche = arrayPointLivres;
        	livre_tres_proche.sort(c);
        	Collections.sort(livre_tres_proche, new Comparator<Point>() {
        		@Override
        		public int compare(Point p1, Point p2)
        		{
        			if((positionJoueur.x-p1.x)+(positionJoueur.y-p1.y)>(positionJoueur.x-p2.x)+)
        			{
        				return 1;
        			}
        			else if()
        			{
        				return -1;
        			}
        			else
        			{
        				return 0;
        			}
        		}
			});
        }

        if(!listeLivreLoin.isEmpty())
        {
        	ArrayList<Point> arrayPointLivresLoin = (ArrayList<Point>) listeLivreLoin.get(2);
        	ArrayList<Point> livre_proche = arrayPointLivresLoin;
        }

        if(!listeJoueur.isEmpty())
        {
        	ArrayList<Point> arrayPointJoueurs = (ArrayList<Point>) listeJoueur.get(0);
        	ArrayList<Point> enn_proches = arrayPointJoueurs;
        }

        if(!listeLit.isEmpty())
        {
        	ArrayList<Point> arrayPointLits = (ArrayList<Point>) listeLit.get(1);
        	ArrayList<Point> lit_proche = arrayPointLits;
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
				if(pts_vie_ennemis_sup)
				{
					prio_harakiri();
				}
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

	public void prio_kamikaze(Point enn_proches)
	{
		direction(enn_proches);
	}

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
	 */

	public static void main(String[] args) {
		Plateau p;     
		//p = new Plateau(1200, MaitreDuJeu.PLATEAU_PAR_DEFAUT);
		p = Plateau.generePlateauAleatoire( 1200, 10, 4, 8, 7);

		MaitreDuJeu jeu = new MaitreDuJeu(p);



		jeu.metJoueurEnPosition(0, new IA1("Carapuce", "cli5_PASS5"));
		jeu.metJoueurEnPosition(1, new Bot_GT("Bulbizarre", "cli4_PASS4"));
		//jeu.metJoueurEnPosition(1, new jeu.Joueur("Rouge"));
		//jeu.metJoueurEnPosition(2, new jeu.Joueur("Jaune"));

		teeeeest t = new teeeeest();

		t.JoueurFortProche(p);

		/*
        FenetreDeJeu f = new FenetreDeJeu(jeu, true);
        f.setMouseClickListener((int x, int y, int bt) -> {
            System.out.println("On a cliqué sur la cellule " + x + "," + y);
        });

        java.awt.EventQueue.invokeLater(() -> {
            f.setVisible(true);
        });  */

	}

	

	/*

	public Action chercheLivre(Plateau t){
    	ArrayList <Node> deplacementVersJoueur = this.joueurLePlusProche(t);
		ArrayList <Node> deplacementVersLivre = livreLePlusProche(t);

		Node n = deplacementVersJoueur.get(deplacementVersJoueur.size()-1);
		int posX = n.getPosX();
		int posY = n.getPosY();
		Joueur j = t.donneJoueurEnPosition(posX,posY);

		// -------------------- CASE 1 : Clear --------------------- 
		if(deplacementVersJoueur.size() > 3) {
			return this.direction(deplacementVersLivre.get(0));

		// ---------------- CASE 2 : Player 3 cells ---------------- 
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

		// ---------------- CASE 3 : Player 2 cells ---------------- 
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

		// -------------- CASE 4 : Player next to bot -------------- 
		} else {
			return this.direction(deplacementVersLivre.get(0));
		}
		return this.direction(deplacementVersLivre.get(0));
    }

	 */
}
