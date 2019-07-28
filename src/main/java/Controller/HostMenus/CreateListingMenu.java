package Controller.HostMenus;

import database.ListingRepository;
import database.SQLController;
import database.models.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;

public class CreateListingMenu {
    public static void createListing(Host host) {
        Scanner sc = new Scanner(System.in);
        String input;

        System.out.println();
        System.out.println("--------LISTING CREATION---------");

        // General Listing info
        String type = getType(sc);
        Double latitude = getLatitude(sc);
        Double longitude = getLongitude(sc);

        // Address
        String postalCode = getPostalCode(sc);
        String city = getCity(sc);
        String country = getCountry(sc);
        Address address = new Address(postalCode, city, country);

        // Amenities
        Integer numberOfGuests = getNumberOfGuests(sc);
        Double bathrooms = getBathrooms(sc);
        Integer bedrooms = getBedrooms(sc);
        Integer beds = getBeds(sc);
        Integer kitchens = getKitchens(sc);
        Integer parkingSpots = getParkingSpots(sc);
        Amenities amenities = new Amenities(numberOfGuests, bathrooms, bedrooms, beds, kitchens, parkingSpots);

        // Availabilities
        List<Availability> availabilities = getAvailabilities(sc);

        // Insert Listing into database
        Listing newListing = new Listing(type, latitude, longitude, address, amenities, availabilities);
        try {
            ListingRepository.createListing(host, newListing);
            System.out.println("Listing created successfully!");
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("An error occurred. Please contact your administrator for help.");
        }
    }

    /*
     * General Listing Info
     */
    private static String getType(Scanner sc) {
        String input;
        do {
            System.out.print("Enter building type: ");
            input = sc.nextLine();
            if (input.length() == 0) {
                System.out.println("Building type is required.");
            }
        } while (input.length() == 0);
        return input;
    }

    private static Double getLatitude(Scanner sc)  {
        String input;
        Double latitude = null;
        do {
            System.out.print("Enter latitude: ");
            input = sc.nextLine();
            if (input.length() == 0) {
                System.out.println("Latitude is required.");
            }
            else {
                try {
                    latitude = Double.parseDouble(input);
                } catch (NumberFormatException exception) {
                    System.out.println("Latitude must be numeric.");
                }
            }
        } while (latitude == null);

        return latitude;
    }

    private static Double getLongitude(Scanner sc)  {
        String input;
        Double longitude = null;
        do {
            System.out.print("Enter longitude: ");
            input = sc.nextLine();
            if (input.length() == 0) {
                System.out.println("Longitude is required.");
            }
            else {
                try {
                    longitude = Double.parseDouble(input);
                } catch (NumberFormatException exception) {
                    System.out.println("Longitude must be numeric.");
                }
            }
        } while (longitude == null);

        return longitude;
    }

    /*
     * Address
     */
    private static String getPostalCode(Scanner sc) {
        String input;
        do {
            System.out.print("Enter postal code: ");
            input = sc.nextLine();
            if (input.length() == 0) {
                System.out.println("Postal code is required.");
            }
        } while (input.length() == 0);
        return input;
    }

    private static String getCity(Scanner sc) {
        String input;
        do {
            System.out.print("Enter city: ");
            input = sc.nextLine();
            if (input.length() == 0) {
                System.out.println("City is required.");
            }
        } while (input.length() == 0);
        return input;
    }

    private static String getCountry(Scanner sc) {
        String input;
        do {
            System.out.print("Enter country: ");
            input = sc.nextLine();
            if (input.length() == 0) {
                System.out.println("Country is required.");
            }
        } while (input.length() == 0);
        return input;
    }

    /*
     * Amenities
     */
    private static Integer getNumberOfGuests(Scanner sc) {
        String input;
        Integer numberOfGuests = null;
        do {
            System.out.print("Enter number of guests: ");
            input = sc.nextLine();
            if (input.length() == 0) {
                System.out.println("Number of guests is required.");
            }
            else {
                try {
                    numberOfGuests = Integer.parseInt(input);
                } catch (NumberFormatException exception) {
                    System.out.println("Number of guests must be an integer.");
                }
                if ((numberOfGuests != null) && (numberOfGuests <= 0)) {
                    numberOfGuests = null;
                    System.out.println("Number of guests must be a positive amount.");
                }
            }
        } while (numberOfGuests == null);

        return numberOfGuests;
    }

    private static Double getBathrooms(Scanner sc) {
        String input;
        Double bathrooms = null;
        do {
            System.out.print("Enter number of bathrooms: ");
            input = sc.nextLine();
            if (input.length() == 0) {
                System.out.println("Number of bathrooms is required.");
            }
            else {
                try {
                    bathrooms = Double.parseDouble(input);
                } catch (NumberFormatException exception) {
                    System.out.println("Number of bathrooms must be an integer.");
                }
                if ((bathrooms != null) && (bathrooms <= 0)) {
                    bathrooms = null;
                    System.out.println("Number of bathrooms must be a positive amount.");
                }
            }
        } while (bathrooms == null);

        return bathrooms;
    }

