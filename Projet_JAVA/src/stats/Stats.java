package stats;

import java.io.File;
import java.util.ArrayList;

public class Stats {

	public static void main(String[] args) {
		File dossier = new File("./statistiques/");
        if(!dossier.exists()) dossier.mkdir();
		File[] fichiers = dossier.listFiles();
		if(fichiers.length != 0) {
			supp(dossier);
		}
		ArrayList<partie> parties = new ArrayList<partie>();
		for(int i = 0; i < 100; i++) {
			if(i == 0) {
				parties.add(new partie(args, i));
			} else {
				parties.add(new partie(args, i));
			}
		}
		for(int i = 0; i < parties.size(); i++) {
			parties.get(i).start();
		}
	}
	
	public static void supp (File f) {
		if(f.isDirectory()) {
			for(File f2 : f.listFiles()) {
				supp(f2);
			} 
		} else {
			f.delete();
		}
	}

}
