package net.kapitencraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class A2 {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("assets/input2.txt");

        Stream<String> content = new BufferedReader(new FileReader(file)).lines();
        int total = content.map(s -> s.split(" "))
                .map(Arrays::stream)
                .map(stringStream -> stringStream.map(Integer::valueOf).toList())
                .filter(A2::checkPossible).toList().size();
        System.out.println(total);
    }

    private static boolean checkPossible(List<Integer> list) {
        System.out.println("Checking " + listDebug(list));
            return print(checkPossible(list, -1));
    }

    private static String listDebug(List<Integer> list) {
        return list.stream().map(Object::toString).collect(Collectors.joining(" ", "'", "'"));
    }

    private static boolean print(boolean in) {
        System.out.println("Result: " + in);
        return in;
    }

    private static boolean checkPossible(List<Integer> list, int skip) {
        Integer last = null;
        Boolean up = null;
        for (int i = 0; i < list.size(); i++) {
            if (i == skip) continue;
            int j = list.get(i);
            if (last != null) {
                if (up == null) {
                    if (last == j || Math.abs(last - j) > 3) {
                        if (skip == -1) {
                            for (int i1 = 0; i1 < list.size(); i1++) {
                                System.out.println("\tA-Checking " + listDebug(list) + ", skipping " + i1);
                                if (checkPossible(list, i1)) {
                                    System.out.println("\tSuccess!");
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                    up = last < j;
                } else if ((up ? last > j : last < j) || Math.abs(last - j) > 3 || last == j) {
                    if (i == list.size() - 1 && skip == -1) {
                        System.out.println("\tIgnoring error on last test");
                        System.out.println("\tSuccess!");
                        return true;
                    }
                    if (skip == -1) {
                        System.out.println("\tError on index " + i);
                        for (int i1 = 0; i1 < list.size(); i1++) {
                            System.out.println("\tB-Checking " + listDebug(list) + ", skipping " + i1);
                            if (checkPossible(list, i1)) {
                                System.out.println("\tSuccess!");
                                return true;
                            }
                        }
                    }
                    return false;
                }
            }
            last = j;
        }
        return true;
    }
}