package net.kapitencraft;

import javax.print.attribute.standard.MediaSize;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class A4 {
    private static final char[] OTHER = {'M', 'A', 'S'};


    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("assets/input4.txt");
        char[][] lines = new BufferedReader(new FileReader(file)).lines().map(String::toCharArray).toArray(char[][]::new);
        int amount = 0;
        for (int l = 0; l < lines.length; l++) { // l = y
            char[] line = lines[l];
            for (int cI = 0; cI < 140; cI++) { // cI = x
                char c = line[cI];
                if (c == 'X') {
                    for (int x = -1; x <= 1; x++) {
                        a: for (int y = -1; y <= 1; y++) {
                            if (x < 0 && cI < 3 || x > 0 && cI >= 137) continue;
                            if (y < 0 && l < 3 || y > 0 && l >= 137) continue;
                            for (int i = 0; i < 3; i++) {
                                if (!(lines[l + y*(i+1)][cI + x*(i+1)] == OTHER[i])) continue a;
                            }
                            amount++;
                        }
                    }
                }
            }
        }
        System.out.println(amount);
    }
}