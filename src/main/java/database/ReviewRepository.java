package database;

import database.models.Renter;
import database.models.User;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReviewRepository {
  /*
  in jdbc throws sql exception but message rejected. catch error sql error
  and check exception.getmessage = rejected
  if it does = throw a different exception like different argument exception
  then you can give proper error exceptions
  if the exception message isnt rejected then throw ooriginal
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
        insertListingReviewStatement.setNull(3);
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
}
