package jw.problems.codingame.medium;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * https://www.codingame.com/ide/puzzle/mars-lander-episode-2
 *
 * The Goal
 * The goal for your program is to safely land the "Mars Lander" shuttle, the landing ship which contains the Opportunity rover. Mars Lander is guided by a program, and right now the failure rate for landing on the NASA simulator is unacceptable.
 *
 * This puzzle is the second level of the "Mars Lander" trilogy. The controls are the same as the previous level but you must now control the angle in order to succeed.
 * Rules
 *
 * Built as a game, the simulator puts Mars Lander on a limited zone of Mars sky.
 * The zone is 7000m wide and 3000m high.
 *
 * There is a unique area of flat ground on the surface of Mars, which is at least 1000 meters wide.
 * Every second, depending on the current flight parameters (location, speed, fuel ...), the program must provide the new desired tilt angle and thrust power of Mars Lander:
 * Angle goes from -90° to 90° . Thrust power goes from 0 to 4 .
 *
 * The game simulates a free fall without atmosphere. Gravity on Mars is 3.711 m/s² . For a thrust power of X, a push force equivalent to X m/s² is generated and X liters of fuel are consumed. As such, a thrust power of 4 in an almost vertical position is needed to compensate for the gravity on Mars.
 *
 * For a landing to be successful, the ship must:
 *
 * land on flat ground
 * land in a vertical position (tilt angle = 0°)
 * vertical speed must be limited ( ≤ 40m/s in absolute value)
 * horizontal speed must be limited ( ≤ 20m/s in absolute value)
 *
 * Note
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
public class MarsLander2 {

    public static void main(String args[]) throws FileNotFoundException {
        Scanner in = new Scanner(System.in);
        MarsLander m = new MarsLander(in);
        m.run();
    }

    private static class MarsLander {
        private Scanner sc;

        private Point[] points;
        private Point lbound;
        private Point rbound;
        private double landingWidth;

        private double X;
        private double Y;
        private double hSpeed;
        private double vSpeed;
        private int fuel;
        private int rotate;
        private int power;
        private final double G = -3.711;
        private int constDeg = (int) Math.floor(Math.toDegrees(Math.acos(-G / 4.0)));

        private boolean isFlipped = false;

        public MarsLander(Scanner sc) {
            this.sc = sc;
        }

        private void initFlip() {
            Point mid = new Point((lbound.x + rbound.x) / 2, lbound.y);
            if (X > mid.x) {
                X = -X;
                int tmp = lbound.x;
                lbound.x = -rbound.x;
                rbound.x = -tmp;
                hSpeed = -hSpeed;
                rotate = -rotate;
                isFlipped = !isFlipped;
            }
        }

        private void parseInput() {
            int surfaceN = sc.nextInt(); // the number of points used to draw the surface of Mars.
            points = new Point[surfaceN];
            for (int i = 0; i < surfaceN; i++) {
                int landX = sc.nextInt(); // X coordinate of a surface point. (0 to 6999)
                int landY = sc.nextInt(); // Y coordinate of a surface point. By linking all the points together in a sequential fashion, you form the surface of Mars.
                points[i] = new Point(landX, landY);
            }
        }

        private void initLandingBounds() {
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
            landingWidth = rbound.x - lbound.x;
        }

        public void printValues(int deg, int p) {
            rotate = deg < rotate ?
                    Math.max(rotate - 15, deg) :
                    Math.min(rotate + 15, deg);
            power = p < power ?
                    Math.max(power - 1, p) :
                    Math.min(power + 1, p);
            System.out.print(isFlipped ? -rotate : rotate);
            System.out.println(" " + power);
        }

        public void output(int deg, int p) {
            printValues(deg, p);
            tick();
            nextInput();
        }

        public void run() {
            parseInput();
            initLandingBounds();
            updateInput();

            while (true) {
                initFlip();
                if (X < lbound.x) {
                    double hMin = 25;
                    double hMax = 30;
                    if (hSpeed > hMax) {
                        while (hSpeed > hMax) {
                            if (vSpeed < 0) {
                                output(constDeg, 4);
                            } else {
                                output(constDeg, 3);
                            }
                        }
                        continue;
                    } else if (hSpeed < hMin) {
                        while (hSpeed < hMin) {
                            if (vSpeed < 0) {
                                output(-constDeg, 4);
                            } else {
                                output(-constDeg, 3);
                            }
                        }
                        continue;
                    } else {
                        while (X < lbound.x) {
                            if (vSpeed < 0) {
                                output(0, 4);
                            } else {
                                output(0, 3);
                            }
                        }
                        continue;
                    }
                }

                initVertical();
                double vt = vt1 + vt2 + 5;
                if (hSpeed > 20 || ((hSpeed > 0) && (vt * hSpeed) + X > rbound.x)) {
                    output(constDeg, 4);
                    continue;
                } else if (hSpeed < -20 || ((hSpeed < 0) && (vt * hSpeed) + X < lbound.x)) {
                    output(-constDeg, 4);
                    continue;
                }

                if (vt1 < 8) {
                    landVertical();
                }
                while (power > 0) {
                    output(0, 0);
                }
                initVertical();
                landVertical();
            }
        }

        private double vt1;
        private double vt2;

        public void initVertical() {
            double s = Y - lbound.y;
            double a1 = -G;
            double a2 = -(G + 4);
            double u1 = -vSpeed;
            double v2 = 40;

            double s1 = (((Math.pow(v2, 2) - Math.pow(u1, 2)) / 2) - (a2 * s)) / (a1 - a2);
            double s2 = s - s1;
            double v1 = Math.sqrt(Math.pow(u1, 2) + (2 * a1 * s1));
            vt1 = (v1 - u1) / a1;
            double u2 = v1;
            vt2 = (v2 - u2) / a2;
        }

        public void landVertical() {
            for (int i = 0; i < vt1 - 3; i++) {
                output(0, 0);
            }
            while (true) {
                if (vSpeed < -39) {
                    output(0, 4);
                } else {
                    output(0, 3);
                }
            }
        }

        public void updateInput() {
            X = sc.nextInt();
            Y = sc.nextInt();
            hSpeed = sc.nextInt(); // the horizontal speed (in m/s), can be negative.
            vSpeed = sc.nextInt(); // the vertical speed (in m/s), can be negative.
            fuel = sc.nextInt(); // the quantity of remaining fuel in liters.
            rotate = sc.nextInt(); // the rotation angle in degrees (-90 to 90).
            power = sc.nextInt(); // the thrust power (0 to 4).
        }

        public void nextInput() {
            int X = sc.nextInt();
            int Y = sc.nextInt();
            int hSpeed = sc.nextInt(); // the horizontal speed (in m/s), can be negative.
            int vSpeed = sc.nextInt(); // the vertical speed (in m/s), can be negative.
            int fuel = sc.nextInt(); // the quantity of remaining fuel in liters.
            int rotate = sc.nextInt(); // the rotation angle in degrees (-90 to 90).
            int power = sc.nextInt(); // the thrust power (0 to 4).
        }

        public void printState() {
            System.err.println(X + " " + Y);
            System.err.println(hSpeed + " " + vSpeed);
            System.err.println(fuel);
            System.err.println(rotate + " " + power);
        }

        public void tick() {
            double rotateR = Math.toRadians(rotate);
            double hThrust = -power * Math.sin(rotateR);
            double vThrust = power * Math.cos(rotateR);
            double vA = vThrust + G;
            double hA = hThrust;
            Y += vSpeed + (vA / 2);
            X += hSpeed + (hA / 2);
            vSpeed += vA;
            hSpeed += hA;
            fuel -= power;
        }
    }
}
