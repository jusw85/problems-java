package jw.problems.adventofcode.aoc2016;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * https://adventofcode.com/2016/day/24
 *
 * --- Day 24: Air Duct Spelunking ---
 *
 * You've finally met your match; the doors that provide access to the roof are locked tight, and all of the controls and related electronics are inaccessible. You simply can't reach them.
 *
 * The robot that cleans the air ducts, however, can.
 *
 * It's not a very fast little robot, but you reconfigure it to be able to interface with some of the exposed wires that have been routed through the HVAC system. If you can direct it to each of those locations, you should be able to bypass the security controls.
 *
 * You extract the duct layout for this area from some blueprints you acquired and create a map with the relevant locations marked (your puzzle input). 0 is your current location, from which the cleaning robot embarks; the other numbers are (in no particular order) the locations the robot needs to visit at least once each. Walls are marked as #, and open passages are marked as .. Numbers behave like open passages.
 *
 * For example, suppose you have a map like the following:
 *
 * ###########
 * #0.1.....2#
 * #.#######.#
 * #4.......3#
 * ###########
 *
 * To reach all of the points of interest as quickly as possible, you would have the robot take the following path:
 *
 * 0 to 4 (2 steps)
 * 4 to 1 (4 steps; it can't move diagonally)
 * 1 to 2 (6 steps)
 * 2 to 3 (2 steps)
 *
 * Since the robot isn't very fast, you need to find it the shortest route. This path is the fewest steps (in the above example, a total of 14) required to start at 0 and then visit every other location at least once.
 *
 * Given your actual map, and starting from location 0, what is the fewest number of steps required to visit every non-0 number marked on the map at least once?
 *
 * Your puzzle answer was 428.
 * --- Part Two ---
 *
 * Of course, if you leave the cleaning robot somewhere weird, someone is bound to notice.
 *
 * What is the fewest number of steps required to start at 0, visit every non-0 number marked on the map at least once, and then return to 0?
 *
 * Your puzzle answer was 680.
 */
public class Day24 {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2016/in24"));
        char[][] data = new char[43][181];
        int i = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            data[i++] = line.toCharArray();
        }

        Maze maze = new Maze(data);
        maze.solve();
    }

    public static class Maze {
        private char[][] maze;
        private Point[] points = new Point[8];
        private int[][] dists = new int[8][8];

        public Maze(char[][] maze) {
            this.maze = maze;
        }

        public void solve() {
            initPoints();
            initDists();
            String in = "1234567";
            int min = Integer.MAX_VALUE;
            String minPath = "";
            do {
                String currpath = "0" + in + "0";
                int len = getLength(currpath);
                if (len < min) {
                    minPath = currpath;
                    min = len;
                }
            } while ((in = nextPermutation(in)) != null);
            System.out.println(minPath);
            System.out.println(min);
        }

        public int getLength(String path) {
            char[] cs = path.toCharArray();
            int c = path.charAt(0) - '0';
            int dist = 0;
            for (int i = 1; i < cs.length; i++) {
                int n = path.charAt(i) - '0';
                dist += dists[c][n];
                c = n;
            }
            return dist;
        }

        public String nextPermutation(String in) {
            StringBuilder sb = new StringBuilder(in);
            int i;
            for (i = in.length() - 2; i >= 0; --i) {
                if (sb.charAt(i) < sb.charAt(i + 1)) {
                    break;
                }
            }
            if (i < 0) {
                return null;
            }

            int j;
            for (j = in.length() - 1; j > i + 1; j--) {
                if (sb.charAt(j) > sb.charAt(i)) {
                    break;
                }
            }
            char c1 = sb.charAt(i);
            char c2 = sb.charAt(j);
            sb.setCharAt(i, c2);
            sb.setCharAt(j, c1);

            StringBuilder sb2 = new StringBuilder(sb.substring(i + 1, in.length()));
            sb2.reverse();
            sb.replace(i + 1, in.length(), sb2.toString());
            return sb.toString();
        }

        public void printDists() {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    System.out.print(dists[i][j] + ",");
                }
                System.out.println();
            }
        }

        public void initPoints() {
            for (int y = 0; y < maze.length; y++) {
                for (int x = 0; x < maze[y].length; x++) {
                    if (maze[y][x] >= '0' && maze[y][x] <= '7') {
                        points[maze[y][x] - '0'] = new Point(x, y);
                    }
                }
            }
        }

        public void initDists() {
            for (int i = 0; i < 8; i++) {
                for (int j = i + 1; j < 8; j++) {
                    List<Point> path = getPath(points[i], points[j]);
                    dists[i][j] = path.size() - 1;
                    dists[j][i] = dists[i][j];
                }
            }
        }

        public List<Point> getPath(Point start, Point end) {
            return getPath(start.x, start.y, end.x, end.y);
        }

        public List<Point> getPath(int sx, int sy, int ex, int ey) {
            Point start = new Point(sx, sy);
            Point dest = new Point(ex, ey);
            Set<Point> visited = new HashSet<>();
            Set<Point> toVisit = new HashSet<>();
            Map<Point, Point> cameFrom = new HashMap<>();
            Map<Point, Integer> gscore = new HashMap<>(); // actual
            Map<Point, Integer> fscore = new HashMap<>(); // estimated

            toVisit.add(start);
            gscore.put(start, 0);
            fscore.put(start, 0 + h(start, dest));

            while (!toVisit.isEmpty()) {
                Point minNode = new Point();
                int minscore = Integer.MAX_VALUE;
                for (Point p : toVisit) {
                    int fp = fscore.get(p);
                    if (fp < minscore) {
                        minNode = p;
                        minscore = fp;
                    }
                }

                if (minNode.equals(dest)) {
                    return constructPath(cameFrom, minNode);
                }

                toVisit.remove(minNode);
                visited.add(minNode);

                for (Point p : getMoves(minNode)) {
                    if (visited.contains(p))
                        continue;
                    if (!toVisit.contains(p)) {
                        toVisit.add(p);
                    }

                    int tentativeGscore = gscore.get(minNode) + 1;
                    if (gscore.containsKey(p) && tentativeGscore >= gscore.get(p))
                        continue;

                    cameFrom.put(p, minNode);
                    gscore.put(p, tentativeGscore);
                    fscore.put(p, tentativeGscore + h(p, dest));
                }
            }
            return null;
        }

        public void printPath(List<Point> path) {
            for (int y = 0; y < maze.length; y++) {
                for (int x = 0; x < maze[y].length; x++) {
                    char c = maze[y][x];
                    Point p = new Point(x, y);
                    if (path.contains(p))
                        c = 'O';
                    System.out.print(c);
                }
                System.out.println();
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int y = 0; y < maze.length; y++) {
                for (int x = 0; x < maze[y].length; x++) {
                    sb.append(maze[y][x]);
                }
                sb.append(System.lineSeparator());
            }
            return sb.toString();
        }

        public List<Point> constructPath(Map<Point, Point> cameFrom, Point p) {
            List<Point> path = new ArrayList<>();
            path.add(p);
            while (cameFrom.containsKey(p)) {
                p = cameFrom.get(p);
                path.add(p);
            }
            return path;
        }

        public List<Point> getMoves(Point p) {
            return getMoves(p.x, p.y);
        }

        public List<Point> getMoves(int x, int y) {
            List<Point> moves = new ArrayList<>();
            if (x > 0 && !isWall(x - 1, y))
                moves.add(new Point(x - 1, y));
            if (y > 0 && !isWall(x, y - 1))
                moves.add(new Point(x, y - 1));
            if (!isWall(x + 1, y))
                moves.add(new Point(x + 1, y));
            if (!isWall(x, y + 1))
                moves.add(new Point(x, y + 1));
            return moves;
        }

        public int h(Point s, Point d) {
            return h(s.x, s.y, d.x, d.y);
        }

        public int h(int sx, int sy, int dx, int dy) {
            return Math.abs(sx - dx) + Math.abs(sy - dy);
        }

        public boolean isWall(Point p) {
            return isWall(p.x, p.y);
        }

        public boolean isWall(int x, int y) {
            return maze[y][x] == '#';
        }
    }
}