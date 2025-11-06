package cat.app.tiledom;

import java.util.Scanner;

import cat.app.tiledom.model.Board;
import cat.app.tiledom.model.RandomTileGenerator;

public class TiledomApplication {
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        int dificultat = 0;

        // Demanem la dificultat a l'usuari
        while (dificultat < 1 || dificultat > 3) {
            System.out.print("Selecciona la dificultat (1 = fàcil, 2 = mitjana, 3 = difícil): ");
            if (sc.hasNextInt()) {
                dificultat = sc.nextInt();
                if (dificultat < 1 || dificultat > 3) {
                    System.out.println("Valor invàlid. Intenta-ho de nou.");
                }
            } else {
                System.out.println("Entrada no vàlida. Escriu un número (1, 2 o 3).");
                sc.next(); // descartar entrada incorrecta
            }
        }

        // Creem un generador real
        RandomTileGenerator gen = new RandomTileGenerator(dificultat);

        // Creem un taulell de dificultat 1 (fàcil)
        Board board = new Board(dificultat, gen);

        System.out.println("\nTaulell inicial (" + dificultat + "):");
        printBoard(board);

        // Bucle de joc
        while (!board.isEmpty() && board.hasAvailableMoves()) {
            System.out.print("Selecciona dues peces (x1 y1 x2 y2): ");
            int x1 = sc.nextInt() - 1; 
            int y1 = sc.nextInt() - 1;
            int x2 = sc.nextInt() - 1;
            int y2 = sc.nextInt() - 1;

            if (board.tryMatch(x1, y1, x2, y2)) {
                System.out.println("Parella eliminada!");
            } else {
                System.out.println("Moviment invàlid!");
            }

            printBoard(board);
        }

        // Resultat final
        if (board.isEmpty()) System.out.println("Has guanyat!");
        else System.out.println("No queden moviments. Fi de la partida.");

        sc.close();
    }
    private static void printBoard(Board board) {
        int[][] tiles = board.getTiles();
        int size = board.getSize();

        System.out.print("    ");
        for (int j = 0; j < size; j++) {
            System.out.printf("%3d", j + 1);
        }
        System.out.println("\n   " + "-".repeat(size * 3));

        for (int i = 0; i < size; i++) {
            System.out.printf("%2d |", i + 1);
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] == 0) System.out.print("  .");
                else System.out.printf("%3d", tiles[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

}