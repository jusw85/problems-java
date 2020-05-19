package jw.problems.atcoder;

import java.util.Scanner;

/**
 * https://atcoder.jp/contests/abc015/tasks/abc015_2
 *
 * B - 高橋くんの集計
 *
 * Time Limit: 2 sec / Memory Limit: 256 MB
 * 問題文
 *
 * 高橋くんは、上司から
 *
 *     会社で作っている N
 *
 *     個のソフトウェアに平均でどれくらいのバグがあるか調べろ
 *     ただし、バグがないソフトは調査母数に含めるな
 *
 * と指示されました。
 *
 * しかも、上司は小数が嫌いです。とはいえ、バグ数の平均値を過小報告するわけにもいかないので、値を切り上げて報告することにしました。
 *
 * 高橋くんは、素早くバグ数の平均値をまとめて上司に報告する必要があります。 ソフトウェアごとのバグ数が与えられるので、バグが含まれるソフトウェアの、バグ数の平均値を小数で切り上げて求めてください。
 * 入力
 *
 * 入力は以下の形式で標準入力から与えられる。
 *
 * N
 *
 *
 * A1
 *
 *  A2
 *
 *  ... AN
 *
 *
 *     1
 *
 * 行目には、ソフトウェアの数を表す整数 N(1≦N≦100)
 * がスペース区切りで与えられる。
 * 2
 * 行目では、それぞれのソフトウェアに含まれるバグの数の情報が、スペース区切りで与えられる。 i 番目のソフトウェアのバグの数は、 i 番目に与えられる整数 Ai(0≦Ai≦100)
 * によって与えられる。
 * 与えられるソフトウェアのバグの合計数は、 1
 *
 *     つ以上であることが保証されている。
 *
 * 出力
 *
 * バグが含まれるソフトウェアの、バグ数の平均値を小数で切り上げて 1
 *
 * 行で出力せよ。出力の末尾には改行をいれること。
 * 入力例1
 * 4
 * 0 1 3 8
 *
 * 出力例1
 * 4
 *
 * バグが含まれるソフトウェアは 3
 * つであり、そのバグの総数は 12 個です。 よって、バグ数の平均値は 4
 *
 * 個です。
 * 入力例2
 * 5
 * 1 4 9 10 15
 *
 * 出力例2
 * 8
 *
 * バグ数の平均値は 7.8
 * であるため、切り上げて 8 と出力する必要があります。
 */
public class Main15B {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int numPrograms = sc.nextInt();
        int totalBugs = 0;
        int totalPrograms = 0;
        int numBugs;
        for (int i = 0; i < numPrograms; i++) {
            if ((numBugs = sc.nextInt()) > 0) {
                totalBugs += numBugs;
                totalPrograms++;
            }
        }
        sc.close();
        System.out.println((int) Math.ceil(1.0 * totalBugs / totalPrograms));
    }

}
