package jw.problems.codingame.medium;

import java.util.Scanner;

/**
 * https://www.codingame.com/ide/puzzle/stock-exchange-losses
 *
 * The Goal
 * A finance company is carrying out a study on the worst stock investments and would like to acquire a program to do so. The program must be able to analyze a chronological series of stock values in order to show the largest loss that it is possible to make by buying a share at a given time t0 and by selling it at a later date t1. The loss will be expressed as the difference in value between t0 and t1. If there is no loss, the loss will be worth 0.
 * Game Input
 * Input
 *
 * Line 1: the number n of stock values available.
 *
 * Line 2: the stock values arranged in order, from the date of their introduction on the stock market v1 until the last known value vn. The values are integers.
 * Output
 * The maximal loss p, expressed negatively if there is a loss, otherwise 0.
 * Constraints
 * 0 < n < 100000
 * 0 < v < 231
 */
public class StockExchangeLosses {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();

        int max = 0;
        int min = 0;
        int maxdiff = 0;
        for (int i = 0; i < n; i++) {
            int v = in.nextInt();
            if (v > max) {
                max = v;
                min = v;
            } else if (v < min) {
                min = v;
                maxdiff = Math.min(maxdiff, min - max);
            }
        }
        System.out.println(maxdiff);
    }

}
