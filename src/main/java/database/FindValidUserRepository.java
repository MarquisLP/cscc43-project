package database;

import database.models.HostRenterListingTuples;
import database.models.Listing;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class FindValidUserRepository {

  public static List<HostRenterListingTuples> getRentersUsingHostSIN(String
      hostSin) throws NoSuchElementException, SQLException {
    SQLController sqlController = SQLController.getInstance();

    List<HostRenterListingTuples> allTuples = new ArrayList<>();
        /*
        GETS intersecion of the users that have rented from each other
         */
    String statementString = String.join(System.getProperty("line.separator"),
        "",
        "SELECT",
        "    *",
        "FROM",
        "    HostedBy AS h",
        "    INNER_JOIN Listing AS l",
        "    INNER JOIN Availability AS a",
        "    INNER JOIN Booking AS b",
        "WHERE",
        "    b.Cancelled = 0",
        "    AND h.SIN = ?",
        "    AND Availability.EndDate = ?",
        ";");
    PreparedStatement getAvailabilityStatement = sqlController
        .prepareStatement(statementString);
    getAvailabilityStatement.setString(1, hostSin);
    ResultSet resultSet = getAvailabilityStatement.executeQuery();

    while (resultSet.next()) {
      HostRenterListingTuples thetuple = new HostRenterListingTuples();
      thetuple.setRenterSin(resultSet.getString("b.SIN"));
      thetuple.setListingId(resultSet.getString("l.ListingID"));
      thetuple.setHostSin(hostSin);
      allTuples.add(thetuple);
    }

    return allTuples;

  }

}
