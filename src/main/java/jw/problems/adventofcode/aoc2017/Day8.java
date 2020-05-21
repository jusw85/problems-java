package jw.problems.adventofcode.aoc2017;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http://adventofcode.com/2017/day/8
 *
 * --- Day 8: I Heard You Like Registers ---
 *
 * You receive a signal directly from the CPU. Because of your recent assistance with jump instructions, it would like you to compute the result of a series of unusual register instructions.
 *
 * Each instruction consists of several parts: the register to modify, whether to increase or decrease that register's value, the amount by which to increase or decrease it, and a condition. If the condition fails, skip the instruction without modifying the register. The registers all start at 0. The instructions look like this:
 *
 * b inc 5 if a > 1
 * a inc 1 if b < 5
 * c dec -10 if a >= 1
 * c inc -20 if c == 10
 *
 * These instructions would be processed as follows:
 *
 * Because a starts at 0, it is not greater than 1, and so b is not modified.
 * a is increased by 1 (to 1) because b is less than 5 (it is 0).
 * c is decreased by -10 (to 10) because a is now greater than or equal to 1 (it is 1).
 * c is increased by -20 (to -10) because c is equal to 10.
 *
 * After this process, the largest value in any register is 1.
 *
 * You might also encounter <= (less than or equal to) or != (not equal to). However, the CPU doesn't have the bandwidth to tell you what all the registers are named, and leaves that to you to determine.
 *
 * What is the largest value in any register after completing the instructions in your puzzle input?
 *
 * Your puzzle answer was 5075.
 * --- Part Two ---
 *
 * To be safe, the CPU also needs to know the highest value held in any register during this process so that it can decide how much memory to allocate to these operations. For example, in the above instructions, the highest value ever held was 10 (in register c after the third instruction was evaluated).
 *
 * Your puzzle answer was 7310.
 */
public class Day8 {

    public static Pattern p = Pattern.compile("([^ ]+) ([^ ]+) (-?\\d+) if (.+)");

    public static ScriptEngine engine;

    static {
        ScriptEngineManager mgr = new ScriptEngineManager();
        engine = mgr.getEngineByName("JavaScript");
    }

    public static void main(String[] args) throws FileNotFoundException, ScriptException {
        Scanner sc = new Scanner(new File("./etc/aoc2017/in8"));
        Map<String, Register> registers = new HashMap<>();
        int max = Integer.MIN_VALUE;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = p.matcher(line);
            m.matches();
            String regId = m.group(1);
            String op = m.group(2);
            int val = Integer.parseInt(m.group(3));
            String cond = m.group(4);

            if (evalCond(registers, cond)) {
                Register reg;
                if (registers.containsKey(regId)) {
                    reg = registers.get(regId);
                } else {
                    reg = new Register(regId);
                    registers.put(regId, reg);
                }
                if (op.equals("inc")) {
                    reg.val += val;
                } else {
                    reg.val -= val;
                }
                if (reg.val > max) {
                    max = reg.val;
                }
            }
        }
        sc.close();
        System.out.println(max);

        max = Integer.MIN_VALUE;
        for (Register reg : registers.values()) {
            if (reg.val > max) {
                max = reg.val;
            }
        }
        System.out.println(max);
    }

    public static boolean evalCond(Map<String, Register> registers, String cond) throws ScriptException {
        Scanner sc = new Scanner(cond);
        String regId = sc.next();
        String val = "0";
        if (registers.containsKey(regId)) {
            val = String.valueOf(registers.get(regId).val);
        }
        cond = cond.replace(regId, val);
        return (boolean) engine.eval(cond);
    }

    public static class Register {
        public String id;
        public int val = 0;

        public Register(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Register{");
            sb.append("id='").append(id).append('\'');
            sb.append(", val=").append(val);
            sb.append('}');
            return sb.toString();
        }
    }

}