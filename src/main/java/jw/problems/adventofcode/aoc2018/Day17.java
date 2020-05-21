package jw.problems.adventofcode.aoc2018;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://adventofcode.com/2018/day/17
 *
 * --- Day 17: Reservoir Research ---
 *
 * You arrive in the year 18. If it weren't for the coat you got in 1018, you would be very cold: the North Pole base hasn't even been constructed.
 *
 * Rather, it hasn't been constructed yet. The Elves are making a little progress, but there's not a lot of liquid water in this climate, so they're getting very dehydrated. Maybe there's more underground?
 *
 * You scan a two-dimensional vertical slice of the ground nearby and discover that it is mostly sand with veins of clay. The scan only provides data with a granularity of square meters, but it should be good enough to determine how much water is trapped there. In the scan, x represents the distance to the right, and y represents the distance down. There is also a spring of water near the surface at x=500, y=0. The scan identifies which square meters are clay (your puzzle input).
 *
 * For example, suppose your scan shows the following veins of clay:
 *
 * x=495, y=2..7
 * y=7, x=495..501
 * x=501, y=3..7
 * x=498, y=2..4
 * x=506, y=1..2
 * x=498, y=10..13
 * x=504, y=10..13
 * y=13, x=498..504
 *
 * Rendering clay as #, sand as ., and the water spring as +, and with x increasing to the right and y increasing downward, this becomes:
 *
 * 44444455555555
 * 99999900000000
 * 45678901234567
 * 0 ......+.......
 * 1 ............#.
 * 2 .#..#.......#.
 * 3 .#..#..#......
 * 4 .#..#..#......
 * 5 .#.....#......
 * 6 .#.....#......
 * 7 .#######......
 * 8 ..............
 * 9 ..............
 * 10 ....#.....#...
 * 11 ....#.....#...
 * 12 ....#.....#...
 * 13 ....#######...
 *
 * The spring of water will produce water forever. Water can move through sand, but is blocked by clay. Water always moves down when possible, and spreads to the left and right otherwise, filling space that has clay on both sides and falling out otherwise.
 *
 * For example, if five squares of water are created, they will flow downward until they reach the clay and settle there. Water that has come to rest is shown here as ~, while sand through which water has passed (but which is now dry again) is shown as |:
 *
 * ......+.......
 * ......|.....#.
 * .#..#.|.....#.
 * .#..#.|#......
 * .#..#.|#......
 * .#....|#......
 * .#~~~~~#......
 * .#######......
 * ..............
 * ..............
 * ....#.....#...
 * ....#.....#...
 * ....#.....#...
 * ....#######...
 *
 * Two squares of water can't occupy the same location. If another five squares of water are created, they will settle on the first five, filling the clay reservoir a little more:
 *
 * ......+.......
 * ......|.....#.
 * .#..#.|.....#.
 * .#..#.|#......
 * .#..#.|#......
 * .#~~~~~#......
 * .#~~~~~#......
 * .#######......
 * ..............
 * ..............
 * ....#.....#...
 * ....#.....#...
 * ....#.....#...
 * ....#######...
 *
 * Water pressure does not apply in this scenario. If another four squares of water are created, they will stay on the right side of the barrier, and no water will reach the left side:
 *
 * ......+.......
 * ......|.....#.
 * .#..#.|.....#.
 * .#..#~~#......
 * .#..#~~#......
 * .#~~~~~#......
 * .#~~~~~#......
 * .#######......
 * ..............
 * ..............
 * ....#.....#...
 * ....#.....#...
 * ....#.....#...
 * ....#######...
 *
 * At this point, the top reservoir overflows. While water can reach the tiles above the surface of the water, it cannot settle there, and so the next five squares of water settle like this:
 *
 * ......+.......
 * ......|.....#.
 * .#..#||||...#.
 * .#..#~~#|.....
 * .#..#~~#|.....
 * .#~~~~~#|.....
 * .#~~~~~#|.....
 * .#######|.....
 * ........|.....
 * ........|.....
 * ....#...|.#...
 * ....#...|.#...
 * ....#~~~~~#...
 * ....#######...
 *
 * Note especially the leftmost |: the new squares of water can reach this tile, but cannot stop there. Instead, eventually, they all fall to the right and settle in the reservoir below.
 *
 * After 10 more squares of water, the bottom reservoir is also full:
 *
 * ......+.......
 * ......|.....#.
 * .#..#||||...#.
 * .#..#~~#|.....
 * .#..#~~#|.....
 * .#~~~~~#|.....
 * .#~~~~~#|.....
 * .#######|.....
 * ........|.....
 * ........|.....
 * ....#~~~~~#...
 * ....#~~~~~#...
 * ....#~~~~~#...
 * ....#######...
 *
 * Finally, while there is nowhere left for the water to settle, it can reach a few more tiles before overflowing beyond the bottom of the scanned data:
 *
 * ......+.......    (line not counted: above minimum y value)
 * ......|.....#.
 * .#..#||||...#.
 * .#..#~~#|.....
 * .#..#~~#|.....
 * .#~~~~~#|.....
 * .#~~~~~#|.....
 * .#######|.....
 * ........|.....
 * ...|||||||||..
 * ...|#~~~~~#|..
 * ...|#~~~~~#|..
 * ...|#~~~~~#|..
 * ...|#######|..
 * ...|.......|..    (line not counted: below maximum y value)
 * ...|.......|..    (line not counted: below maximum y value)
 * ...|.......|..    (line not counted: below maximum y value)
 *
 * How many tiles can be reached by the water? To prevent counting forever, ignore tiles with a y coordinate smaller than the smallest y coordinate in your scan data or larger than the largest one. Any x coordinate is valid. In this example, the lowest y coordinate given is 1, and the highest is 13, causing the water spring (in row 0) and the water falling off the bottom of the render (in rows 14 through infinity) to be ignored.
 *
 * So, in the example above, counting both water at rest (~) and other sand tiles the water can hypothetically reach (|), the total number of tiles the water can reach is 57.
 *
 * How many tiles can the water reach within the range of y values in your scan?
 *
 * Your puzzle answer was 31949.
 * --- Part Two ---
 *
 * After a very long time, the water spring will run dry. How much water will be retained?
 *
 * In the example above, water that won't eventually drain out is shown as ~, a total of 29 tiles.
 *
 * How many water tiles are left after the water spring stops producing water and all remaining water not at rest has drained?
 *
 * Your puzzle answer was 26384.
 */
