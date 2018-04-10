package jw.problems.aoc2016;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http://adventofcode.com/2016/day/10
 *
 * --- Day 10: Balance Bots ---
 *
 * You come upon a factory in which many robots are zooming around handing small microchips to each other.
 *
 * Upon closer examination, you notice that each bot only proceeds when it has two microchips, and once it does, it gives each one to a different bot or puts it in a marked "output" bin. Sometimes, bots take microchips from "input" bins, too.
 *
 * Inspecting one of the microchips, it seems like they each contain a single number; the bots must use some logic to decide what to do with each chip. You access the local control computer and download the bots' instructions (your puzzle input).
 *
 * Some of the instructions specify that a specific-valued microchip should be given to a specific bot; the rest of the instructions indicate what a given bot should do with its lower-value or higher-value chip.
 *
 * For example, consider the following instructions:
 *
 * value 5 goes to bot 2
 * bot 2 gives low to bot 1 and high to bot 0
 * value 3 goes to bot 1
 * bot 1 gives low to output 1 and high to bot 0
 * bot 0 gives low to output 2 and high to output 0
 * value 2 goes to bot 2
 *
 * Initially, bot 1 starts with a value-3 chip, and bot 2 starts with a value-2 chip and a value-5 chip.
 * Because bot 2 has two microchips, it gives its lower one (2) to bot 1 and its higher one (5) to bot 0.
 * Then, bot 1 has two microchips; it puts the value-2 chip in output 1 and gives the value-3 chip to bot 0.
 * Finally, bot 0 has two microchips; it puts the 3 in output 2 and the 5 in output 0.
 *
 * In the end, output bin 0 contains a value-5 microchip, output bin 1 contains a value-2 microchip, and output bin 2 contains a value-3 microchip. In this configuration, bot number 2 is responsible for comparing value-5 microchips with value-2 microchips.
 *
 * Based on your instructions, what is the number of the bot that is responsible for comparing value-61 microchips with value-17 microchips?
 *
 * Your puzzle answer was 181.
 * --- Part Two ---
 *
 * What do you get if you multiply together the values of one chip in each of outputs 0, 1, and 2?
 *
 * Your puzzle answer was 12567.
 */
public class Day10 {

    public static void main(String[] args) throws FileNotFoundException {
        Pattern p1 = Pattern.compile("bot (\\d+) gives low to ([^\\s]+) (\\d+) and high to ([^\\s]+) (\\d+)");
        Pattern p2 = Pattern.compile("value (\\d+) goes to bot (\\d+)");
        Factory f = new Factory();
        List<Point> toProcess = new ArrayList<>();

        Scanner sc = new Scanner(new File("./etc/aoc2016/in10"));
        while (sc.hasNextLine()) {
            String str = sc.nextLine();
            Matcher m1 = p1.matcher(str);
            Matcher m2 = p2.matcher(str);
            if (m1.matches()) {
                int id = Integer.parseInt(m1.group(1));
                int low = Integer.parseInt(m1.group(3));
                int high = Integer.parseInt(m1.group(5));
                Node n = new Node(id, low, strToType(m1.group(2)), high, strToType(m1.group(4)));
                f.addNode(n);
            } else if (m2.matches()) {
                int val = Integer.parseInt(m2.group(1));
                int to = Integer.parseInt(m2.group(2));
                toProcess.add(new Point(val, to));
            }
        }
        sc.close();

        for (Point p : toProcess) {
            f.addValToBot(p.x, p.y);
        }
        f.process();
        f.printResult();
    }

    public static char strToType(String str) {
        if (str.equals("bot")) {
            return 'b';
        } else { // if (str.equals("output")) {
            return 'o';
        }
    }

    public static class Factory {
        private Map<Integer, Node> nodes = new HashMap<>();

        private Set<Integer> tracker61 = new HashSet<>();
        private Set<Integer> tracker17 = new HashSet<>();
        private Map<Integer, String> outputs = new HashMap<>();

        public void addNode(Node n) {
            nodes.put(n.id, n);
        }

        public void addValToBot(int val, int nodeId) {
            nodes.get(nodeId).addVal(val);
            if (val == 61) {
                tracker61.add(nodeId);
            } else if (val == 17) {
                tracker17.add(nodeId);
            }
        }

        public void addValToOutput(int val, int outputId) {
            if (outputs.containsKey(outputId)) {
                outputs.put(outputId, outputs.get(outputId) + "," + val);
            } else {
                outputs.put(outputId, String.valueOf(val));
            }
        }

        public void process() {
            boolean changed = true;
            while (changed) {
                changed = false;
                for (Node n : nodes.values()) {
                    if (n.canProcess()) {
                        changed = true;
                        Point outputs = n.consumeInputs();
                        if (n.outType[0] == 'b') {
                            addValToBot(outputs.x, n.out[0]);
                        } else {
                            addValToOutput(outputs.x, n.out[0]);
                        }
                        if (n.outType[1] == 'b') {
                            addValToBot(outputs.y, n.out[1]);
                        } else {
                            addValToOutput(outputs.y, n.out[1]);
                        }
                    }
                }
            }
        }

        public void printResult() {
            tracker61.retainAll(tracker17);
            System.out.println(tracker61.toArray()[0]);

            int n1 = Integer.parseInt(outputs.get(0));
            int n2 = Integer.parseInt(outputs.get(1));
            int n3 = Integer.parseInt(outputs.get(2));
            System.out.println(n1 * n2 * n3);
            System.out.println();

            for (Map.Entry<Integer, String> e : outputs.entrySet()) {
                System.out.println(e.getKey() + " " + e.getValue());
            }
        }
    }

    public static class Node {
        public int id;
        public char[] outType = new char[2];
        public int[] out = new int[2];

        private int[] in = new int[2];
        private int counter = 0;

        public Node(int id, int o1, char ot1, int o2, char ot2) {
            this.id = id;
            this.out[0] = o1;
            this.out[1] = o2;
            this.outType[0] = ot1;
            this.outType[1] = ot2;
        }

        public void addVal(int val) {
            if (counter > 1)
                return;
            in[counter++] = val;
        }

        public boolean canProcess() {
            return counter >= 2;
        }

        public Point consumeInputs() {
            counter = 0;
            if (in[0] < in[1]) {
                return new Point(in[0], in[1]);
            } else {
                return new Point(in[1], in[0]);
            }
        }
    }

}