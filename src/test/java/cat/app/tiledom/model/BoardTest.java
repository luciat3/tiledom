package cat.app.tiledom.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    //Test per comprovar que la mida es correcte
    @Test
    void testBoardSizeIsCorrect() {

        //Taulell dificultat senzilla 8x8
        Board board_easy = new Board(1);
        assertEquals(8, board_easy.getSize());

        //Taulell dificultat mitjana 10x10
        Board board_medium = new Board(2);
        assertEquals(10, board_medium.getSize());

        //Taulell dificultat difícil 12x12
        Board board_hard = new Board(3);
        assertEquals(12, board_hard.getSize());

        //Asserts pel 0, valors negatius i valors > 3
        try { 

            //fronteres
			Board board_0 = new Board(0);
			assertTrue(false);
			Board board_4 = new Board(4);
			assertTrue(false);

            //negatiu
			Board board_neg = new Board(-10);
			assertTrue(false);

            //positiu
            Board board_pos = new Board(15);
			assertTrue(false);

		}catch(Exception e) {}
    }

    //Test per comprovar que totes les peces estan "unides"
    @Test
    void testBoardConnected() {

    }

    //Test per comprovar que el número de peces sigui parell i una parella de cada
    @Test
    void testBoardHasEvenTiles(){

        // Prova taulell senzill
        Board board_easy = new Board(1);
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

        // Prova taulell intermig
        Board board_medium = new Board(2);
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

        // Prova taulell difícil
        Board board_hard = new Board(3);
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
                if (tiles[i][j] > 0) {
                    // Si hi ha més tipus de peces dels esperats dona error
                    if (tiles[i][j] > typeCount) {
                        fail("S'ha detectat una peça amb tipus fora de rang: " + tiles[i][j]);
                    }
                     types[0]++;
                     types[tiles[i][j]]++;
                } else { // Si hi ha un tipus de peça negatiu dona error
                    fail("S'ha detectat una peça amb valor negatiu");
                }
            }
        }

        return types;
    }
}