package cat.app.tiledom.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    //Test per comprovar que la mida es correcte
    @Test
    void testBoardSizeIsCorrect() {

        //Taulell dificultat senzilla 10x10
        Board board_easy = new Board(1);
        assertEquals(10, board_easy.getSize());

        //Taulell dificultat mitjana 15x15
        Board board_medium = new Board(2);
        assertEquals(15, board_medium.getSize());

        //Taulell dificultat difícil 20x20
        Board board_hard = new Board(3);
        assertEquals(20, board_hard.getSize());
    }

    //Test per comprovar que totes les peces estan "unides"

    //Test per comprovar que el número de peces sigui parell i una parella de cada
}