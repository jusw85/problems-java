package jw.problems.interview;

import java.util.*;

/**
 * Created these interview questions for screening candidates
 * https://www.interviewzen.com/dashboard/8DvfKF
 *
 * Given a string of lowercase characters, write a method that will
 * return an array of characters in descending order of frequency.
 * If multiple characters have the same the number of occurrences, output
 * them in alphabetical order.
 *
 * For example, given:
 * S = dcbadcbadcbadde
 *
 * Result = {'d', 'a', 'b', 'c', 'e'}
 * 'd' occurs 5 times,
 * 'a', 'b', 'c' occurs 3 times, so they are ordered alphabetically,
 * 'e' occurs 1 time
 *
 * You may assume the following limits:
 * String S: 0 <= |S| <= 1000
 * For each c in S: 'a' <= c <= 'z'
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
 * public char[] charFrequency(String s) {
 * return new char[0];
 * }
 */

public class Question1 {

    public static void main(String[] args) {
//        in: dcbadcbadcbadde
//        out: [d, a, b, c, e]
        V2.main(args);
    }

    private static class V2 {
        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            String str = sc.nextLine();
            sc.close();

            char[] chars = charFrequency(str);
            System.out.println(Arrays.toString(chars));
        }

        public static char[] charFrequency(String s) {
            Map<Character, Integer> map = new HashMap<>();
            for (char c : s.toCharArray()) {
                map.put(c, map.getOrDefault(c, 0) + 1);
            }

            List<Map.Entry<Character, Integer>> list = new ArrayList<>(map.entrySet());
            list.sort((o1, o2) -> {
                int valCompare = o2.getValue().compareTo(o1.getValue());
                if (valCompare != 0)
                    return valCompare;
                return o1.getKey().compareTo(o2.getKey());
            });

            char[] chars = new char[list.size()];
            for (int i = 0; i < list.size(); i++) {
                chars[i] = list.get(i).getKey();
            }
            return chars;
        }
    }

    private static class V1 {
        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            String str = sc.nextLine();
            sc.close();

            Map<Character, Integer> map = new LinkedHashMap<>();
            for (char c = 'a'; c <= 'z'; c++) {
                map.put(c, 0);
            }

            for (char c : str.toCharArray()) {
                map.put(c, map.get(c) + 1);
            }

            List<Map.Entry<Character, Integer>> list = new ArrayList<>(
                    map.entrySet());
            Collections.sort(list, Collections
                    .reverseOrder(new Comparator<Map.Entry<Character, Integer>>() {
                        @Override
                        public int compare(Map.Entry<Character, Integer> o1,
                                           Map.Entry<Character, Integer> o2) {
                            return o1.getValue().compareTo(o2.getValue());
                        }
                    }));

            for (Map.Entry<Character, Integer> entry : list) {
                if (entry.getValue() > 0) {
                    System.out.println(entry.getKey());
                }
            }
        }
    }
}
