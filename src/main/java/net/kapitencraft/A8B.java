package net.kapitencraft;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class A8B {

    public static void main(String[] args) throws IOException {
        File file = new File("assets/input8.txt");
        char[][] lines = new BufferedReader(new FileReader(file)).lines().map(String::toCharArray).toArray(char[][]::new);
        Multimap<Character, Vec2> antennaPositions = HashMultimap.create();
        for (int y = 0; y < lines.length; y++) {
            char[] line = lines[y];
            for (int x = 0; x < line.length; x++) {
                char c = line[x];
                if (c != '.') antennaPositions.put(c, new Vec2(x, y));
            }
        }
        Multimap<Integer, Integer> antinodes = HashMultimap.create();
        for (char c : antennaPositions.keySet()) {
            List<Vec2> positions = antennaPositions.get(c).stream().toList();
            for (int i = 0; i < positions.size() - 1; i++) {
                for (int i1 = i+1; i1 < positions.size(); i1++) {
                    Vec2 a = positions.get(i);
                    Vec2 b = positions.get(i1);
                    Vec2 offset1 = a.subtract(b);
                    Vec2 offset2 = b.subtract(a);
                    Vec2 antinode1 = a;
                    Vec2 antinode2 = b;
                    while (antinode1.onField()) {
                        antinodes.put(antinode1.x, antinode1.y);
                        antinode1 = antinode1.add(offset1);
                    }
                    while (antinode2.onField()) {
                        antinodes.put(antinode2.x, antinode2.y);
                        antinode2 = antinode2.add(offset2);
                    }
                }
            }
        }
        System.out.println(antinodes.size());
    }

    private record Vec2(int x, int y) {

        Vec2 subtract(Vec2 other) {
            return new Vec2(x - other.x, y - other.y);
        }

        Vec2 add(Vec2 other) {
            return new Vec2(x + other.x, y + other.y);
        }

        boolean onField() {
            return this.x >= 0 && this.x < 50 && this.y >= 0 && this.y < 50;
        }
    }
}