package iscteiul.ista.battleship;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CompassTest {

    @Test
    void getDirection() {
        // Test that each compass direction returns the correct character
        assertEquals('n', Compass.NORTH.getDirection());
        assertEquals('s', Compass.SOUTH.getDirection());
        assertEquals('e', Compass.EAST.getDirection());
        assertEquals('o', Compass.WEST.getDirection());
        assertEquals('u', Compass.UNKNOWN.getDirection());
    }

    @Test
    void testToString() {
        // Test that toString returns the correct string representation
        assertEquals("n", Compass.NORTH.toString());
        assertEquals("s", Compass.SOUTH.toString());
        assertEquals("e", Compass.EAST.toString());
        assertEquals("o", Compass.WEST.toString());
        assertEquals("u", Compass.UNKNOWN.toString());
    }

    @Test
    void charToCompass() {
        // Test charToCompass with valid characters
        assertEquals(Compass.NORTH, Compass.charToCompass('n'));
        assertEquals(Compass.SOUTH, Compass.charToCompass('s'));
        assertEquals(Compass.EAST, Compass.charToCompass('e'));
        assertEquals(Compass.WEST, Compass.charToCompass('o'));

        // Test charToCompass with invalid character (should return UNKNOWN)
        assertEquals(Compass.UNKNOWN, Compass.charToCompass('x'));
        assertEquals(Compass.UNKNOWN, Compass.charToCompass('N'));
        assertEquals(Compass.UNKNOWN, Compass.charToCompass(' '));
    }

    @Test
    void values() {
        // Test that values() returns all compass directions
        Compass[] directions = Compass.values();
        assertEquals(5, directions.length);
        assertEquals(Compass.NORTH, directions[0]);
        assertEquals(Compass.SOUTH, directions[1]);
        assertEquals(Compass.EAST, directions[2]);
        assertEquals(Compass.WEST, directions[3]);
        assertEquals(Compass.UNKNOWN, directions[4]);
    }

    @Test
    void valueOf() {
        // Test valueOf with valid enum names
        assertEquals(Compass.NORTH, Compass.valueOf("NORTH"));
        assertEquals(Compass.SOUTH, Compass.valueOf("SOUTH"));
        assertEquals(Compass.EAST, Compass.valueOf("EAST"));
        assertEquals(Compass.WEST, Compass.valueOf("WEST"));
        assertEquals(Compass.UNKNOWN, Compass.valueOf("UNKNOWN"));
    }
}
