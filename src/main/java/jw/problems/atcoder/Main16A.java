package jw.problems.atcoder;

import java.util.Scanner;

/**
 * https://atcoder.jp/contests/abc016/tasks/abc016_1
 *
 * A - 12月6日
 *
 * Time Limit: 2 sec / Memory Limit: 256 MB
 * 問題文
 *
 * 12月6日は、月を日で割って割り切れる日です。日付が与えられるので月が日で割り切れるかを判定してください。
 * 入力
 *
 * 入力は以下の形式で標準入力から与えられる。
 *
 * M
 *
 *  D
 *
 *
 *     月を表す整数 M
 *
 * と日を表す整数 D
 *
 *     が空白区切りで与えられる。
 *
 * 出力
 *
 * 月が日で割り切れるなら YES 、割り切れないなら NO を出力せよ。出力の末尾には改行をつけること。
 * 入力例1
 * 1 1
 *
 * 出力例1
 * YES
 *
 * 入力例2
 * 2 29
 *
 * 出力例2
 * NO
 *
 * 入力例3
 * 12 6
 *
 * 出力例3
 * YES
 */
public class Main16A {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int m = sc.nextInt();
        int d = sc.nextInt();
        sc.close();
        System.out.println(m % d == 0 ? "YES" : "NO");
    }

}
