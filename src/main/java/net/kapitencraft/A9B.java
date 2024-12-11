package net.kapitencraft;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class A9B {

    public static void main(String[] args) throws IOException {
        File file = new File("assets/input9.txt");
        char[][] lines = new BufferedReader(new FileReader(file)).lines().map(String::toCharArray).toArray(char[][]::new);
        char[] line = lines[0];
        List<Integer> space = new ArrayList<>();
        boolean empty = false;
        int index = 0;
        int latest = 9999;
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
        int rightIndex = data.length - 1;
        while (data[rightIndex] == -1) rightIndex--;
        while (rightIndex > 0) {
            int leftIndex = 0;
            while (data[leftIndex] != -1) leftIndex++;
            int emptyIndex = -1;
            int fLength = 0;
            int fId = data[rightIndex];
            if (fId < latest) {
                while (data[rightIndex] == fId) {
                    rightIndex--;
                    fLength++;
                    if (rightIndex == 0) break; //done yay
                }
                System.out.println(fId + "*" + fLength);
                while (leftIndex < rightIndex) {
                    int startIndex = leftIndex;
                    int eLength = 0;
                    while (data[leftIndex] == -1) {
                        leftIndex++;
                        eLength++;
                    }
                    System.out.println(startIndex + "-" + (leftIndex - 1) + "  " + eLength);
                    if (eLength >= fLength) {
                        emptyIndex = startIndex;
                        break;
                    } else {
                        while (leftIndex < rightIndex && data[leftIndex] != -1) {
                            leftIndex++;
                        }
                    }
                }
                latest = fId;
            }
            if (emptyIndex == -1) {
                while (rightIndex > 0 && data[rightIndex] == fId) rightIndex--;
            } else {
                System.arraycopy(data, rightIndex + 1, data, emptyIndex, fLength);
                Arrays.fill(data, rightIndex + 1, rightIndex + 1 + fLength, -1);
            }
            while (rightIndex > 0 && data[rightIndex] == -1) rightIndex--;
        }
        File out = new File("assets/input9o.txt");
        FileWriter writer = new FileWriter(out);

        for (Integer i : data) {
            if (i == -1)
                writer.write(".");
            else
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