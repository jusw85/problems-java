package jw.problems.adventofcode.aoc2015;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

/**
 * http://adventofcode.com/2015/day/22
 *
 * --- Day 22: Wizard Simulator 20XX ---
 *
 * Little Henry Case decides that defeating bosses with swords and stuff is boring. Now he's playing the game with a wizard. Of course, he gets stuck on another boss and needs your help again.
 *
 * In this version, combat still proceeds with the player and the boss taking alternating turns. The player still goes first. Now, however, you don't get any equipment; instead, you must choose one of your spells to cast. The first character at or below 0 hit points loses.
 *
 * Since you're a wizard, you don't get to wear armor, and you can't attack normally. However, since you do magic damage, your opponent's armor is ignored, and so the boss effectively has zero armor as well. As before, if armor (from a spell, in this case) would reduce damage below 1, it becomes 1 instead - that is, the boss' attacks always deal at least 1 damage.
 *
 * On each of your turns, you must select one of your spells to cast. If you cannot afford to cast any spell, you lose. Spells cost mana; you start with 500 mana, but have no maximum limit. You must have enough mana to cast a spell, and its cost is immediately deducted when you cast it. Your spells are Magic Missile, Drain, Shield, Poison, and Recharge.
 *
 * Magic Missile costs 53 mana. It instantly does 4 damage.
 * Drain costs 73 mana. It instantly does 2 damage and heals you for 2 hit points.
 * Shield costs 113 mana. It starts an effect that lasts for 6 turns. While it is active, your armor is increased by 7.
 * Poison costs 173 mana. It starts an effect that lasts for 6 turns. At the start of each turn while it is active, it deals the boss 3 damage.
 * Recharge costs 229 mana. It starts an effect that lasts for 5 turns. At the start of each turn while it is active, it gives you 101 new mana.
 *
 * Effects all work the same way. Effects apply at the start of both the player's turns and the boss' turns. Effects are created with a timer (the number of turns they last); at the start of each turn, after they apply any effect they have, their timer is decreased by one. If this decreases the timer to zero, the effect ends. You cannot cast a spell that would start an effect which is already active. However, effects can be started on the same turn they end.
 *
 * For example, suppose the player has 10 hit points and 250 mana, and that the boss has 13 hit points and 8 damage:
 *
 * -- Player turn --
 * - Player has 10 hit points, 0 armor, 250 mana
 * - Boss has 13 hit points
 * Player casts Poison.
 *
 * -- Boss turn --
 * - Player has 10 hit points, 0 armor, 77 mana
 * - Boss has 13 hit points
 * Poison deals 3 damage; its timer is now 5.
 * Boss attacks for 8 damage.
 *
 * -- Player turn --
 * - Player has 2 hit points, 0 armor, 77 mana
 * - Boss has 10 hit points
 * Poison deals 3 damage; its timer is now 4.
 * Player casts Magic Missile, dealing 4 damage.
 *
 * -- Boss turn --
 * - Player has 2 hit points, 0 armor, 24 mana
 * - Boss has 3 hit points
 * Poison deals 3 damage. This kills the boss, and the player wins.
 *
 * Now, suppose the same initial conditions, except that the boss has 14 hit points instead:
 *
 * -- Player turn --
 * - Player has 10 hit points, 0 armor, 250 mana
 * - Boss has 14 hit points
 * Player casts Recharge.
 *
 * -- Boss turn --
 * - Player has 10 hit points, 0 armor, 21 mana
 * - Boss has 14 hit points
 * Recharge provides 101 mana; its timer is now 4.
 * Boss attacks for 8 damage!
 *
 * -- Player turn --
 * - Player has 2 hit points, 0 armor, 122 mana
 * - Boss has 14 hit points
 * Recharge provides 101 mana; its timer is now 3.
 * Player casts Shield, increasing armor by 7.
 *
 * -- Boss turn --
 * - Player has 2 hit points, 7 armor, 110 mana
 * - Boss has 14 hit points
 * Shield's timer is now 5.
 * Recharge provides 101 mana; its timer is now 2.
 * Boss attacks for 8 - 7 = 1 damage!
 *
 * -- Player turn --
 * - Player has 1 hit point, 7 armor, 211 mana
 * - Boss has 14 hit points
 * Shield's timer is now 4.
 * Recharge provides 101 mana; its timer is now 1.
 * Player casts Drain, dealing 2 damage, and healing 2 hit points.
 *
 * -- Boss turn --
 * - Player has 3 hit points, 7 armor, 239 mana
 * - Boss has 12 hit points
 * Shield's timer is now 3.
 * Recharge provides 101 mana; its timer is now 0.
 * Recharge wears off.
 * Boss attacks for 8 - 7 = 1 damage!
 *
 * -- Player turn --
 * - Player has 2 hit points, 7 armor, 340 mana
 * - Boss has 12 hit points
 * Shield's timer is now 2.
 * Player casts Poison.
 *
 * -- Boss turn --
 * - Player has 2 hit points, 7 armor, 167 mana
 * - Boss has 12 hit points
 * Shield's timer is now 1.
 * Poison deals 3 damage; its timer is now 5.
 * Boss attacks for 8 - 7 = 1 damage!
 *
 * -- Player turn --
 * - Player has 1 hit point, 7 armor, 167 mana
 * - Boss has 9 hit points
 * Shield's timer is now 0.
 * Shield wears off, decreasing armor by 7.
 * Poison deals 3 damage; its timer is now 4.
 * Player casts Magic Missile, dealing 4 damage.
 *
 * -- Boss turn --
 * - Player has 1 hit point, 0 armor, 114 mana
 * - Boss has 2 hit points
 * Poison deals 3 damage. This kills the boss, and the player wins.
 *
 * You start with 50 hit points and 500 mana points. The boss's actual stats are in your puzzle input. What is the least amount of mana you can spend and still win the fight? (Do not include mana recharge effects as "spending" negative mana.)
 *
 * Your puzzle answer was 900.
 * --- Part Two ---
 *
 * On the next run through the game, you increase the difficulty to hard.
 *
 * At the start of each player turn (before any other effects apply), you lose 1 hit point. If this brings you to or below 0 hit points, you lose.
 *
 * With the same starting stats for you and the boss, what is the least amount of mana you can spend and still win the fight?
 *
 * Your puzzle answer was 1216.
 */
