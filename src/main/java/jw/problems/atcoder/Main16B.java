package jw.problems.atcoder;

import java.util.Scanner;

/**
 * https://atcoder.jp/contests/abc016/tasks/abc016_2
 *
 * B - A±B Problem
 *
 * Time Limit: 2 sec / Memory Limit: 256 MB
 * 問題文
 *
 * 高橋くんはプログラミングコンテストで A+B を出力するプログラムを書いたつもりでしたが、 A-B を出力するプログラムを書いたような気がしてきて不安になりました。高橋くんは書いたプログラムをうっかり消してしまって入出力しか残っていません。高橋くんが書いたプログラムが A+B なのか、 A-B なのか、どちらの可能性もあるか、どちらでもないかを判定してください。
 *
 * ただし、 A+B と A-B のどちらかの可能性しかない場合、そのプログラムを書いたと判定します。例えば 1 1 という入力に対して 2 を出力しているなら、 A+B の可能性はあるが A-B の可能性はないので、高橋くんは A+B を書いたと判定してください。
 * 入力
 *
 * 入力は以下の形式で標準入力から与えられる。
 *
 * A
 *
 *  B
 *
 *  C
 *
 *
 *     高橋くんのプログラムに対する入力を表す整数 A,B(0≦A,B≦100)
 *
 * と 高橋くんのプログラムの出力を表す整数 C(0≦C≦100)
 *
 *     がスペース区切りで与えられる。
 *
 * 出力
 *
 * 高橋くんが書いたプログラムが A+B だとわかるなら + 、 A-B だとわかるなら - 、どちらの可能性もあるなら ? 、 どちらでもないなら ! を出力せよ。出力の末尾には改行をつけること。
 * 入力例1
 * 1 0 1
 *
 * 出力例1
 * ?
 *
 * A+B と A-B のどちらの可能性もあります。
 * 入力例2
 * 1 1 2
 *
 * 出力例2
 * +
 *
 * A+B であり、 A-B ではないと判定してください。
 * 入力例3
 * 1 1 0
 *
 * 出力例3
 * -
 *
 * A+B ではなく、 A-B だと判定してください。
 * 入力例4
 * 1 1 1
 *
 * 出力例4
 * !
 *
 * A+B と A-B のどちらでもありません。
 */
public class Main16B {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        int b = sc.nextInt();
        int c = sc.nextInt();
        sc.close();
        boolean isAddition = (a + b) == c;
        boolean isDeduction = (a - b) == c;
        if (isAddition && isDeduction)
            System.out.println('?');
        else if (isAddition)
            System.out.println('+');
        else if (isDeduction)
            System.out.println('-');
        else
            System.out.println('!');
    }

}
