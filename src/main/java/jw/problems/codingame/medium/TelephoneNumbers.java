package jw.problems.codingame.medium;

import java.util.Scanner;

/**
 * https://www.codingame.com/ide/puzzle/telephone-numbers
 *
 * The Goal
 * By joining the iDroid smartphone development team, you have been given the responsibility of developing the contact manager. Obviously, what you were not told is that there are strong technical constraints for iDroid: the system doesn’t have much memory and the processor is as fast as a Cyrix from the 90s...
 *
 * In the specifications, there are two points in particular that catch your attention:
 *
 * 1. Intelligent Assistance for entering numbers
 * The numbers corresponding to the first digits entered will be displayed to the user almost instantly.
 *
 *
 * 2. Number storage optimization
 * First digits which are common to the numbers should not be duplicated in the memory.
 *
 * Fortunately, the specifications also have this little chart to guide you in the implementation:
 *
 *
 * Fig 1. Structure of data to stock phone numbers on iDroid
 *
 * Your task is to write a program that displays the number of items (which are numbers) required to store a list of telephone numbers with the structure presented above.
 * Game Input
 * Input
 *
 * Line 1: The number N of telephone numbers.
 *
 * N following lines: Each line contains a phone number, with a maximum length L. Telephone numbers consist of only the digits 0 to 9 included, without any spaces.
 * Output
 * The number of elements (referencing a number) stored in the structure.
 * Constraints
 * 0 ≤ N ≤ 10000
 * 2 ≤ L ≤ 20
 */
public class TelephoneNumbers {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();

        Node root = new Node();
        int numNodes = 0;
        for (int i = 0; i < N; i++) {
            String telephone = in.next();
            Node curr = root;
            for (char c : telephone.toCharArray()) {
                int idx = c - '0';
                if (curr.child[idx] == null) {
                    Node n = new Node();
                    curr.child[idx] = n;
                    numNodes++;
                }
                curr = curr.child[idx];
            }
        }
        System.out.println(numNodes);

    }

    public static class Node {
        public Node[] child = new Node[10];
    }

}
