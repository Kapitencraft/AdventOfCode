package net.kapitencraft;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.*;

public class A6B2 {
    static char[][] field;

    public static void main(String[] args) throws IOException {
        File file = new File("assets/input6.txt");
        field = new BufferedReader(new FileReader(file)).lines().map(String::toCharArray).toArray(char[][]::new);
        Multimap<Integer, Integer> found = HashMultimap.create();
        Vec2 pos = null;
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[0].length; x++) {
                if (field[y][x] == '^') pos = new Vec2(x, y);
            }
        }
        if (pos == null) return;
        Vec2 delta = new Vec2(0, -1);
        pos.set('X');
        int i = 0;
        while (pos.inBounds()) {
            char peek = pos.add(delta).pick();
            if (peek == '#') {
                delta = delta.rotate();
                Vec2 clip = clip(pos, delta, null);
                Vec2 clip2 = clip(pos, delta.invert(), null);
                moveLines.put(delta, new Line(clip, clip2));
            } else {
                pos = pos.add(delta);
                if (check(pos, delta)) {
                    found.put(pos.x, pos.y);
                }
                pos.set('X');
                i++;
                System.out.print("\rProgress: " + i);
            }
        }

        FileWriter writer = new FileWriter("assets/input6o.txt");
        for (char[] line : field) {
            writer.write(line);
            writer.write("\n");
        }
        writer.close();

        System.out.println();
        System.out.print(found.size());
    }

    private static final Multimap<Vec2, Line> moveLines = HashMultimap.create();

    private static boolean check(Vec2 pos, Vec2 delta) {
        Vec2 origin = pos.add(delta);
        Multimap<Vec2, Line> shadowLines = HashMultimap.create();
        while (pos.inBounds()) {
            delta = delta.rotate();
            pos = clip(pos, delta, origin);
            if (hasLine(pos, delta.rotate(), shadowLines)) {
                origin.set('O');
                return true;
            }
            Vec2 lineOrigin = clip(pos, delta.invert(), origin);
            shadowLines.put(delta, new Line(pos, lineOrigin));
        }
        return false;
    }

    private static boolean hasLine(Vec2 pos, Vec2 rot, Multimap<Vec2, Line> internals) {
        for (Line line : moveLines.get(rot)) {
            if (line.inside(pos)) return true;
        }
        for (Line line : internals.get(rot)) {
            if (line.inside(pos)) return true;
        }
        return false;
    }

    private record Line(Vec2 p1, Vec2 p2) {

        boolean isVertical() {
            return p1.x == p2.x;
        }

        boolean isHorizontal() {
            return p1.y == p2.y;
        }

        boolean inside(Vec2 pos) {
            if (isVertical()) return pos.x == p1.x && (p1.y < p2.y ?
                    pos.y >= p1.y && pos.y <= p2.y :
                    pos.y >= p2.y && pos.y <= p2.x);
            else return pos.y == p1.y && (p1.x < p2.x ?
                    pos.x >= p1.x && pos.x <= p2.x :
                    pos.x >= p2.x && pos.x <= p2.x);
        }
    }


    private record Vec2(int x, int y) {

        public Vec2 add(int x, int y) {
            return new Vec2(this.x + x, this.y + y);
        }

        public Vec2 add(Vec2 other) {
            return add(other.x, other.y);
        }

        public Vec2 rotate() {
            int dx = x;
            int dy = y;
            if (dx != 0) {
                dy = dx;
                dx = 0;
            } else {
                dx = -dy;
                dy = 0;
            }
            return new Vec2(dx, dy);
        }


        public boolean inBounds() {
            return y > 0 && y < field.length-1 && x > 0 && x < field[0].length-1;
        }

        public char pick() {
            return field[y][x];
        }

        public void set(char target) {
            if (pick() == 'O') return;
            field[y][x] = target;
        }

        public Vec2 invert() {
            return new Vec2(-x, -y);
        }
    }

    private static Vec2 clip(Vec2 pos, Vec2 delta, Vec2 ghostObstacle) {
        while (pos.inBounds()) {
            Vec2 peekLoc = pos.add(delta);
            char peek =  peekLoc.pick();
            if (peek == '#' || peekLoc.equals(ghostObstacle)) {
                break; //reached blockage, clip complete
            } else {
                pos = pos.add(delta);
            }
        }
        return pos; //may move out of bounds
    }

}