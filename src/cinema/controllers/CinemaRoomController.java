package cinema.controllers;

import cinema.models.Seat;
import cinema.models.Ticket;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class CinemaRoomController {
    List<Ticket> tickets = new ArrayList<>();
    List<Seat> availableSeats = listGeneralSeats();
    int current_income = 0;
    int number_of_available_seats = 81;
    int number_of_purchased_tickets = 0;

    @GetMapping("/seats")
    public Map getSeats() {
        List<Seat> availableSeats = listGeneralSeats();
        return Map.of("total_rows", 9, "total_columns", 9, "available_seats", availableSeats);
    }

    public List listGeneralSeats() {
        List<Seat> availableSeats = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                availableSeats.add(new Seat(i, j));
            }
        }
        return availableSeats;
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> getTicket(@RequestBody Seat body) {
        if (body.getRow() < 1 || body.getRow() > 9 || body.getColumn() < 1 || body.getColumn() > 9) {
            return new ResponseEntity<>(Map.of("error", "The number of a row or a column is out of bounds!"), HttpStatus.BAD_REQUEST);
        }
        for (Seat seat : availableSeats) {
            if (seat.getRow() == body.getRow() && seat.getColumn() == body.getColumn()) {
                Ticket ticket = new Ticket(UUID.randomUUID().toString(), seat);
                tickets.add(ticket);
                availableSeats.remove(seat);
                number_of_available_seats--;
                number_of_purchased_tickets++;
                current_income += ticket.getTicket().getPrice();
                return new ResponseEntity<>(ticket, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(Map.of("error", "The ticket has been already purchased!"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnTicket(@RequestBody Ticket body) {
        for (Ticket ticket : tickets) {
            if (ticket.getToken().equals(body.getToken())) {
                availableSeats.add(ticket.getTicket());
                tickets.remove(ticket);
                number_of_available_seats++;
                number_of_purchased_tickets--;
                current_income -= ticket.getTicket().getPrice();
                return new ResponseEntity<>(Map.of("returned_ticket", ticket.getTicket()), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(Map.of("error", "Wrong token!"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/stats")
    public ResponseEntity<?> returnStats(@RequestParam(required = false) String password) {
        if (!"super_secret".equals(password)) {
            return new ResponseEntity<>(Map.of("error", "The password is wrong!"), HttpStatus.valueOf(401));
        }
        return new ResponseEntity<>(Map.of("current_income", current_income, "number_of_available_seats", number_of_available_seats, "number_of_purchased_tickets", number_of_purchased_tickets), HttpStatus.OK);
    }
}
