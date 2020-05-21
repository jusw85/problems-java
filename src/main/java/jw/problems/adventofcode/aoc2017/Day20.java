package jw.problems.adventofcode.aoc2017;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http://adventofcode.com/2017/day/20
 *
 * --- Day 20: Particle Swarm ---
 *
 * Suddenly, the GPU contacts you, asking for help. Someone has asked it to simulate too many particles, and it won't be able to finish them all in time to render the next frame at this rate.
 *
 * It transmits to you a buffer (your puzzle input) listing each particle in order (starting with particle 0, then particle 1, particle 2, and so on). For each particle, it provides the X, Y, and Z coordinates for the particle's position (p), velocity (v), and acceleration (a), each in the format <X,Y,Z>.
 *
 * Each tick, all particles are updated simultaneously. A particle's properties are updated in the following order:
 *
 * Increase the X velocity by the X acceleration.
 * Increase the Y velocity by the Y acceleration.
 * Increase the Z velocity by the Z acceleration.
 * Increase the X position by the X velocity.
 * Increase the Y position by the Y velocity.
 * Increase the Z position by the Z velocity.
 *
 * Because of seemingly tenuous rationale involving z-buffering, the GPU would like to know which particle will stay closest to position <0,0,0> in the long term. Measure this using the Manhattan distance, which in this situation is simply the sum of the absolute values of a particle's X, Y, and Z position.
 *
 * For example, suppose you are only given two particles, both of which stay entirely on the X-axis (for simplicity). Drawing the current states of particles 0 and 1 (in that order) with an adjacent a number line and diagram of current X positions (marked in parenthesis), the following would take place:
 *
 * p=< 3,0,0>, v=< 2,0,0>, a=<-1,0,0>    -4 -3 -2 -1  0  1  2  3  4
 * p=< 4,0,0>, v=< 0,0,0>, a=<-2,0,0>                         (0)(1)
 *
 * p=< 4,0,0>, v=< 1,0,0>, a=<-1,0,0>    -4 -3 -2 -1  0  1  2  3  4
 * p=< 2,0,0>, v=<-2,0,0>, a=<-2,0,0>                      (1)   (0)
 *
 * p=< 4,0,0>, v=< 0,0,0>, a=<-1,0,0>    -4 -3 -2 -1  0  1  2  3  4
 * p=<-2,0,0>, v=<-4,0,0>, a=<-2,0,0>          (1)               (0)
 *
 * p=< 3,0,0>, v=<-1,0,0>, a=<-1,0,0>    -4 -3 -2 -1  0  1  2  3  4
 * p=<-8,0,0>, v=<-6,0,0>, a=<-2,0,0>                         (0)
 *
 * At this point, particle 1 will never be closer to <0,0,0> than particle 0, and so, in the long run, particle 0 will stay closest.
 *
 * Which particle will stay closest to position <0,0,0> in the long term?
 *
 * Your puzzle answer was 258.
 * --- Part Two ---
 *
 * To simplify the problem further, the GPU would like to remove any particles that collide. Particles collide if their positions ever exactly match. Because particles are updated simultaneously, more than two particles can collide at the same time and place. Once particles collide, they are removed and cannot collide with anything else after that tick.
 *
 * For example:
 *
 * p=<-6,0,0>, v=< 3,0,0>, a=< 0,0,0>
 * p=<-4,0,0>, v=< 2,0,0>, a=< 0,0,0>    -6 -5 -4 -3 -2 -1  0  1  2  3
 * p=<-2,0,0>, v=< 1,0,0>, a=< 0,0,0>    (0)   (1)   (2)            (3)
 * p=< 3,0,0>, v=<-1,0,0>, a=< 0,0,0>
 *
 * p=<-3,0,0>, v=< 3,0,0>, a=< 0,0,0>
 * p=<-2,0,0>, v=< 2,0,0>, a=< 0,0,0>    -6 -5 -4 -3 -2 -1  0  1  2  3
 * p=<-1,0,0>, v=< 1,0,0>, a=< 0,0,0>             (0)(1)(2)      (3)
 * p=< 2,0,0>, v=<-1,0,0>, a=< 0,0,0>
 *
 * p=< 0,0,0>, v=< 3,0,0>, a=< 0,0,0>
 * p=< 0,0,0>, v=< 2,0,0>, a=< 0,0,0>    -6 -5 -4 -3 -2 -1  0  1  2  3
 * p=< 0,0,0>, v=< 1,0,0>, a=< 0,0,0>                       X (3)
 * p=< 1,0,0>, v=<-1,0,0>, a=< 0,0,0>
 *
 * ------destroyed by collision------
 * ------destroyed by collision------    -6 -5 -4 -3 -2 -1  0  1  2  3
 * ------destroyed by collision------                      (3)
 * p=< 0,0,0>, v=<-1,0,0>, a=< 0,0,0>
 *
 * In this example, particles 0, 1, and 2 are simultaneously destroyed at the time and place marked X. On the next tick, particle 3 passes through unharmed.
 *
 * How many particles are left after all collisions are resolved?
 *
 * Your puzzle answer was 707.
 */
