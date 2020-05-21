package jw.problems.adventofcode.aoc2017;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * http://adventofcode.com/2017/day/23
 *
 * --- Day 23: Coprocessor Conflagration ---
 *
 * You decide to head directly to the CPU and fix the printer from there. As you get close, you find an experimental coprocessor doing so much work that the local programs are afraid it will halt and catch fire. This would cause serious issues for the rest of the computer, so you head in and see what you can do.
 *
 * The code it's running seems to be a variant of the kind you saw recently on that tablet. The general functionality seems very similar, but some of the instructions are different:
 *
 * set X Y sets register X to the value of Y.
 * sub X Y decreases register X by the value of Y.
 * mul X Y sets register X to the result of multiplying the value contained in register X by the value of Y.
 * jnz X Y jumps with an offset of the value of Y, but only if the value of X is not zero. (An offset of 2 skips the next instruction, an offset of -1 jumps to the previous instruction, and so on.)
 *
 * Only the instructions listed above are used. The eight registers here, named a through h, all start at 0.
 *
 * The coprocessor is currently set to some kind of debug mode, which allows for testing, but prevents it from doing any meaningful work.
 *
 * If you run the program (your puzzle input), how many times is the mul instruction invoked?
 *
 * Your puzzle answer was 9409.
 * --- Part Two ---
 *
 * Now, it's time to fix the problem.
 *
 * The debug mode switch is wired directly to register a. You flip the switch, which makes register a now start at 1 when the program is executed.
 *
 * Immediately, the coprocessor begins to overheat. Whoever wrote this program obviously didn't choose a very efficient implementation. You'll need to optimize the program if it has any hope of completing before Santa needs that printer working.
 *
 * The coprocessor's ultimate goal is to determine the final value left in register h once the program completes. Technically, if it had that... it wouldn't even need to run the program.
 *
 * After setting register a to 1, if the program were to run to completion, what value would be left in register h?
 *
 * Your puzzle answer was 913.
 */
public class Day23 {

    public static void main(String[] args) throws FileNotFoundException {
        part1();
        part2();
    }

    /*
        start:
        b = 109900 // line 0-8
        c = 126900 // line 0-8
        f = 1 // isPrime = true
        d = 2
        do {
            e = 2
            do {
                if (d * e == b)
                    f = 0 // isPrime = false
            } while (++e != b)
        } while (++d != b)

        if (f == 0) {
            h++
        }

        if (b != c) {
            b += 17
            goto start:
        }
     */
    public static void part2() {
        int c = 126900;
        int b = 109900;

        int count = 0;
        for (int i = b; i <= c; i += 17) {
            if (!isPrime(i)) {
                count++;
            }
        }
        System.out.println(count);
    }

    public static boolean isPrime(int p) {
        if ((p & 1) == 0) {
            return p == 2;
        }

        for (int i = 3; (i * i) <= p; i += 2) {
            if ((p % i) == 0) {
                return false;
            }
        }

        return p != 1;
    }

    public static void part1() throws FileNotFoundException {
        Program p = new Program();

        Scanner sc = new Scanner(new File("./etc/aoc2017/in23"));
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Scanner sc2 = new Scanner(line);
            String cmd = sc2.next();
            if (cmd.startsWith("set")) {
                Set e = new Set();
                e.reg1 = sc2.next();
                e.reg2 = sc2.next();
                p.addExecutable(e);
            } else if (cmd.startsWith("sub")) {
                Sub e = new Sub();
                e.reg1 = sc2.next();
                e.reg2 = sc2.next();
                p.addExecutable(e);
            } else if (cmd.startsWith("mul")) {
                Mul e = new Mul();
                e.reg1 = sc2.next();
                e.reg2 = sc2.next();
                p.addExecutable(e);
            } else if (cmd.startsWith("jnz")) {
                Jnz e = new Jnz();
                e.reg1 = sc2.next();
                e.reg2 = sc2.next();
                p.addExecutable(e);
            }
            sc2.close();
        }
        sc.close();

        p.execDefaultState();
    }

    public interface Executable {
        void exec(State state);
    }

    public static class Program implements Executable {
        private State state = new State();
        private List<Executable> execs = new ArrayList<>();

        public Program() {
        }

        public void addExecutable(Executable r) {
            execs.add(r);
        }

        public void execDefaultState() {
            exec(this.state);
        }

        public void exec(State state) {
            while (state.line < execs.size()) {
                execs.get(state.line).exec(state);
            }
            System.out.println(state.mulCount);
            System.out.println(state);
        }
    }

    public static class State {
        public int line = 0;
        public int mulCount = 0;
        private Map<String, Register> registers = new HashMap<>();

        public State() {
            for (char i = 'a'; i <= 'h'; i++) {
                registers.put(String.valueOf(i), new Register());
            }
        }

        public String toString() {
            final StringBuilder sb = new StringBuilder("State{");
            sb.append("line=").append(line);
            sb.append(", registers=").append(registers);
            sb.append('}');
            return sb.toString();
        }

        public Register getRegister(String name) {
            return registers.get(name);
        }
    }

    public static class Register {
        public long val;

        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(val);
            return sb.toString();
        }
    }

    public static class Jnz implements Executable {
        public String reg1;
        public String reg2;

        public void exec(State state) {
            if (regToVal(state, reg1) != 0) {
                state.line += regToVal(state, reg2);
            } else {
                state.line++;
            }
        }
    }

    public static class Sub implements Executable {
        public String reg1;
        public String reg2;

        public void exec(State state) {
            state.getRegister(reg1).val -= regToVal(state, reg2);
            state.line++;
        }
    }

    public static class Mul implements Executable {
        public String reg1;
        public String reg2;

        public void exec(State state) {
            state.mulCount++;
            state.getRegister(reg1).val *= regToVal(state, reg2);
            state.line++;
        }
    }

    public static class Set implements Executable {
        public String reg1;
        public String reg2;

        public void exec(State state) {
            state.getRegister(reg1).val = regToVal(state, reg2);
            state.line++;
        }
    }

    public static long regToVal(State state, String reg) {
        if (reg.matches("-?\\d+")) {
            return Integer.parseInt(reg);
        } else if (reg.matches("[a-z]")) {
            return state.getRegister(reg).val;
        }
        throw new IllegalArgumentException();
    }

}