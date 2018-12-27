package jw.problems.aoc2015;

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
 * http://adventofcode.com/2015/day/9
 *
 * --- Day 9: All in a Single Night ---
 *
 * Every year, Santa manages to deliver all of his presents in a single night.
 *
 * This year, however, he has some new locations to visit; his elves have provided him the distances between every pair of locations. He can start and end at any two (different) locations he wants, but he must visit each location exactly once. What is the shortest distance he can travel to achieve this?
 *
 * For example, given the following distances:
 *
 * London to Dublin = 464
 * London to Belfast = 518
 * Dublin to Belfast = 141
 *
 * The possible routes are therefore:
 *
 * Dublin -> London -> Belfast = 982
 * London -> Dublin -> Belfast = 605
 * London -> Belfast -> Dublin = 659
 * Dublin -> Belfast -> London = 659
 * Belfast -> Dublin -> London = 605
 * Belfast -> London -> Dublin = 982
 *
 * The shortest of these is London -> Dublin -> Belfast = 605, and so the answer is 605 in this example.
 *
 * What is the distance of the shortest route?
 *
 * Your puzzle answer was 117.
 * --- Part Two ---
 *
 * The next year, just to show off, Santa decides to take the route with the longest distance instead.
 *
 * He can still start and end at any two (different) locations he wants, and he still must visit each location exactly once.
 *
 * For example, given the distances above, the longest route would be 982 via (for example) Dublin -> London -> Belfast.
 *
 * What is the distance of the longest route?
 *
 * Your puzzle answer was 909.
 */
public class Day9 {

    public static Pattern p = Pattern.compile("(\\w+) to (\\w+) = (\\d+)");

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2015/in9"));
        List<String> lines = new ArrayList<>();
        Set<String> citiesSet = new LinkedHashSet<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = p.matcher(line);
            m.matches();
            citiesSet.add(m.group(1));
            citiesSet.add(m.group(2));
            lines.add(line);
        }

        List<String> cities = new ArrayList<>(citiesSet);
        int[][] dists = new int[cities.size()][cities.size()];
        for (String line : lines) {
            Matcher m = p.matcher(line);
            m.matches();
            int from = cities.indexOf(m.group(1));
            int to = cities.indexOf(m.group(2));
            int dist = Integer.parseInt(m.group(3));

            dists[from][to] = dist;
            dists[to][from] = dist;
        }

        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < dists.length; i++) {
            q.add(i);
        }
        Point minmax = new Point(Integer.MAX_VALUE, Integer.MIN_VALUE);
        permute(q, new ArrayDeque<>(), dists, 0, minmax);
        System.out.println(minmax.x);
        System.out.println(minmax.y);
    }

    public static void permute(Queue<Integer> q, Deque<Integer> acc, int[][] dists, int cost, Point minmax) {
        if (q.isEmpty()) {
            if (cost < minmax.x) {
                minmax.x = cost;
            }
            if (cost > minmax.y) {
                minmax.y = cost;
            }
        }
        for (int i = 0; i < q.size(); i++) {
            int idx = q.remove();
            int currCost = 0;
            if (!acc.isEmpty()) {
                currCost = dists[acc.peek()][idx];
                cost += currCost;
            }
            acc.addFirst(idx);
            permute(q, acc, dists, cost, minmax);
            cost -= currCost;
            q.add(acc.removeFirst());
        }
    }

}