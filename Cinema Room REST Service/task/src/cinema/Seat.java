package cinema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Seat {
    @JsonProperty
    private int column;
    @JsonProperty
    private int row;
    @JsonProperty
    private int price;

    public Seat(int row, int column, int price) {
        this.column = column;
        this.row = row;
        this.price = price;
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
