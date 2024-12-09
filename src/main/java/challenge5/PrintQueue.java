package challenge5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PrintQueue {
    public static void main(String[] args) {
        String inputFile = "src/main/java/challenge5/input.txt";
        try {
            processFile(inputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void processFile(String inputFile) throws IOException {
        List<String> input = readInput(inputFile);

        int separatorIndex = input.indexOf("");
        List<String> rules = input.subList(0, separatorIndex);
        List<String> updates = input.subList(separatorIndex + 1, input.size());
        HashMap<Integer, List<Integer>> rulesMap = getRulesMap(rules);

        List<String> correctUpdates = getUpdates(rulesMap, updates, "correct");
        int sumOfMiddleValues = getSumOfMiddleValues(correctUpdates);
        System.out.printf("Sum of middle values required in part 1: %d%n", sumOfMiddleValues);

        List<String> incorrectUpdates = getUpdates(rulesMap, updates, "incorrect");
        List<String> incorrectUpdatesFixed = fixIncorrectUpdates(rulesMap, incorrectUpdates);
        int sumOfMiddleValuesFixed = getSumOfMiddleValues(incorrectUpdatesFixed);
        System.out.printf("Sum of middle values required in part 2: %d%n", sumOfMiddleValuesFixed);

    }

    public static List<String> readInput(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    private static List<String> getUpdates(HashMap<Integer, List<Integer>> rulesMap, List<String> updates, String type) {
        return updates.stream()
                .filter(update -> "correct".equals(type) == isCorrectUpdate(rulesMap, update))
                .toList();
    }

    private static boolean isCorrectUpdate(HashMap<Integer, List<Integer>> rulesMap, String update) {
        List<Integer> updateParts = Arrays.stream(update.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        return IntStream.range(0, updateParts.size() - 1)
                .allMatch(i -> {
                    int key = updateParts.get(i);
                    List<Integer> restrictions = rulesMap.getOrDefault(key, Collections.emptyList());
                    List<Integer> remainingParts = updateParts.subList(i + 1, updateParts.size());
                    return restrictions.stream().noneMatch(remainingParts::contains);
                });
    }

    private static List<String> fixIncorrectUpdates(HashMap<Integer, List<Integer>> rulesMap, List<String> incorrectUpdates) {
        return incorrectUpdates.stream()
                .map(update -> reorderUpdate(rulesMap, update))
                .collect(Collectors.toList());
    }

    private static String reorderUpdate(HashMap<Integer, List<Integer>> rulesMap, String update) {
        List<Integer> updateParts = Arrays.stream(update.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        for (int i = 0; i < updateParts.size() - 1; i++) {
            int key = updateParts.get(i);
            List<Integer> restrictions = rulesMap.getOrDefault(key, Collections.emptyList());
            List<Integer> remainingParts = updateParts.subList(i + 1, updateParts.size());
            if (restrictions.stream().noneMatch(remainingParts::contains)){
                continue;
            }
            Integer restrictedPart = remainingParts.stream()
                    .filter(restrictions::contains)
                    .findFirst().orElse(null);
            int index = updateParts.indexOf(restrictedPart);
            Collections.swap(updateParts, i, index);
        }

        String unifiedUpdate = updateParts.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        return isCorrectUpdate(rulesMap, unifiedUpdate) ? unifiedUpdate : reorderUpdate(rulesMap, unifiedUpdate);
    }

    private static HashMap<Integer, List<Integer>> getRulesMap(List<String> rules) {
        HashMap<Integer, List<Integer>> rulesMap = new HashMap<>();
        for (String rule : rules) {
            String[] ruleParts = rule.split("\\|");
            int value = Integer.parseInt(ruleParts[0]);
            int key = Integer.parseInt(ruleParts[1]);
            rulesMap.computeIfAbsent(key, ArrayList::new).add(value);
        }
        return rulesMap;
    }

    public static int getSumOfMiddleValues(List<String> updates) {
        return updates.stream()
                .map(update -> Arrays.stream(update.split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList()))
                .mapToInt(parts -> parts.get(parts.size() / 2))
                .sum();
    }
}
