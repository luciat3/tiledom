package cat.app.tiledom.model;

public class Board {

    // extraiem el generador per poder aplicar el mock als tests
    private RandomTileGenerator genRandom;

    //mida segons nivell de dificultat -> generarà un taulell tiles[size][size]
    private int size;
    //valor 0 -> no hi ha peça 
    //valors 1-10 -> diferents tipus de peces
    private int tiles[][];

    // --------------------- Funcions inicialització taulell -----------------------------
    public Board() { throw new IllegalArgumentException("Falten variables per inicialitzar el taulell"); }
    // funció que inicialitzi el taulell d'una mida variable segons la dificultat i les peces col·locades aleatòriament
    public Board(int dificultat, RandomTileGenerator gen) {
        if (dificultat < 1 || dificultat > 3) throw new IllegalArgumentException("Dificultat fora de rang (1–3)");

        this.genRandom = gen;

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

    public void setTiles(int[][] tiles) {
        this.tiles = tiles;
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

    // ------------------ Funcions Game Session -----------------------------

    // Comprova si les peces seleccionades tenen al menys un lateral lliure
    // que no siguin posicions lliures
    // i si les dues són del mateix tipus
    public boolean tryMatch(int x1, int y1, int x2, int y2) {
        // son buides
        if (tiles[x1][y1] == 0 || tiles[x2][y2] == 0) return false;
        // son de diferent tipus
        if (tiles[x1][y1] != tiles[x2][y2]) return false;
        
        // tenen al menys un costat buit
        boolean firstFree = isSideFree(x1, y1);
        boolean secondFree = isSideFree(x2, y2);
        if (!(firstFree && secondFree)) return false;

        tiles[x1][y1] = 0;
        tiles[x2][y2] = 0;
        return true;
    }

    // Funció auxiliar per comprovar si tenen un costat buit
    private boolean isSideFree(int i, int j) {
        // si és fora del taulell també és lliure
        boolean leftFree  = (j - 1 < 0) || tiles[i][j - 1] == 0;
        boolean rightFree = (j + 1 >= size) || tiles[i][j + 1] == 0;
        return leftFree || rightFree;
    }

    // Comprova si queden peces al taulell
    public boolean isEmpty() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] != 0) return false;
            }
        }
        return true;
    }
    
    // Comprova si queden moviments disponibles al taulell
    public boolean hasAvailableMoves() {

        //recorrem tot el taulell
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int current = tiles[i][j];
                //ignorem les buides
                if (current == 0) continue;

                //seleccionem les peces amb costats lliures
                if (isSideFree(i, j)) {
                    //busquem si hi ha una del mateix tipus amb costat lliure
                    for (int x = 0; x < size; x++) {
                        for (int y = 0; y < size; y++) {
                            if ((x == i && y == j) || tiles[x][y] == 0) continue;
                            if (tiles[x][y] == current && isSideFree(x, y)) {
                                return true; //hi ha al menys una jugada disponible
                            }
                        }
                    }
                }
            }
        }
        return false; // no se encontró ninguna pareja válida
    }

}