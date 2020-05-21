package jw.problems.interview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created these interview questions for screening candidates
 * https://www.interviewzen.com/dashboard/8DvfKF
 *
 * A random number generator is governed by the following formula:
 * X(n+1) = (A * X(n) + B) % M
 *
 * Write a method that will return an integer array of
 * elements X(k) to X(k + i)
 *
 * For example, given:
 * X(1) = 5, A = 3, B = 2, M = 7, k = 3, i = 1
 *
 * X(2) = (3 * 5 + 2) % 7 = 3
 * X(3) = (3 * 3 + 2) % 7 = 4
 * X(4) = (3 * 4 + 2) % 7 = 0
 *
 * Result = {X(3), X(4)}
 * = {4, 0}
 *
 * You may assume the following limits:
 * Integer A:  0 <= A <= M
 * Integer B:  0 <= B <= M
 * Integer X1: 0 <= X1 <= M
 * Integer M:  2 <= M <= 10^4
 * Integer k:  1 <= k <= 10^9
 * Integer i:  0 <= i <= 10
 *
 * Time Limit: 2sec
 * Memory Limit: 256MB
 *
 * The method should be compilable on OpenJDK 1.8.0_92 without
 * any additional libraries.
 *
 * You are allowed to create additional methods to structure your code.
 * You may use the below method signature.
 *
 * You are advised not to spend more than 30 minutes on this question.
 *
 * public int[] rng(int A, int B, int M, int X1, int k, int i) {
 * return new int[0];
 * }
 */
public class Question2 {

    public static void main(String[] args) {
//        in: 1234 5678 123 9876 12345 5
//        out: 4162 6066 5114 5590 414
//        in: 3 2 5 7 3 2
//        out: 4 0
        V2.main(args);
    }

    private static class V2 {
        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            int a = sc.nextInt();
            int b = sc.nextInt();
            int x1 = sc.nextInt();
            int m = sc.nextInt();
            int offset = sc.nextInt();
            int length = sc.nextInt();
            sc.close();

            int[] results = rng(a, b, m, x1, offset, length);
            System.out.println(Arrays.toString(results));
        }

        public static int[] rng(int a, int b, int m, int x, int offset, int length) {
            List<Integer> rngSequence = new ArrayList<>(m);
            rngSequence.add(x);

            int loopStart = -1;
            for (int i = 1; i < offset; i++) {
                x = getNextValue(a, x, b, m);
                if ((loopStart = rngSequence.indexOf(x)) >= 0) {
                    break;
                }
                rngSequence.add(x);
            }

            if (loopStart < 0) {
                for (int i = offset; i < offset + length; i++) {
                    System.out.println(x);
                    x = getNextValue(a, x, b, m);
                }
            } else {
                rngSequence = rngSequence.subList(loopStart, rngSequence.size());
                int start = (offset - loopStart - 1) % rngSequence.size();

                for (int i = start, j = 0;
                     j < length;
                     i = (i + 1) % rngSequence.size(), j++) {
                    System.out.println(rngSequence.get(i));
                }
            }
            return new int[0];
        }

        private static int getNextValue(int a, int x, int b, int m) {
            return ((a * x) + b) % m;
        }
    }

    private static class V1 {
        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            int a = sc.nextInt();
            int b = sc.nextInt();
            int x = sc.nextInt();
            int m = sc.nextInt();
            int k = sc.nextInt();
            sc.close();

            for (int i = 1; i < k; i++) {
                x = getNextValue(a, x, b, m);
            }
            for (int i = k; i <= k + 4; i++) {
                System.out.println(x);
                x = getNextValue(a, x, b, m);
            }
        }

        private static int getNextValue(int a, int x, int c, int m) {
            return ((a * x) + c) % m;
        }
    }
}
