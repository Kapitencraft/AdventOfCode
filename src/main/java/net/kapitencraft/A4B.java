package net.kapitencraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class A4B {

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("assets/input4.txt");
        char[][] lines = new BufferedReader(new FileReader(file)).lines().map(String::toCharArray).toArray(char[][]::new);
        int amount = 0;
        for (int l = 1; l < lines.length; l++) { // l = y
            char[] line = lines[l];
            for (int cI = 1; cI < 139; cI++) { // cI = x
                char c = line[cI];
                if (c == 'A') {
                    if (l == line.length - 1) continue;
                    char c1 = lines[l+1][cI+1];
                    char c2 = lines[l-1][cI-1];
                    if (!(c1 == 'S' && c2 == 'M' || c1 == 'M' && c2 == 'S')) continue;
                    char c3 = lines[l+1][cI-1];
                    char c4 = lines[l-1][cI+1];
                    if (!(c3 == 'S' && c4 == 'M' || c3 == 'M' && c4 == 'S')) continue;
                    amount++;
                }
            }
        }
        System.out.println(amount);
    }
}