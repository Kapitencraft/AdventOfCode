package net.kapitencraft;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

public class A1A {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("assets/input1.txt");

        Stream<String> content = new BufferedReader(new FileReader(file)).lines();
        List<String[]> split = content.map(s -> s.split(" {3}")).toList();
        Stream<Integer> first = split.stream().map(a -> a[0]).map(Integer::parseInt);
        List<Integer> second = split.stream().map(a -> a[1]).map(Integer::parseInt).toList();
        Stream<Integer> values = first.map(i -> second.stream().filter(i::equals).toList().size() * i);
        int total = values.reduce(0, Integer::sum);
        System.out.println(total);
    }
}