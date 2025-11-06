package cat.app.tiledom.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/* 
El RandomTileGenerator hauria de:

1 - Determinar els tipus de peces segons el nivell de dificultat
2 - Aleatòriament (però amb poca diferència entre grups) assignar quantitats de peces 
parells pertany a cada tipus de peces, sense passar-se del total de peces per dificultat.
3 - Aleatòriament anar col·locant les peces a posicions de la matriu.

    - Senzill (1):
        - cinc tipus de peces 
        - 40 peces en total
    - Intermig (2):
        - set tipus de peces
        - 60 peces en total
    - Difícil (3):
        - deu tipus de peces
        - 90 peces en total


*/

// Fem uns pocs tests per determinar d'on venen els errors, si n'hi han
// la resta de funcionalitats ja es comproven als tests de Board
class RandomTileGeneratorTest {

    RandomTileGenerator gen;

    @BeforeEach
    void setUp() {
        gen = new RandomTileGenerator(); 
    }

    @Test
    void testGeneratesValuesWithinRange() {

        //comprovem que genera valors correctes pel taulell senzill
        gen.setNumTipus(5);
        for (int i = 0; i < 40; i++) {
            int val = gen.genera();
            assertTrue(val >= 1 && val <= 5);
        }

        //comprovem que genera valors correctes pel taulell intermig
        gen.setNumTipus(7);
        for (int i = 0; i < 60; i++) {
            int val = gen.genera();
            assertTrue(val >= 1 && val <= 7);
        }

        //comprovem que genera valors correctes pel taulell difícil
        gen.setNumTipus(10);
        for (int i = 0; i < 90; i++) {
            int val = gen.genera();
            assertTrue(val >= 1 && val <= 10);
        }
    }


    @Test
    void testPairDistribution() {
        // comprovem que cada tipus de peça apareix en parells
        //fàcil
        gen.setNumTipus(5);
        gen.preparePieces();

        assertEquals(40, gen.remainingPieces());

        int[] counts = new int[6]; //guardar quantes vegades apareix cada fitxa
        for (int i = 0; i < 40; i++) {
            counts[gen.genera()]++;
        }

        for (int i = 1; i <= 5; i++) {
            assertEquals(0, counts[i] % 2);
        }

        // comprovar que cada tipus de peces té una quantitat de peces similar a la resta
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (int i = 1; i <= 5; i++) {
            if (counts[i] < min) min = counts[i];
            if (counts[i] > max) max = counts[i];
        }

        // La diferència no hauria de ser més gran que el doble
        assertTrue(max - min <= max / 2);

        //intermig
        gen.setNumTipus(7);
        gen.preparePieces();

        assertEquals(60, gen.remainingPieces());


        int[] counts2 = new int[8]; //guardar quantes vegades apareix cada fitxa
        for (int i = 0; i < 60; i++) {
            counts2[gen.genera()]++;
        }

        for (int i = 1; i <= 7; i++) {
            assertEquals(0, counts2[i] % 2);
        }

        // comprovar que cada tipus de peces té una quantitat de peces similar a la resta
        min = Integer.MAX_VALUE; max = Integer.MIN_VALUE;
        for (int i = 1; i <= 7; i++) {
            if (counts2[i] < min) min = counts2[i];
            if (counts2[i] > max) max = counts2[i];
        }

        //difícil
        gen.setNumTipus(10);
        gen.preparePieces();

        assertEquals(90, gen.remainingPieces());

        int[] counts3 = new int[11]; //guardar quantes vegades apareix cada fitxa
        for (int i = 0; i < 90; i++) {
            counts3[gen.genera()]++;
        }

        for (int i = 1; i <= 10; i++) {
            assertEquals(0, counts3[i] % 2);
        }

        // comprovar que cada tipus de peces té una quantitat de peces similar a la resta
        min = Integer.MAX_VALUE; max = Integer.MIN_VALUE;
        for (int i = 1; i <= 10; i++) {
            if (counts3[i] < min) min = counts3[i];
            if (counts3[i] > max) max = counts3[i];
        }
    }



}
