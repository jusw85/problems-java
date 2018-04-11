package jw.problems.codingame.medium;

import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

/**
 * https://www.codingame.com/ide/puzzle/bender-episode-1
 *
 * The Goal
 *
 * Bender is a depressed robot who heals his depression by partying and drinking alcohol. To save him from a life of debauchery, his creators have reprogrammed the control system with a more rudimentary intelligence. Unfortunately, he has lost his sense of humor and his former friends have now rejected him.
 *
 * Bender is now all alone and is wandering through the streets of Futurama with the intention of ending it all in a suicide booth.
 *
 * To intercept him and save him from almost certain death, the authorities have given you a mission: write a program that will make it possible to foresee the path that Bender follows. To do so, you are given the logic for the new intelligence with which Bender has been programmed as well as a map of the city.
 * Rules
 *
 * The 9 rules of the new Bender system:
 *
 * Bender starts from the place indicated by the @ symbol on the map and heads SOUTH.
 * Bender finishes his journey and dies when he reaches the suicide booth marked $.
 * Obstacles that Bender may encounter are represented by # or X.
 * When Bender encounters an obstacle, he changes direction using the following priorities: SOUTH, EAST, NORTH and WEST. So he first tries to go SOUTH, if he cannot, then he will go EAST, if he still cannot, then he will go NORTH, and finally if he still cannot, then he will go WEST.
 * Along the way, Bender may come across path modifiers that will instantaneously make him change direction. The S modifier will make him turn SOUTH from then on, E, to the EAST, N to the NORTH and W to the WEST.
 * The circuit inverters (I on map) produce a magnetic field which will reverse the direction priorities that Bender should choose when encountering an obstacle. Priorities will become WEST, NORTH, EAST, SOUTH. If Bender returns to an inverter I, then priorities are reset to their original state (SOUTH, EAST, NORTH, WEST).
 * Bender can also find a few beers along his path (B on the map) that will give him strength and put him in “Breaker” mode. Breaker mode allows Bender to destroy and automatically pass through the obstacles represented by the character X (only the obstacles X). When an obstacle is destroyed, it remains so permanently and Bender maintains his course of direction. If Bender is in Breaker mode and passes over a beer again, then he immediately goes out of Breaker mode. The beers remain in place after Bender has passed.
 * 2 teleporters T may be present in the city. If Bender passes over a teleporter, then he is automatically teleported to the position of the other teleporter and he retains his direction and Breaker mode properties.
 * Finally, the space characters are blank areas on the map (no special behavior other than those specified above).
 *
 * Your program must display the sequence of moves taken by Bender according to the map provided as input.
 *
 * The map is divided into lines (L) and columns (C). The contours of the map are always unbreakable # obstacles. The map always has a starting point @ and a suicide booth $.
 *
 * If Bender cannot reach the suicide booth because he is indefinitely looping, then your program must only display LOOP.
 * Example
 *
 * Let the map below:
 * ######
 * #@E $#
 * # N  #
 * #X   #
 * ######
 *
 * In this example, Bender will follow this sequence of moves:
 *
 * SOUTH (initial direction)
 * EAST (because of the obstacle X)
 * NORTH (change of direction caused by N)
 * EAST (change of direction caused by E)
 * EAST (current direction, until end point $)
 *
 * Game Input
 * Input
 *
 * Line 1: the number of lines L and columns C on the map, separated by a space.
 *
 * The following L lines: a line of the length C representing a line on the map. A line can contain the characters #, X, @, $, S, E, N, W, B, I, T and space character.
 * Output
 *
 * If Bender can reach $, then display the sequence of moves he has taken. One move per line: SOUTH for the South, EAST for the East, NORTH for the North and WEST for the west.
 * If Bender cannot reach $, then only display LOOP.
 *
 * Constraints
 * 4 ≤ C ≤ 100
 * 4 ≤ L ≤ 100
 */
public class Bender1 {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        int L = in.nextInt();
        int C = in.nextInt();
        if (in.hasNextLine()) {
            in.nextLine();
        }

        char[][] cs = new char[L][];
        for (int i = 0; i < L; i++) {
            cs[i] = in.nextLine().toCharArray();
        }
        Maze maze = new Maze(cs);

