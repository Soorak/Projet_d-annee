package clientdesarenes;

import org.apache.commons.lang3.time.StopWatch;

import Routines.Routine;
import jeu.Joueur;
import jeu.Plateau;

public class Bot extends jeu.Joueur implements reseau.JoueurReseauInterface {

	String key;
	Routine routine;
	Action a;
	StopWatch s;

	Bot(String id, String cle) {
		super(id);
		key = cle;
		s = new StopWatch();
	}

	@Override
	public Joueur.Action faitUneAction(Plateau t) {
		this.s.reset();
		this.s.start();
		Action a = super.faitUneAction(t);
		// TODO
		this.s.stop();
		System.out.println("Bot.faitUneAction: Je joue " + a);
		System.out.println("Temps décision : " + s.getNanoTime());
		return a;
	}

	@Override
	public String donneID() {
		return donneNom();
	}

	@Override
	public String donneCle() {
		return key;
	}

	@Override
	public void debutNouvellePartie() {
		System.out.println("Bot: La partie commence.");
	}

	@Override
	public void finDeLaPartie(Plateau t) {
		System.out.println("Bot: La partie est finie.");
	}

	@Override
	public void deconnecte() {
		System.out.println("Bot: On est déconnecté du serveur.");
	}

	/**
	 * @param a the action to set
	 */
	public void setAction(Action a) {
		this.a = a;
	}

	/**
	 * @param routine the routine to set
	 */
	public void setRoutine(Routine routine) {
		this.routine = routine;
	}


}
