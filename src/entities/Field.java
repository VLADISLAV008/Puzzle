package entities;

public class Field {
    private int countRows;
    private int countColumns;
    private Integer[][] field;

    public Field(int countRows, int countColumns) {
        this.countRows = countRows;
        this.countColumns = countColumns;
        field = new Integer[countRows][countColumns];
    }

    public int getCountRows() {
        return countRows;
    }

    public int getCountColumns() {
        return countColumns;
    }

    public Integer[][] getField() {
        return field;
    }
}
