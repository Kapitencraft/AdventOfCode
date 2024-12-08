package net.kapitencraft;

import java.io.*;

public class A6 {

    public static void main(String[] args) throws IOException {
        File file = new File("assets/input6.txt");
        char[][] lines = new BufferedReader(new FileReader(file)).lines().map(String::toCharArray).toArray(char[][]::new);
        int dx = 0, dy = -1;
        int y = 65, x = 85;
        lines[y][x] = 'X';
        while (y > 0 && y < 130 && x > 0 && x < 130) {
            char peek = lines[y + dy][x + dx];
            if (peek == '#') {
                if (dx != 0) {
                    dy = dx;
                    dx = 0;
                } else {
                    dx = -dy;
                    dy = 0;
                }
            } else {
                x += dx;
                y += dy;
                lines[y][x] = 'X';
            }
        }

        int result = 0;
        FileWriter writer = new FileWriter("assets/input6o.txt");
        for (char[] line : lines) {
            writer.write(line);
            writer.write("\n");
            for (char c : line) if (c == 'X') result++;
        }
        System.out.println(result);
    }
}