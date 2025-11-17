package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

/**
 * Expanded Testes da classe Ship
 */
@DisplayName("Comprehensive tests for Ship class")
class ShipTest {

    // --- Concrete TestShip for abstract Ship ---
    static class TestShip extends Ship {
        public TestShip(Compass bearing, IPosition pos) {
            super("testship", bearing, pos);
            // Default horizontal 4-position ship
            positions.add(pos);
            positions.add(new Position(pos.getRow(), pos.getColumn() + 1));
            positions.add(new Position(pos.getRow(), pos.getColumn() + 2));
            positions.add(new Position(pos.getRow(), pos.getColumn() + 3));
        }

        @Override
        public Integer getSize() {
            return 4;
        }
    }

    // --- Factory method tests ---
    @Test
    @DisplayName("buildShip() creates correct types and null for invalid")
    void buildShip() {
        assertInstanceOf(Barge.class, Ship.buildShip("barca", Compass.NORTH, new Position(0, 0)));
        assertInstanceOf(Caravel.class, Ship.buildShip("caravela", Compass.NORTH, new Position(0, 0)));
        assertInstanceOf(Carrack.class, Ship.buildShip("nau", Compass.NORTH, new Position(0, 0)));
        assertInstanceOf(Frigate.class, Ship.buildShip("fragata", Compass.NORTH, new Position(0, 0)));
        assertInstanceOf(Galleon.class, Ship.buildShip("galeao", Compass.NORTH, new Position(0, 0)));
        assertNull(Ship.buildShip("invalido", Compass.NORTH, new Position(0, 0)));
    }

    @Test
    @DisplayName("buildShip() throws AssertionError on null bearing or position")
    void buildShipNullInputs() {
        assertThrows(AssertionError.class, () -> Ship.buildShip("barca", null, new Position(0, 0)));
        assertThrows(AssertionError.class, () -> Ship.buildShip("barca", Compass.NORTH, null));
    }

    // --- Position tests ---
    @Test
    @DisplayName("getCategory() returns correct category")
    void getCategory() {
        Ship s = new TestShip(Compass.EAST, new Position(1, 1));
        assertEquals("testship", s.getCategory());
    }

    @Test
    @DisplayName("getPositions() returns correct list of positions")
    void getPositions() {
        Ship s = new TestShip(Compass.EAST, new Position(1, 1));
        assertEquals(4, s.getPositions().size());
    }

    @Test
    @DisplayName("getPosition() returns starting position")
    void getPosition() {
        Position p = new Position(3, 4);
        Ship s = new TestShip(Compass.NORTH, p);
        assertEquals(p, s.getPosition());
    }

    @Test
    @DisplayName("getBearing() returns correct direction")
    void getBearing() {
        Ship s = new TestShip(Compass.SOUTH, new Position(0, 0));
        assertEquals(Compass.SOUTH, s.getBearing());
    }

    // --- Floating tests ---
    @Test
    @DisplayName("stillFloating() true until all positions are hit")
    void stillFloating() {
        Ship s = new TestShip(Compass.EAST, new Position(2, 2));
        assertTrue(s.stillFloating());
        s.getPositions().forEach(IPosition::shoot);
        assertFalse(s.stillFloating());
    }

    @Test
    @DisplayName("stillFloating() returns true if only partially hit")
    void partialHitsStillFloating() {
        Ship s = new TestShip(Compass.EAST, new Position(1, 1));
        s.getPositions().get(0).shoot(); // hit first
        s.getPositions().get(1).shoot(); // hit second
        assertTrue(s.stillFloating());
        s.getPositions().get(2).shoot(); // hit third
        s.getPositions().get(3).shoot(); // hit fourth
        assertFalse(s.stillFloating());
    }

    // --- Top/Bottom/Left/Right tests ---
    @Test
    @DisplayName("Horizontal ship bounds")
    void horizontalShipBounds() {
        Ship s = new TestShip(Compass.EAST, new Position(2, 2));
        assertEquals(2, s.getTopMostPos());
        assertEquals(2, s.getBottomMostPos());
        assertEquals(2, s.getLeftMostPos());
        assertEquals(5, s.getRightMostPos()); // 2,3,4,5
    }

    @Test
    @DisplayName("Vertical ship bounds")
    void verticalShipBounds() {
        Ship s = new Ship("vertical", Compass.NORTH, new Position(2, 3)) {
            {
                positions.add(new Position(2, 3));
                positions.add(new Position(3, 3));
                positions.add(new Position(4, 3));
                positions.add(new Position(5, 3));
            }
            @Override
            public Integer getSize() { return 4; }
        };
        assertEquals(2, s.getTopMostPos());
        assertEquals(5, s.getBottomMostPos());
        assertEquals(3, s.getLeftMostPos());
        assertEquals(3, s.getRightMostPos());
    }



