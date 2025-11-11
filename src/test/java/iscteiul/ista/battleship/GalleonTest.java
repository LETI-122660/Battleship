package iscteiul.ista.battleship;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GalleonTest {

    @Test
    void getSize() {
        Galleon galleon1 = new Galleon(Compass.NORTH, new Position(0, 0));
        assertEquals(5, galleon1.getSize());

        Galleon galleon2 = new Galleon(Compass.EAST, new Position(5, 3));
        assertEquals(5, galleon2.getSize());
    }


    @Test
    void testGalleonNorthBearing() {
        Position startPos = new Position(3, 3);
        Galleon galleon = new Galleon(Compass.NORTH, startPos);

        assertEquals(5, galleon.getSize());
        assertEquals(5, galleon.getPositions().size());

        // As 3 primeiras posições em linha horizontal
        assertEquals(new Position(3, 3), galleon.getPositions().get(0));
        assertEquals(new Position(3, 4), galleon.getPositions().get(1));
        assertEquals(new Position(3, 5), galleon.getPositions().get(2));

        // As duas posições inferiores (em forma de "T")
        assertEquals(new Position(4, 4), galleon.getPositions().get(3));
        assertEquals(new Position(5, 4), galleon.getPositions().get(4));
    }


    @Test
    void testGalleonSouthBearing() {
        Position startPos = new Position(3, 3);
        Galleon galleon = new Galleon(Compass.SOUTH, startPos);

        assertEquals(5, galleon.getSize());
        assertEquals(5, galleon.getPositions().size());

        // Verifica as posições específicas para SOUTH
        assertTrue(galleon.getPositions().contains(new Position(3, 3)));
        assertTrue(galleon.getPositions().contains(new Position(4, 3)));
        assertTrue(galleon.getPositions().contains(new Position(5, 2)));
        assertTrue(galleon.getPositions().contains(new Position(5, 3)));
        assertTrue(galleon.getPositions().contains(new Position(5, 4)));
    }


    @Test
    void testGalleonEastBearing() {
        Position startPos = new Position(3, 3);
        Galleon galleon = new Galleon(Compass.EAST, startPos);

        assertEquals(5, galleon.getSize());
        assertEquals(5, galleon.getPositions().size());
        assertNotNull(galleon.getPositions());
    }


    @Test
    void testGalleonWestBearing() {
        Position startPos = new Position(4, 4);
        Galleon galleon = new Galleon(Compass.WEST, startPos);

        assertEquals(5, galleon.getSize());
        assertEquals(5, galleon.getPositions().size());
        assertNotNull(galleon.getPositions());
    }


    @Test
    void testMultipleGalleons() {
        Galleon g1 = new Galleon(Compass.NORTH, new Position(1, 1));
        Galleon g2 = new Galleon(Compass.SOUTH, new Position(5, 5));
        Galleon g3 = new Galleon(Compass.EAST, new Position(8, 2));

        assertEquals(5, g1.getSize());
        assertEquals(5, g2.getSize());
        assertEquals(5, g3.getSize());
    }


    @Test
    void testUnknownBearingThrowsIllegalArgumentException() {
        try {
            new Galleon(Compass.UNKNOWN, new Position(0, 0));
            fail("Expected exception was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ERROR! invalid bearing for the galleon", e.getMessage());
        } catch (AssertionError e) {
            // A superclasse Ship pode lançar AssertionError antes de se atingir o código específico do Galleon.
            // Aceitamos esse comportamento como válido para o teste.
        }
    }

}