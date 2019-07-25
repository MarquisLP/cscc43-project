package database;

import database.models.Address;
import database.models.Host;
import database.models.Renter;
import database.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.NoSuchElementException;

public class UserRepository {

  public static User getUser(String sin)
      throws NoSuchElementException, SQLException {
    SQLController sqlController = SQLController.getInstance();


        /*
        This gets from the USER table the user object and address object.
         */
    String statementString = String.join(System.getProperty("line.separator"),
        "",
        "SELECT",
        "    SIN, Name, DateOfBirth, Occupation",
        "FROM",
        "    User",
        "WHERE",
        "    User.SIN = ?",
        ";");
    PreparedStatement getUserStatement = sqlController
        .prepareStatement(statementString);
    getUserStatement.setString(1, sin);
    ResultSet resultSet = getUserStatement.executeQuery();

    if (!(resultSet.next())) {
      throw new NoSuchElementException();
    }


        /*
        Getting the RESIDESAT table and getting the users SINGLE address
         */
    statementString = String.join(System.getProperty("line.separator"),
        "",
        "SELECT",
        "    SIN, Country, PostalCode",
        "FROM",
        "    ResidesAt",
        "WHERE",
        "    ResidesAt.SIN = ?",
        ";");
    PreparedStatement getUserResidesAtStatement = sqlController
        .prepareStatement(statementString);
    getUserResidesAtStatement.setString(1, sin);
    ResultSet resultSetOfResidesAt = getUserResidesAtStatement.executeQuery();

    if (!(resultSetOfResidesAt.next())) {
      throw new NoSuchElementException();
    }

        /*
        GETting the ADDRESS table and getting the users SINGLE address for
        that one USER
         */
    statementString = String.join(System.getProperty("line.separator"),
        "",
        "SELECT",
        "    Country, PostalCode, City",
        "FROM",
        "    Address",
        "WHERE",
        "    Address.Country = ?",
        "    AND Address.PostalCode = ?",
        ";");
    PreparedStatement getUserAddressStatement = sqlController.prepareStatement
        (statementString);
    resultSetOfResidesAt.first();
    getUserAddressStatement.setString(1, resultSetOfResidesAt.getString
        ("Country"));
    getUserAddressStatement.setString(2, resultSetOfResidesAt.getString
        ("PostalCode"));
    ResultSet resultSetOfAddress = getUserAddressStatement.executeQuery();

    if (!(resultSetOfAddress.next())) {
      throw new NoSuchElementException();
    }

    resultSetOfAddress.first();
    Address userAddress = new Address(
        resultSetOfAddress.getString("PostalCode"),
        resultSetOfAddress.getString("City"),
        resultSetOfAddress.getString("Country")
    );

    resultSet.first();
    User userData = new User(
        resultSet.getString("SIN"),
        resultSet.getString("Name"),
        resultSet.getDate("DateOfBirth"),
        resultSet.getString("Occupation"),
        userAddress
    );

    /*
    GETting the HOST from the table, if does not exist then check Renter
     */
    statementString = String.join(System.getProperty("line.separator"),
        "",
        "SELECT",
        "    SIN",
        "FROM",
        "    Host",
        "WHERE",
        "    Host.SIN = ?",
        ";");
    PreparedStatement getHostStatement = sqlController
        .prepareStatement(statementString);
    getHostStatement.setString(1, sin);
    resultSet = getHostStatement.executeQuery();

    if (resultSet.next()) {
      resultSet.first();
      return new Host(userData.getSin(), userData.getName(),
          userData.getDateOfBirth(),
          userData.getOccupation(), userData.getAddress());
    } else {
      /*
       If nothing found from the host then returns the Renter
       */
      statementString = String.join(System.getProperty("line.separator"),
          "",
          "SELECT",
          "    SIN, CreditCardNumber",
          "FROM",
          "    Renter",
          "WHERE",
          "    Renter.SIN = ?",
          ";");
      PreparedStatement getRenterStatement = sqlController
          .prepareStatement(statementString);
      getRenterStatement.setString(1, sin);
      resultSet = getRenterStatement.executeQuery();
      resultSet.first();
      return new Renter(userData.getSin(), userData.getName(),
          userData.getDateOfBirth(),
          userData.getOccupation(), userData.getAddress(),
          resultSet.getString("CreditCardNumber"));
    }
  }

  /**
   * Creating a new User on signup.
   */
  public static void createUser(User newUser) throws SQLException {
    SQLController sqlController = SQLController.getInstance();

    // First, we have to insert the User's Address, so that we can reference it in the User table.
    String statementString = String.join(System.getProperty("line.separator"),
        "",
        "INSERT INTO",
        "   Address(PostalCode, City, Country)",
        "VALUES",
        "   (?, ?, ?)",
        ";");
    PreparedStatement insertAddressStatement = sqlController
        .prepareStatement(statementString);
    insertAddressStatement.setString(1, newUser.getAddress().getPostalCode());
    insertAddressStatement.setString(2, newUser.getAddress().getCity());
    insertAddressStatement.setString(3, newUser.getAddress().getCountry());
    insertAddressStatement.executeUpdate();

    // Now we insert into the User table.
    statementString = String.join(System.getProperty("line.separator"),
        "",
        "INSERT INTO",
        "   User(SIN, Name, DateOfBirth, Occupation)",
        "VALUES",
        "   (?, ?, ?, ?)",
        ";");
    PreparedStatement insertUserStatement = sqlController
        .prepareStatement(statementString);
    insertUserStatement.setString(1, newUser.getSin());
    insertUserStatement.setString(2, newUser.getName());
    if (newUser.getDateOfBirth() == null) {
      insertUserStatement.setNull(3, Types.DATE);
    } else {
      insertUserStatement.setDate(3, newUser.getDateOfBirth());
    }
    if (newUser.getOccupation().equals("")) {
      insertUserStatement.setNull(4, Types.VARCHAR);
    } else {
      insertUserStatement.setString(4, newUser.getOccupation());
    }
    insertUserStatement.executeUpdate();

    // Finally, we insert the User's Country and PostalCode and in into the
    // relation tabele, so tha we can find the reference for the User table.
    statementString = String.join(System.getProperty("line.separator"),
        "",
        "INSERT INTO",
        "   ResidesAt(SIN, Country, PostalCode)",
        "VALUES",
        "   (?, ?, ?)",
        ";");
    PreparedStatement insertResidesAtStatement = sqlController
        .prepareStatement(statementString);
    insertResidesAtStatement.setString(1, newUser.getSin());
    insertResidesAtStatement.setString(2, newUser.getAddress().getCountry());
    insertResidesAtStatement.setString(3, newUser.getAddress().getPostalCode());
    insertResidesAtStatement.executeUpdate();

  }
}
