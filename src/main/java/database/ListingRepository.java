package database;

import database.models.Address;
import database.models.Amenities;
import database.models.Host;
import database.models.Listing;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
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

        statementString = String.join(System.getProperty("line.separator"),
                "",
                "INSERT INTO",
                "    Amenities(",
                "       ListingID",
                "       , NumberOfGuests",
                "       , Bathrooms",
                "       , Bedrooms",
                "       , Beds",
                "       , Kitchen",
                "       , Parkingspots",
                "    )",
                "VALUES",
                "    (?, ?, ?, ?, ?, ?, ?)",
                ";");
        PreparedStatement insertAmenities = sqlController.prepareStatement(statementString);
        insertAmenities.setString(1, newListing.getListingId());
        insertAmenities.setInt(2, newListing.getAmenities().getNumberOfGuests());
        insertAmenities.setDouble(3, newListing.getAmenities().getBathrooms());
        insertAmenities.setInt(4, newListing.getAmenities().getBedrooms());
        insertAmenities.setInt(5, newListing.getAmenities().getBeds());
        insertAmenities.setInt(6, newListing.getAmenities().getKitchen());
        insertAmenities.setInt(7, newListing.getAmenities().getParkingSpots());
        insertAmenities.executeUpdate();
    }

    public static Listing getListingById(String listingId) throws SQLException {
        SQLController sqlController = SQLController.getInstance();

        /*
         * Get the listing's address.
         */
        String statementString = String.join(System.getProperty("line.separator"),
                "",
                "SELECT",
                "    PostalCode",
                "    , Country",
                "FROM",
                "    LocatedAt",
                "WHERE",
                "    ListingID = ?",
                ";");
        PreparedStatement getLocatedAtStatement = sqlController.prepareStatement(statementString);
        getLocatedAtStatement.setString(1, listingId);
        ResultSet locatedAtResults = getLocatedAtStatement.executeQuery();
        locatedAtResults.first();
        statementString = String.join(System.getProperty("line.separator"),
                "",
                "SELECT",
                "    City",
                "FROM",
                "    Address",
                "WHERE",
                "    PostalCode = ?",
                "    AND Country = ?",
                ";");
        PreparedStatement getAddressStatement = sqlController.prepareStatement(statementString);
        getAddressStatement.setString(1, locatedAtResults.getString("PostalCode"));
        getAddressStatement.setString(2, locatedAtResults.getString("Country"));
        ResultSet addressResults = getAddressStatement.executeQuery();
        addressResults.first();
        Address address = new Address(
                locatedAtResults.getString("PostalCode"),
                addressResults.getString("City"),
                locatedAtResults.getString("Country")
        );

        /*
         * Get the listing's amenities.
         */
        statementString = String.join(System.getProperty("line.separator"),
                "",
                "SELECT",
                "    NumberOfGuests",
                "    , Bathrooms",
                "    , Bedrooms",
                "    , Beds",
                "    , Kitchen",
                "    , Parkingspots",
                "FROM",
                "    Amenities",
                "WHERE",
                "    ListingID = ?",
                ";");
        PreparedStatement getAmenitiesStatement = sqlController.prepareStatement(statementString);
        getAmenitiesStatement.setString(1, listingId);
        ResultSet amenitiesResult = getAmenitiesStatement.executeQuery();
        amenitiesResult.first();
        Amenities amenities = new Amenities(
                amenitiesResult.getInt("NumberOfGuests"),
                amenitiesResult.getDouble("Bathrooms"),
                amenitiesResult.getInt("Bedrooms"),
                amenitiesResult.getInt("Beds"),
                amenitiesResult.getInt("Kitchen"),
                amenitiesResult.getInt("ParkingSpots")
        );

        /*
         * Get the listing's general info.
         */
        statementString = String.join(System.getProperty("line.separator"),
                "",
                "SELECT",
                "    Type",
                "    , Longitude",
                "    , Latitude",
                "FROM",
                "    Listing",
                "WHERE",
                "    ListingID = ?",
                ";");
        PreparedStatement getListingStatement = sqlController.prepareStatement(statementString);
        getListingStatement.setString(1, listingId);
        ResultSet listingResult = getListingStatement.executeQuery();
        listingResult.first();

        return new Listing(
                listingId,
                listingResult.getString("Type"),
                listingResult.getDouble("Latitude"),
                listingResult.getDouble("Longitude"),
                address,
                amenities
        );
    }

    public static List<Listing> getAllListingsForHost(String sin) throws SQLException {
        List<Listing> listings = new ArrayList<>();
        SQLController sqlController = SQLController.getInstance();

        String statementString = String.join(System.getProperty("line.separator"),
                "",
                "SELECT",
                "    ListingID",
                "FROM",
                "    HostedBy",
                "WHERE",
                "    SIN = ?",
                ";");
        PreparedStatement getHostedByStatement = sqlController.prepareStatement(statementString);
        getHostedByStatement.setString(1, sin);
        ResultSet hostedByResults = getHostedByStatement.executeQuery();

        while (hostedByResults.next()) {
            String listingId = hostedByResults.getString("ListingID");
            listings.add(getListingById(listingId));
        }

        return listings;
    }
}
