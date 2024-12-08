package net.kapitencraft;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import javax.sound.sampled.Line;
import java.io.*;
import java.util.*;

public class A6B {
    private static char[][] lines;
    private static final List<WorkerThread> workers = new ArrayList<>();
    private static final Queue<ExeOrigin> executors = new ArrayDeque<>();
    private static final Multimap<Vec2, Line> moveLines = HashMultimap.create();
    private static final Multimap<Integer, Integer> matches = HashMultimap.create();
    private static Vec2 origin;

    public static void main(String[] args) throws IOException, InterruptedException {
        File file = new File("assets/input6.txt");

        lines = new BufferedReader(new FileReader(file)).lines().map(String::toCharArray).toArray(char[][]::new);

        Vec2 pos = null;
        a: for (int x = 0; x < lines[0].length; x++) {
            for (int y = 0; y < lines.length; y++) {
                if (lines[y][x] == '^') {
                    pos = new Vec2(x, y-1);
                    break a;
                }
            }
        }
        if (pos == null) return;

        Vec2 delta = new Vec2(0, -1);
        origin = new Vec2(pos.x, pos.y+1);
        int index = 0;
        do {
            Vec2 destination = clip(pos, delta, null);
            Vec2 vecRoot = clip(pos, delta.invert(), null);
            moveLines.put(delta.copy(), new Line(destination, vecRoot, index + 1 + pos.dist(destination)));
            do {
                check(delta, pos, index++);
                pos.move(delta);
            } while (!pos.equals(destination));
            delta.rotate();
        } while (pos.inBounds());
        //for (ExeOrigin origin : executors) {
        //    if (lines[origin.pos.y][origin.pos.x] != 'X') break;
        //    char newC;
        //    if (origin.delta.y > 0) newC = '|';
        //    else if (origin.delta.y < 0) newC = '^';
        //    else if (origin.delta.x > 0) newC = '>';
        //    else newC = '<';
        //    lines[origin.pos.y][origin.pos.x] = newC;
        //}
        startQueueWorker();
        while (!workers.isEmpty()) {
            Thread.sleep(500);
        }
        System.out.println();
        FileWriter writer = new FileWriter("assets/input6o.txt");
        for (char[] line : lines) {
            writer.write(line);
            writer.write("\n");
        }
        writer.close();
        System.out.println(matches.size());
    }

    private static Vec2 clip(Vec2 pos, Vec2 delta, Vec2 ghostObstacle) {
        pos = pos.copy();
        while (pos.inBounds()) {
            Vec2 peekLoc = new Vec2(pos.x + delta.x, pos.y + delta.y);
            char peek = lines[peekLoc.y][peekLoc.x];
            if (peek == '#' || peekLoc.equals(ghostObstacle)) {
                break; //reached blockage, clip complete
            } else {
                pos.move(delta);
            }
        }
        return pos; //may move out of bounds
    }

    private static void check(Vec2 currentDelta, Vec2 currentPos, int index) {
        Vec2 temp = currentPos.copy();
        temp.move(currentDelta);
        char newC;
        if (currentDelta.y > 0) newC = '|';
        else if (currentDelta.y < 0) newC = '^';
        else if (currentDelta.x > 0) newC = '>';
        else newC = '<';
        lines[currentPos.y][currentPos.x] = newC;
        if (temp.equals(origin)) return;
        executors.add(new ExeOrigin(currentPos.copy(), currentDelta.copy(), index));
    }

    private static boolean hasLine(Vec2 pos, Vec2 rot, int index, Multimap<Vec2, Line> internals) {
        for (Line line : moveLines.get(rot)) {
            if (line.inside(pos) && line.index < index) return true;
        }
        for (Line line : internals.get(rot)) {
            if (line.inside(pos) && line.index < index) return true;
        }
        return false;
    }

