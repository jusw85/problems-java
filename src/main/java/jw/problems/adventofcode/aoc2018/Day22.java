package jw.problems.adventofcode.aoc2018;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;

/**
 * https://adventofcode.com/2018/day/22
 *
 * --- Day 22: Mode Maze ---
 *
 * This is it, your final stop: the year -483. It's snowing and dark outside; the only light you can see is coming from a small cottage in the distance. You make your way there and knock on the door.
 *
 * A portly man with a large, white beard answers the door and invites you inside. For someone living near the North Pole in -483, he must not get many visitors, but he doesn't act surprised to see you. Instead, he offers you some milk and cookies.
 *
 * After talking for a while, he asks a favor of you. His friend hasn't come back in a few hours, and he's not sure where he is. Scanning the region briefly, you discover one life signal in a cave system nearby; his friend must have taken shelter there. The man asks if you can go there to retrieve his friend.
 *
 * The cave is divided into square regions which are either dominantly rocky, narrow, or wet (called its type). Each region occupies exactly one coordinate in X,Y format where X and Y are integers and zero or greater. (Adjacent regions can be the same type.)
 *
 * The scan (your puzzle input) is not very detailed: it only reveals the depth of the cave system and the coordinates of the target. However, it does not reveal the type of each region. The mouth of the cave is at 0,0.
 *
 * The man explains that due to the unusual geology in the area, there is a method to determine any region's type based on its erosion level. The erosion level of a region can be determined from its geologic index. The geologic index can be determined using the first rule that applies from the list below:
 *
 * The region at 0,0 (the mouth of the cave) has a geologic index of 0.
 * The region at the coordinates of the target has a geologic index of 0.
 * If the region's Y coordinate is 0, the geologic index is its X coordinate times 16807.
 * If the region's X coordinate is 0, the geologic index is its Y coordinate times 48271.
 * Otherwise, the region's geologic index is the result of multiplying the erosion levels of the regions at X-1,Y and X,Y-1.
 *
 * A region's erosion level is its geologic index plus the cave system's depth, all modulo 20183. Then:
 *
 * If the erosion level modulo 3 is 0, the region's type is rocky.
 * If the erosion level modulo 3 is 1, the region's type is wet.
 * If the erosion level modulo 3 is 2, the region's type is narrow.
 *
 * For example, suppose the cave system's depth is 510 and the target's coordinates are 10,10. Using % to represent the modulo operator, the cavern would look as follows:
 *
 * At 0,0, the geologic index is 0. The erosion level is (0 + 510) % 20183 = 510. The type is 510 % 3 = 0, rocky.
 * At 1,0, because the Y coordinate is 0, the geologic index is 1 * 16807 = 16807. The erosion level is (16807 + 510) % 20183 = 17317. The type is 17317 % 3 = 1, wet.
 * At 0,1, because the X coordinate is 0, the geologic index is 1 * 48271 = 48271. The erosion level is (48271 + 510) % 20183 = 8415. The type is 8415 % 3 = 0, rocky.
 * At 1,1, neither coordinate is 0 and it is not the coordinate of the target, so the geologic index is the erosion level of 0,1 (8415) times the erosion level of 1,0 (17317), 8415 * 17317 = 145722555. The erosion level is (145722555 + 510) % 20183 = 1805. The type is 1805 % 3 = 2, narrow.
 * At 10,10, because they are the target's coordinates, the geologic index is 0. The erosion level is (0 + 510) % 20183 = 510. The type is 510 % 3 = 0, rocky.
 *
 * Drawing this same cave system with rocky as ., wet as =, narrow as |, the mouth as M, the target as T, with 0,0 in the top-left corner, X increasing to the right, and Y increasing downward, the top-left corner of the map looks like this:
 *
 * M=.|=.|.|=.|=|=.
 * .|=|=|||..|.=...
 * .==|....||=..|==
 * =.|....|.==.|==.
 * =|..==...=.|==..
 * =||.=.=||=|=..|=
 * |.=.===|||..=..|
 * |..==||=.|==|===
 * .=..===..=|.|||.
 * .======|||=|=.|=
 * .===|=|===T===||
 * =|||...|==..|=.|
 * =.=|=.=..=.||==|
 * ||=|=...|==.=|==
 * |=.=||===.|||===
 * ||.|==.|.|.||=||
 *
 * Before you go in, you should determine the risk level of the area. For the rectangle that has a top-left corner of region 0,0 and a bottom-right corner of the region containing the target, add up the risk level of each individual region: 0 for rocky regions, 1 for wet regions, and 2 for narrow regions.
 *
 * In the cave system above, because the mouth is at 0,0 and the target is at 10,10, adding up the risk level of all regions with an X coordinate from 0 to 10 and a Y coordinate from 0 to 10, this total is 114.
 *
 * What is the total risk level for the smallest rectangle that includes 0,0 and the target's coordinates?
 *
 * Your puzzle answer was 8681.
 * --- Part Two ---
 *
 * Okay, it's time to go rescue the man's friend.
 *
 * As you leave, he hands you some tools: a torch and some climbing gear. You can't equip both tools at once, but you can choose to use neither.
 *
 * Tools can only be used in certain regions:
 *
 * In rocky regions, you can use the climbing gear or the torch. You cannot use neither (you'll likely slip and fall).
 * In wet regions, you can use the climbing gear or neither tool. You cannot use the torch (if it gets wet, you won't have a light source).
 * In narrow regions, you can use the torch or neither tool. You cannot use the climbing gear (it's too bulky to fit).
 *
 * You start at 0,0 (the mouth of the cave) with the torch equipped and must reach the target coordinates as quickly as possible. The regions with negative X or Y are solid rock and cannot be traversed. The fastest route might involve entering regions beyond the X or Y coordinate of the target.
 *
 * You can move to an adjacent region (up, down, left, or right; never diagonally) if your currently equipped tool allows you to enter that region. Moving to an adjacent region takes one minute. (For example, if you have the torch equipped, you can move between rocky and narrow regions, but cannot enter wet regions.)
 *
 * You can change your currently equipped tool or put both away if your new equipment would be valid for your current region. Switching to using the climbing gear, torch, or neither always takes seven minutes, regardless of which tools you start with. (For example, if you are in a rocky region, you can switch from the torch to the climbing gear, but you cannot switch to neither.)
 *
 * Finally, once you reach the target, you need the torch equipped before you can find him in the dark. The target is always in a rocky region, so if you arrive there with climbing gear equipped, you will need to spend seven minutes switching to your torch.
 *
 * For example, using the same cave system as above, starting in the top left corner (0,0) and moving to the bottom right corner (the target, 10,10) as quickly as possible, one possible route is as follows, with your current position marked X:
 *
 * Initially:
 * X=.|=.|.|=.|=|=.
 * .|=|=|||..|.=...
 * .==|....||=..|==
 * =.|....|.==.|==.
 * =|..==...=.|==..
 * =||.=.=||=|=..|=
 * |.=.===|||..=..|
 * |..==||=.|==|===
 * .=..===..=|.|||.
 * .======|||=|=.|=
 * .===|=|===T===||
 * =|||...|==..|=.|
 * =.=|=.=..=.||==|
 * ||=|=...|==.=|==
 * |=.=||===.|||===
 * ||.|==.|.|.||=||
 *
 * Down:
 * M=.|=.|.|=.|=|=.
 * X|=|=|||..|.=...
 * .==|....||=..|==
 * =.|....|.==.|==.
 * =|..==...=.|==..
 * =||.=.=||=|=..|=
 * |.=.===|||..=..|
 * |..==||=.|==|===
 * .=..===..=|.|||.
 * .======|||=|=.|=
 * .===|=|===T===||
 * =|||...|==..|=.|
 * =.=|=.=..=.||==|
 * ||=|=...|==.=|==
 * |=.=||===.|||===
 * ||.|==.|.|.||=||
 *
 * Right:
 * M=.|=.|.|=.|=|=.
 * .X=|=|||..|.=...
 * .==|....||=..|==
 * =.|....|.==.|==.
 * =|..==...=.|==..
 * =||.=.=||=|=..|=
 * |.=.===|||..=..|
 * |..==||=.|==|===
 * .=..===..=|.|||.
 * .======|||=|=.|=
 * .===|=|===T===||
 * =|||...|==..|=.|
 * =.=|=.=..=.||==|
 * ||=|=...|==.=|==
 * |=.=||===.|||===
 * ||.|==.|.|.||=||
 *
 * Switch from using the torch to neither tool:
 * M=.|=.|.|=.|=|=.
 * .X=|=|||..|.=...
 * .==|....||=..|==
 * =.|....|.==.|==.
 * =|..==...=.|==..
 * =||.=.=||=|=..|=
 * |.=.===|||..=..|
 * |..==||=.|==|===
 * .=..===..=|.|||.
 * .======|||=|=.|=
 * .===|=|===T===||
 * =|||...|==..|=.|
 * =.=|=.=..=.||==|
 * ||=|=...|==.=|==
 * |=.=||===.|||===
 * ||.|==.|.|.||=||
 *
 * Right 3:
 * M=.|=.|.|=.|=|=.
 * .|=|X|||..|.=...
 * .==|....||=..|==
 * =.|....|.==.|==.
 * =|..==...=.|==..
 * =||.=.=||=|=..|=
 * |.=.===|||..=..|
 * |..==||=.|==|===
 * .=..===..=|.|||.
 * .======|||=|=.|=
 * .===|=|===T===||
 * =|||...|==..|=.|
 * =.=|=.=..=.||==|
 * ||=|=...|==.=|==
 * |=.=||===.|||===
 * ||.|==.|.|.||=||
 *
 * Switch from using neither tool to the climbing gear:
 * M=.|=.|.|=.|=|=.
 * .|=|X|||..|.=...
 * .==|....||=..|==
 * =.|....|.==.|==.
 * =|..==...=.|==..
 * =||.=.=||=|=..|=
 * |.=.===|||..=..|
 * |..==||=.|==|===
 * .=..===..=|.|||.
 * .======|||=|=.|=
 * .===|=|===T===||
 * =|||...|==..|=.|
 * =.=|=.=..=.||==|
 * ||=|=...|==.=|==
 * |=.=||===.|||===
 * ||.|==.|.|.||=||
 *
 * Down 7:
 * M=.|=.|.|=.|=|=.
 * .|=|=|||..|.=...
 * .==|....||=..|==
 * =.|....|.==.|==.
 * =|..==...=.|==..
 * =||.=.=||=|=..|=
 * |.=.===|||..=..|
 * |..==||=.|==|===
 * .=..X==..=|.|||.
 * .======|||=|=.|=
 * .===|=|===T===||
 * =|||...|==..|=.|
 * =.=|=.=..=.||==|
 * ||=|=...|==.=|==
 * |=.=||===.|||===
 * ||.|==.|.|.||=||
 *
 * Right:
 * M=.|=.|.|=.|=|=.
 * .|=|=|||..|.=...
 * .==|....||=..|==
 * =.|....|.==.|==.
 * =|..==...=.|==..
 * =||.=.=||=|=..|=
 * |.=.===|||..=..|
 * |..==||=.|==|===
 * .=..=X=..=|.|||.
 * .======|||=|=.|=
 * .===|=|===T===||
 * =|||...|==..|=.|
 * =.=|=.=..=.||==|
 * ||=|=...|==.=|==
 * |=.=||===.|||===
 * ||.|==.|.|.||=||
 *
 * Down 3:
 * M=.|=.|.|=.|=|=.
 * .|=|=|||..|.=...
 * .==|....||=..|==
 * =.|....|.==.|==.
 * =|..==...=.|==..
 * =||.=.=||=|=..|=
 * |.=.===|||..=..|
 * |..==||=.|==|===
 * .=..===..=|.|||.
 * .======|||=|=.|=
 * .===|=|===T===||
 * =|||.X.|==..|=.|
 * =.=|=.=..=.||==|
 * ||=|=...|==.=|==
 * |=.=||===.|||===
 * ||.|==.|.|.||=||
 *
 * Right:
 * M=.|=.|.|=.|=|=.
 * .|=|=|||..|.=...
 * .==|....||=..|==
 * =.|....|.==.|==.
 * =|..==...=.|==..
 * =||.=.=||=|=..|=
 * |.=.===|||..=..|
 * |..==||=.|==|===
 * .=..===..=|.|||.
 * .======|||=|=.|=
 * .===|=|===T===||
 * =|||..X|==..|=.|
 * =.=|=.=..=.||==|
 * ||=|=...|==.=|==
 * |=.=||===.|||===
 * ||.|==.|.|.||=||
 *
 * Down:
 * M=.|=.|.|=.|=|=.
 * .|=|=|||..|.=...
 * .==|....||=..|==
 * =.|....|.==.|==.
 * =|..==...=.|==..
 * =||.=.=||=|=..|=
 * |.=.===|||..=..|
 * |..==||=.|==|===
 * .=..===..=|.|||.
 * .======|||=|=.|=
 * .===|=|===T===||
 * =|||...|==..|=.|
 * =.=|=.X..=.||==|
 * ||=|=...|==.=|==
 * |=.=||===.|||===
 * ||.|==.|.|.||=||
 *
 * Right 4:
 * M=.|=.|.|=.|=|=.
 * .|=|=|||..|.=...
 * .==|....||=..|==
 * =.|....|.==.|==.
 * =|..==...=.|==..
 * =||.=.=||=|=..|=
 * |.=.===|||..=..|
 * |..==||=.|==|===
 * .=..===..=|.|||.
 * .======|||=|=.|=
 * .===|=|===T===||
 * =|||...|==..|=.|
 * =.=|=.=..=X||==|
 * ||=|=...|==.=|==
 * |=.=||===.|||===
 * ||.|==.|.|.||=||
 *
 * Up 2:
 * M=.|=.|.|=.|=|=.
 * .|=|=|||..|.=...
 * .==|....||=..|==
 * =.|....|.==.|==.
 * =|..==...=.|==..
 * =||.=.=||=|=..|=
 * |.=.===|||..=..|
 * |..==||=.|==|===
 * .=..===..=|.|||.
 * .======|||=|=.|=
 * .===|=|===X===||
 * =|||...|==..|=.|
 * =.=|=.=..=.||==|
 * ||=|=...|==.=|==
 * |=.=||===.|||===
 * ||.|==.|.|.||=||
 *
 * Switch from using the climbing gear to the torch:
 * M=.|=.|.|=.|=|=.
 * .|=|=|||..|.=...
 * .==|....||=..|==
 * =.|....|.==.|==.
 * =|..==...=.|==..
 * =||.=.=||=|=..|=
 * |.=.===|||..=..|
 * |..==||=.|==|===
 * .=..===..=|.|||.
 * .======|||=|=.|=
 * .===|=|===X===||
 * =|||...|==..|=.|
 * =.=|=.=..=.||==|
 * ||=|=...|==.=|==
 * |=.=||===.|||===
 * ||.|==.|.|.||=||
 *
 * This is tied with other routes as the fastest way to reach the target: 45 minutes. In it, 21 minutes are spent switching tools (three times, seven minutes each) and the remaining 24 minutes are spent moving.
 *
 * What is the fewest number of minutes you can take to reach the target?
 *
 * Your puzzle answer was 1070.
 */
