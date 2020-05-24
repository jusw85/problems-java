package jw.problems.algorithms;

import java.util.*;

/**
 * Permutation implementations
 */
public class Permutation {

    public static void main(String[] args) {
//        Permutator p1 = new Simple1();
//        System.out.println(p1.permute("ABBC"));

//        Simple2 p2 = new Simple2();
//        System.out.println(p2.permute("ABBC"));
//        System.out.println(p2.permute("ABXYZCD", 2, 4));

        Permutator p3 = new Lexicographic();
        System.out.println(p3.permute("3221"));

//        StringPermutationV1 perm = new StringPermutationV1("ABC");
//        int i = 0;
//        for (String s : perm) {
//            i++;
//            System.out.println(s);
//        }
//        System.out.println(i);
    }

    private static class Simple1 implements Permutator {

        @Override
        public List<String> permute(String str) {
            List<String> results = new ArrayList<>();
            perm("", str, results);
            return results;
        }

        private void perm(String prefix, String str, List<String> results) {
            int n = str.length();
            if (n == 0) {
                results.add(prefix);
            } else {
                for (int i = 0; i < n; i++)
                    perm(prefix + str.charAt(i),
                            str.substring(0, i) + str.substring(i + 1), results);
            }
        }
    }

    private static class Simple2 implements Permutator {

        @Override
        public List<String> permute(String str) {
            List<String> results = new ArrayList<>();
            int n = str.length();
            perm(new StringBuilder(str), 0, n - 1, results);
            return results;
        }

        public List<String> permute(String str, int l, int r) {
            List<String> results = new ArrayList<>();
            perm(new StringBuilder(str), l, r, results);
            return results;
        }

        private void perm(StringBuilder sb, int l, int r, List<String> results) {
            if (l == r) {
                results.add(sb.toString());
            } else {
                for (int i = l; i <= r; i++) {
                    swap(sb, l, i);
                    perm(sb, l + 1, r, results);
                    swap(sb, i, l);
                }
            }
        }

        private static void swap(StringBuilder sb, int i, int j) {
            char c = sb.charAt(i);
            sb.setCharAt(i, sb.charAt(j));
            sb.setCharAt(j, c);
        }
    }

    private static class Lexicographic implements Permutator {

        @Override
        public List<String> permute(String str) {
            return iterative(str);
//            return recursive(str);
        }

        public List<String> iterative(String str) {
            List<String> results = new ArrayList<>();
            char[] chars = str.toCharArray();
            Arrays.sort(chars);

            while (true) {
                results.add(new String(chars));

                int i = chars.length - 2;
                for (; i >= 0 && (chars[i] >= chars[i + 1]); i--) ;
                if (i < 0) {
                    break;
                }
//                chars[i+1] >= ... >= chars[length-1] is descending
                assert (chars[i] < chars[i + 1]);

                int j = chars.length - 1;
                for (; chars[i] >= chars[j]; j--) ;
//                chars[j] is the smallest number above chars[i], i+1 <= j < chars.length
                assert (j > i);
                assert (chars[j] > chars[i]);

                swap(chars, i, j);
                reverse(chars, i + 1, chars.length - 1);
            }
            return results;
        }

        public List<String> recursive(String str) {
            List<String> results = new ArrayList<>();
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            r(chars, 0, results);
            return results;
        }

        private void r(char[] chars, int idx, List<String> results) {
            if (idx >= chars.length - 1) {
                results.add(new String(chars));
                return;
            }
            while (true) {
                r(chars, idx + 1, results);
                int i = chars.length - 1;
                for (; i > idx && chars[i] <= chars[idx]; i--) ;
//                i <= idx || chars[i] > chars[idx]
                if (i == idx)
                    break;
                swap(chars, idx, i);
                reverse(chars, idx + 1, chars.length - 1);
            }
        }

        private void reverse(char[] chars, int l, int r) {
            while (l < r) {
                swap(chars, l++, r--);
            }
        }

        private void swap(char[] chars, int i, int j) {
            char c = chars[i];
            chars[i] = chars[j];
            chars[j] = c;
        }
    }

    private interface Permutator {
        List<String> permute(String str);
    }

    /**
     * Original
     * http://stackoverflow.com/a/13037291
     */
    public static class StringPermutationV1 implements Iterable<String> {

        protected final String string;

        public StringPermutationV1(String string) {
            this.string = string;
        }

        @Override
        public Iterator<String> iterator() {
            return new Iterator<String>() {

                char[] array = string.toCharArray();
                int length = string.length();
                int[] numRotations = (length == 0) ? null : new int[length];

                @Override
                public boolean hasNext() {
                    return numRotations != null;
                }

                /**
                 * Generate all permutations using the recursive relation:
                 * perm(0123) = perm(012) + 3, perm(301) + 2, perm(230) + 1, perm(123) + 0
                 * Foreach element i, rotating i-1 times will rotate each element from 0 to i-1 to position i
                 * Keep track of rotations in numRotations array
                 * Iteration for numRotations:
                 * 010
                 * 001
                 * 011
                 * 002
                 * 012
                 * 000
                 */
                @Override
                public String next() {

                    if (numRotations == null) throw new NoSuchElementException();

                    for (int i = 1; i < length; ++i) {
                        rotateForward(i);
                        ++numRotations[i];
                        for (int j = 0; j < i; ++j) {
                            assert numRotations[j] == 0;
                        }
                        if (numRotations[i] <= i) {
                            return new String(array);
                        }
                        numRotations[i] = 0;
                    }
                    numRotations = null;
                    return new String(array);
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }

                /**
                 * Rotate forward once from index 0 to i
                 * i = 2
                 * 012345
                 * 201345
                 */
                private void rotateForward(int i) {
                    char swap = array[i];
                    System.arraycopy(array, 0, array, 1, i);
                    array[0] = swap;
                }
            };
        }
    }

    /**
     * Different iteration order
     */
    public static class StringPermutationV2 implements Iterable<String> {

        protected final String str;

        public StringPermutationV2(String str) {
            this.str = str;
        }

        @Override
        public Iterator<String> iterator() {
            return new StringPermutationIterator(str);
        }

        private class StringPermutationIterator implements Iterator<String> {

            char[] array;
            int length;
            int[] numRotations;

            public StringPermutationIterator(String str) {
                array = str.toCharArray();
                length = str.length();
                numRotations = (length == 0) ? null : new int[length];
            }

            @Override
            public boolean hasNext() {
                return numRotations != null;
            }

            /**
             * Iteration for numRotations:
             * 001
             * 002
             * 010
             * 011
             * 012
             * 000
             */
            @Override
            public String next() {

                if (numRotations == null) throw new NoSuchElementException();

                for (int i = length - 1; i >= 1; i--) {
                    rotateForward(i);
                    ++numRotations[i];
                    if (numRotations[i] <= i) {
                        return new String(array);
                    }
                    numRotations[i] = 0;
                }
                numRotations = null;
                return new String(array);
            }

            private void rotateForward(int i) {
                char swap = array[i];
                System.arraycopy(array, 0, array, 1, i);
                array[0] = swap;
            }

            private void rotateBackward(int i) {
                char swap = array[0];
                System.arraycopy(array, 1, array, 0, i);
                array[i] = swap;
            }
        }
    }
}
