package challenge8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ResonantCollinearity {
    public static void main(String[] args) {
        String inputFile = "src/main/java/challenge8/input.txt";
        try {
            processFile(inputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void processFile(String inputFile) throws IOException {
        List<String> input = readInput(inputFile);
        char[][] matrix = input.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);
        HashMap<Character, List<int[]>> antennas = getAntennas(matrix);

        Set<int[]> antiNodes = generateAntiNodes(antennas, matrix, false);
        System.out.printf("Number of anti-nodes required in part 1: %d\n", antiNodes.size());

        Set<int[]> harmonicAntiNodes = generateAntiNodes(antennas, matrix, true);
        System.out.printf("Number of anti-nodes required in part 2: %d\n", harmonicAntiNodes.size());
    }

    public static List<String> readInput(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    private static HashMap<Character,List<int[]>> getAntennas(char[][] matrix) {
        HashMap<Character, List<int[]>> antennas = new HashMap<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] != '.' && matrix[i][j] != '#') {
                    antennas.computeIfAbsent(matrix[i][j], ArrayList::new).add(new int[]{i, j});
                }
            }
        }
        return antennas;
    }

    private static Set<int[]> generateAntiNodes(HashMap<Character, List<int[]>> antennas, char[][] matrix, boolean isHarmonic) {
        return antennas.keySet().stream()
                .flatMap(k -> antennas.get(k).stream()
                        .flatMap(pos1 -> antennas.get(k).stream()
                                .filter(pos2 -> !Arrays.equals(pos1, pos2))
                                .map(pos2 -> isHarmonic ? getHarmonicAntiNodes(matrix, pos1, pos2) :
                                        List.of(getAntiNode(matrix, pos1, pos2))))
                        .flatMap(List::stream))
                .filter(pos -> pos.length > 0)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Arrays::compare)));
    }

    private static int[] getAntiNode(char[][] matrix, int[] pos1, int[] pos2) {
        int newX = pos1[0] + (pos1[0] - pos2[0]);
        int newY = pos1[1] + (pos1[1] - pos2[1]);
        return isAntiNode(matrix, newX, newY) ? new int[]{newX, newY} : new int[]{};
    }

    private static List<int[]> getHarmonicAntiNodes(char[][] matrix, int[] pos1, int[] pos2) {
        List<int[]> antiNodes = new ArrayList<>();
        int x1 = pos1[0];
        int y1 = pos1[1];
        int x2 = pos2[0];
        int y2 = pos2[1];
        int newX = x1 + (x1 - x2);
        int newY = y1 + (y1 - y2);
        antiNodes.add(pos1);
        while (isAntiNode(matrix, newX, newY)) {
            antiNodes.add(new int[]{newX, newY});
            newX += (x1 - x2);
            newY += (y1 - y2);
        }
        return antiNodes;
    }

    private static boolean isAntiNode(char[][] matrix, int x, int y) {
        return x >= 0 && x < matrix.length && y >= 0 && y < matrix[0].length;
    }

}
