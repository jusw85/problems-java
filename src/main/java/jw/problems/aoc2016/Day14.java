package jw.problems.aoc2016;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://adventofcode.com/2016/day/14
 *
 * --- Day 14: One-Time Pad ---
 *
 * In order to communicate securely with Santa while you're on this mission, you've been using a one-time pad that you generate using a pre-agreed algorithm. Unfortunately, you've run out of keys in your one-time pad, and so you need to generate some more.
 *
 * To generate keys, you first get a stream of random data by taking the MD5 of a pre-arranged salt (your puzzle input) and an increasing integer index (starting with 0, and represented in decimal); the resulting MD5 hash should be represented as a string of lowercase hexadecimal digits.
 *
 * However, not all of these MD5 hashes are keys, and you need 64 new keys for your one-time pad. A hash is a key only if:
 *
 * It contains three of the same character in a row, like 777. Only consider the first such triplet in a hash.
 * One of the next 1000 hashes in the stream contains that same character five times in a row, like 77777.
 *
 * Considering future hashes for five-of-a-kind sequences does not cause those hashes to be skipped; instead, regardless of whether the current hash is a key, always resume testing for keys starting with the very next hash.
 *
 * For example, if the pre-arranged salt is abc:
 *
 * The first index which produces a triple is 18, because the MD5 hash of abc18 contains ...cc38887a5.... However, index 18 does not count as a key for your one-time pad, because none of the next thousand hashes (index 19 through index 1018) contain 88888.
 * The next index which produces a triple is 39; the hash of abc39 contains eee. It is also the first key: one of the next thousand hashes (the one at index 816) contains eeeee.
 * None of the next six triples are keys, but the one after that, at index 92, is: it contains 999 and index 200 contains 99999.
 * Eventually, index 22728 meets all of the criteria to generate the 64th key.
 *
 * So, using our example salt of abc, index 22728 produces the 64th key.
 *
 * Given the actual salt in your puzzle input, what index produces your 64th one-time pad key?
 *
 * Your puzzle answer was 23890.
 * --- Part Two ---
 *
 * Of course, in order to make this process even more secure, you've also implemented key stretching.
 *
 * Key stretching forces attackers to spend more time generating hashes. Unfortunately, it forces everyone else to spend more time, too.
 *
 * To implement key stretching, whenever you generate a hash, before you use it, you first find the MD5 hash of that hash, then the MD5 hash of that hash, and so on, a total of 2016 additional hashings. Always use lowercase hexadecimal representations of hashes.
 *
 * For example, to find the stretched hash for index 0 and salt abc:
 *
 * Find the MD5 hash of abc0: 577571be4de9dcce85a041ba0410f29f.
 * Then, find the MD5 hash of that hash: eec80a0c92dc8a0777c619d9bb51e910.
 * Then, find the MD5 hash of that hash: 16062ce768787384c81fe17a7a60c7e3.
 * ...repeat many times...
 * Then, find the MD5 hash of that hash: a107ff634856bb300138cac6568c0f24.
 *
 * So, the stretched hash for index 0 in this situation is a107ff.... In the end, you find the original hash (one use of MD5), then find the hash-of-the-previous-hash 2016 times, for a total of 2017 uses of MD5.
 *
 * The rest of the process remains the same, but now the keys are entirely different. Again for salt abc:
 *
 * The first triple (222, at index 5) has no matching 22222 in the next thousand hashes.
 * The second triple (eee, at index 10) hash a matching eeeee at index 89, and so it is the first key.
 * Eventually, index 22551 produces the 64th key (triple fff with matching fffff at index 22859.
 *
 * Given the actual salt in your puzzle input and using 2016 extra MD5 calls of key stretching, what index now produces your 64th one-time pad key?
 *
 * Your puzzle answer was 22696.
 */
public class Day14 {

    public static MessageDigest m;
    public static Pattern p1 = Pattern.compile(".*?(.)\\1{2}.*?");
    public static Pattern p2 = Pattern.compile(".*?(.)\\1{4}.*?");

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String salt = "ahsbgdzn";
        List<Key> keys1 = getKeys(salt, 64, false);
        printKeys(keys1);
        List<Key> keys2 = getKeys(salt, 64, true);
        printKeys(keys2);
    }

    public static void printKeys(List<Key> keys) {
        Collections.sort(keys, Comparator.comparing(k2 -> k2.idx));
        for (int j = 0; j < keys.size(); j++) {
            Key k = keys.get(j);
            System.out.print((j + 1) + " ");
            System.out.println(k);
        }
    }

    public static List<Key> getKeys(String salt, int limit, boolean stretched) throws NoSuchAlgorithmException {
        m = MessageDigest.getInstance("MD5");

        Queue<Key> potentialKeys = new LinkedList<>();
        List<Key> actualKeys = new ArrayList<>();
        int i = 0;
        int keyCount = 0;

        while (keyCount < limit) {
            String dat = salt + i;

            String md5;
            if (stretched) {
                md5 = getStretchedMd5(dat, 2016);
            } else {
                md5 = getMd5(dat);
            }

            Matcher m1 = p1.matcher(md5);
            if (m1.find()) {
                Key k = new Key();
                k.md5 = md5;
                k.idx = i;
                k.c = m1.group(1).charAt(0);
                potentialKeys.add(k);
            }

            Matcher m2 = p2.matcher(md5);
            Set<Character> cs = new HashSet<>();
            while (m2.find()) {
                cs.add(m2.group(1).charAt(0));
            }

            if (cs.size() > 0) {
                Iterator<Key> it = potentialKeys.iterator();
                while (it.hasNext()) {
                    Key k = it.next();
                    if (i == k.idx) {
                        continue;
                    }
                    if (i - k.idx > 1000) {
                        it.remove();
                        continue;
                    }
                    if (cs.contains(k.c)) {
                        it.remove();
                        keyCount++;
                        k.matchMd5 = md5;
                        k.matchIdx = i;
                        actualKeys.add(k);
                    }
                }
            }
            i++;
        }
        return actualKeys;
    }

    public static class Key {
        public Integer idx;
        public String md5;
        public char c;

        public Integer matchIdx;
        public String matchMd5;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Key{");
            sb.append("idx=").append(idx);
            sb.append(", md5='").append(md5).append('\'');
            sb.append(", c=").append(c);
            sb.append(", matchIdx=").append(matchIdx);
            sb.append(", matchMd5='").append(matchMd5).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    public static String getStretchedMd5(String str, int numRepeat) {
        String md5 = getMd5(str);
        for (int i = 0; i < numRepeat; i++) {
            md5 = getMd5(md5);
        }
        return md5;
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