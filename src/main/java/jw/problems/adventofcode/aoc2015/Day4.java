package jw.problems.adventofcode.aoc2015;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * http://adventofcode.com/2015/day/4
 *
 * --- Day 4: The Ideal Stocking Stuffer ---
 *
 * Santa needs help mining some AdventCoins (very similar to bitcoins) to use as gifts for all the economically forward-thinking little girls and boys.
 *
 * To do this, he needs to find MD5 hashes which, in hexadecimal, start with at least five zeroes. The input to the MD5 hash is some secret key (your puzzle input, given below) followed by a number in decimal. To mine AdventCoins, you must find Santa the lowest positive number (no leading zeroes: 1, 2, 3, ...) that produces such a hash.
 *
 * For example:
 *
 * If your secret key is abcdef, the answer is 609043, because the MD5 hash of abcdef609043 starts with five zeroes (000001dbbfa...), and it is the lowest such number to do so.
 * If your secret key is pqrstuv, the lowest number it combines with to make an MD5 hash starting with five zeroes is 1048970; that is, the MD5 hash of pqrstuv1048970 looks like 000006136ef....
 *
 * Your puzzle answer was 254575.
 * --- Part Two ---
 *
 * Now find one that starts with six zeroes.
 *
 * Your puzzle answer was 1038736.
 */
public class Day4 {

    public static MessageDigest m;

    public static void main(String[] args) throws NoSuchAlgorithmException {
        m = MessageDigest.getInstance("MD5");
        String input = "bgvyzdsv";
        int i = 0;
        while (true) {
            String str = input + i;
//            if (getMd5(str).startsWith("00000")) {
//                System.out.println(i);
//                break;
//            }
            if (getMd5(str).startsWith("000000")) {
                System.out.println(i);
                break;
            }
            i++;
        }
    }

    public static String getMd5(String str) {
        m.reset();
        m.update(str.getBytes());
        byte[] digest = m.digest();
        return bytesToHex(digest);
    }

    public static String bytesToHex(byte[] bs) {
        final StringBuilder sb = new StringBuilder();
        for (byte b : bs) {
            sb.append(Character.forDigit((b & 0xF0) >> 4, 16));
            sb.append(Character.forDigit(b & 0xF, 16));
        }
        return sb.toString();
    }

}