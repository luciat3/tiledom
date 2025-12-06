package cat.app.tiledom.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;

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

    // -- Tests pel constructor segons dificultat --
    /*
    - Particions:
        1- dificultat == 1 -> frontera
        2- dificultat == 2 -> interior i interior a frontera
        3- dificultat == 3 -> frontera
        4- dificultat == 0 -> IllegalArgument exterior a frontera
        5- dificultat == 4 -> IllegalArgument exterior a frontera
        6- dificultat < 1 -> IllegalArgument
        7- dificultat > 3 -> IllegalArgument
    */

    @Test
    void testConstructor() {
        // dificultat senzilla -> 5 tipus, 40 peces
        RandomTileGenerator easy = new RandomTileGenerator(1);
        assertEquals(5, easy.getNumTipus());
        assertEquals(40, easy.remainingPieces());

        // dificultat intermèdia -> 7 tipus, 60 peces
        RandomTileGenerator medium = new RandomTileGenerator(2);
        assertEquals(7, medium.getNumTipus());
        assertEquals(60, medium.remainingPieces());

        // dificultat difícil -> 10 tipus, 90 peces
        RandomTileGenerator hard = new RandomTileGenerator(3);
        assertEquals(10, hard.getNumTipus());
        assertEquals(90, hard.remainingPieces());

        // fronteres a l'exterior
        assertThrows(IllegalArgumentException.class,
                () -> new RandomTileGenerator(0)); // dificultat == 0
        assertThrows(IllegalArgumentException.class,
                () -> new RandomTileGenerator(4));   // dificultat == 4

        // interiors de particions invàlides
        assertThrows(IllegalArgumentException.class,
                () -> new RandomTileGenerator(-5)); // dificultat < 1
        assertThrows(IllegalArgumentException.class,
                () -> new RandomTileGenerator(10)); // dificultat > 3
    }

    // -- Tests per setNumTipus --
    /*
    - Particions:
        1- setNumTipus == 5 -> 40 peces
        2- setNumTipus == 7 -> 60 peces
        3- setNumTipus == 10 -> 90 peces
        4- 1 <= numTipus < 5 -> IllegalArgument exterior a frontera
        5- 5 < numTipus < 7 -> IllegalArgument exterior a frontera
        6- setNumTipus <= 0 -> IllegalArgument exterior a frontera
        7- 7 < numTipus < 10 -> IllegalArgument
        8- setNumTipus > 10 -> IllegalArgument
    */

    @Test
    void testSetNumTipus() {
        // numTipus == 5
        gen.setNumTipus(5);
        assertEquals(5, gen.getNumTipus());
        assertEquals(40, gen.remainingPieces());

        // numTipus == 7
        gen.setNumTipus(7);
        assertEquals(7, gen.getNumTipus());
        assertEquals(60, gen.remainingPieces());

        // numTipus == 10
        gen.setNumTipus(10);
        assertEquals(10, gen.getNumTipus());
        assertEquals(90, gen.remainingPieces());

        // numTipus <= 0
        assertThrows(IllegalArgumentException.class,
                () -> gen.setNumTipus(0));     // frontera 0
        assertThrows(IllegalArgumentException.class,
                () -> gen.setNumTipus(-3));    // interior < 0

        // 1 <= numTipus < 5
        assertThrows(IllegalArgumentException.class,
                () -> gen.setNumTipus(4));     // just abans de 5

        // 5 < numTipus < 7
        assertThrows(IllegalArgumentException.class,
                () -> gen.setNumTipus(6));     // entre 5 i 7

        // 7 < numTipus < 10
        assertThrows(IllegalArgumentException.class,
                () -> gen.setNumTipus(8));     // entre 7 i 10

        // numTipus > 10
        assertThrows(IllegalArgumentException.class,
                () -> gen.setNumTipus(11));    // just després de 10
        assertThrows(IllegalArgumentException.class,
                () -> gen.setNumTipus(99));    // interior llunyà
    }


    // Comprovacions funció genera
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

    // ---------------------------- CAIXA BLANCA ------------------------------------

    @Test
    void testPreparePieces_BaseLessThanTwo_WhiteBox() throws Exception {
        RandomTileGenerator gen = new RandomTileGenerator();

        // Configurem un estat artificial: poques peces i molts tipus
        Field numTipusField = RandomTileGenerator.class.getDeclaredField("numTipus");
        Field totalPiecesField = RandomTileGenerator.class.getDeclaredField("totalPieces");
        numTipusField.setAccessible(true);
        totalPiecesField.setAccessible(true);

        numTipusField.setInt(gen, 10);   
        totalPiecesField.setInt(gen, 10);

        // Executem preparePieces() per forçar if (base < 2) { base = 2; }
        gen.preparePieces();

        Field piecesField = RandomTileGenerator.class.getDeclaredField("pieces");
        piecesField.setAccessible(true);
        int[] pieces = (int[]) piecesField.get(gen);

        for (int i = 1; i <= 10; i++) {
            assertTrue(pieces[i] >= 2, 
                "Cada tipus ha de tenir almenys 2 peces, però el tipus " + i + " en té " + pieces[i]);
        }
    }

    @Test
void testGeneraReturnsZeroWhenBagIsEmpty() {
    // Usem el constructor amb dificultat per tenir la bossa preparada
    RandomTileGenerator gen = new RandomTileGenerator(1); // fàcil: 40 peces

    int total = gen.remainingPieces();
    assertTrue(total > 0, "La bossa hauria de tenir peces al principi");

    // Consumim totes les peces
    for (int i = 0; i < total; i++) {
        int val = gen.genera();
        // mentre hi ha peces, el valor ha de ser un tipus vàlid
        assertTrue(val >= 1 && val <= gen.getNumTipus());
    }

    // Ara la bossa ha d'estar buida
    assertEquals(0, gen.remainingPieces(), "Després de consumir totes les peces, la bossa ha de quedar buida");

    // Una crida extra a genera() ha de passar per bag.isEmpty() i retornar 0
    int extra = gen.genera();
    assertEquals(0, extra, "Quan la bossa està buida, genera() ha de retornar 0");
}


}
