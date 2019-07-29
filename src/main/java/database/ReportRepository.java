package database;

import database.models.ListingReview;
import database.models.ReportByNumberInCity;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportRepository {

  public static List<ReportByNumberInCity> reportByDateRangeInCity(String startDate, String
      endDate) throws SQLException {

    //SELECTS all LISTINGREVIEW from Table that match the listing ID provided
    SQLController sqlController = SQLController.getInstance();
    List<ReportByNumberInCity> allCities = new ArrayList<>();

    String statementString = String.join(System.getProperty("line.separator"),
        "",
        "SELECT ",
        "    Address.City, COUNT(Address.City) AS Numlistings",
        "FROM",
        "    Listing",
        "    INNER JOIN LocatedAt",
        "    INNER JOIN Address",
        "    INNER JOIN Availability",
        "WHERE",
        "    LocatedAt.Country = Address.Country",
        "    AND LocatedAt.PostalCode = Address.PostalCode",
        "    AND Listing.ListingID = LocatedAt.ListingID",
        "    AND Availability.ListingID = LocatedAt.ListingID",
        "    AND StartDate = ? ",
        "    AND EndDate = ?",
        "GROUP BY City",
        ";");
    PreparedStatement cityCountStatement = sqlController
        .prepareStatement(statementString);
    cityCountStatement.setString(1, startDate);
    cityCountStatement.setString(2, endDate);
    ResultSet resultSet = cityCountStatement.executeQuery();

    while (resultSet.next()) {
      ReportByNumberInCity cityandnum= new ReportByNumberInCity();
      cityandnum.setCityName(resultSet.getString("City"));
      cityandnum.setNumber(resultSet.getInt("Numlistings"));

      allCities.add(cityandnum);
    }

    return allCities;
  }


  public static List<ReportByNumberInCity> reportByDateRangeInCityWithPostalCode
      (String startDate, String endDate, String postCode) throws SQLException {

    //SELECTS all LISTINGREVIEW from Table that match the listing ID provided
    SQLController sqlController = SQLController.getInstance();
    List<ReportByNumberInCity> allCities = new ArrayList<>();

    String statementString = String.join(System.getProperty("line.separator"),
        "",
        "SELECT ",
        "    Address.City, COUNT(Address.City) AS Numlistings",
        "FROM",
        "    Listing",
        "    INNER JOIN LocatedAt",
        "    INNER JOIN Address",
        "    INNER JOIN Availability",
        "WHERE",
        "    LocatedAt.Country = Address.Country",
        "    AND LocatedAt.PostalCode = Address.PostalCode",
        "    AND Listing.ListingID = LocatedAt.ListingID",
        "    AND Availability.ListingID = LocatedAt.ListingID",
        "    AND StartDate = ? ",
        "    AND EndDate = ?",
        "    AND Address.PostalCode = ?",
        "GROUP BY City",
        ";");
    PreparedStatement cityCountStatement = sqlController
        .prepareStatement(statementString);
    cityCountStatement.setString(1, startDate);
    cityCountStatement.setString(2, endDate);
    cityCountStatement.setString(3, postCode);
    ResultSet resultSet = cityCountStatement.executeQuery();

    while (resultSet.next()) {
      ReportByNumberInCity cityandnum= new ReportByNumberInCity();
      cityandnum.setCityName(resultSet.getString("City"));
      cityandnum.setNumber(resultSet.getInt("Numlistings"));

      allCities.add(cityandnum);
    }

    return allCities;
  }
}
