package jw.problems.aoc2017;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http://adventofcode.com/2017/day/12
 *
 * --- Day 12: Digital Plumber ---
 *
 * Walking along the memory banks of the stream, you find a small village that is experiencing a little confusion: some programs can't communicate with each other.
 *
 * Programs in this village communicate using a fixed system of pipes. Messages are passed between programs using these pipes, but most programs aren't connected to each other directly. Instead, programs pass messages between each other until the message reaches the intended recipient.
 *
 * For some reason, though, some of these messages aren't ever reaching their intended recipient, and the programs suspect that some pipes are missing. They would like you to investigate.
 *
 * You walk through the village and record the ID of each program and the IDs with which it can communicate directly (your puzzle input). Each program has one or more programs with which it can communicate, and these pipes are bidirectional; if 8 says it can communicate with 11, then 11 will say it can communicate with 8.
 *
 * You need to figure out how many programs are in the group that contains program ID 0.
 *
 * For example, suppose you go door-to-door like a travelling salesman and record the following list:
 *
 * 0 <-> 2
 * 1 <-> 1
 * 2 <-> 0, 3, 4
 * 3 <-> 2, 4
 * 4 <-> 2, 3, 6
 * 5 <-> 6
 * 6 <-> 4, 5
 *
 * In this example, the following programs are in the group that contains program ID 0:
 *
 * Program 0 by definition.
 * Program 2, directly connected to program 0.
 * Program 3 via program 2.
 * Program 4 via program 2.
 * Program 5 via programs 6, then 4, then 2.
 * Program 6 via programs 4, then 2.
 *
 * Therefore, a total of 6 programs are in this group; all but program 1, which has a pipe that connects it to itself.
 *
 * How many programs are in the group that contains program ID 0?
 *
 * Your puzzle answer was 288.
 * --- Part Two ---
 *
 * There are more programs than just the ones in the group containing program ID 0. The rest of them have no way of reaching that group, and still might have no way of reaching each other.
 *
 * A group is a collection of programs that can all communicate via pipes either directly or indirectly. The programs you identified just a moment ago are all part of the same group. Now, they would like you to determine the total number of groups.
 *
 * In the example above, there were 2 groups: one consisting of programs 0,2,3,4,5,6, and the other consisting solely of program 1.
 *
 * How many groups are there in total?
 *
 * Your puzzle answer was 211.
 */
public class Day12 {
    public static Pattern p = Pattern.compile("(\\d+) <-> (.+)");

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2017/in12"));
        Map<String, Node> nodes = new HashMap<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = p.matcher(line);
            m.matches();
            String id = m.group(1);

            Node n;
            if (nodes.containsKey(id)) {
                n = nodes.get(id);
            } else {
                n = new Node(id);
                nodes.put(id, n);
            }

            String children = m.group(2);
            for (String child : children.split(",")) {
                child = child.trim();

                Node nc;
                if (nodes.containsKey(child)) {
                    nc = nodes.get(child);
                } else {
                    nc = new Node(child);
                    nodes.put(child, nc);
                }
                n.links.add(nc);
                nc.links.add(n);
            }
        }
        sc.close();

        part1(nodes);
        part2(nodes);
    }

    public static void part2(Map<String, Node> nodes) {
        int count = 0;
        while (nodes.size() > 0) {
            Node root = nodes.values().iterator().next();
            Set<String> visited = new HashSet<>();
            Queue<Node> toVisit = new LinkedList<>();
            toVisit.add(root);
            while (toVisit.size() > 0) {
                Node n = toVisit.poll();
                for (Node link : n.links) {
                    if (!visited.contains(link.id)) {
                        toVisit.add(link);
                    }
                }
                visited.add(n.id);
            }
            for (String nid : visited) {
                nodes.remove(nid);
            }
            count++;
        }
        System.out.println(count);
    }

    public static void part1(Map<String, Node> nodes) {
        Set<String> visited = new HashSet<>();
        Queue<Node> toVisit = new LinkedList<>();
        toVisit.add(nodes.get("0"));
        while (toVisit.size() > 0) {
            Node n = toVisit.poll();
            for (Node link : n.links) {
                if (!visited.contains(link.id)) {
                    toVisit.add(link);
                }
            }
            visited.add(n.id);
        }
        System.out.println(visited.size());
    }

    public static class Node {
        public String id;
        public Set<Node> links = new HashSet<>();

        public Node(String id) {
            this.id = id;
        }
    }
}