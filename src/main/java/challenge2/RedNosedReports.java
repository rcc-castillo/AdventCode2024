package challenge2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RedNosedReports {
    public static void main(String[] args) {
        String inputFile = "src/main/java/challenge2/input.txt";
        try {
            processFile(inputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void processFile(String inputFile) throws IOException {
        List<String> input = readInput(inputFile);
        List<Boolean> secureLevels = getSecureLevels(input);
        long secureLevelsNumber = secureLevels.stream().filter(Boolean::booleanValue).count();
        System.out.printf("Number of secure reports required in part one: %d%n", secureLevelsNumber);

        List<Boolean> secureLevelsDampened = getSecureLevelsDampened(input);
        long secureLevelsNumberDampened = secureLevelsDampened.stream().filter(Boolean::booleanValue).count();
        System.out.printf("Number of secure reports required in part two: %d%n", secureLevelsNumberDampened);
    }

    public static List<String> readInput(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    public static List<Boolean> getSecureLevels(List<String> input) {
        return input.stream()
                .map(line -> line.split(" "))
                .map(levels -> checkIncreasingList(levels) || checkDecreasingList(levels))
                .collect(Collectors.toList());
    }

    public static boolean checkIncreasingList(String[] levels) {
        return Arrays.stream(levels)
                .mapToInt(Integer::parseInt)
                .reduce((current, next) -> (next - current >= 1 && next - current <= 3) ? next : Integer.MIN_VALUE)
                .orElse(Integer.MIN_VALUE) != Integer.MIN_VALUE;
    }

    public static boolean checkDecreasingList(String[] levels) {
        return Arrays.stream(levels)
                .mapToInt(Integer::parseInt)
                .reduce((current, next) -> (current - next >= 1 && current - next <= 3) ? next : Integer.MAX_VALUE)
                .orElse(Integer.MAX_VALUE) != Integer.MAX_VALUE;
    }

    private static List<Boolean> getSecureLevelsDampened(List<String> input) {
        return input.stream()
                .map(line -> line.split(" "))
                .map(levels -> checkIncreasingList(levels) ||
                        checkDecreasingList(levels) ||
                        checkIncreasingListWithDampener(levels) ||
                        checkDecreasingListWithDampener(levels))
                .collect(Collectors.toList());
    }

    public static boolean checkIncreasingListWithDampener(String[] levels) {
        int[] intLevels = Arrays.stream(levels).mapToInt(Integer::parseInt).toArray();
        for (int i = 0; i < intLevels.length; i++) {
            int[] modifiedLevels = new int[intLevels.length - 1];
            System.arraycopy(intLevels, 0, modifiedLevels, 0, i);
            System.arraycopy(intLevels, i + 1, modifiedLevels, i, intLevels.length - i - 1);
            if (checkIncreasingList(Arrays.stream(modifiedLevels).mapToObj(String::valueOf).toArray(String[]::new))) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkDecreasingListWithDampener(String[] levels) {
        int[] intLevels = Arrays.stream(levels).mapToInt(Integer::parseInt).toArray();
        for (int i = 0; i < intLevels.length; i++) {
            int[] modifiedLevels = new int[intLevels.length - 1];
            System.arraycopy(intLevels, 0, modifiedLevels, 0, i);
            System.arraycopy(intLevels, i + 1, modifiedLevels, i, intLevels.length - i - 1);
            if (checkDecreasingList(Arrays.stream(modifiedLevels).mapToObj(String::valueOf).toArray(String[]::new))) {
                return true;
            }
        }
        return false;
    }
}
