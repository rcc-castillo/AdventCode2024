package challenge10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HoofIt {
    public static void main(String[] args) {
        String inputFile = "src/main/java/challenge10/input.txt";
        try {
            processFile(inputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void processFile(String inputFile) throws IOException {
        List<String> input = readInput(inputFile);
        int[][] matrix = input.stream()
                .map(s -> Arrays.stream(s.split(""))
                        .map(Integer::parseInt)
                        .mapToInt(Integer::intValue)
                        .toArray())
                .toArray(int[][]::new);

        List<int[]> trailHeads = getTrailElements(matrix, 0);
        List<int[]> trailEnds = getTrailElements(matrix, 9);

        List<Integer> trailScore = getTrailsScore(matrix, trailHeads, trailEnds);
        int maxScore = trailScore.stream().mapToInt(Integer::intValue).sum();
        System.out.printf("Max score required in part 1: %d\n", maxScore);

        List<Integer> trailScore2 = getAllTrails(matrix, trailEnds, trailHeads);
        int maxScore2 = trailScore2.stream().mapToInt(Integer::intValue).sum();
        System.out.printf("Max score required in part 2: %d\n", maxScore2);
    }

    public static List<String> readInput(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    private static List<Integer> getTrailsScore(int[][] matrix, List<int[]> trailHeads, List<int[]> trailEnds) {
        return trailHeads.stream()
                .map(head -> trailEnds.stream()
                        .map(end -> getTrailScore(matrix, head, end))
                        .reduce(Integer::sum)
                        .orElse(0))
                .collect(Collectors.toList());
    }

    private static List<Integer> getAllTrails(int[][] matrix, List<int[]> trailEnds, List<int[]> trailHeads) {
        return trailHeads.stream()
                .map(head -> trailEnds.stream()
                        .mapToInt(end -> getAllTrailPaths(matrix, head, end))
                        .sum())
                .collect(Collectors.toList());
    }

    private static int getTrailScore(int[][] matrix, int[] head, int[] end) {
        if (Arrays.equals(head, end)) return 1;
        boolean isEnd = getAdjacentCells(matrix, head[0], head[1]).stream()
                .anyMatch(cell -> getTrailScore(matrix, cell, end) == 1);
        return isEnd ? 1 : 0;
    }

    private static int getAllTrailPaths(int[][] matrix, int[] head, int[] end) {
        if (Arrays.equals(head, end)) return 1;
        return getAdjacentCells(matrix, head[0], head[1]).stream()
                .map(cell -> getAllTrailPaths(matrix, cell, end))
                .mapToInt(Integer::intValue)
                .sum();
    }

    private static List<int[]> getAdjacentCells(int[][] matrix, int posX, int posY) {
        return IntStream.of(-1, 1)
                .mapToObj(i -> List.of(new int[]{posX + i, posY}, new int[]{posX, posY + i}))
                .flatMap(List::stream)
                .filter(e -> e[0] >= 0 && e[0] < matrix.length && e[1] >= 0 && e[1] < matrix[0].length)
                .filter(e -> matrix[e[0]][e[1]] == matrix[posX][posY] + 1)
                .collect(Collectors.toList());
    }

    private static List<int[]> getTrailElements(int[][] matrix, int searchedElement) {
        return IntStream.range(0, matrix.length)
                .mapToObj(i -> IntStream.range(0, matrix[0].length)
                        .filter(j -> matrix[i][j] == searchedElement)
                        .mapToObj(j -> new int[]{i, j})
                        .collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
