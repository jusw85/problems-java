package jw.problems.adventofcode.aoc2016;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * https://adventofcode.com/2016/day/12
 *
 * --- Day 12: Leonardo's Monorail ---
 *
 * You finally reach the top floor of this building: a garden with a slanted glass ceiling. Looks like there are no more stars to be had.
 *
 * While sitting on a nearby bench amidst some tiger lilies, you manage to decrypt some of the files you extracted from the servers downstairs.
 *
 * According to these documents, Easter Bunny HQ isn't just this building - it's a collection of buildings in the nearby area. They're all connected by a local monorail, and there's another building not far from here! Unfortunately, being night, the monorail is currently not operating.
 *
 * You remotely connect to the monorail control systems and discover that the boot sequence expects a password. The password-checking logic (your puzzle input) is easy to extract, but the code it uses is strange: it's assembunny code designed for the new computer you just assembled. You'll have to execute the code and get the password.
 *
 * The assembunny code you've extracted operates on four registers (a, b, c, and d) that start at 0 and can hold any integer. However, it seems to make use of only a few instructions:
 *
 * cpy x y copies x (either an integer or the value of a register) into register y.
 * inc x increases the value of register x by one.
 * dec x decreases the value of register x by one.
 * jnz x y jumps to an instruction y away (positive means forward; negative means backward), but only if x is not zero.
 *
 * The jnz instruction moves relative to itself: an offset of -1 would continue at the previous instruction, while an offset of 2 would skip over the next instruction.
 *
 * For example:
 *
 * cpy 41 a
 * inc a
 * inc a
 * dec a
 * jnz a 2
 * dec a
 *
 * The above code would set register a to 41, increase its value by 2, decrease its value by 1, and then skip the last dec a (because a is not zero, so the jnz a 2 skips it), leaving register a at 42. When you move past the last instruction, the program halts.
 *
 * After executing the assembunny code in your puzzle input, what value is left in register a?
 *
 * Your puzzle answer was 318117.
 * --- Part Two ---
 *
 * As you head down the fire escape to the monorail, you notice it didn't start; register c needs to be initialized to the position of the ignition key.
 *
 * If you instead initialize register c to be 1, what value is now left in register a?
 *
 * Your puzzle answer was 9227771.
 */
public class Day12 {

    public static void main(String[] args) throws FileNotFoundException {
        Program p = new Program();

        Scanner sc = new Scanner(new File("./etc/aoc2016/in12"));
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Scanner sc2 = new Scanner(line);
            String cmd = sc2.next();
            if (cmd.startsWith("cpy")) {
                Copy e = new Copy();
                e.from = sc2.next();
                e.to = sc2.next();
                p.addExecutable(e);
            } else if (cmd.startsWith("jnz")) {
                Jnz e = new Jnz();
                e.test = sc2.next();
                e.to = sc2.next();
                p.addExecutable(e);
            } else if (cmd.startsWith("inc")) {
                Inc e = new Inc();
                e.reg = sc2.next();
                p.addExecutable(e);
            } else if (cmd.startsWith("dec")) {
                Dec e = new Dec();
                e.reg = sc2.next();
                p.addExecutable(e);
            }
            sc2.close();
        }
        sc.close();
        p.execDefaultState();

        State state = new State();
        state.c.val = 1;
        p.exec(state);
    }

    public interface Executable {
        void exec(State state);
    }

    public static class Program implements Executable {
        private State state = new State();
        private List<Executable> execs = new ArrayList<>();

        public void addExecutable(Executable r) {
            execs.add(r);
        }

        public void execDefaultState() {
            exec(this.state);
        }

        public void exec(State state) {
            while (state.line < execs.size()) {
//				System.out.println(state);
                execs.get(state.line).exec(state);
            }
            System.out.println(state);
        }
    }

    public static class State {
        public int line = 0;
        public Register a = new Register();
        public Register b = new Register();
        public Register c = new Register();
        public Register d = new Register();
        private Map<String, Register> registers = new HashMap<>();

        public State() {
            registers.put("a", a);
            registers.put("b", b);
            registers.put("c", c);
            registers.put("d", d);
        }

        public String toString() {
            return String.format("{line=%d, a=%d, b=%d, c=%d, d=%d}", line, a.val, b.val, c.val, d.val);
        }

        public Register getRegister(String name) {
            return registers.get(name);
        }
    }

    public static class Register {
        public int val;
    }

    public static class Copy implements Executable {
        public String from;
        public String to;

        public void exec(State state) {
//			System.out.println(String.format("cpy %s %s", from, to));
            Register rTo = state.getRegister(to);
            if (from.matches("\\d+")) {
                rTo.val = Integer.parseInt(from);
            } else if (from.matches("[abcd]")) {
                Register rFrom = state.getRegister(from);
                rTo.val = rFrom.val;
            }
            state.line++;
        }
    }

    public static class Jnz implements Executable {
        public String test;
        public String to;

        public void exec(State state) {
//			System.out.println(String.format("jnz %s %s", test, to));
            boolean toJump = false;
            if (test.matches("\\d+")) {
                int iTest = Integer.parseInt(test);
                toJump = iTest != 0;
            } else if (test.matches("[abcd]")) {
                Register rTest = state.getRegister(test);
                toJump = rTest.val != 0;
            }

            if (toJump) {
                state.line += Integer.parseInt(to);
            } else {
                state.line++;
            }
        }
    }

    public static class Inc implements Executable {
        public String reg;

        public void exec(State state) {
//			System.out.println(String.format("inc %s", reg));
            state.getRegister(reg).val++;
            state.line++;
        }
    }

    public static class Dec implements Executable {
        public String reg;

        public void exec(State state) {
//			System.out.println(String.format("dec %s", reg));
            state.getRegister(reg).val--;
            state.line++;
        }
    }
}