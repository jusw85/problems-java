package jw.problems.aoc2016;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * http://adventofcode.com/2016/day/7
 *
 * --- Day 7: Internet Protocol Version 7 ---
 *
 * While snooping around the local network of EBHQ, you compile a list of IP addresses (they're IPv7, of course; IPv6 is much too limited). You'd like to figure out which IPs support TLS (transport-layer snooping).
 *
 * An IP supports TLS if it has an Autonomous Bridge Bypass Annotation, or ABBA. An ABBA is any four-character sequence which consists of a pair of two different characters followed by the reverse of that pair, such as xyyx or abba. However, the IP also must not have an ABBA within any hypernet sequences, which are contained by square brackets.
 *
 * For example:
 *
 * abba[mnop]qrst supports TLS (abba outside square brackets).
 * abcd[bddb]xyyx does not support TLS (bddb is within square brackets, even though xyyx is outside square brackets).
 * aaaa[qwer]tyui does not support TLS (aaaa is invalid; the interior characters must be different).
 * ioxxoj[asdfgh]zxcvbn supports TLS (oxxo is outside square brackets, even though it's within a larger string).
 *
 * How many IPs in your puzzle input support TLS?
 *
 * Your puzzle answer was 110.
 * --- Part Two ---
 *
 * You would also like to know which IPs support SSL (super-secret listening).
 *
 * An IP supports SSL if it has an Area-Broadcast Accessor, or ABA, anywhere in the supernet sequences (outside any square bracketed sections), and a corresponding Byte Allocation Block, or BAB, anywhere in the hypernet sequences. An ABA is any three-character sequence which consists of the same character twice with a different character between them, such as xyx or aba. A corresponding BAB is the same characters but in reversed positions: yxy and bab, respectively.
 *
 * For example:
 *
 * aba[bab]xyz supports SSL (aba outside square brackets with corresponding bab within square brackets).
 * xyx[xyx]xyx does not support SSL (xyx, but no corresponding yxy).
 * aaa[kek]eke supports SSL (eke in supernet with corresponding kek in hypernet; the aaa sequence is not related, because the interior character must be different).
 * zazbz[bzb]cdb supports SSL (zaz has no corresponding aza, but zbz has a corresponding bzb, even though zaz and zbz overlap).
 *
 * How many IPs in your puzzle input support SSL?
 *
 * Your puzzle answer was 242.
 */
public class Day7 {

    public static void main(String[] args) throws FileNotFoundException {
        int tlsCount = 0;
        int sslCount = 0;

        Scanner sc = new Scanner(new File("./etc/aoc2016/in7"));
        while (sc.hasNextLine()) {
            String str = sc.nextLine();
            if (supportsTLS(str)) {
                tlsCount++;
            }
            if (supportsSSL(str)) {
                sslCount++;
            }
        }
        sc.close();
        System.out.println(tlsCount);
        System.out.println(sslCount);
    }

    public static boolean supportsTLS(String ip) {
        int counter = 0;
        boolean inBrackets = false;
        boolean is34Similar = false;
        boolean isAbba = false;

        for (int i = 0; i < ip.length(); i++) {
            if (ip.charAt(i) == '[') {
                counter = 0;
                inBrackets = true;
                continue;
            } else if (ip.charAt(i) == ']') {
                counter = 0;
                inBrackets = false;
                continue;
            }

            if (counter == 2) {
                is34Similar = ip.charAt(i) == ip.charAt(i - 1);
            } else if (counter > 2) {
                char c1 = ip.charAt(i - 3);
                char c3 = ip.charAt(i - 1);
                char c4 = ip.charAt(i);
                boolean is23Similar = is34Similar;
                is34Similar = c3 == c4;
                if (c1 == c4 && is23Similar && !is34Similar) {
                    if (inBrackets) {
                        isAbba = false;
                        break;
                    } else {
                        isAbba = true;
                    }
                }
            }
            counter++;
        }
        return isAbba;
    }

    public static boolean supportsSSL(String str) {
        int counter = 0;
        boolean inBrackets = false;
        Set<String> abas = new HashSet<>();
        Set<String> babs = new HashSet<>();

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '[') {
                counter = 0;
                inBrackets = true;
                continue;
            } else if (str.charAt(i) == ']') {
                counter = 0;
                inBrackets = false;
                continue;
            }
            if (counter > 1) {
                char c1 = str.charAt(i - 2);
                char c2 = str.charAt(i - 1);
                char c3 = str.charAt(i);
                if (c1 == c3) {
                    if (inBrackets) {
                        babs.add("" + c1 + c2 + c3);
                    } else {
                        abas.add("" + c2 + c1 + c2);
                    }
                }
            }
            counter++;
        }
        abas.retainAll(babs);
        return abas.size() > 0;
    }

}