    private static Integer getBedrooms(Scanner sc) {
        String input;
        Integer bedrooms = null;
        do {
            System.out.print("Enter number of bedrooms: ");
            input = sc.nextLine();
            if (input.length() == 0) {
                System.out.println("Number of bedrooms is required.");
            }
            else {
                try {
                    bedrooms = Integer.parseInt(input);
                } catch (NumberFormatException exception) {
                    System.out.println("Number of bedrooms must be an integer.");
                }
                if ((bedrooms != null) && (bedrooms <= 0)) {
                    bedrooms = null;
                    System.out.println("Number of bedrooms must be a positive amount.");
                }
            }
        } while (bedrooms == null);

        return bedrooms;
    }

    private static Integer getBeds(Scanner sc) {
        String input;
        Integer beds = null;
        do {
            System.out.print("Enter number of beds: ");
            input = sc.nextLine();
            if (input.length() == 0) {
                System.out.println("Number of beds is required.");
            }
            else {
                try {
                    beds = Integer.parseInt(input);
                } catch (NumberFormatException exception) {
                    System.out.println("Number of beds must be an integer.");
                }
                if ((beds != null) && (beds <= 0)) {
                    beds = null;
                    System.out.println("Number of beds must be a positive amount.");
                }
            }
        } while (beds == null);

        return beds;
    }

    private static Integer getKitchens(Scanner sc) {
        String input;
        Integer kitchens = null;
        do {
            System.out.print("Enter number of kitchens: ");
            input = sc.nextLine();
            if (input.length() == 0) {
                System.out.println("Number of kitchens is required.");
            }
            else {
                try {
                    kitchens = Integer.parseInt(input);
                } catch (NumberFormatException exception) {
                    System.out.println("Number of kitchens must be an integer.");
                }
                if ((kitchens != null) && (kitchens <= 0)) {
                    kitchens = null;
                    System.out.println("Number of kitchens must be a positive amount.");
                }
            }
        } while (kitchens == null);

        return kitchens;
    }

    private static Integer getParkingSpots(Scanner sc) {
        String input;
        Integer parkingSpots = null;
        do {
            System.out.print("Enter number of parking spots: ");
            input = sc.nextLine();
            if (input.length() == 0) {
                System.out.println("Number of parking spots is required.");
            }
            else {
                try {
                    parkingSpots = Integer.parseInt(input);
                } catch (NumberFormatException exception) {
                    System.out.println("Number of parking spots must be an integer.");
                }
                if ((parkingSpots != null) && (parkingSpots <= 0)) {
                    parkingSpots = null;
                    System.out.println("Number of parking spots must be a positive amount.");
                }
            }
        } while (parkingSpots == null);

        return parkingSpots;
    }

    /*
     * Availabilities
     */
    private static List<Availability> getAvailabilities(Scanner sc) {
        List<Availability> availabilities = new ArrayList<>();
        boolean continueAsking = true;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Availability nextAvailability = null;

        System.out.println("AVAILABITIES");
        System.out.println("Enter availabilities in the following format:");
        System.out.println("    yyyy-MM-dd yyyy-MM-dd price");
        do {
            nextAvailability = getAvailability(sc, dateFormat);
            if (nextAvailability != null) {
                availabilities.add(nextAvailability);
                System.out.println("Availability added to the listing successfully.");
            }
        } while (nextAvailability != null);

        return availabilities;
    }

    private static Availability getAvailability(Scanner sc, SimpleDateFormat dateFormat) {
        String input;
        Availability availability = null;

        do {
            System.out.print("Enter availability (or leave blank to finish): ");
            input = sc.nextLine();

            if (input.equals("")) {
                return null;
            }

            String[] fields = input.split("\\s+");
            if (fields.length != 3) {
                System.out.println("Invalid number of fields");
                continue;
            }

            Timestamp startDate;
            try {
                startDate = new Timestamp((dateFormat.parse(fields[0]).getTime()));
            } catch (ParseException exception) {
                System.out.println("Invalid format for start date");
                continue;
            }

            Timestamp endDate;
            try {
                endDate = new Timestamp((dateFormat.parse(fields[1]).getTime()));
            } catch (ParseException exception) {
                System.out.println("Invalid format for end date");
                continue;
            }

            int price;
            try {
                price = Integer.parseInt(fields[2]);
            } catch (NumberFormatException exception) {
                System.out.println("Price must be a whole number");
                continue;
            }

            availability = new Availability("", startDate, endDate, price);
        } while (availability == null);

        return availability;
    }

}
