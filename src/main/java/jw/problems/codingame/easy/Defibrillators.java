package jw.problems.codingame.easy;

import java.util.Scanner;

/**
 * https://www.codingame.com/ide/puzzle/defibrillators
 *
 * The Goal
 * The city of Montpellier has equipped its streets with defibrillators to help save victims of cardiac arrests. The data corresponding to the position of all defibrillators is available online.
 *
 * Based on the data we provide in the tests, write a program that will allow users to find the defibrillator nearest to their location using their mobile phone.
 * Rules
 * The input data you require for your program is provided in text format.
 * This data is comprised of lines, each of which represents a defibrillator. Each defibrillator is represented by the following fields:
 *
 * A number identifying the defibrillator
 * Name
 * Address
 * Contact Phone number
 * Longitude (degrees)
 * Latitude (degrees)
 *
 * These fields are separated by a semicolon (;).
 *
 * Beware: the decimal numbers use the comma (,) as decimal separator. Remember to turn the comma (,) into dot (.) if necessary in order to use the data in your program.
 *
 * DISTANCE
 * The distance d between two points A and B will be calculated using the following formula:
 * ​
 * Note: In this formula, the latitudes and longitudes are expressed in radians. 6371 corresponds to the radius of the earth in km.
 *
 * The program will display the name of the defibrillator located the closest to the user’s position. This position is given as input to the program.
 * Game Input
 * Input
 *
 * Line 1: User's longitude (in degrees)
 *
 * Line 2: User's latitude (in degrees)
 *
 * Line 3: The number N of defibrillators located in the streets of Montpellier
 *
 * N next lines: a description of each defibrillator
 * Output
 * The name of the defibrillator located the closest to the user’s position.
 * Constraints
 * 0 < N < 10000
 */
public class Defibrillators {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        String LON = in.next();
        String LAT = in.next();
        int N = in.nextInt();
        if (in.hasNextLine()) {
            in.nextLine();
        }

        Coord myCoord = new Coord(parseCoord(LON), parseCoord(LAT));
        String minName = "";
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < N; i++) {
            String DEFIB = in.nextLine();
            String[] fields = DEFIB.split(";");
            String defibName = fields[1];
            Coord defibCoord = new Coord(parseCoord(fields[4]), parseCoord(fields[5]));

            double distance = distance(myCoord, defibCoord);
            if (distance < minDistance) {
                minDistance = distance;
                minName = defibName;
            }
        }
        System.out.println(minName);
    }

    private static double parseCoord(String coord) {
        return Double.parseDouble(coord.replaceAll(",", "."));
    }

    private static double distance(Coord a, Coord b) {
        double x = (b.lon - a.lon) * Math.cos((a.lat + b.lat) / 2.0);
        double y = b.lat - a.lat;
        double d = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) * 6371.0;
        return d;
    }

    private static class Coord {
        public double lon;
        public double lat;

        public Coord(double lon, double lat) {
            this.lon = lon;
            this.lat = lat;
        }
    }

}