    private static void startQueueWorker() {
        ThreadGroup group = new ThreadGroup("Loop Checkers");
        for (int i = 0; i < 8; i++) {
            WorkerThread thread = new WorkerThread(group, i);
            thread.start();
            workers.add(thread);
        }
    }

    private static class WorkerThread extends Thread {
        private Vec2 current;

        public WorkerThread(ThreadGroup group, int id) {
            super(group, "Worker #" + id);
        }

        @Override
        public void run() {
            while (!executors.isEmpty()) {
                ExeOrigin origin = pollElement();
                if (origin == null) continue;
                int startIndex = origin.index;
                Vec2 clipResult = origin.pos.copy();
                Vec2 delta = origin.delta.copy();
                Multimap<Vec2, A6B.Line> internalLines = HashMultimap.create();
                Vec2 ghostObstacle = new Vec2(origin.pos.x + origin.delta.x, origin.pos.y + origin.delta.y);
                do {
                    delta.rotate();
                    Vec2 lineOrigin = clip(clipResult, delta.invert(), ghostObstacle);
                    Vec2 oResult = clipResult.copy();
                    clipResult = clip(clipResult, delta, ghostObstacle);
                    int dist = oResult.dist(clipResult);
                    if (hasLine(clipResult, delta, startIndex, internalLines)) {
                        lines[ghostObstacle.y][ghostObstacle.x] = 'O';
                        matches.put(ghostObstacle.x, ghostObstacle.y);
                        break;
                    }
                    internalLines.put(origin.delta.copy(), new A6B.Line(lineOrigin, clipResult, startIndex += dist));
                    current = clipResult;
                } while (clipResult.inBounds());
            }
            shutDownWorker(this);
        }

        @Override
        public String toString() {
            return getName() + ": " + current;
        }
    }

    private record ExeOrigin(Vec2 pos, Vec2 delta, int index) {

    }

    private static synchronized ExeOrigin pollElement() {
        return executors.poll(); //must be synced
    }

    private static synchronized void shutDownWorker(WorkerThread thread) {
        workers.remove(thread);
    }

    private record Line(Vec2 p1, Vec2 p2, int index) {
            private Line(Vec2 p1, Vec2 p2, int index) {
                this.p1 = p1.copy();
                this.p2 = p2.copy();
                this.index = index;
            }

            boolean isVertical() {
                return p1.x == p2.x;
            }

            boolean isHorizontal() {
                return p1.y == p2.y;
            }

            boolean inside(Vec2 pos) {
                if (isVertical()) return pos.x == p1.x && (p1.y < p2.y ?
                        pos.y >= p1.y && pos.y <= p2.y :
                        pos.y >= p2.y && pos.y <= p2.x);
                else return pos.y == p1.y && (p1.x < p2.x ?
                        pos.x >= p1.x && pos.x <= p2.x :
                        pos.x >= p2.x && pos.x <= p2.x);

            }
        }

    private static final class Vec2 {
        private int x, y;

        private Vec2(int x, int y) {
            this.x = x;
            this.y = y;
        }

        void rotate() {
            if (x != 0) {
                y = x;
                x = 0;
            } else {
                x = -y;
                y = 0;
            }
        }

        void move(int dx, int dy) {
            x+=dx;
            y+=dy;
        }

        void move(Vec2 other) {
            this.move(other.x, other.y);
        }

        boolean inBounds() {
            return y > 0 && y < lines.length - 1 && x > 0 && x < lines[0].length - 1;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Vec2) obj;
            return this.x == that.x &&
                    this.y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "Vec2[" +
                    "x=" + x + ", " +
                    "y=" + y + ']';
        }

        public Vec2 copy() {
            return new Vec2(x, y);
        }

        public Vec2 invert() {
            return new Vec2(-x, -y);
        }

        public int dist(Vec2 destination) {
            if (destination.y == this.y) return Math.abs(destination.x - this.x);
            else return Math.abs(destination.y - this.y);
        }
    }
}