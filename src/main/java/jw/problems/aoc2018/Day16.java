package jw.problems.aoc2018;

import jw.problems.aoc2017.Day23;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://adventofcode.com/2018/day/16
 */
public class Day16 {

    public static Pattern p1 = Pattern.compile("Before:\\s*\\[(\\d+), (\\d+), (\\d+), (\\d+)\\]");
    public static Pattern p2 = Pattern.compile("After:\\s*\\[(\\d+), (\\d+), (\\d+), (\\d+)\\]");

    public static void main(String[] args) throws FileNotFoundException {
        Map<Integer, Set<Instruction.Type>> opcodeMap = new HashMap<>();
        Set<Instruction.Type> instructions = new HashSet<>();
        for (Instruction.Type t : Instruction.Type.values()) {
            instructions.add(t);
        }
        for (int i = 0; i < 16; i++) {
            opcodeMap.put(i, new HashSet<>(instructions));
        }

        Scanner sc = new Scanner(new File("./etc/aoc2018/in16a"));
        int numGT3Opcodes = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.isEmpty())
                continue;
            Matcher m = p1.matcher(line);
            m.matches();
            long in0 = Long.parseLong(m.group(1));
            long in1 = Long.parseLong(m.group(2));
            long in2 = Long.parseLong(m.group(3));
            long in3 = Long.parseLong(m.group(4));
            int opcode = sc.nextInt();
            long arg0 = sc.nextLong();
            long arg1 = sc.nextLong();
            long arg2 = sc.nextLong();
            sc.nextLine();

            line = sc.nextLine();
            m = p2.matcher(line);
            m.matches();
            long out0 = Long.parseLong(m.group(1));
            long out1 = Long.parseLong(m.group(2));
            long out2 = Long.parseLong(m.group(3));
            long out3 = Long.parseLong(m.group(4));

            int numOpcodes = 0;
            Set<Instruction.Type> possibleOpcodes = new HashSet<>();
            for (Instruction.Type t : Instruction.Type.values()) {
                State s = new State(4);
                s.setRegisters(in0, in1, in2, in3);
                Instruction i = new Instruction(t, arg0, arg1, arg2);
                i.exec(s);
                if (s.getRegister(0).val == out0 &&
                        s.getRegister(1).val == out1 &&
                        s.getRegister(2).val == out2 &&
                        s.getRegister(3).val == out3) {
                    numOpcodes++;
                    possibleOpcodes.add(t);
                }
            }
            if (numOpcodes >= 3) {
                numGT3Opcodes++;
            }
            opcodeMap.get(opcode).retainAll(possibleOpcodes);
        }
        sc.close();
        System.out.println(numGT3Opcodes);
        narrowMap(opcodeMap);

        sc = new Scanner(new File("./etc/aoc2018/in16b"));
        State s = new State(4);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Scanner sc2 = new Scanner(line);
            int opcode = sc2.nextInt();
            long arg0 = sc2.nextLong();
            long arg1 = sc2.nextLong();
            long arg2 = sc2.nextLong();
            Instruction i = new Instruction(opcodeMap.get(opcode).iterator().next(), arg0, arg1, arg2);
            i.exec(s);
        }
        System.out.println(s);
    }

    public static <K, V> void narrowMap(Map<K, Set<V>> map) {
        Map<K, Set<V>> map2 = new HashMap<>();
        List<Set<V>> found = new LinkedList<>();
        while (!map.isEmpty()) {
            Iterator<Map.Entry<K, Set<V>>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<K, Set<V>> e = it.next();
                if (e.getValue().size() == 1) {
                    map2.put(e.getKey(), e.getValue());
                    found.add(e.getValue());
                    it.remove();
                }
            }
            for (Map.Entry<K, Set<V>> e : map.entrySet()) {
                for (Set<V> s : found) {
                    e.getValue().removeAll(s);
                }
            }
        }
        map.clear();
        map.putAll(map2);
    }

    public static class Register {
        public long val;

        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(val);
            return sb.toString();
        }
    }

    public static class State {
        public int line = 0;
        private List<Register> registers = new ArrayList<>();

        public State(int numRegisters) {
            for (int i = 0; i < numRegisters; i++) {
                registers.add(new Register());
            }
        }

        public void setRegisters(long... ls) {
            int len = Math.min(ls.length, registers.size());
            for (int i = 0; i < len; i++) {
                registers.get(i).val = ls[i];
            }
        }

        public Register getRegister(long l) {
            return registers.get((int) l);
        }

        public String toString() {
            final StringBuilder sb = new StringBuilder("State{");
            sb.append("line=").append(line);
            sb.append(", registers=").append(registers);
            sb.append('}');
            return sb.toString();
        }
    }

    public static class Instruction {

        public Type type;
        public long arg0;
        public long arg1;
        public long arg2;

        public enum Type {
            ADDR, ADDI, MULR, MULI,
            BANR, BANI, BORR, BORI,
            SETR, SETI,
            GTIR, GTRI, GTRR,
            EQIR, EQRI, EQRR,
        }

        public Instruction(Type type, long arg0, long arg1, long arg2) {
            this.type = type;
            this.arg0 = arg0;
            this.arg1 = arg1;
            this.arg2 = arg2;
        }

        public void exec(State state) {
            switch (type) {
                case ADDR:
                    state.getRegister(arg2).val = state.getRegister(arg0).val + state.getRegister(arg1).val;
                    break;
                case ADDI:
                    state.getRegister(arg2).val = state.getRegister(arg0).val + arg1;
                    break;
                case MULR:
                    state.getRegister(arg2).val = state.getRegister(arg0).val * state.getRegister(arg1).val;
                    break;
                case MULI:
                    state.getRegister(arg2).val = state.getRegister(arg0).val * arg1;
                    break;
                case BANR:
                    state.getRegister(arg2).val = state.getRegister(arg0).val & state.getRegister(arg1).val;
                    break;
                case BANI:
                    state.getRegister(arg2).val = state.getRegister(arg0).val & arg1;
                    break;
                case BORR:
                    state.getRegister(arg2).val = state.getRegister(arg0).val | state.getRegister(arg1).val;
                    break;
                case BORI:
                    state.getRegister(arg2).val = state.getRegister(arg0).val | arg1;
                    break;
                case SETR:
                    state.getRegister(arg2).val = state.getRegister(arg0).val;
                    break;
                case SETI:
                    state.getRegister(arg2).val = arg0;
                    break;
                case GTIR:
                    state.getRegister(arg2).val = arg0 > state.getRegister(arg1).val ? 1 : 0;
                    break;
                case GTRI:
                    state.getRegister(arg2).val = state.getRegister(arg0).val > arg1 ? 1 : 0;
                    break;
                case GTRR:
                    state.getRegister(arg2).val = state.getRegister(arg0).val > state.getRegister(arg1).val ? 1 : 0;
                    break;
                case EQIR:
                    state.getRegister(arg2).val = arg0 == state.getRegister(arg1).val ? 1 : 0;
                    break;
                case EQRI:
                    state.getRegister(arg2).val = state.getRegister(arg0).val == arg1 ? 1 : 0;
                    break;
                case EQRR:
                    state.getRegister(arg2).val = state.getRegister(arg0).val == state.getRegister(arg1).val ? 1 : 0;
                    break;
            }
            state.line++;
        }
    }

}
