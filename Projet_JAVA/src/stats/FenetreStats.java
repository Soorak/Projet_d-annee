package stats;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;

import javax.swing.Box;
import javax.swing.JFrame;

public class FenetreStats extends JFrame {
	
	Courbe c;
	Label bleu;
	Label vert;
	Label rouge;
	Label jaune;
	
	public FenetreStats() {
		this.setTitle("Statistiques");
		this.setSize(800, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Box left = Box.createVerticalBox();
		Box right = Box.createVerticalBox();
		
		
		c = new Courbe();
		c.init();
		c.start();
		c.setBackground(Color.white);
		left.add(c);
	    
		bleu = new Label("Bleu : " + c.getPlusGrandeValeurBleu() + " pts de culture (en moyenne)");
		bleu.setBackground(new Color(132,179,255));
		right.add(bleu);
		
		vert = new Label("Vert : " + c.getPlusGrandeValeurVert() + " pts de culture (en moyenne)");
		vert.setBackground(new Color(132,179,255));
		right.add(vert);
		
		rouge = new Label("Rouge : " + c.getPlusGrandeValeurRouge() + " pts de culture (en moyenne)");
		rouge.setBackground(new Color(132,179,255));
		right.add(rouge);
		
		jaune = new Label("Jaune : " + c.getPlusGrandeValeurJaune() + " pts de culture (en moyenne)");
		jaune.setBackground(new Color(132,179,255));
		right.add(jaune);
		
		this.add(right, BorderLayout.EAST);
		this.add(left);
		
		
		this.setVisible(true);
	}
}
