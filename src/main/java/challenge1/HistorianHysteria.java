package challenge1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


public class HistorianHysteria {

    public static void main(String[] args) {
        String inputFile = "src/main/java/challenge1/input.txt";
        try {
            processFile(inputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void processFile(String inputFile) throws IOException {
        List<String> input = readInput(inputFile);
        List<Integer> listTeam1 = getTeamList(input, 0);
        List<Integer> listTeam2 = getTeamList(input, 1);


        List<Integer> orederedListTeam1 = listTeam1.stream().sorted().toList();
        List<Integer> orederedListTeam2 = listTeam2.stream().sorted().toList();
        System.out.println(listTeam1);
        System.out.println(listTeam2);

        int distance = getDistance(orederedListTeam1, orederedListTeam2);
        System.out.printf("Distance requiered in part 1: %d%n", distance);

        int similitaryScore = getSimilitaryScore(listTeam1, listTeam2);
        System.out.printf("Similarity score requiered in part 2: %d%n", similitaryScore);
    }

    public static List<String> readInput(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    public static List<Integer> getTeamList(List<String> input, int teamId) {
        return input.stream()
                .map(line -> line.split(" {3}"))
                .map(parts -> Integer.valueOf(parts[teamId]))
                .collect(Collectors.toList());
    }

    public static int getDistance(List<Integer> list1, List<Integer> list2) {
        int distance = 0;
        for (int i = 0; i < list1.size(); i++) {
            distance += Math.abs(list1.get(i) - list2.get(i));
        }
        return distance;
    }

    private static int getSimilitaryScore(List<Integer> listTeam1, List<Integer> listTeam2) {
        return listTeam1.stream()
                .mapToInt(id -> id * (int) listTeam2.stream().filter(id::equals).count())
                .sum();
    }
}
