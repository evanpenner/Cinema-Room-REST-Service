package cinema;

import cinema.exceptions.TicketException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;


public class Theatre {
    @JsonProperty
    private int totalRows;
    @JsonProperty
    private int totalColumns;
    @JsonProperty
    private List<Seat> availableSeats;

    private HashMap<UUID, Seat> purchasedSeats;

    public Theatre(int rows, int columns) {
        this.totalRows = rows;
        this.totalColumns = columns;
        this.availableSeats = new ArrayList<>();
        this.purchasedSeats = new HashMap<>();
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= columns; j++) {

                Seat seat = new Seat(i, j, i <= 4 ? 10 : 8);
                availableSeats.add(seat);
            }
        }
    }

    @JsonIgnore
    public int getPurchasedSeatsCount() {
        return getPurchasedSeats().size();
    }

    @JsonIgnore
    public int getAvailableSeatsCount() {
        return getAvailableSeats().size();
    }

    @JsonIgnore
    private HashMap<UUID, Seat> getPurchasedSeats() {
        return this.purchasedSeats;
    }

    @JsonIgnore
    public int getCurrentIncome() {
        int totalIncome = 0;
        for (Seat seat : purchasedSeats.values()) {
            totalIncome += seat.getPrice();
        }
        return totalIncome;
    }

    public int getTotalColumns() {
        return totalColumns;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public List<Seat> getAvailableSeats() {
        return availableSeats;
    }

    public Seat purchase(int row, int column, UUID token) {
        if (row > getTotalRows() || column > getTotalColumns() || row <= 0 || column <= 0)
            throw new ArrayIndexOutOfBoundsException();
        for (Seat seat : getAvailableSeats()) {
            if (seat.getRow() == row && seat.getColumn() == column) {
                availableSeats.remove(seat);
                purchasedSeats.put(token, seat);
                return seat;
            }
        }
        throw new TicketException("The ticket has been already purchased!");
    }

    public Seat refundSeat(UUID token) {
        Seat seat = purchasedSeats.get(token);
        purchasedSeats.remove(token);
        if (seat != null) {
            availableSeats.add(seat);
            return seat;
        }
        throw new TicketException("Wrong token!");
    }
}
