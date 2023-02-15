package cinema.models;

public class Seat {
    private int price;
    private int row;
    private int column;

    public Seat(int row, int column) {
        this.row = row;
        this.column = column;
        this.price = row < 5 ? 10 : 8;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getPrice() {
        return price;
    }
}
