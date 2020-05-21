package jw.problems.adventofcode.aoc2016;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://adventofcode.com/2016/day/20
 *
 * --- Day 20: Firewall Rules ---
 *
 * You'd like to set up a small hidden computer here so you can use it to get back into the network later. However, the corporate firewall only allows communication with certain external IP addresses.
 *
 * You've retrieved the list of blocked IPs from the firewall, but the list seems to be messy and poorly maintained, and it's not clear which IPs are allowed. Also, rather than being written in dot-decimal notation, they are written as plain 32-bit integers, which can have any value from 0 through 4294967295, inclusive.
 *
 * For example, suppose only the values 0 through 9 were valid, and that you retrieved the following blacklist:
 *
 * 5-8
 * 0-2
 * 4-7
 *
 * The blacklist specifies ranges of IPs (inclusive of both the start and end value) that are not allowed. Then, the only IPs that this firewall allows are 3 and 9, since those are the only numbers not in any range.
 *
 * Given the list of blocked IPs you retrieved from the firewall (your puzzle input), what is the lowest-valued IP that is not blocked?
 *
 * Your puzzle answer was 22887907.
 * --- Part Two ---
 *
 * How many IPs are allowed by the blacklist?
 *
 * Your puzzle answer was 109.
 */
public class Day20 {

    public static Pattern p = Pattern.compile("(\\d+)-(\\d+)");

    public static void main(String[] args) throws FileNotFoundException {
        List<MinMax> mms = new ArrayList<>();
        Scanner sc = new Scanner(new File("./etc/aoc2016/in20"));
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = p.matcher(line);
            m.matches();
            long min = Long.parseLong(m.group(1));
            long max = Long.parseLong(m.group(2));
            MinMax minmax = new MinMax(min, max);
            mms.add(minmax);
        }

        Collections.sort(mms, Comparator.comparingLong(o -> o.min));
        System.out.println(getMin(mms));
        System.out.println(getNumAllowed(mms));
    }

    public static long getNumAllowed(List<MinMax> mms) {
        List<MinMax> combined = new ArrayList<>();
        MinMax curr = new MinMax(mms.get(0));
        for (int i = 1; i < mms.size(); i++) {
            MinMax next = mms.get(i);
            if (next.min > (curr.max + 1)) {
                combined.add(curr);
                curr = new MinMax(next);
                continue;
            }
            if (next.max > curr.max) {
                curr.max = next.max;
            }
        }
        combined.add(curr);

        long num = 0;
        for (MinMax m : combined) {
            num += (m.max - m.min) + 1;
        }
        return (4294967296L - num);
    }

    public static long getMin(List<MinMax> mms) {
        long min = 0L;
        for (MinMax mm : mms) {
            if (mm.min > min) {
                break;
            }
            if (min <= mm.max) {
                min = mm.max + 1;
            }
        }
        return min;
    }

    public static class MinMax {
        public long min;
        public long max;

        public MinMax(MinMax m) {
            this.min = m.min;
            this.max = m.max;
        }

        public MinMax(long min, long max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("MinMax{");
            sb.append("min=").append(min);
            sb.append(", max=").append(max);
            sb.append('}');
            return sb.toString();
        }
    }
}