package jw.problems.adventofcode.aoc2018;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://adventofcode.com/2018/day/7
 *
 * --- Day 7: The Sum of Its Parts ---
 *
 * You find yourself standing on a snow-covered coastline; apparently, you landed a little off course. The region is too hilly to see the North Pole from here, but you do spot some Elves that seem to be trying to unpack something that washed ashore. It's quite cold out, so you decide to risk creating a paradox by asking them for directions.
 *
 * "Oh, are you the search party?" Somehow, you can understand whatever Elves from the year 1018 speak; you assume it's Ancient Nordic Elvish. Could the device on your wrist also be a translator? "Those clothes don't look very warm; take this." They hand you a heavy coat.
 *
 * "We do need to find our way back to the North Pole, but we have higher priorities at the moment. You see, believe it or not, this box contains something that will solve all of Santa's transportation problems - at least, that's what it looks like from the pictures in the instructions." It doesn't seem like they can read whatever language it's in, but you can: "Sleigh kit. Some assembly required."
 *
 * "'Sleigh'? What a wonderful name! You must help us assemble this 'sleigh' at once!" They start excitedly pulling more parts out of the box.
 *
 * The instructions specify a series of steps and requirements about which steps must be finished before others can begin (your puzzle input). Each step is designated by a single letter. For example, suppose you have the following instructions:
 *
 * Step C must be finished before step A can begin.
 * Step C must be finished before step F can begin.
 * Step A must be finished before step B can begin.
 * Step A must be finished before step D can begin.
 * Step B must be finished before step E can begin.
 * Step D must be finished before step E can begin.
 * Step F must be finished before step E can begin.
 *
 * Visually, these requirements look like this:
 *
 *
 * -->A--->B--
 * /    \      \
 * C      -->D----->E
 * \           /
 * ---->F-----
 *
 * Your first goal is to determine the order in which the steps should be completed. If more than one step is ready, choose the step which is first alphabetically. In this example, the steps would be completed as follows:
 *
 * Only C is available, and so it is done first.
 * Next, both A and F are available. A is first alphabetically, so it is done next.
 * Then, even though F was available earlier, steps B and D are now also available, and B is the first alphabetically of the three.
 * After that, only D and F are available. E is not available because only some of its prerequisites are complete. Therefore, D is completed next.
 * F is the only choice, so it is done next.
 * Finally, E is completed.
 *
 * So, in this example, the correct order is CABDFE.
 *
 * In what order should the steps in your instructions be completed?
 *
 * Your puzzle answer was OVXCKZBDEHINPFSTJLUYRWGAMQ.
 * --- Part Two ---
 *
 * As you're about to begin construction, four of the Elves offer to help. "The sun will set soon; it'll go faster if we work together." Now, you need to account for multiple people working on steps simultaneously. If multiple steps are available, workers should still begin them in alphabetical order.
 *
 * Each step takes 60 seconds plus an amount corresponding to its letter: A=1, B=2, C=3, and so on. So, step A takes 60+1=61 seconds, while step Z takes 60+26=86 seconds. No time is required between steps.
 *
 * To simplify things for the example, however, suppose you only have help from one Elf (a total of two workers) and that each step takes 60 fewer seconds (so that step A takes 1 second and step Z takes 26 seconds). Then, using the same instructions as above, this is how each second would be spent:
 *
 * Second   Worker 1   Worker 2   Done
 * 0        C          .
 * 1        C          .
 * 2        C          .
 * 3        A          F       C
 * 4        B          F       CA
 * 5        B          F       CA
 * 6        D          F       CAB
 * 7        D          F       CAB
 * 8        D          F       CAB
 * 9        D          .       CABF
 * 10        E          .       CABFD
 * 11        E          .       CABFD
 * 12        E          .       CABFD
 * 13        E          .       CABFD
 * 14        E          .       CABFD
 * 15        .          .       CABFDE
 *
 * Each row represents one second of time. The Second column identifies how many seconds have passed as of the beginning of that second. Each worker column shows the step that worker is currently doing (or . if they are idle). The Done column shows completed steps.
 *
 * Note that the order of the steps has changed; this is because steps now take time to finish and multiple workers can begin multiple steps simultaneously.
 *
 * In this example, it would take 15 seconds for two workers to complete these steps.
 *
 * With 5 workers and the 60+ second step durations described above, how long will it take to complete all of the steps?
 *
 * Your puzzle answer was 955.
 */
