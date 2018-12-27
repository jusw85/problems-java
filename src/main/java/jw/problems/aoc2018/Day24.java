package jw.problems.aoc2018;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * https://adventofcode.com/2018/day/24
 *
 * --- Day 24: Immune System Simulator 20XX ---
 *
 * After a weird buzzing noise, you appear back at the man's cottage. He seems relieved to see his friend, but quickly notices that the little reindeer caught some kind of cold while out exploring.
 *
 * The portly man explains that this reindeer's immune system isn't similar to regular reindeer immune systems:
 *
 * The immune system and the infection each have an army made up of several groups; each group consists of one or more identical units. The armies repeatedly fight until only one army has units remaining.
 *
 * Units within a group all have the same hit points (amount of damage a unit can take before it is destroyed), attack damage (the amount of damage each unit deals), an attack type, an initiative (higher initiative units attack first and win ties), and sometimes weaknesses or immunities. Here is an example group:
 *
 * 18 units each with 729 hit points (weak to fire; immune to cold, slashing)
 * with an attack that does 8 radiation damage at initiative 10
 *
 * Each group also has an effective power: the number of units in that group multiplied by their attack damage. The above group has an effective power of 18 * 8 = 144. Groups never have zero or negative units; instead, the group is removed from combat.
 *
 * Each fight consists of two phases: target selection and attacking.
 *
 * During the target selection phase, each group attempts to choose one target. In decreasing order of effective power, groups choose their targets; in a tie, the group with the higher initiative chooses first. The attacking group chooses to target the group in the enemy army to which it would deal the most damage (after accounting for weaknesses and immunities, but not accounting for whether the defending group has enough units to actually receive all of that damage).
 *
 * If an attacking group is considering two defending groups to which it would deal equal damage, it chooses to target the defending group with the largest effective power; if there is still a tie, it chooses the defending group with the highest initiative. If it cannot deal any defending groups damage, it does not choose a target. Defending groups can only be chosen as a target by one attacking group.
 *
 * At the end of the target selection phase, each group has selected zero or one groups to attack, and each group is being attacked by zero or one groups.
 *
 * During the attacking phase, each group deals damage to the target it selected, if any. Groups attack in decreasing order of initiative, regardless of whether they are part of the infection or the immune system. (If a group contains no units, it cannot attack.)
 *
 * The damage an attacking group deals to a defending group depends on the attacking group's attack type and the defending group's immunities and weaknesses. By default, an attacking group would deal damage equal to its effective power to the defending group. However, if the defending group is immune to the attacking group's attack type, the defending group instead takes no damage; if the defending group is weak to the attacking group's attack type, the defending group instead takes double damage.
 *
 * The defending group only loses whole units from damage; damage is always dealt in such a way that it kills the most units possible, and any remaining damage to a unit that does not immediately kill it is ignored. For example, if a defending group contains 10 units with 10 hit points each and receives 75 damage, it loses exactly 7 units and is left with 3 units at full health.
 *
 * After the fight is over, if both armies still contain units, a new fight begins; combat only ends once one army has lost all of its units.
 *
 * For example, consider the following armies:
 *
 * Immune System:
 * 17 units each with 5390 hit points (weak to radiation, bludgeoning) with
 * an attack that does 4507 fire damage at initiative 2
 * 989 units each with 1274 hit points (immune to fire; weak to bludgeoning,
 * slashing) with an attack that does 25 slashing damage at initiative 3
 *
 * Infection:
 * 801 units each with 4706 hit points (weak to radiation) with an attack
 * that does 116 bludgeoning damage at initiative 1
 * 4485 units each with 2961 hit points (immune to radiation; weak to fire,
 * cold) with an attack that does 12 slashing damage at initiative 4
 *
 * If these armies were to enter combat, the following fights, including details during the target selection and attacking phases, would take place:
 *
 * Immune System:
 * Group 1 contains 17 units
 * Group 2 contains 989 units
 * Infection:
 * Group 1 contains 801 units
 * Group 2 contains 4485 units
 *
 * Infection group 1 would deal defending group 1 185832 damage
 * Infection group 1 would deal defending group 2 185832 damage
 * Infection group 2 would deal defending group 2 107640 damage
 * Immune System group 1 would deal defending group 1 76619 damage
 * Immune System group 1 would deal defending group 2 153238 damage
 * Immune System group 2 would deal defending group 1 24725 damage
 *
 * Infection group 2 attacks defending group 2, killing 84 units
 * Immune System group 2 attacks defending group 1, killing 4 units
 * Immune System group 1 attacks defending group 2, killing 51 units
 * Infection group 1 attacks defending group 1, killing 17 units
 *
 * Immune System:
 * Group 2 contains 905 units
 * Infection:
 * Group 1 contains 797 units
 * Group 2 contains 4434 units
 *
 * Infection group 1 would deal defending group 2 184904 damage
 * Immune System group 2 would deal defending group 1 22625 damage
 * Immune System group 2 would deal defending group 2 22625 damage
 *
 * Immune System group 2 attacks defending group 1, killing 4 units
 * Infection group 1 attacks defending group 2, killing 144 units
 *
 * Immune System:
 * Group 2 contains 761 units
 * Infection:
 * Group 1 contains 793 units
 * Group 2 contains 4434 units
 *
 * Infection group 1 would deal defending group 2 183976 damage
 * Immune System group 2 would deal defending group 1 19025 damage
 * Immune System group 2 would deal defending group 2 19025 damage
 *
 * Immune System group 2 attacks defending group 1, killing 4 units
 * Infection group 1 attacks defending group 2, killing 143 units
 *
 * Immune System:
 * Group 2 contains 618 units
 * Infection:
 * Group 1 contains 789 units
 * Group 2 contains 4434 units
 *
 * Infection group 1 would deal defending group 2 183048 damage
 * Immune System group 2 would deal defending group 1 15450 damage
 * Immune System group 2 would deal defending group 2 15450 damage
 *
 * Immune System group 2 attacks defending group 1, killing 3 units
 * Infection group 1 attacks defending group 2, killing 143 units
 *
 * Immune System:
 * Group 2 contains 475 units
 * Infection:
 * Group 1 contains 786 units
 * Group 2 contains 4434 units
 *
 * Infection group 1 would deal defending group 2 182352 damage
 * Immune System group 2 would deal defending group 1 11875 damage
 * Immune System group 2 would deal defending group 2 11875 damage
 *
 * Immune System group 2 attacks defending group 1, killing 2 units
 * Infection group 1 attacks defending group 2, killing 142 units
 *
 * Immune System:
 * Group 2 contains 333 units
 * Infection:
 * Group 1 contains 784 units
 * Group 2 contains 4434 units
 *
 * Infection group 1 would deal defending group 2 181888 damage
 * Immune System group 2 would deal defending group 1 8325 damage
 * Immune System group 2 would deal defending group 2 8325 damage
 *
 * Immune System group 2 attacks defending group 1, killing 1 unit
 * Infection group 1 attacks defending group 2, killing 142 units
 *
 * Immune System:
 * Group 2 contains 191 units
 * Infection:
 * Group 1 contains 783 units
 * Group 2 contains 4434 units
 *
 * Infection group 1 would deal defending group 2 181656 damage
 * Immune System group 2 would deal defending group 1 4775 damage
 * Immune System group 2 would deal defending group 2 4775 damage
 *
 * Immune System group 2 attacks defending group 1, killing 1 unit
 * Infection group 1 attacks defending group 2, killing 142 units
 *
 * Immune System:
 * Group 2 contains 49 units
 * Infection:
 * Group 1 contains 782 units
 * Group 2 contains 4434 units
 *
 * Infection group 1 would deal defending group 2 181424 damage
 * Immune System group 2 would deal defending group 1 1225 damage
 * Immune System group 2 would deal defending group 2 1225 damage
 *
 * Immune System group 2 attacks defending group 1, killing 0 units
 * Infection group 1 attacks defending group 2, killing 49 units
 *
 * Immune System:
 * No groups remain.
 * Infection:
 * Group 1 contains 782 units
 * Group 2 contains 4434 units
 *
 * In the example above, the winning army ends up with 782 + 4434 = 5216 units.
 *
 * You scan the reindeer's condition (your puzzle input); the white-bearded man looks nervous. As it stands now, how many units would the winning army have?
 *
 * Your puzzle answer was 15470.
 * --- Part Two ---
 *
 * Things aren't looking good for the reindeer. The man asks whether more milk and cookies would help you think.
 *
 * If only you could give the reindeer's immune system a boost, you might be able to change the outcome of the combat.
 *
 * A boost is an integer increase in immune system units' attack damage. For example, if you were to boost the above example's immune system's units by 1570, the armies would instead look like this:
 *
 * Immune System:
 * 17 units each with 5390 hit points (weak to radiation, bludgeoning) with
 * an attack that does 6077 fire damage at initiative 2
 * 989 units each with 1274 hit points (immune to fire; weak to bludgeoning,
 * slashing) with an attack that does 1595 slashing damage at initiative 3
 *
 * Infection:
 * 801 units each with 4706 hit points (weak to radiation) with an attack
 * that does 116 bludgeoning damage at initiative 1
 * 4485 units each with 2961 hit points (immune to radiation; weak to fire,
 * cold) with an attack that does 12 slashing damage at initiative 4
 *
 * With this boost, the combat proceeds differently:
 *
 * Immune System:
 * Group 2 contains 989 units
 * Group 1 contains 17 units
 * Infection:
 * Group 1 contains 801 units
 * Group 2 contains 4485 units
 *
 * Infection group 1 would deal defending group 2 185832 damage
 * Infection group 1 would deal defending group 1 185832 damage
 * Infection group 2 would deal defending group 1 53820 damage
 * Immune System group 2 would deal defending group 1 1577455 damage
 * Immune System group 2 would deal defending group 2 1577455 damage
 * Immune System group 1 would deal defending group 2 206618 damage
 *
 * Infection group 2 attacks defending group 1, killing 9 units
 * Immune System group 2 attacks defending group 1, killing 335 units
 * Immune System group 1 attacks defending group 2, killing 32 units
 * Infection group 1 attacks defending group 2, killing 84 units
 *
 * Immune System:
 * Group 2 contains 905 units
 * Group 1 contains 8 units
 * Infection:
 * Group 1 contains 466 units
 * Group 2 contains 4453 units
 *
 * Infection group 1 would deal defending group 2 108112 damage
 * Infection group 1 would deal defending group 1 108112 damage
 * Infection group 2 would deal defending group 1 53436 damage
 * Immune System group 2 would deal defending group 1 1443475 damage
 * Immune System group 2 would deal defending group 2 1443475 damage
 * Immune System group 1 would deal defending group 2 97232 damage
 *
 * Infection group 2 attacks defending group 1, killing 8 units
 * Immune System group 2 attacks defending group 1, killing 306 units
 * Infection group 1 attacks defending group 2, killing 29 units
 *
 * Immune System:
 * Group 2 contains 876 units
 * Infection:
 * Group 2 contains 4453 units
 * Group 1 contains 160 units
 *
 * Infection group 2 would deal defending group 2 106872 damage
 * Immune System group 2 would deal defending group 2 1397220 damage
 * Immune System group 2 would deal defending group 1 1397220 damage
 *
 * Infection group 2 attacks defending group 2, killing 83 units
 * Immune System group 2 attacks defending group 2, killing 427 units
 *
 * After a few fights...
 *
 * Immune System:
 * Group 2 contains 64 units
 * Infection:
 * Group 2 contains 214 units
 * Group 1 contains 19 units
 *
 * Infection group 2 would deal defending group 2 5136 damage
 * Immune System group 2 would deal defending group 2 102080 damage
 * Immune System group 2 would deal defending group 1 102080 damage
 *
 * Infection group 2 attacks defending group 2, killing 4 units
 * Immune System group 2 attacks defending group 2, killing 32 units
 *
 * Immune System:
 * Group 2 contains 60 units
 * Infection:
 * Group 1 contains 19 units
 * Group 2 contains 182 units
 *
 * Infection group 1 would deal defending group 2 4408 damage
 * Immune System group 2 would deal defending group 1 95700 damage
 * Immune System group 2 would deal defending group 2 95700 damage
 *
 * Immune System group 2 attacks defending group 1, killing 19 units
 *
 * Immune System:
 * Group 2 contains 60 units
 * Infection:
 * Group 2 contains 182 units
 *
 * Infection group 2 would deal defending group 2 4368 damage
 * Immune System group 2 would deal defending group 2 95700 damage
 *
 * Infection group 2 attacks defending group 2, killing 3 units
 * Immune System group 2 attacks defending group 2, killing 30 units
 *
 * After a few more fights...
 *
 * Immune System:
 * Group 2 contains 51 units
 * Infection:
 * Group 2 contains 40 units
 *
 * Infection group 2 would deal defending group 2 960 damage
 * Immune System group 2 would deal defending group 2 81345 damage
 *
 * Infection group 2 attacks defending group 2, killing 0 units
 * Immune System group 2 attacks defending group 2, killing 27 units
 *
 * Immune System:
 * Group 2 contains 51 units
 * Infection:
 * Group 2 contains 13 units
 *
 * Infection group 2 would deal defending group 2 312 damage
 * Immune System group 2 would deal defending group 2 81345 damage
 *
 * Infection group 2 attacks defending group 2, killing 0 units
 * Immune System group 2 attacks defending group 2, killing 13 units
 *
 * Immune System:
 * Group 2 contains 51 units
 * Infection:
 * No groups remain.
 *
 * This boost would allow the immune system's armies to win! It would be left with 51 units.
 *
 * You don't even know how you could boost the reindeer's immune system or what effect it might have, so you need to be cautious and find the smallest boost that would allow the immune system to win.
 *
 * How many units does the immune system have left after getting the smallest boost it needs to win?
 *
 * Your puzzle answer was 5742.
 */
