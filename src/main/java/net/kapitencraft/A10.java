package net.kapitencraft;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class A10 {

    public static void main(String[] args) throws IOException {
        File file = new File("assets/input10.txt");
        char[][] lines = new BufferedReader(new FileReader(file)).lines().map(String::toCharArray).toArray(char[][]::new);
        int count = 0;
        for (int y = 0; y < lines.length; y++) {
            char[] line = lines[y];
            for (int x = 0; x < lines.length; x++) {
                char c = line[x];
                if (c == '0') {
                   count += findPaths(y, x, lines);
                }
            }
        }
        System.out.println(count);
    }

    private static int findPaths(int y, int x, char[][] field) {
        AtomicInteger integer = new AtomicInteger();
        getByNumber(y, x, field, 1, integer);
        return integer.get();
    }

    private static void getByNumber(int y, int x, char[][] field, int start, AtomicInteger integer) {
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dx == 0 ^ dy == 0) { //using xor (for once)
                    int nX = x + dx, nY = y + dy;
                    if (nX < 0 || nX > field[0].length-1 || nY < 0 || nY > field.length-1) continue;
                    char t = field[nY][nX];
                    if (t - 48 == start) {
                        if (start < 9) {
                            getByNumber(nY, nX, field, start + 1, integer);
                        } else integer.incrementAndGet();
                    }
                }
            }
        }
    }
}