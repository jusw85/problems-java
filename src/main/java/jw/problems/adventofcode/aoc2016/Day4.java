package jw.problems.adventofcode.aoc2016;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http://adventofcode.com/2016/day/4
 *
 * --- Day 4: Security Through Obscurity ---
 *
 * Finally, you come across an information kiosk with a list of rooms. Of course, the list is encrypted and full of decoy data, but the instructions to decode the list are barely hidden nearby. Better remove the decoy data first.
 *
 * Each room consists of an encrypted name (lowercase letters separated by dashes) followed by a dash, a sector ID, and a checksum in square brackets.
 *
 * A room is real (not a decoy) if the checksum is the five most common letters in the encrypted name, in order, with ties broken by alphabetization. For example:
 *
 * aaaaa-bbb-z-y-x-123[abxyz] is a real room because the most common letters are a (5), b (3), and then a tie between x, y, and z, which are listed alphabetically.
 * a-b-c-d-e-f-g-h-987[abcde] is a real room because although the letters are all tied (1 of each), the first five are listed alphabetically.
 * not-a-real-room-404[oarel] is a real room.
 * totally-real-room-200[decoy] is not.
 *
 * Of the real rooms from the list above, the sum of their sector IDs is 1514.
 *
 * What is the sum of the sector IDs of the real rooms?
 *
 * Your puzzle answer was 158835.
 * --- Part Two ---
 *
 * With all the decoy data out of the way, it's time to decrypt this list and get moving.
 *
 * The room names are encrypted by a state-of-the-art shift cipher, which is nearly unbreakable without the right software. However, the information kiosk designers at Easter Bunny HQ were not expecting to deal with a master cryptographer like yourself.
 *
 * To decrypt a room name, rotate each letter forward through the alphabet a number of times equal to the room's sector ID. A becomes B, B becomes C, Z becomes A, and so on. Dashes become spaces.
 *
 * For example, the real name for qzmt-zixmtkozy-ivhz-343 is very encrypted name.
 *
 * What is the sector ID of the room where North Pole objects are stored?
 *
 * Your puzzle answer was 993.
 */
public class Day4 {

    public static void main(String[] args) throws FileNotFoundException {
        Pattern p = Pattern.compile("(.*)-(\\d+)\\[(.*)\\]");

        Scanner sc = new Scanner(new File("./etc/aoc2016/in4"));
        int count = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = p.matcher(line);
            m.matches();

            String name = m.group(1);
            int id = Integer.parseInt(m.group(2));
            String cksum = m.group(3);

            if (getCksum(name).equals(cksum)) {
                System.out.println(rotate(name, id) + " " + id);
                count += id;
            }
        }
        System.out.println(count);
        sc.close();
    }

    public static String getCksum(String str) {
        Map<Character, Integer> map = new LinkedHashMap<>();
        for (char c = 'a'; c <= 'z'; c++) {
            map.put(c, 0);
        }

        for (char c : str.toCharArray()) {
            if (c != '-') {
                map.put(c, map.get(c) + 1);
            }
        }

        List<Map.Entry<Character, Integer>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, Collections.reverseOrder(
                Comparator.comparing(Map.Entry::getValue)));

        String cksum = "";
        for (int i = 0; i < 5; i++) {
            cksum += list.get(i).getKey();
        }
        return cksum;
    }

    public static String rotate(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c == '-') {
                sb.append(' ');
            } else {
                char nc = (char) ((((c - 'a') + count) % 26) + 'a');
                sb.append(nc);
            }
        }
        return sb.toString();
    }

}