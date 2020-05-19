package jw.problems.challenge.challenge1;

import java.util.Scanner;

/**
 * Question
 *
 * Your task is to connect N computers with cables that can communicate in both directions and to check if those computers can actually interact with each other.
 * Your work will follow the two instructions shown below:
 *
 * make A B time
 * Connect Computer A and Computer B with a cable.
 * This cable will become unavailable when the value is bigger than time seconds. If the value equals to time seconds, the cable is still available.
 * Multiple cables may be connected between aforementioned Computer A and B.
 * check A B time
 * Predict if computer A and B can communicate within time seconds with the connected cable(s).
 * Computer A and B may be connected through other computers.
 * If Computer A and B can communicate, output YES in one string. If they can’t, output NO in one string.
 * The cables that are connected by all the make instructions existing after particular check instruction are not available in time series.
 *
 * The instructions will be processed in the descending order of input.
 *
 * Determine if Computer A and B are able to communicate every time there is check instruction. If they can communicate, please output YES in one string. If not, output NO in one string.
 * Input
 *
 * Input will be given in the following format from Standard Input:
 *
 * N Q
 * S_1 A_1 B_1 time_1
 * :
 * S_Q A_Q B_Q time_Q
 *
 * On the first string, N(2≦N≦20), an integer which shows the number of computers, and Q(1≦Q≦500), another integer which shows the number of the instructions, will be given with a half-width break.
 * From the second string to the N-th string, you will be given instructions.
 * 1≦A_k , B_k≦N and 1≦time_k≦10^4 are guaranteed.
 * There are only two kinds of S_k: make or check.
 *
 * Output
 *
 * Determine if Computer A and B are able to communicate every time there is check instruction. If they can communicate, please output YES in one string. If not, output NO in one string.
 * Also, make sure to insert a line break at the end of the output.
 * Input Example # 1
 *
 * 3 5
 * make 1 2 1000
 * check 1 3 500
 * make 3 2 2000
 * check 1 3 500
 * check 1 3 1500
 *
 * Output Example #1
 *
 * NO
 * YES
 * NO
 *
 * Input Example #2
 *
 * 4 11
 * make 1 2 2000
 * make 2 3 3000
 * make 3 4 2500
 * check 1 4 1500
 * check 1 3 2000
 * check 1 3 2500
 * make 1 4 3000
 * check 1 3 2500
 * check 1 3 3000
 * make 2 4 3000
 * check 1 3 3000
 *
 * Output Example #2
 *
 * YES
 * YES
 * NO
 * YES
 * NO
 * YES
 */
public class Main3 {

    private int[][] network;
    private int size;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int numNodes = sc.nextInt();
        int numLines = sc.nextInt();
        sc.nextLine();

        Main3 network = new Main3(numNodes);
        for (int i = 0; i < numLines; i++) {
            Scanner sc2 = new Scanner(sc.nextLine());
            String type = sc2.next();
            int src = sc2.nextInt();
            int dest = sc2.nextInt();
            int cost = sc2.nextInt();
            if (type.equals("make")) {
                network.make(src, dest, cost);
            } else if (type.equals("check")) {
                boolean reachable = network.check(src, dest, cost);
                System.out.println(reachable ? "YES" : "NO");
            }
            sc2.close();
        }
        sc.close();
    }

    public Main3(int size) {
        this.network = new int[size][size];
        this.size = size;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j)
                    network[i][j] = 0;
                else
                    network[i][j] = -1;
            }
        }
    }

    /**
     * Make a connection from src to dest with timing cost
     * <p>
     * Assume offset by 1
     *
     * @param src
     * @param dest
     * @param cost
     */
    public void make(int src, int dest, int cost) {
        int currentCost = network[src - 1][dest - 1];
        if (currentCost < 0 || cost > currentCost) {
            network[src - 1][dest - 1] = cost;
            network[dest - 1][src - 1] = cost;
        }
    }

    /**
     * Check if connection possible from src to dest with minimum required
     * timing mincost
     * <p>
     * Assume offset by 1
     *
     * @param src
     * @param dest
     * @param mincost
     */
    public boolean check(int src, int dest, int mincost) {
        boolean[] visited = new boolean[size];
        for (int i = 0; i < size; i++)
            visited[i] = false;
        return check(src - 1, dest - 1, mincost, visited);
    }

    private boolean check(int src, int dest, int mincost, boolean[] visited) {
        if (src == dest)
            return true;
        visited[src] = true;
        for (int i = 0; i < size; i++) {
            if (src != i && network[src][i] >= mincost && !visited[i]) {
                if (check(i, dest, mincost, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

}