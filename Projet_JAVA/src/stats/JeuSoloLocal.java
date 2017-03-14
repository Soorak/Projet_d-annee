package stats;

import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import clientdesarenes.Bot;
import jeu.Joueur;
import jeu.MaitreDuJeu;
import jeu.MaitreDuJeuListener;
import jeu.Plateau;

public class JeuSoloLocal {

    public static void main(String[] args) {
        Plateau p;     
        //p = new Plateau(1200, MaitreDuJeu.PLATEAU_PAR_DEFAUT);
        p = Plateau.generePlateauAleatoire( 1200, 10, 4, 8, 7);
        
        MaitreDuJeu jeu = new MaitreDuJeu(p);
        
        jeu.metJoueurEnPosition(0, new Bot("Carapuce", "cli5_PASS5"));
        jeu.metJoueurEnPosition(1, new Bot("Bulbizarre", "cli4_PASS4"));
        //jeu.metJoueurEnPosition(1, new jeu.Joueur("Rouge"));
        //jeu.metJoueurEnPosition(2, new jeu.Joueur("Jaune"));
        
        /*
        FenetreDeJeu f = new FenetreDeJeu(jeu, true);
        f.setMouseClickListener((int x, int y, int bt) -> {
            System.out.println("On a cliqué sur la cellule " + x + "," + y);
        });
        
        java.awt.EventQueue.invokeLater(() -> {
            f.setVisible(true);
        }); 
        */
        
        if(p.donneTourCourant() == 0) {
	        File dossier = new File("./statistiques/");
	        if(!dossier.exists()) dossier.mkdir();
	        for(int i = 0; i < 4; i++) {
	        	File doss_jou = new File(dossier.getPath() + "/" + i + "/");
	        	if(!doss_jou.exists()) doss_jou.mkdir();
	        	File[] fichiers = doss_jou.listFiles();
				File file = new File(doss_jou.getPath() + "/" + fichiers.length);
				if(!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
	        }
        }

        jeu.addEcouteurDuJeu( new MaitreDuJeuListener() {
            @Override
            public void afficheSymbole(MaitreDuJeu arg0, Symboles arg1, Point arg2, int arg3, int arg4) {                
            }

            @Override
            public void nouveauMessage(MaitreDuJeu arg0, String arg1) {    
                System.out.println( arg1);
            }

            @Override
            public void unJeuAChange(MaitreDuJeu arg0) {
                if ( arg0.donneTourCourant() == 1200 ) {
                    System.out.println("****** Fin de la partie ******");
                    for (int i = 0; i < 4; i++) {
                        System.out.println( "Joueur"
                            + i +": " + arg0.donneJoueur(i).donnePointsCulture());                        
                    }
                    //System.out.println( arg0.donneInfos());
                    //System.exit(0);
                } else {
                	recuperer_stats(p, p.donneJoueur(p.donneJoueurCourant()));
                }
            }});
        jeu.continueLaPartie(true);
    }
    
    public static void recuperer_stats (Plateau t, Joueur joueur) {
		File dossier = new File("./statistiques/" + joueur.donneCouleurNumerique() + "/" );
		File[] fichiers = dossier.listFiles();
		File file = new File(dossier.getPath() + "/" + (fichiers.length-1));
		try {
			FileWriter writer = new FileWriter(file, true);
			// On récupère les informations (tour, pt culture, pt esprit) du joueur 1
			String toWrite = t.donneTourCourant() + ";" + joueur.donnePointsCulture() + ";" + joueur.donneEsprit()+"\n";
			writer.write(toWrite);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}