package jw.problems.aoc2016;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * http://adventofcode.com/2016/day/11
 *
 * --- Day 11: Radioisotope Thermoelectric Generators ---
 *
 * You come upon a column of four floors that have been entirely sealed off from the rest of the building except for a small dedicated lobby. There are some radiation warnings and a big sign which reads "Radioisotope Testing Facility".
 *
 * According to the project status state, this facility is currently being used to experiment with Radioisotope Thermoelectric Generators (RTGs, or simply "generators") that are designed to be paired with specially-constructed microchips. Basically, an RTG is a highly radioactive rock that generates electricity through heat.
 *
 * The experimental RTGs have poor radiation containment, so they're dangerously radioactive. The chips are prototypes and don't have normal radiation shielding, but they do have the ability to generate an electromagnetic radiation shield when powered. Unfortunately, they can only be powered by their corresponding RTG. An RTG powering a microchip is still dangerous to other microchips.
 *
 * In other words, if a chip is ever left in the same area as another RTG, and it's not connected to its own RTG, the chip will be fried. Therefore, it is assumed that you will follow procedure and keep chips connected to their corresponding RTG when they're in the same room, and away from other RTGs otherwise.
 *
 * These microchips sound very interesting and useful to your current activities, and you'd like to try to retrieve them. The fourth floor of the facility has an assembling machine which can make a self-contained, shielded computer for you to take with you - that is, if you can bring it all of the RTGs and microchips.
 *
 * Within the radiation-shielded part of the facility (in which it's safe to have these pre-assembly RTGs), there is an elevator that can move between the four floors. Its capacity rating means it can carry at most yourself and two RTGs or microchips in any combination. (They're rigged to some heavy diagnostic equipment - the assembling machine will detach it for you.) As a security measure, the elevator will only function if it contains at least one RTG or microchip. The elevator always stops on each floor to recharge, and this takes long enough that the items within it and the items on that floor can irradiate each other. (You can prevent this if a Microchip and its Generator end up on the same floor in this way, as they can be connected while the elevator is recharging.)
 *
 * You make some notes of the locations of each component of interest (your puzzle input). Before you don a hazmat suit and start moving things around, you'd like to have an idea of what you need to do.
 *
 * When you enter the containment area, you and the elevator will start on the first floor.
 *
 * For example, suppose the isolated area has the following arrangement:
 *
 * The first floor contains a hydrogen-compatible microchip and a lithium-compatible microchip.
 * The second floor contains a hydrogen generator.
 * The third floor contains a lithium generator.
 * The fourth floor contains nothing relevant.
 *
 * As a diagram (F# for a Floor number, E for Elevator, H for Hydrogen, L for Lithium, M for Microchip, and G for Generator), the initial state looks like this:
 *
 * F4 .  .  .  .  .
 * F3 .  .  .  LG .
 * F2 .  HG .  .  .
 * F1 E  .  HM .  LM
 *
 * Then, to get everything up to the assembling machine on the fourth floor, the following steps could be taken:
 *
 * Bring the Hydrogen-compatible Microchip to the second floor, which is safe because it can get power from the Hydrogen Generator:
 *
 * F4 .  .  .  .  .
 * F3 .  .  .  LG .
 * F2 E  HG HM .  .
 * F1 .  .  .  .  LM
 *
 * Bring both Hydrogen-related items to the third floor, which is safe because the Hydrogen-compatible microchip is getting power from its generator:
 *
 * F4 .  .  .  .  .
 * F3 E  HG HM LG .
 * F2 .  .  .  .  .
 * F1 .  .  .  .  LM
 *
 * Leave the Hydrogen Generator on floor three, but bring the Hydrogen-compatible Microchip back down with you so you can still use the elevator:
 *
 * F4 .  .  .  .  .
 * F3 .  HG .  LG .
 * F2 E  .  HM .  .
 * F1 .  .  .  .  LM
 *
 * At the first floor, grab the Lithium-compatible Microchip, which is safe because Microchips don't affect each other:
 *
 * F4 .  .  .  .  .
 * F3 .  HG .  LG .
 * F2 .  .  .  .  .
 * F1 E  .  HM .  LM
 *
 * Bring both Microchips up one floor, where there is nothing to fry them:
 *
 * F4 .  .  .  .  .
 * F3 .  HG .  LG .
 * F2 E  .  HM .  LM
 * F1 .  .  .  .  .
 *
 * Bring both Microchips up again to floor three, where they can be temporarily connected to their corresponding generators while the elevator recharges, preventing either of them from being fried:
 *
 * F4 .  .  .  .  .
 * F3 E  HG HM LG LM
 * F2 .  .  .  .  .
 * F1 .  .  .  .  .
 *
 * Bring both Microchips to the fourth floor:
 *
 * F4 E  .  HM .  LM
 * F3 .  HG .  LG .
 * F2 .  .  .  .  .
 * F1 .  .  .  .  .
 *
 * Leave the Lithium-compatible microchip on the fourth floor, but bring the Hydrogen-compatible one so you can still use the elevator; this is safe because although the Lithium Generator is on the destination floor, you can connect Hydrogen-compatible microchip to the Hydrogen Generator there:
 *
 * F4 .  .  .  .  LM
 * F3 E  HG HM LG .
 * F2 .  .  .  .  .
 * F1 .  .  .  .  .
 *
 * Bring both Generators up to the fourth floor, which is safe because you can connect the Lithium-compatible Microchip to the Lithium Generator upon arrival:
 *
 * F4 E  HG .  LG LM
 * F3 .  .  HM .  .
 * F2 .  .  .  .  .
 * F1 .  .  .  .  .
 *
 * Bring the Lithium Microchip with you to the third floor so you can use the elevator:
 *
 * F4 .  HG .  LG .
 * F3 E  .  HM .  LM
 * F2 .  .  .  .  .
 * F1 .  .  .  .  .
 *
 * Bring both Microchips to the fourth floor:
 *
 * F4 E  HG HM LG LM
 * F3 .  .  .  .  .
 * F2 .  .  .  .  .
 * F1 .  .  .  .  .
 *
 * In this arrangement, it takes 11 steps to collect all of the objects at the fourth floor for assembly. (Each elevator stop counts as one step, even if nothing is added to or removed from it.)
 *
 * In your situation, what is the minimum number of steps required to bring all of the objects to the fourth floor?
 *
 * Your puzzle answer was 37.
 * --- Part Two ---
 *
 * You step into the cleanroom separating the lobby from the isolated area and put on the hazmat suit.
 *
 * Upon entering the isolated containment area, however, you notice some extra parts on the first floor that weren't listed on the record outside:
 *
 * An elerium generator.
 * An elerium-compatible microchip.
 * A dilithium generator.
 * A dilithium-compatible microchip.
 *
 * These work just like the other generators and microchips. You'll have to get them up to assembly as well.
 *
 * What is the minimum number of steps required to bring all of the objects, including these four new ones, to the fourth floor?
 *
 * Your puzzle answer was 61.
 */
public class Day11 {

    /*-
     * SG SM PG PM                   EG EM DG DM
     *             TG    RG RM CG CM
     *                TM
     *
     */
    public static void main(String[] args) {
        int[][] s1 = {
                {0, 1, 0, 1},
                {1, 0, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 0}};
        int[][] s2 = {
                {1, 1, 1, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        int[][] s3 = {
                {1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1},
                {0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        Board b1 = new Board(s1, 0, 0);
        Board b2 = new Board(s2, 0, 0);
        Board b3 = new Board(s3, 0, 0);
        System.out.println(b1.solve());
        System.out.println(b2.solve());
        System.out.println(b3.solve());
    }

    public static class Board {
        private int[][] state;
        private int elevator;
        private int depth;

        public Board(int[][] state, int elevator, int depth) {
            this.state = state;
            this.elevator = elevator;
            this.depth = depth;
        }

        private String buildFinalNormString() {
            StringBuilder sb = new StringBuilder();
            sb.append(state.length - 1);
            for (int i = 0; i < state.length; i++) {
                sb.append(',');
                for (int j = 0; j < state[i].length; j++) {
                    char c = i == state.length - 1 ? '1' : '0';
                    sb.append(c);
                }
            }
            return sb.toString();
        }

        public String toNormString() {
            StringBuilder sb = new StringBuilder();
            sb.append(elevator);
            for (int i = 0; i < state.length; i++) {
                sb.append(",");
                for (int j = 0; j < state[i].length; j++) {
                    sb.append(state[i][j]);
                }
            }
            return sb.toString();
        }

        public int solve() {
            Set<String> visited = new HashSet<>();
            Queue<Board> toVisit = new LinkedList<>();
            toVisit.add(this);
            visited.add(toNormString());
            String finalState = buildFinalNormString();

            while (toVisit.size() > 0) {
                Board b = toVisit.poll();
                if (b.toNormString().equals(finalState)) {
                    return b.depth;
                }
                for (Board bt : b.getMoves()) {
                    String ns = bt.toNormString();
                    if (!visited.contains(ns)) {
                        toVisit.add(bt);
                        visited.add(ns);
                    }
                }
            }
            return -1;
        }

        public List<Board> getMoves() {
            List<Board> moves = new ArrayList<>();
            int[] row = state[elevator];
            boolean canMoveUp = elevator < state.length - 1;
            boolean canMoveDown = elevator > 0;

            for (int i = 0; i < row.length; i++) {
                if (row[i] == 1) {
                    List<Board> newMoves = new ArrayList<>();
                    if (canMoveUp) {
                        int[][] ns = cloneArray(state);
                        ns[elevator][i] = 0;
                        ns[elevator + 1][i] = 1;
                        newMoves.add(new Board(ns, elevator + 1, depth + 1));

                        for (int j = i + 1; j < row.length; j++) {
                            if (row[j] == 1) {
                                int[][] ns2 = cloneArray(ns);
                                ns2[elevator][j] = 0;
                                ns2[elevator + 1][j] = 1;
                                newMoves.add(new Board(ns2, elevator + 1, depth + 1));
                            }
                        }
                    }
                    if (canMoveDown) {
                        int[][] ns = cloneArray(state);
                        ns[elevator][i] = 0;
                        ns[elevator - 1][i] = 1;
                        newMoves.add(new Board(ns, elevator - 1, depth + 1));

                        for (int j = i + 1; j < row.length; j++) {
                            if (row[j] == 1) {
                                int[][] ns2 = cloneArray(ns);
                                ns2[elevator][j] = 0;
                                ns2[elevator - 1][j] = 1;
                                newMoves.add(new Board(ns2, elevator - 1, depth + 1));
                            }
                        }
                    }
                    for (Board b : newMoves) {
                        if (b.isValid()) {
                            moves.add(b);
                        }
                    }
                }
            }
            return moves;
        }

        private boolean isValid() {
            for (int i = 0; i < state.length; i++) {
                for (int j = 1; j < state[i].length; j += 2) {
                    if (state[i][j] == 1 && state[i][j - 1] == 0) {
                        for (int k = 0; k < state[i].length; k += 2) {
                            if (state[i][k] == 1) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }
    }

    public static void printArray(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j]);
            }
            System.out.println();
        }
    }

    public static int[][] cloneArray(int[][] in) {
        int[][] out = new int[in.length][in[0].length];
        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < in[i].length; j++) {
                out[i][j] = in[i][j];
            }
        }
        return out;
    }

}