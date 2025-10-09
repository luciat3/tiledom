package cat.app.tiledom.model;

public class Board {

    // funció que inicialitzi el taulell d'una mida variable segons la dificultat i les peces col·locades aleatòriament
    public Board(int dificultat) {
        size = 0;
    }

    public int getSize(){
        return size;
    }

    private int size;
}