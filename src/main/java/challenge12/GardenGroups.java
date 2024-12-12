package challenge12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GardenGroups {
    public static void main(String[] args) {
        String inputFile = "src/main/java/challenge12/input.txt";
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

        Map<Character, List<Set<GardenPlot>>> gardenRegions = getGardenRegions(matrix);

        int fencePriceByPerimeter = getFencePriceByPerimeter(gardenRegions);
        System.out.printf("Total price of fence required in part 1: $%d%n", fencePriceByPerimeter);

        int fencePriceBySides = getFencePriceBySides(gardenRegions);
        System.out.printf("Total price of fence required in part 2: $%d", fencePriceBySides);
    }

    public static List<String> readInput(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    private static int getFencePriceByPerimeter(Map<Character, List<Set<GardenPlot>>> gardenRegions) {
        return gardenRegions.values().stream()
                .flatMap(List::stream)
                .mapToInt(region -> getPlotPerimeter(region) * region.size())
                .sum();
    }

    private static int getPlotPerimeter(Set<GardenPlot> region) {
        return region.stream().mapToInt(plot -> 4 - plot.countAdjacentPlots(region)).sum();
    }

    private static int getFencePriceBySides(Map<Character, List<Set<GardenPlot>>> gardenRegions) {
        return gardenRegions.values().stream()
                .flatMap(List::stream)
                .mapToInt(region -> getNumSides(region) * region.size())
                .sum();
    }

    private static int getNumSides(Set<GardenPlot> region) {
        int numSides = 0;
        for (GardenPlot plot : region) {
            if (!plot.hasLeftNeighbor(region) && !plot.hasTopNeighbor(region) ||
                    plot.hasLeftNeighbor(region) && plot.hasTopNeighbor(region) && !plot.hasTopLeftNeighbor(region)) {
                numSides++;
            }
            if (!plot.hasTopNeighbor(region) && !plot.hasRightNeighbor(region) ||
                    plot.hasTopNeighbor(region) && plot.hasRightNeighbor(region) && !plot.hasTopRightNeighbor(region)) {
                numSides++;
            }
            if (!plot.hasRightNeighbor(region) && !plot.hasBottomNeighbor(region) ||
                    plot.hasRightNeighbor(region) && plot.hasBottomNeighbor(region) && !plot.hasBottomRightNeighbor(region)) {
                numSides++;
            }
            if (!plot.hasBottomNeighbor(region) && !plot.hasLeftNeighbor(region) ||
                    plot.hasBottomNeighbor(region) && plot.hasLeftNeighbor(region) && !plot.hasBottomLeftNeighbor(region)) {
                numSides++;
            }
        }
        return numSides;
    }

    private static Map<Character, List<Set<GardenPlot>>> getGardenRegions(char[][] matrix) {
        Map<Character, List<Set<GardenPlot>>> gardenRegions = new HashMap<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                Set<GardenPlot> visited = new HashSet<>();
                GardenPlot current = new GardenPlot(matrix[i][j], i, j);
                exploreAdjacentPlots(current, matrix, visited);
                addPlotToRegion(gardenRegions, current, visited);
            }
        }
        return gardenRegions;
    }

    private static void exploreAdjacentPlots(GardenPlot current, char[][] matrix, Set<GardenPlot> visited) {
        if (visited.contains(current)) return;
        visited.add(current);
        int[][] directions = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}};
        for (int[] dir : directions) {
            int newX = current.getPosX() + dir[0];
            int newY = current.getPosY() + dir[1];
            GardenPlot neighbor = new GardenPlot(current.getPlotType(), newX, newY);
            if (neighbor.inBounds(matrix) && matrix[newX][newY] == neighbor.getPlotType()) {
                exploreAdjacentPlots(neighbor, matrix, visited);
            }
        }
    }

    private static void addPlotToRegion(Map<Character, List<Set<GardenPlot>>> gardenRegions, GardenPlot current, Set<GardenPlot> visited) {
        gardenRegions.computeIfAbsent(current.getPlotType(), k -> new ArrayList<>())
                .stream()
                .filter(region -> region.contains(current))
                .findFirst()
                .orElseGet(() -> {
                    Set<GardenPlot> newRegion = new HashSet<>();
                    gardenRegions.get(current.getPlotType()).add(newRegion);
                    return newRegion;
                })
                .addAll(visited);
    }
}