public class Day22 {

    public static class Unit {
        public int hp;
        public int dmg;
        public int arm;
        public int mana;

        public Unit(int hp, int dmg, int arm, int mana) {
            this.hp = hp;
            this.dmg = dmg;
            this.arm = arm;
            this.mana = mana;
        }

        public Unit(Unit o) {
            this.hp = o.hp;
            this.dmg = o.dmg;
            this.arm = o.arm;
            this.mana = o.mana;
        }

        public void attack(Unit o) {
            o.hp -= Math.max((dmg - o.arm), 1);
        }

        public boolean isDead() {
            return hp <= 0;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Unit{");
            sb.append("hp=").append(hp);
            sb.append(", dmg=").append(dmg);
            sb.append(", arm=").append(arm);
            sb.append(", mana=").append(mana);
            sb.append('}');
            return sb.toString();
        }
    }

    public static class State {
        public Unit pc;
        public Unit boss;
        public int manaSpent;
        public int turn = 0;
        public Set<TimedEffect> activeEffects;
        public List<String> cast;

        public State(Unit pc, Unit boss) {
            this.pc = pc;
            this.boss = boss;
            this.activeEffects = new HashSet<>();
            this.cast = new ArrayList<>();
        }

        public State(State o) {
            pc = new Unit(o.pc);
            boss = new Unit(o.boss);
            manaSpent = o.manaSpent;
            turn = o.turn;
            activeEffects = new HashSet<>();
            for (TimedEffect e : o.activeEffects) {
                activeEffects.add(new TimedEffect(e));
            }
            cast = new ArrayList<>();
            cast.addAll(o.cast);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("State{");
            sb.append("pc=").append(pc);
            sb.append(", boss=").append(boss);
            sb.append(", manaSpent=").append(manaSpent);
            sb.append(", turn=").append(turn);
            sb.append(", activeEffects=").append(activeEffects);
            sb.append(", cast=").append(cast);
            sb.append('}');
            return sb.toString();
        }
    }

    public static class Spell {
        public String name;
        public int cost;
        public Effect e;

        public Spell(String name, int cost, Effect e) {
            this.name = name;
            this.cost = cost;
            this.e = e;
        }
    }

    @FunctionalInterface
    public interface Effect {
        void effect(State s);
    }

    public static class AddTimedEffect implements Effect {

        public TimedEffect e;

        public AddTimedEffect(TimedEffect e) {
            this.e = e;
        }

        @Override
        public void effect(State s) {
            s.activeEffects.add(new TimedEffect(e));
        }
    }

