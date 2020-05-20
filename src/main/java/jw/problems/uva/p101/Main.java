package jw.problems.uva.p101;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=3&page=show_problem&problem=37
 *
 * ./etc/uva/p101.pdf
 */
public class Main {

    public static void main(String[] args) throws NumberFormatException, IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        int size = Integer.parseInt(input.readLine());
        Main world = new Main(size);
        String line;
        while (!(line = input.readLine()).equals("quit")) {
            StringTokenizer token = new StringTokenizer(line);
            String op1 = token.nextToken();
            int i = Integer.parseInt(token.nextToken());
            String op2 = token.nextToken();
            int j = Integer.parseInt(token.nextToken());

            if (op1.equals("move")) {
                if (op2.equals("onto")) {
                    world.moveOnto(i, j);
                } else {
                    world.moveOver(i, j);
                }
            } else {
                if (op2.equals("onto")) {
                    world.pileOnto(i, j);
                } else {
                    world.pileOver(i, j);
                }

            }
        }
        world.pprint();
    }

    private Point[] blockPos;
    private List<Integer>[] stacks;

    public Main(int size) {
        blockPos = new Point[size];
        for (int i = 0; i < blockPos.length; i++) {
            blockPos[i] = new Point(i, 0);
        }
        stacks = new List[size];
        for (int i = 0; i < size; i++) {
            stacks[i] = new ArrayList<>(size);
            stacks[i].add(i);
        }
    }

    public boolean isValid(Point p1, Point p2) {
        if (p1.x == p2.x) {
            return false;
        }
        return true;
    }

    public void moveOver(int b1, int b2) {
        if (!isValid(blockPos[b1], blockPos[b2])) {
            return;
        }
        clearAbove(b1);
        pileOver(b1, b2);
    }

    public void moveOnto(int b1, int b2) {
        if (!isValid(blockPos[b1], blockPos[b2])) {
            return;
        }
        clearAbove(b1);
        clearAbove(b2);
        pileOver(b1, b2);
    }

    public void pileOnto(int b1, int b2) {
        if (!isValid(blockPos[b1], blockPos[b2])) {
            return;
        }
        clearAbove(b2);
        pileOver(b1, b2);
    }

    public void pileOver(int b1, int b2) {
        if (!isValid(blockPos[b1], blockPos[b2])) {
            return;
        }
        Point pos1 = blockPos[b1];
        Point pos2 = blockPos[b2];
        List<Integer> stack1 = stacks[pos1.x];
        int from = pos1.y;
        for (int i = from; i < stack1.size(); i++) {
            moveDirect(stack1.get(i), pos2.x);
        }
        stack1.subList(from, stack1.size()).clear();
    }

    public void clearAbove(int b) {
        Point pos = blockPos[b];
        List<Integer> stack = stacks[pos.x];
        int from = pos.y + 1;
        for (int i = from; i < stack.size(); i++) {
            int b2 = stack.get(i);
            moveDirect(b2, b2);
        }
        stack.subList(from, stack.size()).clear();
    }

    private void moveDirect(int b, int stackNum) {
        List<Integer> stack = stacks[stackNum];
        stack.add(b);
        Point pos = blockPos[b];
        pos.x = stackNum;
        pos.y = stack.size() - 1;
    }

    public void pprint() {
        for (int i = 0; i < stacks.length; i++) {
            System.out.print(i + ":");
            for (int j : stacks[i]) {
                System.out.print(" " + j);
            }
            System.out.println();
        }
    }

}
