package jw.problems.adventofcode.aoc2018;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * https://adventofcode.com/2018/day/13
 *
 * --- Day 13: Mine Cart Madness ---
 *
 * A crop of this size requires significant logistics to transport produce, soil, fertilizer, and so on. The Elves are very busy pushing things around in carts on some kind of rudimentary system of tracks they've come up with.
 *
 * Seeing as how cart-and-track systems don't appear in recorded history for another 1000 years, the Elves seem to be making this up as they go along. They haven't even figured out how to avoid collisions yet.
 *
 * You map out the tracks (your puzzle input) and see where you can help.
 *
 * Tracks consist of straight paths (| and -), curves (/ and \), and intersections (+). Curves connect exactly two perpendicular pieces of track; for example, this is a closed loop:
 *
 * /----\
 * |    |
 * |    |
 * \----/
 *
 * Intersections occur when two perpendicular paths cross. At an intersection, a cart is capable of turning left, turning right, or continuing straight. Here are two loops connected by two intersections:
 *
 * /-----\
 * |     |
 * |  /--+--\
 * |  |  |  |
 * \--+--/  |
 * |     |
 * \-----/
 *
 * Several carts are also on the tracks. Carts always face either up (^), down (v), left (<), or right (>). (On your initial map, the track under each cart is a straight path matching the direction the cart is facing.)
 *
 * Each time a cart has the option to turn (by arriving at any intersection), it turns left the first time, goes straight the second time, turns right the third time, and then repeats those directions starting again with left the fourth time, straight the fifth time, and so on. This process is independent of the particular intersection at which the cart has arrived - that is, the cart has no per-intersection memory.
 *
 * Carts all move at the same speed; they take turns moving a single step at a time. They do this based on their current location: carts on the top row move first (acting from left to right), then carts on the second row move (again from left to right), then carts on the third row, and so on. Once each cart has moved one step, the process repeats; each of these loops is called a tick.
 *
 * For example, suppose there are two carts on a straight track:
 *
 * |  |  |  |  |
 * v  |  |  |  |
 * |  v  v  |  |
 * |  |  |  v  X
 * |  |  ^  ^  |
 * ^  ^  |  |  |
 * |  |  |  |  |
 *
 * First, the top cart moves. It is facing down (v), so it moves down one square. Second, the bottom cart moves. It is facing up (^), so it moves up one square. Because all carts have moved, the first tick ends. Then, the process repeats, starting with the first cart. The first cart moves down, then the second cart moves up - right into the first cart, colliding with it! (The location of the crash is marked with an X.) This ends the second and last tick.
 *
 * Here is a longer example:
 *
 * /->-\
 * |   |  /----\
 * | /-+--+-\  |
 * | | |  | v  |
 * \-+-/  \-+--/
 * \------/
 *
 * /-->\
 * |   |  /----\
 * | /-+--+-\  |
 * | | |  | |  |
 * \-+-/  \->--/
 * \------/
 *
 * /---v
 * |   |  /----\
 * | /-+--+-\  |
 * | | |  | |  |
 * \-+-/  \-+>-/
 * \------/
 *
 * /---\
 * |   v  /----\
 * | /-+--+-\  |
 * | | |  | |  |
 * \-+-/  \-+->/
 * \------/
 *
 * /---\
 * |   |  /----\
 * | /->--+-\  |
 * | | |  | |  |
 * \-+-/  \-+--^
 * \------/
 *
 * /---\
 * |   |  /----\
 * | /-+>-+-\  |
 * | | |  | |  ^
 * \-+-/  \-+--/
 * \------/
 *
 * /---\
 * |   |  /----\
 * | /-+->+-\  ^
 * | | |  | |  |
 * \-+-/  \-+--/
 * \------/
 *
 * /---\
 * |   |  /----<
 * | /-+-->-\  |
 * | | |  | |  |
 * \-+-/  \-+--/
 * \------/
 *
 * /---\
 * |   |  /---<\
 * | /-+--+>\  |
 * | | |  | |  |
 * \-+-/  \-+--/
 * \------/
 *
 * /---\
 * |   |  /--<-\
 * | /-+--+-v  |
 * | | |  | |  |
 * \-+-/  \-+--/
 * \------/
 *
 * /---\
 * |   |  /-<--\
 * | /-+--+-\  |
 * | | |  | v  |
 * \-+-/  \-+--/
 * \------/
 *
 * /---\
 * |   |  /<---\
 * | /-+--+-\  |
 * | | |  | |  |
 * \-+-/  \-<--/
 * \------/
 *
 * /---\
 * |   |  v----\
 * | /-+--+-\  |
 * | | |  | |  |
 * \-+-/  \<+--/
 * \------/
 *
 * /---\
 * |   |  /----\
 * | /-+--v-\  |
 * | | |  | |  |
 * \-+-/  ^-+--/
 * \------/
 *
 * /---\
 * |   |  /----\
 * | /-+--+-\  |
 * | | |  X |  |
 * \-+-/  \-+--/
 * \------/
 *
 * After following their respective paths for a while, the carts eventually crash. To help prevent crashes, you'd like to know the location of the first crash. Locations are given in X,Y coordinates, where the furthest left column is X=0 and the furthest top row is Y=0:
 *
 * 111
 * 0123456789012
 * 0/---\
 * 1|   |  /----\
 * 2| /-+--+-\  |
 * 3| | |  X |  |
 * 4\-+-/  \-+--/
 * 5  \------/
 *
 * In this example, the location of the first crash is 7,3.
 *
 * Your puzzle answer was 32,8.
 * --- Part Two ---
 *
 * There isn't much you can do to prevent crashes in this ridiculous system. However, by predicting the crashes, the Elves know where to be in advance and instantly remove the two crashing carts the moment any crash occurs.
 *
 * They can proceed like this for a while, but eventually, they're going to run out of carts. It could be useful to figure out where the last cart that hasn't crashed will end up.
 *
 * For example:
 *
 * />-<\
 * |   |
 * | /<+-\
 * | | | v
 * \>+</ |
 * |   ^
 * \<->/
 *
 * /---\
 * |   |
 * | v-+-\
 * | | | |
 * \-+-/ |
 * |   |
 * ^---^
 *
 * /---\
 * |   |
 * | /-+-\
 * | v | |
 * \-+-/ |
 * ^   ^
 * \---/
 *
 * /---\
 * |   |
 * | /-+-\
 * | | | |
 * \-+-/ ^
 * |   |
 * \---/
 *
 * After four very expensive crashes, a tick ends with only one cart remaining; its final location is 6,4.
 *
 * What is the location of the last cart at the end of the first tick where it is the only cart left?
 *
 * Your puzzle answer was 38,38.
 */
