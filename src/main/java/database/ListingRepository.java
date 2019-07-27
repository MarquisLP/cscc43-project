package database;

import database.models.*;

import java.sql.*;
import java.util.*;

public class ListingRepository {
    public static class ListingQueryOptions {
        private String type = null;
        private Integer maxPrice = null;
        private Integer minNumberOfGuests = null;
        private Double minBathrooms = null;
        private Integer minBedrooms = null;
        private Integer minBeds = null;
        private Integer minKitchens = null;
        private Integer minParkingSpots = null;
        private SortOrder sortOrder = SortOrder.ASCENDING;
        private Timestamp startDate = null;
        private Timestamp endDate = null;

        public ListingQueryOptions() {
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getMaxPrice() {
            return maxPrice;
        }

        public void setMaxPrice(Integer maxPrice) {
            this.maxPrice = maxPrice;
        }

        public Integer getMinNumberOfGuests() {
            return minNumberOfGuests;
        }

        public void setMinNumberOfGuests(Integer minNumberOfGuests) {
            this.minNumberOfGuests = minNumberOfGuests;
        }

        public Double getMinBathrooms() {
            return minBathrooms;
        }

        public void setMinBathrooms(Double minBathrooms) {
            this.minBathrooms = minBathrooms;
        }

        public Integer getMinBedrooms() {
            return minBedrooms;
        }

        public void setMinBedrooms(Integer minBedrooms) {
            this.minBedrooms = minBedrooms;
        }

        public Integer getMinBeds() {
            return minBeds;
        }

        public void setMinBeds(Integer minBeds) {
            this.minBeds = minBeds;
        }

        public Integer getMinKitchens() {
            return minKitchens;
        }

        public void setMinKitchens(Integer minKitchens) {
            this.minKitchens = minKitchens;
        }

        public Integer getMinParkingSpots() {
            return minParkingSpots;
        }

        public void setMinParkingSpots(Integer minParkingSpots) {
            this.minParkingSpots = minParkingSpots;
        }

        public SortOrder getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(SortOrder sortOrder) {
            this.sortOrder = sortOrder;
        }

        public Timestamp getStartDate() {
            return startDate;
        }

        public void setStartDate(Timestamp startDate) {
            this.startDate = startDate;
        }

        public Timestamp getEndDate() {
            return endDate;
        }

        public void setEndDate(Timestamp endDate) {
            this.endDate = endDate;
        }
    }

    public enum SortField {
        PRICE,
        DISTANCE
    }

    public enum SortOrder {
        ASCENDING,
        DESCENDING
    }

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

        for (Availability availability : newListing.getAvailabilities()) {
            statementString = String.join(System.getProperty("line.separator"),
                    "",
                    "INSERT INTO",
                    "    Availability(",
                    "       ListingID",
                    "       , StartDate",
                    "       , EndDate",
                    "       , Price",
                    "    )",
                    "VALUES",
                    "    (?, ?, ?, ?)",
                    ";");
            PreparedStatement insertAvailabilityStatement = sqlController.prepareStatement(statementString);
            insertAvailabilityStatement.setString(1, newListing.getListingId());
            insertAvailabilityStatement.setTimestamp(2, availability.getStartDate());
            insertAvailabilityStatement.setTimestamp(3, availability.getEndDate());
            insertAvailabilityStatement.setInt(4, availability.getPrice());
            insertAvailabilityStatement.executeUpdate();
        }
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
         * Get the listing's availabilities.
         */
        List<Availability> availabilities = new ArrayList<>();
        statementString = String.join(System.getProperty("line.separator"),
                "",
                "SELECT",
                "    StartDate",
                "    , EndDate",
                "    , Price",
                "FROM",
                "    Availability",
                "WHERE",
                "    ListingID = ?",
                "    AND NOT EXISTS (",
                "       SELECT",
                "           *",
                "       FROM",
                "           Booking",
                "       WHERE",
                "           Booking.ListingID = ?",
                "           AND Booking.StartDate = Availability.StartDate",
                "           AND Booking.EndDate = Availability.EndDate",
                "    )",
                ";");
        PreparedStatement getAvailabilitiesStatement = sqlController.prepareStatement(statementString);
        getAvailabilitiesStatement.setString(1, listingId);
        getAvailabilitiesStatement.setString(2, listingId);
        ResultSet availabilityResults = getAvailabilitiesStatement.executeQuery();
        while (availabilityResults.next()) {
            Availability availability = new Availability(
                    listingId,
                    availabilityResults.getTimestamp("StartDate"),
                    availabilityResults.getTimestamp("EndDate"),
                    availabilityResults.getInt("Price")
            );
            availabilities.add(availability);
        }

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
                amenities,
                availabilities
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

