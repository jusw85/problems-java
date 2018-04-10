package jw.problems.aoc2016;

import java.util.BitSet;

/**
 * https://adventofcode.com/2016/day/19
 *
 * --- Day 19: An Elephant Named Joseph ---
 *
 * The Elves contact you over a highly secure emergency channel. Back at the North Pole, the Elves are busy misunderstanding White Elephant parties.
 *
 * Each Elf brings a present. They all sit in a circle, numbered starting with position 1. Then, starting with the first Elf, they take turns stealing all the presents from the Elf to their left. An Elf with no presents is removed from the circle and does not take turns.
 *
 * For example, with five Elves (numbered 1 to 5):
 *
 * 1
 * 5   2
 * 4 3
 *
 * Elf 1 takes Elf 2's present.
 * Elf 2 has no presents and is skipped.
 * Elf 3 takes Elf 4's present.
 * Elf 4 has no presents and is also skipped.
 * Elf 5 takes Elf 1's two presents.
 * Neither Elf 1 nor Elf 2 have any presents, so both are skipped.
 * Elf 3 takes Elf 5's three presents.
 *
 * So, with five Elves, the Elf that sits starting in position 3 gets all the presents.
 *
 * With the number of Elves given in your puzzle input, which Elf gets all the presents?
 *
 * Your puzzle answer was 1842613.
 * --- Part Two ---
 *
 * Realizing the folly of their present-exchange rules, the Elves agree to instead steal presents from the Elf directly across the circle. If two Elves are across the circle, the one on the left (from the perspective of the stealer) is stolen from. The other rules remain unchanged: Elves with no presents are removed from the circle entirely, and the other elves move in slightly to keep the circle evenly spaced.
 *
 * For example, with five Elves (again numbered 1 to 5):
 *
 * The Elves sit in a circle; Elf 1 goes first:
 *
 * 1
 * 5   2
 * 4 3
 *
 * Elves 3 and 4 are across the circle; Elf 3's present is stolen, being the one to the left. Elf 3 leaves the circle, and the rest of the Elves move in:
 *
 * 1           1
 * 5   2  -->  5   2
 * 4 -          4
 *
 * Elf 2 steals from the Elf directly across the circle, Elf 5:
 *
 * 1         1
 * -   2  -->     2
 * 4         4
 *
 * Next is Elf 4 who, choosing between Elves 1 and 2, steals from Elf 1:
 *
 * -          2
 * 2  -->
 * 4          4
 *
 * Finally, Elf 2 steals from Elf 4:
 *
 * 2
 * -->  2
 * -
 *
 * So, with five Elves, the Elf that sits starting in position 2 gets all the presents.
 *
 * With the number of Elves given in your puzzle input, which Elf now gets all the presents?
 *
 * Your puzzle answer was 1424135.
 */
public class Day19 {

    public static void main(String[] args) {
        int len = 3018458;
//        System.out.println(part1Slow(len));
//        System.out.println(part2Slow(len));
        System.out.println(part1Fast(len));
        System.out.println(part2Fast(len));
    }

    // ~1min runtime
    // josephus problem for closed solution
    public static int part1Slow(int len) {
        BitSet bs = new BitSet(len);
        bs.set(0, len);

        int i = 0;
        while (bs.cardinality() > 1) {
            int j = bs.nextSetBit(i + 1);
            if (j < 0) {
                j = bs.nextSetBit(0);
            }
            bs.set(j, false);
            int k = bs.nextSetBit(j + 1);
            if (k < 0) {
                k = bs.nextSetBit(0);
            }
            i = k;
        }
        return i;
    }

    // ~4hr runtime
    public static int part2Slow(int len) {
        BitSet bs = new BitSet(len);
        bs.set(0, len);

        int i = 0;
        while (bs.cardinality() > 1) {
            int mid = bs.cardinality() / 2;
            int k = i;
            for (int j = 0; j < mid; j++) {
                k = bs.nextSetBit(k + 1);
                if (k < 0) {
                    k = bs.nextSetBit(0);
                }
            }
            bs.set(k, false);
            i = bs.nextSetBit(i + 1);
            if (i < 0) {
                i = bs.nextSetBit(0);
            }
        }
        return i;
    }

    // use solution pattern from part1Slow to determine closed solution
    public static int part1Fast(int len) {
        if (len == 0 || len == 1)
            return 0;
        int s = 2;
        int l = 2;
        while (!(len >= s && len < (s + l))) {
            s += l;
            l *= 2;
        }
        return 2 * (len - s);
    }

    // use solution pattern from part2Slow to determine closed solution
    public static int part2Fast(int len) {
        if (len == 0 || len == 1)
            return 0;
        int s = 2;
        int l = 2;
        while (!(len >= s && len < (s + l))) {
            s += l;
            l *= 3;
        }
        if (len < s + (l / 2)) {
            return len - s;
        } else {
            return (s + l) - (2 * (s + l - len));
        }
    }
}