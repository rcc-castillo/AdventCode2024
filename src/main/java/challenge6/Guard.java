package challenge6;

import java.util.HashSet;
import java.util.Set;

public class Guard {
    private int x, y;
    private char direction;
    private final Set<String> visitedPath;
    boolean inLoop;


    public Guard(int x, int y, char direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.inLoop = false;
        this.visitedPath = new HashSet<>();
        this.visitedPath.add(this.x + "," + this.y + "," + this.direction);
    }

    public void move() {
        switch (this.direction) {
            case '<' -> --this.y;
            case '^' -> --this.x;
            case '>' -> ++this.y;
            case 'v' -> ++this.x;
            default -> throw new IllegalArgumentException("Invalid direction");
        }
        checkLoop();
    }

    public void turn() {
        this.direction = switch (this.direction) {
            case '<' -> '^';
            case '^' -> '>';
            case '>' -> 'v';
            case 'v' -> '<';
            default -> throw new IllegalArgumentException("Invalid direction");
        };
    }

    public boolean canAdvance(char[][] matrix) {

        return switch (this.direction) {
                case '<' -> matrix[this.x][this.y - 1] != '#';
                case '^' -> matrix[this.x - 1][this.y] != '#';
                case '>' -> matrix[this.x][this.y + 1] != '#';
                case 'v' -> matrix[this.x + 1][this.y] != '#';
                default -> false;
        };
    }

    public boolean isGoingOutOfBounds(char[][] matrix) {
        return switch (this.direction) {
            case '<' -> this.y == 0;
            case '^' -> this.x == 0;
            case '>' -> this.y == matrix[0].length - 1;
            case 'v' -> this.x == matrix.length - 1;
            default -> false;
        };
    }

    public void checkLoop() {
        boolean inLoop = this.visitedPath.contains(this.x + "," + this.y + "," + this.direction);
        this.visitedPath.add(this.x + "," + this.y + "," + this.direction);
        this.inLoop = inLoop;
    }

    public Set<String> getVisitedPath() {
        return this.visitedPath;
    }

    public boolean isInLoop() {
        return this.inLoop;
    }
}
