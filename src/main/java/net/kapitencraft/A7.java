package net.kapitencraft;

import java.io.*;
import java.util.Arrays;

public class A7 {

    public static void main(String[] args) throws IOException {
        File file = new File("assets/input7.txt");
        String[] lines = new BufferedReader(new FileReader(file)).lines().toArray(String[]::new);
        long count = 0;
        for (String line : lines) {
            int colonIndex = line.indexOf(':');
            long eval = Long.parseLong(line.substring(0, colonIndex));
            Long[] params = Arrays.stream(line.substring(colonIndex + 2).split(" ")).map(Long::parseLong).toArray(Long[]::new);
            if (check(params[0], 1, params, eval)) count+=eval;
        }
        System.out.println(count);
    }

    private static boolean check(long in, int index, Long[] args, long expected) {
        if (index < args.length-1) {
            return check(in * args[index], index+1, args, expected) ||
                    check(in + args[index], index+1, args, expected) ||
                    check(chain(in, args[index]), index+1, args, expected);
        } else
            return in * args[index] == expected || in + args[index] == expected || chain(in, args[index]) == expected;
    }

    //part 2 yay
    private static long chain(long a, long b) {
        return Long.parseLong(a + String.valueOf(b));
    }
}