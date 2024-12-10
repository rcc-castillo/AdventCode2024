package challenge9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DiskFragmenter {
    public static void main(String[] args) {
        String inputFile = "src/main/java/challenge9/input.txt";
        try {
            processFile(inputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void processFile(String inputFile) throws IOException {
        String input = readInput(inputFile).getFirst();
        List<Character> disk = getDiskIdRepresentation(input);
        System.out.println("Disk representation: " + disk);
        List<Character> compressedDisk = compressDisk(disk);
        System.out.println("Compressed disk: " + compressedDisk);
    }

    public static List<String> readInput(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    private static List<Character> getDiskIdRepresentation(String input) {
        StringBuilder disk = new StringBuilder();
        int counter = 0;
        for (int i = 0; i < input.length(); i++) {
            if ((i + 1) % 2 == 0) {
                disk.append(".".repeat(Character.getNumericValue(input.charAt(i))));
            }
            else {
                disk.append(String.valueOf(counter).repeat(Character.getNumericValue(input.charAt(i))));
                counter++;
            }
        }
        return disk.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
    }

    private static List<Character> compressDisk(List<Character> disk) {
        if (disk.indexOf('.') == disk.indexOf(disk.removeLast())) {
            return disk;
        }
        if (disk.getLast().equals('.')) {
            disk.removeLast();
        }
        System.out.println(disk.indexOf('.'));
        System.out.println(disk.indexOf(disk.removeLast()));
        Collections.swap(disk, disk.indexOf('.'), disk.indexOf(disk.removeLast()));
        return compressDisk(disk);
    }
}
