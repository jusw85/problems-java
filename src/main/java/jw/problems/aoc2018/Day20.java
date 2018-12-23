package jw.problems.aoc2018;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * https://adventofcode.com/2018/day/20
 *
 * --- Day 20: A Regular Map ---
 *
 * While you were learning about instruction pointers, the Elves made considerable progress. When you look up, you discover that the North Pole base construction project has completely surrounded you.
 *
 * The area you are in is made up entirely of rooms and doors. The rooms are arranged in a grid, and rooms only connect to adjacent rooms when a door is present between them.
 *
 * For example, drawing rooms as ., walls as #, doors as | or -, your current position as X, and where north is up, the area you're in might look like this:
 *
 * #####
 * #.|.#
 * #-###
 * #.|X#
 * #####
 *
 * You get the attention of a passing construction Elf and ask for a map. "I don't have time to draw out a map of this place - it's huge. Instead, I can give you directions to every room in the facility!" He writes down some directions on a piece of parchment and runs off. In the example above, the instructions might have been ^WNE$, a regular expression or "regex" (your puzzle input).
 *
 * The regex matches routes (like WNE for "west, north, east") that will take you from your current room through various doors in the facility. In aggregate, the routes will take you through every door in the facility at least once; mapping out all of these routes will let you build a proper map and find your way around.
 *
 * ^ and $ are at the beginning and end of your regex; these just mean that the regex doesn't match anything outside the routes it describes. (Specifically, ^ matches the start of the route, and $ matches the end of it.) These characters will not appear elsewhere in the regex.
 *
 * The rest of the regex matches various sequences of the characters N (north), S (south), E (east), and W (west). In the example above, ^WNE$ matches only one route, WNE, which means you can move west, then north, then east from your current position. Sequences of letters like this always match that exact route in the same order.
 *
 * Sometimes, the route can branch. A branch is given by a list of options separated by pipes (|) and wrapped in parentheses. So, ^N(E|W)N$ contains a branch: after going north, you must choose to go either east or west before finishing your route by going north again. By tracing out the possible routes after branching, you can determine where the doors are and, therefore, where the rooms are in the facility.
 *
 * For example, consider this regex: ^ENWWW(NEEE|SSE(EE|N))$
 *
 * This regex begins with ENWWW, which means that from your current position, all routes must begin by moving east, north, and then west three times, in that order. After this, there is a branch. Before you consider the branch, this is what you know about the map so far, with doors you aren't sure about marked with a ?:
 *
 * #?#?#?#?#
 * ?.|.|.|.?
 * #?#?#?#-#
 * ?X|.?
 * #?#?#
 *
 * After this point, there is (NEEE|SSE(EE|N)). This gives you exactly two options: NEEE and SSE(EE|N). By following NEEE, the map now looks like this:
 *
 * #?#?#?#?#
 * ?.|.|.|.?
 * #-#?#?#?#
 * ?.|.|.|.?
 * #?#?#?#-#
 * ?X|.?
 * #?#?#
 *
 * Now, only SSE(EE|N) remains. Because it is in the same parenthesized group as NEEE, it starts from the same room NEEE started in. It states that starting from that point, there exist doors which will allow you to move south twice, then east; this ends up at another branch. After that, you can either move east twice or north once. This information fills in the rest of the doors:
 *
 * #?#?#?#?#
 * ?.|.|.|.?
 * #-#?#?#?#
 * ?.|.|.|.?
 * #-#?#?#-#
 * ?.?.?X|.?
 * #-#-#?#?#
 * ?.|.|.|.?
 * #?#?#?#?#
 *
 * Once you've followed all possible routes, you know the remaining unknown parts are all walls, producing a finished map of the facility:
 *
 * #########
 * #.|.|.|.#
 * #-#######
 * #.|.|.|.#
 * #-#####-#
 * #.#.#X|.#
 * #-#-#####
 * #.|.|.|.#
 * #########
 *
 * Sometimes, a list of options can have an empty option, like (NEWS|WNSE|). This means that routes at this point could effectively skip the options in parentheses and move on immediately. For example, consider this regex and the corresponding map:
 *
 * ^ENNWSWW(NEWS|)SSSEEN(WNSE|)EE(SWEN|)NNN$
 *
 * ###########
 * #.|.#.|.#.#
 * #-###-#-#-#
 * #.|.|.#.#.#
 * #-#####-#-#
 * #.#.#X|.#.#
 * #-#-#####-#
 * #.#.|.|.|.#
 * #-###-###-#
 * #.|.|.#.|.#
 * ###########
 *
 * This regex has one main route which, at three locations, can optionally include additional detours and be valid: (NEWS|), (WNSE|), and (SWEN|). Regardless of which option is taken, the route continues from the position it is left at after taking those steps. So, for example, this regex matches all of the following routes (and more that aren't listed here):
 *
 * ENNWSWWSSSEENEENNN
 * ENNWSWWNEWSSSSEENEENNN
 * ENNWSWWNEWSSSSEENEESWENNNN
 * ENNWSWWSSSEENWNSEEENNN
 *
 * By following the various routes the regex matches, a full map of all of the doors and rooms in the facility can be assembled.
 *
 * To get a sense for the size of this facility, you'd like to determine which room is furthest from you: specifically, you would like to find the room for which the shortest path to that room would require passing through the most doors.
 *
 * In the first example (^WNE$), this would be the north-east corner 3 doors away.
 * In the second example (^ENWWW(NEEE|SSE(EE|N))$), this would be the south-east corner 10 doors away.
 * In the third example (^ENNWSWW(NEWS|)SSSEEN(WNSE|)EE(SWEN|)NNN$), this would be the north-east corner 18 doors away.
 *
 * Here are a few more examples:
 *
 * Regex: ^ESSWWN(E|NNENN(EESS(WNSE|)SSS|WWWSSSSE(SW|NNNE)))$
 * Furthest room requires passing 23 doors
 *
 * #############
 * #.|.|.|.|.|.#
 * #-#####-###-#
 * #.#.|.#.#.#.#
 * #-#-###-#-#-#
 * #.#.#.|.#.|.#
 * #-#-#-#####-#
 * #.#.#.#X|.#.#
 * #-#-#-###-#-#
 * #.|.#.|.#.#.#
 * ###-#-###-#-#
 * #.|.#.|.|.#.#
 * #############
 *
 * Regex: ^WSSEESWWWNW(S|NENNEEEENN(ESSSSW(NWSW|SSEN)|WSWWN(E|WWS(E|SS))))$
 * Furthest room requires passing 31 doors
 *
 * ###############
 * #.|.|.|.#.|.|.#
 * #-###-###-#-#-#
 * #.|.#.|.|.#.#.#
 * #-#########-#-#
 * #.#.|.|.|.|.#.#
 * #-#-#########-#
 * #.#.#.|X#.|.#.#
 * ###-#-###-#-#-#
 * #.|.#.#.|.#.|.#
 * #-###-#####-###
 * #.|.#.|.|.#.#.#
 * #-#-#####-#-#-#
 * #.#.|.|.|.#.|.#
 * ###############
 *
 * What is the largest number of doors you would be required to pass through to reach a room? That is, find the room for which the shortest path from your starting location to that room would require passing through the most doors; what is the fewest doors you can pass through to reach it?
 *
 * Your puzzle answer was 3207.
 * --- Part Two ---
 *
 * Okay, so the facility is big.
 *
 * How many rooms have a shortest path from your current location that pass through at least 1000 doors?
 *
 * Your puzzle answer was 8361.
 */
public class Day20 {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2018/in20"));
        String line = sc.nextLine();
        sc.close();

        int size = 250;
        Point st = new Point(size / 2, size / 2);
        char[][] grid = initGrid(size);
        grid[st.y][st.x] = '.';

        StringWalker s = new StringWalker(line);
        s.nextChar();

        Set<Point> set = new HashSet();
        set.add(new Point(st));
        mapper(s, grid, set);
//        pprint(grid);
        bfs(grid, st);
    }


    public static class Node {
        public Point p;
        public int depth;

        public Node(Point p, int depth) {
            this.p = p;
            this.depth = depth;
        }
    }

    public static void bfs(char[][] grid, Point st) {
        Queue<Node> toVisit = new LinkedList<>();
        Set<Point> visited = new HashSet<>();
        toVisit.add(new Node(st, 0));
        visited.add(st);
        int maxDepth = Integer.MIN_VALUE;
        int gt1000 = 0;
        while (!toVisit.isEmpty()) {
            Node node = toVisit.poll();
            Point np = node.p;
            Point[] ps1 = {
                    new Point(np.x - 1, np.y),
                    new Point(np.x + 1, np.y),
                    new Point(np.x, np.y + 1),
                    new Point(np.x, np.y - 1)};
            Point[] ps2 = {
                    new Point(np.x - 2, np.y),
                    new Point(np.x + 2, np.y),
                    new Point(np.x, np.y + 2),
                    new Point(np.x, np.y - 2)};
            for (int i = 0; i < ps1.length; i++) {
                Point p1 = ps1[i];
                Point p2 = ps2[i];
                if (grid[p1.y][p1.x] == '.' && !visited.contains(p2)) {
                    visited.add(p2);
                    int newDepth = node.depth + 1;
                    if (newDepth > maxDepth) {
                        maxDepth = newDepth;
                    }
                    if (newDepth >= 1000) {
                        gt1000++;
                    }
                    toVisit.add(new Node(p2, newDepth));
                }
            }
        }
        System.out.println(maxDepth);
        System.out.println(gt1000);
    }

    public static Set<Point> mapper(StringWalker s, char[][] grid, Set<Point> set) {
        while (true) {
            char c = s.nextChar();
            switch (c) {
                case 'E':
                    for (Point p : set) {
                        grid[p.y][++p.x] = '.';
                        grid[p.y][++p.x] = '.';
                    }
                    break;
                case 'N':
                    for (Point p : set) {
                        grid[--p.y][p.x] = '.';
                        grid[--p.y][p.x] = '.';
                    }
                    break;
                case 'S':
                    for (Point p : set) {
                        grid[++p.y][p.x] = '.';
                        grid[++p.y][p.x] = '.';
                    }
                    break;
                case 'W':
                    for (Point p : set) {
                        grid[p.y][--p.x] = '.';
                        grid[p.y][--p.x] = '.';
                    }
                    break;
                case '(':
                    Set<Point> newSet = new HashSet();
                    while (s.peekPrev() != ')') {
                        newSet.addAll(mapper(s, grid, copySet(set)));
                    }
                    set.clear();
                    set.addAll(newSet);
                    break;
                case ')':
                    return set;
                case '|':
                    return set;
                case '$':
                    return set;
            }
        }
    }

    public static Set<Point> copySet(Set<Point> set) {
        Set<Point> newSet = new HashSet();
        for (Point p : set) {
            newSet.add(new Point(p));
        }
        return newSet;
    }

    public static class StringWalker {
        private String s;
        private int idx;

        public StringWalker(String s) {
            this.s = s;
            idx = 0;
        }

        public char nextChar() {
            return s.charAt(idx++);
        }

        public char prevChar() {
            return s.charAt(idx--);
        }

        public char peek() {
            return s.charAt(idx);
        }

        public char peekPrev() {
            return s.charAt(idx - 1);
        }
    }

    public static char[][] initGrid(int size) {
        char[][] grid = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = '#';
            }
        }
        return grid;
    }

    public static void pprint(char[][] grid) {
        pprint(grid, 0, grid.length, 0, grid[0].length);
    }

    public static void pprint(char[][] grid, int y0, int y1, int x0, int x1) {
        for (int i = Math.max(y0, 0); i < y1; i++) {
            for (int j = Math.max(x0, 0); j < x1; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }
}
