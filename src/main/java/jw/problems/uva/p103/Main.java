package jw.problems.uva.p103;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=3&page=show_problem&problem=39
 *
 * ./etc/uva/p103.pdf
 */
public class Main {

    private int numBoxes;
    private int dims;
    private int[][] boxes;
    private boolean[][] connected;

    public Main(int numBoxes, int dims) {
        this.numBoxes = numBoxes;
        this.dims = dims;

        boxes = new int[numBoxes][dims];
        connected = new boolean[numBoxes][numBoxes];
    }

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = in.readLine()) != null && line.length() > 0) {
            StringTokenizer token = new StringTokenizer(line);
            int numBoxes = Integer.parseInt(token.nextToken());
            int dims = Integer.parseInt(token.nextToken());
            Main p = new Main(numBoxes, dims);
            for (int i = 0; i < numBoxes; i++) {
                line = in.readLine();
                token = new StringTokenizer(line);
                int[] vals = new int[dims];
                for (int j = 0; j < dims; j++) {
                    vals[j] = Integer.parseInt(token.nextToken());
                }
                p.addBox(i, vals);
            }
            p.solve();
        }
    }

    public void solve() {
        int[] numIncoming = new int[numBoxes];
        for (int i = 0; i < connected.length; i++) {
            for (int j = 0; j < connected.length; j++) {
                if (contains(i, j)) {
                    connected[i][j] = true;
                    numIncoming[j]++;
                }
            }
        }

        int[] depths = new int[numBoxes];
        int[] parents = new int[numBoxes];
        Queue<Integer> toVisit = new LinkedList<>();
        for (int i = 0; i < numIncoming.length; i++) {
            if (numIncoming[i] == 0) {
                toVisit.add(i);
                depths[i] = 1;
                parents[i] = -1;
            }
        }

        while (toVisit.size() > 0) {
            int n = toVisit.poll();
            for (int i = 0; i < numBoxes; i++) {
                if (connected[n][i]) {
                    numIncoming[i]--;
                    if (numIncoming[i] <= 0) {
                        toVisit.add(i);
                        depths[i] = depths[n] + 1;
                        parents[i] = n;
                    }
                }
            }
        }

        int maxDepth = -1;
        int maxIdx = 0;
        for (int i = 0; i < depths.length; i++) {
            if (depths[i] > maxDepth) {
                maxDepth = depths[i];
                maxIdx = i;
            }
        }

        System.out.println(maxDepth);
        Deque<Integer> stack = new LinkedList<>();
        stack.addLast(maxIdx + 1);
        int i = maxIdx;
        while (parents[i] >= 0) {
            stack.addLast((parents[i] + 1));
            i = parents[i];
        }
        String prefix = "";
        while (!stack.isEmpty()) {
            System.out.print(prefix + stack.pollLast());
            prefix = " ";
        }
        System.out.println();
    }

    public void addBox(int num, int[] vals) {
        Arrays.sort(vals);
        boxes[num] = vals;
    }

    public boolean contains(int bn1, int bn2) {
        int[] b1 = boxes[bn1];
        int[] b2 = boxes[bn2];
        for (int i = 0; i < dims; i++) {
            if (b1[i] >= b2[i]) {
                return false;
            }
        }
        return true;
    }

}
