package stats;

import java.awt.Container;
import java.awt.Frame;

public class partie extends Thread {
	
	private String[] args;
	private int numThread;
	private static int tour = 0;
	
	public partie (String[] args, int numThread) {
		this.args = args;
		this.numThread = numThread;
	}
	
	public void run() {
		super.run();
		while(true) {
			try {
				if(tour == numThread) {
					System.err.println("Je joue le thread " + numThread);
					JeuSoloLocal.main(args);
					this.sleep(300);
					tour++;
					break;
				} else {
					this.sleep(500);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
