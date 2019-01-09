package jw.problems.codingame.easy;

import java.awt.Point;
import java.util.Scanner;

/**
 * https://www.codingame.com/ide/puzzle/mars-lander-episode-1
 *
 * The Goal
 * The goal for your program is to safely land the "Mars Lander" shuttle, the landing ship which contains the Opportunity rover. Mars Lander is guided by a program, and right now the failure rate for landing on the NASA simulator is unacceptable.
 *
 * Note that it may look like a difficult problem, but in reality the problem is easy to solve. This puzzle is the first level among three, therefore, we need to present you some controls you won't need in order to complete this first level.
 * Rules
 *
 * Built as a game, the simulator puts Mars Lander on a limited zone of Mars sky.
 * The zone is 7000m wide and 3000m high.
 *
 * For this level, Mars Lander is above the landing zone, in vertical position, with no initial speed.
 *
 * There is a unique area of flat ground on the surface of Mars, which is at least 1000 meters wide.
 * Every second, depending on the current flight parameters (location, speed, fuel ...), the program must provide the new desired tilt angle and thrust power of Mars Lander:
 * Angle goes from -90° to 90° . Thrust power goes from 0 to 4 .
 *
 * For this level, you only need to control the thrust power: the tilt angle should be 0.
 * The game simulates a free fall without atmosphere. Gravity on Mars is 3.711 m/s² . For a thrust power of X, a push force equivalent to X m/s² is generated and X liters of fuel are consumed. As such, a thrust power of 4 in an almost vertical position is needed to compensate for the gravity on Mars.
 *
 * For a landing to be successful, the ship must:
 *
 * land on flat ground
 * land in a vertical position (tilt angle = 0°)
 * vertical speed must be limited ( ≤ 40m/s in absolute value)
 * horizontal speed must be limited ( ≤ 20m/s in absolute value)
 *
 *
 * Remember that this puzzle was simplified:
 *
 * the landing zone is just below the shuttle. You can therefore ignore rotation and always output 0 as the target angle.
 * you don't need to store the coordinates of the surface of Mars to succeed.
 * you only need your vertical landing speed to be between 0 and 40m/s – your horizontal speed being nil.
 * As the shuttle falls, the vertical speed is negative. As the shuttle flies upward, the vertical speed is positive.
 *
 * Note
 * For this first level, Mars Lander will go through a single test.
 *
 * Tests and validators are only slightly different. A program that passes a given test will pass the corresponding validator without any problem.
 * Game Input
 * The program must first read the initialization data from standard input. Then, within an infinite loop, the program must read the data from the standard input related to Mars Lander's current state and provide to the standard output the instructions to move Mars Lander.
 * Initialization input
 * Line 1: the number surfaceN of points used to draw the surface of Mars.
 * Next surfaceN lines: a couple of integers landX landY providing the coordinates of a ground point. By linking all the points together in a sequential fashion, you form the surface of Mars which is composed of several segments. For the first point, landX = 0 and for the last point, landX = 6999
 * Input for one game turn
 * A single line with 7 integers: X Y hSpeed vSpeed fuel rotate power
 *
 * X,Y are the coordinates of Mars Lander (in meters).
 * hSpeed and vSpeed are the horizontal and vertical speed of Mars Lander (in m/s). These can be negative depending on the direction of Mars Lander.
 * fuel is the remaining quantity of fuel in liters. When there is no more fuel, the power of thrusters falls to zero.
 * rotate is the angle of rotation of Mars Lander expressed in degrees.
 * power is the thrust power of the landing ship.
 *
 * Output for one game turn
 * A single line with 2 integers: rotate power :
 *
 * rotate is the desired rotation angle for Mars Lander. Please note that for each turn the actual value of the angle is limited to the value of the previous turn +/- 15°.
 * power is the desired thrust power. 0 = off. 4 = maximum power. Please note that for each turn the value of the actual power is limited to the value of the previous turn +/- 1.
 *
 * Constraints
 * 2 ≤ surfaceN < 30
 * 0 ≤ X < 7000
 * 0 ≤ Y < 3000
 * -500 < hSpeed, vSpeed < 500
 * 0 ≤ fuel ≤ 2000
 * -90 ≤ rotate ≤ 90
 * 0 ≤ power ≤ 4
 * Response time per turn ≤ 100ms
 */
public class MarsLander1 {

    private static Point[] points;
    private static Point lbound;
    private static Point rbound;

    private static double X;
    private static double Y;
    private static double hSpeed;
    private static double vSpeed;
    private static int fuel;
    private static int rotate;
    private static int power;
    private static final double G = -3.711;

    private static void initLanding() {
        Point prev = points[0];
        lbound = new Point(-1, -1);
        rbound = new Point(-1, -1);
        for (int i = 1; i < points.length; i++) {
            if (points[i].y == prev.y) {
                lbound = prev;
                rbound = points[i];
                break;
            }
            prev = points[i];
        }
    }

    private static void parseInput(Scanner in) {
        int surfaceN = in.nextInt(); // the number of points used to draw the surface of Mars.
        points = new Point[surfaceN];
        for (int i = 0; i < surfaceN; i++) {
            int landX = in.nextInt(); // X coordinate of a surface point. (0 to 6999)
            int landY = in.nextInt(); // Y coordinate of a surface point. By linking all the points together in a sequential fashion, you form the surface of Mars.
            points[i] = new Point(landX, landY);
        }
        initLanding();
    }

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        parseInput(in);

        // game loop
        while (true) {
            updateInput(in);
            double s = Y - lbound.y;
            double a1 = -G;
            double a2 = -(G + 4);
            double u1 = 0;
            double v2 = 40;

            double s1 = (((Math.pow(v2, 2) - Math.pow(u1, 2)) / 2) - (a2 * s)) / (a1 - a2);
            double s2 = s - s1;
            double v1 = Math.sqrt(Math.pow(u1, 2) + (2 * a1 * s1));
            double t1 = (v1 - u1) / a1;

            for (int i = 0; i < t1 - 3; i++) {
                System.out.println("0 0");
            }
            while (true) {
                System.out.println("0 4");
            }
        }
    }

    public static void updateInput(Scanner in) {
        X = in.nextInt();
        Y = in.nextInt();
        hSpeed = in.nextInt(); // the horizontal speed (in m/s), can be negative.
        vSpeed = in.nextInt(); // the vertical speed (in m/s), can be negative.
        fuel = in.nextInt(); // the quantity of remaining fuel in liters.
        rotate = in.nextInt(); // the rotation angle in degrees (-90 to 90).
        power = in.nextInt(); // the thrust power (0 to 4).
    }

    public static void printState() {
        System.err.println(X + " " + Y);
        System.err.println(hSpeed + " " + vSpeed);
        System.err.println(fuel);
        System.err.println(rotate + " " + power);
    }

    public static void tick() {
        double rotateR = Math.toRadians(rotate);
        double hThrust = -power * Math.sin(rotateR);
        double vThrust = power * Math.cos(rotateR);
        double vA = vThrust + G;
        double hA = hThrust;
        X += vSpeed + (vA / 2);
        Y += hSpeed + (hA / 2);
        vSpeed += vA;
        hSpeed += hA;
        fuel -= power;
    }
}
