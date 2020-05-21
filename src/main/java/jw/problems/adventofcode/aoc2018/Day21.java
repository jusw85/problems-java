package jw.problems.adventofcode.aoc2018;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://adventofcode.com/2018/day/21
 *
 * --- Day 21: Chronal Conversion ---
 *
 * You should have been watching where you were going, because as you wander the new North Pole base, you trip and fall into a very deep hole!
 *
 * Just kidding. You're falling through time again.
 *
 * If you keep up your current pace, you should have resolved all of the temporal anomalies by the next time the device activates. Since you have very little interest in browsing history in 500-year increments for the rest of your life, you need to find a way to get back to your present time.
 *
 * After a little research, you discover two important facts about the behavior of the device:
 *
 * First, you discover that the device is hard-wired to always send you back in time in 500-year increments. Changing this is probably not feasible.
 *
 * Second, you discover the activation system (your puzzle input) for the time travel module. Currently, it appears to run forever without halting.
 *
 * If you can cause the activation system to halt at a specific moment, maybe you can make the device send you so far back in time that you cause an integer underflow in time itself and wrap around back to your current time!
 *
 * The device executes the program as specified in manual section one and manual section two.
 *
 * Your goal is to figure out how the program works and cause it to halt. You can only control register 0; every other register begins at 0 as usual.
 *
 * Because time travel is a dangerous activity, the activation system begins with a few instructions which verify that bitwise AND (via bani) does a numeric operation and not an operation as if the inputs were interpreted as strings. If the test fails, it enters an infinite loop re-running the test instead of allowing the program to execute normally. If the test passes, the program continues, and assumes that all other bitwise operations (banr, bori, and borr) also interpret their inputs as numbers. (Clearly, the Elves who wrote this system were worried that someone might introduce a bug while trying to emulate this system with a scripting language.)
 *
 * What is the lowest non-negative integer value for register 0 that causes the program to halt after executing the fewest instructions? (Executing the same instruction multiple times counts as multiple instructions executed.)
 *
 * Your puzzle answer was 10961197.
 * --- Part Two ---
 *
 * In order to determine the timing window for your underflow exploit, you also need an upper bound:
 *
 * What is the lowest non-negative integer value for register 0 that causes the program to halt after executing the most instructions? (The program must actually halt; running forever does not count as halting.)
 *
 * Your puzzle answer was 8164934.
 */

//1		    seti 123 x 4			// r4 = 123
//2		    bani 4 456 4			// r4 = r4 & 456
//3	    *	eqri 4 72 4				// if (r4 == 72) goto 6 else goto 1
//4		    addr 4 3 3
//5			seti 0 x 3
//6	    	seti 0 x 4				// r4 = 0
//7		    bori 4 65536 1			// r1 = r4 | 65536 (10000000000000000)
//8	    	seti 678134 x 4			// r4 = 678134
//9		    bani 1 255 5			// r5 = r1 & 255 (11111111)
//10		addr 4 5 4				// r4 = r4 + r5
//11		bani 4 16777215 4		// r4 = r4 & 16777215 (111111111111111111111111)
//12		muli 4 65899 4			// r4 *= 65899
//13		bani 4 16777215 4		// r4 = r4 & 16777215 (111111111111111111111111) (0 <= r4 <= 16777215)
//14	*	gtir 256 1 5			// if (256 > r1) goto goto 17(29) else goto 18
//15		addr 5 3 3
//16		addi 3 1 3
//17			seti 27 x 3			// goto 29
//18		seti 0 x 5				// r5 = 0
//19		addi 5 1 2				// r2 = r5 + 1
//20		muli 2 256 2			// r2 *= 256
//21	*	gtrr 2 1 2				// if (r2 > r1) goto 27 else goto 25
//22		addr 2 3 3
//23		addi 3 1 3
//24			seti 25 x 3
//25		addi 5 1 5				// r5++
//26		seti 17 x 3				// goto 19
//27		setr 5 x 1				// r1 = r5
//28		seti 7 x 3				// goto 9
//29	*	eqrr 4 0 5				// if (r4 == r0) end else goto 7
//30		addr 5 3 3
//31			seti 5 x 3
public class Day21 {

    public static Pattern p1 = Pattern.compile("#ip (\\d+)");
    public static Pattern p2 = Pattern.compile("([a-z]{4}) (\\d+) (\\d+) (\\d+)");

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2018/in21"));
        String line = sc.nextLine();
        Matcher m = p1.matcher(line);
        m.matches();
        Program p = new Program(6, Integer.parseInt(m.group(1)));

        while (sc.hasNextLine()) {
            line = sc.nextLine();
            m = p2.matcher(line);
            m.matches();
            Instruction.Type type = Instruction.Type.valueOf(m.group(1).toUpperCase());
            long arg0 = Long.parseLong(m.group(2));
            long arg1 = Long.parseLong(m.group(3));
            long arg2 = Long.parseLong(m.group(4));
            Instruction i = new Instruction(type, arg0, arg1, arg2);
            p.addExecutable(i);
        }
        sc.close();
        algoV2();
    }

    public static void algoV1() {
        long r0 = 0, r1 = 0, r2 = 0, r3 = 0, r4 = 0, r5 = 0;
        r1 = r4 | 65536; //a
        r4 = 678134;
        r5 = r1 & 255;  // b
        r4 = r4 + r5;
        r4 &= 16777215;
        r4 *= 65899;
        r4 &= 16777215;
        if (256 > r1) {
            if (r4 == r0) {
                return;
            } else {
//                goto a
            }
        } else {
            r5 = 0;
            r2 = r5 + 1; // c
            r2 *= 256;
            if (r2 > r1) {
                r1 = r5;
//                goto b
            } else {
                r5++;
//                goto c
            }
        }
    }

    public static void algoV2() {
        long r1 = 0, r4 = 0;
        Set<Long> s = new HashSet<>();
        long prev = 0;
        while (true) {
            r1 = r4 | 65536;
            r4 = 678134;
            while (true) {
                r4 = r4 + (r1 & 255);
                r4 &= 16777215;
                r4 *= 65899;
                r4 &= 16777215;
                if (r1 < 256) {
                    if (s.size() == 0) {
                        System.out.println(r4);
                    }
                    if (!s.contains(r4)) {
                        s.add(r4);
                    } else {
                        System.out.println(prev);
                        return;
                    }
                    prev = r4;
                    break;
                } else {
                    r1 /= 256;
                }
            }
        }

    }

    public interface Executable {
        void exec(State state);
    }

    public static class Program implements Executable {
        private int ip;
        private State state;
        private List<Executable> execs = new ArrayList<>();

        public Program(int numRegisters, int ip) {
            state = new State(numRegisters);
            this.ip = ip;
        }

        public void addExecutable(Executable r) {
            execs.add(r);
        }

        public void execDefaultState() {
            exec(this.state);
        }

        public void exec(State state) {
            Register ipRegister = state.getRegister(ip);
            int line = (int) ipRegister.val;
            while (line < execs.size()) {
                execs.get(line).exec(state);
                line = (int) (++ipRegister.val);
            }
            System.out.println(state);
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

    public static class State {
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
            final StringBuilder sb = new StringBuilder();
            sb.append("registers=").append(registers);
            return sb.toString();
        }
    }

    public static class Instruction implements Executable {

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
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Instruction{");
            sb.append(type);
            sb.append(", ").append(arg0);
            sb.append(", ").append(arg1);
            sb.append(", ").append(arg2);
            sb.append('}');
            return sb.toString();
        }
    }

}
