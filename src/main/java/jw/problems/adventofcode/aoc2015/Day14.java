package jw.problems.adventofcode.aoc2015;

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
 * http://adventofcode.com/2015/day/14
 *
 * --- Day 14: Reindeer Olympics ---
 *
 * This year is the Reindeer Olympics! Reindeer can fly at high speeds, but must rest occasionally to recover their energy. Santa would like to know which of his reindeer is fastest, and so he has them race.
 *
 * Reindeer can only either be flying (always at their top speed) or resting (not moving at all), and always spend whole seconds in either state.
 *
 * For example, suppose you have the following Reindeer:
 *
 * Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
 * Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
 *
 * After one second, Comet has gone 14 km, while Dancer has gone 16 km. After ten seconds, Comet has gone 140 km, while Dancer has gone 160 km. On the eleventh second, Comet begins resting (staying at 140 km), and Dancer continues on for a total distance of 176 km. On the 12th second, both reindeer are resting. They continue to rest until the 138th second, when Comet flies for another ten seconds. On the 174th second, Dancer flies for another 11 seconds.
 *
 * In this example, after the 1000th second, both reindeer are resting, and Comet is in the lead at 1120 km (poor Dancer has only gotten 1056 km by that point). So, in this situation, Comet would win (if the race ended at 1000 seconds).
 *
 * Given the descriptions of each reindeer (in your puzzle input), after exactly 2503 seconds, what distance has the winning reindeer traveled?
 *
 * Your puzzle answer was 2640.
 * --- Part Two ---
 *
 * Seeing how reindeer move in bursts, Santa decides he's not pleased with the old scoring system.
 *
 * Instead, at the end of each second, he awards one point to the reindeer currently in the lead. (If there are multiple reindeer tied for the lead, they each get one point.) He keeps the traditional 2503 second time limit, of course, as doing otherwise would be entirely ridiculous.
 *
 * Given the example reindeer from above, after the first second, Dancer is in the lead and gets one point. He stays in the lead until several seconds into Comet's second burst: after the 140th second, Comet pulls into the lead and gets his first point. Of course, since Dancer had been in the lead for the 139 seconds before that, he has accumulated 139 points by the 140th second.
 *
 * After the 1000th second, Dancer has accumulated 689 points, while poor Comet, our old champion, only has 312. So, with the new scoring system, Dancer would win (if the race ended at 1000 seconds).
 *
 * Again given the descriptions of each reindeer (in your puzzle input), after exactly 2503 seconds, how many points does the winning reindeer have?
 *
 * Your puzzle answer was 1102.
 */
public class Day14 {

    public static Pattern p = Pattern.compile(
            "(\\w+) can fly (-?\\d+) km/s for (-?\\d+) seconds, but then must rest for (-?\\d+) seconds.");

    public static class Flyer {
        public String name;
        public int spd;
        public int moveSec;
        public int restSec;
        private int totalSec;

        public Flyer(String name, int spd, int moveSec, int restSec) {
            this.name = name;
            this.spd = spd;
            this.moveSec = moveSec;
            this.restSec = restSec;
            this.totalSec = moveSec + restSec;
        }

        public int getDist(int time) {
            int q = time / totalSec;
            int r = time % totalSec;
            return spd * ((moveSec * q) + Math.min(moveSec, r));
        }
    }

    public static List<Flyer> parseInput(String inFile) throws FileNotFoundException {
        List<Flyer> flyers = new ArrayList<>();
        Scanner sc = new Scanner(new File(inFile));
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = p.matcher(line);
            m.matches();
            String name = m.group(1);
            int spd = Integer.parseInt(m.group(2));
            int sec = Integer.parseInt(m.group(3));
            int rest = Integer.parseInt(m.group(4));
            flyers.add(new Flyer(name, spd, sec, rest));
        }
        return flyers;
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<Flyer> flyers = parseInput("./etc/aoc2015/in14");
        int time = 2503;
        flyers.sort((o1, o2) -> -Integer.compare(o1.getDist(time), o2.getDist(time)));
        System.out.println(flyers.get(0).getDist(time));

        Map<String, Integer> score = new HashMap<>();
        for (Flyer flyer : flyers) {
            score.put(flyer.name, 0);
        }
        for (int i = 0; i < time; i++) {
            final int t = i;
            flyers.sort((o1, o2) -> -Integer.compare(o1.getDist(t), o2.getDist(t)));
            score.put(flyers.get(0).name, score.get(flyers.get(0).name) + 1);
        }
        int max = score.values().stream()
                .mapToInt(Integer::intValue)
                .max().getAsInt();
        System.out.println(max);
    }

}