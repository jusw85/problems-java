package jw.problems.adventofcode.aoc2017;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * http://adventofcode.com/2017/day/2
 *
 * --- Day 2: Corruption Checksum ---
 *
 * As you walk through the door, a glowing humanoid shape yells in your direction. "You there! Your state appears to be idle. Come help us repair the corruption in this spreadsheet - if we take another millisecond, we'll have to display an hourglass cursor!"
 *
 * The spreadsheet consists of rows of apparently-random numbers. To make sure the recovery process is on the right track, they need you to calculate the spreadsheet's checksum. For each row, determine the difference between the largest value and the smallest value; the checksum is the sum of all of these differences.
 *
 * For example, given the following spreadsheet:
 *
 * 5 1 9 5
 * 7 5 3
 * 2 4 6 8
 *
 * The first row's largest and smallest values are 9 and 1, and their difference is 8.
 * The second row's largest and smallest values are 7 and 3, and their difference is 4.
 * The third row's difference is 6.
 *
 * In this example, the spreadsheet's checksum would be 8 + 4 + 6 = 18.
 *
 * What is the checksum for the spreadsheet in your puzzle input?
 *
 * Your puzzle answer was 36174.
 * --- Part Two ---
 *
 * "Great work; looks like we're on the right track after all. Here's a star for your effort." However, the program seems a little worried. Can programs be worried?
 *
 * "Based on what we're seeing, it looks like all the User wanted is some information about the evenly divisible values in the spreadsheet. Unfortunately, none of us are equipped for that kind of calculation - most of us specialize in bitwise operations."
 *
 * It sounds like the goal is to find the only two numbers in each row where one evenly divides the other - that is, where the result of the division operation is a whole number. They would like you to find those numbers on each line, divide them, and add up each line's result.
 *
 * For example, given the following spreadsheet:
 *
 * 5 9 2 8
 * 9 4 7 3
 * 3 8 6 5
 *
 * In the first row, the only two numbers that evenly divide are 8 and 2; the result of this division is 4.
 * In the second row, the two numbers are 9 and 3; the result is 3.
 * In the third row, the result is 2.
 *
 * In this example, the sum of the results would be 4 + 3 + 2 = 9.
 *
 * What is the sum of each row's result in your puzzle input?
 *
 * Your puzzle answer was 244.
 */
public class Day2 {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2017/in2"));
        List<List<Integer>> nums = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Scanner sc2 = new Scanner(line);
            List<Integer> nums2 = new ArrayList<>();
            while (sc2.hasNextInt()) {
                nums2.add(sc2.nextInt());
            }
            sc2.close();
            nums.add(nums2);
        }
        sc.close();
        part1(nums);
        part2(nums);
    }

    public static void part1(List<List<Integer>> nums) {
        int cksum = 0;
        for (List<Integer> nums2 : nums) {
            Collections.sort(nums2);
            int max = nums2.get(nums2.size() - 1);
            int min = nums2.get(0);
            cksum += (max - min);
        }
        System.out.println(cksum);
    }

    public static void part2(List<List<Integer>> nums) {
        int cksum = 0;
        for (List<Integer> nums2 : nums) {
            Collections.sort(nums2);
            outer:
            for (int i = 0; i < nums2.size() - 2; i++) {
                for (int j = i + 1; j < nums2.size() - 1; j++) {
                    int i1 = nums2.get(i);
                    int i2 = nums2.get(j);
                    if (i2 % i1 == 0) {
                        cksum += i2 / i1;
                        break outer;
                    }
                }
            }
        }
        System.out.println(cksum);
    }
}