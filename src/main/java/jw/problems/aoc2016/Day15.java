package jw.problems.aoc2016;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://adventofcode.com/2016/day/15
 *
 * --- Day 15: Timing is Everything ---
 *
 * The halls open into an interior plaza containing a large kinetic sculpture. The sculpture is in a sealed enclosure and seems to involve a set of identical spherical capsules that are carried to the top and allowed to bounce through the maze of spinning pieces.
 *
 * Part of the sculpture is even interactive! When a button is pressed, a capsule is dropped and tries to fall through slots in a set of rotating discs to finally go through a little hole at the bottom and come out of the sculpture. If any of the slots aren't aligned with the capsule as it passes, the capsule bounces off the disc and soars away. You feel compelled to get one of those capsules.
 *
 * The discs pause their motion each second and come in different sizes; they seem to each have a fixed number of positions at which they stop. You decide to call the position with the slot 0, and count up for each position it reaches next.
 *
 * Furthermore, the discs are spaced out so that after you push the button, one second elapses before the first disc is reached, and one second elapses as the capsule passes from one disc to the one below it. So, if you push the button at time=100, then the capsule reaches the top disc at time=101, the second disc at time=102, the third disc at time=103, and so on.
 *
 * The button will only drop a capsule at an integer time - no fractional seconds allowed.
 *
 * For example, at time=0, suppose you see the following arrangement:
 *
 * Disc #1 has 5 positions; at time=0, it is at position 4.
 * Disc #2 has 2 positions; at time=0, it is at position 1.
 *
 * If you press the button exactly at time=0, the capsule would start to fall; it would reach the first disc at time=1. Since the first disc was at position 4 at time=0, by time=1 it has ticked one position forward. As a five-position disc, the next position is 0, and the capsule falls through the slot.
 *
 * Then, at time=2, the capsule reaches the second disc. The second disc has ticked forward two positions at this point: it started at position 1, then continued to position 0, and finally ended up at position 1 again. Because there's only a slot at position 0, the capsule bounces away.
 *
 * If, however, you wait until time=5 to push the button, then when the capsule reaches each disc, the first disc will have ticked forward 5+1 = 6 times (to position 0), and the second disc will have ticked forward 5+2 = 7 times (also to position 0). In this case, the capsule would fall through the discs and come out of the machine.
 *
 * However, your situation has more than two discs; you've noted their positions in your puzzle input. What is the first time you can press the button to get a capsule?
 *
 * Your puzzle answer was 317371.
 * --- Part Two ---
 *
 * After getting the first capsule (it contained a star! what great fortune!), the machine detects your success and begins to rearrange itself.
 *
 * When it's done, the discs are back in their original configuration as if it were time=0 again, but a new disc with 11 positions and starting at position 0 has appeared exactly one second below the previously-bottom disc.
 *
 * With this new disc, and counting again starting from time=0 with the configuration in your puzzle input, what is the first time you can press the button to get another capsule?
 *
 * Your puzzle answer was 2080951.
 */
public class Day15 {

    public static Pattern p = Pattern.compile("Disc #(\\d+) has (\\d+) positions; at time=0, it is at position (\\d+).");

    //    Disc #i has n positions; at time=0, it is at position p.
    //    a = (2n-(p+i)) % n
    //    x = a (mod n)
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2016/in15"));
        Queue<Congruence> cs = new LinkedList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = p.matcher(line);
            m.matches();
            int i = Integer.parseInt(m.group(1));
            int n = Integer.parseInt(m.group(2));
            int p = Integer.parseInt(m.group(3));

            int a = ((2 * n) - (p + i)) % n;
            cs.add(new Congruence(a, n));
        }
        sc.close();

        while (cs.size() > 1) {
            cs.add(solveCongruence(cs.poll(), cs.poll()));
        }
        System.out.println(cs.peek());
    }

    //    using Chinese Remainder Theorem
    public static Congruence solveCongruence(Congruence c1, Congruence c2) {
        BezoutResult r = bezout(c1.n, c2.n);
        int a = (c1.a * r.v1 * c2.n) + (c2.a * r.u1 * c1.n);
        int n = c1.n * c2.n;
        a = a % n;
        while (a < 0) {
            a += n;
        }
        return new Congruence(a, n);
    }

    public static BezoutResult bezout(int a, int b) {
        int n1 = a, n2 = b;
        int u1 = 1, v1 = 0;
        int u2 = 0, v2 = 1;
//        invariant:
//        n1 = (u1 * a) + (v1 * b)
//        n2 = (u2 * a) + (v2 * b)
        while (n2 != 0) {
            int q = n1 / n2;
            int n1s = n1;
            int u1s = u1;
            int v1s = v1;
            n1 = n2;
            u1 = u2;
            v1 = v2;
            n2 = n1s - (q * n2);
            u2 = u1s - (q * u2);
            v2 = v1s - (q * v2);
        }
        BezoutResult r = new BezoutResult(a, b, n1, u1, v1, u2, v2);
        return r;
    }

    public static class Congruence {
        public int a;
        public int n;

        public Congruence(int a, int n) {
            this.a = a;
            this.n = n;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Congruence{");
            sb.append("a=").append(a);
            sb.append(", n=").append(n);
            sb.append('}');
            return sb.toString();
        }
    }

    public static class BezoutResult {
        public int a;
        public int b;
        public int gcd;
        public int u1;
        public int v1;
        public int u2;
        public int v2;

        public BezoutResult(int a, int b, int gcd, int u1, int v1, int u2, int v2) {
            this.a = a;
            this.b = b;
            this.gcd = gcd;
            this.u1 = u1;
            this.v1 = v1;
            this.u2 = u2;
            this.v2 = v2;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("BezoutResult{");
            sb.append("a=").append(a);
            sb.append(", b=").append(b);
            sb.append(", gcd=").append(gcd);
            sb.append(", u1=").append(u1);
            sb.append(", v1=").append(v1);
            sb.append(", u2=").append(u2);
            sb.append(", v2=").append(v2);
            sb.append('}');
            return sb.toString();
        }
    }
}