    public static class TimedEffect implements Effect {
        public String name;
        public int duration;
        public Effect e;

        public TimedEffect(String name, int duration, Effect e) {
            this.name = name;
            this.duration = duration;
            this.e = e;
        }

        public TimedEffect(TimedEffect o) {
            this.name = o.name;
            this.duration = o.duration;
            this.e = o.e;
        }

        @Override
        public void effect(State s) {
            duration--;
            e.effect(s);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("TimedEffect{");
            sb.append("name='").append(name).append('\'');
            sb.append(", duration=").append(duration);
            sb.append('}');
            return sb.toString();
        }
    }

    public static Map<String, Spell> spellbook;

    static {
        Spell magicMissile = new Spell(
                "MagicMissile", 53,
                (State s) -> s.boss.hp -= 4);
        Spell drain = new Spell(
                "Drain", 73,
                (State s) -> {
                    s.boss.hp -= 2;
                    s.pc.hp += 2;
                });
        TimedEffect shieldBuff = new TimedEffect(
                "ShieldBuff", 6,
                (State s) -> s.pc.arm = 7);
        TimedEffect poisonDebuff = new TimedEffect(
                "PoisonDebuff", 6,
                (State s) -> s.boss.hp -= 3);
        TimedEffect rechargeBuff = new TimedEffect(
                "RechargeBuff", 5,
                (State s) -> s.pc.mana += 101);
        Spell shield = new Spell(
                "Shield", 113, new AddTimedEffect(shieldBuff));
        Spell poison = new Spell(
                "Poison", 173, new AddTimedEffect(poisonDebuff));
        Spell recharge = new Spell(
                "Recharge", 229, new AddTimedEffect(rechargeBuff));

        spellbook = new HashMap<>();
        spellbook.put(magicMissile.name, magicMissile);
        spellbook.put(drain.name, drain);
        spellbook.put(shield.name, shield);
        spellbook.put(poison.name, poison);
        spellbook.put(recharge.name, recharge);
    }

    public static boolean isValid(State s, Spell spell) {
        if (s.pc.mana < spell.cost) {
            return false;
        }
        if (spell.e instanceof AddTimedEffect) {
            TimedEffect te = ((AddTimedEffect) spell.e).e;
            for (TimedEffect activeEffect : s.activeEffects) {
                if (activeEffect.name.equals(te.name) && activeEffect.duration > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void tick(State s) {
        tick(s, null);
    }

    public static void tick(State s, Spell spell) {
        s.pc.arm = 0;

        Iterator<TimedEffect> it = s.activeEffects.iterator();
        while (it.hasNext()) {
            TimedEffect e = it.next();
            e.effect(s);
            if (e.duration <= 0) {
                it.remove();
            }
        }

        if (s.turn == 0) {
            s.manaSpent += spell.cost;
            s.pc.mana -= spell.cost;
            s.cast.add(spell.name);
            spell.e.effect(s);
        } else {
            s.boss.attack(s.pc);
        }
        s.turn = (s.turn + 1) % 2;
    }

    public static Unit parseInput(String inFile) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inFile));
        int hp = Integer.parseInt(sc.findInLine("\\d+"));
        sc.nextLine();
        int dmg = Integer.parseInt(sc.findInLine("\\d+"));
        sc.nextLine();
        return new Unit(hp, dmg, 0, 0);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Unit boss = parseInput("./etc/aoc2015/in22");
        Unit pc = new Unit(50, 0, 0, 500);
        sim(pc, boss, false);
        sim(pc, boss, true);
    }

    public static void sim(Unit pc, Unit boss, boolean isHard) {
        Queue<State> q = new PriorityQueue<>(Comparator.comparingInt(o -> o.manaSpent));
        q.offer(new State(pc, boss));

        while (!q.isEmpty()) {
            State s = q.poll();
            if (s.turn == 1) {
                tick(s);
                if (!s.pc.isDead()) {
                    q.offer(s);
                }
            } else {
                if (isHard) {
                    s.pc.hp -= 1;
                    if (s.pc.isDead()) {
                        continue;
                    }
                }
                for (Spell spell : spellbook.values()) {
                    if (isValid(s, spell)) {
                        State newState = new State(s);
                        tick(newState, spell);
                        q.offer(newState);
                    }
                }
            }
            if (s.boss.isDead()) {
                System.out.println(s.manaSpent);
                System.out.println(s.cast);
                break;
            }
        }
    }
}