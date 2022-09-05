package cinema;

import cinema.exceptions.PasswordException;
import cinema.exceptions.TicketException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@SpringBootApplication
public class Main {
    Theatre theatre = new Theatre(9, 9);

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @GetMapping("/seats")
    public Theatre getSeats() {
        return theatre;
    }

    @PostMapping("/stats")
    public Map<String, Integer> getStatistics(@RequestParam(defaultValue = "") String password) {
        if (!password.equals("super_secret"))
            throw new PasswordException("The password is wrong!");
        HashMap<String, Integer> map = new HashMap<>();
        map.put("current_income", theatre.getCurrentIncome());
        map.put("number_of_available_seats", theatre.getAvailableSeatsCount());
        map.put("number_of_purchased_tickets", theatre.getPurchasedSeatsCount());
        return map;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({TicketException.class})
    public String error(TicketException e) {
        return "{\n" + "    \"error\": \"" + e.getMessage() + "\"\n" + "}";
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(PasswordException.class)
    public String passwordException(PasswordException e) {
        return "{\n" + "    \"error\": \"" + e.getMessage() + "\"\n" + "}";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ArrayIndexOutOfBoundsException.class})
    public String outOfbounds() {
        return "{\n" + "    \"error\": \"The number of a row or a column is out of bounds!\"\n" + "}";
    }

    @PostMapping("/purchase")
    public Map<String, Object> purchaseSeat(@RequestBody Map<String, Integer> body) {
        int row = body.get("row");
        int column = body.get("column");
        HashMap<String, Object> map = new HashMap<>();
        UUID uuid = UUID.randomUUID();
        map.put("ticket", theatre.purchase(row, column, uuid));
        map.put("token", uuid);
        return map;
    }

    @PostMapping("/return")
    public Map<String, Object> returnSeat(@RequestBody Map<String, String> body) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("returned_ticket", theatre.refundSeat(UUID.fromString(body.get("token"))));
        return map;
    }

}
