package jw.problems.atcoder;

import java.util.Scanner;

/**
 * https://atcoder.jp/contests/abc016/tasks/abc016_4
 *
 * D - 一刀両断
 *
 * Time Limit: 2 sec / Memory Limit: 256 MB
 * 問題文
 *
 * 高橋くんは鍛錬の結果、空手チョップで木の板を切断できるようになりました。空手チョップの軌道を表す線分と板の形を表す多角形が与えられるので、板がいくつに切断されたか求めてください。
 * 入力
 *
 * 入力は以下の形式で標準入力から与えられる。
 *
 * Ax
 *
 *  Ay
 *
 *  Bx
 *
 *  By
 *
 *
 * N
 *
 *
 * X1
 *
 *  Y1
 *
 *
 * X2
 *
 *  Y2
 *
 *
 * :
 * XN
 *
 *  YN
 *
 *
 *     1
 *
 * 行目には、線分の端点の座標 Ax,Ay,Bx,By
 * がスペース区切りで与えられる。
 * 2
 * 行目には、多角形の頂点数 N(3≦N≦100)
 * が与えられる。
 * 3
 * 行目からの N 行では、各頂点の座標 Xi,Yi
 *
 *     がスペース区切りで与えられる。
 *     入力で与えられる座標は-1000以上1000以下の整数である。
 *
 * 入力で与えられる線分と多角形は以下の性質を満たす。
 *
 *     多角形の頂点は反時計回りの順で与えられる。
 *     多角形の頂点は線分から0.1以上離れている。
 *     線分の端点は多角形から0.1以上離れている。
 *     線分の端点は多角形の外部にある。
 *     多角形の連続する３頂点が一直線上に並ぶことはない。
 *
 * すなわち、以下のような入力は与えられない。
 *
 *     A,B:多角形の頂点が線分上にある。
 *     C:線分の端点が多角形の辺上にある。
 *     D:多角形の辺と線分が重なる。
 *     E:線分の端点が多角形の内部にある。
 *
 * 出力
 *
 * 板がいくつに切断されるかを出力せよ。出力の末尾には改行をつけること。
 * 入力例1
 * -2 0 2 0
 * 4
 * 1 1
 * -1 1
 * -1 -1
 * 1 -1
 *
 * 出力例1
 * 2
 *
 * 入力例2
 * -3 1 3 1
 * 8
 * 2 2
 * 1 2
 * 1 0
 * -1 0
 * -1 2
 * -2 2
 * -2 -1
 * 2 -1
 *
 * 出力例2
 * 3
 */
public class Main16D {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Point p1 = new Point(sc.nextInt(), sc.nextInt());
        Point p2 = new Point(sc.nextInt(), sc.nextInt());
        Line slice = new Line(p1, p2);
        int numPoints = sc.nextInt();

        Point firstPoint = new Point(sc.nextInt(), sc.nextInt());
        Point prevPoint = new Point(firstPoint);
        int numIntersections = 0;
        for (int i = 1; i < numPoints; i++) {
            Point nextPoint = new Point(sc.nextInt(), sc.nextInt());
            if (isIntersecting(slice, new Line(prevPoint, nextPoint)))
                numIntersections++;
            prevPoint = nextPoint;
        }
        if (isIntersecting(slice, new Line(prevPoint, firstPoint)))
            numIntersections++;
        sc.close();
        System.out.println((numIntersections / 2) + 1);
    }

    private static boolean isIntersecting(Point ap1, Point ap2, Point bp1, Point bp2) {
        Vector ap1ap2 = ap2.sub(ap1);
        Vector ap1bp1 = bp1.sub(ap1);
        Vector ap1bp2 = bp2.sub(ap1);
        boolean aDividesB = Integer.signum(ap1ap2.cross(ap1bp1)) * Integer.signum(ap1ap2.cross(ap1bp2)) < 0;

        Vector bp1bp2 = bp2.sub(bp1);
        Vector bp1ap1 = ap1.sub(bp1);
        Vector bp1ap2 = ap2.sub(bp1);
        boolean bDividesA = Integer.signum(bp1bp2.cross(bp1ap1)) * Integer.signum(bp1bp2.cross(bp1ap2)) < 0;

        return aDividesB && bDividesA;
    }

    private static boolean isIntersecting(Line a, Line b) {
        return isIntersecting(a.p1, a.p2, b.p1, b.p2);
    }

    private static class Line {
        public Point p1;
        public Point p2;

        public Line(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
        }
    }

    private static class Vector {
        public int x;
        public int y;

        public Vector(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int cross(Vector v) {
            return x * v.y - v.x * y;
        }
    }

    private static class Point {
        public int x;
        public int y;

        public Point(Point p) {
            this.x = p.x;
            this.y = p.y;
        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Vector sub(Point p) {
            return new Vector(x - p.x, y - p.y);
        }
    }

    private static int ccw(Point p0, Point p1, Point p2) {
        int dx1 = (p1.x - p0.x), dy1 = (p1.y - p0.y);
        int dx2 = (p2.x - p0.x), dy2 = (p2.y - p0.y);
        if (dy1 * dx2 < dy2 * dx1)
            return 1;
        if (dy1 * dx2 > dy2 * dx1)
            return -1;
        if (dx1 * dx2 < 0 || dy1 * dy2 < 0)
            return -1;
        if ((Math.sqrt(dx1) + Math.sqrt(dy1)) >= (Math.sqrt(dx2) + Math.sqrt(dy2)))
            return 0;
        else
            return 1;
    }

}
