package net.kapitencraft;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class A9B2 {

    public static void main(String[] args) throws IOException {
        File file = new File("assets/input9.txt");
        char[][] lines = new BufferedReader(new FileReader(file)).lines().map(String::toCharArray).toArray(char[][]::new);
        char[] line = lines[0];
        List<StorageObject> space = new ArrayList<>();
        boolean empty = false;
        {
            int index = 0;
            for (char c : line) {
                int i = c - 48; //ASCII number start is 48 = 0
                if (empty) {
                    if (i > 0) space.add(new FreeSpace(i));
                } else {
                    space.add(new FileObj(i, index));
                    index++;
                }
                empty = !empty;
            }
        }
        int visited = 9999;
        int rightIndex = space.size();
        while (rightIndex > 0) {
            rightIndex--;
            StorageObject object = space.get(rightIndex);
            if (object.isEmpty()) {
                if (rightIndex == space.size()-1) space.remove(rightIndex);
                continue;
            }
            FileObj fileObj = (FileObj) object;
            if (visited < fileObj.id()) {
                continue;
            }
            visited = fileObj.id();
            int searchSize = object.size();
            int foundIndex = -1;
            for (int i = 0; i < rightIndex; i++) {
                StorageObject obj = space.get(i);
                if (obj.isEmpty() && obj.size() >= searchSize) {
                    foundIndex = i;
                    break;
                }
            }
            if (foundIndex != -1) {
                StorageObject oldSpace = space.get(foundIndex);
                FreeSpace newSpace = new FreeSpace(oldSpace.size() - searchSize);
                space.set(rightIndex, new FreeSpace(searchSize));
                if (newSpace.size() > 0) space.set(foundIndex, newSpace);
                else {
                    space.remove(foundIndex);
                    rightIndex--;
                }
                space.add(foundIndex, object);
                rightIndex++;
                if (space.size()-1 != rightIndex) {
                    StorageObject left = space.get(rightIndex-1);
                    StorageObject self = space.get(rightIndex);
                    StorageObject right = space.get(rightIndex+1);
                    int totalSize = self.size();
                    if (left.isEmpty()) totalSize += left.size();
                    if (right.isEmpty()) totalSize += right.size();
                    FreeSpace replace = new FreeSpace(totalSize);
                    space.set(rightIndex, replace);
                    if (right.isEmpty()) space.remove(rightIndex+1);
                    if (left.isEmpty()) {
                        space.remove(rightIndex-1);
                    }
                } else {
                    space.remove(rightIndex);
                }
            }
        }
        FileWriter writer = new FileWriter("assets/input9o.txt");
        int index = 0;
        long result = 0;
        for (StorageObject object : space) {
            if (object.isEmpty()) {
                index += object.size();
                writer.write(".".repeat(object.size()));
                continue;
            }
            int id = ((FileObj) object).id();
            int oIndex = index;
            writer.write(String.valueOf((char) (id + 48)).repeat(object.size()));
            for (; index < oIndex + object.size(); index++) {
                result += (long) index * id;
            }
        }
        writer.close();
        System.out.println(result);
    }

    interface StorageObject {

        int size();

        boolean isEmpty();
    }

    record FreeSpace(int size) implements StorageObject {

        @Override
        public boolean isEmpty() {
            return true;
        }
    }

    record FileObj(int size, int id) implements StorageObject {

        @Override
        public boolean isEmpty() {
            return false;
        }
    }
}