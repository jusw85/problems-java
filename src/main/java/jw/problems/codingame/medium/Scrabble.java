package jw.problems.codingame.medium;

import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * https://www.codingame.com/ide/puzzle/scrabble
 *
 * The Goal
 *
 * When playing Scrabble©, each player draws 7 letters and must find a word that scores the most points using these letters.
 *
 * A player doesn't necessarily have to make a 7-letter word; the word can be shorter. The only constraint is that the word must be made using the 7 letters which the player has drawn.
 *
 * For example, with the letters  etaenhs, some possible words are: ethane, hates, sane, ant.
 *
 * Your objective is to find the word that scores the most points using the available letters (1 to 7 letters).
 * Rules
 *
 * In Scrabble©, each letter is weighted with a score depending on how difficult it is to place that letter in a word. You will see below a table showing the points corresponding to each letter:
 *
 * Letters 	Points
 * e, a, i, o, n, r, t, l, s, u 	1
 * d, g 	2
 * b, c, m, p 	3
 * f, h, v, w, y 	4
 * k 	5
 * j, x 	8
 * q, z 	10
 *
 * The word banjo earns you 3 + 1 + 1 + 8 + 1 = 14 points.
 *
 * A dictionary of authorized words is provided as input for the program. The program must find the word in the dictionary which wins the most points for the seven given letters (a letter can only be used once). If two words win the same number of points, then the word which appears first in the order of the given dictionary should be chosen.
 *
 *
 * All words will only be composed of alphabetical characters in lower case. There will always be at least one possible word.
 * Game Input
 * Input
 *
 * Line 1: the number N of words in the dictionary
 *
 * N following lines: the words in the dictionary. One word per line.
 *
 * Last line: the 7 letters available.
 * Output
 * The word that scores the most points using the available letters (1 to 7 letters). The word must belong to the dictionary. Each letter must be used at most once in the solution. There is always a solution.
 * Constraints
 * 0 < N < 100000
 * Words in the dictionary have a maximum length of 30 characters.
 */
public class Scrabble {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        if (in.hasNextLine()) {
            in.nextLine();
        }

        TreeSet<Word> pq = new TreeSet<>((w1, w2) -> {
            if (w1.points < w2.points) {
                return 1;
            } else if (w1.points > w2.points) {
                return -1;
            } else if (w1.order < w2.order) {
                return -1;
            } else if (w1.order > w2.order) {
                return 1;
            } else {
                return 0;
            }
        });

        for (int i = 0; i < N; i++) {
            String W = in.nextLine();
            if (W.length() > 7) {
                continue;
            }
            Word word = new Word();
            word.name = W;
            word.points = wordToPoints(W);
            word.order = i;
            pq.add(word);
        }

        String LETTERS = in.nextLine();
        char[] chs = LETTERS.toCharArray();
        Arrays.sort(chs);
        String sortedLetters = new String(chs);
        for (Word w : pq) {
            if (stringContains(sortedLetters, w.name)) {
                System.out.println(w.name);
                break;
            }
        }
    }

    public static boolean stringContains(String parent, String subset) {
        char[] chs = subset.toCharArray();
        Arrays.sort(chs);
        StringBuilder sb = new StringBuilder();
        sb.append(".*");
        for (char c : chs) {
            sb.append(c).append(".*");
        }
        return parent.matches(sb.toString());
    }

    public static class Word {
        public String name;
        public int points;
        public int order;
    }

    public static int wordToPoints(String word) {
        int points = 0;
        for (char c : word.toCharArray()) {
            switch (c) {
                case 'e':
                case 'a':
                case 'i':
                case 'o':
                case 'n':
                case 'r':
                case 't':
                case 'l':
                case 's':
                case 'u':
                    points += 1;
                    break;
                case 'd':
                case 'g':
                    points += 2;
                    break;
                case 'b':
                case 'c':
                case 'm':
                case 'p':
                    points += 3;
                    break;
                case 'f':
                case 'h':
                case 'v':
                case 'w':
                case 'y':
                    points += 4;
                    break;
                case 'k':
                    points += 5;
                    break;
                case 'j':
                case 'x':
                    points += 8;
                    break;
                case 'q':
                case 'z':
                    points += 10;
                    break;
            }
        }
        return points;
    }

}
