package clientdesarenes;

import java.io.File;
import java.io.IOException;

import gui.FenetreDeJeu;
import jeu.MaitreDuJeu;
import jeu.Plateau;

public class JeuSoloLocal {
    
    public static void main(String[] args) {
        Plateau p;     
        //p = new Plateau(1200, MaitreDuJeu.PLATEAU_PAR_DEFAUT);
        p = Plateau.generePlateauAleatoire( 1200, 10, 4, 8, 7);
        
        MaitreDuJeu jeu = new MaitreDuJeu(p);
        
        jeu.metJoueurEnPosition(0, new IA1("Carapuce", "cli5_PASS5"));
        jeu.metJoueurEnPosition(1, new Bot_GT("Bulbizarre", "cli4_PASS4"));
        //jeu.metJoueurEnPosition(1, new jeu.Joueur("Rouge"));
        //jeu.metJoueurEnPosition(2, new jeu.Joueur("Jaune"));
       
        FenetreDeJeu f = new FenetreDeJeu(jeu, true);
        f.setMouseClickListener((int x, int y, int bt) -> {
            System.out.println("On a cliqué sur la cellule " + x + "," + y);
        });
        
        java.awt.EventQueue.invokeLater(() -> {
            f.setVisible(true);
        });  
        
        File dossier = new File("./stats");
		File[] fichiers = dossier.listFiles();
		File file = new File("./stats/" + fichiers.length);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
}
