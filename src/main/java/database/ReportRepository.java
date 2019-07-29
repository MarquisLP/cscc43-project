package database;

import database.models.ListingReview;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportRepository {

  /**
   *
   * @param startDate
   * @param endDate
   * @throws SQLException
   */
  public static void reportByDateRangeInCity(String startDate, String
      endDate) throws SQLException {

    //SELECTS all LISTINGREVIEW from Table that match the listing ID provided
    SQLController sqlController = SQLController.getInstance();

    String statementString = String.join(System.getProperty("line.separator"),
        "",
        "SELECT ",
        "    Address.City, COUNT(Address.City) AS Numlistings",
        "FROM",
        "    Listing",
        "    INNER JOIN LocatedAt",
        "    INNER JOIN Address",
        "    INNER JOIN Availability",
        "    INNER JOIN Booking",
        "WHERE",
        "    LocatedAt.Country = Address.Country",
        "    AND LocatedAt.PostalCode = Address.PostalCode",
        "    AND Listing.ListingID = LocatedAt.ListingID",
        "    AND Availability.ListingID = LocatedAt.ListingID",
        "    AND Booking.ListingID = Availability.ListingID",
        "    AND Booking.StartDate = Availability.StartDate",
        "    AND Booking.EndDate = Availability.EndDate",
        "    AND Booking.StartDate >= ? ",
        "    AND Booking.EndDate <= ?",
        "GROUP BY City",
        ";");
    PreparedStatement cityCountStatement = sqlController
        .prepareStatement(statementString);
    cityCountStatement.setString(1, startDate);
    cityCountStatement.setString(2, endDate);
    ResultSet resultSet = cityCountStatement.executeQuery();

    while (resultSet.next()) {
      resultSet.getString("City");
      System.out.println("City: " + resultSet.getString("City")
          + " Number : " + resultSet.getInt("Numlistings"));

    }
  }


  /**
   *
   * @param startDate
   * @param endDate
   * @param postCode
   * @throws SQLException
   */
  public static void reportByDateRangeInCityWithPostalCode
  (String startDate, String endDate, String postCode) throws SQLException {

    //SELECTS all LISTINGREVIEW from Table that match the listing ID provided
    SQLController sqlController = SQLController.getInstance();

    String statementString = String.join(System.getProperty("line.separator"),
        "",
        "SELECT ",
        "    Address.City, COUNT(Address.City) AS Numlistings",
        "FROM",
        "    Listing",
        "    INNER JOIN LocatedAt",
        "    INNER JOIN Address",
        "    INNER JOIN Availability",
        "    INNER JOIN Booking",
        "WHERE",
        "    LocatedAt.Country = Address.Country",
        "    AND LocatedAt.PostalCode = Address.PostalCode",
        "    AND Listing.ListingID = LocatedAt.ListingID",
        "    AND Availability.ListingID = LocatedAt.ListingID",
        "    AND Booking.ListingID = Availability.ListingID",
        "    AND Booking.StartDate = Availability.StartDate",
        "    AND Booking.EndDate = Availability.EndDate",
        "    AND Booking.StartDate >= ? ",
        "    AND Booking.EndDate <= ?",
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
      resultSet.getString("City");
      System.out.println("City: " + resultSet.getString("City")
          + " Number : " + resultSet.getInt("Numlistings"));

    }
  }

  /****************************************************************************
   *
   ****************************************************************************/


  /**
   *
   * @throws SQLException
   */
  public static void reportlistingpercountry() throws SQLException {

    //SELECTS all LISTINGREVIEW from Table that match the listing ID provided
    SQLController sqlController = SQLController.getInstance();
    String countryName;

    String statementString = String.join(System.getProperty("line.separator"),
        "",
        "SELECT ",
        "    Address.Country, COUNT(Address.Country) AS Numlistings",
        "FROM",
        "    Listing",
        "    INNER JOIN LocatedAt",
        "    INNER JOIN Address",
        "WHERE",
        "    LocatedAt.Country = Address.Country",
        "    AND LocatedAt.PostalCode = Address.PostalCode",
        "    AND Listing.ListingID = LocatedAt.ListingID",
        "GROUP BY Country",
        ";");
    PreparedStatement countryCountStatement = sqlController
        .prepareStatement(statementString);
    ResultSet resultSet = countryCountStatement.executeQuery();

    while (resultSet.next()) {
      countryName = resultSet.getString("Country");
      System.out.println("Country: " + countryName
          + " Amount: " + resultSet.getInt("Numlistings"));
    }
  }


  /**
   *
   * @throws SQLException
   */
  public static void reportlistingpercountryandcity() throws SQLException {

    //SELECTS all LISTINGREVIEW from Table that match the listing ID provided
    SQLController sqlController = SQLController.getInstance();
    String countryName;

    String statementString = String.join(System.getProperty("line.separator"),
        "",
        "SELECT ",
        "    Address.Country, COUNT(Address.Country) AS Numlistings",
        "FROM",
        "    Listing",
        "    INNER JOIN LocatedAt",
        "    INNER JOIN Address",
        "WHERE",
        "    LocatedAt.Country = Address.Country",
        "    AND LocatedAt.PostalCode = Address.PostalCode",
        "    AND Listing.ListingID = LocatedAt.ListingID",
        "GROUP BY Country",
        ";");
    PreparedStatement countryCountStatement = sqlController
        .prepareStatement(statementString);
    ResultSet resultSet = countryCountStatement.executeQuery();

    while (resultSet.next()) {
      countryName = resultSet.getString("Country");
      System.out.println("Country: " + countryName
          + " Amount: " + resultSet.getInt("Numlistings"));

      String statementStringCity = String.join(System.getProperty("line"
              + ".separator"),
          "",
          "SELECT ",
          "    Address.City, COUNT(Address.City) AS NumlistingsForCity",
          "FROM",
          "    Listing",
          "    INNER JOIN LocatedAt",
          "    INNER JOIN Address",
          "WHERE",
          "    LocatedAt.Country = Address.Country",
          "    AND LocatedAt.PostalCode = Address.PostalCode",
          "    AND Listing.ListingID = LocatedAt.ListingID",
          "    AND LocatedAt.Country = ?",
          "GROUP BY City",
          ";");

      PreparedStatement getBookingStatementCity = sqlController
          .prepareStatement(statementStringCity);
      getBookingStatementCity.setString(1, countryName);
      ResultSet resultSetCity = getBookingStatementCity.executeQuery();
      while (resultSetCity.next()) {
        System.out.println("    City Name: " + resultSetCity.getString("City")
            + " Amount: " + resultSetCity.getInt("NumlistingsForCity"));
      }
    }
  }


  /**
   * Christ, look at what you did to my boy... this garbage fire of code
   */
  public static void reportlistingpercountryandcityandpostal() throws
      SQLException {

    //SELECTS all LISTINGREVIEW from Table that match the listing ID provided
    SQLController sqlController = SQLController.getInstance();
    String countryName;
    String cityName;
    String stringtoprint;

    String statementString = String.join(System.getProperty("line.separator"),
        "",
        "SELECT ",
        "    Address.Country, COUNT(Address.Country) AS Numlistings",
        "FROM",
        "    Listing",
        "    INNER JOIN LocatedAt",
        "    INNER JOIN Address",
        "WHERE",
        "    LocatedAt.Country = Address.Country",
        "    AND LocatedAt.PostalCode = Address.PostalCode",
        "    AND Listing.ListingID = LocatedAt.ListingID",
        "GROUP BY Country",
        ";");
    PreparedStatement countryCountStatement = sqlController
        .prepareStatement(statementString);
    ResultSet resultSet = countryCountStatement.executeQuery();

    while (resultSet.next()) {
      try {
        stringtoprint = "";
        countryName = resultSet.getString("Country");
        stringtoprint = ("Country: " + countryName
            + " Amount: " + resultSet.getInt("Numlistings") + "\n");

        String statementStringCity = String.join(System.getProperty("line"
                + ".separator"),
            "",
            "SELECT ",
            "    Address.City, COUNT(Address.City) AS NumlistingsForCity",
            "FROM",
            "    Listing",
            "    INNER JOIN LocatedAt",
            "    INNER JOIN Address",
            "WHERE",
            "    LocatedAt.Country = Address.Country",
            "    AND LocatedAt.PostalCode = Address.PostalCode",
            "    AND Listing.ListingID = LocatedAt.ListingID",
            "    AND LocatedAt.Country = ?",
            "GROUP BY City",
            ";");

        PreparedStatement getBookingStatementCity = sqlController
            .prepareStatement(statementStringCity);
        getBookingStatementCity.setString(1, countryName);
        ResultSet resultSetCity = getBookingStatementCity.executeQuery();
        while (resultSetCity.next()) {
          cityName = resultSetCity.getString("City");
          stringtoprint +=
              ("    City Name: " + cityName + " Amount: "
                  + resultSetCity.getInt("NumlistingsForCity") + "\n");

          String statementStringPostal = String.join(System.getProperty("line"
                  + ".separator"),
              "",
              "SELECT ",
              "    Address.PostalCode, COUNT(Address.PostalCode) AS "
                  + "NumlistingsForPostal",
              "FROM",
              "    Listing",
              "    INNER JOIN LocatedAt",
              "    INNER JOIN Address",
              "WHERE",
              "    LocatedAt.Country = Address.Country",
              "    AND LocatedAt.PostalCode = Address.PostalCode",
              "    AND Listing.ListingID = LocatedAt.ListingID",
              "    AND LocatedAt.Country = ?",
              "    AND Address.City = ?",
              "GROUP BY PostalCode",
              ";");

          PreparedStatement getBookingStatementPostal = sqlController
              .prepareStatement(statementStringPostal);
          getBookingStatementPostal.setString(1, countryName);
          getBookingStatementPostal.setString(2, cityName);
          ResultSet resultSetPostal = getBookingStatementPostal.executeQuery();
          while (resultSetPostal.next()) {
            stringtoprint += ("        Postal: "
                + resultSetPostal.getString("PostalCode")
                + " Amount: " + resultSetPostal.getInt
                ("NumlistingsForPostal") + "\n");
          }
        }
        System.out.println(stringtoprint);
      } catch (SQLException exception) {
        System.out.println("error");
        continue;
      }
    }
  }

  /***************************************************************************
   *
   ***************************************************************************/


  public static void rankHostByTotalListingPerCountry() throws SQLException{

    //SELECTS all LISTINGREVIEW from Table that match the listing ID provided
    SQLController sqlController = SQLController.getInstance();

    String statementString = String.join(System.getProperty("line.separator"),
        "",
        "SELECT ",
        "    Host.SIN, LocatedAt.Country, COUNT(Listing.ListingID) AS num ",
        "FROM",
        "    Listing",
        "    INNER JOIN HostedBy",
        "    INNER JOIN LocatedAt",
        "    INNER JOIN Host",
        "WHERE",
        "    LocatedAt.ListingID = Listing.ListingID",
        "    AND HostedBy.SIN = Host.SIN",
        "    AND HostedBy.ListingID = Listing.ListingID",
        "GROUP BY Host.SIN, LocatedAt.Country",
        "ORDER BY Country ASC, num DESC",
        ";");
    PreparedStatement cityCountStatement = sqlController
        .prepareStatement(statementString);
    ResultSet resultSet = cityCountStatement.executeQuery();

    while (resultSet.next()) {
      System.out.println("Sin: " + resultSet.getString("SIN")
          + " In " + resultSet.getString("Country")
          + " With " + resultSet.getInt("num") + " units");
    }
  }


  public static void rankHostByTotalListingRefinedToCity() throws SQLException{

    //SELECTS all LISTINGREVIEW from Table that match the listing ID provided
    SQLController sqlController = SQLController.getInstance();

    String statementString = String.join(System.getProperty("line.separator"),
        "",
        "SELECT ",
        "    Host.SIN, Address.City, COUNT(Listing.ListingID) AS num",
        "FROM",
        "    Listing",
        "    INNER JOIN HostedBy",
        "    INNER JOIN LocatedAt",
        "    INNER JOIN Host",
        "    INNER JOIN Address",
        "WHERE",
        "    LocatedAt.ListingID = Listing.ListingID",
        "    AND HostedBy.SIN = Host.SIN",
        "    AND HostedBy.ListingID = Listing.ListingID",
        "    AND Address.PostalCode = LocatedAt.PostalCode",
        "    AND Address.Country = LocatedAt.Country",
        "GROUP BY Host.SIN, Address.City",
        "ORDER BY City ASC, num DESC",
        ";");
    PreparedStatement cityCountStatement = sqlController
        .prepareStatement(statementString);
    ResultSet resultSet = cityCountStatement.executeQuery();

    while (resultSet.next()) {
      System.out.println("Sin: " + resultSet.getString("SIN")
          + " In " + resultSet.getString("city")
          + " With " + resultSet.getInt("num") + " units");
    }
  }


}