        Bender b = new Bender();
        System.out.println(b.walk(maze));
    }

    public static class Bender {
        private Point p;
        private Direction d = Direction.S;
        private boolean isBreaker = false;
        private boolean isInverted = false;
        private Direction[] dirPriorities = PRI;
        private Set<BenderState> visited = new HashSet<>();
        private Queue<String> moves = new LinkedList<>();

        public static final Direction[] PRI = {Direction.S, Direction.E, Direction.N, Direction.W};
        public static final Direction[] RPRI = {Direction.W, Direction.N, Direction.E, Direction.S};

        public String walk(Maze maze) {
            p = maze.getStart();
            boolean found = false;
            while (!found) {
                BenderState bs = new BenderState();
                if (visited.contains(bs)) {
                    break;
                }
                visited.add(new BenderState());
                Point n = new Point(p.x + d.x, p.y + d.y);
                char c = maze.getTile(n);
                boolean moved = true;
                switch (c) {
                    case '$':
                        found = true;
                        break;
                    case '#':
                        for (Direction dir : dirPriorities) {
                            if (maze.isWalkable(p.y + dir.y, p.x + dir.x, isBreaker)) {
                                d = dir;
                                break;
                            }
                        }
                        moved = false;
                        break;
                    case 'X': // block
                        if (isBreaker) {
                            maze.breakX(n);
                            visited.clear();
                        } else {
                            for (Direction dir : dirPriorities) {
                                if (maze.isWalkable(p.y + dir.y, p.x + dir.x, isBreaker)) {
                                    d = dir;
                                    break;
                                }
                            }
                            moved = false;
                        }
                        break;
                    case 'I': // inverter
                        isInverted = !isInverted;
                        if (isInverted)
                            dirPriorities = RPRI;
                        else
                            dirPriorities = PRI;
                        p = n;
                        break;
                    case 'T': // teleport
                        n = maze.teleport(n);
                        break;
                    case 'B': // breaker
                        isBreaker = !isBreaker;
                        break;
                    default:
                        break;
                }
                if (moved) {
                    p = n;
                    moves.add(d.toString());
                }
                switch (c) {
                    case 'S':
                        d = Direction.S;
                        break;
                    case 'E':
                        d = Direction.E;
                        break;
                    case 'N':
                        d = Direction.N;
                        break;
                    case 'W':
                        d = Direction.W;
                        break;
                    default:
                        break;
                }
            }
            if (!found)
                return "LOOP";
            StringBuilder sb = new StringBuilder();
            for (String move : moves) {
                sb.append(move).append(System.lineSeparator());
            }
            return sb.toString();
        }

        private class BenderState {
            private Point p;
            private Direction d;
            private boolean isBreaker;
            private boolean isInverted;

            public BenderState() {
                this.p = Bender.this.p;
                this.d = Bender.this.d;
                this.isBreaker = Bender.this.isBreaker;
                this.isInverted = Bender.this.isInverted;
            }

            public boolean equals(Object o) {
                BenderState that = (BenderState) o;
                return isBreaker == that.isBreaker &&
                        isInverted == that.isInverted &&
                        Objects.equals(p, that.p) &&
                        d == that.d;
            }

            public int hashCode() {
                return Objects.hash(p, d, isBreaker, isInverted);
            }
        }
    }

    public static class Maze {
        private char[][] maze;
        private Point start = new Point();
        private Point t1 = null;
        private Point t2 = null;

        public Maze(char[][] maze) {
            this.maze = maze;

            for (int y = 0; y < maze.length; y++) {
                for (int x = 0; x < maze[y].length; x++) {
                    if (maze[y][x] == '@') {
                        start = new Point(x, y);
                    } else if (maze[y][x] == 'T') {
                        Point t = new Point(x, y);
                        if (t1 == null) {
                            t1 = t;
                        } else {
                            t2 = t;
                        }
                    }
                }
            }
        }

        public void breakX(Point p) {
            breakX(p.y, p.x);
        }

        public void breakX(int y, int x) {
            if (maze[y][x] == 'X')
                maze[y][x] = ' ';
        }

        public Point teleport(Point p) {
            return teleport(p.y, p.x);
        }

        public Point teleport(int y, int x) {
            if (t1.y == y && t1.x == x) {
                return new Point(t2);
            }
            return new Point(t1);
        }

        public boolean isWalkable(Point p, boolean isBreaker) {
            return isWalkable(p.y, p.x, isBreaker);
        }

        public boolean isWalkable(int y, int x, boolean isBreaker) {
            return (!(maze[y][x] == '#' || (!isBreaker && maze[y][x] == 'X')));
        }

        public char getTile(Point p) {
            return getTile(p.y, p.x);
        }

        public char getTile(int y, int x) {
            return maze[y][x];
        }

        public Point getStart() {
            return new Point(start);
        }

        public void pprint(int y, int x) {
            for (int i = 0; i < maze.length; i++) {
                for (int j = 0; j < maze[i].length; j++) {
                    if (i == y && j == x) {
                        System.out.print('@');
                    } else {
                        System.out.print(maze[i][j]);
                    }
                }
                System.out.println();
            }
        }
    }

    public enum Direction {
        S(1, 0, "SOUTH"),
        E(0, 1, "EAST"),
        N(-1, 0, "NORTH"),
        W(0, -1, "WEST");

        public final int y;
        public final int x;
        private final String name;

        Direction(int y, int x, String name) {
            this.y = y;
            this.x = x;
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

}
