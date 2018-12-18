package jw.problems.aoc2018;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://adventofcode.com/2018/day/12
 *
 * --- Day 12: Subterranean Sustainability ---
 *
 * The year 518 is significantly more underground than your history books implied. Either that, or you've arrived in a vast cavern network under the North Pole.
 *
 * After exploring a little, you discover a long tunnel that contains a row of small pots as far as you can see to your left and right. A few of them contain plants - someone is trying to grow things in these geothermally-heated caves.
 *
 * The pots are numbered, with 0 in front of you. To the left, the pots are numbered -1, -2, -3, and so on; to the right, 1, 2, 3.... Your puzzle input contains a list of pots from 0 to the right and whether they do (#) or do not (.) currently contain a plant, the initial state. (No other pots currently contain plants.) For example, an initial state of #..##.... indicates that pots 0, 3, and 4 currently contain plants.
 *
 * Your puzzle input also contains some notes you find on a nearby table: someone has been trying to figure out how these plants spread to nearby pots. Based on the notes, for each generation of plants, a given pot has or does not have a plant based on whether that pot (and the two pots on either side of it) had a plant in the last generation. These are written as LLCRR => N, where L are pots to the left, C is the current pot being considered, R are the pots to the right, and N is whether the current pot will have a plant in the next generation. For example:
 *
 * A note like ..#.. => . means that a pot that contains a plant but with no plants within two pots of it will not have a plant in it during the next generation.
 * A note like ##.## => . means that an empty pot with two plants on each side of it will remain empty in the next generation.
 * A note like .##.# => # means that a pot has a plant in a given generation if, in the previous generation, there were plants in that pot, the one immediately to the left, and the one two pots to the right, but not in the ones immediately to the right and two to the left.
 *
 * It's not clear what these plants are for, but you're sure it's important, so you'd like to make sure the current configuration of plants is sustainable by determining what will happen after 20 generations.
 *
 * For example, given the following input:
 *
 * initial state: #..#.#..##......###...###
 *
 * ...## => #
 * ..#.. => #
 * .#... => #
 * .#.#. => #
 * .#.## => #
 * .##.. => #
 * .#### => #
 * #.#.# => #
 * #.### => #
 * ##.#. => #
 * ##.## => #
 * ###.. => #
 * ###.# => #
 * ####. => #
 *
 * For brevity, in this example, only the combinations which do produce a plant are listed. (Your input includes all possible combinations.) Then, the next 20 generations will look like this:
 *
 * 1         2         3
 * 0         0         0         0
 * 0: ...#..#.#..##......###...###...........
 * 1: ...#...#....#.....#..#..#..#...........
 * 2: ...##..##...##....#..#..#..##..........
 * 3: ..#.#...#..#.#....#..#..#...#..........
 * 4: ...#.#..#...#.#...#..#..##..##.........
 * 5: ....#...##...#.#..#..#...#...#.........
 * 6: ....##.#.#....#...#..##..##..##........
 * 7: ...#..###.#...##..#...#...#...#........
 * 8: ...#....##.#.#.#..##..##..##..##.......
 * 9: ...##..#..#####....#...#...#...#.......
 * 10: ..#.#..#...#.##....##..##..##..##......
 * 11: ...#...##...#.#...#.#...#...#...#......
 * 12: ...##.#.#....#.#...#.#..##..##..##.....
 * 13: ..#..###.#....#.#...#....#...#...#.....
 * 14: ..#....##.#....#.#..##...##..##..##....
 * 15: ..##..#..#.#....#....#..#.#...#...#....
 * 16: .#.#..#...#.#...##...#...#.#..##..##...
 * 17: ..#...##...#.#.#.#...##...#....#...#...
 * 18: ..##.#.#....#####.#.#.#...##...##..##..
 * 19: .#..###.#..#.#.#######.#.#.#..#.#...#..
 * 20: .#....##....#####...#######....#.#..##.
 *
 * The generation is shown along the left, where 0 is the initial state. The pot numbers are shown along the top, where 0 labels the center pot, negative-numbered pots extend to the left, and positive pots extend toward the right. Remember, the initial state begins at pot 0, which is not the leftmost pot used in this example.
 *
 * After one generation, only seven plants remain. The one in pot 0 matched the rule looking for ..#.., the one in pot 4 matched the rule looking for .#.#., pot 9 matched .##.., and so on.
 *
 * In this example, after 20 generations, the pots shown as # contain plants, the furthest left of which is pot -2, and the furthest right of which is pot 34. Adding up all the numbers of plant-containing pots after the 20th generation produces 325.
 *
 * After 20 generations, what is the sum of the numbers of all pots which contain a plant?
 *
 * Your puzzle answer was 3221.
 * --- Part Two ---
 *
 * You realize that 20 generations aren't enough. After all, these plants will need to last another 1500 years to even reach your timeline, not to mention your future.
 *
 * After fifty billion (50000000000) generations, what is the sum of the numbers of all pots which contain a plant?
 *
 * Your puzzle answer was 2600000001872.
 */
public class Day12 {

    public static Pattern p1 = Pattern.compile("initial state: (.*+)");
    public static Pattern p2 = Pattern.compile("([.#]+) => ([.#]+)");

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2018/in12"));
        String line = sc.nextLine();
        Matcher m = p1.matcher(line);
        m.matches();
        StringBuilder sb = new StringBuilder(m.group(1));

        Map<String, String> map = new HashMap<>();
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            if (line.isEmpty()) {
                continue;
            }
            m = p2.matcher(line);
            m.matches();
            map.put(m.group(1), m.group(2));
        }

        int idx = 0;
        for (int i = 0; i < 200; i++) {
            idx = tick(sb, map, idx);
            if (i == 19) {
                System.out.println(i + " " + idx + " " + countPlants(sb, idx) + " " + sb);
            }
            if (i >= 100) {
                pprint(i, idx, countPlants(sb, idx), sb, -3);
            }
        }
        System.out.println((50000000000L - 129) * 52 + 8580);
    }

    public static void pprint(int i, int idx, int sum, StringBuilder sb, int anchor) {
        StringBuilder sb2 = new StringBuilder(sb);

        for (int j = 0; j < idx - anchor; j++) {
            sb2.insert(0, ".");
        }

        System.out.println(i + " " + anchor + " " + sum + " " + sb2);
    }

    public static int countPlants(StringBuilder sb, int idx) {
        int sum = 0;
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '#') {
                sum += idx + i;
            }
        }
        return sum;
    }

    public static int tick(StringBuilder sb, Map<String, String> map, int idx) {
        idx += sb.indexOf("#") - 2;
        sb.delete(0, sb.indexOf("#"));
        sb.delete(sb.lastIndexOf("#") + 1, sb.length());
        sb.append("....");
        sb.insert(0, "....");
        StringBuilder sb2 = new StringBuilder();
        for (int i = 2; i < sb.length() - 2; i++) {
            String s = sb.substring(i - 2, i + 3);
            if (map.containsKey(s)) {
                sb2.append(map.get(s));
            } else {
                sb2.append('.');
            }
        }
        sb.setLength(0);
        sb.append(sb2);
        return idx;
    }
}
