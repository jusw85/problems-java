package jw.problems.aoc2017;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

/**
 * http://adventofcode.com/2017/day/18
 *
 * --- Day 18: Duet ---
 *
 * You discover a tablet containing some strange assembly code labeled simply "Duet". Rather than bother the sound card with it, you decide to run the code yourself. Unfortunately, you don't see any documentation, so you're left to figure out what the instructions mean on your own.
 *
 * It seems like the assembly is meant to operate on a set of registers that are each named with a single letter and that can each hold a single integer. You suppose each register should start with a value of 0.
 *
 * There aren't that many instructions, so it shouldn't be hard to figure out what they do. Here's what you determine:
 *
 * snd X plays a sound with a frequency equal to the value of X.
 * set X Y sets register X to the value of Y.
 * add X Y increases register X by the value of Y.
 * mul X Y sets register X to the result of multiplying the value contained in register X by the value of Y.
 * mod X Y sets register X to the remainder of dividing the value contained in register X by the value of Y (that is, it sets X to the result of X modulo Y).
 * rcv X recovers the frequency of the last sound played, but only when the value of X is not zero. (If it is zero, the command does nothing.)
 * jgz X Y jumps with an offset of the value of Y, but only if the value of X is greater than zero. (An offset of 2 skips the next instruction, an offset of -1 jumps to the previous instruction, and so on.)
 *
 * Many of the instructions can take either a register (a single letter) or a number. The value of a register is the integer it contains; the value of a number is that number.
 *
 * After each jump instruction, the program continues with the instruction to which the jump jumped. After any other instruction, the program continues with the next instruction. Continuing (or jumping) off either end of the program terminates it.
 *
 * For example:
 *
 * set a 1
 * add a 2
 * mul a a
 * mod a 5
 * snd a
 * set a 0
 * rcv a
 * jgz a -1
 * set a 1
 * jgz a -2
 *
 * The first four instructions set a to 1, add 2 to it, square it, and then set it to itself modulo 5, resulting in a value of 4.
 * Then, a sound with frequency 4 (the value of a) is played.
 * After that, a is set to 0, causing the subsequent rcv and jgz instructions to both be skipped (rcv because a is 0, and jgz because a is not greater than 0).
 * Finally, a is set to 1, causing the next jgz instruction to activate, jumping back two instructions to another jump, which jumps again to the rcv, which ultimately triggers the recover operation.
 *
 * At the time the recover operation is executed, the frequency of the last sound played is 4.
 *
 * What is the value of the recovered frequency (the value of the most recently played sound) the first time a rcv instruction is executed with a non-zero value?
 *
 * Your puzzle answer was 4601.
 * --- Part Two ---
 *
 * As you congratulate yourself for a job well done, you notice that the documentation has been on the back of the tablet this entire time. While you actually got most of the instructions correct, there are a few key differences. This assembly code isn't about sound at all - it's meant to be run twice at the same time.
 *
 * Each running copy of the program has its own set of registers and follows the code independently - in fact, the programs don't even necessarily run at the same speed. To coordinate, they use the send (snd) and receive (rcv) instructions:
 *
 * snd X sends the value of X to the other program. These values wait in a queue until that program is ready to receive them. Each program has its own message queue, so a program can never receive a message it sent.
 * rcv X receives the next value and stores it in register X. If no values are in the queue, the program waits for a value to be sent to it. Programs do not continue to the next instruction until they have received a value. Values are received in the order they are sent.
 *
 * Each program also has its own program ID (one 0 and the other 1); the register p should begin with this value.
 *
 * For example:
 *
 * snd 1
 * snd 2
 * snd p
 * rcv a
 * rcv b
 * rcv c
 * rcv d
 *
 * Both programs begin by sending three values to the other. Program 0 sends 1, 2, 0; program 1 sends 1, 2, 1. Then, each program receives a value (both 1) and stores it in a, receives another value (both 2) and stores it in b, and then each receives the program ID of the other program (program 0 receives 1; program 1 receives 0) and stores it in c. Each program now sees a different value in its own copy of register c.
 *
 * Finally, both programs try to rcv a fourth time, but no data is waiting for either of them, and they reach a deadlock. When this happens, both programs terminate.
 *
 * It should be noted that it would be equally valid for the programs to run at different speeds; for example, program 0 might have sent all three values and then stopped at the first rcv before program 1 executed even its first instruction.
 *
 * Once both of your programs have terminated (regardless of what caused them to do so), how many times did program 1 send a value?
 *
 * Your puzzle answer was 6858.
 */
public class Day18 {

