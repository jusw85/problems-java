package jw.problems.codingame.medium;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * https://www.codingame.com/ide/puzzle/conway-sequence
 *
 * The Goal
 * You mission is to print a specific line of the Conway sequence.
 * Rules
 *
 * Warning! This sequence can make you ill. The reasoning is simple but unusual: Read a line aloud whilst looking at the line above and you will notice that each line (except the first) makes ​​an inventory of the previous line.
 *
 * 1
 * 1 1
 * 2 1
 * 1 2 1 1
 * 1 1 1 2 2 1
 * 3 1 2 2 1 1
 * ...
 *
 * - Line 3 shows 2 1 because the line 2 contains two 1, one after the other.
 * - Line 4 displays 1 2 1 1 because the line 3 contains one 2 followed by one 1.
 * - Line 5 displays 1 1 1 2 2 1 because the line 4 contains one 1 followed by one 2 followed by two 1.
 *
 * This sequence refers to a technique used to encode ranges in order to compress identical values ​​without losing any information. This type of method is used, amongst others, to compress images.
 *
 * Your mission is to write a program that will display the line L of this series on the basis of an original number R (R equals 1 in our example).
 * Game Input
 * Input
 *
 * Line 1: The original number R of the sequence.
 *
 * Line 2: The line L to display. The index of the first line is 1.
 * Output
 * The line L of the sequence. Each element of the sequence is separated by a space.
 * Constraints
 * 0 < R < 100
 * 0 < L ≤ 25
 */
public class ConwaySequence {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int R = in.nextInt();
        int L = in.nextInt();

        LinkedList<Integer> q = new LinkedList<>();
        q.add(R);
        q.add(-1);
        for (int i = 0; i < L - 1; i++) {
            int prev = q.poll();
            int curr = 0;
            int count = 1;
            while (curr >= 0) {
                curr = q.poll();
                if (prev == curr) {
                    count++;
                } else {
                    q.add(count);
                    q.add(prev);
                    prev = curr;
                    count = 1;
                }
            }
            q.add(-1);
        }
        q.removeLast();
        String prefix = "";
        for (int i : q) {
            System.out.print(prefix + i);
            prefix = " ";
        }
    }

}