    /**
     * Delete a Listing from the database.
     * @param listingId ID of the listing to delete.
     * @throws SQLException if an error occurs while parsing SQL code
     * @throws NoSuchElementException if no Listing with the given listingID exists
     * @throws IllegalArgumentException if the given Listing currently has future, uncancelled bookings
     */
    public static void deleteListing(String listingId) throws SQLException, NoSuchElementException, IllegalArgumentException {
        SQLController sqlController = SQLController.getInstance();
        String statementString = String.join(System.getProperty("line.separator"),
                "",
                "DELETE FROM",
                "    Listing",
                "WHERE",
                "    ListingID = ?",
                ";");
        PreparedStatement deleteListingStatement = sqlController.prepareStatement(statementString);
        deleteListingStatement.setString(1, listingId);

        int numRowsDeleted = 0;
        try {
            numRowsDeleted = deleteListingStatement.executeUpdate();
        }
        catch (SQLException exception) {
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

    public static List<Availability> getListingsWithinDistance(double latitude, double longitude, double distance,
                                                          SortField sortField, ListingQueryOptions queryOptions) throws SQLException {
        List<Availability> availabilities = new ArrayList<>();

        SQLController sqlController = SQLController.getInstance();
        // Latitude-Longitude distance calculation query from Kaletha on StackOverflow:
        // https://stackoverflow.com/a/5548877
        String statementString = String.join(System.getProperty("line.separator"),
                "",
                "SELECT",
                "    l.ListingID",
                "    , av.StartDate",
                "    , av.EndDate",
                "    , av.Price",
                "    , SQRT(",
                "        POW(69.1 * (l.Latitude - ?), 2)",
                "        + POW(69.1 * (? - l.Longitude) * COS(l.Latitude / 57.3), 2)) AS Distance",
                "FROM",
                "   Listing AS l",
                "   INNER JOIN Availability AS av ON av.ListingID = l.ListingID",
                "   INNER JOIN Amenities AS am ON am.ListingID = l.ListingID",
                "WHERE",
                "   ((? is NULL) OR (l.Type = ?))",
                "   AND ((? is NULL) OR (av.Price <= ?))",
                "   AND ((? is NULL) OR (am.NumberOfGuests <= ?))",
                "   AND ((? is NULL) OR (am.Bathrooms <= ?))",
                "   AND ((? is NULL) OR (am.Bedrooms <= ?))",
                "   AND ((? is NULL) OR (am.Beds <= ?))",
                "   AND ((? is NULL) OR (am.Kitchen <= ?))",
                "   AND ((? is NULL) OR (am.ParkingSpots <= ?))",
                "   AND ((? is NULL) OR (av.StartDate >= ?))",
                "   AND ((? is NULL) OR (av.EndDate <= ?))",
                "HAVING",
                "   Distance * 1.609344 < ?"
                );
        if (sortField.equals(SortField.DISTANCE)) {
            statementString += "\nORDER BY Distance ";
        }
        else if (sortField.equals(SortField.PRICE)) {
            statementString += "\nORDER BY av.Price ";
        }
        if (queryOptions.getSortOrder().equals(SortOrder.ASCENDING)) {
            statementString += "ASC";
        }
        else if (queryOptions.getSortOrder().equals(SortOrder.DESCENDING)) {
            statementString += "DESC";
        }
        PreparedStatement getListingsStatement = sqlController.prepareStatement(statementString);
        getListingsStatement.setDouble(1, latitude);
        getListingsStatement.setDouble(2, longitude);

        // Optional query filters
        if (queryOptions.getType() == null) {
            getListingsStatement.setNull(3, Types.VARCHAR);
            getListingsStatement.setNull(4, Types.VARCHAR);
        }
        else {
            getListingsStatement.setString(3, queryOptions.getType());
            getListingsStatement.setString(4, queryOptions.getType());
        }
        if (queryOptions.getMaxPrice() == null) {
            getListingsStatement.setNull(5, Types.INTEGER);
            getListingsStatement.setNull(6, Types.INTEGER);
        }
        else {
            getListingsStatement.setInt(5, queryOptions.getMaxPrice());
            getListingsStatement.setInt(6, queryOptions.getMaxPrice());
        }
        if (queryOptions.getMinNumberOfGuests() == null) {
            getListingsStatement.setNull(7, Types.INTEGER);
            getListingsStatement.setNull(8, Types.INTEGER);
        }
        else {
            getListingsStatement.setInt(7, queryOptions.getMinNumberOfGuests());
            getListingsStatement.setInt(8, queryOptions.getMinNumberOfGuests());
        }
        if (queryOptions.getMinBathrooms() == null) {
            getListingsStatement.setNull(9, Types.DOUBLE);
            getListingsStatement.setNull(10, Types.DOUBLE);
        }
        else {
            getListingsStatement.setDouble(9, queryOptions.getMinBathrooms());
            getListingsStatement.setDouble(10, queryOptions.getMinBathrooms());
        }
        if (queryOptions.getMinBedrooms() == null) {
            getListingsStatement.setNull(11, Types.INTEGER);
            getListingsStatement.setNull(12, Types.INTEGER);
        }
        else {
            getListingsStatement.setInt(11, queryOptions.getMinBedrooms());
            getListingsStatement.setInt(12, queryOptions.getMinBedrooms());
        }
        if (queryOptions.getMinBeds() == null) {
            getListingsStatement.setNull(13, Types.INTEGER);
            getListingsStatement.setNull(14, Types.INTEGER);
        }
        else {
            getListingsStatement.setInt(13, queryOptions.getMinBeds());
            getListingsStatement.setInt(14, queryOptions.getMinBeds());
        }
        if (queryOptions.getMinKitchens() == null) {
            getListingsStatement.setNull(15, Types.INTEGER);
            getListingsStatement.setNull(16, Types.INTEGER);
        }
        else {
            getListingsStatement.setInt(15, queryOptions.getMinKitchens());
            getListingsStatement.setInt(16, queryOptions.getMinKitchens());
        }
        if (queryOptions.getMinParkingSpots() == null) {
            getListingsStatement.setNull(17, Types.INTEGER);
            getListingsStatement.setNull(18, Types.INTEGER);
        }
        else {
            getListingsStatement.setInt(17, queryOptions.getMinParkingSpots());
            getListingsStatement.setInt(18, queryOptions.getMinParkingSpots());
        }
        if (queryOptions.getStartDate() == null) {
            getListingsStatement.setNull(19, Types.INTEGER);
            getListingsStatement.setNull(20, Types.INTEGER);
        }
        else {
            getListingsStatement.setTimestamp(19, queryOptions.getStartDate());
            getListingsStatement.setTimestamp(20, queryOptions.getStartDate());
        }
        if (queryOptions.getEndDate() == null) {
            getListingsStatement.setNull(21, Types.INTEGER);
            getListingsStatement.setNull(22, Types.INTEGER);
        }
        else {
            getListingsStatement.setTimestamp(21, queryOptions.getEndDate());
            getListingsStatement.setTimestamp(22, queryOptions.getEndDate());
        }

        // Distance is set last because it's in a HAVING clause
        getListingsStatement.setDouble(23, distance);

        ResultSet getListingResults = getListingsStatement.executeQuery();

        while (getListingResults.next()) {
            Availability availability = new Availability(
                    getListingResults.getString("ListingID"),
                    getListingResults.getTimestamp("StartDate"),
                    getListingResults.getTimestamp("EndDate"),
                    getListingResults.getInt("Price")
            );
            availabilities.add(availability);
        }

        return availabilities;
    }
}
