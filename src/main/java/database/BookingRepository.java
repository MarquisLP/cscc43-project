package database;

import database.models.Booking;
import database.models.Host;
import database.models.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class BookingRepository {


  /**
   *
   * @param listingID
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
        "    Availability.ListingID = ?",
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
        "    Booking.ListingID = ?",
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
      Timestamp starttime = (resultSet.getTimestamp("StartDate"));
      booking.setStartDate(starttime);
      Timestamp endtime = (resultSet.getTimestamp("EndDate"));
      booking.setEndDate(endtime);
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

    if (booking.equals(null)) {
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
        "    Booking.ListingID = ?",
        "    AND Booking.StartDate = ?",
        "    AND Booking.EndDate = ?",
        ";");
    PreparedStatement updateBookingStatement = sqlController
        .prepareStatement(statementString);
    updateBookingStatement.setBoolean(1, true);
    updateBookingStatement.setString(2, booking.getListingID());
    updateBookingStatement.setTimestamp(3, booking.getStartDate());
    updateBookingStatement.setTimestamp(4, booking.getEndDate());
    updateBookingStatement.setString(5, booking.getSin());
    updateBookingStatement.executeUpdate();
  }


  public static void cancelBookingAsHost(String listingID, String startDate, String
          endDate, Host host) throws NoSuchElementException, IllegalArgumentException, SQLException {

    SQLController sqlController = SQLController.getInstance();

    String statementString = String.join(System.getProperty("line.separator"),
            "",
            "SELECT",
            "    b.ListingID AS ListingID, StartDate, EndDate, b.SIN AS RenterSIN, Cancelled, h.SIN AS HostSIN",
            "FROM",
            "    Booking AS b",
            "    INNER JOIN HostedBy AS h ON b.ListingID = h.ListingID",
            "WHERE",
            "    b.ListingID = ?",
            "    AND b.StartDate = ?",
            "    AND b.EndDate = ?",
            ";");
    PreparedStatement getBookingStatement = sqlController
            .prepareStatement(statementString);
    getBookingStatement.setString(1, listingID);
    getBookingStatement.setString(2, startDate);
    getBookingStatement.setString(3, endDate);
    ResultSet resultSet = getBookingStatement.executeQuery();
    if (!resultSet.next()) {
        throw new NoSuchElementException();
    }
    if (!(resultSet.getString("HostSIN").equals(host.getSin()))) {
      throw new IllegalArgumentException();
    }

    Booking booking = new Booking();
    resultSet.first();
    booking.setListingID(resultSet.getString("ListingID"));
    Timestamp starttime = (resultSet.getTimestamp("StartDate"));
    booking.setStartDate(starttime);
    Timestamp endtime = (resultSet.getTimestamp("EndDate"));
    booking.setEndDate(endtime);
    booking.setSin(resultSet.getString("RenterSIN"));
    booking.setCancelled(resultSet.getBoolean("Cancelled"));

        /*
          UPDATES the table to indicate the booking was cancelled.
         */
    statementString = String.join(System.getProperty("line.separator"),
            "",
            "UPDATE",
            "    Booking",
            "SET",
            "    Cancelled = ?",
            "WHERE",
            "    Booking.ListingID = ?",
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
    updateBookingStatement.executeUpdate();
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

    if(booking.equals(null)) {
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
        "    Booking.ListingID = ?",
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
    updateBookingStatement.executeUpdate();
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
  public static List<Booking> getBookingsByUser(User user) throws
      NoSuchElementException, SQLException {

    SQLController sqlController = SQLController.getInstance();

    List<Booking> allBookings = new ArrayList<>();
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
        "    Booking.SIN = ?",
        "    AND Booking.Cancelled = False",
        ";");
    PreparedStatement getBookingStatement = sqlController
        .prepareStatement(statementString);
    getBookingStatement.setString(1, user.getSin());
    ResultSet resultSet = getBookingStatement.executeQuery();

    while (resultSet.next()) {
      Booking booking = new Booking();
      resultSet.first();
      booking.setListingID(resultSet.getString("ListingID"));
      Timestamp starttime = (resultSet.getTimestamp("StartDate"));
      booking.setStartDate(starttime);
      Timestamp endtime = (resultSet.getTimestamp("EndDate"));
      booking.setEndDate(endtime);
      booking.setSin(resultSet.getString("SIN"));
      booking.setCancelled(resultSet.getBoolean("Cancelled"));
      allBookings.add(booking);
    }

    return allBookings;
  }

  /**
   *
   * @param listingID
   * @return
   * @throws NoSuchElementException
   * @throws SQLException
   */
  public static List<Booking> getBookingsByListing(String listingID) throws
      NoSuchElementException, SQLException {

    SQLController sqlController = SQLController.getInstance();

    List<Booking> allBookings = new ArrayList<>();
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
        "    Booking.ListingID = ?",
        "    AND Booking.Cancelled = False",
        ";");
    PreparedStatement getBookingStatement = sqlController
        .prepareStatement(statementString);
    getBookingStatement.setString(1,listingID);
    ResultSet resultSet = getBookingStatement.executeQuery();

    while (resultSet.next()) {
      Booking booking = new Booking();
      resultSet.first();
      booking.setListingID(resultSet.getString("ListingID"));
      Timestamp starttime = (resultSet.getTimestamp("StartDate"));
      booking.setStartDate(starttime);
      Timestamp endtime = (resultSet.getTimestamp("EndDate"));
      booking.setEndDate(endtime);
      booking.setSin(resultSet.getString("SIN"));
      booking.setCancelled(resultSet.getBoolean("Cancelled"));
      allBookings.add(booking);
    }

    return allBookings;
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
  public static List<Booking> getCancelledBookingsByUser(User user) throws
      NoSuchElementException, SQLException {

    SQLController sqlController = SQLController.getInstance();

    List<Booking> allBookings = new ArrayList<>();
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
        "    Booking.SIN = ?",
        "    AND Booking.Cancelled = True",
        ";");
    PreparedStatement getBookingStatement = sqlController
        .prepareStatement(statementString);
    getBookingStatement.setString(1, user.getSin());
    ResultSet resultSet = getBookingStatement.executeQuery();

    while (resultSet.next()) {
      Booking booking = new Booking();
      resultSet.first();
      booking.setListingID(resultSet.getString("ListingID"));
      Timestamp starttime = (resultSet.getTimestamp("StartDate"));
      booking.setStartDate(starttime);
      Timestamp endtime = (resultSet.getTimestamp("EndDate"));
      booking.setEndDate(endtime);
      booking.setSin(resultSet.getString("SIN"));
      booking.setCancelled(resultSet.getBoolean("Cancelled"));
      allBookings.add(booking);
    }

    return allBookings;
  }

  /**
   *
   * @param listingID
   * @return
   * @throws NoSuchElementException
   * @throws SQLException
   */
  public static List<Booking> getCancelledBookingsByListing(String listingID)
      throws NoSuchElementException, SQLException {

    SQLController sqlController = SQLController.getInstance();

    List<Booking> allBookings = new ArrayList<>();
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
        "    Booking.ListingID = ?",
        "    AND Booking.Cancelled = True",
        ";");
    PreparedStatement getBookingStatement = sqlController
        .prepareStatement(statementString);
    getBookingStatement.setString(1,listingID);
    ResultSet resultSet = getBookingStatement.executeQuery();

    while (resultSet.next()) {
      Booking booking = new Booking();
      resultSet.first();
      booking.setListingID(resultSet.getString("ListingID"));
      Timestamp starttime = (resultSet.getTimestamp("StartDate"));
      booking.setStartDate(starttime);
      Timestamp endtime = (resultSet.getTimestamp("EndDate"));
      booking.setEndDate(endtime);
      booking.setSin(resultSet.getString("SIN"));
      booking.setCancelled(resultSet.getBoolean("Cancelled"));
      allBookings.add(booking);
    }

    return allBookings;
  }
}
