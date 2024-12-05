package challenge4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CeresSearch {
    public static void main(String[] args) {
        String inputFile = "src/main/java/challenge4/input.txt";
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

        int result = countXMAS(matrix);
        System.out.printf("Total XMAS required in part 1: %d%n%n", result);

        int xmasPatternCount = countXMASPattern(matrix);
        System.out.printf("Total X-MAS patterns required in part 2: %d%n", xmasPatternCount);
    }

    public static List<String> readInput(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    private static int countXMAS(char[][] matrix) {
        int countInRows = Arrays.stream(matrix)
                .mapToInt(row ->countXMASInString(String.valueOf(row)))
                .sum();

        int countInColumns = Arrays.stream(transpose(matrix))
                .mapToInt(row ->countXMASInString(String.valueOf(row)))
                .sum();

        List<String> allDiagonals = getAllDiagonals(matrix);
        int countInDiagonals = allDiagonals.stream()
                .mapToInt(CeresSearch::countXMASInString)
                .sum();

        return countInRows + countInColumns + countInDiagonals;
    }

    private static int countXMASInString(String rowString) {
        String reversedRowString = new StringBuilder(rowString).reverse().toString();
        return rowString.split("XMAS", -1).length - 1 +
                reversedRowString.split("XMAS", -1).length - 1;
    }

    private static char[][] transpose(char[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        char[][] transposed = new char[columns][rows];
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                transposed[i][j] = matrix[j][i];
            }
        }
        return transposed;
    }

    public static List<String> getAllDiagonals(char[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        return Stream.concat(
                Stream.concat(
                        IntStream.range(0, rows).mapToObj(i -> getDiagonal(matrix, i, 0, true)),
                        IntStream.range(1, cols).mapToObj(j -> getDiagonal(matrix, 0, j, true))
                ),
                Stream.concat(
                        IntStream.range(0, rows).mapToObj(i -> getDiagonal(matrix, i, cols - 1, false)),
                        IntStream.range(1, cols - 1).mapToObj(j -> getDiagonal(matrix, 0, j, false))
                )
        ).collect(Collectors.toList());
    }

    private static String getDiagonal(char[][] matrix, int row, int col, boolean leftToRight) {
        StringBuilder diagonal = new StringBuilder();
        while (row < matrix.length && col >= 0 && col < matrix[0].length) {
            diagonal.append(matrix[row][col]);
            row++;
            col += leftToRight ? 1 : -1;
        }
        return diagonal.toString();
    }

    private static int countXMASPattern(char[][] matrix) {
        return (int) IntStream.range(1, matrix.length - 1)
                .flatMap(i -> IntStream.range(1, matrix[0].length - 1)
                        .filter(j -> isXMASPattern(matrix, i, j)))
                .count();
    }

    private static boolean isXMASPattern(char[][] matrix, int row, int col) {
        return (matrix[row - 1][col - 1] == 'M' && matrix[row][col] == 'A' && matrix[row + 1][col + 1] == 'S' &&
                        matrix[row - 1][col + 1] == 'M' && matrix[row + 1][col - 1] == 'S') ||

                (matrix[row - 1][col - 1] == 'S' && matrix[row][col] == 'A' && matrix[row + 1][col + 1] == 'M' &&
                        matrix[row - 1][col + 1] == 'S' && matrix[row + 1][col - 1] == 'M') ||

                (matrix[row - 1][col - 1] == 'M' && matrix[row][col] == 'A' && matrix[row + 1][col + 1] == 'S' &&
                        matrix[row - 1][col + 1] == 'S' && matrix[row + 1][col - 1] == 'M') ||

                (matrix[row - 1][col - 1] == 'S' && matrix[row][col] == 'A' && matrix[row + 1][col + 1] == 'M' &&
                        matrix[row - 1][col + 1] == 'M' && matrix[row + 1][col - 1] == 'S');
    }
}
