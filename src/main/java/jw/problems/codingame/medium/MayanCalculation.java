package jw.problems.codingame.medium;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

/**
 * https://www.codingame.com/ide/puzzle/mayan-calculation
 *
 * The Goal
 * Upon discovering a new Maya site, hundreds of mathematics, physics and astronomy books have been uncovered.
 *
 * The end of the world might arrive sooner than we thought, but we need you to be sure that it doesn't!
 *
 * Thus, in order to computerize the mayan scientific calculations, you're asked to develop a program capable of performing basic arithmetical operations between two mayan numbers.
 * Rules
 * The mayan numerical system contains 20 numerals going from 0 to 19. Here's an ASCII example of their representation:
 * 0 	1 	2 	3 	4 	5 	6 	7 	8 	9
 * .oo.
 * o..o
 * .oo.
 * .... 	o...
 * ....
 * ....
 * .... 	oo..
 * ....
 * ....
 * .... 	ooo.
 * ....
 * ....
 * .... 	oooo
 * ....
 * ....
 * .... 	....
 * ----
 * ....
 * .... 	o...
 * ----
 * ....
 * .... 	oo..
 * ----
 * ....
 * .... 	ooo.
 * ----
 * ....
 * .... 	oooo
 * ----
 * ....
 * ....
 * 10 	11 	12 	13 	14 	15 	16 	17 	18 	19
 * ....
 * ----
 * ----
 * .... 	o...
 * ----
 * ----
 * .... 	oo..
 * ----
 * ----
 * .... 	ooo.
 * ----
 * ----
 * .... 	oooo
 * ----
 * ----
 * .... 	....
 * ----
 * ----
 * ---- 	o...
 * ----
 * ----
 * ---- 	oo..
 * ----
 * ----
 * ---- 	ooo.
 * ----
 * ----
 * ---- 	oooo
 * ----
 * ----
 * ----
 * A mayan number is divided into vertical sections. Each section contains a numeral (from 0 to 19) representing a power of 20. The lowest section corresponds to 200, the one above to 201 and so on.
 *
 * Thereby, the example below is : (12 x 20 x 20) + (0 x 20) + 5 = 4805
 *
 *
 * To spice the problem up, the mayans used several dialects, and the graphical representation of the numerals could vary from one dialect to another.
 *
 * The representation of the mayan numerals will be given as the input of your program in the form of ASCII characters. You will have to display the result of the operation between the two given mayan numbers. The possible operations are *, /, +, -
 * Game Input
 * Input
 *
 * Line 1: the width L and height H of a mayan numeral.
 *
 * H next lines: the ASCII representation of the 20 mayan numerals. Each line is (20 x L) characters long.
 *
 * Next line: the amount of lines S1 of the first number.
 *
 * S1 next lines: the first number, each line having L characters.
 *
 * Next line: the amount of lines S2 of the second number.
 *
 * S2 next lines: the second number, each line having L characters.
 *
 * Last line: the operation to carry out: *, /, +, or -
 * Output
 * The result of the operation in mayan numeration, H lines per section, each line having L characters.
 * Constraints
 * 0 < L, H < 100
 * 0 < S1, S2 < 1000
 * The remainder of a division is always 0.
 * The mayan numbers given as input will not exceed 263.
 */
public class MayanCalculation {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int L = in.nextInt();
        int H = in.nextInt();

        int numLetters = 20;
        StringBuilder[] sbs = new StringBuilder[numLetters];
        for (int i = 0; i < sbs.length; i++) {
            sbs[i] = new StringBuilder();
        }
        for (int i = 0; i < H; i++) {
            StringBuilder line = new StringBuilder(in.next());
            for (int j = 0; j < sbs.length; j++) {
                int startIdx = j * L;
                int endIdx = startIdx + L;
                sbs[j].append(line.substring(startIdx, endIdx));
            }
        }

        Map<Integer, String> numStrMap = new HashMap<>();
        Map<String, Integer> strNumMap = new HashMap<>();
        for (int i = 0; i < sbs.length; i++) {
            numStrMap.put(i, sbs[i].toString());
            strNumMap.put(sbs[i].toString(), i);
        }

        int S1 = in.nextInt();
        long n1 = strToNum(in, S1, strNumMap, H);
        int S2 = in.nextInt();
        long n2 = strToNum(in, S2, strNumMap, H);

        String operation = in.next();
        long result = 0L;
        switch (operation) {
            case "+":
                result = n1 + n2;
                break;
            case "-":
                result = n1 - n2;
                break;
            case "*":
                result = n1 * n2;
                break;
            case "/":
                result = n1 / n2;
                break;
        }

        System.out.println(numToStr(numStrMap, result, H, L));
    }

    public static String numToStr(Map<Integer, String> numStrMap, long num, int H, int L) {
        Stack<Integer> st = new Stack<>();
        while (num > 0) {
            st.add((int) (num % 20));
            num /= 20;
        }
        if (st.isEmpty()) {
            st.add(0);
        }

        StringBuilder sb = new StringBuilder();
        while (!st.empty()) {
            String str = numStrMap.get(st.pop());
            for (int i = 0; i < H; i++) {
                int startIdx = i * L;
                int endIdx = startIdx + L;
                sb.append(str.substring(startIdx, endIdx));
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }

    public static long strToNum(Scanner in, int numLines, Map<String, Integer> strNumMap, int H) {
        int len = numLines / H;
        int[] nums = new int[len];
        for (int i = 0; i < nums.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < H; j++) {
                sb.append(in.next());
            }
            nums[i] = strNumMap.get(sb.toString());
        }

        long n = 0;
        for (int i = 0; i < nums.length; i++) {
            n *= 20;
            n += nums[i];
        }
        return n;
    }
}
