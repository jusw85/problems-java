package jw.problems.uva.p104;

import java.util.Scanner;

/**
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=3&page=show_problem&problem=40
 *
 * ./etc/uva/p104.pdf
 */
public class Main {

    private int n;
    private double[][][] m;
    private int[][][] parent;

    public Main(double[][] m0) {
        this.n = m0.length;
        this.m = new double[n][n][n];
        this.parent = new int[n][n][n];
        this.m[0] = m0;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine() && (line = sc.nextLine()).length() > 0) {
            int n = Integer.parseInt(line.trim());
            double[][] m = new double[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i == j) {
                        m[i][j] = 1;
                    } else {
                        m[i][j] = sc.nextDouble();
                    }
                }
                sc.nextLine();
            }
            Main p = new Main(m);
            p.compute();
            p.print();
        }
        sc.close();
    }

    // m[0][i][j] = base conversion rate from i to j after 1 exchange
    // m[d][i][j] = optimal conversion rate from i to j after (d + 1) exchanges
    public void compute() {
        for (int d = 1; d < n; d++) {
            for (int k = 0; k < n; k++) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        double tmp = m[d - 1][i][k] * m[0][k][j];
                        if (tmp > m[d][i][j]) {
                            m[d][i][j] = tmp;
                            parent[d][i][j] = k;
                        }
                    }
                }
            }
        }
    }

    public void print() {
        for (int d = 1; d < n; d++) {
            for (int i = 0; i < n; i++) {
                if (m[d][i][i] > 1.01) {
                    construct(d, i);
                    return;
                }
            }
        }
        System.out.println("no arbitrage sequence exists");
    }

    // if p[d][i][j] = k,
    //   optimal conversion rate from i to j after (d + 1) exchanges =
    //   optimal conversion rate from i to k after d exchanges + base(k, j)
    private void construct(int d0, int i0) {
        int[] l = new int[d0 + 2];
        l[0] = i0;
        l[l.length - 1] = i0;

        int k = i0;
        for (int d = d0; d > 0; d--) {
            l[d] = parent[d][i0][k];
            k = parent[d][i0][k];
        }

        String prefix = "";
        for (int j = 0; j < l.length; j++) {
            System.out.print(prefix + (l[j] + 1));
            prefix = " ";
        }
        System.out.println();
    }

}
