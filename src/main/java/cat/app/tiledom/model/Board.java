package cat.app.tiledom.model;

public class Board {

    // extraiem el generador per poder aplicar el mock als tests
    private RandomTileGenerator genRandom;

    public Board() {}
    // funció que inicialitzi el taulell d'una mida variable segons la dificultat i les peces col·locades aleatòriament
    public Board(int dificultat, RandomTileGenerator mockGen) {
        if (dificultat < 1 || dificultat > 3) throw new IllegalArgumentException("Dificultat fora de rang (1–3)");

        this.genRandom = mockGen;

        int numTipus = 0, numPeces = 0;
        // mida i peces segons dificultat
        switch (dificultat) {
            case 1 -> {size = 8; numTipus = 5; numPeces = 40;}
            case 2 -> {size = 10; numTipus = 7; numPeces = 60;}
            case 3 -> {size = 12; numTipus = 10; numPeces = 90;}
        }
        tiles = new int[size][size];
        setTiles(size, numTipus, numPeces);


    }

    public int getSize(){
        return size;
    }

    public int[][] getTiles(){
        return tiles;
    }

    public void setTiles(int size, int numTipus, int numPeces){
        int side = (int) Math.ceil(Math.sqrt(numPeces)); // costat mínim que pot contenir totes les peces

        // Calculem l'offset per centrar el bloc
        int start = (size - side) / 2;
        int end = start + side;

        // Omplim les peces dins del bloc central
        int placed = 0;
        for (int i = 0; i < size && placed < numPeces; i++) {
            for (int j = 0; j < size && placed < numPeces; j++) {
                // només col·loca peces si la posició està dins del bloc central
                // i una petita probabilitat extra si és al voltant
                boolean insideCenter = i >= start && i < end && j >= start && j < end;
                if (insideCenter || (Math.random() % 5 == 0)) { 
                    int value = genRandom.genera();
                    tiles[i][j] = value;
                    placed++;
                }

            }
        }
    }
    
    //mida segons nivell de dificultat -> generarà un taulell tiles[size][size]
    private int size;
    //valor 0 -> no hi ha peça 
    //valors 1-10 -> diferents tipus de peces
    private int tiles[][];
}