public class Day13 {

    /**
     * <  ^  >  v
     * abcdefghijkl
     */
    public static Map<Character, Character> map = new HashMap<>();

    static {
        map.put('a', 'k');
        map.put('b', 'c');
        map.put('c', 'd');

        map.put('d', 'b');
        map.put('e', 'f');
        map.put('f', 'g');

        map.put('g', 'e');
        map.put('h', 'i');
        map.put('i', 'j');

        map.put('j', 'h');
        map.put('k', 'l');
        map.put('l', 'a');
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2018/in13"));
        char[][] grid = new char[150][150];
        char[][] orig = new char[150][150];

        int numCars = 0;
        for (int i = 0; i < grid.length; i++) {
            String line = sc.nextLine();
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                orig[i][j] = c;
                grid[i][j] = c;
                if (c == '<') {
                    orig[i][j] = '-';
                    grid[i][j] = 'a';
                    numCars++;
                } else if (c == '^') {
                    orig[i][j] = '|';
                    grid[i][j] = 'd';
                    numCars++;
                } else if (c == '>') {
                    orig[i][j] = '-';
                    grid[i][j] = 'g';
                    numCars++;
                } else if (c == 'v') {
                    orig[i][j] = '|';
                    grid[i][j] = 'j';
                    numCars++;
                }
            }
        }
        sc.close();

        while (numCars > 1) {
            Set<Point> visited = new HashSet<>();
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    int cy = i;
                    int cx = j;
                    char c = grid[cy][cx];
                    boolean l = c >= 'a' && c <= 'c';
                    boolean u = c >= 'd' && c <= 'f';
                    boolean r = c >= 'g' && c <= 'i';
                    boolean d = c >= 'j' && c <= 'l';

                    if (l || u || r || d) {
                        if (visited.contains(new Point(cx, cy))) {
                            continue;
                        }
                        grid[cy][cx] = orig[cy][cx];
                        if (l) cx--;
                        else if (u) cy--;
                        else if (r) cx++;
                        else if (d) cy++;
                        if (d || r) visited.add(new Point(cx, cy));
                        char next = grid[cy][cx];
                        if (next == '\\') {
                            grid[cy][cx] = (l || r) ? turnCW(c) : turnCCW(c);
                        } else if (next == '/') {
                            grid[cy][cx] = (l || r) ? turnCCW(c) : turnCW(c);
                        } else if (next == '+') {
                            grid[cy][cx] = map.get(c);
                        } else if (next >= 'a' && next <= 'l') {
                            grid[cy][cx] = orig[cy][cx];
                            System.out.println("Collision at " + cx + "," + cy);
                            numCars -= 2;
                        } else if ((l || r) && next == '-') {
                            grid[cy][cx] = c;
                        } else if ((u || d) && next == '|') {
                            grid[cy][cx] = c;
                        } else {
                            assert false;
                        }
                    }
                }
            }
        }
        if (numCars == 1) {
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (grid[i][j] >= 'a' && grid[i][j] <= 'l') {
                        System.out.println(j + "," + i);
                    }
                }
            }
        }
    }

    public static char turnCW(char c) {
        char c2 = (char) (c + 3);
        if (c2 > 'l') {
            c2 = (char) ('a' + (c2 - 'l' - 1));
        }
        return c2;
    }

    public static char turnCCW(char c) {
        char c2 = (char) (c - 3);
        if (c2 < 'a') {
            c2 = (char) ('l' - ('a' - c2 - 1));
        }
        return c2;
    }

    public static void pprint(char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }
}
