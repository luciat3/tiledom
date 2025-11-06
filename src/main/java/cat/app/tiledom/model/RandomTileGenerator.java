package cat.app.tiledom.model;

import java.util.Random;

public class RandomTileGenerator {

    private int numTipus;
    private int[] pieces;
    public RandomTileGenerator() {
    }

    public int genera() {
        return 0;
    }

    // distribueix el total de peces corresponent per dificultat
    // en els tipus corresponents de manera parell i equilibrat
    public void preparePieces() {}

    // retorna el total de peces que queden per col·locar
    public int remainingPieces() {return 0;}

    public void setNumTipus(int numTipus) {
        this.numTipus = numTipus;
    }
    public int getNumTipus() {return numTipus;}

}
