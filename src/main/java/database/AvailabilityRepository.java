package database;

import database.models.Availability;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    /**
     * Update the date range for an Availability.
     * @param availability The Availability that will be updated
     * @param newStartDate The Availability's new starting date
     * @param newEndDate The Availability's new ending date
     * @throws SQLException if a parse error occurs with SQL code
     * @throws NoSuchElementException if the given Availability doesn't exists in the database
     * @throws IllegalArgumentException if the given Availability currently has future, non-cancelled bookings
     * @throws UnsupportedOperationException if newEndDate < newStartDate
     * @throws IndexOutOfBoundsException if the new date range would conflict with an existing Availability for the same listing
     */
    public static void updateAvailabilityDateRange(Availability availability, Timestamp newStartDate, Timestamp newEndDate)
            throws SQLException, NoSuchElementException, IllegalArgumentException, UnsupportedOperationException, IndexOutOfBoundsException {
        if (newStartDate.after(newEndDate)) {
            throw new UnsupportedOperationException();
        }

        SQLController sqlController = SQLController.getInstance();
        String statementString = String.join(System.getProperty("line.separator"),
                "",
                "SELECT",
                "    *",
                "FROM",
                "    Availability",
                "WHERE",
                "    ListingID = ?",
                "    AND ((StartDate <> ?) AND (EndDate <> ?))",
                "    AND (((StartDate >= ?) AND (StartDate <= ?)) OR ((EndDate <= ?) AND (EndDate >= ?))",
                "         OR ((StartDate <= ?) AND (EndDate >= ?)) OR ((EndDate >= ?) AND (StartDate <= ?)))",
                ";");
        PreparedStatement getAvailabilitiesStatement = sqlController.prepareStatement(statementString);
        getAvailabilitiesStatement.setString(1, availability.getListingId());
        getAvailabilitiesStatement.setTimestamp(2, availability.getStartDate());
        getAvailabilitiesStatement.setTimestamp(3, availability.getEndDate());
        getAvailabilitiesStatement.setTimestamp(4, newStartDate);
        getAvailabilitiesStatement.setTimestamp(5, newEndDate);
        getAvailabilitiesStatement.setTimestamp(6, newEndDate);
        getAvailabilitiesStatement.setTimestamp(7, newStartDate);
        getAvailabilitiesStatement.setTimestamp(8, newStartDate);
        getAvailabilitiesStatement.setTimestamp(9, newStartDate);
        getAvailabilitiesStatement.setTimestamp(10, newEndDate);
        getAvailabilitiesStatement.setTimestamp(11, newEndDate);
        ResultSet getAvailabilitiesResults = getAvailabilitiesStatement.executeQuery();
        if (getAvailabilitiesResults.next()) {
            throw new IndexOutOfBoundsException();
        }

        statementString = String.join(System.getProperty("line.separator"),
                "",
                "UPDATE",
                "    Availability",
                "SET",
                "    StartDate = ?",
                "    , EndDate = ?",
                "WHERE",
                "    ListingID = ?",
                "    AND StartDate = ?",
                "    AND EndDate = ?",
                ";");
        PreparedStatement updateAvailabilityStatement = sqlController.prepareStatement(statementString);
        updateAvailabilityStatement.setTimestamp(1, newStartDate);
        updateAvailabilityStatement.setTimestamp(2, newEndDate);
        updateAvailabilityStatement.setString(3, availability.getListingId());
        updateAvailabilityStatement.setTimestamp(4, availability.getStartDate());
        updateAvailabilityStatement.setTimestamp(5, availability.getEndDate());

        int numRowsUpdated = 0;
        try {
            numRowsUpdated = updateAvailabilityStatement.executeUpdate();
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

    /**
     * Creates a new Availability.
     * @param availability The Availability that will be created
     * @throws SQLException if there is a parse error in the SQL code
     * @throws UnsupportedOperationException if availability's start date is later than its end date
     * @throws IndexOutOfBoundsException if availability's date range would conflict with an existing Availability for the same listing
     */
    public static void createAvailability(Availability availability)
            throws SQLException, UnsupportedOperationException, IndexOutOfBoundsException {
        if (availability.getStartDate().after(availability.getEndDate())) {
            throw new UnsupportedOperationException();
        }
        SQLController sqlController = SQLController.getInstance();
        String statementString = String.join(System.getProperty("line.separator"),
                "",
                "SELECT",
                "    *",
                "FROM",
                "    Availability",
                "WHERE",
                "    ListingID = ?",
                "    AND (((StartDate >= ?) AND (StartDate <= ?)) OR ((EndDate <= ?) AND (EndDate >= ?))",
                "         OR ((StartDate <= ?) AND (EndDate >= ?)) OR ((EndDate >= ?) AND (StartDate <= ?)))",
                ";");
        PreparedStatement getAvailabilitiesStatement = sqlController.prepareStatement(statementString);
        getAvailabilitiesStatement.setString(1, availability.getListingId());
        getAvailabilitiesStatement.setTimestamp(2, availability.getStartDate());
        getAvailabilitiesStatement.setTimestamp(3, availability.getEndDate());
        getAvailabilitiesStatement.setTimestamp(4, availability.getEndDate());
        getAvailabilitiesStatement.setTimestamp(5, availability.getStartDate());
        getAvailabilitiesStatement.setTimestamp(6, availability.getStartDate());
        getAvailabilitiesStatement.setTimestamp(7, availability.getStartDate());
        getAvailabilitiesStatement.setTimestamp(8, availability.getEndDate());
        getAvailabilitiesStatement.setTimestamp(9, availability.getEndDate());
        ResultSet getAvailabilitiesResults = getAvailabilitiesStatement.executeQuery();
        if (getAvailabilitiesResults.next()) {
            throw new IndexOutOfBoundsException();
        }

        statementString = String.join(System.getProperty("line.separator"),
                "",
                "INSERT INTO",
                "    Availability",
                "       (ListingID, StartDate, EndDate, Price)",
                "    VALUES",
                "       (?, ?, ?, ?)",
                ";");
        PreparedStatement insertAvailabilityStatement = sqlController.prepareStatement(statementString);
        insertAvailabilityStatement.setString(1, availability.getListingId());
        insertAvailabilityStatement.setTimestamp(2, availability.getStartDate());
        insertAvailabilityStatement.setTimestamp(3, availability.getEndDate());
        insertAvailabilityStatement.setInt(4, availability.getPrice());
        insertAvailabilityStatement.executeUpdate();
    }
}
