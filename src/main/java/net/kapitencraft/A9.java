package net.kapitencraft;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class A9 {

    public static void main(String[] args) throws IOException {
        File file = new File("assets/input9.txt");
        char[][] lines = new BufferedReader(new FileReader(file)).lines().map(String::toCharArray).toArray(char[][]::new);
        char[] line = lines[0];
        List<Integer> space = new ArrayList<>();
        boolean empty = false;
        int index = 0;
        for (char c : line) {
            int i = c - 48; //ASCII number start is 48 = 0
            if (empty) {
                for (int i1 = 0; i1 < i; i1++) space.add(-1);
            } else {
                for (int i1 = 0; i1 < i; i1++) space.add(index);
                index++;
            }
            empty = !empty;
        }
        Integer[] data = space.toArray(Integer[]::new);
        int leftIndex = 0, rightIndex = data.length - 1;
        while (data[leftIndex] != -1) leftIndex++;
        while (data[rightIndex] == -1) rightIndex--;
        while (leftIndex + 2 < rightIndex) {
            data[leftIndex] = data[rightIndex];
            data[rightIndex] = -1;

            while (data[leftIndex] != -1) leftIndex++;
            while (data[rightIndex] == -1) rightIndex--;
        }
        File out = new File("assets/input9o.txt");
        FileWriter writer = new FileWriter(out);

        for (Integer i : data) {
            writer.write(String.valueOf(i));
            writer.write(",");
        }
        writer.close();
        long result = 0;
        for (int i = 0; i < data.length; i++) {
            int i1 = data[i];
            if (i1 == -1) continue;
            result += (long) i * i1;
        }
        System.out.println(result);
    }
}