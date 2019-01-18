package jw.problems;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * https://play.google.com/store/apps/details?id=com.sm.calculateme
 * Simple solver for the calculator game
 * Supports basic arithmetic operations, insert number, reverse, and delete last number
 * Sample format is {"+ 1", "- 1", "* 1", "/ 1", "i 1", "r", "<"} respectively
 */
public class CalculatorGame {
    public static DecimalFormat df = new DecimalFormat("#");

    public static void main(String[] args) {
        int startValue = 0;
        int goal = 102;
        int maxMoves = 4;
        String[] ops = {"+ 1", "<", "i 10"};
        String sol = solve(startValue, goal, maxMoves, ops);
        System.out.println(sol);
    }

    public static String solve(double startValue, double goal, int maxMoves, String[] ops) {
        List<Op> ls = new ArrayList<>();
        for (String str : ops) {
            ls.add(Op.toOp(str));
        }
        StringBuilder sb = new StringBuilder();
        if (solve(startValue, goal, 0, maxMoves, ls, sb)) {
            return sb.substring(1);
        }
        return null;
    }

    public static boolean solve(double i, double goal, int depth, int maxdepth, List<Op> ls, StringBuilder sb) {
        if (Math.abs(i - goal) <= 0.001) {
            return true;
        }
        if (depth >= maxdepth) {
            return false;
        }
        for (Op op : ls) {
            boolean found = solve(op.exec(i), goal, depth + 1, maxdepth, ls, sb);
            if (found) {
                sb.insert(0, "," + op.toString());
                return true;
            }
        }
        return false;
    }

    public static class Op {
        private char op;
        private double val;

        public String toString() {
            return "" + op + (int) val;
        }

        public double exec(double i) {
            switch (op) {
                case '-':
                    i -= val;
                    break;
                case '+':
                    i += val;
                    break;
                case '*':
                    i *= val;
                    break;
                case '/':
                    i /= val;
                    break;
                case 'i': // insert number
                    int length = String.valueOf((int) val).length();
                    i = (i * Math.pow(10, length)) + val;
                    break;
                case '<': // chomp last digit
                    i = (int) i / 10;
                    break;
                case 'r': // reverse
                    boolean neg = false;
                    if (i < 0) {
                        neg = true;
                        i = Math.abs(i);
                    }
                    String str = df.format(i);
                    String rstr = new StringBuilder(str).reverse().toString();
                    i = Double.parseDouble(rstr);
                    if (neg) {
                        i *= -1;
                    }
                    break;
            }
            return i;
        }

        public static Op toOp(String str) {
            Op op = new Op();
            Scanner sc = new Scanner(str);
            op.op = sc.next().charAt(0);
            if (sc.hasNextDouble())
                op.val = sc.nextDouble();
            sc.close();
            return op;
        }
    }
}
