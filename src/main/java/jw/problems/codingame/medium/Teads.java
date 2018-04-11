package jw.problems.codingame.medium;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * https://www.codingame.com/ide/puzzle/teads-sponsored-contest
 *
 * The Goal
 * Here at Teads we know that to maximize the impact of an advertisement, the message needs to spread far and quickly.
 *
 * You are given data to calculate viral potential, represented by a network of people ready to relay a message to more people.
 * We can assume this network contains no cyclic relation.
 * For example, if person #1 has a relation with person #2 and if person #2 has a relation with person #3, then it is impossible for #3 to have a direct relation with #1.
 *
 * When an individual broadcasts a message, it is counted as a single step, meaning that the time it takes to broadcast the message is independant from the amount of people in direct relation with the individual. We will consider that this event will always take 1 hour.
 *
 * Here is an example with persons #1, #2, #3, #4, #5, #6, #7 and #8 linked like so:
 *
 *
 * Here, by choosing to start propagation of the message with person #1, 4 hours will be needed to share the message to the entire network:
 *
 *
 * 1.   #1 relays the message to #2
 * 2.   #2 then relays it to #3
 * 3.   #3 relays it to #4 and #7.
 * 4.   #4 relays it to #5 and #6, while #7 relays it to #8
 *
 * If we decide now to start the propagation with person #3, then only 2 hours are needed:
 *
 *
 * 1.   #3 relays the message to #2, #4 and #7
 * 2.   #2 relays it to #1 ; #4 relays it to #5 and #6 ; #7 relays it to #8
 *
 * In this exercice, your mission consists in finding the minimal amount of hours it would take for a message to propagate across the entire network given to you as input.
 * Game Input
 * Input
 *
 * Line 1: N the number of adjacency relations.
 *
 * N next lines: an adjancency relation between two people, expressed as X (space) Y, meaning that X is adjacent to Y.
 * Output
 * The minimal amount of steps required to completely propagate the advertisement.
 * Contraints
 * 0 < N< 150000
 * 0 ≤ X,Y < 200000
 */
public class Teads {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int num = in.nextInt(); // the number of adjacency relations

        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int i = 0; i < num; i++) {
            int xi = in.nextInt(); // the ID of a person which is adjacent to yi
            int yi = in.nextInt(); // the ID of a person which is adjacent to xi

            addEdge(graph, xi, yi);
            addEdge(graph, yi, xi);
        }

        Map<Integer, Deque<Integer>> pathMap = new HashMap<>();
        int rootId = graph.entrySet().iterator().next().getKey();
        buildPathMap(new LinkedList<>(), -1, rootId, graph, pathMap);

        int currentLongest = Integer.MAX_VALUE;
        while (true) {
            List<Integer> longestPaths = getLongestPaths(pathMap);
            int len = pathMap.get(longestPaths.get(0)).size();
            if (len >= currentLongest) {
                break;
            }
            currentLongest = len;

            int head = pathMap.get(longestPaths.get(0)).getFirst();
            for (int i = 1; i < longestPaths.size(); i++) {
                int head2 = pathMap.get(longestPaths.get(i)).getFirst();
                if (head2 != head) {
                    break;
                }
            }

            for (Map.Entry<Integer, Deque<Integer>> entry : pathMap.entrySet()) {
                Deque<Integer> path = entry.getValue();
                if (path.size() > 0 && path.peek() == head) {
                    path.removeFirst();
                } else {
                    path.addFirst(head);
                }
            }
        }
        System.out.println(currentLongest);
    }

    public static void addEdge(Map<Integer, List<Integer>> graph, int n1, int n2) {
        List<Integer> l;
        if (graph.containsKey(n1)) {
            l = graph.get(n1);
        } else {
            l = new ArrayList<>();
            graph.put(n1, l);
        }
        l.add(n2);
    }

    public static void buildPathMap(Deque<Integer> currPath, int prevNode, int currNode,
                                    Map<Integer, List<Integer>> graph, Map<Integer, Deque<Integer>> pathMap) {
        pathMap.put(currNode, currPath);
        for (int edge : graph.get(currNode)) {
            if (edge == prevNode) continue;
            Deque<Integer> newPath = new LinkedList<>(currPath);
            newPath.add(edge);
            buildPathMap(newPath, currNode, edge, graph, pathMap);
        }
    }

    public static List<Integer> getLongestPaths(Map<Integer, Deque<Integer>> pathMap) {
        int max = Integer.MIN_VALUE;
        List<Integer> l = new ArrayList<>();
        for (Map.Entry<Integer, Deque<Integer>> entry : pathMap.entrySet()) {
            int id = entry.getKey();
            int len = entry.getValue().size();
            if (len > max) {
                l.clear();
                l.add(id);
                max = len;
            } else if (len == max) {
                l.add(id);
            }
        }
        return l;
    }
}
