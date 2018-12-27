package jw.problems.aoc2015;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http://adventofcode.com/2015/day/16
 * --- Day 16: Aunt Sue ---
 *
 * Your Aunt Sue has given you a wonderful gift, and you'd like to send her a thank you card. However, there's a small problem: she signed it "From, Aunt Sue".
 *
 * You have 500 Aunts named "Sue".
 *
 * So, to avoid sending the card to the wrong person, you need to figure out which Aunt Sue (which you conveniently number 1 to 500, for sanity) gave you the gift. You open the present and, as luck would have it, good ol' Aunt Sue got you a My First Crime Scene Analysis Machine! Just what you wanted. Or needed, as the case may be.
 *
 * The My First Crime Scene Analysis Machine (MFCSAM for short) can detect a few specific compounds in a given sample, as well as how many distinct kinds of those compounds there are. According to the instructions, these are what the MFCSAM can detect:
 *
 * children, by human DNA age analysis.
 * cats. It doesn't differentiate individual breeds.
 * Several seemingly random breeds of dog: samoyeds, pomeranians, akitas, and vizslas.
 * goldfish. No other kinds of fish.
 * trees, all in one group.
 * cars, presumably by exhaust or gasoline or something.
 * perfumes, which is handy, since many of your Aunts Sue wear a few kinds.
 *
 * In fact, many of your Aunts Sue have many of these. You put the wrapping from the gift into the MFCSAM. It beeps inquisitively at you a few times and then prints out a message on ticker tape:
 *
 * children: 3
 * cats: 7
 * samoyeds: 2
 * pomeranians: 3
 * akitas: 0
 * vizslas: 0
 * goldfish: 5
 * trees: 3
 * cars: 2
 * perfumes: 1
 *
 * You make a list of the things you can remember about each Aunt Sue. Things missing from your list aren't zero - you simply don't remember the value.
 *
 * What is the number of the Sue that got you the gift?
 *
 * Your puzzle answer was 40.
 * --- Part Two ---
 *
 * As you're about to send the thank you note, something in the MFCSAM's instructions catches your eye. Apparently, it has an outdated retroencabulator, and so the output from the machine isn't exact values - some of them indicate ranges.
 *
 * In particular, the cats and trees readings indicates that there are greater than that many (due to the unpredictable nuclear decay of cat dander and tree pollen), while the pomeranians and goldfish readings indicate that there are fewer than that many (due to the modial interaction of magnetoreluctance).
 *
 * What is the number of the real Aunt Sue?
 *
 * Your puzzle answer was 241.
 */
public class Day16 {

    public static Pattern p1 = Pattern.compile("Sue (\\d+): (.*)");
    public static Pattern p2 = Pattern.compile("(\\w+): (\\d+)");

    public static class Sue {
        public int idx;
        public Map<String, Integer> map = new HashMap<>();

        public Sue(int idx) {
            this.idx = idx;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Sue{");
            sb.append("idx=").append(idx);
            sb.append(", map=").append(map);
            sb.append('}');
            return sb.toString();
        }
    }

    public static Sue parseInputLimit(String inFile) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inFile));
        Sue s = new Sue(0);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = p2.matcher(line);
            m.matches();
            String name = m.group(1);
            int val = Integer.parseInt(m.group(2));
            s.map.put(name, val);
        }
        return s;
    }

    public static List<Sue> parseInput(String inFile) throws FileNotFoundException {
        List<Sue> ss = new ArrayList<>();
        Scanner sc = new Scanner(new File(inFile));
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = p1.matcher(line);
            m.matches();
            int idx = Integer.parseInt(m.group(1));
            Sue s = new Sue(idx);

            Matcher m2 = p2.matcher(m.group(2));
            while (m2.find()) {
                String name = m2.group(1);
                int val = Integer.parseInt(m2.group(2));
                s.map.put(name, val);
            }
            ss.add(s);
        }
        return ss;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Sue s0 = parseInputLimit("./etc/aoc2015/in16b");
        List<Sue> ss = parseInput("./etc/aoc2015/in16a");

        part1(ss, s0);
        part2(ss, s0);
    }

    public static void part2(List<Sue> ss, Sue s0) {
        for (Sue s : ss) {
            boolean isValid = true;
            for (Map.Entry<String, Integer> e : s.map.entrySet()) {
                String type = e.getKey();
                int val0 = s0.map.get(type);
                int testVal = e.getValue();
                if (type.equals("cats") || type.equals("trees")) {
                    if (testVal <= val0) {
                        isValid = false;
                        break;
                    }
                } else if (type.equals("pomeranians") || type.equals("goldfish")) {
                    if (testVal >= val0) {
                        isValid = false;
                        break;
                    }
                } else {
                    if (val0 != testVal) {
                        isValid = false;
                        break;
                    }
                }
            }
            if (isValid) {
                System.out.println(s.idx);
                break;
            }
        }
    }

    public static void part1(List<Sue> ss, Sue s0) {
        for (Sue s : ss) {
            boolean isValid = true;
            for (Map.Entry<String, Integer> e : s.map.entrySet()) {
                if (s0.map.get(e.getKey()) != e.getValue()) {
                    isValid = false;
                    break;
                }
            }
            if (isValid) {
                System.out.println(s.idx);
                break;
            }
        }
    }

}