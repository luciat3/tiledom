package cat.app.tiledom.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    Board tauler;
    RandomTileGenerator mockGen = new RandomTileGenerator();

    // funció necessària per externalitzar el funcionament del random mentre no l'implementem
    private void configurarMock(int maxType) {
        java.util.concurrent.atomic.AtomicInteger counter = new java.util.concurrent.atomic.AtomicInteger(0);
        when(mockGen.genera()).thenAnswer(inv -> ((counter.getAndIncrement() / 2) % maxType) + 1);
    }

    
    @BeforeEach
    void setUp() {
        /*
        // Mock object de RandomTileGenerator
        mockGen = mock(RandomTileGenerator.class);
        when(mockGen.genera()).thenReturn(1);
        */

        // ara que ja hem implementat RandomTileGenerator no cal fer servir el mock
    }

    //Test constructor sense paràmetres (inutilitzat)
    @Test
    void testDefaultConstructor() {
        assertThrows(IllegalArgumentException.class, Board::new);
    }    

    //Test per comprovar que la mida es correcte
    @Test
    void testBoardSizeIsCorrect() {

        //Taulell dificultat senzilla 8x8
        Board board_easy = new Board(1, new RandomTileGenerator(1));
        assertEquals(8, board_easy.getSize());

        //Taulell dificultat mitjana 10x10
        Board board_medium = new Board(2, new RandomTileGenerator(2));
        assertEquals(10, board_medium.getSize());

        //Taulell dificultat difícil 12x12
        Board board_hard = new Board(3,new RandomTileGenerator(3));
        assertEquals(12, board_hard.getSize());

        //Asserts pel 0, valors negatius i valors > 3
        try { 

            //fronteres
			Board board_0 = new Board(0, mockGen);
			assertTrue(false);
			Board board_4 = new Board(4, mockGen);
			assertTrue(false);

            //negatiu
			Board board_neg = new Board(-10, mockGen);
			assertTrue(false);

            //positiu
            Board board_pos = new Board(15, mockGen);
			assertTrue(false);

		}catch(Exception e) {}
    }

    //Test per comprovar que totes les peces estan "unides"
    @Test
    void testBoardConnected() {

        // --- Particions equivalents: dificultats 1, 2, 3 ---
        Board board_easy = new Board(1, new RandomTileGenerator(1));
        Board board_medium = new Board(2, new RandomTileGenerator(2));
        Board board_hard = new Board(3, new RandomTileGenerator(3));

        assertTrue(isConnected(board_easy), "Taulell fàcil no connectat");
        assertTrue(isConnected(board_medium), "Taulell intermig no connectat");
        assertTrue(isConnected(board_hard), "Taulell difícil no connectat");
    }

    private boolean isConnected(Board board) {
        int[][] tiles = board.getTiles();
        int size = board.getSize();
        boolean[][] visited = new boolean[size][size];

        int totalPieces = 0;
        int connectedPieces = 0;

        // Buscar la primera peça (>0)
        int startI = -1, startJ = -1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] > 0) {
                    totalPieces++;
                    if (startI == -1) {
                        startI = i;
                        startJ = j;
                    }
                }
            }
        }

        // Si no hi ha peces, per definició és "connectat"
        if (startI == -1) return true;

        // BFS per comptar peces connectades
        java.util.Queue<int[]> q = new java.util.LinkedList<>();
        q.add(new int[]{startI, startJ});
        visited[startI][startJ] = true;

        int[] dirs = {-1, 0, 1, 0, -1};
        while (!q.isEmpty()) {
            int[] p = q.poll();
            connectedPieces++;
            for (int d = 0; d < 4; d++) {
                int ni = p[0] + dirs[d];
                int nj = p[1] + dirs[d+1];
                if (ni >= 0 && nj >= 0 && ni < size && nj < size) {
                    if (tiles[ni][nj] > 0 && !visited[ni][nj]) {
                        visited[ni][nj] = true;
                        q.add(new int[]{ni, nj});
                    }
                }
            }
        }

        // Si totes les peces trobades = total peces → taulell connectat
        return connectedPieces == totalPieces;
    }

    //Test per comprovar que el número de peces sigui parell i una parella de cada
    @Test
    void testBoardHasEvenTiles(){

        // -------------------Prova taulell senzill-------------------
        //configurarMock(5);
        Board board_easy = new Board(1, new RandomTileGenerator(1));
        int[] count = countTiles(board_easy, 5);
        // - Només hi ha 40 peces
        assertEquals(40, count[0]);
        // - Només hi ha cinc tipus de peces -> Comprovació de valors invàlids pels tipus de peces dins de countTiles
        int aux = 0;
        boolean parells = true;
        for (int i=1; i<6; i++) {
            if (count[i] > 0) {
                aux++;
                if (count[i] % 2 != 0) parells = false;
            }
        }
        assertEquals(5, aux);
        // - Cada tipus apareix de manera parell
        assertTrue(parells);

        // -------------------Prova taulell intermig-------------------
        //configurarMock(7);
        Board board_medium = new Board(2, new RandomTileGenerator(2));
        count = countTiles(board_medium, 7);
        // - Només hi ha 60 peces
        assertEquals(60, count[0]);
        // - Només hi ha set tipus de peces -> Comprovació de valors invàlids pels tipus de peces dins de countTiles
        aux = 0;
        parells = true;
        for (int i=1; i<8; i++) {
            if (count[i] > 0) {
                aux++;
                if (count[i] % 2 != 0) parells = false;
            }
        }
        assertEquals(7, aux);
        // - Cada tipus apareix de manera parell
        assertTrue(parells);

        // -------------------Prova taulell difícil-------------------
        //configurarMock(10);
        Board board_hard = new Board(3, new RandomTileGenerator(3));
        count = countTiles(board_hard, 10);
        // - Només hi ha 60 peces
        assertEquals(90, count[0]);
        // - Només hi ha deu tipus de peces -> Comprovació de valors invàlids pels tipus de peces dins de countTiles
        aux = 0;
        parells = true;
        for (int i=1; i<11; i++) {
            if (count[i] > 0) {
                aux++;
                if (count[i] % 2 != 0) parells = false;
            }
        }
        assertEquals(10, aux);
        // - Cada tipus apareix de manera parell
        assertTrue(parells);
    }

    private int[] countTiles(Board board, int typeCount) {
        
        int size = board.getSize();
        int[][] tiles = board.getTiles();

        int[] types = new int[typeCount+1]; // de l'1 al 5, posició 0 pel total de peces

        for (int i=0; i<size; i++){
            for (int j=0; j<size; j++) {
                int v = tiles[i][j];

                if (v < 0) {
                    fail("S'ha detectat una peça amb valor negatiu");
                }
                if (v == 0) {
                    continue; // ignorem casella buida
                }
                if (v > typeCount) {
                    fail("S'ha detectat una peça amb tipus fora de rang: " + v);
                }
                types[0]++;
                types[v]++;
            }
        }

        return types;
    }

    private int expectedPiecesForSize(int size) {
        return switch (size) {
            case 8  -> 40;
            case 10 -> 60;
            case 12 -> 90;
            default -> throw new IllegalArgumentException("Mida desconeguda: " + size);
        };
    }

    /*
    @Test
    void testBoardConstructor_UsesRandomGenerator() {
        when(mockGen.genera()).thenReturn(5);

        Board board = new Board(1, mockGen);

        // Verifiquem que s'ha fet servir el mock les vegades correctes
        int expected = expectedPiecesForSize(board.getSize());
        verify(mockGen, atLeast(expected)).genera();
        assertEquals(5, board.getTiles()[0][0]);
    }
        */

    // ----------- Tests per funcions derivades de Game Session ------------

    // -- Tests TryMatch() --
    /*
    Comprovacions:
        1- Peces sense cap costat lliure -> False
        2- Peces al límit del taulell amb costats lliures -> True
        3- Peces amb costats lliures per peces buides (0) -> True
        4- Peces buides -> False
        5- Peça buida i peça plena -> False
        6- Peça plena i peça buida -> False

        extra: als casos true comprova que s'hagin marcat correctament com a 0.
    */
    @Test
    void testTryMatch() {
        Board board = new Board(1, new RandomTileGenerator(1));
        int[][] tiles = {
            {2, 1, 1, 2}
        };
        board.setTiles(tiles);
        //comprovem les del mig (bloquejades)
        assertFalse(board.tryMatch(0,1,0,2)); 
        //comprovem les exteriors (al borde)
        assertTrue(board.tryMatch(0, 0, 0, 3));
        //comprovem que les hagi marcat a 0
        int[][] taulell = board.getTiles();
        assertEquals(0, taulell[0][0]);
        assertEquals(0, taulell[0][3]);

        int[][] tiles2 = {
            {0, 2, 1, 1, 2, 0}
        };
        board.setTiles(tiles2);
        //comprovem les exteriors (lliures)
        assertTrue(board.tryMatch(0, 1, 0, 4));
        //comprovem que les hagi posat a 0
        taulell = board.getTiles();
        assertEquals(0, taulell[0][1]);
        assertEquals(0, taulell[0][4]);
        //comprovem peces buides
        board.setTiles(tiles2);
        assertFalse(board.tryMatch(0, 0, 0, 5));
        //peça buida i peça plena
        board.setTiles(tiles2);
        assertFalse(board.tryMatch(0, 0, 0, 1));
        //peça plena i peça buida
        board.setTiles(tiles2);
        assertFalse(board.tryMatch(0, 1, 0, 5));

        int[][] tiles3 = {
            {0, 2, 1, 1, 3, 0}
        };
        board.setTiles(tiles3);

        //comprovem peces diferents
        assertFalse(board.tryMatch(0, 1, 0, 4));
    }

    @Test
    void testTryMatchFailsWhenSameTileSelected() {
        //test per comprovar que una mateixa fitxa no s'elimini en seleccionar-la a si mateixa
        Board board = new Board(1, new RandomTileGenerator(1));
        int[][] tiles = {
            {2, 1, 1, 2}
        };
        board.setTiles(tiles);

        assertEquals(2, board.getTiles()[0][0]);

        //intentem fer match de la peça amb ella mateixa
        boolean matched = board.tryMatch(0, 0, 0, 0);

        //el mètode ha de considerar-ho un moviment invàlid
        assertFalse(matched);
        //la peça no s'ha d'eliminar del taulell
        assertEquals(2, board.getTiles()[0][0]);
    }

    /*
    TryMatch Pairwise:

    Tenim tres factors dins la funció amb els que podem dissenyar un test pairwise:
    1- Type: Les peces són del mateix tipus
    2- FirstFree: La primera peça té (almenys) un costat lliure
    3- SecondFree: La segona peça té (almenys) un costat lliure

    Type | FirstFree | SecondFree | Resultat
    ----------------------------------------
      Sí |   Sí      |   Sí       |  True
      Sí |   No      |   No       |  False
      No |   Sí      |   No       |  False
      No |   No      |   Sí       |  False
    
    */

    @Test
    void testTryMatchPairwise() {
        // ---- Cas 1
        Board board1 = new Board(1, new RandomTileGenerator(1));
        int[][] tiles1 = {
                {2, 0, 0, 2}
        };
        board1.setTiles(tiles1);

        assertTrue(board1.tryMatch(0, 0, 0, 3));

        // ---- Cas 2
        Board board2 = new Board(1, new RandomTileGenerator(1));
        int[][] tiles2 = {
                {1, 2, 2, 1}
        };
        board2.setTiles(tiles2);

        assertFalse(board2.tryMatch(0, 1, 0, 2));

        // ---- Cas 3
        Board board3 = new Board(1, new RandomTileGenerator(1));
        int[][] tiles3 = {
                {2, 1, 3, 3}
        };
        board3.setTiles(tiles3);

        assertFalse(board3.tryMatch(0, 0, 0, 2));

        // ---- Cas 4
        Board board4 = new Board(1, new RandomTileGenerator(1));
        int[][] tiles4 = {
                {1, 1, 3, 2}
        };
        board4.setTiles(tiles4);

        assertFalse(board4.tryMatch(0, 1, 0, 3));
    }


    @Test
    void testIsEmpty() {
        Board board = new Board(1, new RandomTileGenerator(1));
        int size = board.getSize();
        int[][] tiles = new int[size][size];

        //taulell buit
        board.setTiles(tiles);
        assertTrue(board.isEmpty());

        //taulell amb una sola peça
        tiles[2][3] = 1;
        board.setTiles(tiles);
        assertFalse(board.isEmpty());

        //eliminem manualment la peça
        tiles[2][3] = 0;
        board.setTiles(tiles);
        assertTrue(board.isEmpty());
    }

    // -- Test hasAvailableMoves
    /*
    Comprovacions:
        1. Taulell amb un moviment disponible -> True
        2. Taulell amb peces però sense moviments disponibles -> False
        3. Taulell buit -> False
        4. Taulell amb una sola peça -> False
    */
    @Test
    void testHasAvailableMoves() {
        Board board = new Board(1, new RandomTileGenerator(1));

        int[][] tiles = {
            {1, 1, 0},
            {0, 0, 0},
            {0, 0, 0}
        };
        board.setTiles(tiles);

        assertTrue(board.hasAvailableMoves());

        //canviem la peça de la primera posició
        tiles[0][0] = 2;
        board.setTiles(tiles);

        assertFalse(board.hasAvailableMoves());

        // taulell buit
        int size = board.getSize();
        int[][] empty = new int[size][size];

        board.setTiles(empty);
        assertFalse(board.hasAvailableMoves());

        tiles[0][1] = 1; // una sola peça
        board.setTiles(tiles);

        assertFalse(board.hasAvailableMoves());
    }


    // --------------------------------------------------------------------------------------------
    //auxiliar per trobar errors
    private void printBoard(Board board) {
        System.out.println("Taulell " + board.getSize() + "x" + board.getSize() + ":");
        int[][] tiles = board.getTiles();
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                System.out.print(String.format("%2d ", tiles[i][j]));
            }
            System.out.println();
        }
        System.out.println();
    }


}
