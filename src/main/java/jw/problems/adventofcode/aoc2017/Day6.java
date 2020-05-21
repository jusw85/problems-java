package jw.problems.adventofcode.aoc2017;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * http://adventofcode.com/2017/day/6
 *
 * --- Day 6: Memory Reallocation ---
 *
 * A debugger program here is having an issue: it is trying to repair a memory reallocation routine, but it keeps getting stuck in an infinite loop.
 *
 * In this area, there are sixteen memory banks; each memory bank can hold any number of blocks. The goal of the reallocation routine is to balance the blocks between the memory banks.
 *
 * The reallocation routine operates in cycles. In each cycle, it finds the memory bank with the most blocks (ties won by the lowest-numbered memory bank) and redistributes those blocks among the banks. To do this, it removes all of the blocks from the selected bank, then moves to the next (by index) memory bank and inserts one of the blocks. It continues doing this until it runs out of blocks; if it reaches the last memory bank, it wraps around to the first one.
 *
 * The debugger would like to know how many redistributions can be done before a blocks-in-banks configuration is produced that has been seen before.
 *
 * For example, imagine a scenario with only four memory banks:
 *
 * The banks start with 0, 2, 7, and 0 blocks. The third bank has the most blocks, so it is chosen for redistribution.
 * Starting with the next bank (the fourth bank) and then continuing to the first bank, the second bank, and so on, the 7 blocks are spread out over the memory banks. The fourth, first, and second banks get two blocks each, and the third bank gets one back. The final result looks like this: 2 4 1 2.
 * Next, the second bank is chosen because it contains the most blocks (four). Because there are four memory banks, each gets one block. The result is: 3 1 2 3.
 * Now, there is a tie between the first and fourth memory banks, both of which have three blocks. The first bank wins the tie, and its three blocks are distributed evenly over the other three banks, leaving it with none: 0 2 3 4.
 * The fourth bank is chosen, and its four blocks are distributed such that each of the four banks receives one: 1 3 4 1.
 * The third bank is chosen, and the same thing happens: 2 4 1 2.
 *
 * At this point, we've reached a state we've seen before: 2 4 1 2 was already seen. The infinite loop is detected after the fifth block redistribution cycle, and so the answer in this example is 5.
 *
 * Given the initial block counts in your puzzle input, how many redistribution cycles must be completed before a configuration is produced that has been seen before?
 *
 * Your puzzle answer was 6681.
 * --- Part Two ---
 *
 * Out of curiosity, the debugger would also like to know the size of the loop: starting from a state that has already been seen, how many block redistribution cycles must be performed before that same state is seen again?
 *
 * In the example above, 2 4 1 2 is seen again after four cycles, and so the answer in that example would be 4.
 *
 * How many cycles are in the infinite loop that arises from the configuration in your puzzle input?
 *
 * Your puzzle answer was 2392.
 */
public class Day6 {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2017/in6"));
        List<Integer> isl = new ArrayList<>();
        while (sc.hasNextInt()) {
            isl.add(sc.nextInt());
        }
        sc.close();

        List<String> visited = new ArrayList<>();
        int[] is = isl.stream().mapToInt(i -> i).toArray();

        int count = 0;
        while (!visited.contains(arrToString(is))) {
            visited.add(arrToString(is));
            redist(is);
            count++;
        }
        System.out.println(count);
        System.out.println(visited.size() - visited.indexOf(arrToString(is)));
    }

    public static void redist(int[] is) {
        int maxIdx = maxIdx(is);
        int n = is[maxIdx];
        int q = n / is.length;
        int r = n % is.length;

        is[maxIdx] = 0;
        for (int i = 0; i < is.length; i++) {
            is[i] += q;
        }
        for (int i = (maxIdx + 1) % is.length; r > 0; i = (i + 1) % is.length) {
            is[i]++;
            r--;
        }

    }

    public static int maxIdx(int[] is) {
        int max = Integer.MIN_VALUE;
        int maxIdx = -1;
        for (int i = 0; i < is.length; i++) {
            if (is[i] > max) {
                max = is[i];
                maxIdx = i;
            }
        }
        return maxIdx;
    }

    public static String arrToString(int[] is) {
        StringBuilder sb = new StringBuilder();
        String prefix = "";
        for (int i = 0; i < is.length; i++) {
            sb.append(prefix);
            sb.append(is[i]);
            prefix = ",";
        }
        return sb.toString();
    }
}