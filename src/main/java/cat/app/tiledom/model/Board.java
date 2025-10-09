package cat.app.tiledom.model;

public class Board {

    // funció que inicialitzi el taulell d'una mida variable segons la dificultat i les peces col·locades aleatòriament
    public Board(int dificultat) {
        size = 0;
        tiles = new int[1][1];
    }

    public int getSize(){
        return size;
    }

    public int[][] getTiles(){
        return tiles;
    }

    //mida segons nivell de dificultat -> generarà un taulell tiles[size][size]
    private int size;
    //valor 0 -> no hi ha peça 
    //valors 1-10 -> diferents tipus de peces
    private int tiles[][];
}