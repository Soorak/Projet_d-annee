package stats;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Label;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class Courbe extends Applet{
	
	private Label l;
	
	public void init () {
		l = new Label();
		l.setBackground(new Color(132,179,255));
		this.add(l);
	}
	
	public void paint(Graphics g) {
		File dossier = new File("../statistiques/");
		File[] fichiers = dossier.listFiles();
		
		int[][][] values = new int[4][51][300]; // 4 joueurs, 50 fichier max, 301 lignes max
		int[][] sumCult = new int[4][300]; // 4 joueurs, 300 valeurs max
		int[][] avgCult = new int[4][300]; // 4 joueurs, 300 moyennes max
		
		String line;
		for(File dos : fichiers) {
			File[] f_joueurs = dos.listFiles();
			for(File f : f_joueurs) {
				InputStream fis;
				try {
					fis = new FileInputStream(f);
				    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
				    BufferedReader br = new BufferedReader(isr);
				    while ((line = br.readLine()) != null) {
				        String[] strings = line.split(";");
				        values[Integer.parseInt(dos.getName())][Integer.parseInt(f.getName())][(Integer.parseInt(strings[0])/4)] = Integer.parseInt(strings[1]);
				    }
			    } catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		// Calcul de la somme de tout les tour
		for(int i = 0; i < fichiers.length; i++) {
			for(int j = 0; j < values.length; j++) {
				int k = 0;
				for(Integer val : values[i][j]) {
					sumCult[i][k++] += val;
				}
			}
		}
		
		int plusGrandeValeur = 0;
		for(int i = 0; i < fichiers.length; i++) {
			for(int j = 0; j < avgCult[i].length; j++) {
				avgCult[i][j] = sumCult[i][j] / fichiers.length;
				if(avgCult[i][j] > plusGrandeValeur) plusGrandeValeur = avgCult[i][j];
			}
		}
		
		int denominateur = 2;
		if(plusGrandeValeur > 800 && plusGrandeValeur < 1600) {
			denominateur = 3;
		} else if (plusGrandeValeur > 1600 && plusGrandeValeur < 2400) {
			denominateur = 4;
		} else if (plusGrandeValeur > 2400 && plusGrandeValeur < 3200) {
			denominateur = 5;
		} else if (plusGrandeValeur > 3200 && plusGrandeValeur < 4000) {
			denominateur = 6;
		} else if (plusGrandeValeur > 4000){
			denominateur = 7;
		}
		
		for(int i = 0; i < fichiers.length; i++) {
			switch(i) {
				case 0 :
					g.setColor(Color.BLUE);
					break;
				case 1 :
					g.setColor(Color.GREEN);
					break;
				case 2 :
					g.setColor(Color.RED);
					break;
				case 3 :
					g.setColor(new Color(232, 242, 43));
					break;
			}
			for(int j = 0; j < avgCult[i].length; j++) {
				avgCult[i][j] = sumCult[i][j] / fichiers.length;
				if(j != 0) {
					g.drawLine((j-1),(plusGrandeValeur-avgCult[i][j-1])/denominateur,j,(plusGrandeValeur-avgCult[i][j])/denominateur);
				}
			}
		}

		this.setSize(320, plusGrandeValeur/denominateur + 20);
		l.setText("Tour 300 : " + plusGrandeValeur + " pts de culture (en moyenne)");
	}
}
