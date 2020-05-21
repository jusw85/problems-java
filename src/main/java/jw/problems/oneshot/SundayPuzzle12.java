package jw.problems.oneshot;

import java.util.ArrayList;
import java.util.List;

/**
 * https://io9.gizmodo.com/can-you-decipher-the-message-hidden-in-this-star-1673831322
 * The numbers from 1 to 16 were written in the circles of the diagram below in such a way that the sum of any four numbers in a straight line was the same. Then the number 1 was replaced by the first letter of a saying, number 2 by the second letter, etc. The final configuration is shown. What was the saying?
 *
 * sum(line) is ((sum(1..16) * 2) / 8) = 34
 * DOAGOODTURNDAILY
 */
public class SundayPuzzle12 {

    public static char[] letters = {'O', 'D', 'D', 'U', 'N', 'G', 'R', 'O', 'L', 'A', 'O', 'A', 'I', 'Y', 'D', 'T'};

    public static int[][] graph = {
            {0, 1, 2, 3},
            {3, 4, 5, 6},
            {6, 7, 1, 8},
            {8, 9, 4, 10},
            {10, 11, 7, 12},
            {12, 13, 9, 14},
            {14, 2, 11, 15},
            {15, 5, 13, 0}};

    public static void main(String[] args) {
        List<Integer> numbersLeft = new ArrayList<>();
        for (int i = 16; i >= 1; i--) {
            numbersLeft.add(i);
        }

        int[] sol = new int[16];
        for (int i = 0; i < sol.length; i++) {
            sol[i] = 0;
        }

        findSolution(numbersLeft, 0, sol, 0, 0);
    }

    public static void findSolution(List<Integer> numbersLeft, int sumTo, int[] sol, int row, int col) {
        if (sumTo < 0) {
            return;
        }

        if (col == 0) {
            if (sumTo != 0)
                return;
            else
                sumTo = 34;
        }

        if (row == graph.length) {
            printSolution(sol);
            return;
        }

        int node = graph[row][col];
        int nextcol = (col + 1) % 4;
        int nextrow = nextcol == 0 ? row + 1 : row;

        if (sol[node] == 0) {
            int length = numbersLeft.size();
            for (int i = 0; i < length; i++) {
                sol[node] = numbersLeft.remove(i);
                findSolution(numbersLeft, sumTo - sol[node], sol, nextrow, nextcol);
                numbersLeft.add(i, sol[node]);
            }
            sol[node] = 0;
        } else {
            findSolution(numbersLeft, sumTo - sol[node], sol, nextrow, nextcol);
        }
    }

    public static void printSolution(int[] sol) {
        StringBuilder sb = new StringBuilder();
        sb.setLength(16);
        for (int i = 0; i < sol.length; i++) {
            int idx = sol[i] - 1;
            sb.setCharAt(idx, letters[i]);
        }
        System.out.println(sb.toString());
    }

}
