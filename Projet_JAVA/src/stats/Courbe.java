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

public class Courbe extends Applet {
	
	private Label l;
	
	public void init () {
		l = new Label();
		l.setBackground(new Color(132,179,255));
		this.add(l);
	}
	
	public void paint(Graphics g) {
		File dossier = new File("../stats/");
		File[] fichiers = dossier.listFiles();
		
		int[][] values = new int[50][300]; // 50 fichier max, 301 lignes max
		int[] sumCult = new int[300]; // 301 valeurs max
		int[] avgCult = new int[300]; // 301 moyennes max
		
		String line;
		for(File f : fichiers) {
			InputStream fis;
			try {
				fis = new FileInputStream(f);
			    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			    BufferedReader br = new BufferedReader(isr);
			    while ((line = br.readLine()) != null) {
			        String[] strings = line.split(";");
			        values[Integer.parseInt(f.getName())][(Integer.parseInt(strings[0])/4)] = Integer.parseInt(strings[1]);
			    }
		    } catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// Calcul de la somme de tout les tour
		for(int i = 0; i < values.length; i++) {
			int y = 0;
			for(Integer val : values[i]) {
				sumCult[y++] += val;
			}
		}
		
		int plusGrandeValeur = 0;
		for(int i = 0; i < avgCult.length; i++) {
			avgCult[i] = sumCult[i] / fichiers.length;
			if(avgCult[i] > plusGrandeValeur) plusGrandeValeur = avgCult[i];
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
		
		for(int i = 0; i < avgCult.length; i++) {
			avgCult[i] = sumCult[i] / fichiers.length;
			if(i != 0) {
				g.drawLine(avgCult[i-1]/denominateur,(300-(i-1)),avgCult[i]/denominateur,(300-i));
			}
		}

		this.setSize(plusGrandeValeur/denominateur + 20, 320);
		l.setText("Tour 300 : " + plusGrandeValeur + " pts de culture (en moyenne)");
	}
}
