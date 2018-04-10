package jw.problems.aoc2016;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * https://adventofcode.com/2016/day/13
 *
 * --- Day 13: A Maze of Twisty Little Cubicles ---
 *
 * You arrive at the first floor of this new building to discover a much less welcoming environment than the shiny atrium of the last one. Instead, you are in a maze of twisty little cubicles, all alike.
 *
 * Every location in this area is addressed by a pair of non-negative integers (x,y). Each such coordinate is either a wall or an open space. You can't move diagonally. The cube maze starts at 0,0 and seems to extend infinitely toward positive x and y; negative values are invalid, as they represent a location outside the building. You are in a small waiting area at 1,1.
 *
 * While it seems chaotic, a nearby morale-boosting poster explains, the layout is actually quite logical. You can determine whether a given x,y coordinate will be a wall or an open space using a simple system:
 *
 * Find x*x + 3*x + 2*x*y + y + y*y.
 * Add the office designer's favorite number (your puzzle input).
 * Find the binary representation of that sum; count the number of bits that are 1.
 * If the number of bits that are 1 is even, it's an open space.
 * If the number of bits that are 1 is odd, it's a wall.
 *
 * For example, if the office designer's favorite number were 10, drawing walls as # and open spaces as ., the corner of the building containing 0,0 would look like this:
 *
 * 0123456789
 * 0 .#.####.##
 * 1 ..#..#...#
 * 2 #....##...
 * 3 ###.#.###.
 * 4 .##..#..#.
 * 5 ..##....#.
 * 6 #...##.###
 *
 * Now, suppose you wanted to reach 7,4. The shortest route you could take is marked as O:
 *
 * 0123456789
 * 0 .#.####.##
 * 1 .O#..#...#
 * 2 #OOO.##...
 * 3 ###O#.###.
 * 4 .##OO#OO#.
 * 5 ..##OOO.#.
 * 6 #...##.###
 *
 * Thus, reaching 7,4 would take a minimum of 11 steps (starting from your current location, 1,1).
 *
 * What is the fewest number of steps required for you to reach 31,39?
 *
 * Your puzzle answer was 92.
 * --- Part Two ---
 *
 * How many locations (distinct x,y coordinates, including your starting location) can you reach in at most 50 steps?
 *
 * Your puzzle answer was 124.
 */
public class Day13 {

    public static void main(String[] args) {
        Maze m1 = new Maze(10, 1, 1);
        m1.solve(7, 4);
        m1.pprint(10, 7);

        Maze m2 = new Maze(1350, 1, 1);
        m2.solve(31, 39);
        m2.pprint(50, 50);
        m2.countLocations(50);
    }

    public static class Maze {
        private int input;
        private int x;
        private int y;
        private List<Point> path = new ArrayList<>();

        public Maze(int input, int startX, int startY) {
            this.input = input;
            this.x = startX;
            this.y = startY;
        }

        public void solve(int dx, int dy) {
            path.clear();
            Point start = new Point(x, y);
            Point dest = new Point(dx, dy);
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
                    path = constructPath(cameFrom, minNode);
                    return;
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
                    if (gscore.containsKey(p)
                            && tentativeGscore >= gscore.get(p))
                        continue;

                    cameFrom.put(p, minNode);
                    gscore.put(p, tentativeGscore);
                    fscore.put(p, tentativeGscore + h(p, dest));
                }
            }
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

        public boolean isWall(int ix, int iy) {
            long x = (long) ix;
            long y = (long) iy;
            long val = (x * x) + (3 * x) + (2 * x * y) + y + (y * y);
            val += input;
            int c = 0;
            while (val > 0) {
                if ((val & 1) == 1)
                    c++;
                val >>= 1;
            }
            return (c % 2) != 0;
        }

        public void pprint(int nx, int ny) {
            for (int i = 0; i < ny; i++) {
                for (int j = 0; j < nx; j++) {
                    char c = isWall(j, i) ? '#' : ' ';
                    Point p = new Point(j, i);
                    if (path.contains(p))
                        c = 'O';
                    System.out.print(c);
                }
                System.out.println();
            }
            System.out.println(path.size());
        }

        public void countLocations(int numSteps) {
            int xMin = Math.max(0, x - numSteps);
            int xMax = x + numSteps;
            int yMin = Math.max(0, y - numSteps);
            int yMax = y + numSteps;
            int count = 0;

            for (int i = yMin; i <= yMax; i++) {
                for (int j = xMin; j <= xMax; j++) {
                    if (isWall(j, i))
                        continue;
                    solve(j, i);
                    if ((path.size() > 0) && path.size() - 1 <= numSteps) {
                        count++;
                    }
                }
            }
            System.out.println(count);
        }
    }
}