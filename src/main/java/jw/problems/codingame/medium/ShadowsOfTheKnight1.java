package jw.problems.codingame.medium;

import java.awt.Point;
import java.util.Scanner;

/**
 * https://www.codingame.com/ide/puzzle/shadows-of-the-knight-episode-1
 *
 * The Goal
 * Batman will look for the hostages on a given building by jumping from one window to another using his grapnel gun. Batman's goal is to jump to the window where the hostages are located in order to disarm the bombs. Unfortunately he has a limited number of jumps before the bombs go off...
 * Rules
 * Before each jump, the heat-signature device will provide Batman with the direction of the bombs based on Batman current position:
 *
 * U (Up)
 * UR (Up-Right)
 * R (Right)
 * DR (Down-Right)
 * D (Down)
 * DL (Down-Left)
 * L (Left)
 * UL (Up-Left)
 *
 *
 * Your mission is to program the device so that it indicates the location of the next window Batman should jump to in order to reach the bombs' room as soon as possible.
 *
 * Buildings are represented as a rectangular array of windows, the window in the top left corner of the building is at index (0,0).
 * Note
 * For some tests, the bombs' location may change from one execution to the other: the goal is to help you find the best algorithm in all cases.
 *
 * The tests provided are similar to the validation tests used to compute the final score but remain different.
 * Game Input
 * The program must first read the initialization data from standard input. Then, within an infinite loop, read the device data from the standard input and provide to the standard output the next movement instruction.
 * Initialization input
 *
 * Line 1 : 2 integers W H. The (W, H) couple represents the width and height of the building as a number of windows.
 *
 * Line 2 : 1 integer N, which represents the number of jumps Batman can make before the bombs go off.
 *
 * Line 3 : 2 integers X0 Y0, representing the starting position of Batman.
 * Input for one game turn
 * The direction indicating where the bomb is.
 * Output for one game turn
 * A single line with 2 integers X Y separated by a space character. (X, Y) represents the location of the next window Batman should jump to. X represents the index along the horizontal axis, Y represents the index along the vertical axis. (0,0) is located in the top-left corner of the building.
 * Constraints
 * 1 ≤ W ≤ 10000
 * 1 ≤ H ≤ 10000
 * 2 ≤ N ≤ 100
 * 0 ≤ X, X0 < W
 * 0 ≤ Y, Y0 < H
 * Response time per turn ≤ 150ms
 * Response time per turn ≤ 150ms
 */
public class ShadowsOfTheKnight1 {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int W = in.nextInt(); // width of the building.
        int H = in.nextInt(); // height of the building.
        int N = in.nextInt(); // maximum number of turns before game over.
        int X = in.nextInt();
        int Y = in.nextInt();

        Point tl = new Point(0, 0);
        Point br = new Point(W - 1, H - 1);
        while (true) {
            String bombDir = in.next();
            switch (bombDir) {
                case "U":
                    br.y = Y - 1;
                    br.x = X;
                    tl.x = X;
                    break;
                case "UR":
                    br.y = Y - 1;
                    tl.x = X + 1;
                    break;
                case "UL":
                    br.y = Y - 1;
                    br.x = X - 1;
                    break;
                case "D":
                    tl.y = Y + 1;
                    br.x = X;
                    tl.x = X;
                    break;
                case "DL":
                    tl.y = Y + 1;
                    br.x = X - 1;
                    break;
                case "DR":
                    tl.y = Y + 1;
                    tl.x = X + 1;
                    break;
                case "R":
                    tl.x = X + 1;
                    tl.y = Y;
                    br.y = Y;
                    break;
                case "L":
                    br.x = X - 1;
                    tl.y = Y;
                    br.y = Y;
                    break;
            }
            int newX = (br.x + tl.x) / 2;
            int newY = (br.y + tl.y) / 2;
            X = newX;
            Y = newY;
            System.out.println(newX + " " + newY);
        }
    }

}
