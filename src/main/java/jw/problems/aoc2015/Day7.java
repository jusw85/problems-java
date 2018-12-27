package jw.problems.aoc2015;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http://adventofcode.com/2015/day/7
 *
 * --- Day 7: Some Assembly Required ---
 *
 * This year, Santa brought little Bobby Tables a set of wires and bitwise logic gates! Unfortunately, little Bobby is a little under the recommended age range, and he needs help assembling the circuit.
 *
 * Each wire has an identifier (some lowercase letters) and can carry a 16-bit signal (a number from 0 to 65535). A signal is provided to each wire by a gate, another wire, or some specific value. Each wire can only get a signal from one source, but can provide its signal to multiple destinations. A gate provides no signal until all of its inputs have a signal.
 *
 * The included instructions booklet describes how to connect the parts together: x AND y -> z means to connect wires x and y to an AND gate, and then connect its output to wire z.
 *
 * For example:
 *
 * 123 -> x means that the signal 123 is provided to wire x.
 * x AND y -> z means that the bitwise AND of wire x and wire y is provided to wire z.
 * p LSHIFT 2 -> q means that the value from wire p is left-shifted by 2 and then provided to wire q.
 * NOT e -> f means that the bitwise complement of the value from wire e is provided to wire f.
 *
 * Other possible gates include OR (bitwise OR) and RSHIFT (right-shift). If, for some reason, you'd like to emulate the circuit instead, almost all programming languages (for example, C, JavaScript, or Python) provide operators for these gates.
 *
 * For example, here is a simple circuit:
 *
 * 123 -> x
 * 456 -> y
 * x AND y -> d
 * x OR y -> e
 * x LSHIFT 2 -> f
 * y RSHIFT 2 -> g
 * NOT x -> h
 * NOT y -> i
 *
 * After it is run, these are the signals on the wires:
 *
 * d: 72
 * e: 507
 * f: 492
 * g: 114
 * h: 65412
 * i: 65079
 * x: 123
 * y: 456
 *
 * In little Bobby's kit's instructions booklet (provided as your puzzle input), what signal is ultimately provided to wire a?
 *
 * Your puzzle answer was 956.
 * --- Part Two ---
 *
 * Now, take the signal you got on wire a, override wire b to that signal, and reset the other wires (including wire a). What new signal is ultimately provided to wire a?
 *
 * Your puzzle answer was 40149.
 */
public class Day7 {

    public static Pattern p1 = Pattern.compile("(.*?)([A-Z]+) (.*) -> (\\w+)");
    public static Pattern p2 = Pattern.compile("(.*) -> (\\w+)");

    public static class Operation {
        public String target;
        public String op;
        public String arg0;
        public String arg1;

        public Operation(String target, String op, String arg0, String arg1) {
            this.target = target;
            this.op = op;
            this.arg0 = arg0;
            this.arg1 = arg1;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Operation{");
            sb.append("target='").append(target).append('\'');
            sb.append(", op='").append(op).append('\'');
            sb.append(", arg0='").append(arg0).append('\'');
            sb.append(", arg1='").append(arg1).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Map<String, Operation> map = new HashMap<>();
        Scanner sc = new Scanner(new File("./etc/aoc2015/in7"));
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = p1.matcher(line);
            if (m.matches()) {
                String arg0 = m.group(1).trim();
                String op = m.group(2).trim();
                String arg1 = m.group(3).trim();
                String target = m.group(4).trim();
                map.put(target, new Operation(target, op, arg0, arg1));
            } else {
                m = p2.matcher(line);
                m.matches();
                String arg1 = m.group(1).trim();
                String op = "VAL";
                String target = m.group(2).trim();
                map.put(target, new Operation(target, op, null, arg1));
            }
        }
        sc.close();

        int aVal = getVal(map, "a");
        System.out.println(aVal);
        map.get("b").arg1 = String.valueOf(aVal);
        cache.clear();
        System.out.println(getVal(map, "a"));
    }

    public static Map<String, Integer> cache = new HashMap<>();

    public static int getVal(Map<String, Operation> map, String r) {
        if (cache.containsKey(r)) {
            return cache.get(r);
        }
        Operation o = map.get(r);
        int a0, a1;
        int v;
        switch (o.op) {
            case "LSHIFT":
                a0 = getVal(map, o.arg0);
                a1 = Integer.parseInt(o.arg1);
                v = (a0 << a1) & 65535;
                break;
            case "RSHIFT":
                a0 = getVal(map, o.arg0);
                a1 = Integer.parseInt(o.arg1);
                v = (a0 >> a1);
                break;
            case "AND":
                a0 = getRegOrInt(map, o.arg0);
                a1 = getRegOrInt(map, o.arg1);
                v = (a0 & a1);
                break;
            case "OR":
                a0 = getRegOrInt(map, o.arg0);
                a1 = getRegOrInt(map, o.arg1);
                v = (a0 | a1);
                break;
            case "NOT":
                a1 = getVal(map, o.arg1);
                v = ~a1 & 65535;
                break;
            case "VAL":
                v = getRegOrInt(map, o.arg1);
                break;
            default:
                throw new RuntimeException("Unknown op");
        }
        cache.put(r, v);
        return v;
    }

    public static int getRegOrInt(Map<String, Operation> map, String arg) {
        if (arg.matches("-?\\d+")) {
            return Integer.parseInt(arg);
        } else {
            return getVal(map, arg);
        }
    }

}