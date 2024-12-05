package net.kapitencraft;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class A5B {

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("assets/input5.txt");
        String[] lines = new BufferedReader(new FileReader(file)).lines().toArray(String[]::new);
        Multimap<Integer, Integer> rules = HashMultimap.create(); //save rules
        int i = 0;
        for (; i < lines.length; i++) {
            String line = lines[i];
            if (line.isEmpty())
                break;
            String[] rule = line.split("\\|");
            rules.put(Integer.parseInt(rule[0]), Integer.parseInt(rule[1])); // earlier -> later map
        }
        i++;
        List<List<Integer>> incorrect = new ArrayList<>();
        a: for (; i < lines.length; i++) {
            Multimap<Integer, Integer> rulesCopy = HashMultimap.create(rules);
            String line = lines[i];
            List<Integer> updates = Arrays.stream(line.split(",")).map(Integer::parseInt).toList();

            List<Integer> remaining = new ArrayList<>(rulesCopy.keySet().stream().toList());
            remaining.removeAll(updates);
            remaining.forEach(rulesCopy::removeAll);
            for (int update : updates) {
                if (rulesCopy.containsValue(update)) {
                    incorrect.add(updates);
                    continue a;
                } //the rules expect the update to occur at a later state
                rulesCopy.removeAll(update); //tries to remove this earlier number from the rules, allowing for any elements, listed as later, to not "continue a"
            }
        }

        Comparator<Integer> incorrectSorter = (l, l1) -> Objects.equals(l, l1) ? 0 : rules.get(l).contains(l1) ? 1 : -1;

        int amount = 0;
        for (List<Integer> list : incorrect) {
            list = new ArrayList<>(list); //is immutable
            list.sort(incorrectSorter);
            amount += list.get(list.size() / 2);
        }
        System.out.println(amount);
    }
}