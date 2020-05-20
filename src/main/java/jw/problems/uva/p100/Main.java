package jw.problems.uva.p100;

import java.io.IOException;
import java.util.*;

/**
 * Template from https://onlinejudge.org/data/p100.java.html
 *
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=3&page=show_problem&problem=36
 *
 * ./etc/uva/p100.pdf
 */
public class Main {

    static String readln(int maxLg) { // utility function to read from stdin
        byte lin[] = new byte[maxLg];
        int lg = 0, car = -1;

        try {
            while (lg < maxLg) {
                car = System.in.read();
                if ((car < 0) || (car == '\n'))
                    break;
                lin[lg++] += car;
            }
        } catch (IOException e) {
            return (null);
        }

        if ((car < 0) && (lg == 0))
            return (null); // eof
        return (new String(lin, 0, lg));
    }

    public static void main(String[] args) {
        String input;
        StringTokenizer idata;
        int i, j, start, end;

        Main solver = new Main();
        while ((input = Main.readln(255)) != null) {
            idata = new StringTokenizer(input);
            i = Integer.parseInt(idata.nextToken());
            j = Integer.parseInt(idata.nextToken());
            if (i < j) {
                start = i;
                end = j;
            } else {
                start = j;
                end = i;
            }
            try {
                int max = solver.findMax(start, end);
                System.out.println(i + " " + j + " " + max);
            } catch (Exception e) {
                System.err.println("err");
            }
        }
    }

    private Map<Integer, Integer> cache;
    private Deque<Integer> path;

    public Main() {
        cache = new HashMap<>();
        cache.put(1, 1);
        path = new LinkedList<>();
    }

    public int findMax(int i, int j) {
        int max = -1;
        for (; i <= j; i++) {
            int cycleLength = getCycleLength(i);
            if (cycleLength > max) {
                max = cycleLength;
            }
        }
        return max;
    }

    public int getCycleLength(int n) {
        while (!cache.containsKey(n)) {
            path.addLast(n);
            if (n % 2 == 0) {
                n /= 2;
            } else {
                n = 3 * n + 1;
            }
        }

        Integer i;
        int length = cache.get(n);
        while ((i = path.pollLast()) != null) {
            cache.put(i, ++length);
        }
        return length;
    }

}
