package jw.problems.challenge.challenge1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Question
 *
 * Examine the following random number generator:
 *
 * X_{n+1} = (A \times X_n + B) % M A, B, X_1, K and M will be given.
 *
 * Output the random numbers from K-th to K+4-th.
 * Input
 *
 * Input will be given in the following format from Standard Input
 *
 * A B X_1 K M
 *
 * Integer A(0≦A<M), Integer B(0≦B<M), Integer X_1(0≦X_1<M), Integer K(1≦K<10^9) and Integer M(2≦M≦10^4) will be given in one string.
 * Output
 *
 * Output the random numbers from K to K+4.
 * Also, make sure to insert a line break at the end of the output.
 * Input Example # 1
 *
 * 3 2 5 1 7
 *
 * Output Example #1
 *
 * 5
 * 3
 * 4
 * 0
 * 2
 *
 * The initial value of random number X_1 is 5.
 * X_2 = (3 \times 5 + 2) % 7 = 3
 * X_3 = (3 \times 3 + 2) % 7 = 4
 * X_4 = (3 \times 4 + 2) % 7 = 0
 * X_5 = (3 \times 0 + 2) % 7 = 2
 *
 * Input Example #2
 *
 * 1234 5678 123 12345 9876
 *
 * Output Example #2
 *
 * 4162
 * 6066
 * 5114
 * 5590
 * 414
 */
public class Main2 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        int b = sc.nextInt();
        int x = sc.nextInt();
        int k = sc.nextInt();
        int m = sc.nextInt();
        sc.close();

        List<Integer> list = new ArrayList<>(m);
        list.add(x);

        int seqStart = -1;
        for (int i = 1; i < k; i++) {
            x = getNextValue(a, x, b, m, k);
            if (list.contains(x)) {
                seqStart = list.indexOf(x);
                k -= seqStart;
                break;
            }
            list.add(x);
        }
        if (seqStart < 0) {
            for (int i = k; i <= k + 4; i++) {
                System.out.println(x);
                x = getNextValue(a, x, b, m, k);
            }
        } else {
            list = list.subList(seqStart, list.size());
            int start = (k - 1) % list.size();
            Integer[] arr = new Integer[list.size()];
            arr = list.toArray(arr);
            for (int i = start, j = 0; j < 5; i = (i + 1) % list.size(), j++) {
                System.out.println(arr[i]);
            }
        }
    }

    private static int getNextValue(int a, int x, int c, int m, int k) {
        return ((a * x) + c) % m;
    }

}