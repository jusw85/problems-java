package jw.problems.adventofcode.aoc2015;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http://adventofcode.com/2015/day/19
 *
 * --- Day 19: Medicine for Rudolph ---
 *
 * Rudolph the Red-Nosed Reindeer is sick! His nose isn't shining very brightly, and he needs medicine.
 *
 * Red-Nosed Reindeer biology isn't similar to regular reindeer biology; Rudolph is going to need custom-made medicine. Unfortunately, Red-Nosed Reindeer chemistry isn't similar to regular reindeer chemistry, either.
 *
 * The North Pole is equipped with a Red-Nosed Reindeer nuclear fusion/fission plant, capable of constructing any Red-Nosed Reindeer molecule you need. It works by starting with some input molecule and then doing a series of replacements, one per step, until it has the right molecule.
 *
 * However, the machine has to be calibrated before it can be used. Calibration involves determining the number of molecules that can be generated in one step from a given starting point.
 *
 * For example, imagine a simpler machine that supports only the following replacements:
 *
 * H => HO
 * H => OH
 * O => HH
 *
 * Given the replacements above and starting with HOH, the following molecules could be generated:
 *
 * HOOH (via H => HO on the first H).
 * HOHO (via H => HO on the second H).
 * OHOH (via H => OH on the first H).
 * HOOH (via H => OH on the second H).
 * HHHH (via O => HH).
 *
 * So, in the example above, there are 4 distinct molecules (not five, because HOOH appears twice) after one replacement from HOH. Santa's favorite molecule, HOHOHO, can become 7 distinct molecules (over nine replacements: six from H, and three from O).
 *
 * The machine replaces without regard for the surrounding characters. For example, given the string H2O, the transition H => OO would result in OO2O.
 *
 * Your puzzle input describes all of the possible replacements and, at the bottom, the medicine molecule for which you need to calibrate the machine. How many distinct molecules can be created after all the different ways you can do one replacement on the medicine molecule?
 *
 * Your puzzle answer was 518.
 * --- Part Two ---
 *
 * Now that the machine is calibrated, you're ready to begin molecule fabrication.
 *
 * Molecule fabrication always begins with just a single electron, e, and applying replacements one at a time, just like the ones during calibration.
 *
 * For example, suppose you have the following replacements:
 *
 * e => H
 * e => O
 * H => HO
 * H => OH
 * O => HH
 *
 * If you'd like to make HOH, you start with e, and then make the following replacements:
 *
 * e => O to get O
 * O => HH to get HH
 * H => OH (on the second H) to get HOH
 *
 * So, you could make HOH after 3 steps. Santa's favorite molecule, HOHOHO, can be made in 6 steps.
 *
 * How long will it take to make the medicine? Given the available replacements and the medicine molecule in your puzzle input, what is the fewest number of steps to go from e to the medicine molecule?
 *
 * Your puzzle answer was 200.
 */
public class Day19 {

    public static Map<String, List<String>> parseInputReplacements(String inFile) throws FileNotFoundException {
        Pattern p = Pattern.compile("(\\w+) => (\\w+)");
        Scanner sc = new Scanner(new File(inFile));
        Map<String, List<String>> map = new HashMap<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = p.matcher(line);
            m.matches();
            String from = replaceChars(m.group(1));
            String to = replaceChars(m.group(2));
            if (map.containsKey(from)) {
                map.get(from).add(to);
            } else {
                List<String> l = new ArrayList<>();
                l.add(to);
                map.put(from, l);
            }
        }
        return map;
    }

    public static String parseInput(String inFile) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inFile));
        return sc.next();
    }

    public static void main(String[] args) throws FileNotFoundException {
        Map<String, List<String>> map = parseInputReplacements("./etc/aoc2015/in19a");
        String in = replaceChars(parseInput("./etc/aoc2015/in19b"));
        part1(map, in);
        part2(map, in);
    }

    public static String replaceChars(String in) {
        String out = in
                .replaceAll("Rn", "{")
                .replaceAll("Ar", "}")
                .replaceAll("Y", ",")
                .replaceAll("Al", "L")
                .replaceAll("Th", "T")
                .replaceAll("Ti", "I")
                .replaceAll("Ca", "D")
                .replaceAll("Mg", "M")
                .replaceAll("Si", "S");
        return out;
    }

    public static Map<String, String> reverse(Map<String, List<String>> in) {
        Map<String, String> out = new HashMap<>();
        for (Map.Entry<String, List<String>> e : in.entrySet()) {
            for (String s : e.getValue()) {
                out.put(new StringBuilder(s).reverse().toString(), e.getKey());
            }
        }
        return out;
    }

    public static void part1(Map<String, List<String>> map, String in) {
        Set<String> found = new HashSet<>();
        for (Map.Entry<String, List<String>> e : map.entrySet()) {
            String from = e.getKey();
            List<String> tos = e.getValue();

            int i = 0;
            int idx;
            while ((idx = in.indexOf(from, i)) >= 0) {
                i = idx + (from.length());
                for (String to : tos) {
                    String s = in.substring(0, idx) + to + in.substring(idx + from.length(), in.length());
                    found.add(s);
                }
            }
        }
        System.out.println(found.size());
    }

    public static void part2(Map<String, List<String>> map, String in) {
        Map<String, String> m = reverse(map);
        in = new StringBuilder(in).reverse().toString();

        StringBuilder sb = new StringBuilder();
        char[] chars = in.toCharArray();
        int count = 0;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            sb.append(c);
            for (Map.Entry<String, String> e : m.entrySet()) {
                String from = e.getKey();
                String to = e.getValue();

                String s = sb.substring(Math.max(0, sb.length() - from.length()), sb.length());
                if (s.equals(from)) {
                    sb.delete(sb.length() - from.length(), sb.length());
                    sb.append(to);
                    count++;
                }
            }
        }
        System.out.println(count + 1);
    }
}