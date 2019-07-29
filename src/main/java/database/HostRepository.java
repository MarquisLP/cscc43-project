package database;

import database.models.Host;
import database.models.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class HostRepository {
    public static void createHost(Host newHost) throws SQLException {
        User newUser = new User(newHost.getSin(), newHost.getName(),
                newHost.getDateOfBirth(), newHost.getOccupation(), newHost.getAddress());
        UserRepository.createUser(newUser);

        String statementString = String.join(System.getProperty("line.separator"),
                "",
                "INSERT INTO",
                "    Host(SIN)",
                "VALUES",
                "    (?)",
                ";");
        SQLController sqlController = SQLController.getInstance();
        PreparedStatement insertHostStatement = sqlController.prepareStatement(statementString.toString());
        insertHostStatement.setString(1, newHost.getSin());
        insertHostStatement.executeUpdate();
    }

    public static void deleteHostAccount(Host host)
            throws SQLException, IllegalArgumentException, NoSuchElementException {
        String statementString = String.join(System.getProperty("line.separator"),
                "",
                "DELETE",
                "   l",
                "FROM",
                "    Listing AS l",
                "    INNER JOIN HostedBy AS h ON h.ListingID = l.ListingID",
                "WHERE",
                "    h.SIN = ?",
                ";");
        SQLController sqlController = SQLController.getInstance();
        PreparedStatement deleteHostedListingsStatement = sqlController.prepareStatement(statementString.toString());
        deleteHostedListingsStatement.setString(1, host.getSin());
        try {
            deleteHostedListingsStatement.executeUpdate();
        } catch (SQLException exception) {
            if (exception.getMessage().equals("REJECTED")) {
                throw new IllegalArgumentException();
            }
            else {
                throw exception;
            }
        }

        statementString = String.join(System.getProperty("line.separator"),
                "",
                "DELETE FROM",
                "    User",
                "WHERE",
                "    SIN = ?",
                ";");
        PreparedStatement deleteUserStatement = sqlController.prepareStatement(statementString.toString());
        deleteUserStatement.setString(1, host.getSin());
        int numRowsDeleted = 0;
        try {
            numRowsDeleted = deleteUserStatement.executeUpdate();
        } catch (SQLException exception) {
            if (exception.getMessage().equals("REJECTED")) {
                throw new IllegalArgumentException();
            }
            else {
                throw exception;
            }
        }
        if (numRowsDeleted == 0) {
            throw new NoSuchElementException();
        }
    }
}