    public static void main(String[] args) throws FileNotFoundException {
        Program p0 = new Program();

        Scanner sc = new Scanner(new File("./etc/aoc2017/in18"));
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Scanner sc2 = new Scanner(line);
            String cmd = sc2.next();
            if (cmd.startsWith("snd")) {
                Snd e = new Snd();
                e.reg = sc2.next();
                p0.addExecutable(e);
            } else if (cmd.startsWith("set")) {
                Set e = new Set();
                e.reg1 = sc2.next();
                e.reg2 = sc2.next();
                p0.addExecutable(e);
            } else if (cmd.startsWith("add")) {
                Add e = new Add();
                e.reg1 = sc2.next();
                e.reg2 = sc2.next();
                p0.addExecutable(e);
            } else if (cmd.startsWith("mul")) {
                Mul e = new Mul();
                e.reg1 = sc2.next();
                e.reg2 = sc2.next();
                p0.addExecutable(e);
            } else if (cmd.startsWith("mod")) {
                Mod e = new Mod();
                e.reg1 = sc2.next();
                e.reg2 = sc2.next();
                p0.addExecutable(e);
            } else if (cmd.startsWith("rcv")) {
                Rcv e = new Rcv();
                e.reg = sc2.next();
                p0.addExecutable(e);
            } else if (cmd.startsWith("jgz")) {
                Jgz e = new Jgz();
                e.reg1 = sc2.next();
                e.reg2 = sc2.next();
                p0.addExecutable(e);
            }
            sc2.close();
        }
        sc.close();

        Programs ps = new Programs();
        p0.setPid(0);
        Program p1 = new Program(p0);
        p1.setPid(1);
        ps.p0 = p0;
        ps.p1 = p1;
        ps.run();
    }

    public interface Executable {
        void exec(State state);
    }

    public static class Programs {
        public Program p0;
        public Program p1;

        public void run() {
            p0.link(p1);
            while (!(p0.isBlocked() && p1.isBlocked())) {
                p0.execDefaultState();
                p1.execDefaultState();
            }
            System.out.println(p1.state.sndCount);
        }
    }

    public static class Program implements Executable {
        private State state = new State();
        private List<Executable> execs = new ArrayList<>();
        private int pid;

        public Program() {
        }

        public Program(Program p) {
            execs = p.execs;
        }

        public void addExecutable(Executable r) {
            execs.add(r);
        }

        public void execDefaultState() {
            exec(this.state);
        }

        public void exec(State state) {
            while (state.line < execs.size() && !isBlocked()) {
                execs.get(state.line).exec(state);
            }
        }

        public void setPid(int pid) {
            this.pid = pid;
            state.pid = pid;
            state.p.val = pid;
        }

        public void link(Program p) {
            state.rcvq = p.state.sndq;
            p.state.rcvq = state.sndq;
        }

        public boolean isBlocked() {
            if (state.line < execs.size() &&
                    execs.get(state.line) instanceof Rcv &&
                    state.rcvq.size() <= 0)
                return true;
            return false;
        }
    }

    public static class State {
        public int line = 0;
        public int pid = 0;
        public int sndCount = 0;
        public Register a = new Register();
        public Register b = new Register();
        public Register f = new Register();
        public Register i = new Register();
        public Register p = new Register();
        public long lastSnd = 0;
        public Queue<Long> sndq = new LinkedList<>();
        public Queue<Long> rcvq = null;
        public boolean isBlocked = false;
        private Map<String, Register> registers = new HashMap<>();

        public State() {
            registers.put("a", a);
            registers.put("b", b);
            registers.put("f", f);
            registers.put("i", i);
            registers.put("p", p);
        }

        public String toString() {
            return String.format("{line=%d, a=%d, b=%d, f=%d, i=%d, p=%d}",
                    line, a.val, b.val, f.val, i.val, p.val);
        }

        public Register getRegister(String name) {
            return registers.get(name);
        }
    }

    public static class Register {
        public long val;
    }

    public static class Jgz implements Executable {
        public String reg1;
        public String reg2;

        public void exec(State state) {
            if (regToVal(state, reg1) > 0) {
                state.line += regToVal(state, reg2);
            } else {
                state.line++;
            }
        }
    }

    public static class Mod implements Executable {
        public String reg1;
        public String reg2;

        public void exec(State state) {
            state.getRegister(reg1).val %= regToVal(state, reg2);
            state.line++;
        }
    }

    public static class Add implements Executable {
        public String reg1;
        public String reg2;

        public void exec(State state) {
            state.getRegister(reg1).val += regToVal(state, reg2);
            state.line++;
        }
    }

    public static class Mul implements Executable {
        public String reg1;
        public String reg2;

        public void exec(State state) {
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

    public static class Snd implements Executable {
        public String reg;

        public void exec(State state) {
            state.sndCount++;
            state.sndq.add(regToVal(state, reg));
            state.line++;
        }
    }

    public static class Rcv implements Executable {
        public String reg;

        public void exec(State state) {
            if (state.rcvq.peek() != null) {
                state.getRegister(reg).val = state.rcvq.poll();
                state.line++;
                state.isBlocked = false;
            } else {
                state.isBlocked = true;
            }
        }
    }

    public static class Snd2 implements Executable {
        public String reg;

        public void exec(State state) {
            state.lastSnd = regToVal(state, reg);
            state.line++;
        }
    }

    public static class Rcv2 implements Executable {
        public String reg;

        public void exec(State state) {
            if (regToVal(state, reg) != 0) {
                System.out.println("Recovering " + state.lastSnd);
                System.exit(1);
            }
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