public class Day24 {

    public static void main(String[] args) throws FileNotFoundException {
        part1();
        part2();
    }

    public static void part1() throws FileNotFoundException {
        FightSim sim = parseInput("./etc/aoc2018/in24");
        sim.fight();
        System.out.println(sim.unitsLeft());
    }

    public static void part2() throws FileNotFoundException {
        FightSim sim;
        int i = 0;
        do {
            sim = parseInput("./etc/aoc2018/in24");
            sim.immuneAttackBoost(i++);
            sim.fight();
        } while (!sim.didImmuneWin());
        System.out.println(sim.unitsLeft());
    }

    public enum UnitType {
        IMMUNE, INFECTION
    }

    public static class Unit {
        public int idx;
        public UnitType type;
        public int count;
        public int hp;
        public int atk;
        public String atkType;
        public int initiative;
        public List<String> immunes;
        public List<String> weaks;

        public Unit(int idx, UnitType type, int count, int hp, int atk, String atkType, int initiative, List<String> immunes, List<String> weaks) {
            this.idx = idx;
            this.type = type;
            this.count = count;
            this.hp = hp;
            this.atk = atk;
            this.atkType = atkType;
            this.initiative = initiative;
            this.immunes = immunes;
            this.weaks = weaks;
        }

        public int ep() {
            return count * atk;
        }

