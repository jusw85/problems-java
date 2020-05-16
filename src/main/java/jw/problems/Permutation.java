package jw.problems;

import java.util.Arrays;

/**
 * Permutation implementations
 */
public class Permutation {

    public static void main(String[] args) {
//        Permutator p1 = new Simple1();
//        p1.permute("ABBC");

//        Simple2 p2 = new Simple2();
//        p2.permute("ABBC");
//        p2.permute("ABXYZCD", 2,4);

        Permutator p3 = new Lexicographic();
        p3.permute("CBBA");
    }

    private static class Simple1 implements Permutator {

        @Override
        public void permute(String str) {
            perm("", str);
        }

        private void perm(String prefix, String str) {
            int n = str.length();
            if (n == 0)
                System.out.println(prefix);
            else {
                for (int i = 0; i < n; i++)
                    perm(prefix + str.charAt(i),
                            str.substring(0, i) + str.substring(i + 1));
            }
        }
    }

    private static class Simple2 implements Permutator {

        @Override
        public void permute(String str) {
            int n = str.length();
            perm(new StringBuilder(str), 0, n - 1);
        }

        public void permute(String str, int l, int r) {
            perm(new StringBuilder(str), l, r);
        }

        private void perm(StringBuilder sb, int l, int r) {
            if (l == r) {
                System.out.println(sb);
            } else {
                for (int i = l; i <= r; i++) {
                    swap(sb, l, i);
                    perm(sb, l + 1, r);
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
        public void permute(String str) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);

            while (true) {
                printChars(chars);

                int i = chars.length - 2;
                while (i >= 0) {
                    if (chars[i] >= chars[i + 1]) i--;
                    else break;
                }
                if (i < 0) {
                    return;
                }

                int j = chars.length - 1;
                while (chars[i] >= chars[j]) {
                    j--;
                }

                swap(chars, i, j);
                reverse(chars, i + 1, chars.length - 1);
            }
        }

        private void reverse(char[] chars, int l, int r) {
            while (l < r) {
                swap(chars, l, r);
                l++;
                r--;
            }
        }

        private void swap(char[] chars, int i, int j) {
            char c = chars[i];
            chars[i] = chars[j];
            chars[j] = c;
        }

        private void printChars(char[] chars) {
            System.out.println(new String(chars));
        }
    }

    private interface Permutator {
        void permute(String str);
    }
}
