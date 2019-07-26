package database;

import database.models.Booking;
import database.models.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class BookingRepository {


  /**
   *
   * @param listingId
   * @param startDate
   * @param endDate
   * @throws NoSuchElementException
   * @throws SQLException
   */
  public static void booking(String listingID, String startDate, String
      endDate, User user) throws NoSuchElementException, SQLException {

    SQLController sqlController = SQLController.getInstance();

        /*
        GETS the AVAILABILTY the user chose from the list, if it does not
        exist does not book anything.
         */
    String statementString = String.join(System.getProperty("line.separator"),
        "",
        "SELECT",
        "    ListingID, StartDate, EndDate",
        "FROM",
        "    Availability",
        "WHERE",
        "    Availability.Listing = ?",
        "    AND Availability.StartDate = ?",
        "    AND Availability.EndDate = ?",
        ";");
    PreparedStatement getAvailabilityStatement = sqlController
        .prepareStatement(statementString);
    getAvailabilityStatement.setString(1, listingID);
    getAvailabilityStatement.setString(2, startDate);
    getAvailabilityStatement.setString(3, endDate);
    ResultSet resultSet = getAvailabilityStatement.executeQuery();

    /*
    If element doesn't exist it wont book anything for the user
     */
    if (!(resultSet.next())) {
      throw new NoSuchElementException();
    }

    resultSet.first();

    // Finally, we insert the User's Country and PostalCode and in into the
    // relation table, so tha we can find the reference for the User table.
    statementString = String.join(System.getProperty("line.separator"),
        "",
        "INSERT INTO",
        "   Booking(ListingID, StartDate, EndDate, SIN, Cancelled)",
        "VALUES",
        "   (?, ?, ?, ?, ?)",
        ";");
    PreparedStatement insertBookingStatement = sqlController
        .prepareStatement(statementString);
    insertBookingStatement.setString(1, listingID);
    insertBookingStatement.setString(2, startDate);
    insertBookingStatement.setString(3, endDate);
    insertBookingStatement.setString(4, user.getSin());
    insertBookingStatement.setBoolean(5, false);
    insertBookingStatement.executeUpdate();


  }

  /**
   *
   * @param listingID
   * @param startDate
   * @param endDate
   * @param user
   * @throws NoSuchElementException
   * @throws SQLException
   */
  public static Booking getBooking(String listingID, String startDate, String
      endDate, User user) throws NoSuchElementException, SQLException {

    SQLController sqlController = SQLController.getInstance();

    Booking booking = null;
        /*
        GETS the BOOKING the user chose from the list, if it does not
        exist returns null.
         */
    String statementString = String.join(System.getProperty("line.separator"),
        "",
        "SELECT",
        "    ListingID, StartDate, EndDate, SIN, Cancelled",
        "FROM",
        "    Booking",
        "WHERE",
        "    Booking.Listing = ?",
        "    AND Booking.StartDate = ?",
        "    AND Booking.EndDate = ?",
        "    AND Booking.SIN = ?",
        ";");
    PreparedStatement getBookingStatement = sqlController
        .prepareStatement(statementString);
    getBookingStatement.setString(1, listingID);
    getBookingStatement.setString(2, startDate);
    getBookingStatement.setString(3, endDate);
    getBookingStatement.setString(4, user.getSin());
    ResultSet resultSet = getBookingStatement.executeQuery();

    /*
    If element doesn't exist it wont populate
     */
    if (!(resultSet.next())) {
      throw new NoSuchElementException();
    } else {
      booking = new Booking();
      resultSet.first();
      booking.setListingID(resultSet.getString("ListingID"));
      booking.setStartDate(resultSet.getTimestamp("StartDate"));
      booking.setEndDate(resultSet.getTimestamp("EndDate"));
      booking.setSin(resultSet.getString("SIN"));
      booking.setCancelled(resultSet.getBoolean("Cancelled"));
    }

    return booking;
  }

  /**
   *
   * @param listingID
   * @param startDate
   * @param endDate
   * @param user
   * @return
   * @throws NoSuchElementException
   * @throws SQLException
   */
  public static void cancelBooking(String listingID, String startDate, String
      endDate, User user) throws NoSuchElementException, SQLException {

    SQLController sqlController = SQLController.getInstance();

    Booking booking = null;
    try {
      booking = BookingRepository.getBooking(listingID, startDate,
          endDate, user);
    } catch (NoSuchElementException exception) {
      throw new NoSuchElementException();
    }

    if booking.equals(null) {
      throw new NoSuchElementException();
    }

        /*
          UPDATES the table to indicate the booking was cancelled.
         */
    String statementString = String.join(System.getProperty("line.separator"),
        "",
        "UPDATE",
        "    Booking",
        "SET",
        "    Cancelled = ?",
        "WHERE",
        "    Booking.Listing = ?",
        "    AND Booking.StartDate = ?",
        "    AND Booking.EndDate = ?",
        "    AND Booking.SIN = ?",
        ";");
    PreparedStatement updateBookingStatement = sqlController
        .prepareStatement(statementString);
    updateBookingStatement.setBoolean(1, true);
    updateBookingStatement.setString(2, booking.getListingID());
    updateBookingStatement.setTimestamp(3, booking.getStartDate());
    updateBookingStatement.setTimestamp(4, booking.getEndDate());
    updateBookingStatement.setString(5, booking.getSin());
    ResultSet resultSet = updateBookingStatement.executeQuery();
  }


  /**
   *
   * @param listingID
   * @param startDate
   * @param endDate
   * @param user
   * @throws NoSuchElementException
   * @throws SQLException
   */
  public static void deleteBooking(String listingID, String startDate, String
      endDate, User user) throws NoSuchElementException, SQLException {

    SQLController sqlController = SQLController.getInstance();

    Booking booking = null;
    try {
      booking = BookingRepository.getBooking(listingID, startDate,
          endDate, user);
    } catch (NoSuchElementException exception) {
      throw new NoSuchElementException();
    }

    if booking.equals(null) {
      throw new NoSuchElementException();
    }

        /*
          DELETES the table to indicate the booking was cancelled.
         */
    String statementString = String.join(System.getProperty("line.separator"),
        "",
        "DELETE FROM",
        "    Booking",
        "WHERE",
        "    Booking.Listing = ?",
        "    AND Booking.StartDate = ?",
        "    AND Booking.EndDate = ?",
        "    AND Booking.SIN = ?",
        ";");
    PreparedStatement updateBookingStatement = sqlController
        .prepareStatement(statementString);
    updateBookingStatement.setString(1, booking.getListingID());
    updateBookingStatement.setTimestamp(2, booking.getStartDate());
    updateBookingStatement.setTimestamp(3, booking.getEndDate());
    updateBookingStatement.setString(4, booking.getSin());
    ResultSet resultSet = updateBookingStatement.executeQuery();
  }
}