        public int damageDealt(Unit o) {
            if (o.immunes.contains(atkType)) {
                return 0;
            } else if (o.weaks.contains(atkType)) {
                return ep() * 2;
            } else {
                return ep();
            }
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Unit{");
            sb.append("idx=").append(idx);
            sb.append(", type=").append(type);
            sb.append(", count=").append(count);
            sb.append(", hp=").append(hp);
            sb.append(", atk=").append(atk);
            sb.append(", atkType='").append(atkType).append('\'');
            sb.append(", initiative=").append(initiative);
            sb.append(", immunes=").append(immunes);
            sb.append(", weaks=").append(weaks);
            sb.append('}');
            return sb.toString();
        }
    }

    public static class FightSim {
        public List<Unit> immune;
        public List<Unit> infection;
        public List<Unit> all;
        private int currentTickKills = 0;

        public FightSim(List<Unit> immune, List<Unit> infection) {
            this.immune = immune;
            this.infection = infection;
            all = new ArrayList<>();
            all.addAll(immune);
            all.addAll(infection);
        }

        public void immuneAttackBoost(int immuneBoost) {
            for (Unit u : immune) {
                u.atk += immuneBoost;
            }
        }

        public boolean didImmuneWin() {
            return immune.size() > 0 && infection.isEmpty();
        }

