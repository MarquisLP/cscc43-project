package database;

import database.models.Availability;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.NoSuchElementException;

public class AvailabilityRepository {
    /**
     * Change the price of an Availability.
     * @param availability the Availability that will be updated
     * @param newPrice The Availability's new price.
     * @throws SQLException if a parsing error occurs with SQL code
     * @throws NoSuchElementException if the given Availability doesn't exists in the database
     * @throws IllegalArgumentException if the given Availability currently has future, non-cancelled bookings
     */
    public static void updateAvailabilityPrice(Availability availability, int newPrice)
            throws SQLException, NoSuchElementException, IllegalArgumentException {
        SQLController sqlController = SQLController.getInstance();
        String statementString = String.join(System.getProperty("line.separator"),
                "",
                "UPDATE",
                "    Availability",
                "SET",
                "    Price = ?",
                "WHERE",
                "    ListingID = ?",
                "    AND StartDate = ?",
                "    AND EndDate = ?",
                ";");
        PreparedStatement getLocatedAtStatement = sqlController.prepareStatement(statementString);
        getLocatedAtStatement.setInt(1, newPrice);
        getLocatedAtStatement.setString(2, availability.getListingId());
        getLocatedAtStatement.setTimestamp(3, availability.getStartDate());
        getLocatedAtStatement.setTimestamp(4, availability.getEndDate());

        int numRowsUpdated = 0;
        try {
            numRowsUpdated = getLocatedAtStatement.executeUpdate();
        }
        catch (SQLException exception) {
            if (exception.getMessage().equals("REJECTED")) {
                throw new IllegalArgumentException();
            }
            else {
                throw exception;
            }
        }
        if (numRowsUpdated == 0) {
            throw new NoSuchElementException();
        }
    }
}
