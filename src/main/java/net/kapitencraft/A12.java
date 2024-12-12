package net.kapitencraft;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class A12 {

    public static void main(String[] args) throws IOException {
        File file = new File("assets/input11.txt");
        char[][] lines = new BufferedReader(new FileReader(file)).lines().map(String::toCharArray).toArray(char[][]::new);
        Multimap<Character, Region> regions = HashMultimap.create();
        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines.length; x++) {
                int finalX = x;
                int finalY = y;
                if (regions.values().stream().noneMatch(r -> r.contains(finalX, finalY))) {
                    char c = lines[y][x];
                    regions.put(c, createRegion(lines, x, y, c));
                }
            }
        }
    }

    private static Region createRegion(char[][] field, int x, int y, char regionMarker) {

    }

    private static class Region {
        private final Multimap<Integer, Integer> plots = HashMultimap.create();

        public boolean contains(int x, int y) {
            return plots.containsEntry(x, y);
        }

        public void add(int x, int y) {
            plots.put(x, y);
        }
    }
}