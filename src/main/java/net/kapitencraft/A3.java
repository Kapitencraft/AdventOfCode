package net.kapitencraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class A3 {
    private static final Pattern OPERATION_PATTERN = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
    private static final Pattern DONT_PATTERN = Pattern.compile("don't\\(\\)");
    private static final Pattern DO_PATTERN = Pattern.compile("do\\(\\)");

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("assets/input3.txt");
        String content = new BufferedReader(new FileReader(file)).lines().collect(Collectors.joining("\n"));
        Matcher operation = OPERATION_PATTERN.matcher(content);
        Matcher dont = DONT_PATTERN.matcher(content);
        Matcher doMatcher = DO_PATTERN.matcher(content);
        int current = 0;
        for (int index = 0; operation.find(index);) {
            if (dont.find(index) && dont.start() < operation.start()) {
                if (doMatcher.find(dont.end())) {
                    index = doMatcher.end();
                } else {
                    break;
                }
            } else {
                current += Integer.parseInt(operation.group(1)) * Integer.parseInt(operation.group(2));
                index = operation.end();
            }
        }
        System.out.println(current);
    }
}