package jw.problems.aoc2017;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * http://adventofcode.com/2017/day/16
 *
 * --- Day 16: Permutation Promenade ---
 *
 * You come upon a very unusual sight; a group of programs here appear to be dancing.
 *
 * There are sixteen programs in total, named a through p. They start by standing in a line: a stands in position 0, b stands in position 1, and so on until p, which stands in position 15.
 *
 * The programs' dance consists of a sequence of dance moves:
 *
 * Spin, written sX, makes X programs move from the end to the front, but maintain their order otherwise. (For example, s3 on abcde produces cdeab).
 * Exchange, written xA/B, makes the programs at positions A and B swap places.
 * Partner, written pA/B, makes the programs named A and B swap places.
 *
 * For example, with only five programs standing in a line (abcde), they could do the following dance:
 *
 * s1, a spin of size 1: eabcd.
 * x3/4, swapping the last two programs: eabdc.
 * pe/b, swapping programs e and b: baedc.
 *
 * After finishing their dance, the programs end up in order baedc.
 *
 * You watch the dance for a while and record their dance moves (your puzzle input). In what order are the programs standing after their dance?
 *
 * Your puzzle answer was fnloekigdmpajchb.
 * --- Part Two ---
 *
 * Now that you're starting to get a feel for the dance moves, you turn your attention to the dance as a whole.
 *
 * Keeping the positions they ended up in from their previous dance, the programs perform it again and again: including the first dance, a total of one billion (1000000000) times.
 *
 * In the example above, their second dance would begin with the order baedc, and use the same dance moves:
 *
 * s1, a spin of size 1: cbaed.
 * x3/4, swapping the last two programs: cbade.
 * pe/b, swapping programs e and b: ceadb.
 *
 * In what order are the programs standing after their billion dances?
 *
 * Your puzzle answer was amkjepdhifolgncb.
 */
public class Day16 {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2017/in16"));
        String line = sc.nextLine();
        sc.close();

        StringBuilder sb = getRoot();
        List<SwapL> execL = new ArrayList<>();
        List<Executable> execS = new ArrayList<>();
        for (String cmd : line.split(",")) {
            if (cmd.startsWith("s")) {
                int steps = Integer.parseInt(cmd.substring(1));
                Rotate e = new Rotate();
                e.steps = steps % 16;
                execS.add(e);
            } else if (cmd.startsWith("x")) {
                String[] s = cmd.substring(1).split("/");
                SwapP e = new SwapP();
                e.p1 = Integer.parseInt(s[0]);
                e.p2 = Integer.parseInt(s[1]);
                execS.add(e);
            } else if (cmd.startsWith("p")) {
                String[] s = cmd.substring(1).split("/");
                SwapL e = new SwapL();
                e.c1 = s[0].charAt(0);
                e.c2 = s[1].charAt(0);
                execL.add(e);
            }
        }

        Map<Character, Character> lmap = simplifyLExec(execL);
        for (Executable e : execS) {
            e.exec(sb);
        }
        int[] posmap = getSwapMap(getRoot(), sb);
        sb = getRoot();

        for (int i = 0; i < 1000000000; i++) {
            StringBuilder sb2 = new StringBuilder();
            for (int j = 0; j < posmap.length; j++) {
                sb2.append(sb.charAt(posmap[j]));
            }
            for (Map.Entry<Character, Character> entry : lmap.entrySet()) {
                int idx = sb2.indexOf(String.valueOf(entry.getKey()));
                sb.setCharAt(idx, entry.getValue());
            }
        }
        System.out.println(sb);
    }

    public static Map<Character, Character> simplifyLExec(List<SwapL> swapLs) {
        Map<Character, Character> map = new HashMap<>();
        for (char c = 'a'; c <= 'p'; c++) {
            map.put(c, c);
        }
        for (SwapL l : swapLs) {
            char tmp = map.get(l.c1);
            map.put(l.c1, map.get(l.c2));
            map.put(l.c2, tmp);
        }
        Map<Character, Character> inverse = new HashMap<>();
        for (Map.Entry<Character, Character> entry : map.entrySet()) {
            inverse.put(entry.getValue(), entry.getKey());
        }
        return inverse;
    }

    public static int[] getSwapMap(StringBuilder orig, StringBuilder sb) {
        int[] map = new int[orig.length()];
        for (int i = 0; i < orig.length(); i++) {
            map[i] = orig.indexOf(String.valueOf(sb.charAt(i)));
        }
        return map;
    }

    public static StringBuilder getRoot() {
        StringBuilder sb = new StringBuilder();
        for (char c = 'a'; c <= 'p'; c++) {
            sb.append(c);
        }
        return sb;
    }

    public static class SwapP implements Executable {
        public int p1;
        public int p2;

        public void exec(StringBuilder sb) {
            char c1 = sb.charAt(p1);
            char c2 = sb.charAt(p2);
            sb.setCharAt(p1, c2);
            sb.setCharAt(p2, c1);
        }
    }

    public static class SwapL implements Executable {
        public char c1;
        public char c2;

        public void exec(StringBuilder sb) {
            int p1 = sb.indexOf(String.valueOf(c1));
            int p2 = sb.indexOf(String.valueOf(c2));
            sb.setCharAt(p1, c2);
            sb.setCharAt(p2, c1);
        }
    }

    public static class Rotate implements Executable {
        public int steps;

        public void exec(StringBuilder sb) {
            String str = sb.substring(sb.length() - steps) + sb.substring(0, sb.length() - steps);
            sb.setLength(0);
            sb.append(str);
        }
    }

    public interface Executable {
        void exec(StringBuilder sb);
    }

}