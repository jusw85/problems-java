package jw.problems.aoc2017;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http://adventofcode.com/2017/day/7
 *
 * --- Day 7: Recursive Circus ---
 *
 * Wandering further through the circuits of the computer, you come upon a tower of programs that have gotten themselves into a bit of trouble. A recursive algorithm has gotten out of hand, and now they're balanced precariously in a large tower.
 *
 * One program at the bottom supports the entire tower. It's holding a large disc, and on the disc are balanced several more sub-towers. At the bottom of these sub-towers, standing on the bottom disc, are other programs, each holding their own disc, and so on. At the very tops of these sub-sub-sub-...-towers, many programs stand simply keeping the disc below them balanced but with no disc of their own.
 *
 * You offer to help, but first you need to understand the structure of these towers. You ask each program to yell out their name, their weight, and (if they're holding a disc) the names of the programs immediately above them balancing on that disc. You write this information down (your puzzle input). Unfortunately, in their panic, they don't do this in an orderly fashion; by the time you're done, you're not sure which program gave which information.
 *
 * For example, if your list is the following:
 *
 * pbga (66)
 * xhth (57)
 * ebii (61)
 * havc (66)
 * ktlj (57)
 * fwft (72) -> ktlj, cntj, xhth
 * qoyq (66)
 * padx (45) -> pbga, havc, qoyq
 * tknk (41) -> ugml, padx, fwft
 * jptl (61)
 * ugml (68) -> gyxo, ebii, jptl
 * gyxo (61)
 * cntj (57)
 *
 * ...then you would be able to recreate the structure of the towers that looks like this:
 *
 * gyxo
 * /
 * ugml - ebii
 * /      \
 * |         jptl
 * |
 * |         pbga
 * /        /
 * tknk --- padx - havc
 * \        \
 * |         qoyq
 * |
 * |         ktlj
 * \      /
 * fwft - cntj
 * \
 * xhth
 *
 * In this example, tknk is at the bottom of the tower (the bottom program), and is holding up ugml, padx, and fwft. Those programs are, in turn, holding up other programs; in this example, none of those programs are holding up any other programs, and are all the tops of their own towers. (The actual tower balancing in front of you is much larger.)
 *
 * Before you're ready to help them, you need to make sure your information is correct. What is the name of the bottom program?
 *
 * Your puzzle answer was hmvwl.
 * --- Part Two ---
 *
 * The programs explain the situation: they can't get down. Rather, they could get down, if they weren't expending all of their energy trying to keep the tower balanced. Apparently, one program has the wrong weight, and until it's fixed, they're stuck here.
 *
 * For any program holding a disc, each program standing on that disc forms a sub-tower. Each of those sub-towers are supposed to be the same weight, or the disc itself isn't balanced. The weight of a tower is the sum of the weights of the programs in that tower.
 *
 * In the example above, this means that for ugml's disc to be balanced, gyxo, ebii, and jptl must all have the same weight, and they do: 61.
 *
 * However, for tknk to be balanced, each of the programs standing on its disc and all programs above it must each match. This means that the following sums must all be the same:
 *
 * ugml + (gyxo + ebii + jptl) = 68 + (61 + 61 + 61) = 251
 * padx + (pbga + havc + qoyq) = 45 + (66 + 66 + 66) = 243
 * fwft + (ktlj + cntj + xhth) = 72 + (57 + 57 + 57) = 243
 *
 * As you can see, tknk's disc is unbalanced: ugml's stack is heavier than the other two. Even though the nodes above ugml are balanced, ugml itself is too heavy: it needs to be 8 units lighter for its stack to weigh 243 and keep the towers balanced. If this change were made, its weight would be 60.
 *
 * Given that exactly one program is the wrong weight, what would its weight need to be to balance the entire tower?
 *
 * Your puzzle answer was 1853.
 */
public class Day7 {

    public static Pattern p = Pattern.compile("(.+) \\((\\d+)\\)(?: -> (.*))?");

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2017/in7"));
        Map<String, Node> nodes = new HashMap<>();

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = p.matcher(line);
            m.matches();
            String id = m.group(1);
            int weight = Integer.parseInt(m.group(2));

            Node n;
            if (nodes.containsKey(id)) {
                n = nodes.get(id);
                n.weight = weight;
            } else {
                n = new Node(id, weight);
                nodes.put(id, n);
            }

            String children = m.group(3);
            if (children != null) {
                for (String child : children.split(",")) {
                    child = child.trim();

                    Node nc;
                    if (nodes.containsKey(child)) {
                        nc = nodes.get(child);
                    } else {
                        nc = new Node(child);
                        nodes.put(child, nc);
                    }
                    nc.parent = n;
                    n.children.add(nc);
                }
            }
        }
        sc.close();
        Node n = nodes.values().iterator().next();
        while (n.parent != null) {
            n = n.parent;
        }
        Node root = n;
        System.out.println(root);
        computeWeights(root);
    }

    public static void computeWeights(Node n) {
        if (n.children.size() <= 0) {
            n.combinedWeight = n.weight;
        }
        int childWeights = 0;
        int cw0 = 0;
        boolean imbalanced = false;
        for (Node nc : n.children) {
            computeWeights(nc);
            childWeights += nc.combinedWeight;

            if (cw0 <= 0) {
                cw0 = nc.combinedWeight;
            } else {
                if (nc.combinedWeight != cw0) {
                    imbalanced = true;
                }
            }
        }
        n.combinedWeight = childWeights + n.weight;
        if (imbalanced) {
            System.out.println(n.id);
            System.out.println(n.weight);
            System.out.println(n.combinedWeight);
            for (Node nc : n.children) {
                System.out.println(" -" + nc.id);
                System.out.println("  " + nc.weight);
                System.out.println("  " + nc.combinedWeight);
            }
        }
    }

    public static class Node {
        public String id;
        public int weight;
        public int combinedWeight;
        public Node parent;
        public List<Node> children = new ArrayList<>();

        public Node(String id) {
            this.id = id;
        }

        public Node(String id, int weight) {
            this.id = id;
            this.weight = weight;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Node{");
            sb.append("id='").append(id).append('\'');
            sb.append(", weight=").append(weight);
            sb.append(", combinedWeight=").append(combinedWeight);
            sb.append('}');
            return sb.toString();
        }
    }
}