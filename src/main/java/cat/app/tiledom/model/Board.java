package cat.app.tiledom.model;

import java.util.Random;

public class Board {

    // extraiem el generador per poder aplicar el mock als tests
    private RandomTileGenerator genRandom;

    public Board() {}
    // funció que inicialitzi el taulell d'una mida variable segons la dificultat i les peces col·locades aleatòriament
    public Board(int dificultat, RandomTileGenerator mockGen) {
        size = 0;
        tiles = new int[1][1];

        // farà servir el generador random en implementar la funció
        this.genRandom = mockGen;
    }

    public int getSize(){
        return size;
    }

    public int[][] getTiles(){
        return tiles;
    }

    public void setTiles(int[][] generatedTiles) {
        tiles = generatedTiles;
    }
    
    //mida segons nivell de dificultat -> generarà un taulell tiles[size][size]
    private int size;
    //valor 0 -> no hi ha peça 
    //valors 1-10 -> diferents tipus de peces
    private int tiles[][];
}