package jw.problems.aoc2015;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * http://adventofcode.com/2015/day/17
 *
 * --- Day 17: No Such Thing as Too Much ---
 *
 * The elves bought too much eggnog again - 150 liters this time. To fit it all into your refrigerator, you'll need to move it into smaller containers. You take an inventory of the capacities of the available containers.
 *
 * For example, suppose you have containers of size 20, 15, 10, 5, and 5 liters. If you need to store 25 liters, there are four ways to do it:
 *
 * 15 and 10
 * 20 and 5 (the first 5)
 * 20 and 5 (the second 5)
 * 15, 5, and 5
 *
 * Filling all containers entirely, how many different combinations of containers can exactly fit all 150 liters of eggnog?
 *
 * Your puzzle answer was 4372.
 * --- Part Two ---
 *
 * While playing with all the containers in the kitchen, another load of eggnog arrives! The shipping and receiving department is requesting as many containers as you can spare.
 *
 * Find the minimum number of containers that can exactly fit all 150 liters of eggnog. How many different ways can you fill that number of containers and still hold exactly 150 litres?
 *
 * In the example above, the minimum number of containers was two. There were three ways to use that many containers, and so the answer there would be 3.
 *
 * Your puzzle answer was 4.
 */
public class Day17 {

    public static List<Integer> parseInput(String inFile) throws FileNotFoundException {
        List<Integer> is = new ArrayList<>();
        Scanner sc = new Scanner(new File(inFile));
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            is.add(Integer.parseInt(line));
        }
        return is;
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<Integer> is = parseInput("./etc/aoc2015/in17");
        Result r = new Result();
        permute(150, is, 0, r, 0);
        System.out.println(r);
    }

    public static class Result {
        int num;
        int min = Integer.MAX_VALUE;
        int numWays;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Result{");
            sb.append("num=").append(num);
            sb.append(", min=").append(min);
            sb.append(", numWays=").append(numWays);
            sb.append('}');
            return sb.toString();
        }
    }

    public static void permute(int num, List<Integer> is, int idx, Result r, int numContainers) {
        if (idx == is.size()) {
            if (num == 0) {
                r.num++;
                if (numContainers < r.min) {
                    r.min = numContainers;
                    r.numWays = 1;
                } else if (numContainers == r.min) {
                    r.numWays++;
                }
            }
            return;
        }
        permute(num, is, idx + 1, r, numContainers);
        permute(num - is.get(idx), is, idx + 1, r, numContainers + 1);
    }

}