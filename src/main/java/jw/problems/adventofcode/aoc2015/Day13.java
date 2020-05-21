package jw.problems.adventofcode.aoc2015;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http://adventofcode.com/2015/day/13
 *
 * --- Day 13: Knights of the Dinner Table ---
 *
 * In years past, the holiday feast with your family hasn't gone so well. Not everyone gets along! This year, you resolve, will be different. You're going to find the optimal seating arrangement and avoid all those awkward conversations.
 *
 * You start by writing up a list of everyone invited and the amount their happiness would increase or decrease if they were to find themselves sitting next to each other person. You have a circular table that will be just big enough to fit everyone comfortably, and so each person will have exactly two neighbors.
 *
 * For example, suppose you have only four attendees planned, and you calculate their potential happiness as follows:
 *
 * Alice would gain 54 happiness units by sitting next to Bob.
 * Alice would lose 79 happiness units by sitting next to Carol.
 * Alice would lose 2 happiness units by sitting next to David.
 * Bob would gain 83 happiness units by sitting next to Alice.
 * Bob would lose 7 happiness units by sitting next to Carol.
 * Bob would lose 63 happiness units by sitting next to David.
 * Carol would lose 62 happiness units by sitting next to Alice.
 * Carol would gain 60 happiness units by sitting next to Bob.
 * Carol would gain 55 happiness units by sitting next to David.
 * David would gain 46 happiness units by sitting next to Alice.
 * David would lose 7 happiness units by sitting next to Bob.
 * David would gain 41 happiness units by sitting next to Carol.
 *
 * Then, if you seat Alice next to David, Alice would lose 2 happiness units (because David talks so much), but David would gain 46 happiness units (because Alice is such a good listener), for a total change of 44.
 *
 * If you continue around the table, you could then seat Bob next to Alice (Bob gains 83, Alice gains 54). Finally, seat Carol, who sits next to Bob (Carol gains 60, Bob loses 7) and David (Carol gains 55, David gains 41). The arrangement looks like this:
 *
 * +41 +46
 * +55   David    -2
 * Carol       Alice
 * +60    Bob    +54
 * -7  +83
 *
 * After trying every other seating arrangement in this hypothetical scenario, you find that this one is the most optimal, with a total change in happiness of 330.
 *
 * What is the total change in happiness for the optimal seating arrangement of the actual guest list?
 *
 * Your puzzle answer was 709.
 * --- Part Two ---
 *
 * In all the commotion, you realize that you forgot to seat yourself. At this point, you're pretty apathetic toward the whole thing, and your happiness wouldn't really go up or down regardless of who you sit next to. You assume everyone else would be just as ambivalent about sitting next to you, too.
 *
 * So, add yourself to the list, and give all happiness relationships that involve you a score of 0.
 *
 * What is the total change in happiness for the optimal seating arrangement that actually includes yourself?
 *
 * Your puzzle answer was 668.
 */
public class Day13 {

    public static Pattern p = Pattern.compile("(\\w+) would (gain|lose) (-?\\d+) happiness units by sitting next to (\\w+).");

    public static int[][] parseInput(String inFile) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inFile));
        List<String> lines = new ArrayList<>();
        Set<String> namesSet = new LinkedHashSet<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = p.matcher(line);
            m.matches();
            namesSet.add(m.group(1));
            namesSet.add(m.group(4));
            lines.add(line);
        }

        List<String> names = new ArrayList<>(namesSet);
        int[][] happy = new int[names.size()][names.size()];
        for (String line : lines) {
            Matcher m = p.matcher(line);
            m.matches();
            int from = names.indexOf(m.group(1));
            int to = names.indexOf(m.group(4));
            int dist = Integer.parseInt(m.group(3));
            if (m.group(2).equals("lose")) {
                dist = -dist;
            }
            happy[from][to] = dist;
        }
        return happy;
    }

    public static void main(String[] args) throws FileNotFoundException {
        int[][] happy = parseInput("./etc/aoc2015/in13");
        System.out.println(getOptimal(happy));
        System.out.println(getOptimal(part2(happy)));
    }

    public static int[][] part2(int[][] in) {
        int[][] out = new int[in.length + 1][in.length + 1];
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out.length; j++) {
                if ((i == out.length - 1) || (j == out.length - 1)) {
                    out[i][j] = 0;
                } else {
                    out[i][j] = in[i][j];
                }
            }
        }
        return out;
    }

    public static int getOptimal(int[][] happy) {
        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < happy.length; i++) {
            q.add(i);
        }
        Point minmax = new Point(Integer.MAX_VALUE, Integer.MIN_VALUE);
        permute(q, new ArrayDeque<>(), happy, 0, minmax);
        return minmax.y;
    }

    public static void permute(Queue<Integer> q, Deque<Integer> acc, int[][] happy, int cost, Point minmax) {
        if (q.isEmpty()) {
            int finalCost = cost +
                    happy[acc.peekFirst()][acc.peekLast()] +
                    happy[acc.peekLast()][acc.peekFirst()];
            if (finalCost < minmax.x) {
                minmax.x = finalCost;
            }
            if (finalCost > minmax.y) {
                minmax.y = finalCost;
            }
            return;
        }
        for (int i = 0; i < q.size(); i++) {
            int idx = q.remove();
            int currCost = 0;
            if (!acc.isEmpty()) {
                currCost = happy[acc.peek()][idx] + happy[idx][acc.peek()];
                cost += currCost;
            }
            acc.addFirst(idx);
            permute(q, acc, happy, cost, minmax);
            cost -= currCost;
            q.add(acc.removeFirst());
        }
    }

}