public class Day7 {
    public static Pattern p = Pattern.compile("Step ([A-Z]) must be finished before step ([A-Z]) can begin.");

    public static void main(String[] args) throws FileNotFoundException {
//        part1(args);
        part2(args);
    }

    private static class Node {
        public int id;
        public int t;

        public Node(int id, int t) {
            this.id = id;
            this.t = t;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Node{");
            sb.append("id=").append(id);
            sb.append("/").append((char) ('A' + id));
            sb.append(", t=").append(t);
            sb.append('}');
            return sb.toString();
        }
    }

    public static void part2(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2018/in7"));
        int[][] graph = new int[26][26];
        int numWorkers = 5;

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = p.matcher(line);
            m.matches();
            int from = m.group(1).charAt(0) - 'A';
            int to = m.group(2).charAt(0) - 'A';
            graph[from][to] = 1;
        }

        List<Node> working = new LinkedList<>();
        for (int i = 0; i < graph[0].length; i++) {
            if (noIncoming(graph, i)) {
                working.add(new Node(i, i + 61));
            }
        }
        Collections.sort(working, Comparator.comparingInt(o -> o.t));

        int time = 0;
        List<Node> todo = new LinkedList<>();
        while (!working.isEmpty()) {
            Node n = working.get(0);
            working.remove(0);
            time += n.t;
//            System.out.println("time=" + time + ": " + n + " done");

            for (Node node : working) {
                node.t -= n.t;
            }

            for (int i = 0; i < graph[n.id].length; i++) {
                if (graph[n.id][i] > 0) {
                    graph[n.id][i] = 0;
                    if (noIncoming(graph, i)) {
                        todo.add(new Node(i, i + 61));
                    }
                }
            }

            while (working.size() < numWorkers && todo.size() > 0) {
//                System.out.println("adding " + todo.get(0));
                working.add(todo.get(0));
                todo.remove(0);
            }
            if (todo.size() > 0) {
                System.out.println("q full");
            }

            Collections.sort(working, Comparator.comparingInt(o -> o.t));
        }

        System.out.println(time);
    }

    public static void part1(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2018/in7"));

        int[][] graph = new int[26][26];
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = p.matcher(line);
            m.matches();
            int from = m.group(1).charAt(0) - 'A';
            int to = m.group(2).charAt(0) - 'A';
            graph[from][to] = 1;
        }

        List<Integer> q = new LinkedList<>();
        for (int i = 0; i < graph[0].length; i++) {
            if (noIncoming(graph, i)) {
                q.add(i);
            }
        }
        Collections.sort(q);

        List<Integer> res = new LinkedList<>();
        while (!q.isEmpty()) {
            int next = q.get(0);
            q.remove(0);
            res.add(next);
            for (int i = 0; i < graph[next].length; i++) {
                if (graph[next][i] > 0) {
                    graph[next][i] = 0;

                    if (noIncoming(graph, i)) {
                        q.add(i);
                    }
                }
            }
            Collections.sort(q);
        }

        pprintQ(res);
    }

    public static boolean noIncoming(int[][] graph, int i) {
        boolean toVisit = true;
        for (int j = 0; j < graph.length; j++) {
            if (graph[j][i] > 0) {
                toVisit = false;
                break;
            }
        }
        return toVisit;
    }

    public static void pprintQ(List<Integer> q) {
        for (Integer i : q) {
            System.out.print((char) ('A' + i));
        }
        System.out.println();
    }

    public static void pprintG(int[][] graph) {
        System.out.print(" ");
        for (int i = 0; i < graph.length; i++) {
            System.out.print((char) ('A' + i));
        }
        System.out.println();
        for (int i = 0; i < graph.length; i++) {
            System.out.print((char) ('A' + i));
            for (int j = 0; j < graph[i].length; j++) {
                System.out.print(graph[i][j]);
            }
            System.out.println();
        }
    }
}
