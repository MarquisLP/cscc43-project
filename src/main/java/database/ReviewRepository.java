package database;

import database.models.Renter;
import database.models.User;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

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
      comment, int rating) throws SQLException, IllegalAccessException {

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




}
