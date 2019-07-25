package database;

import database.models.Host;
import database.models.Listing;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Random;

public class ListingRepository {
    public static void createListing(Host host, Listing newListing) throws SQLException {
        SQLController sqlController = SQLController.getInstance();

        String statementString = String.join(System.getProperty("line.separator"),
                "",
                "INSERT INTO",
                "    Listing(ListingID, Type, Longitude, Latitude)",
                "VALUES",
                "    (?, ?, ?, ?)",
                ";");
        PreparedStatement insertListingStatement = sqlController.prepareStatement(statementString);
        insertListingStatement.setString(1, newListing.getListingId());
        insertListingStatement.setString(2, newListing.getType());
        insertListingStatement.setDouble(3, newListing.getLatitude());
        insertListingStatement.setDouble(4, newListing.getLongitude());
        insertListingStatement.executeUpdate();

        statementString = String.join(System.getProperty("line.separator"),
                "",
                "SELECT",
                "    PostalCode",
                "    , City",
                "    , Country",
                "FROM",
                "    Address",
                "WHERE",
                "    PostalCode = ?",
                "    AND City = ?",
                ";");
        PreparedStatement getExistingAddressStatement = sqlController.prepareStatement(statementString);
        getExistingAddressStatement.setString(1, newListing.getAddress().getPostalCode().toLowerCase());
        getExistingAddressStatement.setString(2, newListing.getAddress().getCity().toLowerCase());
        ResultSet resultSet = getExistingAddressStatement.executeQuery();
        if (!(resultSet.next())) {
            statementString = String.join(System.getProperty("line.separator"),
                    "",
                    "INSERT INTO",
                    "    Address(PostalCode, City, Country)",
                    "VALUES",
                    "    (?, ?, ?)",
                    ";");
            PreparedStatement insertAddressStatement = sqlController.prepareStatement(statementString);
            insertAddressStatement.setString(1, newListing.getAddress().getPostalCode());
            insertAddressStatement.setString(2, newListing.getAddress().getCity());
            insertAddressStatement.setString(3, newListing.getAddress().getCountry());
            insertAddressStatement.executeUpdate();
        }

        statementString = String.join(System.getProperty("line.separator"),
                "",
                "INSERT INTO",
                "    LocatedAt(ListingID, PostalCode, Country)",
                "VALUES",
                "    (?, ?, ?)",
                ";");
        PreparedStatement insertLocatedAt = sqlController.prepareStatement(statementString);
        insertLocatedAt.setString(1, newListing.getListingId());
        insertLocatedAt.setString(2, newListing.getAddress().getPostalCode());
        insertLocatedAt.setString(3, newListing.getAddress().getCountry());
        insertLocatedAt.executeUpdate();

        statementString = String.join(System.getProperty("line.separator"),
                "",
                "INSERT INTO",
                "    HostedBy(ListingID, SIN)",
                "VALUES",
                "    (?, ?)",
                ";");
        PreparedStatement insertHostedBy = sqlController.prepareStatement(statementString);
        insertHostedBy.setString(1, newListing.getListingId());
        insertHostedBy.setString(2, host.getSin());
        insertHostedBy.executeUpdate();
    }

}
