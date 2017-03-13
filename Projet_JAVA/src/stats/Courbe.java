package stats;

import java.applet.Applet;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class Courbe extends Applet {

	//La formule de ta courbe :
	double f(int tour, int culture) {
		return culture/tour;
	}

	public void paint(Graphics g) {
		File dossier = new File("./stats");
		File[] fichiers = dossier.listFiles();
		
		int[][] values;
		
		String line;
		for(File f : fichiers) {
			InputStream fis = new FileInputStream(f);
		    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
		    BufferedReader br = new BufferedReader(isr);
		    while ((line = br.readLine()) != null) {
		        String[] strings = line.split(";");
		        values[Integer.parseInt(f.getName())][Integer.parseInt(strings[0])] = Integer.parseInt(strings[1]);
		    }
		}
		
		for (int x = 0; x < getSize().width; x++) {
			g.drawLine(x, (int) f(x), x + 1, (int) f(x + 1));
		}
	}
}
