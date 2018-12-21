package jw.problems.aoc2018;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://adventofcode.com/2018/day/16
 * --- Day 16: Chronal Classification ---
 *
 * As you see the Elves defend their hot chocolate successfully, you go back to falling through time. This is going to become a problem.
 *
 * If you're ever going to return to your own time, you need to understand how this device on your wrist works. You have a little while before you reach your next destination, and with a bit of trial and error, you manage to pull up a programming manual on the device's tiny screen.
 *
 * According to the manual, the device has four registers (numbered 0 through 3) that can be manipulated by instructions containing one of 16 opcodes. The registers start with the value 0.
 *
 * Every instruction consists of four values: an opcode, two inputs (named A and B), and an output (named C), in that order. The opcode specifies the behavior of the instruction and how the inputs are interpreted. The output, C, is always treated as a register.
 *
 * In the opcode descriptions below, if something says "value A", it means to take the number given as A literally. (This is also called an "immediate" value.) If something says "register A", it means to use the number given as A to read from (or write to) the register with that number. So, if the opcode addi adds register A and value B, storing the result in register C, and the instruction addi 0 7 3 is encountered, it would add 7 to the value contained by register 0 and store the sum in register 3, never modifying registers 0, 1, or 2 in the process.
 *
 * Many opcodes are similar except for how they interpret their arguments. The opcodes fall into seven general categories:
 *
 * Addition:
 *
 * addr (add register) stores into register C the result of adding register A and register B.
 * addi (add immediate) stores into register C the result of adding register A and value B.
 *
 * Multiplication:
 *
 * mulr (multiply register) stores into register C the result of multiplying register A and register B.
 * muli (multiply immediate) stores into register C the result of multiplying register A and value B.
 *
 * Bitwise AND:
 *
 * banr (bitwise AND register) stores into register C the result of the bitwise AND of register A and register B.
 * bani (bitwise AND immediate) stores into register C the result of the bitwise AND of register A and value B.
 *
 * Bitwise OR:
 *
 * borr (bitwise OR register) stores into register C the result of the bitwise OR of register A and register B.
 * bori (bitwise OR immediate) stores into register C the result of the bitwise OR of register A and value B.
 *
 * Assignment:
 *
 * setr (set register) copies the contents of register A into register C. (Input B is ignored.)
 * seti (set immediate) stores value A into register C. (Input B is ignored.)
 *
 * Greater-than testing:
 *
 * gtir (greater-than immediate/register) sets register C to 1 if value A is greater than register B. Otherwise, register C is set to 0.
 * gtri (greater-than register/immediate) sets register C to 1 if register A is greater than value B. Otherwise, register C is set to 0.
 * gtrr (greater-than register/register) sets register C to 1 if register A is greater than register B. Otherwise, register C is set to 0.
 *
 * Equality testing:
 *
 * eqir (equal immediate/register) sets register C to 1 if value A is equal to register B. Otherwise, register C is set to 0.
 * eqri (equal register/immediate) sets register C to 1 if register A is equal to value B. Otherwise, register C is set to 0.
 * eqrr (equal register/register) sets register C to 1 if register A is equal to register B. Otherwise, register C is set to 0.
 *
 * Unfortunately, while the manual gives the name of each opcode, it doesn't seem to indicate the number. However, you can monitor the CPU to see the contents of the registers before and after instructions are executed to try to work them out. Each opcode has a number from 0 through 15, but the manual doesn't say which is which. For example, suppose you capture the following sample:
 *
 * Before: [3, 2, 1, 1]
 * 9 2 1 2
 * After:  [3, 2, 2, 1]
 *
 * This sample shows the effect of the instruction 9 2 1 2 on the registers. Before the instruction is executed, register 0 has value 3, register 1 has value 2, and registers 2 and 3 have value 1. After the instruction is executed, register 2's value becomes 2.
 *
 * The instruction itself, 9 2 1 2, means that opcode 9 was executed with A=2, B=1, and C=2. Opcode 9 could be any of the 16 opcodes listed above, but only three of them behave in a way that would cause the result shown in the sample:
 *
 * Opcode 9 could be mulr: register 2 (which has a value of 1) times register 1 (which has a value of 2) produces 2, which matches the value stored in the output register, register 2.
 * Opcode 9 could be addi: register 2 (which has a value of 1) plus value 1 produces 2, which matches the value stored in the output register, register 2.
 * Opcode 9 could be seti: value 2 matches the value stored in the output register, register 2; the number given for B is irrelevant.
 *
 * None of the other opcodes produce the result captured in the sample. Because of this, the sample above behaves like three opcodes.
 *
 * You collect many of these samples (the first section of your puzzle input). The manual also includes a small test program (the second section of your puzzle input) - you can ignore it for now.
 *
 * Ignoring the opcode numbers, how many samples in your puzzle input behave like three or more opcodes?
 *
 * Your puzzle answer was 592.
 * --- Part Two ---
 *
 * Using the samples you collected, work out the number of each opcode and execute the test program (the second section of your puzzle input).
 *
 * What value is contained in register 0 after executing the test program?
 *
 * Your puzzle answer was 557.
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
