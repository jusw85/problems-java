package jw.problems.challenge.challenge1;

import java.util.*;

/**
 * Question
 *
 * You will be given a character string S, which only consists of half width characters.
 *
 * Output each character in descending order of the most frequent occurrence, separating each with a newline.
 * However, if there multiple alphabets have same number of occurrence, output them in alphabetical order.
 * Input
 *
 * Input will be given in the following format from Standard Input:
 *
 * S
 *
 * A character string S(1≦|S|≦1,000) will be given as one string.
 *
 * Define the length of the string as |S|
 * S consists of 26 alphabets from a to z with half width.
 *
 * Output
 *
 * Output each character in descending order of the most frequent occurrence, separating each with a newline.
 * However, if there multiple alphabets have same number of occurrence, output them in alphabetical order.
 * Also, make sure to insert a line break at the end of the output.
 * Input Example # 1
 *
 * dcbadcbadcbadde
 *
 * Output Example #1
 *
 * d
 * a
 * b
 * c
 * e
 *
 * Output d, the most frequently occurring value, which occurs 5 times.
 * Then, a,b, and c occur 3 times, therefore output them in alphabetical order; a,b and c.
 * Lastly, output e,which occurs 1 time.
 * Do not output that is not contained in the character string S.
 *
 * Input Example #2
 *
 * ajklfajdlkfajsdklfjalljaklsdfjaklsdjf
 *
 * Output Example #2
 *
 * j
 * l
 * a
 * f
 * k
 * d
 * s
 *
 * Input Example #3
 *
 * z
 *
 * Output Example #3
 *
 * z
 */
public class Main1 {

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