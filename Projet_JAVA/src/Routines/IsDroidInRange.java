package Routines;

import clientdesarenes.BotJD;
import jeu.Plateau;

public class IsDroidInRange extends Routine {

    public IsDroidInRange() {}

    @Override
    public void reset() {
        start();
    }

    @Override
    public void act(BotJD droid, Plateau board) {
        // find droid in range
        for (BotJD enemy : board.getDroids()) {
            if (!droid.getName().equals(enemy)) {
                if (isInRange(droid, enemy)) {
                    succeed();
                    break;
                }
            }
        }
        fail();
    }

    private boolean isInRange(BotJD droid, Plateau enemy) {
        return (Math.abs(droid.getX() - enemy.getX()) <= droid.getRange()
                && Math.abs(droid.getY() - enemy.getY()) <= droid.getRange());
    }
}