    // --- Occupies tests ---
    @Test
    @DisplayName("occupies() detects occupied positions")
    void occupies() {
        Ship s = new TestShip(Compass.EAST, new Position(1, 1));
        assertTrue(s.occupies(new Position(1, 1)));
        assertTrue(s.occupies(new Position(1, 2)));
        assertTrue(s.occupies(new Position(1, 3)));
        assertTrue(s.occupies(new Position(1, 4)));
        assertFalse(s.occupies(new Position(1, 5)));
    }

    @Test
    @DisplayName("occupies() throws AssertionError on null input")
    void occupiesNull() {
        Ship s = new TestShip(Compass.EAST, new Position(1, 1));
        assertThrows(AssertionError.class, () -> s.occupies(null));
    }

    // --- tooCloseTo tests ---
    @Test
    @DisplayName("tooCloseTo(IPosition) detects adjacency")
    void tooCloseToPosition() {
        Ship s = new TestShip(Compass.EAST, new Position(1, 1));
        assertTrue(s.tooCloseTo(new Position(0, 0))); // adjacent
        assertFalse(s.tooCloseTo(new Position(10, 10))); // far
    }

    @Test
    @DisplayName("tooCloseTo(IShip) detects adjacency")
    void tooCloseToShip() {
        Ship s1 = new TestShip(Compass.EAST, new Position(1, 1));
        Ship s2 = new TestShip(Compass.EAST, new Position(2, 2));
        Ship s3 = new TestShip(Compass.EAST, new Position(10, 10));

        assertTrue(s1.tooCloseTo(s2));
        assertFalse(s1.tooCloseTo(s3));

        IShip null_ship = null;
        assertThrows(AssertionError.class, () -> s1.tooCloseTo(null_ship));
    }

    @Test
    @DisplayName("tooCloseTo() non-adjacent diagonal positions")
    void diagonalNonAdjacent() {
        Ship s = new TestShip(Compass.EAST, new Position(5, 5));
        assertFalse(s.tooCloseTo(new Position(8, 8)));
    }

    @Test
    @DisplayName("tooCloseTo() with multiple ships")
    void multipleShipAdjacency() {
        Ship s1 = new TestShip(Compass.EAST, new Position(1, 1));
        Ship s2 = new TestShip(Compass.EAST, new Position(4, 4));
        Ship s3 = new TestShip(Compass.EAST, new Position(1, 2));
        assertTrue(s1.tooCloseTo(s3));
        assertFalse(s1.tooCloseTo(s2));
    }

    // --- Shoot tests ---
    @Test
    @DisplayName("shoot() marks the correct position as hit")
    void shoot() {
        Ship s = new TestShip(Compass.EAST, new Position(1, 1));
        IPosition target = s.getPositions().get(0);
        assertFalse(target.isHit());
        s.shoot(target);
        assertTrue(target.isHit());
    }

    @Test
    @DisplayName("shoot() throws AssertionError on null input")
    void shootNull() {
        Ship s = new TestShip(Compass.EAST, new Position(1, 1));
        assertThrows(AssertionError.class, () -> s.shoot(null));
    }

    @Test
    @DisplayName("shoot() multiple times on same position")
    void shootMultipleTimes() {
        Ship s = new TestShip(Compass.EAST, new Position(1, 1));
        IPosition pos = s.getPositions().get(0);
        s.shoot(pos);
        s.shoot(pos);
        assertTrue(pos.isHit());
    }

    // --- toString tests ---
    @Test
    @DisplayName("toString() contains category, bearing, position")
    void testToString() {
        Ship s = new TestShip(Compass.WEST, new Position(4, 2));
        String str = s.toString();
        assertTrue(str.contains("testship"));
        assertTrue(str.contains("4"));
        assertTrue(str.contains("2"));
    }

    // --- Empty ship tests ---
    @Test
    @DisplayName("Empty ship positions should behave safely")
    void emptyShip() {
        Ship s = new Ship("empty", Compass.NORTH, new Position(0, 0)) {
            @Override
            public Integer getSize() { return 0; }
        };
        s.positions = new ArrayList<>(); // no positions
        assertFalse(s.stillFloating());
        assertThrows(IndexOutOfBoundsException.class, s::getTopMostPos);
        assertThrows(IndexOutOfBoundsException.class, s::getBottomMostPos);
        assertThrows(IndexOutOfBoundsException.class, s::getLeftMostPos);
        assertThrows(IndexOutOfBoundsException.class, s::getRightMostPos);
    }


}
