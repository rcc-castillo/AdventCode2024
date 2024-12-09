package challenge7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BridgeRepair {
    public static void main(String[] args) {
        String inputFile = "src/main/java/challenge7/input.txt";
        try {
            processFile(inputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void processFile(String inputFile) throws IOException {
        List<String> input = readInput(inputFile);
        List<Long> trueCalibratedValues = getTrueCalibratedValues(input, false);
        Long sum = trueCalibratedValues.stream().mapToLong(Long::longValue).sum();
        System.out.printf("Sum of true calibrated values required in part 1: %d\n", sum);

        List<Long> trueCalibratedValuesWithConcatOperator = getTrueCalibratedValues(input, true);
        Long sumWithConcatOperator = trueCalibratedValuesWithConcatOperator.stream().mapToLong(Long::longValue).sum();
        System.out.printf("Sum of true calibrated values required in part 2: %d\n", sumWithConcatOperator);
    }

    public static List<String> readInput(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    private static List<Long> getTrueCalibratedValues(List<String> input, boolean whithConcatOperator) {
        return input.stream()
                .map(line -> testEquation(line, whithConcatOperator))
                .filter(value -> value != Long.MIN_VALUE)
                .collect(Collectors.toList());
    }

    private static Long testEquation(String line, boolean withConcatOperator) {
        String[] equation = line.split(": ");
        long target = Long.parseLong(equation[0]);
        int[] operators = Stream.of(equation[1].split(" "))
                .mapToInt(Integer::parseInt)
                .toArray();
        boolean isCalibrated = withConcatOperator ?
                evaluateWithConcatOperator(target, operators, 1, operators[0]) :
                evaluateEquation(target, operators, 1, operators[0]);
        return isCalibrated ? target : Long.MIN_VALUE;
    }

    private static boolean evaluateWithConcatOperator(long target, int[] operators, int index, long result) {
        if (index == operators.length) {
            return result == target;
        }
        long concatOperation = Long.parseLong(result + "" + operators[index]);
        return evaluateWithConcatOperator(target, operators, index + 1, result + operators[index])
                || evaluateWithConcatOperator(target, operators, index + 1, result * operators[index])
                || evaluateWithConcatOperator(target, operators, index + 1, concatOperation);

    }

    private static boolean evaluateEquation(long target, int[] operators, int index, long result) {
        if (index == operators.length) {
            return result == target;
        }

        return evaluateEquation(target, operators, index + 1, result + operators[index])
                || evaluateEquation(target, operators, index + 1, result * operators[index]);
    }
}