        public void fight() {
            while ((!(infection.isEmpty() || immune.isEmpty()))) {
                tick();
                if (currentTickKills <= 0) {
                    break;
                }
            }
        }

        public int unitsLeft() {
            int count = 0;
            for (Unit u : all) {
                count += u.count;
            }
            return count;
        }

        public void tick() {
            Map<Unit, Unit> targetPairs = calcTargetPairs();
            doAttack(targetPairs);
        }

        private void doAttack(Map<Unit, Unit> targetPairs) {
            all.sort((o1, o2) -> -Integer.compare(o1.initiative, o2.initiative));
            currentTickKills = 0;
            for (Unit u : all) {
                if (u.count <= 0)
                    continue;
                Unit t = targetPairs.get(u);
                if (t != null) {
                    int dmg = u.damageDealt(t);
                    int kills = dmg / t.hp;
                    kills = Math.min(kills, t.count);
                    t.count -= kills;
                    currentTickKills += kills;
//                    System.out.println(u.type + " " + u.idx + " to " + t.type + " " + t.idx + " kill " + kills);
                }
            }
            Iterator<Unit> it = all.iterator();
            while (it.hasNext()) {
                Unit u = it.next();
                if (u.count <= 0) {
                    it.remove();
                    if (u.type == UnitType.INFECTION) {
                        infection.remove(u);
                    } else {
                        immune.remove(u);
                    }
//                    System.out.println(u.type + " " + u.idx + " death");
                }
            }
        }

