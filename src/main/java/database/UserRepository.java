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
    public static User getUser(String sin) throws NoSuchElementException, SQLException {
        SQLController sqlController = SQLController.getInstance();

        String statementString = String.join(System.getProperty("line.separator"),
                "",
                "SELECT",
                "    SIN, Name, DateOfBirth, Occupation",
                "FROM",
                "    User",
                "WHERE",
                "    User.SIN = ?",
                ";");
        PreparedStatement getUserStatement = sqlController.prepareStatement(statementString);
        getUserStatement.setString(1, sin);
        ResultSet resultSet = getUserStatement.executeQuery();

        if (!(resultSet.next())) {
            throw new NoSuchElementException();
        }

        resultSet.first();
        User userData = new User(
            resultSet.getString("SIN"),
            resultSet.getString("Name"),
            resultSet.getDate("DateOfBirth"),
            resultSet.getString("Occupation"),
            //TODO: In User table, add foreign key reference to Address
            new Address("", "", "")
        );

        statementString = String.join(System.getProperty("line.separator"),
                "",
                "SELECT",
                "    SIN",
                "FROM",
                "    Host",
                "WHERE",
                "    Host.SIN = ?",
                ";");
        PreparedStatement getHostStatement = sqlController.prepareStatement(statementString);
        getHostStatement.setString(1, sin);
        resultSet = getHostStatement.executeQuery();

        if (resultSet.next()) {
            resultSet.first();
            return new Host(userData.getSin(), userData.getName(), userData.getDateOfBirth(),
                    userData.getOccupation(), userData.getAddress());
        }
        else {
            statementString = String.join(System.getProperty("line.separator"),
                    "",
                    "SELECT",
                    "    SIN, CreditCardNumber",
                    "FROM",
                    "    Renter",
                    "WHERE",
                    "    Renter.SIN = ?",
                    ";");
            PreparedStatement getRenterStatement = sqlController.prepareStatement(statementString);
            getRenterStatement.setString(1, sin);
            resultSet = getRenterStatement.executeQuery();
            resultSet.first();
            return new Renter(userData.getSin(), userData.getName(), userData.getDateOfBirth(),
                    userData.getOccupation(), userData.getAddress(), resultSet.getString("CreditCardNumber"));
        }
    }

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
        PreparedStatement insertAddressStatement = sqlController.prepareStatement(statementString);
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
        PreparedStatement insertUserStatement = sqlController.prepareStatement(statementString);
        insertUserStatement.setString(1, newUser.getSin());
        insertUserStatement.setString(2, newUser.getName());
        if (newUser.getDateOfBirth() == null) {
            insertUserStatement.setNull(3, Types.DATE);
        }
        else {
            insertUserStatement.setDate(3, newUser.getDateOfBirth());
        }
        if (newUser.getOccupation().equals("")) {
            insertUserStatement.setNull(4, Types.VARCHAR);
        }
        else {
            insertUserStatement.setString(4, newUser.getOccupation());
        }
        insertUserStatement.executeUpdate();
    }
}
