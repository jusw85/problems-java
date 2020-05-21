package jw.problems.adventofcode.aoc2018;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://adventofcode.com/2018/day/23
 *
 * --- Day 23: Experimental Emergency Teleportation ---
 *
 * Using your torch to search the darkness of the rocky cavern, you finally locate the man's friend: a small reindeer.
 *
 * You're not sure how it got so far in this cave. It looks sick - too sick to walk - and too heavy for you to carry all the way back. Sleighs won't be invented for another 1500 years, of course.
 *
 * The only option is experimental emergency teleportation.
 *
 * You hit the "experimental emergency teleportation" button on the device and push I accept the risk on no fewer than 18 different warning messages. Immediately, the device deploys hundreds of tiny nanobots which fly around the cavern, apparently assembling themselves into a very specific formation. The device lists the X,Y,Z position (pos) for each nanobot as well as its signal radius (r) on its tiny screen (your puzzle input).
 *
 * Each nanobot can transmit signals to any integer coordinate which is a distance away from it less than or equal to its signal radius (as measured by Manhattan distance). Coordinates a distance away of less than or equal to a nanobot's signal radius are said to be in range of that nanobot.
 *
 * Before you start the teleportation process, you should determine which nanobot is the strongest (that is, which has the largest signal radius) and then, for that nanobot, the total number of nanobots that are in range of it, including itself.
 *
 * For example, given the following nanobots:
 *
 * pos=<0,0,0>, r=4
 * pos=<1,0,0>, r=1
 * pos=<4,0,0>, r=3
 * pos=<0,2,0>, r=1
 * pos=<0,5,0>, r=3
 * pos=<0,0,3>, r=1
 * pos=<1,1,1>, r=1
 * pos=<1,1,2>, r=1
 * pos=<1,3,1>, r=1
 *
 * The strongest nanobot is the first one (position 0,0,0) because its signal radius, 4 is the largest. Using that nanobot's location and signal radius, the following nanobots are in or out of range:
 *
 * The nanobot at 0,0,0 is distance 0 away, and so it is in range.
 * The nanobot at 1,0,0 is distance 1 away, and so it is in range.
 * The nanobot at 4,0,0 is distance 4 away, and so it is in range.
 * The nanobot at 0,2,0 is distance 2 away, and so it is in range.
 * The nanobot at 0,5,0 is distance 5 away, and so it is not in range.
 * The nanobot at 0,0,3 is distance 3 away, and so it is in range.
 * The nanobot at 1,1,1 is distance 3 away, and so it is in range.
 * The nanobot at 1,1,2 is distance 4 away, and so it is in range.
 * The nanobot at 1,3,1 is distance 5 away, and so it is not in range.
 *
 * In this example, in total, 7 nanobots are in range of the nanobot with the largest signal radius.
 *
 * Find the nanobot with the largest signal radius. How many nanobots are in range of its signals?
 *
 * Your puzzle answer was 704.
 * --- Part Two ---
 *
 * Now, you just need to figure out where to position yourself so that you're actually teleported when the nanobots activate.
 *
 * To increase the probability of success, you need to find the coordinate which puts you in range of the largest number of nanobots. If there are multiple, choose one closest to your position (0,0,0, measured by manhattan distance).
 *
 * For example, given the following nanobot formation:
 *
 * pos=<10,12,12>, r=2
 * pos=<12,14,12>, r=2
 * pos=<16,12,12>, r=4
 * pos=<14,14,14>, r=6
 * pos=<50,50,50>, r=200
 * pos=<10,10,10>, r=5
 *
 * Many coordinates are in range of some of the nanobots in this formation. However, only the coordinate 12,12,12 is in range of the most nanobots: it is in range of the first five, but is not in range of the nanobot at 10,10,10. (All other coordinates are in range of fewer than five nanobots.) This coordinate's distance from 0,0,0 is 36.
 *
 * Find the coordinates that are in range of the largest number of nanobots. What is the shortest manhattan distance between any of those points and 0,0,0?
 *
 * Your puzzle answer was 111960222.
 */
public class Day23 {

    public static Pattern p = Pattern.compile("pos=<(-?\\d+),(-?\\d+),(-?\\d+)>, r=(\\d+)");

    public static class Bot {
        public int x;
        public int y;
        public int z;
        public int r;

        public Bot(int x, int y, int z, int r) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.r = r;
        }

        public boolean intersects(Bot o) {
            return dist(o) <= (r + o.r);
        }

        public boolean inRange(int ox, int oy, int oz) {
            return dist(ox, oy, oz) <= r;
        }

        public boolean inRange(Bot o) {
            return inRange(o.x, o.y, o.z);
        }

