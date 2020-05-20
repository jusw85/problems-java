package uva;

import jw.problems.uva.p100.Main;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class P100Test {

    @Test
    public void single() {
        Main p = new Main();
        int c;

        c = p.getCycleLength(1);
        assertEquals(1, c);

        c = p.getCycleLength(2);
        assertEquals(2, c);

        c = p.getCycleLength(4);
        assertEquals(3, c);

        c = p.getCycleLength(22);
        assertEquals(16, c);
    }

    @Test
    public void range() {
        Main p = new Main();
        int max;

        max = p.findMax(1, 10);
        assertEquals(20, max);

        max = p.findMax(100, 200);
        assertEquals(125, max);

        max = p.findMax(201, 210);
        assertEquals(89, max);

        max = p.findMax(900, 1000);
        assertEquals(174, max);

        max = p.findMax(1, 110000);
        assertEquals(354, max);
    }
}
