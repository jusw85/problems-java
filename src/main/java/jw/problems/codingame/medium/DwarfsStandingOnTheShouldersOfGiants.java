package jw.problems.codingame.medium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * https://www.codingame.com/ide/puzzle/dwarfs-standing-on-the-shoulders-of-giants
 *
 * The Goal
 * The saying "Dwarfs standing on the shoulders of giants" refers to the importance of being able to build upon the work of our predecessors.
 *
 * When we read texts, we often only get a small glance of this dependence: this person influenced that person. Thereafter, we learn that the second person, in turn influenced a third and so on. In this exercise we’re interested in the chain of influence and more precisely in finding the longest possible chain.​
 * Rules
 *
 * We choose to represent each person by a distinct integer. If person #1 has influenced persons #2 and #3 and person #3 has influenced #4 then there is a succession of thoughts between #1, #3 and #4. In this case, it’s the longest succession and the expected result will be 3, since it involves 3 people.
 *
 *
 * If we were to complete this example when we learn that person #2 also influenced persons #4 and #5, then the longest succession will still have a length of 3, but there will now be several of them.
 *
 *
 * If we now add that person #10 influenced person #11, the result remains 3. However, as soon as we learn that #10 also influenced #1 and #3, then the result becomes 4, since there is now a succession involving 4 people, which is #10, #1, #2, #5.
 *
 *
 * Note: It takes time for a thought to influence others. So, we will suppose that it is not possible to have a mutual influence between people, i.e. If A influences B (even indirectly through other people), then B will not influence A (even indirectly). Also, you can not influence yourself.
 * Game Input
 * Input
 *
 * Line 1: The number N of relationships of influence.
 *
 * N following lines: a relationship of influence between two people in the form of X (whitespace) Y, which indicates that X influences Y. The relationships of influence are listed in any order.
 * Output
 * The number of people involved in the longest succession of influences.
 * Constraints
 * 0 < N < 10000
 * 0 < X, Y < 10000
 */
public class DwarfsStandingOnTheShouldersOfGiants {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();

        Map<Integer, List<Integer>> parentsMap = new HashMap<>();
        Map<Integer, Integer> lengths = new HashMap<>();

        for (int i = 0; i < n; i++) {
            int x = in.nextInt();
            int y = in.nextInt();

            if (!parentsMap.containsKey(y)) {
                Integer[] parents = {x};
                parentsMap.put(y, new ArrayList<>(Arrays.asList(parents)));
                lengths.put(y, 1);
            } else {
                List<Integer> parents = parentsMap.get(y);
                parents.add(x);
            }

            int newlen = lengths.get(y) + 1;
            if (!parentsMap.containsKey(x)) {
                parentsMap.put(x, new ArrayList<>());
                lengths.put(x, newlen);
            } else {
                propagateLength(parentsMap, lengths, x, newlen);
            }
        }

        int max = Integer.MIN_VALUE;
        for (int len : lengths.values()) {
            max = Math.max(len, max);
        }
        System.out.println(max);
    }

    public static void propagateLength(Map<Integer, List<Integer>> parentsMap,
                                       Map<Integer, Integer> lengths, int node, int newlen) {
        int oldlen = lengths.get(node);
        if (newlen > oldlen) {
            lengths.put(node, newlen);
            List<Integer> parents = parentsMap.get(node);
            for (int parent : parents) {
                propagateLength(parentsMap, lengths, parent, newlen + 1);
            }
        }
    }

}
