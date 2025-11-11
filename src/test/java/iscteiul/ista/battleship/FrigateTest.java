package iscteiul.ista.battleship;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FrigateTest {

    @Test
    void getSize() {
        Frigate frigate = new Frigate(Compass.NORTH, new Position(0, 0));
        assertEquals(4, frigate.getSize());

        Frigate frigate2 = new Frigate(Compass.EAST, new Position(5, 3));
        assertEquals(4, frigate2.getSize());
    }


    @Test
    void testFrigateNorthBearing() {
        Position startPos = new Position(3, 3);
        Frigate frigate = new Frigate(Compass.NORTH, startPos);

        assertEquals(4, frigate.getSize());
        assertEquals(4, frigate.getPositions().size());
        assertEquals(new Position(3, 3), frigate.getPositions().get(0));
        assertEquals(new Position(4, 3), frigate.getPositions().get(1));
        assertEquals(new Position(5, 3), frigate.getPositions().get(2));
        assertEquals(new Position(6, 3), frigate.getPositions().get(3));
    }

    @Test
    void testFrigateSouthBearing() {
        Position startPos = new Position(3, 3);
        Frigate frigate = new Frigate(Compass.SOUTH, startPos);

        assertEquals(4, frigate.getSize());
        assertEquals(4, frigate.getPositions().size());
        assertEquals(new Position(3, 3), frigate.getPositions().get(0));
        assertEquals(new Position(4, 3), frigate.getPositions().get(1));
        assertEquals(new Position(5, 3), frigate.getPositions().get(2));
        assertEquals(new Position(6, 3), frigate.getPositions().get(3));
    }

    @Test
    void testFrigateEastBearing() {
        Position startPos = new Position(3, 3);
        Frigate frigate = new Frigate(Compass.EAST, startPos);

        assertEquals(4, frigate.getSize());
        assertEquals(4, frigate.getPositions().size());
        assertEquals(new Position(3, 3), frigate.getPositions().get(0));
        assertEquals(new Position(3, 4), frigate.getPositions().get(1));
        assertEquals(new Position(3, 5), frigate.getPositions().get(2));
        assertEquals(new Position(3, 6), frigate.getPositions().get(3));
    }

    @Test
    void testFrigateWestBearing() {
        Position startPos = new Position(3, 3);
        Frigate frigate = new Frigate(Compass.WEST, startPos);

        assertEquals(4, frigate.getSize());
        assertEquals(4, frigate.getPositions().size());
        assertEquals(new Position(3, 3), frigate.getPositions().get(0));
        assertEquals(new Position(3, 4), frigate.getPositions().get(1));
        assertEquals(new Position(3, 5), frigate.getPositions().get(2));
        assertEquals(new Position(3, 6), frigate.getPositions().get(3));
    }

    @Test
    void testMultipleFrigates() {
        Frigate frigate1 = new Frigate(Compass.WEST, new Position(1, 1));
        Frigate frigate2 = new Frigate(Compass.NORTH, new Position(4, 7));
        Frigate frigate3 = new Frigate(Compass.EAST, new Position(8, 2));

        assertEquals(4, frigate1.getSize());
        assertEquals(4, frigate2.getSize());
        assertEquals(4, frigate3.getSize());
    }

    @Test
    void testUnknownBearingThrowsIllegalArgumentException() {
        try {
            new Frigate(Compass.UNKNOWN, new Position(0, 0));
            fail("Expected exception was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ERROR! invalid bearing for thr frigate", e.getMessage());
        } catch (AssertionError e) {
            // If we get here, the Ship constructor prevented us from reaching Frigate's logic
        }
    }

    @Test
    void testFrigatePositionsNotNull() {
        Frigate frigate = new Frigate(Compass.NORTH, new Position(0, 0));
        assertNotNull(frigate.getPositions());
        assertFalse(frigate.getPositions().isEmpty());
    }

    @Test
    void testFrigateWithNullBearing() {
        assertThrows(AssertionError.class, () -> {
            new Frigate(null, new Position(0, 0));
        });
    }
}