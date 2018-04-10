package jw.problems.aoc2017;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;

/**
 * http://adventofcode.com/2017/day/24
 *
 * --- Day 24: Electromagnetic Moat ---
 *
 * The CPU itself is a large, black building surrounded by a bottomless pit. Enormous metal tubes extend outward from the side of the building at regular intervals and descend down into the void. There's no way to cross, but you need to get inside.
 *
 * No way, of course, other than building a bridge out of the magnetic components strewn about nearby.
 *
 * Each component has two ports, one on each end. The ports come in all different types, and only matching types can be connected. You take an inventory of the components by their port types (your puzzle input). Each port is identified by the number of pins it uses; more pins mean a stronger connection for your bridge. A 3/7 component, for example, has a type-3 port on one side, and a type-7 port on the other.
 *
 * Your side of the pit is metallic; a perfect surface to connect a magnetic, zero-pin port. Because of this, the first port you use must be of type 0. It doesn't matter what type of port you end with; your goal is just to make the bridge as strong as possible.
 *
 * The strength of a bridge is the sum of the port types in each component. For example, if your bridge is made of components 0/3, 3/7, and 7/4, your bridge has a strength of 0+3 + 3+7 + 7+4 = 24.
 *
 * For example, suppose you had the following components:
 *
 * 0/2
 * 2/2
 * 2/3
 * 3/4
 * 3/5
 * 0/1
 * 10/1
 * 9/10
 *
 * With them, you could make the following valid bridges:
 *
 * 0/1
 * 0/1--10/1
 * 0/1--10/1--9/10
 * 0/2
 * 0/2--2/3
 * 0/2--2/3--3/4
 * 0/2--2/3--3/5
 * 0/2--2/2
 * 0/2--2/2--2/3
 * 0/2--2/2--2/3--3/4
 * 0/2--2/2--2/3--3/5
 *
 * (Note how, as shown by 10/1, order of ports within a component doesn't matter. However, you may only use each port on a component once.)
 *
 * Of these bridges, the strongest one is 0/1--10/1--9/10; it has a strength of 0+1 + 1+10 + 10+9 = 31.
 *
 * What is the strength of the strongest bridge you can make with the components you have available?
 *
 * Your puzzle answer was 1656.
 * --- Part Two ---
 *
 * The bridge you've built isn't long enough; you can't jump the rest of the way.
 *
 * In the example above, there are two longest bridges:
 *
 * 0/2--2/2--2/3--3/4
 * 0/2--2/2--2/3--3/5
 *
 * Of them, the one which uses the 3/5 component is stronger; its strength is 0+2 + 2+2 + 2+3 + 3+5 = 19.
 *
 * What is the strength of the longest bridge you can make? If you can make multiple bridges of the longest length, pick the strongest one.
 *
 * Your puzzle answer was 1642.
 */
public class Day24 {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2017/in24"));
        List<Pin> pins = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] s = line.split("/");
            Pin p = new Pin(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
            pins.add(p);
        }
        sc.close();

        Stack<Pin> ps = findStrongest(0, pins);
        System.out.println(getStackWeight(ps));
        System.out.println(ps.size());
        ps = findLongest(0, pins);
        System.out.println(getStackWeight(ps));
        System.out.println(ps.size());
    }

    public static Stack<Pin> findLongest(int port, List<Pin> pins) {
        int maxLength = Integer.MIN_VALUE;
        int maxWeight = Integer.MIN_VALUE;
        Stack<Pin> maxStack = null;
        for (Pin pin : pins) {
            if (pin.canLink(port)) {
                List<Pin> tmp = new ArrayList<>();
                tmp.addAll(pins);
                tmp.remove(pin);
                Stack<Pin> st = findLongest(pin.getOther(port), tmp);
                if (st == null) {
                    st = new Stack<>();
                }
                int sw = getStackWeight(st);
                if (st.size() > maxLength ||
                        (st.size() == maxLength && sw >= maxWeight)) {
                    maxLength = st.size();
                    maxWeight = sw;
                    maxStack = st;
                    maxStack.push(pin);
                }
            }
        }
        return maxStack;
    }

    public static Stack<Pin> findStrongest(int port, List<Pin> pins) {
        int maxWeight = Integer.MIN_VALUE;
        Stack<Pin> maxStack = null;
        for (Pin pin : pins) {
            if (pin.canLink(port)) {
                List<Pin> tmp = new ArrayList<>();
                tmp.addAll(pins);
                tmp.remove(pin);
                Stack<Pin> st = findStrongest(pin.getOther(port), tmp);
                if (st == null) {
                    st = new Stack<>();
                }
                int sw = getStackWeight(st);
                if (sw >= maxWeight) {
                    maxWeight = sw;
                    maxStack = st;
                    maxStack.push(pin);
                }
            }
        }
        return maxStack;
    }

    public static int getStackWeight(Stack<Pin> st) {
        int weight = 0;
        for (Pin p : st) {
            weight += p.getWeight();
        }
        return weight;
    }

    public static class Pin {
        public int p1;
        public int p2;

        public Pin(int p1, int p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        public int getOther(int p) {
            return p ^ p1 ^ p2;
        }

        public boolean canLink(int p) {
            return p1 == p || p2 == p;
        }

        public int getWeight() {
            return p1 + p2;
        }

        public boolean equals(Object o) {
            Pin pin = (Pin) o;
            return p1 == pin.p1 && p2 == pin.p2;
        }

        public int hashCode() {
            return Objects.hash(p1, p2);
        }

        public String toString() {
            final StringBuilder sb = new StringBuilder("Pin{");
            sb.append("p1=").append(p1);
            sb.append(", p2=").append(p2);
            sb.append('}');
            return sb.toString();
        }
    }
}