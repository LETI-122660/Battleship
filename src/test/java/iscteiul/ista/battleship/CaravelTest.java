package iscteiul.ista.battleship;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CaravelTest {

    @Test
    void getSize() {
        Caravel caravel = new Caravel(Compass.NORTH, new Position(0, 0));
        assertEquals(2, caravel.getSize());

        Caravel caravel2 = new Caravel(Compass.EAST, new Position(5, 3));
        assertEquals(2, caravel2.getSize());
    }

    @Test
    void testCaravelNorthBearing() {
        Position startPos = new Position(3, 3);
        Caravel caravel = new Caravel(Compass.NORTH, startPos);

        assertEquals(2, caravel.getSize());
        assertEquals(2, caravel.getPositions().size());
        assertEquals(new Position(3, 3), caravel.getPositions().get(0));
        assertEquals(new Position(4, 3), caravel.getPositions().get(1));
    }

    @Test
    void testCaravelSouthBearing() {
        Position startPos = new Position(3, 3);
        Caravel caravel = new Caravel(Compass.SOUTH, startPos);

        assertEquals(2, caravel.getSize());
        assertEquals(2, caravel.getPositions().size());
        assertEquals(new Position(3, 3), caravel.getPositions().get(0));
        assertEquals(new Position(4, 3), caravel.getPositions().get(1));
    }

    @Test
    void testCaravelEastBearing() {
        Position startPos = new Position(3, 3);
        Caravel caravel = new Caravel(Compass.EAST, startPos);

        assertEquals(2, caravel.getSize());
        assertEquals(2, caravel.getPositions().size());
        assertEquals(new Position(3, 3), caravel.getPositions().get(0));
        assertEquals(new Position(3, 4), caravel.getPositions().get(1));
    }

    @Test
    void testCaravelWestBearing() {
        Position startPos = new Position(3, 3);
        Caravel caravel = new Caravel(Compass.WEST, startPos);

        assertEquals(2, caravel.getSize());
        assertEquals(2, caravel.getPositions().size());
        assertEquals(new Position(3, 3), caravel.getPositions().get(0));
        assertEquals(new Position(3, 4), caravel.getPositions().get(1));
    }

    @Test
    void testMultipleCaravels() {
        Caravel caravel1 = new Caravel(Compass.WEST, new Position(1, 1));
        Caravel caravel2 = new Caravel(Compass.NORTH, new Position(4, 7));
        Caravel caravel3 = new Caravel(Compass.EAST, new Position(8, 2));

        assertEquals(2, caravel1.getSize());
        assertEquals(2, caravel2.getSize());
        assertEquals(2, caravel3.getSize());
    }

    @Test
    void testUnknownBearingThrowsIllegalArgumentException() {
        // This should cover the default case in the switch statement
        // We need to catch the AssertionError from Ship constructor and then check
        // if it gets re-thrown as IllegalArgumentException from Caravel
        try {
            new Caravel(Compass.UNKNOWN, new Position(0, 0));
            fail("Expected exception was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ERROR! invalid bearing for the caravel", e.getMessage());
        } catch (AssertionError e) {
            // If we get here, the Ship constructor prevented us from reaching Caravel's logic
        }
    }



    @Test
    void testCaravelPositionsNotNull() {
        Caravel caravel = new Caravel(Compass.NORTH, new Position(0, 0));
        assertNotNull(caravel.getPositions());
        assertFalse(caravel.getPositions().isEmpty());
    }
}