        public int dist(Bot o) {
            return dist(o.x, o.y, o.z);
        }

        public int dist(int ox, int oy, int oz) {
            return Math.abs(ox - x) + Math.abs(oy - y) + Math.abs(oz - z);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Bot{");
            sb.append("x=").append(x);
            sb.append(", y=").append(y);
            sb.append(", z=").append(z);
            sb.append(", r=").append(r);
            sb.append('}');
            return sb.toString();
        }
    }

    public static Comparator<Bot> radiusComp = (o1, o2) -> -Integer.compare(o1.r, o2.r);

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2018/in23"));
        List<Bot> bs = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = p.matcher(line);
            m.matches();
            int x = Integer.parseInt(m.group(1));
            int y = Integer.parseInt(m.group(2));
            int z = Integer.parseInt(m.group(3));
            int r = Integer.parseInt(m.group(4));
            Bot b = new Bot(x, y, z, r);
            bs.add(b);
        }
        sc.close();

        part1(bs);
        part2(bs);
    }

    public static void part2(List<Bot> bs) {
        boolean[][] intersect = new boolean[bs.size()][bs.size()];
        for (int i = 0; i < intersect.length; i++) {
            for (int j = i + 1; j < intersect.length; j++) {
                Bot b1 = bs.get(i);
                Bot b2 = bs.get(j);
                if (b1.intersects(b2)) {
                    intersect[i][j] = true;
                    intersect[j][i] = true;
                }
            }
        }
        Set<Set<Integer>> cliques = bronKerbosch(intersect);
        Set<Integer> maxClique = null;
        int maxSize = Integer.MIN_VALUE;
        for (Set<Integer> clique : cliques) {
            if (clique.size() > maxSize) {
                maxSize = clique.size();
                maxClique = clique;
            }
        }

        int maxDist = Integer.MIN_VALUE;
        for (Integer i : maxClique) {
            Bot bot = bs.get(i);
            int dist = Math.max(bot.dist(0, 0, 0) - bot.r, 0);
            if (dist > maxDist) {
                maxDist = dist;
            }
        }
        System.out.println(maxDist);
    }

    public static Set<Set<Integer>> bronKerbosch(boolean[][] graph) {
        Set<Integer>[] neighbours = getNeighbours(graph);
        Set<Integer> subg = new HashSet<>();
        Set<Integer> cand = new HashSet<>();
        for (int i = 0; i < graph.length; i++) {
            subg.add(i);
            cand.add(i);
        }
        Deque<Integer> q = new ArrayDeque<>();
        Set<Set<Integer>> results = new HashSet<>();
        bronKerbosch2(neighbours, subg, cand, q, results);
        return results;
    }

    private static void bronKerbosch2(
            Set<Integer>[] neighbours, Set<Integer> subg, Set<Integer> cand,
            Deque<Integer> q, Set<Set<Integer>> results) {
        int u = subg.iterator().next();
        for (Integer v : difference(cand, neighbours[u])) {
            cand.remove(v);
            q.addLast(v);
            Set<Integer> subgV = intersect(subg, neighbours[v]);
            if (subgV.isEmpty()) {
                results.add(new HashSet<>(q));
            } else {
                Set<Integer> candV = intersect(cand, neighbours[v]);
                if (!candV.isEmpty()) {
                    bronKerbosch2(neighbours, subgV, candV, q, results);
                }
            }
            q.removeLast();
        }
    }

    public static Set<Integer> difference(Set<Integer> s1, Set<Integer> s2) {
        Set<Integer> r = new HashSet<>();
        for (Integer i : s1) {
            if (!s2.contains(i)) {
                r.add(i);
            }
        }
        return r;
    }

    public static Set<Integer> intersect(Set<Integer> s1, Set<Integer> s2) {
        Set<Integer> r = new HashSet<>();
        for (Integer i : s1) {
            if (s2.contains(i)) {
                r.add(i);
            }
        }
        return r;
    }

    private static Set<Integer>[] getNeighbours(boolean[][] graph) {
        Set<Integer>[] neighbours = new Set[graph.length];
        for (int i = 0; i < neighbours.length; i++) {
            Set<Integer> s = new HashSet<>();
            for (int j = 0; j < graph[i].length; j++) {
                if (graph[i][j]) {
                    s.add(j);
                }
            }
            neighbours[i] = s;
        }
        return neighbours;
    }

    public static void part1(List<Bot> bs) {
        bs.sort(radiusComp);
        Bot strongest = bs.get(0);

        int inRange = 0;
        for (Bot b : bs) {
            if (strongest.inRange(b)) {
                inRange++;
            }
        }
        System.out.println(inRange);
    }

}
