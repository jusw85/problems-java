package jw.problems.adventofcode.aoc2016;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * http://adventofcode.com/2016/day/6
 *
 * --- Day 6: Signals and Noise ---
 *
 * Something is jamming your communications with Santa. Fortunately, your signal is only partially jammed, and protocol in situations like this is to switch to a simple repetition code to get the message through.
 *
 * In this model, the same message is sent repeatedly. You've recorded the repeating message signal (your puzzle input), but the data seems quite corrupted - almost too badly to recover. Almost.
 *
 * All you need to do is figure out which character is most frequent for each position. For example, suppose you had recorded the following messages:
 *
 * eedadn
 * drvtee
 * eandsr
 * raavrd
 * atevrs
 * tsrnev
 * sdttsa
 * rasrtv
 * nssdts
 * ntnada
 * svetve
 * tesnvt
 * vntsnd
 * vrdear
 * dvrsen
 * enarar
 *
 * The most common character in the first column is e; in the second, a; in the third, s, and so on. Combining these characters returns the error-corrected message, easter.
 *
 * Given the recording in your puzzle input, what is the error-corrected version of the message being sent?
 *
 * Your puzzle answer was dzqckwsd.
 * --- Part Two ---
 *
 * Of course, that would be the message - if you hadn't agreed to use a modified repetition code instead.
 *
 * In this modified code, the sender instead transmits what looks like random data, but for each character, the character they actually want to send is slightly less likely than the others. Even after signal-jamming noise, you can look at the letter distributions in each column and choose the least common letter to reconstruct the original message.
 *
 * In the above example, the least common character in the first column is a; in the second, d, and so on. Repeating this process for the remaining characters produces the original message, advent.
 *
 * Given the recording in your puzzle input and this new decoding methodology, what is the original message that Santa is trying to send?
 *
 * Your puzzle answer was lragovly.
 */
public class Day6 {

    public static void main(String[] args) throws FileNotFoundException {
        Map<Character, Integer>[] maps = new LinkedHashMap[8];
        for (int i = 0; i < maps.length; i++) {
            maps[i] = new LinkedHashMap<>();
            for (char c = 'a'; c <= 'z'; c++) {
                maps[i].put(c, 0);
            }
        }

        Scanner sc = new Scanner(new File("./etc/aoc2016/in6"));
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            for (int i = 0; i < 8; i++) {
                char c = line.charAt(i);
                maps[i].put(c, maps[i].get(c) + 1);
            }
        }
        sc.close();

        for (int i = 0; i < 8; i++) {
            printLargest(maps[i]);
        }
        System.out.println();
        for (int i = 0; i < 8; i++) {
            printSmallest(maps[i]);
        }
    }

    public static void printLargest(Map<Character, Integer> map) {
        List<Map.Entry<Character, Integer>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, Comparator.comparing(Map.Entry::getValue));
        System.out.print(list.get(list.size() - 1).getKey());
    }

    public static void printSmallest(Map<Character, Integer> map) {
        List<Map.Entry<Character, Integer>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, Comparator.comparing(Map.Entry::getValue));
        System.out.print(list.get(0).getKey());
    }

}