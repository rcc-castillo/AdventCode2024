package challenge11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlutonianPebbles {
    public static void main(String[] args) {
        String inputFile = "src/main/java/challenge11/input.txt";
        try {
            processFile(inputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void processFile(String inputFile) throws IOException {
        String input = readInput(inputFile).getFirst();
        List<String> strPebbles = Arrays.asList(input.split(" "));
        List<Long> pebbles = strPebbles.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
        System.out.println(pebbles);
        long numberOfPebbles = countPebblesAfterBlinks(pebbles, 25);
        System.out.printf("Number of pebbles required in part 1: %d%n", numberOfPebbles);

        numberOfPebbles = countPebblesAfterBlinks(pebbles, 75);
        System.out.printf("Number of pebbles required in part 2: %d%n", numberOfPebbles);
    }

    public static List<String> readInput(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    private static long countPebblesAfterBlinks(List<Long> pebbles, int times) {
        Map<Long, Long> pebblesCount = new HashMap<>();
        for (long pebble : pebbles) {
            pebblesCount.merge(pebble, 1L, Long::sum);
        }
        for (int i = 0; i < times; i++) {
            pebblesCount = processPebbles(pebblesCount);
        }
        return pebblesCount.values().stream().mapToLong(Long::longValue).sum();
    }

    private static Map<Long, Long> processPebbles(Map<Long, Long> pebblesCount) {
        Map<Long, Long> newPebblesCount = new HashMap<>();
        for (Map.Entry<Long, Long> entry : pebblesCount.entrySet()) {
            Long pebble = entry.getKey();
            Long count = entry.getValue();
            if (isZeroEngrave(pebble)) {
                newPebblesCount.merge(1L, count, Long::sum);
            }else if (isEven(pebble)) {
                List<Long> splitPebbles = splitPebble(pebble);
                newPebblesCount.merge(splitPebbles.get(0), count, Long::sum);
                newPebblesCount.merge(splitPebbles.get(1), count, Long::sum);
            } else {
                newPebblesCount.merge(pebble * 2024, count, Long::sum);
            }
        }
        return newPebblesCount;
    }

    private static List<Long> splitPebble(Long pebble) {
        String pebbleStr = pebble.toString();
        int half = pebble.toString().length() / 2;
        String firstHalf = pebbleStr.substring(0, half);
        String secondHalf = pebbleStr.substring(half);
        return Arrays.asList(Long.parseLong(firstHalf), Long.parseLong(secondHalf));
    }

    private static boolean isZeroEngrave(Long pebble) {
        return pebble == 0;
    }

    private static boolean isEven(Long pebble) {
        return String.valueOf(pebble).length() % 2 == 0;
    }
}