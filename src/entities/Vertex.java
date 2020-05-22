package entities;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Vertex {
    private int row;
    private int column;
    private List<Integer> possibleNumbers;

    public Vertex(int row, int column) {
        this.row = row;
        this.column = column;
        possibleNumbers = new LinkedList<>();
        for (int i = 1; i < 9; i++) {
            possibleNumbers.add(i);
        }
    }

    public List<Integer> getPossibleNumbers() {
        return possibleNumbers;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getRandomPossibleNumber() {
        final Random random = new Random();
        int index = random.nextInt(possibleNumbers.size());
        return possibleNumbers.get(index);
}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return row == vertex.row &&
                column == vertex.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }
}
