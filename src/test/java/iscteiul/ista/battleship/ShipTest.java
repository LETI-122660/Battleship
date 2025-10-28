package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes da classe Ship
 */
@DisplayName("Testes da classe Ship")
class ShipTest {

    // --- Classe auxiliar para testes (subclasse concreta de Ship) ---
    static class TestShip extends Ship {
        public TestShip(Compass bearing, IPosition pos) {
            super("testship", bearing, pos);
            // Exemplo: navio com 2 posições (horizontal)
            positions.add(pos);
            positions.add(new Position(pos.getRow(), pos.getColumn() + 1));
        }

        @Override
        public Integer getSize() {
            return 2;
        }
    }

    // --- Testes individuais ---

    @Test
    @DisplayName("buildShip() deve criar o tipo de navio correto")
    void buildShip() {
        Ship s = Ship.buildShip("fragata", Compass.NORTH, new Position(2, 3));
        assertNotNull(s);
        assertEquals("Fragata", s.getCategory());
        assertEquals(Compass.NORTH, s.getBearing());
    }

    @Test
    @DisplayName("getCategory() deve retornar a categoria correta")
    void getCategory() {
        Ship s = new TestShip(Compass.EAST, new Position(1, 1));
        assertEquals("testship", s.getCategory());
    }

    @Test
    @DisplayName("getPositions() deve retornar todas as posições ocupadas")
    void getPositions() {
        Ship s = new TestShip(Compass.EAST, new Position(1, 1));
        assertEquals(2, s.getPositions().size());
    }

    @Test
    @DisplayName("getPosition() deve retornar a posição inicial do navio")
    void getPosition() {
        Position p = new Position(3, 4);
        Ship s = new TestShip(Compass.NORTH, p);
        assertEquals(p, s.getPosition());
    }

    @Test
    @DisplayName("getBearing() deve retornar a direção correta")
    void getBearing() {
        Ship s = new TestShip(Compass.SOUTH, new Position(0, 0));
        assertEquals(Compass.SOUTH, s.getBearing());
    }

    @Test
    @DisplayName("stillFloating() deve retornar true enquanto houver posições não atingidas")
    void stillFloating() {
        Ship s = new TestShip(Compass.EAST, new Position(2, 2));
        assertTrue(s.stillFloating());
        s.getPositions().forEach(IPosition::shoot);
        assertFalse(s.stillFloating());
    }

    @Test
    @DisplayName("getTopMostPos() deve retornar a linha mais alta")
    void getTopMostPos() {
        Ship s = new TestShip(Compass.EAST, new Position(2, 2));
        assertEquals(2, s.getTopMostPos());
    }

    @Test
    @DisplayName("getBottomMostPos() deve retornar a linha mais baixa")
    void getBottomMostPos() {
        Ship s = new TestShip(Compass.EAST, new Position(2, 2));
        assertEquals(2, s.getBottomMostPos());
    }

    @Test
    @DisplayName("getLeftMostPos() deve retornar a coluna mais à esquerda")
    void getLeftMostPos() {
        Ship s = new TestShip(Compass.EAST, new Position(2, 2));
        assertEquals(2, s.getLeftMostPos());
    }

    @Test
    @DisplayName("getRightMostPos() deve retornar a coluna mais à direita")
    void getRightMostPos() {
        Ship s = new TestShip(Compass.EAST, new Position(2, 2));
        assertEquals(3, s.getRightMostPos());
    }

    @Test
    @DisplayName("occupies() deve identificar corretamente se a posição é ocupada")
    void occupies() {
        Ship s = new TestShip(Compass.EAST, new Position(1, 1));
        assertTrue(s.occupies(new Position(1, 1)));
        assertTrue(s.occupies(new Position(1, 2)));
        assertFalse(s.occupies(new Position(1, 3)));
    }

    @Test
    @DisplayName("tooCloseTo(IShip) deve detectar navios adjacentes")
    void tooCloseTo() {
        Ship s1 = new TestShip(Compass.EAST, new Position(1, 1));
        Ship s2 = new TestShip(Compass.EAST, new Position(2, 2));
        assertTrue(s1.tooCloseTo(s2));
    }

    @Test
    @DisplayName("tooCloseTo(IPosition) deve detectar posições adjacentes")
    void testTooCloseTo() {
        Ship s = new TestShip(Compass.EAST, new Position(1, 1));
        assertTrue(s.tooCloseTo(new Position(0, 0)));
        assertFalse(s.tooCloseTo(new Position(5, 5)));
    }

    @Test
    @DisplayName("shoot() deve marcar a posição como atingida")
    void shoot() {
        Ship s = new TestShip(Compass.EAST, new Position(1, 1));
        IPosition target = s.getPositions().get(0);
        assertFalse(target.isHit());
        s.shoot(target);
        assertTrue(target.isHit());
    }

    @Test
    @DisplayName("toString() deve retornar uma descrição legível do navio")
    void testToString() {
        Ship s = new TestShip(Compass.WEST, new Position(4, 2));
        String result = s.toString();
        System.out.println(result);
        assertTrue(result.contains("testship"));
        assertTrue(result.contains("o"));
        assertTrue(result.contains("4"));
        assertTrue(result.contains("2"));
    }
}
