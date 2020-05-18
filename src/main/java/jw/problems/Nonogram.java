package jw.problems;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Brute force solver for Nonograms
 * Text file input format:
 * numRows numColumns
 * (numRows rows of row constraints)
 * (numCols rows of column constraints)
 */
public class Nonogram {

    public static void main(String[] args) throws IOException {
        Nonogram solver = new Nonogram(new File("etc/nonograms/griddler-207129.txt"));
        solver.solve();
        solver.print();
        solver.printImage(new File("./out.png"));
    }

    public static final char UNKNOWN = '?';
    public static final char BLACK = 'B';
    public static final char WHITE = '_';

    private File file;
    private boolean isParsed;

    public char[][] grid;
    public int numRows = 0;
    public int numCols = 0;
    public int[][] rowConstraints;
    public int[][] colConstraints;
    public Pattern[] rowPatterns;
    public Pattern[] colPatterns;

    public Nonogram(File file) {
        setFile(file);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        isParsed = false;
    }

    public void parse() throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        numRows = sc.nextInt();
        numCols = sc.nextInt();
        sc.nextLine();
        grid = new char[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                grid[i][j] = UNKNOWN;
            }
        }
        rowConstraints = new int[numRows][];
        for (int i = 0; i < numRows; i++) {
            String line = sc.nextLine();
            rowConstraints[i] = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
        }
        colConstraints = new int[numCols][];
        for (int i = 0; i < numCols; i++) {
            String line = sc.nextLine();
            colConstraints[i] = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
        }
        sc.close();

        rowPatterns = new Pattern[numRows];
        for (int i = 0; i < rowConstraints.length; i++) {
            rowPatterns[i] = constraintToPattern(rowConstraints[i]);
        }

        colPatterns = new Pattern[numCols];
        for (int i = 0; i < colConstraints.length; i++) {
            colPatterns[i] = constraintToPattern(colConstraints[i]);
        }
        isParsed = true;
    }

    private static Pattern constraintToPattern(int[] constraints) {
        StringBuilder regex = new StringBuilder();
        regex.append("[" + WHITE + UNKNOWN + "]*");
        String prefix = "";
        for (int i : constraints) {
            regex.append(prefix).append("[" + BLACK + UNKNOWN + "]{" + i + "}");
            prefix = "[" + WHITE + UNKNOWN + "]+";
        }
        regex.append("[" + WHITE + UNKNOWN + "]*");
        return Pattern.compile(regex.toString());
    }

    public void solve() throws FileNotFoundException {
        if (!isParsed) {
            parse();
        }
        firstPass();
        bruteForce();
    }

    private void bruteForce() {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int row = 0; row < numRows; row++) {
                StringBuilder sb = new StringBuilder(numCols);
                for (char c : grid[row]) {
                    sb.append(c);
                }
                changed |= regexBruteForce(sb, rowPatterns[row]);
                for (int col = 0; col < numCols; col++) {
                    grid[row][col] = sb.charAt(col);
                }
            }

            for (int col = 0; col < numCols; col++) {
                StringBuilder sb = new StringBuilder(numRows);
                for (int row = 0; row < numRows; row++) {
                    sb.append(grid[row][col]);
                }
                changed |= regexBruteForce(sb, colPatterns[col]);
                for (int row = 0; row < numRows; row++) {
                    grid[row][col] = sb.charAt(row);
                }
            }
        }
    }

    private boolean regexBruteForce(StringBuilder sb, Pattern p) {
        boolean changed = false;
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == UNKNOWN) {
                sb.setCharAt(i, BLACK);
                boolean possiblyBlack = p.matcher(sb).matches();
                sb.setCharAt(i, WHITE);
                boolean possiblyWhite = p.matcher(sb).matches();

                if (possiblyBlack && possiblyWhite) {
                    sb.setCharAt(i, UNKNOWN);
                } else if (possiblyBlack) {
                    sb.setCharAt(i, BLACK);
                    changed = true;
                } else if (possiblyWhite) {
                    sb.setCharAt(i, WHITE);
                    changed = true;
                } else {
                    System.out.println("Failed to match");
                }
            }
        }
        return changed;
    }

    private void firstPass() {
        firstPassRow();
        firstPassCol();
    }

    private void firstPassRow() {
        for (int row = 0; row < numRows; row++) {
            char[] tmp = new char[numCols];
            for (int i = 0; i < tmp.length; i++) {
                tmp[i] = UNKNOWN;
            }

            simpleBoxes(tmp, rowConstraints[row]);

            for (int i = 0; i < tmp.length; i++) {
                if (grid[row][i] == UNKNOWN) {
                    grid[row][i] = tmp[i];
                }
            }
        }
    }

    private void firstPassCol() {
        for (int col = 0; col < numCols; col++) {
            char[] tmp = new char[numRows];
            for (int i = 0; i < tmp.length; i++) {
                tmp[i] = UNKNOWN;
            }

            simpleBoxes(tmp, colConstraints[col]);

            for (int i = 0; i < tmp.length; i++) {
                if (grid[i][col] == UNKNOWN) {
                    grid[i][col] = tmp[i];
                }
            }
        }
    }

    private void simpleBoxes(char[] line, int[] constraint) {
        int sum = 0;
        int gap = 0;
        for (int val : constraint) {
            sum += (gap + val);
            gap = 1;
        }
        int remainder = line.length - sum;

        int ptr = 0;
        for (int val : constraint) {
            if (val - remainder <= 0) {
                ptr += (val + 1);
            } else {
                ptr += remainder;
                for (int end = ptr + val - remainder; ptr < end; ptr++) {
                    line[ptr] = BLACK;
                }
                ptr++;
            }
        }
    }

    public void print() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }

    public void printRowConstraints() {
        printConstraints(rowConstraints);
    }

    public void printColConstraints() {
        printConstraints(colConstraints);
    }

    private void printConstraints(int[][] constraints) {
        for (int[] constraint : constraints) {
            for (int i : constraint) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
    }

    public void printImage(File imageFile) throws IOException {
        BufferedImage bi = new BufferedImage(numCols, numRows, BufferedImage.TYPE_INT_RGB);
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                Color.WHITE.getRGB();
                bi.setRGB(col, row, charToRGB(grid[row][col]));
            }
        }
        ImageIO.write(bi, "png", imageFile);
    }

    private int charToRGB(char c) {
        switch (c) {
            case BLACK:
                return Color.BLACK.getRGB();
            case WHITE:
                return Color.WHITE.getRGB();
            default:
                return Color.RED.getRGB();
        }
    }
}
