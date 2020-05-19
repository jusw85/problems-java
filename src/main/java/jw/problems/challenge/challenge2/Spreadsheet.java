package jw.problems.challenge.challenge2;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A spreadsheet consists of a two-dimensional array of cells, labeled A1, A2, etc. Rows are identified using letters, columns by numbers. Each cell contains either an integer (its value) or an expression. Expressions contain integers, cell references, and the operators '+', '-', '*', '/' with the usual rules of evaluation – note that the input is RPN and should be evaluated in stack order.
 *
 * The spreadsheet input is defined as follows:
 *
 * Line 1: two integers, defining the width and height of the spreadsheet (n, m)
 *
 * n*m lines each containing an expression which is the value of the corresponding cell (cells enumerated in the order A1, A2, A, B1, ...)
 *
 * The Input
 *
 * 3 2
 * A2
 * 4 5 *
 * A1
 * A1 B2 / 2 +
 * 3
 * 39 B1 B2 * /
 * The Output
 *
 * 3 2
 * 20.00000
 * 20.00000
 * 20.00000
 * 8.66667
 * 3.00000
 * 1.50000
 */
public class Spreadsheet {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int numColumns = sc.nextInt();
        int numRows = sc.nextInt();
        sc.nextLine();

        Spreadsheet spreadsheet = new Spreadsheet(numRows, numColumns);

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                String line = sc.nextLine();
                spreadsheet.insertCell(i, j, line);
            }
        }

        sc.close();

        try {
            spreadsheet.evaluate();
            spreadsheet.pprint();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private int numRows;
    private int numCols;
    private String[][] exprs;
    private double[][] values;

    public static final Pattern CELL_PATTERN = Pattern
            .compile("(-?)([A-Z])([0-9]+)");
    public static final Pattern NUMBER_PATTERN = Pattern.compile("-?\\d+");

    public Spreadsheet(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        exprs = new String[numRows][numCols];
        values = new double[numRows][numCols];
    }

    public void insertCell(int row, int col, String line) {
        exprs[row][col] = line;
    }

    public void evaluate() throws Exception {
        boolean[][][][] dependencyGraph = new boolean[numRows][numCols][numRows][numCols];
        int[][] numIncoming = new int[numRows][numCols];

        for (int i = 0; i < exprs.length; i++) {
            for (int j = 0; j < exprs[i].length; j++) {
                Matcher m = CELL_PATTERN.matcher(exprs[i][j]);
                while (m.find()) {
                    String rowRef = m.group(2);
                    String colRef = m.group(3);
                    int srcRow = rowRef.charAt(0) - 'A';
                    int srcCol = Integer.parseInt(colRef) - 1;
                    dependencyGraph[srcRow][srcCol][i][j] = true;
                    numIncoming[i][j]++;
                }
            }
        }

        Queue<Point> toProcess = new LinkedList<Point>();
        for (int i = 0; i < numIncoming.length; i++) {
            for (int j = 0; j < numIncoming[i].length; j++) {
                if (numIncoming[i][j] <= 0) {
                    toProcess.offer(new Point(i, j));
                }
            }
        }

        Point point;
        while ((point = toProcess.poll()) != null) {
            int srcRow = point.x;
            int srcCol = point.y;
            values[srcRow][srcCol] = evaluateExpression(exprs[srcRow][srcCol]);

            boolean[][] destGraph = dependencyGraph[srcRow][srcCol];
            for (int i = 0; i < destGraph.length; i++) {
                for (int j = 0; j < destGraph[i].length; j++) {
                    if (destGraph[i][j]) {
                        numIncoming[i][j]--;
                        if (numIncoming[i][j] <= 0) {
                            toProcess.offer(new Point(i, j));
                        }
                    }
                }
            }
        }

        for (int i = 0; i < numIncoming.length; i++) {
            for (int j = 0; j < numIncoming[i].length; j++) {
                if (numIncoming[i][j] > 0) {
                    throw new Exception("Cycle detected");
                }
            }
        }
    }

    private double evaluateExpression(String expr) throws Exception {
        Scanner sc = new Scanner(expr);
        Stack<Double> stack = new Stack<Double>();
        while (sc.hasNext()) {
            String token = sc.next();
            if (token.equals("*")) {
                stack.push(stack.pop() * stack.pop());
            } else if (token.equals("+")) {
                stack.push(stack.pop() + stack.pop());
            } else if (token.equals("++")) {
                stack.push(stack.pop() + 1);
            } else if (token.equals("--")) {
                stack.push(stack.pop() - 1);
            } else if (token.equals("-")) {
                double val2 = stack.pop();
                double val1 = stack.pop();
                stack.push(val1 - val2);
            } else if (token.equals("/")) {
                double val2 = stack.pop();
                double val1 = stack.pop();
                stack.push(val1 / val2);
            } else {
                stack.push(tokenToValue(token));
            }
        }
        sc.close();
        return stack.pop();
    }

    private double tokenToValue(String token) throws Exception {
        Matcher m;
        if (NUMBER_PATTERN.matcher(token).matches()) {
            return Double.parseDouble(token);
        } else if ((m = CELL_PATTERN.matcher(token)).matches()) {
            String negative = m.group(1);
            String rowRef = m.group(2);
            String colRef = m.group(3);
            int srcRow = rowRef.charAt(0) - 'A';
            int srcCol = Integer.parseInt(colRef) - 1;
            double value = values[srcRow][srcCol];
            return negative.equals("") ? value : -1 * value;
        } else {
            throw new Exception("Invalid token: " + token);
        }
    }

    private void pprint() {
        System.out.println(numCols + " " + numRows);
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                System.out.format("%.5f", values[i][j]);
                System.out.println();
            }
        }
    }

}
