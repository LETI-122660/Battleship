package iscteiul.ista.battleship;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void getRow() {
        Position position = new Position(3, 5);
        assertEquals(3, position.getRow());

        Position position2 = new Position(7, 2);
        assertEquals(7, position2.getRow());
    }

    @Test
    void getColumn() {
        Position position = new Position(3, 5);
        assertEquals(5, position.getColumn());

        Position position2 = new Position(7, 2);
        assertEquals(2, position2.getColumn());
    }

    @Test
    void testHashCode() {
        Position position1 = new Position(3, 5);
        Position position2 = new Position(3, 5);
        assertEquals(position1.hashCode(), position2.hashCode());

        // Test that different positions have different hash codes (usually)
        Position position3 = new Position(4, 5);
        assertNotEquals(position1.hashCode(), position3.hashCode());

        // Test hash code consistency when isOccupied changes
        Position position4 = new Position(3, 5);
        int hashBefore = position4.hashCode();
        position4.occupy();
        int hashAfter = position4.hashCode();
        assertNotEquals(hashBefore, hashAfter);

        // Test hash code consistency when isHit changes
        Position position5 = new Position(3, 5);
        hashBefore = position5.hashCode();
        position5.shoot();
        hashAfter = position5.hashCode();
        assertNotEquals(hashBefore, hashAfter);
    }

    @Test
    void testEquals() {
        Position position1 = new Position(3, 5);
        Position position2 = new Position(3, 5);

        // Test equals with same position
        assertTrue(position1.equals(position2));

        // Test equals with same object (this == otherPosition)
        assertTrue(position1.equals(position1));

        // Test equals with different positions
        Position position3 = new Position(4, 5);
        assertFalse(position1.equals(position3));

        // Test equals with null
        assertFalse(position1.equals(null));

        // Test equals with different object type
        assertFalse(position1.equals("not a position"));

        // Test equals with different class implementing IPosition
        IPosition differentIPosition = new IPosition() {
            @Override
            public int getRow() { return 3; }
            @Override
            public int getColumn() { return 5; }
            @Override
            public boolean isAdjacentTo(IPosition other) { return false; }
            @Override
            public void occupy() {}
            @Override
            public void shoot() {}
            @Override
            public boolean isOccupied() { return false; }
            @Override
            public boolean isHit() { return false; }
        };
        assertTrue(position1.equals(differentIPosition));

        // Test equals with object that is not IPosition
        Object notIPosition = new Object();
        assertFalse(position1.equals(notIPosition));
    }

    @Test
    void isAdjacentTo() {
        Position position = new Position(3, 3);

        assertTrue(position.isAdjacentTo(new Position(2, 3)));
        assertTrue(position.isAdjacentTo(new Position(4, 3)));
        assertTrue(position.isAdjacentTo(new Position(3, 2)));
        assertTrue(position.isAdjacentTo(new Position(3, 4)));
        assertTrue(position.isAdjacentTo(new Position(2, 2)));
        assertTrue(position.isAdjacentTo(new Position(3, 3))); // mesma posição
        assertTrue(position.isAdjacentTo(new Position(2, 4))); // diagonal
        assertTrue(position.isAdjacentTo(new Position(4, 2))); // diagonal
        assertTrue(position.isAdjacentTo(new Position(4, 4))); // diagonal
        assertFalse(position.isAdjacentTo(new Position(5, 3)));
        assertFalse(position.isAdjacentTo(new Position(3, 5)));
        assertFalse(position.isAdjacentTo(new Position(0, 0)));
    }

    @Test
    void occupy() {
        Position position = new Position(3, 5);
        assertFalse(position.isOccupied());
        position.occupy();
        assertTrue(position.isOccupied());

        // Test multiple occupies
        position.occupy();
        assertTrue(position.isOccupied()); // Should remain true
    }

    @Test
    void shoot() {
        Position position = new Position(3, 5);
        assertFalse(position.isHit());
        position.shoot();
        assertTrue(position.isHit());

        // Test multiple shoots
        position.shoot();
        assertTrue(position.isHit()); // Should remain true
    }

    @Test
    void isOccupied() {
        Position position = new Position(3, 5);
        assertFalse(position.isOccupied());
        position.occupy();
        assertTrue(position.isOccupied());

        // Test with different positions
        Position position2 = new Position(1, 1);
        assertFalse(position2.isOccupied());
    }

    @Test
    void isHit() {
        Position position = new Position(3, 5);
        assertFalse(position.isHit());
        position.shoot();
        assertTrue(position.isHit());

        // Test with different positions
        Position position2 = new Position(1, 1);
        assertFalse(position2.isHit());
    }

    @Test
    void testToString() {
        Position position = new Position(3, 5);
        assertEquals("Linha = 3 Coluna = 5", position.toString());

        Position position2 = new Position(7, 2);
        assertEquals("Linha = 7 Coluna = 2", position2.toString());

        // Test with edge cases
        Position position3 = new Position(0, 0);
        assertEquals("Linha = 0 Coluna = 0", position3.toString());

        Position position4 = new Position(10, 10);
        assertEquals("Linha = 10 Coluna = 10", position4.toString());
    }

    @Test
    void testInitialState() {
        // Test that position starts with correct initial state
        Position position = new Position(1, 1);
        assertFalse(position.isOccupied());
        assertFalse(position.isHit());
        assertEquals(1, position.getRow());
        assertEquals(1, position.getColumn());

        // Test with different coordinates
        Position position2 = new Position(0, 0);
        assertFalse(position2.isOccupied());
        assertFalse(position2.isHit());
        assertEquals(0, position2.getRow());
        assertEquals(0, position2.getColumn());
    }

    @Test
    void testHashCodeConsistency() {
        Position position = new Position(3, 5);
        int initialHashCode = position.hashCode();

        // Hash code deve ser consistente
        assertEquals(initialHashCode, position.hashCode());

        // Posições iguais devem ter hash codes iguais
        Position samePosition = new Position(3, 5);
        assertEquals(position.hashCode(), samePosition.hashCode());

        // Test that occupied state affects hash code
        Position occupiedPosition = new Position(3, 5);
        occupiedPosition.occupy();
        assertNotEquals(position.hashCode(), occupiedPosition.hashCode());

        // Test that hit state affects hash code
        Position hitPosition = new Position(3, 5);
        hitPosition.shoot();
        assertNotEquals(position.hashCode(), hitPosition.hashCode());
    }

    @Test
    void testEqualsWithDifferentStates() {
        Position position1 = new Position(3, 5);
        Position position2 = new Position(3, 5);

        // Should be equal even with different states
        position1.occupy();
        position2.shoot();
        assertTrue(position1.equals(position2)); // equals only compares row and column
    }
}