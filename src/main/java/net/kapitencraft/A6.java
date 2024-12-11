package net.kapitencraft;

import java.io.*;

public class A6 {

    public static void main(String[] args) throws IOException {
        File file = new File("assets/input6.txt");
        char[][] lines = new BufferedReader(new FileReader(file)).lines().map(String::toCharArray).toArray(char[][]::new);
        Vec2 delta = new Vec2(0, -1);
        Vec2 pos = new Vec2(85, 65);
        pos.set(lines, 'X');
        while (pos.inBounds()) {
            char peek = pos.add(delta).pick(lines);
            if (peek == '#') {
                delta = delta.rotate();
            } else {
                pos = pos.add(delta);
                pos.set(lines, 'X');
            }
        }

        int result = 0;
        FileWriter writer = new FileWriter("assets/input6o.txt");
        for (char[] line : lines) {
            writer.write(line);
            writer.write("\n");
            for (char c : line) if (c == 'X') result++;
        }
        System.out.println(result);
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
            return y > 0 && y < 130 && x > 0 && x < 130;
        }

        public char pick(char[][] field) {
            return field[y][x];
        }

        public void set(char[][] field, char target) {
            field[y][x] = target;
        }
    }
}