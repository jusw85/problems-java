package jw.problems;

/**
 * Brute forcing GCHQ 2015 Part 4
 * http://www.cub-zone-often.org.uk/layered/
 * https://www.gchq.gov.uk/information/director-gchqs-christmas-puzzle-part-4
 * https://www.reddit.com/r/puzzles/comments/3w6ja9/gchqs_christmas_puzzler_thread_spoilers_tagged/cxtysm6/
 */
public class Gchq2015 {

    public static void main(String[] args) {
        int i = 52;
        String j = "30.87";
        for (int k = 0; k < 256; k++) {
            String dat = i + "\0" + j + "\0" + k;
            if (hash(dat)) {
                System.out.println(i + "." + j + "." + k);
                break;
            }
        }
    }

    public static boolean hash(String dat) {
        int resultA = (int) 3141592654L;
        int resultB = 1234567890;
        for (int i = 0; i < 2; i++) {
            int initA = resultA;
            int initB = resultB;
            for (int j = 0; j < dat.length(); j++) {
                resultA += dat.toLowerCase().charAt(j);
                resultB = (resultA * 31) ^ resultB;
                int tmp = resultA & resultA;
                resultA = resultB & resultB;
                resultB = tmp;
            }
            resultA = resultA ^ initA;
            resultB = resultB ^ initB;
        }
        if (resultA == 1824745082L && resultB == 560037081L) {
            return true;
        }
        return false;
    }
}
