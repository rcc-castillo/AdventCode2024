package challenge12;

import java.util.Objects;
import java.util.Set;

public class GardenPlot {
    private final char plotType;
    private final int posX;
    private final int posY;

    public GardenPlot(char plotType, int posX, int posY) {
        this.plotType = plotType;
        this.posX = posX;
        this.posY = posY;
    }

    public int countAdjacentPlots(Set<GardenPlot> region) {
        return (int) region.stream()
                .filter(plot -> Math.abs(plot.posX - this.posX) == 1 && plot.posY == this.posY ||
                        Math.abs(plot.posY - this.posY) == 1 && plot.posX == this.posX)
                .count();
    }

    public boolean hasLeftNeighbor(Set<GardenPlot> region) {
        return region.contains(new GardenPlot(this.plotType, this.posX - 1, this.posY));
    }

    public boolean hasRightNeighbor(Set<GardenPlot> region) {
        return region.contains(new GardenPlot(this.plotType, this.posX + 1, this.posY));
    }

    public boolean hasTopNeighbor(Set<GardenPlot> region) {
        return region.contains(new GardenPlot(this.plotType, this.posX, this.posY - 1));
    }

    public boolean hasBottomNeighbor(Set<GardenPlot> region) {
        return region.contains(new GardenPlot(this.plotType, this.posX, this.posY + 1));
    }

    public boolean hasTopLeftNeighbor(Set<GardenPlot> region) {
        return region.contains(new GardenPlot(this.plotType, this.posX - 1, this.posY - 1));
    }

    public boolean hasTopRightNeighbor(Set<GardenPlot> region) {
        return region.contains(new GardenPlot(this.plotType, this.posX + 1, this.posY - 1));
    }

    public boolean hasBottomLeftNeighbor(Set<GardenPlot> region) {
        return region.contains(new GardenPlot(this.plotType, this.posX - 1, this.posY + 1));
    }

    public boolean hasBottomRightNeighbor(Set<GardenPlot> region) {
        return region.contains(new GardenPlot(this.plotType, this.posX + 1, this.posY + 1));
    }

    public boolean inBounds(char[][] matrix) {
        return this.posX >= 0 && this.posX < matrix.length && this.posY >= 0 && this.posY <matrix[0].length;
    }

    public char getPlotType() {
        return plotType;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GardenPlot that = (GardenPlot) o;
        return plotType == that.plotType && posX == that.posX && posY == that.posY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(plotType, posX, posY);
    }
}
