package database;

import database.models.Listing;
import database.models.ListingReview;
import database.models.Renter;
import database.models.User;
import database.models.UserReview;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ReviewRepository {
  /*
  in jdbc throws sql exception but message rejected. catch error sql error
  and check exception.getmessage = rejected
  if it does = throw a different exception like different argument exception
  then you can give proper error exceptions
  if the exception message isnt rejected then throw ooriginal
  */

  /**
   *
   * @param listingID
   * @param sin
   * @param comment
   * @param rating
   * @throws SQLException
   * @throws IllegalAccessException
   */
  public static void createListingReview(String listingID, String sin, String
      comment, int rating) throws SQLException, IllegalAccessException {

    //INSERTS into LISTINGREVIEW Table
    try {
      String statementString = String.join(System.getProperty("line.separator"),
          "",
          "INSERT INTO",
          "    ListingReview (ListingID, SIN, Comment, Rating)",
          "VALUES",
          "    (?, ?, ?, ?)",
          ";");
      SQLController sqlController = SQLController.getInstance();
      PreparedStatement insertListingReviewStatement = sqlController
          .prepareStatement(statementString.toString());
      insertListingReviewStatement.setString(1, listingID);
      insertListingReviewStatement.setString(2, sin);
      if (comment.equals("")) {
        insertListingReviewStatement.setNull(3, Types.VARCHAR);
      } else {
        insertListingReviewStatement.setString(3, comment);
      }
      insertListingReviewStatement.setInt(4, rating);
      insertListingReviewStatement.executeUpdate();
    } catch (SQLException exception) {
      if (exception.getMessage().equals("REJECTED")) {
        throw new IllegalArgumentException("Must leave review for those you "
            + "have booked and stayed with");
      } else {
        exception.printStackTrace();
      }
    }
  }

  /**
   *
   * @param hostSin
   * @param renterSin
   * @param comment
   * @param rating
   * @throws SQLException
   * @throws IllegalAccessException
   */
  public static void createHostReview(String hostSin, String renterSin, String
      comment, int rating) throws SQLException, IllegalAccessException {

    //INSERTS into HOSTREVIEW Table
    try {
      String statementString = String.join(System.getProperty("line.separator"),
          "",
          "INSERT INTO",
          "    HostReview (HostSIN, RenterSIN, Comment, Rating)",
          "VALUES",
          "    (?, ?, ?, ?)",
          ";");
      SQLController sqlController = SQLController.getInstance();
      PreparedStatement insertHostReviewStatement = sqlController
          .prepareStatement(statementString.toString());
      insertHostReviewStatement.setString(1, hostSin);
      insertHostReviewStatement.setString(2, renterSin);
      if (comment.equals("")) {
        insertHostReviewStatement.setNull(3, Types.VARCHAR);
      } else {
        insertHostReviewStatement.setString(3, comment);
      }
      insertHostReviewStatement.setInt(4, rating);
      insertHostReviewStatement.executeUpdate();
    } catch (SQLException exception) {
      if (exception.getMessage().equals("REJECTED")) {
        throw new IllegalArgumentException("Must leave review for those you "
            + "have booked and stayed with");
      } else {
        exception.printStackTrace();
      }
    }
  }


  /**
   *
   * @param hostSin
   * @param renterSin
   * @param comment
   * @param rating
   * @throws SQLException
   * @throws IllegalAccessException
   */
  public static void createRenterReview(String hostSin, String renterSin, String
      comment, int rating) throws SQLException, IllegalArgumentException {

    //INSERTS into RENTERREVIEW Table
    try {
      String statementString = String.join(System.getProperty("line.separator"),
          "",
          "INSERT INTO",
          "    RenterReview (HostSIN, RenterSIN, Comment, Rating)",
          "VALUES",
          "    (?, ?, ?, ?)",
          ";");
      SQLController sqlController = SQLController.getInstance();
      PreparedStatement insertRenterReviewStatement = sqlController
          .prepareStatement(statementString.toString());
      insertRenterReviewStatement.setString(1, hostSin);
      insertRenterReviewStatement.setString(2, renterSin);
      if (comment.equals("")) {
        insertRenterReviewStatement.setNull(3, Types.VARCHAR);
      } else {
        insertRenterReviewStatement.setString(3, comment);
      }
      insertRenterReviewStatement.setInt(4, rating);
      insertRenterReviewStatement.executeUpdate();
    } catch (SQLException exception) {
      if (exception.getMessage().equals("REJECTED")) {
        throw new IllegalArgumentException("Must leave review for those you "
            + "have booked and stayed with");
      } else {
        exception.printStackTrace();
      }
    }
  }


  /**
   *
   * @param listingID
   * @return
   * @throws SQLException
   * @throws IllegalAccessException
   */
  public static List<ListingReview> getAllLsitingReviewsByListingId(String
      listingID) throws SQLException, IllegalAccessException {

    //SELECTS all LISTINGREVIEW from Table that match the listing ID provided
    SQLController sqlController = SQLController.getInstance();
    List<ListingReview> listingReviews = new ArrayList<>();

    String statementString = String.join(System.getProperty("line.separator"),
        "",
        "SELECT",
        "    ListingID, SIN, Comment, Rating",
        "FROM",
        "    ListingReview",
        "WHERE",
        "    ListingReview.ListingID = ?",
        ";");
    PreparedStatement getBookingStatement = sqlController
        .prepareStatement(statementString);
    getBookingStatement.setString(1, listingID);
    ResultSet resultSet = getBookingStatement.executeQuery();

    while (resultSet.next()) {
      ListingReview review = new ListingReview();
      review.setListingID(listingID);
      review.setRenterSin(resultSet.getString("SIN"));
      review.setComment(resultSet.getString("Comment"));
      review.setRating(resultSet.getInt("Rating"));

      listingReviews.add(review);
    }

    return listingReviews;
  }

  public static List<ListingReview> getAllListingReviewsByRenterSin(String
      renterSin) throws SQLException, IllegalAccessException {

    //SELECTS all LISTINGREVIEW from Table that match the listing ID provided
    SQLController sqlController = SQLController.getInstance();
    List<ListingReview> listingReviews = new ArrayList<>();

    String statementString = String.join(System.getProperty("line.separator"),
        "",
        "SELECT",
        "    ListingID, SIN, Comment, Rating",
        "FROM",
        "    ListingReview",
        "WHERE",
        "    ListingReview.SIN = ?",
        ";");
    PreparedStatement getBookingStatement = sqlController
        .prepareStatement(statementString);
    getBookingStatement.setString(1, renterSin);
    ResultSet resultSet = getBookingStatement.executeQuery();

    while (resultSet.next()) {
      ListingReview review = new ListingReview();
      review.setListingID(resultSet.getString("ListingID"));
      review.setRenterSin(renterSin);
      review.setComment(resultSet.getString("Comment"));
      review.setRating(resultSet.getInt("Rating"));

      listingReviews.add(review);
    }

    return listingReviews;
  }


  /**
   *
   * @param hostsin
   * @return
   * @throws SQLException
   * @throws IllegalAccessException
   */
  public static List<UserReview> getAllHostReviewsByHostSin(String hostsin)
      throws SQLException {

    //SELECTS all LISTINGREVIEW from Table that match the listing ID provided
    SQLController sqlController = SQLController.getInstance();
    List<UserReview> userReviews = new ArrayList<>();

    String statementString = String.join(System.getProperty("line.separator"),
        "",
        "SELECT",
        "    HostSIN, RenterSIN, Comment, Rating",
        "FROM",
        "    HostReview",
        "WHERE",
        "    HostReview.HostSIN = ?",
        ";");
    PreparedStatement getBookingStatement = sqlController
        .prepareStatement(statementString);
    getBookingStatement.setString(1, hostsin);
    ResultSet resultSet = getBookingStatement.executeQuery();

    while (resultSet.next()) {
      UserReview review = new UserReview();
      review.setHostSin(hostsin);
      review.setRenterSin(resultSet.getString("RenterSIN"));
      review.setComment(resultSet.getString("Comment"));
      review.setRating(resultSet.getInt("Rating"));

      userReviews.add(review);
    }

    return userReviews;
  }


  public static List<UserReview> getAllHostReviewsByRenterSin(String renterSin)
      throws SQLException, IllegalAccessException {

    //SELECTS all LISTINGREVIEW from Table that match the listing ID provided
    SQLController sqlController = SQLController.getInstance();
    List<UserReview> userReviews = new ArrayList<>();

    String statementString = String.join(System.getProperty("line.separator"),
        "",
        "SELECT",
        "    HostSIN, RenterSIN, Comment, Rating",
        "FROM",
        "    HostReview",
        "WHERE",
        "    HostReview.RenterSIN = ?",
        ";");
    PreparedStatement getBookingStatement = sqlController
        .prepareStatement(statementString);
    getBookingStatement.setString(1, renterSin);
    ResultSet resultSet = getBookingStatement.executeQuery();

    while (resultSet.next()) {
      UserReview review = new UserReview();
      review.setHostSin(resultSet.getString("HostSIN"));
      review.setRenterSin(renterSin);
      review.setComment(resultSet.getString("Comment"));
      review.setRating(resultSet.getInt("Rating"));

      userReviews.add(review);
    }

    return userReviews;
  }


  /**
   *
   * @param renterSin
   * @return
   * @throws SQLException
   * @throws IllegalAccessException
   */
  public static List<UserReview> getAllRenterReviewsByRenterSin(String
      renterSin) throws SQLException, IllegalAccessException {

    //SELECTS all LISTINGREVIEW from Table that match the listing ID provided
    SQLController sqlController = SQLController.getInstance();
    List<UserReview> userReviews = new ArrayList<>();

    String statementString = String.join(System.getProperty("line.separator"),
        "",
        "SELECT",
        "    HostSIN, RenterSIN, Comment, Rating",
        "FROM",
        "    RenterReview",
        "WHERE",
        "    RenterReview.RenterSIN = ?",
        ";");
    PreparedStatement getBookingStatement = sqlController
        .prepareStatement(statementString);
    getBookingStatement.setString(1, renterSin);
    ResultSet resultSet = getBookingStatement.executeQuery();

    while (resultSet.next()) {
      UserReview review = new UserReview();
      review.setHostSin(resultSet.getString("HostSIN"));
      review.setRenterSin(renterSin);
      review.setComment(resultSet.getString("Comment"));
      review.setRating(resultSet.getInt("Rating"));

      userReviews.add(review);
    }

    return userReviews;
  }


  /**
   *
   * @param renterSin
   * @return
   * @throws SQLException
   * @throws IllegalAccessException
   */
  public static List<UserReview> getAllRenterReviewsByHostSin(String
      hostSin) throws SQLException, IllegalAccessException {

    //SELECTS all LISTINGREVIEW from Table that match the listing ID provided
    SQLController sqlController = SQLController.getInstance();
    List<UserReview> userReviews = new ArrayList<>();

    String statementString = String.join(System.getProperty("line.separator"),
        "",
        "SELECT",
        "    HostSIN, RenterSIN, Comment, Rating",
        "FROM",
        "    RenterReview",
        "WHERE",
        "    RenterReview.HostSIN = ?",
        ";");
    PreparedStatement getBookingStatement = sqlController
        .prepareStatement(statementString);
    getBookingStatement.setString(1, hostSin);
    ResultSet resultSet = getBookingStatement.executeQuery();

    while (resultSet.next()) {
      UserReview review = new UserReview();
      review.setHostSin(hostSin);
      review.setRenterSin(resultSet.getString("RenterSIN"));
      review.setComment(resultSet.getString("Comment"));
      review.setRating(resultSet.getInt("Rating"));

      userReviews.add(review);
    }

    return userReviews;
  }

}
