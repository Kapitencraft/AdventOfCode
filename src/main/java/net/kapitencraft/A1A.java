package net.kapitencraft;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class A1A {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("assets/input1.txt");

        Stream<String> content = new BufferedReader(new FileReader(file)).lines();
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        content.map(s -> s.split(" {3}"))
                .forEach(strings -> {
                    list1.add(Integer.valueOf(strings[0]));
                    list2.add(Integer.valueOf(strings[1]));
                });
        list1.sort(Integer::compareTo);
        list2.sort(Integer::compareTo);
        Map<Integer, Integer> lookup = new HashMap<>();
        int total = 0;
        for (int l1 : list1) {
            if (!lookup.containsKey(l1)) {
                int valTotal = 0;
                for (int l2 : list2) {
                    if (l2 > l1) break;
                    if (l2 == l1) valTotal++;
                }
                lookup.put(l1, valTotal * l1);
            }
            total += lookup.get(l1);
        }
        System.out.println(total);
    }
}