package entities;

import javafx.util.Pair;

import java.util.*;

public final class Generator {
    private static List<Vertex> vertices;
    private static Vertex[][] table;
    private static Field field;

    public static Field generateField(int rows, int columns) {
        initialize(rows, columns);

        Integer[][] board = field.getField();
        while (vertices.size() != 0) {
            Vertex vertex = selectRandomVertex();
            if (vertex.getPossibleNumbers().size() == 0) {
                initialize(rows, columns);
                board = field.getField();
            } else {
                int value = vertex.getRandomPossibleNumber();
                table[vertex.getRow()][vertex.getColumn()] = null;
                board[vertex.getRow()][vertex.getColumn()] = value;

                updateVertices(vertex, value);
                removeSingleVertices();
                removeSingleVariantForNumber();
            }
        }
        return field;
    }

    private static void initialize(int rows, int columns) {
        field = new Field(rows, columns);
        table = new Vertex[rows][columns];
        vertices = new LinkedList<>();

        Integer[][] board = field.getField();
        for (int i = 0; i < field.getCountRows(); i++) {
            for (int j = 0; j < field.getCountColumns(); j++) {
                if (i % 2 == 1 && j % 2 == 1) {
                    board[i][j] = -1;
                    table[i][j] = null;
                } else {
                    board[i][j] = null;
                    Vertex v = new Vertex(i, j);
                    vertices.add(v);
                    table[i][j] = v;
                }
            }
        }
    }

    private static Vertex selectRandomVertex() {
        final Random random = new Random();
        int index = random.nextInt(vertices.size());
        return vertices.remove(index);
    }

    private static void updateVertices(Vertex vertex, Integer value) {
        List<Pair<Integer, Integer>> neighbors = Neighbors.getNeighbors(field, vertex.getRow(), vertex.getColumn());
        for (Pair<Integer, Integer> cord : neighbors) {
            Integer number = field.getField()[cord.getKey()][cord.getValue()];
            if (number != null && number == -1) {
                List<Pair<Integer, Integer>> ballNeighbors = Neighbors.getNeighbors(field, cord.getKey(), cord.getValue());
                for (Pair<Integer, Integer> coordinates : ballNeighbors) {
                    Vertex v = table[coordinates.getKey()][coordinates.getValue()];
                    if (v != null) {
                        v.getPossibleNumbers().remove(value);
                    }
                }
            }
        }
    }

    private static void removeSingleVertices() {
        boolean notRemove = true;
        while (notRemove) {
            notRemove = false;
            for (Iterator<Vertex> i = vertices.iterator(); i.hasNext(); ) {
                Vertex v = i.next();
                if (v.getPossibleNumbers().size() == 1) {
                    i.remove();
                    table[v.getRow()][v.getColumn()] = null;
                    updateVertices(v, v.getPossibleNumbers().get(0));
                    notRemove = true;
                }
            }
        }
    }

    private static void removeSingleVariantForNumber() {
        boolean notRemove = true;
        while (notRemove) {
            notRemove = false;
            for (int i = 1; i < field.getCountRows(); i += 2) {
                for (int j = 1; j < field.getCountColumns(); j += 2) {
                    Pair<Vertex, Integer> pair = singleVariantForNumber(i, j);
                    if (pair != null) {
                        Vertex vertex = pair.getKey();
                        int value = pair.getValue();
                        vertices.remove(vertex);
                        table[vertex.getRow()][vertex.getColumn()] = null;
                        updateVertices(vertex, value);
                        notRemove = true;
                    }
                }
            }
        }
        removeSingleVertices();
    }

    private static Pair<Vertex, Integer> singleVariantForNumber(int rowBall, int columnBall) {
        List<Pair<Integer, Integer>> neighbors = Neighbors.getNeighbors(field, rowBall, columnBall);
        for (int i = 1; i < 9; i++) {
            int count = 0;
            Vertex v = null;
            for (Pair<Integer, Integer> cord : neighbors) {
                if (table[cord.getKey()][cord.getValue()] != null && table[cord.getKey()][cord.getValue()].getPossibleNumbers().contains(i)) {
                    count++;
                    v = table[cord.getKey()][cord.getValue()];
                }
            }
            if (count == 1) {
                return new Pair<>(v, i);
            }
        }
        return null;
    }
}
