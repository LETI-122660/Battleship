package iscteiul.ista.battleship;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FleetTest {

    private Fleet fleet;

    @BeforeEach
    void setUp() {
        fleet = new Fleet();
    }

    private static final class FakePos implements IPosition {
        private final int row, col;
        private boolean occ = false, hit = false;
        FakePos(int r, int c) { row = r; col = c; }

        @Override public int  getRow()                  { return row; }
        @Override public int  getColumn()               { return col; }
        @Override public void occupy()                  { occ = true; }
        @Override public void shoot()                   { hit = true; }
        @Override public boolean isOccupied()           { return occ; }
        @Override public boolean isHit()                { return hit; }
        @Override public boolean isAdjacentTo(IPosition o) {
            int dr = Math.abs(row - o.getRow()), dc = Math.abs(col - o.getColumn());
            return (dr <= 1 && dc <= 1) && !(dr == 0 && dc == 0);
        }
        @Override public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof IPosition p)) return false;
            return row == p.getRow() && col == p.getColumn();
        }
        @Override public int hashCode() { return Objects.hash(row, col); }
        @Override public String toString() { return "(" + row + "," + col + ")"; }
    }
    private static FakePos pos(int r,int c){ return new FakePos(r,c); }

    private static final class FakeShip implements IShip {
        private final String category;
        private final int left, right, top, bottom;
        private final List<IPosition> positions = new ArrayList<>();
        private boolean floating;

        // para simular colisão
        private final Set<IShip> tooClose = new HashSet<>();

        FakeShip(String category, boolean floating, int left, int right, int top, int bottom) {
            this.category = category; this.floating = floating;
            this.left = left; this.right = right; this.top = top; this.bottom = bottom;
        }

        // utilitários
        void occupy(int r, int c){ positions.add(new FakePos(r,c)); }
        void markTooCloseTo(IShip other){ tooClose.add(other); }

        // IShip
        @Override public String          getCategory()     { return category; }
        @Override public Integer         getSize()         { return positions.size(); }
        @Override public List<IPosition> getPositions()    { return positions; }
        @Override public IPosition       getPosition()     { return positions.isEmpty()? new FakePos(0,0):positions.get(0); }
        @Override public Compass         getBearing()      { return null; } // não usado
        @Override public boolean         stillFloating()   { return floating; }

        @Override public int getTopMostPos()    { return top; }
        @Override public int getBottomMostPos() { return bottom; }
        @Override public int getLeftMostPos()   { return left; }
        @Override public int getRightMostPos()  { return right; }

        @Override public boolean occupies(IPosition p){ return positions.stream().anyMatch(p::equals); }
        @Override public boolean tooCloseTo(IShip other){ return tooClose.contains(other); }
        @Override public boolean tooCloseTo(IPosition p){
            return positions.stream().anyMatch(q -> q.isAdjacentTo(p));
        }
        @Override public void shoot(IPosition p){ positions.stream().filter(p::equals).forEach(IPosition::shoot); }

        @Override public String toString(){
            return "Ship[" + category + ", L=" + left + ", R=" + right + ", T=" + top + ", B=" + bottom + ", F=" + floating + "]";
        }
    }

    // helper: capturar System.out apenas quando necessário
    private static String captureOut(Runnable r){
        PrintStream prev = System.out;
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buf));
        try { r.run(); } finally { System.setOut(prev); }
        return buf.toString();
    }

    // ───────────────────── TESTES (simples → complexo) ─────────────────────

    @Test
    void startsEmpty() {
        assertNotNull(fleet.getShips());
        assertTrue(fleet.getShips().isEmpty());
    }

    @Test
    void addShip_acceptsValidInsideBoard() {
        FakeShip s = new FakeShip("Fragata", true, 0,0,0,0);
        assertTrue(fleet.addShip(s));
        assertEquals(1, fleet.getShips().size());
        assertSame(s, fleet.getShips().get(0));
    }

    @Test
    void printShips_printsItems_andEmptyPrintsNothing() {
        // vazio → nada
        String emptyOut = captureOut(() -> Fleet.printShips(new ArrayList<>()));
        assertEquals("", emptyOut);

        // com 1
        FakeShip s = new FakeShip("Barca", true, 0,0,0,0);
        String out = captureOut(() -> Fleet.printShips(List.of(s)));
        assertTrue(out.contains(s.toString()));
    }

    @Test
    void filters_whenEmpty() {
        assertTrue(fleet.getShipsLike("Fragata").isEmpty());
        assertTrue(fleet.getFloatingShips().isEmpty());
        assertNull(fleet.shipAt(pos(2,2)));
    }

    @Test
    void addShip_rejectsOutOfBoard_allEdges() {
        int N = IFleet.BOARD_SIZE;
        assertFalse(fleet.addShip(new FakeShip("Nau", true, -1, 0, 0, 0))); // left < 0
        assertFalse(fleet.addShip(new FakeShip("Nau", true, 0, 0, -1, 0))); // top  < 0
        assertFalse(fleet.addShip(new FakeShip("Nau", true, 0, N, 0, 0)));  // right > N-1
        assertFalse(fleet.addShip(new FakeShip("Nau", true, 0, 0, 0, N)));  // bottom> N-1
    }

    @Test
    void addShip_acceptsExactlyOnEdges() {
        int N = IFleet.BOARD_SIZE;
        FakeShip edge = new FakeShip("Fragata", true, 0, N - 1, 0, N - 1);
        assertTrue(fleet.addShip(edge));
    }

    @Test
    void addShip_rejectsCollision() {
        FakeShip a = new FakeShip("Fragata", true, 0,0,0,0);
        FakeShip b = new FakeShip("Nau", true, 1,1,1,1);
        assertTrue(fleet.addShip(a));
        a.markTooCloseTo(b);
        assertFalse(fleet.addShip(b));
    }

    @Test
    void shipAt_andFilters_work() {
        FakeShip f1 = new FakeShip("Fragata", true, 0,0,0,0); f1.occupy(3,5);
        FakeShip n1 = new FakeShip("Nau", true, 1,1,1,1);
        FakeShip f2 = new FakeShip("Fragata", false, 2,2,2,2);
        fleet.addShip(f1); fleet.addShip(n1); fleet.addShip(f2);

        assertSame(f1, fleet.shipAt(new FakePos(3,5)));
        assertNull(fleet.shipAt(new FakePos(9,9)));

        var like = fleet.getShipsLike("Fragata");
        assertEquals(2, like.size());
        assertTrue(like.contains(f1) && like.contains(f2));

        var floating = fleet.getFloatingShips();
        assertEquals(2, floating.size());
        assertTrue(floating.contains(f1) && floating.contains(n1));
        assertFalse(floating.contains(f2));
    }

    @Test
    void addShip_respectsFleetSizeLimit_branchOnCount() {
        // preencher até ao limite
        while (fleet.getShips().size() < IFleet.FLEET_SIZE) {
            int i = fleet.getShips().size();
            assertTrue(fleet.addShip(new FakeShip("Barca", true, i, i, 0, 0)));
        }
        // ultrapassar → rejeita (exercita a primeira parte do if composto)
        assertFalse(fleet.addShip(new FakeShip("Barca", true, 10, 10, 0, 0)));
    }

    @Test
    void printingHelpers() {
        FakeShip a = new FakeShip("Galeao", true, 0,0,0,0);
        FakeShip b = new FakeShip("Caravela", false, 1,1,1,1);
        fleet.addShip(a); fleet.addShip(b);

        String all = captureOut(fleet::printAllShips);
        assertTrue(all.contains(a.toString()) && all.contains(b.toString()));

        String onlyFloating = captureOut(fleet::printFloatingShips);
        assertTrue(onlyFloating.contains(a.toString()));
        assertFalse(onlyFloating.contains(b.toString()));

        String onlyNau = captureOut(() -> fleet.printShipsByCategory("Nau"));
        assertFalse(onlyNau.contains(a.toString()));
        assertFalse(onlyNau.contains(b.toString()));
    }

    @Test
    void printStatus_printsNothingWhenEmpty_andPrintsWhenHasShips() {
        String empty = captureOut(fleet::printStatus);
        assertEquals("", empty);

        FakeShip f = new FakeShip("Fragata", true, 0,0,0,0);
        FakeShip n = new FakeShip("Nau", false, 1,1,1,1);
        fleet.addShip(f); fleet.addShip(n);

        String s = captureOut(fleet::printStatus);
        assertTrue(s.contains(f.toString()) && s.contains(n.toString()));
    }

    @Test
    void printShipsByCategory_assertsOnNull_whenAssertionsEnabled() {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true; // só fica true com -ea
        if (!assertionsEnabled) return;
        assertThrows(AssertionError.class, () -> fleet.printShipsByCategory(null));
    }

    /**
     * Cobre o ramo onde ships.size() <= FLEET_SIZE é TRUE,
     * isInsideBoard(s) é TRUE e !colisionRisk(s) é FALSE → não adiciona.
     */
    @Test
    void addShip_collisionBranchFalse() {
        FakeShip a = new FakeShip("Fragata", true, 0,0,0,0);
        FakeShip b = new FakeShip("Nau", true, 1,1,1,1);

        // primeiro entra normalmente
        assertTrue(fleet.addShip(a));

        // como o Fleet verifica ships[i].tooCloseTo(s),
        // é o EXISTENTE que deve denunciar o novo navio
        a.markTooCloseTo(b);

        // agora o 3º termo (!colisionRisk) fica FALSE
        assertFalse(fleet.addShip(b));
    }


    /**
     * Cobre o ramo onde ships.size() <= FLEET_SIZE é FALSE (size > FLEET_SIZE),
     * ou seja, o **primeiro termo** do if falha e o if faz short-circuit.
     */
    @Test
    void addShip_sizeTermFalse_shortCircuit() {
        // Preenche até size == FLEET_SIZE (todos aceites)
        while (fleet.getShips().size() < IFleet.FLEET_SIZE) {
            assertTrue(fleet.addShip(new FakeShip("Barca", true, 0,0,0,0)));
        }

        // Este ainda entra porque ships.size() == FLEET_SIZE satisfaz <=
        assertTrue(fleet.addShip(new FakeShip("Barca", true, 0,0,0,0)));

        // Agora ships.size() == FLEET_SIZE + 1 → ships.size() <= FLEET_SIZE é FALSE
        // ⇒ o if falha no 1º termo
        assertFalse(fleet.addShip(new FakeShip("Barca", true, 0,0,0,0)));
    }

}
