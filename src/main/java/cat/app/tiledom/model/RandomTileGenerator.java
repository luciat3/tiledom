package cat.app.tiledom.model;

import java.util.*;

public class RandomTileGenerator {

    private int numTipus;
    private int totalPieces;
    // cada posició de l'array és un tipus de peça i conté la seva quantitat
    private int[] pieces;
    // lista de les peces barrejades aleatòriament
    private List<Integer> bag;
    private Random rand = new Random();

    public RandomTileGenerator(){}

    // amb la dificultat podem conèixer la quantitat de peces i tipus
    public RandomTileGenerator(int dificultat) {
        switch (dificultat) {
            case 1 -> { numTipus = 5; totalPieces = 40; }   // fàcil
            case 2 -> { numTipus = 7; totalPieces = 60; }   // intermig
            case 3 -> { numTipus = 10; totalPieces = 90; }  // difícil
            default -> throw new IllegalArgumentException("Dificultat fora de rang (1–3)");
        }

        preparePieces();
    }

    // envia una peça restant aleatòria
    public int genera() {
        if (bag.isEmpty()) return 0;
        return bag.remove(bag.size() - 1);
    }

    // distribueix el total de peces corresponent per dificultat
    // en els tipus corresponents de manera parell i equilibrat
    public void preparePieces() {
        // mantenim control de la quantitat de peces per tipus
        pieces = new int[numTipus + 1];
        bag = new ArrayList<>();

        // quantitat de peces per tipus sense aletorietat
        int base = totalPieces / numTipus;
        if (base % 2 != 0) {
            base--;                         // assegurem que és parell
        }
        if (base < 2) {
            base = 2;                       // com a mínim 2 peces per tipus
        }
        int assigned = 0;

        // distribuim amb aleatorietat
        for (int i = 1; i <= numTipus; i++) {
            int variation = (rand.nextInt(3) - 1) * 2; //-2, 0, 2
            int count = base + variation;

            if (count % 2 != 0) count++;

            if ((assigned + count) <= totalPieces ) {
                pieces[i] = count;
                assigned += count;
            } else {
                pieces[i] = totalPieces - assigned;
                assigned = assigned + (totalPieces - assigned);
            }
        }

        // ajustem si han quedat peces sense assignar
        while (assigned < totalPieces) {
            int i = rand.nextInt(numTipus) + 1;
            pieces[i] += 2;
            assigned += 2;
        }

        // omplim la bossa de peces 
        for (int i = 1; i <= numTipus; i++) {
            for (int j = 0; j < pieces[i]; j++) {
                bag.add(i);
            }
        }

        // barregem la bossa
        Collections.shuffle(bag, rand);

    }

    // retorna el total de peces que queden per col·locar
    public int remainingPieces() {return bag.size();}

    public void setNumTipus(int numTipus) {
        this.numTipus = numTipus;
        switch (numTipus) {
            case 5 -> totalPieces = 40;
            case 7 -> totalPieces = 60;
            case 10 -> totalPieces = 90;
            default -> throw new IllegalArgumentException("Nombre de tipus no vàlid");
        }
        preparePieces();
    }
    public int getNumTipus() {return numTipus;}

}
