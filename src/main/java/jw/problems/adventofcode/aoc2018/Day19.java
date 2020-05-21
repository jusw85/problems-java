package jw.problems.adventofcode.aoc2018;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://adventofcode.com/2018/day/19
 *
 * --- Day 19: Go With The Flow ---
 *
 * With the Elves well on their way constructing the North Pole base, you turn your attention back to understanding the inner workings of programming the device.
 *
 * You can't help but notice that the device's opcodes don't contain any flow control like jump instructions. The device's manual goes on to explain:
 *
 * "In programs where flow control is required, the instruction pointer can be bound to a register so that it can be manipulated directly. This way, setr/seti can function as absolute jumps, addr/addi can function as relative jumps, and other opcodes can cause truly fascinating effects."
 *
 * This mechanism is achieved through a declaration like #ip 1, which would modify register 1 so that accesses to it let the program indirectly access the instruction pointer itself. To compensate for this kind of binding, there are now six registers (numbered 0 through 5); the five not bound to the instruction pointer behave as normal. Otherwise, the same rules apply as the last time you worked with this device.
 *
 * When the instruction pointer is bound to a register, its value is written to that register just before each instruction is executed, and the value of that register is written back to the instruction pointer immediately after each instruction finishes execution. Afterward, move to the next instruction by adding one to the instruction pointer, even if the value in the instruction pointer was just updated by an instruction. (Because of this, instructions must effectively set the instruction pointer to the instruction before the one they want executed next.)
 *
 * The instruction pointer is 0 during the first instruction, 1 during the second, and so on. If the instruction pointer ever causes the device to attempt to load an instruction outside the instructions defined in the program, the program instead immediately halts. The instruction pointer starts at 0.
 *
 * It turns out that this new information is already proving useful: the CPU in the device is not very powerful, and a background process is occupying most of its time. You dump the background process' declarations and instructions to a file (your puzzle input), making sure to use the names of the opcodes rather than the numbers.
 *
 * For example, suppose you have the following program:
 *
 * #ip 0
 * seti 5 0 1
 * seti 6 0 2
 * addi 0 1 0
 * addr 1 2 3
 * setr 1 0 0
 * seti 8 0 4
 * seti 9 0 5
 *
 * When executed, the following instructions are executed. Each line contains the value of the instruction pointer at the time the instruction started, the values of the six registers before executing the instructions (in square brackets), the instruction itself, and the values of the six registers after executing the instruction (also in square brackets).
 *
 * ip=0 [0, 0, 0, 0, 0, 0] seti 5 0 1 [0, 5, 0, 0, 0, 0]
 * ip=1 [1, 5, 0, 0, 0, 0] seti 6 0 2 [1, 5, 6, 0, 0, 0]
 * ip=2 [2, 5, 6, 0, 0, 0] addi 0 1 0 [3, 5, 6, 0, 0, 0]
 * ip=4 [4, 5, 6, 0, 0, 0] setr 1 0 0 [5, 5, 6, 0, 0, 0]
 * ip=6 [6, 5, 6, 0, 0, 0] seti 9 0 5 [6, 5, 6, 0, 0, 9]
 *
 * In detail, when running this program, the following events occur:
 *
 * The first line (#ip 0) indicates that the instruction pointer should be bound to register 0 in this program. This is not an instruction, and so the value of the instruction pointer does not change during the processing of this line.
 * The instruction pointer contains 0, and so the first instruction is executed (seti 5 0 1). It updates register 0 to the current instruction pointer value (0), sets register 1 to 5, sets the instruction pointer to the value of register 0 (which has no effect, as the instruction did not modify register 0), and then adds one to the instruction pointer.
 * The instruction pointer contains 1, and so the second instruction, seti 6 0 2, is executed. This is very similar to the instruction before it: 6 is stored in register 2, and the instruction pointer is left with the value 2.
 * The instruction pointer is 2, which points at the instruction addi 0 1 0. This is like a relative jump: the value of the instruction pointer, 2, is loaded into register 0. Then, addi finds the result of adding the value in register 0 and the value 1, storing the result, 3, back in register 0. Register 0 is then copied back to the instruction pointer, which will cause it to end up 1 larger than it would have otherwise and skip the next instruction (addr 1 2 3) entirely. Finally, 1 is added to the instruction pointer.
 * The instruction pointer is 4, so the instruction setr 1 0 0 is run. This is like an absolute jump: it copies the value contained in register 1, 5, into register 0, which causes it to end up in the instruction pointer. The instruction pointer is then incremented, leaving it at 6.
 * The instruction pointer is 6, so the instruction seti 9 0 5 stores 9 into register 5. The instruction pointer is incremented, causing it to point outside the program, and so the program ends.
 *
 * What value is left in register 0 when the background process halts?
 *
 * Your puzzle answer was 1152.
 * --- Part Two ---
 *
 * A new background process immediately spins up in its place. It appears identical, but on closer inspection, you notice that this time, register 0 started with the value 1.
 *
 * What value is left in register 0 when this new background process halts?
 *
 * Your puzzle answer was 12690000.
 */

//addi 2 16 2  // r2 += 16
//seti 1 0 1   // r1 = 1
//	seti 1 4 3   // r3 = 1
//		mulr 1 3 4   // r4 = r1 * r3
//		* eqrr 4 5 4   // r4 = r4 == r5 ? 1 : 0
//		addr 4 2 2   // r2 = r2 + r4
//		addi 2 1 2   // r2 += 1
//		addr 1 0 0   // r0 = r0 + r1
//		addi 3 1 3   // r3 += 1
//		* gtrr 3 5 4   // r4 = r3 > r5 ? 1 : 0
//		addr 2 4 2   // r2 = r2 + r4
//		seti 2 5 2   // r2 = 2
//	addi 1 1 1   // r1 += 1
//	gtrr 1 5 4   // r4 = r1 > r5 ? 1 : 0
//	addr 4 2 2   // r2 = r2 + r4
//	seti 1 1 2   // r2 = 1
//mulr 2 2 2   // r2 *= r2 (end)
//addi 5 2 5   // r5 += 2
//mulr 5 5 5   // r5 *= r5
//mulr 2 5 5   // r5 = r2 * r5 (r2 = 19)
//muli 5 11 5  // r5 *= 11
//addi 4 5 4   // r4 += 5
//mulr 4 2 4   // r4 = r2 * r4 (r2 = 22)
//addi 4 9 4   // r4 += 9
//addr 5 4 5   // r5 = r4 + r5
//addr 2 0 2   // r2 = r0 + r2
//seti 0 0 2
//setr 2 3 4   // r4 = r2 (r2 = 27)
//mulr 4 2 4   // r4 = r2 * r4 (r2 = 28)
//addr 2 4 4   // r4 = r2 + r4 (r2 = 29)
//mulr 2 4 4   // r4 = r2 * r4 (r2 = 30)
//muli 4 14 4  // r4 *= 14
//mulr 4 2 4   // r4 = r2 * r4 (r2 = 32)
//addr 5 4 5   // r5 = r4 + r5
//seti 0 6 0   // r0 = 0
//seti 0 3 2   // r2 = 0
public class Day19 {

    public static Pattern p1 = Pattern.compile("#ip (\\d+)");
    public static Pattern p2 = Pattern.compile("([a-z]{4}) (\\d+) (\\d+) (\\d+)");

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2018/in19"));
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
        p.execDefaultState();
        System.out.println(algoV2(955));
        System.out.println(algoV2(10551355));
    }

    public static long algoV1(long in) {
        long r0 = 0;
        long r5 = in;

        long r1 = 1;
        do {
            long r3 = 1;
            do {
                if (r1 * r3 == r5) {
                    r0 = r0 + r1;
                }
                r3++;
            } while (r3 <= r5);
            r1++;
        } while (r1 <= r5);
        return r0;
    }


    public static long algoV2(long in) {
        long sum = 0;
        for (long i = 1; i <= in; i++) {
            if (in % i == 0) {
                sum += i;
            }
        }
        return sum;
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
