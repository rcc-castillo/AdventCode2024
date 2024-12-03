package challenge3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MullItOver {
    public static void main(String[] args) {
        String inputFile = "src/main/java/challenge3/input.txt";
        try {
            processFile(inputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void processFile(String inputFile) throws IOException {
        List<String> input = readInput(inputFile);
        //System.out.println(input);
        List<String> multiplications = processLines(input, false);
        int result = getSumOfMultiplications(multiplications);
        System.out.printf("Sum of multiplications required in part1: %d%n", result);

        List<String> multiplications2 = processLines(input, true);
        int result2 = getSumOfMultiplications(multiplications2);
        System.out.printf("Sum of multiplications required in part2: %d%n", result2);
    }

    public static List<String> readInput(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    private static List<String> processLines(List<String> input, boolean processWithDoStatements) {
        return processWithDoStatements ? processWithDoStatements(input) : processWithoutDoStatements(input);
    }

    private static List<String> processWithoutDoStatements(List<String> input) {
        return input.stream()
                .flatMap(MullItOver::findMultiplications)
                .toList();
    }

    private static List<String> processWithDoStatements(List<String> input) {
        String linesJoined = String.join("", input);
        List<String> segments = getSegmentationByDoStatements(linesJoined);

        segments.forEach(System.out::println);

        return segments.stream()
                .filter(segment -> !segment.startsWith("don't()"))
                .flatMap(MullItOver::findMultiplications)
                .toList();
    }

    private static List<String> getSegmentationByDoStatements(String input) {
        List<Integer> segmentIndexes = Pattern.compile("do\\(\\)|don't\\(\\)")
                .matcher(input)
                .results()
                .map(MatchResult::start)
                .collect(Collectors.toList());
        segmentIndexes.addFirst(0);
        segmentIndexes.addLast(input.length());

        return segmentIndexes.stream()
                .map(i -> {
                    int currentIndex = segmentIndexes.indexOf(i);
                    if (currentIndex == segmentIndexes.size() - 1) {
                        return "";
                    }
                    return input.substring(segmentIndexes.get(currentIndex), segmentIndexes.get(currentIndex + 1));
                })
                .toList();
    }

    private static Stream<String> findMultiplications(String inputLine) {
        return Pattern.compile("mul\\(\\d{1,3},\\d{1,3}\\)")
                .matcher(inputLine)
                .results()
                .map(MatchResult::group);
    }

    private static int getSumOfMultiplications(List<String> multiplications) {
        return multiplications.stream()
                .mapToInt(multiplication -> {
                    String[] numbers = multiplication.substring(4, multiplication.length() - 1).split(",");
                    return Integer.parseInt(numbers[0]) * Integer.parseInt(numbers[1]);
                })
                .sum();
    }
}
