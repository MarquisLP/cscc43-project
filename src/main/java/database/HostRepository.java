package database;

import database.models.Host;
import database.models.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
