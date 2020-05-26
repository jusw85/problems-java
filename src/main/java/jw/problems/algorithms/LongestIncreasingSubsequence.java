package jw.problems.algorithms;

import java.util.*;

/**
 * LIS([1, 10, 2, 20, 3, 4, 5]) = [1, 2, 3, 4, 5]
 */
public class LongestIncreasingSubsequence {

    public static void main(String[] args) {
//        int[] x = { 1, -1, 2, 1000, 3, -1 };
//        int[] x = {1, 10, 2, 20, 3, 4, 5};
//        int[] x = {10, 1, 20, 2};
//        int[] x = {10, 1, 20, 2, 50};
//        int[] x = {10, 1, 20, 2, 50, 1000, 100, 2000, 200, 5000};
        int[] x = {2, 6, 3, 4, 1, 2, 9, 5, 8};

        Queue<Integer> q;
//        q = Recursive.lis(x);
//        q = DP.lis(x);
        q = DP2.lis(x);
        System.out.println(q);

        LIS.main(args);
    }

    private static class Recursive {
        public static Queue<Integer> lis(int[] x) {
            return lis(x, x.length - 1, Optional.empty());
        }

        private static Queue<Integer> lis(int[] x, int n, Optional<Integer> max) {
            if (n < 0) {
                return new LinkedList<>();
            }
            int elem = x[n];
            Queue<Integer> seqWithoutElem = lis(x, n - 1, max);
            if (!max.isPresent() || elem < max.get()) {
                Queue<Integer> seqWithElem = lis(x, n - 1, Optional.of(elem));
                seqWithElem.add(elem);
                if (seqWithElem.size() > seqWithoutElem.size()) {
                    return seqWithElem;
                }
            }
            return seqWithoutElem;
        }
    }

    /**
     * O(n2)
     * https://stackoverflow.com/a/2631810
     */
    private static class DP {

        /**
         * <pre>
         * x: [10, 1, 20, 2, 50]
         * dp: [1, 1, 2, 2, 3]
         * prev: [-1, -1, 0, 1, 2]
         *
         * dp[i] = length of LIS ending at x[i]
         * </pre>
         */
        private static Queue<Integer> lis(int[] x) {
            if (x.length < 0) {
                return new LinkedList<>();
            }
            int[] dp = new int[x.length];
            int[] prev = new int[x.length];
            int longest = -1;
            int longestIdx = -1;

            for (int i = 0; i < x.length; i++) {
                dp[i] = 1;
                prev[i] = -1;

                for (int j = 0; j < i; j++) {
                    if (dp[j] + 1 > dp[i] && x[j] < x[i]) {
                        dp[i] = dp[j] + 1;
                        prev[i] = j;
                    }
                }

                if (dp[i] > longest) {
                    longest = dp[i];
                    longestIdx = i;
                }
            }
//            System.out.println("x: " + Arrays.toString(x));
//            System.out.println("dp: " + Arrays.toString(dp));
//            System.out.println("prev: " + Arrays.toString(prev));
//            System.out.println(longest);
//            System.out.println(longestIdx);

            Deque<Integer> seq = new LinkedList<>();
            seq.addFirst(x[longestIdx]);
            for (int i = prev[longestIdx]; i >= 0; i = prev[i]) {
                seq.addFirst(x[i]);
            }
            return seq;
        }
    }


    /**
     * O(nlogn)
     * https://stackoverflow.com/a/2631810
     */
    private static class DP2 {

