// java
package iscteiul.ista.battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Position testPosition;
    private Position invalidPos;
    private Caravel testShip;
    private Fleet testFleet;
    private Game testGame;

    @BeforeEach
    void setup() {
        testPosition = new Position(0, 0);
        invalidPos = new Position(100, 100);
        testShip = new Caravel(Compass.EAST, testPosition);
        testFleet = new Fleet();
        testFleet.addShip(testShip);
        testGame = new Game(testFleet);
    }

    @Nested
    class methodTests {
        @Test
        void fire() {
            List<IPosition> posList = testShip.getPositions();
            Position pos1 = (Position) posList.get(0);
            Position pos2 = new Position(3, 3);

            testGame.fire(invalidPos);
            assertEquals(1, testGame.getInvalidShots());

            testGame.fire(pos1);
            assertEquals(1, testGame.getHits());

            testGame.fire(pos1);
            assertEquals(1, testGame.getRepeatedShots());

            testGame.fire(pos2); // valid miss
            assertEquals(1, testGame.getHits());
        }

        @Test
        void getShots() {
            List<IPosition> posList = testShip.getPositions();
            Position pos1 = (Position) posList.get(0);
            Position pos2 = new Position(3, 3);

            testGame.fire(invalidPos); // not added
            testGame.fire(pos1);      // added
            testGame.fire(pos1);      // repeated, not added
            testGame.fire(pos2);      // added

            List<IPosition> shots = testGame.getShots();
            assertEquals(2, shots.size());
            assertTrue(shots.contains(pos1));
            assertTrue(shots.contains(pos2));
        }

        @Test
        void getRepeatedShots() {
            Position p = (Position) testShip.getPositions().get(0);
            testGame.fire(p);
            testGame.fire(p); // repeated
            assertEquals(1, testGame.getRepeatedShots());
        }

        @Test
        void getInvalidShots() {
            testGame.fire(invalidPos);
            testGame.fire(invalidPos);
            assertEquals(2, testGame.getInvalidShots());
        }

        @Test
        void getHits() {
            Position p = (Position) testShip.getPositions().get(0);
            testGame.fire(p);
            assertEquals(1, testGame.getHits());
        }

        @Test
        void getSunkShips() {
            // shoot all positions of the ship to sink it
            for (IPosition p : testShip.getPositions()) {
                testGame.fire(p);
            }
            assertEquals(1, testGame.getSunkShips());
        }

        @Test
        void getRemainingShips() {
            // initially one floating ship
            assertEquals(1, testGame.getRemainingShips());

            // sink it
            for (IPosition p : testShip.getPositions()) {
                testGame.fire(p);
            }
            assertEquals(0, testGame.getRemainingShips());
        }

        @Test
        void printBoard() {
            // capture stdout
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(out));
            try {
                testGame.printBoard(testShip.getPositions(), '#');
            } finally {
                System.setOut(originalOut);
            }
            String printed = out.toString();
            assertTrue(printed.contains("#"));
        }

        @Test
        void printValidShots() {
            Position p = (Position) testShip.getPositions().get(0);
            testGame.fire(p);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(out));
            try {
                testGame.printValidShots();
            } finally {
                System.setOut(originalOut);
            }
            String printed = out.toString();
            assertTrue(printed.contains("X"));
        }
    }
    @Nested
    class ShotsTests {
        @Test
        void validShots_hit_and_sink_and_counters_update() {

            // first hit
            IShip result1 = testGame.fire(new Position(0,0));
            assertNull(result1);
            assertEquals(1, testGame.getShots().size());
            assertEquals(0, testGame.getInvalidShots());
            assertEquals(0, testGame.getRepeatedShots());
            assertEquals(1, testGame.getHits());
            assertEquals(0, testGame.getSunkShips());
            assertEquals(1, testGame.getRemainingShips());

            // second hit sinks
            IShip result2 = testGame.fire(new Position(0,1));
            assertNotNull(result2);
            assertEquals(2, testGame.getShots().size());
            assertEquals(0, testGame.getInvalidShots());
            assertEquals(0, testGame.getRepeatedShots());
            assertEquals(2, testGame.getHits());
            assertEquals(1, testGame.getSunkShips());
            assertEquals(0, testGame.getRemainingShips());
        }

        @Test
        void repeated_shots_counted() {

            // valid shot but no ship
            IPosition pos = new Position(0, 0);
            testGame.fire(pos);
            assertEquals(1, testGame.getShots().size());

            // repeated shot
            testGame.fire(pos);
            assertEquals(1, testGame.getRepeatedShots());
            assertEquals(1, testGame.getShots().size(), "repeated shots should not be added");
        }

        @Test
        void invalid_shots_counted() {
            // invalid shot (row negative)
            IPosition invalid = new Position(-1, 0);
            testGame.fire(invalid);
            assertEquals(1, testGame.getInvalidShots());
            assertEquals(0, testGame.getShots().size());

            // invalid shot (col negative)
            IPosition invalid1 = new Position(0, -1);
            testGame.fire(invalid1);
            assertEquals(2, testGame.getInvalidShots());
            assertEquals(0, testGame.getShots().size());

            // invalid shot (out of upper bound)
            IPosition oob = new Position(IFleet.BOARD_SIZE+1, 0);
            testGame.fire(oob);
            assertEquals(3, testGame.getInvalidShots());
            assertEquals(0, testGame.getShots().size());

            // invalid shot (out of lower bound)
            IPosition oob1 = new Position(0, IFleet.BOARD_SIZE+1);
            testGame.fire(oob1);
            assertEquals(4, testGame.getInvalidShots());
            assertEquals(0, testGame.getShots().size());
        }
    }

    @Nested
    class PrintTests {
        @Test
        void print_valid_shots_and_fleet_output_contains_markers() {
            List<IPosition> shipPos = List.of(new Position(1,1), new Position(2,2));

            // fire one shot
            testGame.fire(new Position(1,1));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(baos));
            try {
                testGame.printValidShots();
                testGame.printFleet();
            } finally {
                System.setOut(originalOut);
            }
            String out = baos.toString();

            // should contain at least one X for the shot and '#' for ship positions
            assertTrue(out.contains("X") , "output should contain an X for a valid shot");
            assertTrue(out.contains("#"), "output should contain # for fleet positions");
        }
    }

}
