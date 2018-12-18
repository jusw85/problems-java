package jw.problems.aoc2018;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://adventofcode.com/2018/day/6
 *
 * --- Day 6: Chronal Coordinates ---
 *
 * The device on your wrist beeps several times, and once again you feel like you're falling.
 *
 * "Situation critical," the device announces. "Destination indeterminate. Chronal interference detected. Please specify new target coordinates."
 *
 * The device then produces a list of coordinates (your puzzle input). Are they places it thinks are safe or dangerous? It recommends you check manual page 729. The Elves did not give you a manual.
 *
 * If they're dangerous, maybe you can minimize the danger by finding the coordinate that gives the largest distance from the other points.
 *
 * Using only the Manhattan distance, determine the area around each coordinate by counting the number of integer X,Y locations that are closest to that coordinate (and aren't tied in distance to any other coordinate).
 *
 * Your goal is to find the size of the largest area that isn't infinite. For example, consider the following list of coordinates:
 *
 * 1, 1
 * 1, 6
 * 8, 3
 * 3, 4
 * 5, 5
 * 8, 9
 *
 * If we name these coordinates A through F, we can draw them on a grid, putting 0,0 at the top left:
 *
 * ..........
 * .A........
 * ..........
 * ........C.
 * ...D......
 * .....E....
 * .B........
 * ..........
 * ..........
 * ........F.
 *
 * This view is partial - the actual grid extends infinitely in all directions. Using the Manhattan distance, each location's closest coordinate can be determined, shown here in lowercase:
 *
 * aaaaa.cccc
 * aAaaa.cccc
 * aaaddecccc
 * aadddeccCc
 * ..dDdeeccc
 * bb.deEeecc
 * bBb.eeee..
 * bbb.eeefff
 * bbb.eeffff
 * bbb.ffffFf
 *
 * Locations shown as . are equally far from two or more coordinates, and so they don't count as being closest to any.
 *
 * In this example, the areas of coordinates A, B, C, and F are infinite - while not shown here, their areas extend forever outside the visible grid. However, the areas of coordinates D and E are finite: D is closest to 9 locations, and E is closest to 17 (both including the coordinate's location itself). Therefore, in this example, the size of the largest area is 17.
 *
 * What is the size of the largest area that isn't infinite?
 *
 * Your puzzle answer was 6047.
 * --- Part Two ---
 *
 * On the other hand, if the coordinates are safe, maybe the best you can do is try to find a region near as many coordinates as possible.
 *
 * For example, suppose you want the sum of the Manhattan distance to all of the coordinates to be less than 32. For each location, add up the distances to all of the given coordinates; if the total of those distances is less than 32, that location is within the desired region. Using the same coordinates as above, the resulting region looks like this:
 *
 * ..........
 * .A........
 * ..........
 * ...###..C.
 * ..#D###...
 * ..###E#...
 * .B.###....
 * ..........
 * ..........
 * ........F.
 *
 * In particular, consider the highlighted location 4,3 located at the top middle of the region. Its calculation is as follows, where abs() is the absolute value function:
 *
 * Distance to coordinate A: abs(4-1) + abs(3-1) =  5
 * Distance to coordinate B: abs(4-1) + abs(3-6) =  6
 * Distance to coordinate C: abs(4-8) + abs(3-3) =  4
 * Distance to coordinate D: abs(4-3) + abs(3-4) =  2
 * Distance to coordinate E: abs(4-5) + abs(3-5) =  3
 * Distance to coordinate F: abs(4-8) + abs(3-9) = 10
 * Total distance: 5 + 6 + 4 + 2 + 3 + 10 = 30
 *
 * Because the total distance to all coordinates (30) is less than 32, the location is within the region.
 *
 * This region, which also includes coordinates D and E, has a total size of 16.
 *
 * Your actual region will need to be much larger than this example, though, instead including all locations with a total distance of less than 10000.
 *
 * What is the size of the region containing all locations which have a total distance to all given coordinates of less than 10000?
 *
 * Your puzzle answer was 46320.
 */
public class Day6 {
    public static Pattern p = Pattern.compile("(\\d+), (\\d+)");

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2018/in6"));
        List<Point> ps = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = p.matcher(line);
            m.matches();
            int x = Integer.parseInt(m.group(1));
            int y = Integer.parseInt(m.group(2));
            Point p = new Point(x, y);
            ps.add(p);
        }

        part1(ps);
        part2(ps);
    }

    public static void part2(List<Point> ps) {
        int bounds = 5000;
        int count = 0;
        for (int i = -bounds; i < bounds; i++) {
            for (int j = -bounds; j < bounds; j++) {
                Point cp = new Point(j, i);
                int sum = 0;
                for (Point p : ps) {
                    sum += dist(p, cp);
                }
                if (sum < 10000) {
                    count++;
                }
            }
            if (i % 1000 == 0) {
                System.out.println(i);
            }
        }
        System.out.println(count);
    }

    public static void part1(List<Point> ps) {
        Map<Point, Integer> map1 = getDists(ps, 500);
        Map<Point, Integer> map2 = getDists(ps, 1000);
        Map<Point, Integer> map = new HashMap<>();

        for (Map.Entry<Point, Integer> e : map1.entrySet()) {
            if (e.getValue().equals(map2.get(e.getKey()))) {
                map.put(e.getKey(), e.getValue());
            }
        }

        for (Map.Entry<Point, Integer> e : sortByValue(map).entrySet()) {
            System.out.println(e);
        }
    }

    public static Map<Point, Integer> getDists(List<Point> ps, int bounds) {
        Map<Point, Integer> map = new HashMap<>();
        for (Point p : ps) {
            map.put(p, 0);
        }
        for (int i = -bounds; i < bounds; i++) {
            for (int j = -bounds; j < bounds; j++) {
                Point cp = new Point(j, i);
                int min = Integer.MAX_VALUE;
                Point minPoint = null;
                for (Point p : ps) {
                    int d = dist(cp, p);
                    if (d < min) {
                        min = d;
                        minPoint = p;
                    } else if (d == min) {
                        minPoint = null;
                    }
                }
                if (minPoint != null) {
                    map.put(minPoint, map.get(minPoint) + 1);
                }
            }
        }
        return map;
    }

    public static int dist(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}
