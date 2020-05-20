package jw.problems.uva.p102;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=3&page=show_problem&problem=38
 *
 * ./etc/uva/p102.pdf
 */
public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = in.readLine()) != null && line.length() > 0) {
            StringTokenizer token = new StringTokenizer(line);
            int[] vals = new int[9];
            for (int i = 0; i < vals.length; i++) {
                vals[i] = Integer.parseInt(token.nextToken());
            }
            Main p = new Main();
            p.eval(vals);
            p.pprint();
        }
    }

    // 012 345 678
    // BGC BGC BGC
    private static final String[] ck = {"BCG", "BGC", "CBG", "CGB", "GBC", "GCB"};
    private static final int[][] cv = {{0, 5, 7}, {0, 4, 8}, {2, 3, 7}, {2, 4, 6}, {1, 3, 8}, {1, 5, 6}};

    private int minval = Integer.MAX_VALUE;
    private int minidx = 0;

    public void eval(int[] bgcbgcbgc) {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += bgcbgcbgc[i];
        }
        for (int i = 0; i < 6; i++) {
            int movements = sum;
            int[] hold = cv[i];
            for (int j = 0; j < 3; j++) {
                movements -= bgcbgcbgc[hold[j]];
            }
            if (movements < minval) {
                minval = movements;
                minidx = i;
            }
        }
    }

    public void pprint() {
        System.out.println(ck[minidx] + " " + minval);
    }

}
