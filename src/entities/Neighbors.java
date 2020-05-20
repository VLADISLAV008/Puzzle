package entities;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.List;

public final class Neighbors {

    public static List<Pair<Integer, Integer>> getNeighbors(Field field, int row, int column) {
        List<Pair<Integer, Integer>> neighbors = new ArrayList<>();
        addNeighbor(field, neighbors, row + 1, column);
        addNeighbor(field, neighbors, row, column + 1);
        addNeighbor(field, neighbors, row - 1, column);
        addNeighbor(field, neighbors, row, column - 1);
        addNeighbor(field, neighbors, row + 1, column + 1);
        addNeighbor(field, neighbors, row - 1, column + 1);
        addNeighbor(field, neighbors, row - 1, column - 1);
        addNeighbor(field, neighbors, row + 1, column - 1);
        return neighbors;
    }

    private static void addNeighbor(Field field, List<Pair<Integer, Integer>> list, int row, int column) {
        if (row >= 0 && column >= 0 && row < field.getCountRows() && column < field.getCountColumns()) {
            list.add(new Pair<>(row, column));
        }
    }
}
