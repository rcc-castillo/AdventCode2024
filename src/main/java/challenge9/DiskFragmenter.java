package challenge9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        List<String> disk = getDiskIdRepresentation(input);

        List<String> compressedDisk = compressDisk(disk);
        long fileSystemChecksum = getFileSystemChecksum(compressedDisk);
        System.out.printf("File system checksum required in part 1: %d%n", fileSystemChecksum);

        disk = getDiskIdRepresentation(input);
        List<String> compressedDiskOptimized = compressDiskOptimized(disk);
        fileSystemChecksum = getFileSystemChecksum(compressedDiskOptimized);
        System.out.printf("File system checksum required in part 2: %d%n", fileSystemChecksum);
    }

    public static List<String> readInput(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    private static List<String> getDiskIdRepresentation(String input) {
        List<String> disk = new ArrayList<>();

        int counter = 0;
        for (int i = 0; i < input.length(); i++) {
            int num = Character.getNumericValue(input.charAt(i));
            if ((i + 1) % 2 == 0) disk.addAll(Collections.nCopies(num, "."));
            else {
                disk.addAll(Collections.nCopies(num, String.valueOf(counter)));
                counter++;
            }
        }
        return disk;
    }

    private static List<String> compressDisk(List<String> disk) {
        for (int readIndex = disk.size() - 1; readIndex >= 0; readIndex--) {
            int writeIndex = disk.indexOf(".");
            if (readIndex < writeIndex) break;
            disk.set(writeIndex, disk.get(readIndex));
            if (readIndex != writeIndex) disk.set(readIndex, ".");
        }
        return disk;
    }

    private static List<String> compressDiskOptimized(List<String> disk) {
        List<int[]> freeContiguousSpace = getContiguousSpace(disk);
        Map<Integer, Integer> diskMap = disk.stream()
                .filter(c -> !c.equals("."))
                .collect(Collectors.toMap(Integer::parseInt, integer -> 1, Integer::sum, LinkedHashMap::new));

        List<Integer> sortedFiles = new ArrayList<>(diskMap.keySet());
        sortedFiles.sort(Collections.reverseOrder());

        sortedFiles.forEach(file -> placeFileInDisk(disk, diskMap, freeContiguousSpace, file));
        return disk;
    }

    private static void placeFileInDisk(List<String> disk, Map<Integer, Integer> diskMap, List<int[]> freeContiguousSpace, int file) {
        int fileSize = diskMap.get(file);
        int lastIndexOfFile = disk.lastIndexOf(String.valueOf(file));
        findSuitableSpace(freeContiguousSpace, fileSize, lastIndexOfFile).ifPresent(space -> {
            int start = space[0];
            diskMap.put(file, -1);
            placeFile(disk, file, fileSize, start);
            removePreviousElements(disk, file, start + fileSize);
            updateSpace(freeContiguousSpace, space, fileSize);
        });
    }

    private static Optional<int[]> findSuitableSpace(List<int[]> freeContiguousSpace, int fileSize, int lastIndexOfFile) {
        return freeContiguousSpace.stream()
                .filter(space -> (space[1] - space[0] + 1) >= fileSize && lastIndexOfFile > space[0])
                .findFirst();
    }

    private static void placeFile(List<String> disk, int file, int fileSize, int start) {
        IntStream.range(0, fileSize).forEach(i -> disk.set(start + i, String.valueOf(file)));
    }

    private static void removePreviousElements(List<String> disk, int file, int start) {
        IntStream.range(start, disk.size())
                .filter(i -> disk.get(i).equals(String.valueOf(file)))
                .forEach(i -> disk.set(i, "."));
    }

    private static void updateSpace(List<int[]> freeContiguousSpace, int[] space, int fileSize) {
        space[0] += fileSize;
        if (space[0] > space[1]) freeContiguousSpace.remove(space);
    }

    private static List<int[]> getContiguousSpace(List<String> disk) {
        List<int[]> contiguousSpace = new ArrayList<>();
        for (int i = 0; i < disk.size(); i++) {
            if (disk.get(i).equals(".")) {
                int start = i;
                while (i < disk.size() && disk.get(i).equals(".")) i++;
                contiguousSpace.add(new int[]{start, i - 1});
            }
        }
        return contiguousSpace;
    }

    private static long getFileSystemChecksum(List<String> compressedDisk) {
        return IntStream.range(0, compressedDisk.size())
                .mapToLong(i -> !compressedDisk.get(i).equals(".") ?
                        Long.parseLong(compressedDisk.get(i)) * i : 0)
                .sum();
    }
}
