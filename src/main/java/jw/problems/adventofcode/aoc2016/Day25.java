package jw.problems.adventofcode.aoc2016;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * https://adventofcode.com/2016/day/25
 *
 * --- Day 25: Clock Signal ---
 *
 * You open the door and find yourself on the roof. The city sprawls away from you for miles and miles.
 *
 * There's not much time now - it's already Christmas, but you're nowhere near the North Pole, much too far to deliver these stars to the sleigh in time.
 *
 * However, maybe the huge antenna up here can offer a solution. After all, the sleigh doesn't need the stars, exactly; it needs the timing data they provide, and you happen to have a massive signal generator right here.
 *
 * You connect the stars you have to your prototype computer, connect that to the antenna, and begin the transmission.
 *
 * Nothing happens.
 *
 * You call the service number printed on the side of the antenna and quickly explain the situation. "I'm not sure what kind of equipment you have connected over there," he says, "but you need a clock signal." You try to explain that this is a signal for a clock.
 *
 * "No, no, a clock signal - timing information so the antenna computer knows how to read the data you're sending it. An endless, alternating pattern of 0, 1, 0, 1, 0, 1, 0, 1, 0, 1...." He trails off.
 *
 * You ask if the antenna can handle a clock signal at the frequency you would need to use for the data from the stars. "There's no way it can! The only antenna we've installed capable of that is on top of a top-secret Easter Bunny installation, and you're definitely not-" You hang up the phone.
 *
 * You've extracted the antenna's clock signal generation assembunny code (your puzzle input); it looks mostly compatible with code you worked on just recently.
 *
 * This antenna code, being a signal generator, uses one extra instruction:
 *
 * out x transmits x (either an integer or the value of a register) as the next value for the clock signal.
 *
 * The code takes a value (via register a) that describes the signal to generate, but you're not sure how it's used. You'll have to find the input to produce the right signal through experimentation.
 *
 * What is the lowest positive integer that can be used to initialize register a and cause the code to output a clock signal of 0, 1, 0, 1... repeating forever?
 *
 * Your puzzle answer was 192.
 * --- Part Two ---
 *
 * The antenna is ready. Now, all you need is the fifty stars required to generate the signal for the sleigh, but you don't have enough.
 *
 * You look toward the sky in desperation... suddenly noticing that a lone star has been installed at the top of the antenna! Only 49 more to go.
 *
 * If you like, you can
 * .
 */
public class Day25 {

    /*
     * cpy a d
     * cpy 9 c
     * cpy 282 b
     * inc d
     * dec b
     * jnz b -2
     * dec c
     * jnz c -5   // {line=8, a=a, b=0, c=0, d=a+(9*282)}
     * cpy d a                   --2
     * jnz 0 0                --1
     * cpy a b
     * cpy 0 a
     * cpy 2 c   -------
     * jnz b 2  --  -- ^
     * jnz 1 6   v   ^ ^ --
     * dec b    --   ^ ^  v
     * dec c         ^ ^  v
     * jnz c -4     -- ^  v
     * inc a           ^  v
     * jnz 1 -7  -------  v
     * cpy 2 b           -- // {line=21, a=(a+(9*282))/2, b=0, c=2 if even, 1 if odd, d=a+(9*282)}
     * jnz c 2  --  --
     * jnz 1 4   v   ^
     * dec b    --   ^
     * dec c         ^
     * jnz 1 -4     --
     * jnz 0 0
     * out b    // {line=28, a=(a+(9*282))/2, b=0 if even, 1 if odd, c=0, d=a+(9*282)}
     * jnz a -19             --1
     * jnz 1 -21               --2
     */
    public static void main(String[] args) throws FileNotFoundException {
        int x = 0;
        while (x <= 9 * 282) {
            if (x % 2 == 0) {
                x = (x * 2) + 1;
            } else {
                x *= 2;
            }
        }
        int val = x - (9 * 282);

        State s1 = new State();
        s1.a.val = val;
        run("./etc/aoc2016/in25", s1);
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
            } else if (cmd.startsWith("out")) {
                Out e = new Out();
                e.reg = sc2.next();
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
//                System.out.println(state);
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

    public static class Out implements Executable {
        public String reg;

        public void exec(State state) {
            System.out.print(state.getRegister(reg).val + ",");
            state.line++;
        }
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