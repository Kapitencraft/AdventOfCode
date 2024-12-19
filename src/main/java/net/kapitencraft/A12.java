package net.kapitencraft;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class A12 {

    public static void main(String[] args) throws IOException {
        File file = new File("assets/input12d.txt");
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

        int total = 0;
        for (Region region : regions.values()) {
            int outline = region.getOutlineLength();
            int size = region.getTotalElements();
            total += outline * size;
        }
        System.out.println(total);
    }

    private static Region createRegion(char[][] field, int x, int y, char regionMarker) {
        Multimap<Integer, Integer> map = HashMultimap.create();
        addRegionElements(field, x, y, regionMarker, map);
        return new Region(map);
    }

    private static void addRegionElements(char[][] field, int x, int y, char regionMarker, Multimap<Integer, Integer> added) {
        added.put(x, y);
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dy != 0 ^ dx != 0) {
                    int nX = x + dx;
                    int nY = y + dy;
                    if (nX < 0 || nY < 0 || nX >= field[0].length || nY >= field.length) continue;
                    if (!added.containsEntry(nX, nY) && field[nY][nX] == regionMarker) {
                        addRegionElements(field, nX, nY, regionMarker, added);
                    }
                }
            }
        }

    }

    private static class Region {
        private final Multimap<Integer, Integer> plots;

        public Region(Multimap<Integer, Integer> map) {
            this.plots = map;
        }

        public boolean contains(int x, int y) {
            return plots.containsEntry(x, y);
        }

        public int getTotalElements() {
            return plots.size();
        }

        public int getOutlineLength() {
            Collection<Map.Entry<Integer, Integer>> entries = plots.entries();
            int surrounding = 0;
            for (Map.Entry<Integer, Integer> entry : entries) {
                int x = entry.getKey(), y = entry.getValue();
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        if (dy != 0 ^ dx != 0) {
                            int nX = x + dx;
                            int nY = y + dy;
                            if (!plots.containsEntry(nX, nY)) {
                                surrounding++;
                            }
                        }
                    }
                }
            }
            return surrounding;
        }
    }
}