package jw.problems.aoc2015;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * http://adventofcode.com/2015/day/21
 *
 * --- Day 21: RPG Simulator 20XX ---
 *
 * Little Henry Case got a new video game for Christmas. It's an RPG, and he's stuck on a boss. He needs to know what equipment to buy at the shop. He hands you the controller.
 *
 * In this game, the player (you) and the enemy (the boss) take turns attacking. The player always goes first. Each attack reduces the opponent's hit points by at least 1. The first character at or below 0 hit points loses.
 *
 * Damage dealt by an attacker each turn is equal to the attacker's damage score minus the defender's armor score. An attacker always does at least 1 damage. So, if the attacker has a damage score of 8, and the defender has an armor score of 3, the defender loses 5 hit points. If the defender had an armor score of 300, the defender would still lose 1 hit point.
 *
 * Your damage score and armor score both start at zero. They can be increased by buying items in exchange for gold. You start with no items and have as much gold as you need. Your total damage or armor is equal to the sum of those stats from all of your items. You have 100 hit points.
 *
 * Here is what the item shop is selling:
 *
 * Weapons:    Cost  Damage  Armor
 * Dagger        8     4       0
 * Shortsword   10     5       0
 * Warhammer    25     6       0
 * Longsword    40     7       0
 * Greataxe     74     8       0
 *
 * Armor:      Cost  Damage  Armor
 * Leather      13     0       1
 * Chainmail    31     0       2
 * Splintmail   53     0       3
 * Bandedmail   75     0       4
 * Platemail   102     0       5
 *
 * Rings:      Cost  Damage  Armor
 * Damage +1    25     1       0
 * Damage +2    50     2       0
 * Damage +3   100     3       0
 * Defense +1   20     0       1
 * Defense +2   40     0       2
 * Defense +3   80     0       3
 *
 * You must buy exactly one weapon; no dual-wielding. Armor is optional, but you can't use more than one. You can buy 0-2 rings (at most one for each hand). You must use any items you buy. The shop only has one of each item, so you can't buy, for example, two rings of Damage +3.
 *
 * For example, suppose you have 8 hit points, 5 damage, and 5 armor, and that the boss has 12 hit points, 7 damage, and 2 armor:
 *
 * The player deals 5-2 = 3 damage; the boss goes down to 9 hit points.
 * The boss deals 7-5 = 2 damage; the player goes down to 6 hit points.
 * The player deals 5-2 = 3 damage; the boss goes down to 6 hit points.
 * The boss deals 7-5 = 2 damage; the player goes down to 4 hit points.
 * The player deals 5-2 = 3 damage; the boss goes down to 3 hit points.
 * The boss deals 7-5 = 2 damage; the player goes down to 2 hit points.
 * The player deals 5-2 = 3 damage; the boss goes down to 0 hit points.
 *
 * In this scenario, the player wins! (Barely.)
 *
 * You have 100 hit points. The boss's actual stats are in your puzzle input. What is the least amount of gold you can spend and still win the fight?
 *
 * Your puzzle answer was 121.
 * --- Part Two ---
 *
 * Turns out the shopkeeper is working with the boss, and can persuade you to buy whatever items he wants. The other rules still apply, and he still only has one of each item.
 *
 * What is the most amount of gold you can spend and still lose the fight?
 *
 * Your puzzle answer was 201.
 */
public class Day21 {

    public static class Unit {
        public int hp;
        public int dmg;
        public int arm;

        public Unit(int hp, int dmg, int arm) {
            this.hp = hp;
            this.dmg = dmg;
            this.arm = arm;
        }

        public Unit(Unit o) {
            this.hp = o.hp;
            this.dmg = o.dmg;
            this.arm = o.arm;
        }

        public void attack(Unit o) {
            o.hp -= Math.max((dmg - o.arm), 1);
        }

        public boolean isDead() {
            return hp <= 0;
        }

        public void wield(Item i) {
            dmg += i.dmg;
            arm += i.arm;
        }

