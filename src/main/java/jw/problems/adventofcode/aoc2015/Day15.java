package jw.problems.adventofcode.aoc2015;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http://adventofcode.com/2015/day/15
 *
 * --- Day 15: Science for Hungry People ---
 *
 * Today, you set out on the task of perfecting your milk-dunking cookie recipe. All you have to do is find the right balance of ingredients.
 *
 * Your recipe leaves room for exactly 100 teaspoons of ingredients. You make a list of the remaining ingredients you could use to finish the recipe (your puzzle input) and their properties per teaspoon:
 *
 * capacity (how well it helps the cookie absorb milk)
 * durability (how well it keeps the cookie intact when full of milk)
 * flavor (how tasty it makes the cookie)
 * texture (how it improves the feel of the cookie)
 * calories (how many calories it adds to the cookie)
 *
 * You can only measure ingredients in whole-teaspoon amounts accurately, and you have to be accurate so you can reproduce your results in the future. The total score of a cookie can be found by adding up each of the properties (negative totals become 0) and then multiplying together everything except calories.
 *
 * For instance, suppose you have these two ingredients:
 *
 * Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
 * Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
 *
 * Then, choosing to use 44 teaspoons of butterscotch and 56 teaspoons of cinnamon (because the amounts of each ingredient must add up to 100) would result in a cookie with the following properties:
 *
 * A capacity of 44*-1 + 56*2 = 68
 * A durability of 44*-2 + 56*3 = 80
 * A flavor of 44*6 + 56*-2 = 152
 * A texture of 44*3 + 56*-1 = 76
 *
 * Multiplying these together (68 * 80 * 152 * 76, ignoring calories for now) results in a total score of 62842880, which happens to be the best score possible given these ingredients. If any properties had produced a negative total, it would have instead become zero, causing the whole score to multiply to zero.
 *
 * Given the ingredients in your kitchen and their properties, what is the total score of the highest-scoring cookie you can make?
 *
 * Your puzzle answer was 222870.
 * --- Part Two ---
 *
 * Your cookie recipe becomes wildly popular! Someone asks if you can make another recipe that has exactly 500 calories per cookie (so they can use it as a meal replacement). Keep the rest of your award-winning process the same (100 teaspoons, same ingredients, same scoring system).
 *
 * For example, given the ingredients above, if you had instead selected 40 teaspoons of butterscotch and 60 teaspoons of cinnamon (which still adds to 100), the total calorie count would be 40*8 + 60*3 = 500. The total score would go down, though: only 57600000, the best you can do in such trying circumstances.
 *
 * Given the ingredients in your kitchen and their properties, what is the total score of the highest-scoring cookie you can make with a calorie total of 500?
 *
 * Your puzzle answer was 117936.
 */
public class Day15 {

    public static Pattern p = Pattern.compile(
            "(\\w+): capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)");

    public static class Ingredient {
        public String name;
        public int cap;
        public int dur;
        public int flav;
        public int text;
        public int cal;

        public Ingredient(String name) {
            this.name = name;
        }

        public Ingredient(String name, int cap, int dur, int flav, int text, int cal) {
            this.name = name;
            this.cap = cap;
            this.dur = dur;
            this.flav = flav;
            this.text = text;
            this.cal = cal;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Ingredient{");
            sb.append("name='").append(name).append('\'');
            sb.append(", cap=").append(cap);
            sb.append(", dur=").append(dur);
            sb.append(", flav=").append(flav);
            sb.append(", text=").append(text);
            sb.append(", cal=").append(cal);
            sb.append('}');
            return sb.toString();
        }
    }

    public static List<Ingredient> parseInput(String inFile) throws FileNotFoundException {
        List<Ingredient> ings = new ArrayList<>();
        Scanner sc = new Scanner(new File(inFile));
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = p.matcher(line);
            m.matches();
            String name = m.group(1);
            int cap = Integer.parseInt(m.group(2));
            int dur = Integer.parseInt(m.group(3));
            int flav = Integer.parseInt(m.group(4));
            int text = Integer.parseInt(m.group(5));
            int cal = Integer.parseInt(m.group(6));
            ings.add(new Ingredient(name, cap, dur, flav, text, cal));
        }
        return ings;
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<Ingredient> ings = parseInput("./etc/aoc2015/in15");
        int[] acc = new int[ings.size()];

        Result r = new Result();
        permute(ings, 100, acc, 0, r, -1);
        System.out.println(r.max);

        r = new Result();
        permute(ings, 100, acc, 0, r, 500);
        System.out.println(r.max);
    }

    public static class Result {
        public long max;
    }

    public static void permute(List<Ingredient> ings, int num, int[] acc, int idx, Result r, int cal) {
        if (idx == acc.length - 1) {
            acc[idx] = num;
            long score = getScore(ings, acc, cal);
            if (score > r.max) {
                r.max = score;
            }
            return;
        }
        for (int i = 0; i <= num; i++) {
            acc[idx] = i;
            permute(ings, num - i, acc, idx + 1, r, cal);
        }
    }

    public static long getScore(List<Ingredient> ings, int[] nums, int cal) {
        Ingredient c = new Ingredient("Combined");
        for (int i = 0; i < nums.length; i++) {
            Ingredient ing = ings.get(i);
            c.cap += nums[i] * ing.cap;
            c.dur += nums[i] * ing.dur;
            c.flav += nums[i] * ing.flav;
            c.text += nums[i] * ing.text;
            c.cal += nums[i] * ing.cal;
        }
        c.cap = Math.max(0, c.cap);
        c.dur = Math.max(0, c.dur);
        c.flav = Math.max(0, c.flav);
        c.text = Math.max(0, c.text);
        long score = c.cap * c.dur * c.flav * c.text;
        if (cal > 0) {
            if (cal == c.cal) {
                return score;
            } else {
                return -1;
            }
        }
        return score;
    }

}