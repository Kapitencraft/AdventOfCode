package net.kapitencraft;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class A11 {
    static HashMap<String, Long> convertCache = new HashMap<>();
    static HashMap<Long, Long> mulCache = new HashMap<>();

    public static void main(String[] args) throws IOException {
        File file = new File("assets/input11.txt");
        String[] lines = new BufferedReader(new FileReader(file)).lines().toArray(String[]::new);
        List<Long> stoneIds = Arrays.stream(lines[0].split(" ")).map(Long::valueOf).collect(Collectors.toList());
        for (int cycle = 0; cycle < 75; cycle++) {
            System.out.print("\rProgress: " + cycle + ", Size: " + stoneIds.size());
            for (int i = 0; i < stoneIds.size(); i++) {
                long l = stoneIds.get(i);
                if (l == 0) {
                    stoneIds.set(i, 1L);
                    continue;
                }
                String s = String.valueOf(l);
                if (s.length() % 2 == 0) {
                    int halfLength = s.length() / 2;
                    stoneIds.set(i, convert(s.substring(0, halfLength)));
                    stoneIds.add(i+1, convert(s.substring(halfLength)));
                    i++;
                } else stoneIds.set(i, multiply(l));
            }
        }
        System.out.println(stoneIds.size());
    }

    private static long multiply(long in) {
        if (!mulCache.containsKey(in)) mulCache.put(in, in*2024);
        return mulCache.get(in);
    }

    private static long convert(String in) {
        if (!convertCache.containsKey(in)) convertCache.put(in, Long.valueOf(in));
        return convertCache.get(in);
    }
}