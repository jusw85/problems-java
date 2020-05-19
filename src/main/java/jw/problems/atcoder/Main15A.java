package jw.problems.atcoder;

import java.util.Scanner;

/**
 * https://atcoder.jp/contests/abc015/tasks/abc015_1
 *
 * A - 高橋くんの研修
 *
 * Time Limit: 2 sec / Memory Limit: 256 MB
 * 問題文
 *
 * 高橋くんは、ソフトウェア会社に勤めています。 その会社では、短い変数名はバグを生む原因だと信じられており、長い変数名を使う習慣があります。
 *
 * いま高橋くんは 2
 *
 * つの変数名を思いつきましたが、残念なことにその長さを見分けることが出来ません。
 *
 * いろんな意味で可哀想な彼の代わりに、与えられた 2
 *
 * つの小文字アルファベットのみからなる文字列のうち、文字数が長い方の文字列を求めてください。
 * 入力
 *
 * 入力は以下の形式で標準入力から与えられる。
 *
 * A
 *
 *
 * B
 *
 *
 *     1
 *
 * 行目には、文字列 A(1≦|A|≦50)
 * が与えられる。
 * 2
 * 行目には、文字列 B(1≦|B|≦50)
 * が与えられる。
 * 文字列 A,B
 * には、小文字アルファベットのみが含まれることが保証されている。
 * 文字列 A,B
 *
 *     の長さは異なることが保証されている。
 *
 * 出力
 *
 * 文字数が長い方の文字列を 1
 *
 * 行で出力せよ。出力の末尾にも改行をいれること。
 * 入力例1
 * isuruu
 * isleapyear
 *
 * 出力例1
 * isleapyear
 *
 * isuruuは 6
 * 文字、isleapyearは 10
 *
 * 文字であるため、isleapyearを出力します。
 * 入力例2
 * ttttiiiimmmmeeee
 * time
 *
 * 出力例2
 * ttttiiiimmmmeeee
 *
 * このような変数名は邪悪ですが、彼の所属する会社では正義です。
 */
public class Main15A {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s1 = sc.next();
        String s2 = sc.next();
        sc.close();
        System.out.println(s1.length() >= s2.length() ? s1 : s2);
    }

}