public class Day17 {

    public static Pattern p = Pattern.compile("([yx])=(\\d+), ([yx])=(\\d+)\\.\\.(\\d+)");

    public static void main(String[] args) throws IOException {
        char[][] grid = initGrid(2000, 800);
        Bounds b = fileToGrid("./etc/aoc2018/in17", grid);
        int minX = b.tl.x;
        int minY = b.tl.y;
        int maxX = b.br.x;
        int maxY = b.br.y;
        boolean isChanged = true;
        grid[1][500] = '|';
        while (isChanged) {
            isChanged = false;
            for (int i = 0; i < maxY; i++) {
                for (int j = minX - 2; j < maxX + 2; j++) {
                    if (grid[i][j] == '|') {
                        if (grid[i + 1][j] == '.') {
                            grid[i + 1][j] = '|';
                            isChanged = true;
                        } else if (grid[i + 1][j] == '~' || grid[i + 1][j] == '#') {
                            if (grid[i][j - 1] == '.') {
                                grid[i][j - 1] = '|';
                                isChanged = true;
                            }
                            if (grid[i][j + 1] == '.') {
                                grid[i][j + 1] = '|';
                                isChanged = true;
                            }
                        }
                    } else if (grid[i][j] == '#' && grid[i][j + 1] == '|') {
                        boolean restWater = true;
                        int k;
                        for (k = j + 1; grid[i][k] != '#'; k++) {
                            if (!(grid[i][k] == '|' && (grid[i + 1][k] == '#' || grid[i + 1][k] == '~'))) {
                                restWater = false;
                                break;
                            }
                        }
                        if (restWater) {
                            for (int l = j + 1; l < k; l++) {
                                grid[i][l] = '~';
                            }
                            isChanged = true;
                        }
                    }
                }
            }
        }
//        Bounds tmp = new Bounds();
//        tmp.tl = new Point(minX - 2, 0);
//        tmp.br = new Point(maxX + 2, maxY);
//        gridToFile(grid, "out.txt", tmp);

        int numMove = 0;
        int numRest = 0;
        for (int i = minY; i <= maxY; i++) {
            for (int j = 0; j <= maxX + 1; j++) {
                if (grid[i][j] == '|') {
                    numMove++;
                } else if (grid[i][j] == '~') {
                    numRest++;
                }
            }
        }
        System.out.println(numMove);
        System.out.println(numRest);
        System.out.println(numMove + numRest);
    }

    public static void gridToFile(char[][] grid, String filename, Bounds b) throws IOException {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"))) {
            for (int i = b.tl.y; i <= b.br.y; i++) {
                for (int j = b.tl.x; j <= b.br.x; j++) {
                    writer.write(grid[i][j]);
                }
                writer.write("\n");
            }

        }
    }

    public static void drawRegion(char[][] grid, char c, int y0, int y1, int x0, int x1) {
        for (int i = y0; i <= y1; i++) {
            for (int j = x0; j <= x1; j++) {
                grid[i][j] = c;
            }
        }
    }

    public static char[][] initGrid(int yMax, int xMax) {
        char[][] grid = new char[yMax][xMax];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = '.';
            }
        }
        grid[0][500] = '+';
        return grid;
    }

    public static class Bounds {
        public Point tl;
        public Point br;
    }

    public static Bounds fileToGrid(String inFile, char[][] grid) throws FileNotFoundException {
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        Scanner sc = new Scanner(new File(inFile));
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = p.matcher(line);
            m.matches();
            char yx = m.group(1).charAt(0);
            int i = Integer.parseInt(m.group(2));
            int j = Integer.parseInt(m.group(4));
            int k = Integer.parseInt(m.group(5));
            if (yx == 'x') {
                drawRegion(grid, '#', j, k, i, i);
                if (j < minY || k < minY) {
                    minY = Math.min(j, k);
                }
                if (j > maxY || k > maxY) {
                    maxY = Math.max(j, k);
                }
                if (i < minX) {
                    minX = i;
                }
                if (i > maxX) {
                    maxX = i;
                }
            } else {
                drawRegion(grid, '#', i, i, j, k);
                if (i < minY) {
                    minY = i;
                }
                if (i > maxY) {
                    maxY = i;
                }
                if (j < minX || k < minX) {
                    minX = Math.min(j, k);
                }
                if (j > maxX || k > maxX) {
                    maxX = Math.max(j, k);
                }
            }
        }
        sc.close();
        Bounds b = new Bounds();
        b.tl = new Point(minX, minY);
        b.br = new Point(maxX, maxY);
        return b;
    }

    public static void pprint(char[][] grid, int y, int x, int size) {
        pprint(grid, y - (size / 2), y + (size / 2),
                x - (size / 2), x + (size / 2));
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
