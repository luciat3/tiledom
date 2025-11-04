package cat.app.tiledom;

import cat.app.tiledom.model.Board;
import cat.app.tiledom.model.RandomTileGenerator;

public class TiledomApplication {
    public static void main(String[] args) {
        // Creem un generador real
        RandomTileGenerator gen = new RandomTileGenerator();

        // Creem un taulell de dificultat 1 (fàcil)
        Board board = new Board(1, gen);

        // Mostrem el taulell per consola
        int[][] tiles = board.getTiles();
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                System.out.print(tiles[i][j] + " ");
            }
            System.out.println();
        }
    }
}