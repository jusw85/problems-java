package jw.problems.uva.p105;

import java.util.*;

/**
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=3&page=show_problem&problem=41
 *
 * ./etc/uva/p105.pdf
 */
public class Main {

    public static void main(String[] args) {
        Main p = new Main();
        Scanner sc = new Scanner(System.in);
        String line;
        int idx = 0;
        while (sc.hasNextLine() && (line = sc.nextLine()).length() > 0) {
            Scanner sc2 = new Scanner(line);
            p.add(idx++, sc2.nextInt(), sc2.nextInt(), sc2.nextInt());
            sc2.close();
        }
        sc.close();
        p.solve();
    }

    private Queue<Node> nodes;

    public Main() {
        // group by x asc, then group by close then open, then group by height desc
        nodes = new PriorityQueue<>(5000, (o1, o2) -> {
            int xComp = Integer.compare(o1.x, o2.x);
            if (xComp != 0)
                return xComp;
            int openComp = Boolean.compare(o1.isOpen, o2.isOpen);
            if (openComp != 0) {
                return openComp;
            }
            return Integer.compare(o2.h, o1.h);
        });
    }

    public void add(int idx, int s, int h, int e) {
        Node nStart = new Node(idx, s, h, true);
        Node nEnd = new Node(idx, e, 0, false);
        nodes.add(nStart);
        nodes.add(nEnd);
    }

    public void solve() {
        Map<Integer, Node> open = new HashMap<>();
        int curHeight = 0;
        boolean prevIsOpen = false;
        int prevX = 0;
        Node n;
        String prefix = "";
        while ((n = nodes.poll()) != null) {
            if (n.isOpen) {
                open.put(n.idx, n);
            } else {
                open.remove(n.idx);
            }

            boolean curIsOpen = n.isOpen;
            int curX = n.x;

            Collection<Node> openNodes = open.values();
            if (openNodes.isEmpty()) {
                System.out.print(prefix + curX + " 0");
                prefix = " ";
            } else if (prevX != curX || prevIsOpen != curIsOpen) {
                int max = -1;
                for (Node node : openNodes) {
                    if (node.h > max) {
                        max = node.h;
                    }
                }
//                int max = openNodes.stream().max(Comparator.comparingInt(o -> o.h)).get().h;
                if (max != curHeight) {
                    curHeight = max;
                    System.out.print(prefix + curX + " " + max);
                    prefix = " ";
                }
            }
            prevIsOpen = curIsOpen;
            prevX = curX;
        }
        System.out.println();
    }

    class Node {
        public int idx;
        public int x;
        public int h;
        public boolean isOpen;

        public Node(int idx, int x, int h, boolean isOpen) {
            this.idx = idx;
            this.x = x;
            this.h = h;
            this.isOpen = isOpen;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "idx=" + idx +
                    ", x=" + x +
                    ", h=" + h +
                    ", isOpen=" + isOpen +
                    '}';
        }
    }

}
