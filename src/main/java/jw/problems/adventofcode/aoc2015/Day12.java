package jw.problems.adventofcode.aoc2015;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http://adventofcode.com/2015/day/12
 *
 * --- Day 12: JSAbacusFramework.io ---
 *
 * Santa's Accounting-Elves need help balancing the books after a recent order. Unfortunately, their accounting software uses a peculiar storage format. That's where you come in.
 *
 * They have a JSON document which contains a variety of things: arrays ([1,2,3]), objects ({"a":1, "b":2}), numbers, and strings. Your first job is to simply find all of the numbers throughout the document and add them together.
 *
 * For example:
 *
 * [1,2,3] and {"a":2,"b":4} both have a sum of 6.
 * [[[3]]] and {"a":{"b":4},"c":-1} both have a sum of 3.
 * {"a":[-1,1]} and [-1,{"a":1}] both have a sum of 0.
 * [] and {} both have a sum of 0.
 *
 * You will not encounter any strings containing numbers.
 *
 * What is the sum of all numbers in the document?
 *
 * Your puzzle answer was 156366.
 * --- Part Two ---
 *
 * Uh oh - the Accounting-Elves have realized that they double-counted everything red.
 *
 * Ignore any object (and all of its children) which has any property with the value "red". Do this only for objects ({...}), not arrays ([...]).
 *
 * [1,2,3] still has a sum of 6.
 * [1,{"c":"red","b":2},3] now has a sum of 4, because the middle object is ignored.
 * {"d":"red","e":[1,2,3,4],"f":5} now has a sum of 0, because the entire structure is ignored.
 * [1,"red",5] has a sum of 6, because "red" in an array has no effect.
 *
 * Your puzzle answer was 96852.
 */
public class Day12 {

    public static Pattern p = Pattern.compile("(-?\\d+)");

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2015/in12"));
        String line = sc.nextLine();
        part1(line);
        part2(line);
    }

    public static void part2(String line) {
        JSONArray a = new JSONArray(line);
        System.out.println(count(a));
    }

    private static int count(JSONArray in) {
        int count = 0;
        for (Object o : in) {
            count += countObj(o);
        }
        return count;
    }

    private static int count(JSONObject in) {
        for (String key : in.keySet()) {
            Object o = in.get(key);
            if (o instanceof String) {
                if (o.equals("red")) {
                    return 0;
                }
            }
        }
        int count = 0;
        for (String key : in.keySet()) {
            Object o = in.get(key);
            count += countObj(o);
        }
        return count;
    }

    private static int countObj(Object in) {
        int count = 0;
        if (in instanceof JSONArray) {
            count += count((JSONArray) in);
        } else if (in instanceof JSONObject) {
            count += count((JSONObject) in);
        } else if (in instanceof Integer) {
            count += (int) in;
        }
        return count;
    }

    public static void part1(String line) {
        Matcher m = p.matcher(line);
        int count = 0;
        while (m.find()) {
            int i = Integer.parseInt(m.group(1));
            count += i;
        }
        System.out.println(count);
    }

}