public class Day22 {

    public static void main(String[] args) {
        Cave cave = new Cave(5616, new Point(10, 785));
        System.out.println(cave.calcRiskLevel());
        System.out.println(cave.calcTargetMinTime());
    }

    public enum Tool {
        TORCH,
        GEAR,
        NIL,
    }

    public static class State {
        public Tool tool;
        public int time;
        public Point pos;

        public State(Tool tool, int time, Point pos) {
            this.tool = tool;
            this.time = time;
            this.pos = pos;
        }
    }

    public static class Cave {
        private int depth;
        private Point target;
        private int[][] grid;
        private char[][] gridC;
        private int[][][] gridT;

        public Cave(int depth, Point target) {
            this.depth = depth;
            this.target = target;
            grid = new int[target.y * 2][target.x * 5];
            gridC = new char[grid.length][grid[0].length];
            gridT = new int[grid.length][grid[0].length][3];

            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    grid[i][j] = -1;
                }
            }
            for (int i = 0; i < gridT.length; i++) {
                for (int j = 0; j < gridT[i].length; j++) {
                    for (int k = 0; k < gridT[i][j].length; k++) {
                        gridT[i][j][k] = Integer.MAX_VALUE;
                    }
                }
            }

            fillCharGrid();
        }

        public int calcTargetMinTime() {
            State s = new State(Tool.TORCH, 0, new Point(0, 0));
            return bfs(s);
        }

        private Point[] dirs = {new Point(0, 1), new Point(0, -1), new Point(1, 0), new Point(-1, 0)};

        private int bfs(State initialState) {
            State s = initialState;
            gridT[s.pos.y][s.pos.x][0] = s.time;
            Queue<State> toVisit = new LinkedList<>();
            toVisit.add(s);

            while (!toVisit.isEmpty()) {
                s = toVisit.poll();
                int changeToolTime = s.time + 7;
                for (Tool tool : Tool.values()) {
                    if (s.tool != tool &&
                            canSwitchTool(s, tool) &&
                            changeToolTime < gridT[s.pos.y][s.pos.x][tool.ordinal()]) {
                        toVisit.add(new State(tool, changeToolTime, new Point(s.pos)));
                    }
                }

                int moveTime = s.time + 1;
                for (Point p : dirs) {
                    Point np = new Point(s.pos.x + p.x, s.pos.y + p.y);
                    if (canMove(s, np) &&
                            (moveTime < gridT[np.y][np.x][s.tool.ordinal()])) {
                        gridT[np.y][np.x][s.tool.ordinal()] = moveTime;
                        toVisit.add(new State(s.tool, moveTime, np));
                    }
                }
            }
            return gridT[target.y][target.x][Tool.TORCH.ordinal()];
        }

        private boolean isLegal(char c, Tool tool) {
            return (c == '.' && (tool == Tool.GEAR || tool == Tool.TORCH)) ||
                    (c == '=' && (tool == Tool.GEAR || tool == Tool.NIL)) ||
                    (c == '|' && (tool == Tool.NIL || tool == Tool.TORCH));
        }

        private boolean canSwitchTool(State s, Tool tool) {
            return isLegal(gridC[s.pos.y][s.pos.x], tool);
        }

        private boolean canMove(State s, Point newPoint) {
            if (newPoint.y < 0 || newPoint.x < 0 ||
                    newPoint.y >= gridC.length || newPoint.x >= gridC[0].length) {
                return false;
            }
            return isLegal(gridC[newPoint.y][newPoint.x], s.tool);
        }

        private void fillCharGrid() {
            for (int i = 0; i < gridC.length; i++) {
                for (int j = 0; j < gridC[i].length; j++) {
                    char c = ' ';
                    switch (getErosionLevel(i, j) % 3) {
                        case 0:
                            c = '.';
                            break;
                        case 1:
                            c = '=';
                            break;
                        case 2:
                            c = '|';
                            break;
                    }
                    gridC[i][j] = c;
                }
            }
        }

        public int getErosionLevel(int y, int x) {
            if (grid[y][x] >= 0) {
                return grid[y][x];
            }
            long geologicIndex;
            if ((y == 0 && x == 0) || (y == target.y && x == target.x)) {
                geologicIndex = 0;
            } else if (y == 0) {
                geologicIndex = x * 16807;
            } else if (x == 0) {
                geologicIndex = y * 48271;
            } else {
                geologicIndex = getErosionLevel(y, x - 1) * getErosionLevel(y - 1, x);
            }
            int el = (int) ((geologicIndex + depth) % 20183);
            grid[y][x] = el;
            return el;
        }

        public int calcRiskLevel() {
            int risk = 0;
            for (int i = 0; i <= target.y; i++) {
                for (int j = 0; j <= target.x; j++) {
                    switch (gridC[i][j]) {
                        case '.':
                            break;
                        case '=':
                            risk += 1;
                            break;
                        case '|':
                            risk += 2;
                            break;
                    }
                }
            }
            return risk;
        }

        public void pprint() {
            for (int i = 0; i < gridC.length; i++) {
                for (int j = 0; j < gridC[i].length; j++) {
                    System.out.print(gridC[i][j]);
                }
                System.out.println();
            }
        }
    }

}
