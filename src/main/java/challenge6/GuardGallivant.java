package challenge6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

public class GuardGallivant {
    public static void main(String[] args) {
        String inputFile = "src/main/java/challenge6/input.txt";
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
        Guard guard = getGuardPosition(matrix);
        Set<String> visitedPath = predictGuardPath(matrix, guard);
        Set<String> visitedCells = getVisitedCells(visitedPath);
        System.out.printf("Visited cells by guard required in part 1: %d%n", visitedCells.size());

        int loopPositions = findLoopPositions(matrix);
        System.out.printf("Possible obstacle positions for loop required in part 2: %d%n", loopPositions);
    }

    public static List<String> readInput(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    private static Guard getGuardPosition(char[][] matrix) {
        List<Character> validChars = List.of('<', '^', '>', 'v');
        return IntStream.range(0, matrix.length)
                .mapToObj(i -> IntStream.range(0, matrix[i].length)
                        .filter(j -> validChars.contains(matrix[i][j]))
                        .mapToObj(j -> new Guard(i, j, matrix[i][j]))
                        .findFirst()
                        .orElse(null))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Guard not found"));
    }

    private static Set<String> predictGuardPath(char[][] matrix, Guard guard) {
        if (guard.canAdvance(matrix)) {
            guard.move();
        } else {
            guard.turn();
        }
        if (guard.isInLoop()) { return guard.getVisitedPath(); }
        return guard.isGoingOutOfBounds(matrix) ? guard.getVisitedPath() : predictGuardPath(matrix, guard);
    }

    private static int findLoopPositions(char[][] matrix) {
        Guard guard = getGuardPosition(matrix);

        Set<String> visitedPath = predictGuardPath(matrix, guard);
        Set<String> visitedCells = getVisitedCells(visitedPath);

        int positionsForLoop = 0;
        for (String cell : visitedCells) {
            String[] cellParts = cell.split(",");
            int x = Integer.parseInt(cellParts[0]);
            int y = Integer.parseInt(cellParts[1]);
            if (matrix[x][y] == '.') {
                matrix[x][y] = '#';
                if (isGuardInLoop(matrix)) {
                    positionsForLoop++;
                }
                matrix[x][y] = '.';
            }
        }
        return positionsForLoop;
    }

    public static boolean isGuardInLoop(char[][] matrix) {
        Guard guard = getGuardPosition(matrix);
        predictGuardPath(matrix, guard);
        return guard.inLoop;
    }

    public static Set<String> getVisitedCells(Set<String> visitedPath) {
        return visitedPath.stream()
                .map(s -> s.split(",")[0] + "," + s.split(",")[1])
                .collect(java.util.stream.Collectors.toSet());
    }
}
