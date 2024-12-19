package net.kapitencraft;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class A19 {

    public static void main(String[] args) throws IOException {
        File file = new File("assets/input12d.txt");
        char[][] lines = new BufferedReader(new FileReader(file)).lines().map(String::toCharArray).toArray(char[][]::new);

        String patternData = new String(lines[0]);
        String[] patterns = patternData.split(", ");
        List<Character> singletons = new ArrayList<>();
        Multimap<Character, PatternRef>
        List<char[]> otherPatterns = new ArrayList<>();
        for (String pattern : patterns) {
            if (pattern.length() == 1) singletons.add(pattern.charAt(0));
            else otherPatterns.add(pattern.toCharArray());
        }

        for (int i  = 2; i < lines.length; i++) {
            char[] design = lines[i];
            for (int cI = 0; cI < design.length; cI++) {
                char stripe = design[cI];
                if (singletons.contains(stripe)) continue;
            }
        }
    }

    private record PatternRef(int index, int patternId) {
    }
}