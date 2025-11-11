package iscteiul.ista.battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BargeTest {

    @Test
    void getSize() {
        // Create a barge and test that it always returns size 1
        Barge barge = new Barge(Compass.NORTH, new Position(0, 0));
        assertEquals(1, barge.getSize());

        // Test with different bearing and position
        Barge barge2 = new Barge(Compass.EAST, new Position(5, 3));
        assertEquals(1, barge2.getSize());
    }

    @Test
    void testBargeConstructorAndProperties() {
        // Test barge creation and verify its properties
        Position position = new Position(2, 2);
        Barge barge = new Barge(Compass.SOUTH, position);

        // Verify size is always 1
        assertEquals(1, barge.getSize());

        // Verify positions contain exactly one position (the one it was created with)
        assertEquals(1, barge.getPositions().size());
        assertEquals(position, barge.getPositions().get(0));
    }

    @Test
    void testMultipleBarges() {
        // Test multiple barge instances to ensure consistent behavior
        Barge barge1 = new Barge(Compass.WEST, new Position(1, 1));
        Barge barge2 = new Barge(Compass.NORTH, new Position(4, 7));
        Barge barge3 = new Barge(Compass.EAST, new Position(8, 2));

        // All barges should have size 1
        assertEquals(1, barge1.getSize());
        assertEquals(1, barge2.getSize());
        assertEquals(1, barge3.getSize());
    }
}