package jw.problems.aoc2016;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * https://adventofcode.com/2016/day/23
 *
 * --- Day 23: Safe Cracking ---
 *
 * This is one of the top floors of the nicest tower in EBHQ. The Easter Bunny's private office is here, complete with a safe hidden behind a painting, and who wouldn't hide a star in a safe behind a painting?
 *
 * The safe has a digital screen and keypad for code entry. A sticky note attached to the safe has a password hint on it: "eggs". The painting is of a large rabbit coloring some eggs. You see 7.
 *
 * When you go to type the code, though, nothing appears on the display; instead, the keypad comes apart in your hands, apparently having been smashed. Behind it is some kind of socket - one that matches a connector in your prototype computer! You pull apart the smashed keypad and extract the logic circuit, plug it into your computer, and plug your computer into the safe.
 *
 * Now, you just need to figure out what output the keypad would have sent to the safe. You extract the assembunny code from the logic chip (your puzzle input).
 *
 * The code looks like it uses almost the same architecture and instruction set that the monorail computer used! You should be able to use the same assembunny interpreter for this as you did there, but with one new instruction:
 *
 * tgl x toggles the instruction x away (pointing at instructions like jnz does: positive means forward; negative means backward):
 *
 * For one-argument instructions, inc becomes dec, and all other one-argument instructions become inc.
 * For two-argument instructions, jnz becomes cpy, and all other two-instructions become jnz.
 * The arguments of a toggled instruction are not affected.
 * If an attempt is made to toggle an instruction outside the program, nothing happens.
 * If toggling produces an invalid instruction (like cpy 1 2) and an attempt is later made to execute that instruction, skip it instead.
 * If tgl toggles itself (for example, if a is 0, tgl a would target itself and become inc a), the resulting instruction is not executed until the next time it is reached.
 *
 * For example, given this program:
 *
 * cpy 2 a
 * tgl a
 * tgl a
 * tgl a
 * cpy 1 a
 * dec a
 * dec a
 *
 * cpy 2 a initializes register a to 2.
 * The first tgl a toggles an instruction a (2) away from it, which changes the third tgl a into inc a.
 * The second tgl a also modifies an instruction 2 away from it, which changes the cpy 1 a into jnz 1 a.
 * The fourth line, which is now inc a, increments a to 3.
 * Finally, the fifth line, which is now jnz 1 a, jumps a (3) instructions ahead, skipping the dec a instructions.
 *
 * In this example, the final value in register a is 3.
 *
 * The rest of the electronics seem to place the keypad entry (the number of eggs, 7) in register a, run the code, and then send the value left in register a to the safe.
 *
 * What value should be sent to the safe?
 *
 * Your puzzle answer was 11739.
 * --- Part Two ---
 *
 * The safe doesn't open, but it does make several angry noises to express its frustration.
 *
 * You're quite sure your logic is working correctly, so the only other thing is... you check the painting again. As it turns out, colored eggs are still eggs. Now you count 12.
 *
 * As you run the program with this new input, the prototype computer begins to overheat. You wonder what's taking so long, and whether the lack of any instruction more powerful than "add one" has anything to do with it. Don't bunnies usually multiply?
 *
 * Anyway, what value should actually be sent to the safe?
 *
 * Your puzzle answer was 479008299.
 */
public class Day23 {

    public static void main(String[] args) throws FileNotFoundException {
        State s1 = new State();
        s1.a.val = 7;
        run("./etc/aoc2016/in23", s1);

        State s2 = new State();
        int a = 132;
        for (int i = 10; i >= 2; i--) {
            a *= i;
        }
        s2.a.val = a;
        run("./etc/aoc2016/in23a", s2);
    }

    public static void run(String inFile, State state) throws FileNotFoundException {
        Program p = new Program();

        Scanner sc = new Scanner(new File(inFile));
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
            } else if (cmd.startsWith("tgl")) {
                Tgl e = new Tgl();
                e.x = sc2.next();
                p.addExecutable(e);
            }
            sc2.close();
        }
        sc.close();
        p.exec(state);
    }

    public interface Executable {
        void exec(State state);
    }

    public static class Program implements Executable {
        public State state = new State();
        public List<Executable> execs = new ArrayList<>();

        public void addExecutable(Executable r) {
            execs.add(r);
        }

        public void execDefaultState() {
            exec(state);
        }

        public void exec(State state) {
            state.execs = execs;
            while (state.line < execs.size()) {
                Executable e = execs.get(state.line);
                e.exec(state);
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
        public List<Executable> execs = null;
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

    public static class Tgl implements Executable {
        public String x;

        public void exec(State state) {
            int xi = 0;
            if (x.matches("\\d+")) {
                xi = Integer.parseInt(x);
            } else if (x.matches("[abcd]")) {
                xi = state.getRegister(x).val;
            }
            xi += state.line;

            if (xi >= state.execs.size() || xi < 0) {
                state.line++;
                return;
            }
            Executable exec = state.execs.get(xi);
            Executable newExec = null;
            if (exec instanceof Tgl) {
                Inc e = new Inc();
                e.reg = ((Tgl) exec).x;
                newExec = e;
            } else if (exec instanceof Dec) {
                Inc e = new Inc();
                e.reg = ((Dec) exec).reg;
                newExec = e;
            } else if (exec instanceof Inc) {
                Dec e = new Dec();
                e.reg = ((Inc) exec).reg;
                newExec = e;
            } else if (exec instanceof Copy) {
                Jnz e = new Jnz();
                e.test = ((Copy) exec).from;
                e.to = ((Copy) exec).to;
                newExec = e;
            } else if (exec instanceof Jnz) {
                Copy e = new Copy();
                e.from = ((Jnz) exec).test;
                e.to = ((Jnz) exec).to;
                newExec = e;
            }
            state.execs.remove(xi);
            state.execs.add(xi, newExec);

            state.line++;
        }
    }

    public static class Copy implements Executable {
        public String from;
        public String to;

        public void exec(State state) {
//			System.out.println(String.format("cpy %s %s", from, to));
            if (!to.matches("[abcd]")) {
                state.line++;
                return;
            }
            Register rTo = state.getRegister(to);
            if (from.matches("-?\\d+")) {
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

            int jumpTo = 0;
            if (to.matches("-?\\d+")) {
                jumpTo = Integer.parseInt(to);
            } else if (to.matches("[abcd]")) {
                Register rTo = state.getRegister(to);
                jumpTo = rTo.val;
            }

            if (toJump) {
                state.line += jumpTo;
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