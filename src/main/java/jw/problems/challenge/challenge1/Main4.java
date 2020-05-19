package jw.problems.challenge.challenge1;

import java.util.*;

/**
 * Question
 *
 * There are N students in a classroom. You will be given N of relationships with your friends.
 *
 * Each student has a student number ranging from 1 to N, and the student whose student number is i is called i.
 *
 * In this classroom, they have a routine they use to pass on information utilizing their contact network. They will pass on the information following the rules shown below:
 *
 * First, Student 1 will receive the information. At this stage, no one except for Student 1 knows about this information.
 * The person who received the information will choose one person who has not received it yet with equal probability then deliver the info to them.
 * This process ends when all of your friends learned about the info.
 *
 * Output the expected value of the number of people to deliver the information.
 * Errors are acceptable when the value of absolute errors or relative errors are no more than 10^{-6}.
 * Input
 *
 * Input will be given in the following format from Standard Input:
 *
 * N
 * c_{11} c_{12} c_{13} ... c_{1N}
 * c_{21} c_{22} c_{23} ... c_{2N}
 * :
 * c_{N1} c_{N2} c_{N3} ... c_{NN}
 *
 * On the first string, integer that indicates the number of people in the classroom, N(1≦N≦14), will be given.
 * From the second string to the N string, friendships will be given.
 * c_ij indicates the following about integers i and j:
 * If c_ij is Y, the student i and the student j are friends.
 * If c_ij is N, the student i and the student j are not friends.
 *
 * Output
 *
 * Output the expected value of the number of people to deliver the information.
 * Errors are acceptable when the value of absolute errors or relative errors are no more than 10^{-6}.
 * Also, make sure to insert a line break at the end of the output.
 * Input Example # 1
 *
 * in
 * 4
 * NYYY
 * YNNN
 * YNNY
 * YNYN
 *
 * Output Example #1
 *
 * 2.66666666666666666
 *
 * The number of people who received the information When Student 1 delivers the information to Student 2 is 2 people.
 * When Student 3 delivers the information to Student 4, the number of people who received the information is 3 people.
 *
 * Input Example #2
 *
 * 5
 * NNNNN
 * NNYYY
 * NYNYY
 * NYYNY
 * NYYYN
 *
 * Output Example #2
 *
 * 1
 *
 * Since Student 1 has no friend, the information will never reach anyone except for Student 1.
 *
 * Input Example #3
 *
 * 10
 * NNNNYNYNNY
 * NNYYNNYNYY
 * NYNYYYNYYN
 * NYYNYYYNYY
 * YNYYNYYYNN
 * NNYYYNYYYN
 * YYNYYYNYYN
 * NNYNYYYNYN
 * NYYYNYYYNY
 * YYNYNNNNYN
 *
 * Output Example# 3
 *
 * 9.5018896919296
 */
public class Main4 {

    private boolean[][] network;
    private int size;
    private Map<Integer, Map<List<Boolean>, Double>> expectations = new HashMap<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int numNodes = sc.nextInt();
        sc.nextLine();

        Main4 network = new Main4(numNodes);
        for (int i = 0; i < numNodes; i++) {
            String line = sc.nextLine();
            for (int j = 0; j < numNodes; j++)
                if (line.charAt(j) == 'Y')
                    network.connect(i, j);
        }
        sc.close();
        System.out.println(network.getExpectation(0));
    }

    public Main4(int size) {
        this.network = new boolean[size][size];
        this.size = size;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                network[i][j] = false;
            }
        }
    }

    public void connect(int i, int j) {
        network[i][j] = true;
    }

    public void disconnect(int i, int j) {
        network[i][j] = false;
    }

    public double getExpectation(int src) {
        Boolean[] visited = new Boolean[size];
        for (int i = 0; i < size; i++)
            visited[i] = false;
        return getExpectation(src, visited, 0);
    }

    private double getExpectation(int src, Boolean[] visited, int numVisited) {
        visited[src] = true;
        numVisited++;
        int numNeighbours = 0;
        double sumExpectation = 0;
        for (int i = 0; i < size; i++) {
            if (network[src][i] && !visited[i]) {
                numNeighbours++;
                double expectation = 0;
                if (!expectations.containsKey(i)) {
                    Map<List<Boolean>, Double> map = new HashMap<>();
                    expectations.put(i, map);
                }

                Map<List<Boolean>, Double> map = expectations.get(i);
                List<Boolean> visitedList = Arrays.asList(visited);
                if (map.containsKey(visitedList)) {
                    expectation = map.get(visitedList);
                } else {
                    expectation = getExpectation(i, visited.clone(), numVisited);
                    map.put(Arrays.asList(visited), expectation);
                }
                sumExpectation += expectation;
            }
        }
        return numNeighbours == 0 ? numVisited : sumExpectation / numNeighbours;
    }

    // Time limit exceeded
    private double getExpectationNaive(int src, boolean[] visited, int numVisited) {
        visited[src] = true;
        numVisited++;
        int numNeighbours = 0;
        double sumExpectation = 0;
        for (int i = 0; i < size; i++) {
            if (network[src][i] && !visited[i]) {
                numNeighbours++;
                sumExpectation += getExpectationNaive(i, visited.clone(), numVisited);
            }
        }
        return numNeighbours == 0 ? numVisited : sumExpectation / numNeighbours;
    }

}