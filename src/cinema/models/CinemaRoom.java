package cinema.models;

import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class CinemaRoom {
    private List<Seat> availableSeats;
    private int totalRows;
    private int totalColumns;

    public CinemaRoom(int totalRows, int totalColumns, List<Seat> availableSeats) {
        this.totalRows = totalRows;
        this.totalColumns = totalColumns;
        this.availableSeats = availableSeats;
    }

    public String toJson() {
        JsonArray availableSeatsArray = new JsonArray();
        for (int row = 1; row <= 9;row++) {
            for (int column = 1; column <= 9; column++) {
                JsonObject seatObject = new JsonObject();
                seatObject.addProperty("row", row);
                seatObject.addProperty("column", column);
                availableSeatsArray.add(seatObject);
            }
        }
        JsonObject jsonObject = new JsonObject();
        return new Gson().toJson(jsonObject);
    }
}
