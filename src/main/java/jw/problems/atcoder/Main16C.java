package jw.problems.atcoder;

import java.util.Scanner;

/**
 * https://atcoder.jp/contests/abc016/tasks/abc016_3
 *
 * C - 友達の友達
 *
 * Time Limit: 2 sec / Memory Limit: 256 MB
 * 問題文
 *
 * 高橋くんはSNSの管理者をしています。このSNSではユーザ同士が友達という関係で繋がることができます。高橋くんはそれぞれのユーザの「友達の友達」が何人いるかを調べることにしました。友達関係が与えられるので、各ユーザの「友達の友達」の人数を求めてください。ただし、自分自身や友達は、「友達の友達」に含みません。
 * 入力
 *
 * 入力は以下の形式で標準入力から与えられる。
 *
 * N
 *
 *  M
 *
 *
 * A1
 *
 *  B1
 *
 *
 * A2
 *
 *  B2
 *
 *
 * :
 * AM
 *
 *  BM
 *
 *
 *     1
 *
 * 行目には、ユーザ数 N(1≦N≦10) と友達の組の数 M(0≦M≦N×(N−1)/2)
 * がスペース区切りで与えられる。
 * 各ユーザには 1
 * から N
 * までのユーザIDが割り当てられている。
 * 2
 * 行目からの M
 *
 *     行では、友達関係にあるユーザのID \(A_i,B_i (1≦A_i がスペース区切りで与えられる。ただし、 i≠j ならば (A_i,B_i)≠(A_j,B_j) を満たす。\)
 *
 * 出力
 *
 * 各ユーザの友達の友達の人数をユーザIDの小さい順に一行ごと出力せよ。出力の末尾には改行をつけること。
 * 入力例1
 * 3 2
 * 1 2
 * 2 3
 *
 * 出力例1
 * 1
 * 0
 * 1
 *
 * 入力例2
 * 3 3
 * 1 2
 * 1 3
 * 2 3
 *
 * 出力例2
 * 0
 * 0
 * 0
 *
 * 入力例3
 * 8 12
 * 1 6
 * 1 7
 * 1 8
 * 2 5
 * 2 6
 * 3 5
 * 3 6
 * 4 5
 * 4 8
 * 5 6
 * 5 7
 * 7 8
 *
 * 出力例3
 * 4
 * 4
 * 4
 * 5
 * 2
 * 3
 * 4
 * 2
 */
public class Main16C {

    private int[][] network;
    private int size;
    private boolean isDirty = false;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int numFriends = sc.nextInt();
        int numLines = sc.nextInt();
        sc.nextLine();

        Main16C network = new Main16C(numFriends);
        for (int i = 0; i < numLines; i++) {
            int src = sc.nextInt();
            int dest = sc.nextInt();
            sc.nextLine();
            network.connect(src, dest);
        }
        sc.close();

        network.print();
    }

    public Main16C(int size) {
        this.network = new int[size][size];
        this.size = size;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j)
                    network[i][j] = 0;
                else
                    network[i][j] = -1;
            }
        }
    }

    public void connect(int src, int dest) {
        isDirty = true;
        network[src - 1][dest - 1] = 1;
        network[dest - 1][src - 1] = 1;
    }

    public void update() {
        for (int k = 0; k < size; k++) {
            for (int i = 0; i < size; i++) {
                for (int j = i + 1; j < size; j++) {
                    if (k != i && k != j && network[i][k] > 0 && network[k][j] > 0) {
                        int newcost = network[i][k] + network[k][j];
                        if (network[i][j] < 0 || newcost < network[i][j]) {
                            network[i][j] = newcost;
                            network[j][i] = newcost;
                        }
                    }
                }
            }
        }
        isDirty = false;
    }

    public void print() {
        if (isDirty)
            update();

        for (int i = 0; i < size; i++) {
            int numFOAF = 0;
            for (int j = 0; j < size; j++) {
                if (network[i][j] == 2)
                    numFOAF++;
            }
            System.out.println(numFOAF);
        }
    }

}
