package cat.app.tiledom.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Fem uns pocs tests per determinar d'on venen els errors, si n'hi han
// les funcionalitats ja es comproven als tests de Board
class RandomTileGeneratorTest {

    RandomTileGenerator gen;

    @BeforeEach
    void setUp() {
        gen = new RandomTileGenerator(5); 
    }

    @Test
    void testGeneratesValuesWithinRange() {
        //comprovem que genera valors correctes
        for (int i = 0; i < 50; i++) {
            int val = gen.genera();
            assertTrue(val >= 1 && val <= 5);
        }
    }

}
