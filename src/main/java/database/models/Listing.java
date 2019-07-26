package database.models;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

public class Listing {
    private String listingId;
    private String type;
    private double latitude;
    private double longitude;
    private Address address;
    private Amenities amenities;
    private List<Availability> availabilities;

    public Listing(String listingId, String type, double latitude, double longitude, Address address, Amenities amenities, List<Availability> availabilities) {
        this.listingId = listingId;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.amenities = amenities;
        this.availabilities = availabilities;
    }

    public Listing(String type, double latitude, double longitude, Address address, Amenities amenities, List<Availability> availabilities) {
        this.listingId = generateListingId();
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.amenities = amenities;
        this.availabilities = availabilities;
    }

    /*
     * Code copied from Baeldung:
     * https://www.baeldung.com/java-random-string
     */
    private static String generateListingId() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateString = dateFormat.format(new Date());

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return dateString + buffer.toString();
    }

    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Amenities getAmenities() {
        return amenities;
    }

    public void setAmenities(Amenities amenities) {
        this.amenities = amenities;
    }

    public List<Availability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(List<Availability> availabilities) {
        this.availabilities = availabilities;
    }
}
