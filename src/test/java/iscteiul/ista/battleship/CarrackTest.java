package iscteiul.ista.battleship;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CarrackTest {

    @Test
    void getSize() {
        Carrack carrack = new Carrack(Compass.NORTH, new Position(0, 0));
        assertEquals(3, carrack.getSize());

        Carrack carrack2 = new Carrack(Compass.EAST, new Position(5, 3));
        assertEquals(3, carrack2.getSize());
    }

    @Test
    void testCarrackConstructorAndProperties() {
        Position position = new Position(2, 2);
        Carrack carrack = new Carrack(Compass.SOUTH, position);

        assertEquals(3, carrack.getSize());
        assertEquals(3, carrack.getPositions().size());
        assertEquals(position, carrack.getPositions().get(0));
        assertEquals(new Position(3, 2), carrack.getPositions().get(1));
        assertEquals(new Position(4, 2), carrack.getPositions().get(2));
    }

    @Test
    void testMultipleCarracks() {
        Carrack carrack1 = new Carrack(Compass.WEST, new Position(1, 1));
        Carrack carrack2 = new Carrack(Compass.NORTH, new Position(4, 7));
        Carrack carrack3 = new Carrack(Compass.EAST, new Position(8, 2));

        assertEquals(3, carrack1.getSize());
        assertEquals(3, carrack2.getSize());
        assertEquals(3, carrack3.getSize());
    }

    @Test
    void testInvalidBearing() {
        // This tests the default case in switch statement (IllegalArgumentException)
        assertThrows(IllegalArgumentException.class, () -> {
            new Carrack(Compass.UNKNOWN, new Position(0, 0));
        });
    }
}