        private Map<Unit, Unit> calcTargetPairs() {
            all.sort((o1, o2) -> {
                int res = -Integer.compare(o1.ep(), o2.ep());
                if (res != 0) return res;
                return -Integer.compare(o1.initiative, o2.initiative);
            });
            List<Unit> immuneTargets = new ArrayList<>(immune);
            List<Unit> infectionTargets = new ArrayList<>(infection);
            Map<Unit, Unit> targetPairs = new HashMap<>();
            for (Unit u : all) {
                List<Unit> targets;
                if (u.type == UnitType.IMMUNE) {
                    targets = infectionTargets;
                } else {
                    targets = immuneTargets;
                }
                targets.sort((o1, o2) -> {
                    int res = -Integer.compare(u.damageDealt(o1), u.damageDealt(o2));
                    if (res != 0) {
                        return res;
                    }
                    res = -Integer.compare(o1.ep(), o2.ep());
                    if (res != 0) {
                        return res;
                    }
                    return -Integer.compare(o1.initiative, o2.initiative);
                });

                Unit target = null;
                if (!targets.isEmpty() && u.damageDealt(targets.get(0)) > 0) {
                    target = targets.get(0);
                    targets.remove(target);
                }
                targetPairs.put(u, target);
            }
            return targetPairs;
        }
    }

    public static Pattern p1 = Pattern.compile("(\\d+) units each with (\\d+) hit points (?:\\((.*)\\) )?with an attack that does (\\d+) (\\w+) damage at initiative (\\d+)");
    public static Pattern p2 = Pattern.compile("(immune|weak) to (.*)");

    public static FightSim parseInput(String inFile) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inFile));

        List<Unit> immune = new ArrayList<>();
        List<Unit> infection = new ArrayList<>();
        List<Unit> current = immune;
        UnitType currentType = UnitType.IMMUNE;
        int idx = 1;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.isEmpty()) continue;
            if (line.equals("Immune System:")) {
                idx = 1;
                current = immune;
                currentType = UnitType.IMMUNE;
                continue;
            } else if (line.equals("Infection:")) {
                idx = 1;
                current = infection;
                currentType = UnitType.INFECTION;
                continue;
            }
            Matcher m = p1.matcher(line);
            m.matches();
            int count = Integer.parseInt(m.group(1));
            int hp = Integer.parseInt(m.group(2));
            int atk = Integer.parseInt(m.group(4));
            String atkType = m.group(5);
            int initiative = Integer.parseInt(m.group(6));
            String atkMods = m.group(3);

            List<String> immunes = new ArrayList<>();
            List<String> weaks = new ArrayList<>();
            if (atkMods != null) {
                for (String s : atkMods.split(";")) {
                    m = p2.matcher(s);
                    while (m.find()) {
                        boolean isWeak = m.group(1).equals("weak");
                        Stream.of(m.group(2).split(",")).map(t -> t.trim()).forEach(t -> {
                            if (isWeak) {
                                weaks.add(t);
                            } else {
                                immunes.add(t);
                            }
                        });
                    }
                }
            }
            Unit u = new Unit(idx++, currentType, count, hp, atk, atkType, initiative, immunes, weaks);
            current.add(u);

        }
        sc.close();
        return new FightSim(immune, infection);
    }
}