        /**
         * <pre>
         *
         * s[i] = for a LIS of length i, s[i] is the smallest value that terminates it
         * s[i] is always sorted
         *
         * x = [2 6 3 4 1 2 9 5 8]
         *
         * - - S = {} - Initialize S to the empty set
         * 2 - S = {2} - New largest LIS
         * 6 - S = {2, 6} - New largest LIS
         * 3 - S = {2, 3} - Changed 6 to 3
         * 4 - S = {2, 3, 4} - New largest LIS
         * 1 - S = {1, 3, 4} - Changed 2 to 1
         * 2 - S = {1, 2, 4} - Changed 3 to 2
         * 9 - S = {1, 2, 4, 9} - New largest LIS
         * 5 - S = {1, 2, 4, 5} - Changed 9 to 5
         * 8 - S = {1, 2, 4, 5, 8} - New largest LIS
         *
         * sIdx[i] = x-index to values in s
         * sIdx = {4, 5, 3, 7, 8}
         * prev[i] = previous x-index for LIS ending at x[i]
         * </pre>
         */
        private static Queue<Integer> lis(int[] x) {
            assert x != null && x.length > 0;

            List<Integer> s = new ArrayList<>(x.length);
            List<Integer> sIdx = new ArrayList<>(x.length);
            int[] prev = new int[x.length];
            for (int i = 0; i < x.length; i++) {
                if (s.isEmpty()) {
                    prev[i] = -1;
                    s.add(x[i]);
                    sIdx.add(i);
                } else if (x[i] > s.get(s.size() - 1)) {
                    prev[i] = sIdx.get(sIdx.size() - 1);
                    s.add(x[i]);
                    sIdx.add(i);
                } else {
                    int idx = binarySearch(s, x[i], 0, s.size() - 1);
                    prev[i] = prev[sIdx.get(idx)];
                    s.set(idx, x[i]);
                    sIdx.set(idx, i);
                }
            }
//            System.out.println("x: " + Arrays.toString(x));
//            System.out.println("s: " + s);
//            System.out.println("sIdx: " + sIdx);
//            System.out.println("prev: " + Arrays.toString(prev));

            Deque<Integer> seq = new LinkedList<>();
            int i = sIdx.get(sIdx.size() - 1);
            seq.addFirst(x[i]);
            for (i = prev[i]; i >= 0; i = prev[i]) {
                seq.addFirst(x[i]);
            }
            return seq;
        }

        private static int binarySearch(List<Integer> s, int i, int l, int r) {
            assert i < s.get(s.size() - 1);
            assert l <= r;
            while (l < r) {
                int m = (l + r) / 2;
                int sm = s.get(m);
                if (i < sm) {
                    r = m;
                } else {
                    l = m + 1;
                }
            }
            assert l == r;
            return l;
        }
    }

    /**
     * http://rosettacode.org/wiki/Longest_increasing_subsequence#Java
     * Class based approach
     *
     * A solution based on patience sorting, except that it is not necessary to keep the whole pile,
     * only the top (in solitaire, bottom) of the pile, along with pointers from each "card" to the
     * top of its "previous" pile.
     */
    private static class LIS {

        /**
         * Collections.binarySearch([10,20,30], 0) = -1
         * Collections.binarySearch([10,20,30], 15) = -2
         * Collections.binarySearch([10,20,30], 40) = -4
         * ~-1 = 0
         * ~-2 = 1
         * ~-4 = 3
         */
        public static <E extends Comparable<? super E>> List<E> lis(List<E> n) {
            List<Node<E>> pileTops = new ArrayList<>();
            // sort into piles
            for (E x : n) {
                Node<E> node = new Node<>();
                node.value = x;
                int i = Collections.binarySearch(pileTops, node);
                if (i < 0) i = ~i;
                if (i != 0)
                    node.pointer = pileTops.get(i - 1);
                if (i != pileTops.size())
                    pileTops.set(i, node);
                else
                    pileTops.add(node);
            }
            // extract LIS from nodes
            List<E> result = new ArrayList<E>();
            for (Node<E> node = pileTops.size() == 0 ? null : pileTops.get(pileTops.size() - 1);
                 node != null; node = node.pointer)
                result.add(node.value);
            Collections.reverse(result);
            return result;
        }

        private static class Node<E extends Comparable<? super E>> implements Comparable<Node<E>> {
            public E value;
            public Node<E> pointer;

            public int compareTo(Node<E> y) {
                return value.compareTo(y.value);
            }
        }

        public static void main(String[] args) {
            List<Integer> d = Arrays.asList(3, 2, 6, 4, 5, 1);
            System.out.printf("an L.I.S. of %s is %s\n", d, lis(d));
            d = Arrays.asList(0, 8, 4, 12, 2, 10, 6, 14, 1, 9, 5, 13, 3, 11, 7, 15);
            System.out.printf("an L.I.S. of %s is %s\n", d, lis(d));
        }
    }

}
