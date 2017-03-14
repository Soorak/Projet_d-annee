package stats;

import java.io.File;
import java.util.ArrayList;

public class Stats {

	public static void main(String[] args) {
		File dossier = new File("./statistiques");
        if(!dossier.exists()) dossier.mkdir();
		File[] fichiers = dossier.listFiles();
		if(fichiers.length != 0) {
			for(File f : fichiers) {
				f.delete();
			}
		}
		ArrayList<partie> parties = new ArrayList<partie>();
		for(int i = 0; i < 50; i++) {
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

}
