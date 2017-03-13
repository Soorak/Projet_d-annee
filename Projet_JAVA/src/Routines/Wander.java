package Routines;

import java.util.Random;

import clientdesarenes.BotJD;
import jeu.Plateau;

public class Wander extends Routine {

    private static Random random = new Random();
    private final Plateau board;
    private MoveTo moveTo;

    @Override
    public void start() {
        super.start();
        this.moveTo.start();
    }

    public void reset() {
        this.moveTo = new MoveTo(random.nextInt(board.getWidth()), random.nextInt(board.getHeight()));
    }

    public Wander(Plateau board) {
        super();
        this.board = board;
        this.moveTo = new MoveTo(random.nextInt(board.getWidth()), random.nextInt(board.getHeight()));
    }

    @Override
    public void act(BotJD droid, Plateau board) {
        if (!moveTo.isRunning()) {
            return;
        }
        this.moveTo.act(droid, board);
        if (this.moveTo.isSuccess()) {
            succeed();
        } else if (this.moveTo.isFailure()) {
            fail();
        }
    }
}