public class Day20 {

    public static Pattern p1 = Pattern.compile("p=<([^>]+)>, v=<([^>]+)>, a=<([^>]+)>");
    public static Pattern p2 = Pattern.compile("(-?\\d+),(-?\\d+),(-?\\d+)");

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./etc/aoc2017/in20"));
        List<Particle> parts = new ArrayList<>();
        int id = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = p1.matcher(line);
            m.matches();
            Vec3 p = parseVec3(m.group(1));
            Vec3 v = parseVec3(m.group(2));
            Vec3 a = parseVec3(m.group(3));
            Particle part = new Particle(id++, p, v, a);
            parts.add(part);
        }
        sc.close();

        for (int i = 0; i < 100; i++) {
            simulate(parts);
            removeCollision(parts);
        }
        System.out.println(parts.size());
    }

    public static void simulate(List<Particle> parts) {
        for (Particle part : parts) {
            part.tick();
        }
    }

    public static void removeCollision(List<Particle> parts) {
        Set<Particle> hs = new HashSet<>();
        List<Particle> toRemove = new ArrayList<>();
        for (Particle part : parts) {
            if (!hs.contains(part)) {
                hs.add(part);
            } else {
                toRemove.add(part);
            }
        }
        for (Particle part : toRemove) {
            hs.remove(part);
        }
        parts.clear();
        parts.addAll(hs);
    }

    public static void part1(List<Particle> parts) {
        long min = Long.MAX_VALUE;
        for (Particle part : parts) {
            if (part.a.magnitude() <= min) {
                min = part.a.magnitude();
                System.out.println(part);
            }
        }
    }

    public static Vec3 parseVec3(String vec3) {
        Matcher m = p2.matcher(vec3);
        m.matches();
        long lx = Long.parseLong(m.group(1));
        long ly = Long.parseLong(m.group(2));
        long lz = Long.parseLong(m.group(3));
        return new Vec3(lx, ly, lz);
    }

    public static class Particle {
        public int id;
        public Vec3 p;
        public Vec3 v;
        public Vec3 a;

        public Particle(int id, Vec3 p, Vec3 v, Vec3 a) {
            this.id = id;
            this.p = p;
            this.v = v;
            this.a = a;
        }

        public void tick() {
            v.x += a.x;
            v.y += a.y;
            v.z += a.z;
            p.x += v.x;
            p.y += v.y;
            p.z += v.z;
        }

        public boolean equals(Object o) {
            Particle particle = (Particle) o;
            return particle.p.x == p.x && particle.p.y == p.y && particle.p.z == p.z;
        }

        public int hashCode() {
            return Objects.hash(p.x, p.y, p.z);
        }

        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("id=").append(id);
            sb.append(", p=").append(p);
            sb.append(", v=").append(v);
            sb.append(", a=").append(a);
            return sb.toString();
        }
    }

    public static class Vec3 {
        public long x;
        public long y;
        public long z;

        public Vec3(long x, long y, long z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public long magnitude() {
            return Math.abs(x) + Math.abs(y) + Math.abs(z);
        }

        public String toString() {
            final StringBuilder sb = new StringBuilder("<");
            sb.append(x);
            sb.append(",").append(y);
            sb.append(",").append(z);
            sb.append('>');
            return sb.toString();
        }
    }

}