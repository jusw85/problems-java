package jw.problems.interview;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created these interview questions for screening candidates
 * https://www.interviewzen.com/dashboard/8NWv2B
 *
 * You are given a non-empty integer array A, and a duplicated array B
 * with zero or more elements removed i.e. B is a subset of A.
 *
 * Your job is to determine the elements removed from B, and return the
 * results in an integer array. The order of elements in the returned
 * result is not important.
 *
 * For example, given:
 * A = [1, 1, -1, 4, 6, 10]
 * B = [6, 1, 10]
 * Result = [1, -1, 4]
 *
 * You may assume the following limits:
 * len(A):  0 < len(A) < 1024
 * len(B):  0 <= len(B) <= len(A)
 *
 * Time Limit: 2sec
 * Memory Limit: 256MB
 *
 * The method should be compilable on OpenJDK 1.8.0_92 without
 * any additional libraries.
 *
 * You are allowed to create additional methods to structure your code.
 * You may use the below method template.
 *
 * You are advised not to spend more than 20 minutes on this question.
 *
 * public int[] missingElements(int[] a, int[] b) {
 * // WRITE YOUR CODE HERE
 * return new int[0];
 * }
 */
public class Question3 {

    public static void main(String[] args) {
        int[] a = {1, 1, -1, 4, 6, 10};
        int[] b = {6, 1, 10};
        int[] x = missingElements(a, b);

        System.out.println(Arrays.toString(x));
    }

    public static int[] missingElements(int[] a, int[] b) {
        Map<Integer, Integer> map = new HashMap();
        for (int i : a) {
            if (map.containsKey(i)) {
                map.put(i, map.get(i) + 1);
            } else {
                map.put(i, 1);
            }
        }
        for (int i : b) {
            if (map.containsKey(i)) {
                int newCount = map.get(i) - 1;
                if (newCount <= 0) {
                    map.remove(i);
                } else {
                    map.put(i, newCount);
                }
            }
        }
        return map.keySet().stream().mapToInt(i -> i).toArray();
    }
}
