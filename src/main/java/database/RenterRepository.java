package database;

import database.models.Renter;
import database.models.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RenterRepository {
    public static void createRenter(Renter newRenter) throws SQLException {
        User newUser = new User(newRenter.getSin(), newRenter.getName(),
                newRenter.getDateOfBirth(), newRenter.getOccupation(), newRenter.getAddress());
        UserRepository.createUser(newUser);

        String statementString = String.join(System.getProperty("line.separator"),
                "",
                "INSERT INTO",
                "    Renter(SIN, CreditCardNumber)",
                "VALUES",
                "    (?, ?)",
                ";");
        SQLController sqlController = SQLController.getInstance();
        PreparedStatement insertHostStatement = sqlController.prepareStatement(statementString.toString());
        insertHostStatement.setString(1, newRenter.getSin());
        insertHostStatement.setString(2, newRenter.getCreditCardNumber());
        insertHostStatement.executeUpdate();
    }
}
