package database;

import database.models.Booking;
import database.models.Renter;
import database.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class RenterRepository {

  public static void createRenter(Renter newRenter) throws SQLException {
    User newUser = new User(newRenter.getSin(), newRenter.getName(),
        newRenter.getDateOfBirth(), newRenter.getOccupation(),
        newRenter.getAddress());

    //INSERTS into USER table
    UserRepository.createUser(newUser);

    //INSERTS into RENTER table
    String statementString = String.join(System.getProperty("line.separator"),
        "",
        "INSERT INTO",
        "    Renter(SIN, CreditCardNumber)",
        "VALUES",
        "    (?, ?)",
        ";");
    SQLController sqlController = SQLController.getInstance();
    PreparedStatement insertHostStatement = sqlController
        .prepareStatement(statementString.toString());
    insertHostStatement.setString(1, newRenter.getSin());
    insertHostStatement.setString(2, newRenter.getCreditCardNumber());
    insertHostStatement.executeUpdate();
  }

  public static void deleteRenterAccount(Renter renter) throws SQLException, NoSuchElementException {
    String statementString = String.join(System.getProperty("line.separator"),
            "",
            "DELETE FROM",
            "    User",
            "WHERE",
            "    SIN = ?",
            ";");
    SQLController sqlController = SQLController.getInstance();
    PreparedStatement deleteUserStatement = sqlController.prepareStatement(statementString);
    deleteUserStatement.setString(1, renter.getSin());

    int numRowsDeleted = 0;
    numRowsDeleted = deleteUserStatement.executeUpdate();
    if (numRowsDeleted == 0) {
      throw new NoSuchElementException();
    }
  }

}
