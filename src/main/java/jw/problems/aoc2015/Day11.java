package jw.problems.aoc2015;

/**
 * http://adventofcode.com/2015/day/11
 *
 * --- Day 11: Corporate Policy ---
 *
 * Santa's previous password expired, and he needs help choosing a new one.
 *
 * To help him remember his new password after the old one expires, Santa has devised a method of coming up with a password based on the previous one. Corporate policy dictates that passwords must be exactly eight lowercase letters (for security reasons), so he finds his new password by incrementing his old password string repeatedly until it is valid.
 *
 * Incrementing is just like counting with numbers: xx, xy, xz, ya, yb, and so on. Increase the rightmost letter one step; if it was z, it wraps around to a, and repeat with the next letter to the left until one doesn't wrap around.
 *
 * Unfortunately for Santa, a new Security-Elf recently started, and he has imposed some additional password requirements:
 *
 * Passwords must include one increasing straight of at least three letters, like abc, bcd, cde, and so on, up to xyz. They cannot skip letters; abd doesn't count.
 * Passwords may not contain the letters i, o, or l, as these letters can be mistaken for other characters and are therefore confusing.
 * Passwords must contain at least two different, non-overlapping pairs of letters, like aa, bb, or zz.
 *
 * For example:
 *
 * hijklmmn meets the first requirement (because it contains the straight hij) but fails the second requirement requirement (because it contains i and l).
 * abbceffg meets the third requirement (because it repeats bb and ff) but fails the first requirement.
 * abbcegjk fails the third requirement, because it only has one double letter (bb).
 * The next password after abcdefgh is abcdffaa.
 * The next password after ghijklmn is ghjaabcc, because you eventually skip all the passwords that start with ghi..., since i is not allowed.
 *
 * Given Santa's current password (your puzzle input), what should his next password be?
 *
 * Your puzzle answer was vzbxxyzz.
 * --- Part Two ---
 *
 * Santa's password expired again. What's the next one?
 *
 * Your puzzle answer was vzcaabcc.
 */
public class Day11 {

    public static void main(String[] args) {
        String input = "vzbxkghb";
        StringBuilder sb = new StringBuilder(input);

        while (!isValid(sb.toString())) {
            nextPassword(sb);
        }
        System.out.println(sb);

        nextPassword(sb);
        while (!isValid(sb.toString())) {
            nextPassword(sb);
        }
        System.out.println(sb);
    }

    public static boolean isValid(String s) {
        if (s.indexOf("i") > 0 || s.indexOf("o") > 0 || s.indexOf("l") > 0) {
            return false;
        }
        boolean hasDoubleLetter = s.matches(".*(.)\\1.*(.)\\2.*");
        return hasDoubleLetter && hasConsecutive(s);
    }

    public static boolean hasConsecutive(String s) {
        for (int i = 0; i < s.length() - 2; i++) {
            if ((s.charAt(i + 2) - s.charAt(i + 1) == 1) &&
                    (s.charAt(i + 1) - s.charAt(i) == 1)) {
                return true;
            }
        }
        return false;
    }

    public static void nextPassword(StringBuilder sb) {
        boolean isDone = false;
        int i = sb.length() - 1;
        while (i >= 0 && !isDone) {
            char c = sb.charAt(i);
            if (c != 'z') {
                isDone = true;
            }
            sb.setCharAt(i, nextChar(c));
            i--;
        }
    }

    public static char nextChar(char c) {
        if (c != 'z') {
            return (char) ((int) c + 1);
        }
        return 'a';
    }
}