        public void unwield(Item i) {
            dmg -= i.dmg;
            arm -= i.arm;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Unit{");
            sb.append("hp=").append(hp);
            sb.append(", dmg=").append(dmg);
            sb.append(", arm=").append(arm);
            sb.append('}');
            return sb.toString();
        }
    }

    public static class Item {
        public int cost;
        public int dmg;
        public int arm;

        public Item(int cost, int dmg, int arm) {
            this.cost = cost;
            this.dmg = dmg;
            this.arm = arm;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Item{");
            sb.append("cost=").append(cost);
            sb.append(", dmg=").append(dmg);
            sb.append(", arm=").append(arm);
            sb.append('}');
            return sb.toString();
        }
    }

    public static class Shop {
        public List<Item> weaps;
        public List<Item> arms;
        public List<Item> rings;

        public Shop(List<Item> weaps, List<Item> arms, List<Item> rings) {
            this.weaps = weaps;
            this.arms = arms;
            this.rings = rings;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Shop{");
            sb.append("weaps=").append(weaps);
            sb.append(", arms=").append(arms);
            sb.append(", rings=").append(rings);
            sb.append('}');
            return sb.toString();
        }
    }

    public static Unit parseInput(String inFile) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inFile));
        int hp = Integer.parseInt(sc.findInLine("\\d+"));
        sc.nextLine();
        int dmg = Integer.parseInt(sc.findInLine("\\d+"));
        sc.nextLine();
        int arm = Integer.parseInt(sc.findInLine("\\d+"));
        sc.close();
        return new Unit(hp, dmg, arm);
    }

    private static Shop parseInputShop(String inFile) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(inFile));
        List<Item> weaps = parseItems(sc, 5);
        List<Item> arms = parseItems(sc, 6);
        List<Item> rings = parseItems(sc, 7);
        sc.close();
        return new Shop(weaps, arms, rings);
    }

    private static List<Item> parseItems(Scanner sc, int num) {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            int cost = Integer.parseInt(sc.findInLine("\\d+"));
            int dmg = Integer.parseInt(sc.findInLine("\\d+"));
            int arm = Integer.parseInt(sc.findInLine("\\d+"));
            sc.nextLine();
            items.add(new Item(cost, dmg, arm));
        }
        return items;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Unit boss = parseInput("./etc/aoc2015/in21");
        Shop shop = parseInputShop("./etc/aoc2015/in21a");
        Unit pc = new Unit(100, 0, 0);
        solve(pc, boss, shop);
    }

    public static void solve(Unit pc, Unit boss, Shop shop) {
        int minCost = Integer.MAX_VALUE;
        int maxCost = Integer.MIN_VALUE;
        for (Item weap : shop.weaps) {
            pc.wield(weap);
            for (Item arm : shop.arms) {
                pc.wield(arm);
                for (int i = 0; i < shop.rings.size() - 1; i++) {
                    for (int j = i + 1; j < shop.rings.size(); j++) {
                        Item r1 = shop.rings.get(i);
                        Item r2 = shop.rings.get(j);
                        pc.wield(r1);
                        pc.wield(r2);
                        int totalCost = weap.cost + arm.cost + r1.cost + r2.cost;
                        if (doesPlayerWin(pc, boss) && (totalCost < minCost)) {
                            minCost = totalCost;
                        }
                        if (!doesPlayerWin(pc, boss) && (totalCost > maxCost)) {
                            maxCost = totalCost;
                        }
                        pc.unwield(r2);
                        pc.unwield(r1);
                    }
                }
                pc.unwield(arm);
            }
            pc.unwield(weap);
        }
        System.out.println(minCost);
        System.out.println(maxCost);
    }

    public static boolean doesPlayerWin(Unit pc, Unit boss) {
        Unit a = new Unit(pc);
        Unit b = new Unit(boss);
        while (true) {
            a.attack(b);
            if (a.isDead() || b.isDead()) {
                break;
            }
            b.attack(a);
            if (a.isDead() || b.isDead()) {
                break;
            }
        }
        return !a.isDead();
    }
}