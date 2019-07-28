package Controller.RenterMenus;

import database.BookingRepository;
import database.ListingRepository;
import database.ListingRepository.ListingQueryOptions;
import database.ListingRepository.SortField;
import database.SQLController;
import database.models.Address;
import database.models.Availability;
import database.models.Renter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class BookListingsMenus {

  private static Timestamp startTime = null;
  private static Timestamp endTime = null;
  private static ListingQueryOptions options = new ListingQueryOptions();

  public static void SeeAndBookListingsMenu(Renter renter) {

    startTime = null;
    endTime = null;

    boolean success = true;
    //start and end dates for extra filters

    SQLController sqlMngr = null;
    // 'sc' is needed in order to scan the inputs provided by the user
    Scanner sc = null;

    if (sc == null) {
      sc = new Scanner(System.in);
    }
    if (sqlMngr == null) {
      sqlMngr = SQLController.getInstance();
    }

    boolean quit = false;
    System.out.println("");
    System.out.println("***************************");
    System.out.println("**VIEW LISTING & BOOK MENU*");
    System.out.println("***************************");
    System.out.println("");

    do {
      String input = "";
      int choice = -1;

      System.out.println("Choose one of the following:");
      System.out.println("0: Return to main menu");
      System.out.println("1: Search by Latitude & Longitude");
      System.out.println("2: Search by nearby Postal Codes");
      System.out.println("3: Search by exact address");
      System.out.println("4: Set search filters");
      System.out.println("5: Clear all filters");
      System.out.println("6: Choose and Book a listing!");

      input = sc.nextLine();
      try {
        choice = Integer.parseInt(input);
      } catch (NumberFormatException exception) {
        System.out.println("Invalid option\n");
        continue;
      }

      switch (choice) {
        case 0:
          System.out.println("Goodbye!");
          quit = true;
          break;
        case 1:
          // 1: Search by Latitude & Longitude
          searchByLatAndLong();
          break;
        case 2:
          // 2: Search by near Postal Codes
          searchNearPostalCodes();
          break;
        case 3:
          // 3: Search by exact address
          searchByAddress();
          break;
        case 4:
          // 4: Search by filters
          searchByFilters();
          break;
        case 5:
          // 5: Clear filters
          //Note this may not work i just made it a new object.
          clearAllFilters();
          break;
        case 6:
          // 6: Book listing
          bookListing(renter);
          break;
        default:
          System.out.println("Invalid option\n");
          break;
      }


    } while (!quit);
  }

  private static void bookListing(Renter renter) {
    String listingId = "";
    String startTime = null;
    String endTime = null;
    String input = "";
    SQLController sqlMngr = null;
    Scanner sc = null;
    boolean breakloop = false;

    if (sc == null) {
      sc = new Scanner(System.in);
    }
    if (sqlMngr == null) {
      sqlMngr = SQLController.getInstance();
    }

    System.out.println("Enter the ListingID of the unit, be exact");
    listingId = sc.nextLine();

    System.out.println("Enter the Start date of the unit, be exact YYYY-MM-DD");
    startTime = sc.nextLine();

    System.out.println("Enter the End date of the unit, be exact YYYY-MM-DD");
    endTime = sc.nextLine();

    try {
      BookingRepository.booking(listingId, startTime, endTime, renter);
      System.out.println("Listing booked! ID: " + listingId + " from " +
          startTime + " to " + endTime);
    } catch (SQLException exception) {
      System.out.println("SQL exception");
    } catch (NoSuchElementException exception) {
      System.out.println("No element exception");
    }

  }


  private static void searchByLatAndLong() {

    List<Availability> availabilities = new ArrayList<>();

    SQLController sqlMngr = null;
    Scanner sc = null;
    double latitude = 0.0;
    double longitude = 0.0;
    double choice = -1.0;
    boolean breakloop = false;
    double distance = 10.0;

    if (sc == null) {
      sc = new Scanner(System.in);
    }
    if (sqlMngr == null) {
      sqlMngr = SQLController.getInstance();
    }

    System.out.println("Would you like to search by specific date range?(y/n)");
    System.out.println("Note: This will override any previous filters set");
    String input = sc.nextLine();
    if (input.equals("y") || input.equals("Y")) {
      setDateHelper();
    }
    input = "";

    System.out.println("Enter the latitude to search from");
    do {
      choice = -1;
      input = "";
      breakloop = false;
      input = sc.nextLine();

      try {
        choice = Double.parseDouble(input);
        latitude = choice;
        breakloop = true;
      } catch (NumberFormatException exception) {
        System.out.println("Invalid option must be in latitude\n");
        continue;
      }

    } while (!breakloop);

    System.out.println("Enter the longitude to search from");
    do {
      choice = -1;
      input = "";
      breakloop = false;
      input = sc.nextLine();

      try {
        choice = Double.parseDouble(input);
        longitude = choice;
        breakloop = true;
      } catch (NumberFormatException exception) {
        System.out.println("Invalid option must be in longitude\n");
        continue;
      }

    } while (!breakloop);

    System.out.println("Enter the max distance (Note leave blanck to "
        + "leave default at 10)");
    do {
      choice = -1;
      input = "";
      breakloop = false;
      input = sc.nextLine();

      if (!(input.equals(""))) {
        try {
          choice = Double.parseDouble(input);
          distance = choice;
          breakloop = true;
        } catch (NumberFormatException exception) {
          System.out.println("Invalid option must be distance\n");
          continue;
        }
      }
    } while (!breakloop);

    System.out.println("Filter by distance or price enter d/p default search "
        + "by distance");

    SortField sortField = ListingRepository.SortField.DISTANCE;

    if (input.equals("d")) {
      sortField = ListingRepository.SortField.DISTANCE;
    } else if (input.equals("p")) {
      sortField = ListingRepository.SortField.PRICE;
    }

    try {
      availabilities = ListingRepository.getListingsWithinDistance(latitude,
          longitude, distance, sortField, options);
    } catch (SQLException exception) {
      System.out.println("SQL exception");
    } catch (NoSuchElementException exception) {
      System.out.println("No element exception");
    }

    System.out.println("Listings near the chosen postal code: ");
    int i = 1;
    String toPrint = "";
    for (Availability availability : availabilities) {
      toPrint = i + ": " + availability.toString();
      i = i + 1;
      System.out.println(toPrint);
    }


  }

  /**
   *
   */
  private static void searchNearPostalCodes() {

    List<Availability> availabilities = new ArrayList<>();

    SQLController sqlMngr = null;
    Scanner sc = null;

    if (sc == null) {
      sc = new Scanner(System.in);
    }
    if (sqlMngr == null) {
      sqlMngr = SQLController.getInstance();
    }

    System.out.println("Would you like to search by specific date range?(y/n)");
    System.out.println("Note: This will override any previous filters set");
    String input = sc.nextLine();

    if (input.equals("y") || input.equals("Y")) {
      setDateHelper();
    }
    input = "";
    System.out.println("Enter postal code to search");
    input = sc.nextLine();

    try {
      availabilities = ListingRepository
          .getListingsNearPostalCode(input, options);
    } catch (SQLException exception) {
      System.out.println("SQL exception");
    } catch (NoSuchElementException exception) {
      System.out.println("No element exception");
    }

    System.out.println("Listings near the chosen postal code: ");
    int i = 1;
    String toPrint = "";
    for (Availability availability : availabilities) {
      toPrint = i + ": " + availability.toString();
      i = i + 1;
      System.out.println(toPrint);
    }

  }

  /**
   *
   */
  private static void searchByAddress() {
    List<Availability> availabilities = new ArrayList<>();

    SQLController sqlMngr = null;
    Scanner sc = null;
    Address address = new Address();

    if (sc == null) {
      sc = new Scanner(System.in);
    }
    if (sqlMngr == null) {
      sqlMngr = SQLController.getInstance();
    }

    System.out.println("Would you like to search by specific date range?(y/n)");
    System.out.println("Note: This will override any previous filters set");
    String input = sc.nextLine();

    if (input.equals("y") || input.equals("Y")) {
      setDateHelper();
    }
    input = "";
    System.out.println("Enter Country to search");
    input = sc.nextLine();
    address.setCountry(input);

    System.out.println("Enter city to search");
    input = sc.nextLine();
    address.setCity(input);

    System.out.println("Enter postal code to search");
    input = sc.nextLine();
    address.setPostalCode(input);

    try {
      availabilities = ListingRepository.getListingsAtExactAddress(address,
          options);
    } catch (SQLException exception) {
      System.out.println("SQL exception");
    } catch (NoSuchElementException exception) {
      System.out.println("No element exception");
    }

    System.out.println("Listings with the address: ");
    int i = 1;
    String toPrint = "";
    for (Availability availability : availabilities) {
      toPrint = i + ": " + availability.toString();
      i = i + 1;
      System.out.println(toPrint);
    }

  }


  /**
   *
   */
  private static void searchByFilters() {
    List<Availability> availabilities = new ArrayList<>();
    int choice = -1;
    double doubleChoice = -1.0;
    Timestamp dateChoice = null;
    boolean breakloop = false;

    SQLController sqlMngr = null;
    Scanner sc = null;
    if (sc == null) {
      sc = new Scanner(System.in);
    }
    if (sqlMngr == null) {
      sqlMngr = SQLController.getInstance();
    }

    System.out.println("Would you like to search by unit type?");
    System.out.println("If so type the type of unit you are looking for.");
    System.out.println("If not just press enter.");
    String input = sc.nextLine();
    if (!(input.equals(""))) {
      options.setType(input);
    }

    System.out.println("Would you like to search by max price?");
    System.out.println("If so enter the price as just a number ie: 550.");
    System.out.println("If not just press enter.");

    do {
      choice = -1;
      input = "";
      breakloop = false;
      input = sc.nextLine();

      if (!(input.equals(""))) {
        try {
          choice = Integer.parseInt(input);
          // Sets max price
          options.setMaxPrice(choice);
          breakloop = true;
        } catch (NumberFormatException exception) {
          System.out.println("Invalid option must be in this format : ###\n");
          continue;
        }
      }
      if (input.equals("")) {
        breakloop = true;
      }
    } while (breakloop == false);

    System.out.println("Would you like to search by minimum guests?");
    System.out.println("If so enter the guests as just a number ie: 3.");
    System.out.println("If not just press enter.");

    do {
      choice = -1;
      input = "";
      breakloop = false;
      input = sc.nextLine();

      if (!(input.equals(""))) {
        try {
          choice = Integer.parseInt(input);
          // Sets min guest
          options.setMinNumberOfGuests(choice);
          breakloop = true;
        } catch (NumberFormatException exception) {
          System.out.println("Invalid option must be in this format : #\n");
          continue;
        }
      }
      if (input.equals("")) {
        breakloop = true;
      }
    } while (breakloop == false);

    System.out.println("Would you like to search by minimum bathrooms?");
    System.out.println("If so enter as just a number ie: 2 or 1.5");
    System.out.println("If not just press enter.");

    do {
      doubleChoice = -1.0;
      input = "";
      breakloop = false;
      input = sc.nextLine();

      if (!(input.equals(""))) {
        try {
          doubleChoice = Double.parseDouble(input);
          // Sets min bathrooms
          options.setMinBathrooms(doubleChoice);
          breakloop = true;
        } catch (NumberFormatException exception) {
          System.out.println("Invalid option must be in this format : #\n");
          continue;
        }
      }
      if (input.equals("")) {
        breakloop = true;
      }
    } while (breakloop == false);

    System.out.println("Would you like to search by minimum bedrooms?");
    System.out.println("If so enter as just a number ie: 2");
    System.out.println("If not just press enter.");

    do {
      choice = -1;
      input = "";
      breakloop = false;
      input = sc.nextLine();

      if (!(input.equals(""))) {
        try {
          choice = Integer.parseInt(input);
          // Sets min bedrooms
          options.setMinBedrooms(choice);
          breakloop = true;
        } catch (NumberFormatException exception) {
          System.out.println("Invalid option must be in this format : #\n");
          continue;
        }
      }
      if (input.equals("")) {
        breakloop = true;
      }
    } while (breakloop == false);

    System.out.println("Would you like to search by minimum beds?");
    System.out.println("If so enter as just a number ie: 2");
    System.out.println("If not just press enter.");

    do {
      choice = -1;
      input = "";
      breakloop = false;
      input = sc.nextLine();

      if (!(input.equals(""))) {
        try {
          choice = Integer.parseInt(input);
          // Sets min beds
          options.setMinBeds(choice);
          breakloop = true;
        } catch (NumberFormatException exception) {
          System.out.println("Invalid option must be in this format : #\n");
          continue;
        }
      }
      if (input.equals("")) {
        breakloop = true;
      }
    } while (breakloop == false);

    System.out.println("Would you like to search by minimum kitchens?");
    System.out.println("If so enter as just a number ie: 2");
    System.out.println("If not just press enter.");

    do {
      choice = -1;
      input = "";
      breakloop = false;
      input = sc.nextLine();

      if (!(input.equals(""))) {
        try {
          choice = Integer.parseInt(input);
          // Sets min bedrooms
          options.setMinKitchens(choice);
          breakloop = true;
        } catch (NumberFormatException exception) {
          System.out.println("Invalid option must be in this format : #\n");
          continue;
        }
      }
      if (input.equals("")) {
        breakloop = true;
      }
    } while (breakloop == false);

    System.out.println("Would you like to search by minimum parking spots?");
    System.out.println("If so enter as just a number ie: 2");
    System.out.println("If not just press enter.");

    do {
      choice = -1;
      input = "";
      breakloop = false;
      input = sc.nextLine();

      if (!(input.equals(""))) {
        try {
          choice = Integer.parseInt(input);
          // Sets min bedrooms
          options.setMinParkingSpots(choice);
          breakloop = true;
        } catch (NumberFormatException exception) {
          System.out.println("Invalid option must be in this format : #\n");
          continue;
        }
      }
      if (input.equals("")) {
        breakloop = true;
      }
    } while (breakloop == false);

    setDateHelper();

    System.out.println("Filters set, please choose how to search!");

  }


  /**
   *
   */
  private static void setDateHelper() {

    Timestamp dateChoice = null;
    boolean breakloop = false;
    String input = "";

    SQLController sqlMngr = null;
    Scanner sc = null;
    if (sc == null) {
      sc = new Scanner(System.in);
    }
    if (sqlMngr == null) {
      sqlMngr = SQLController.getInstance();
    }

    System.out.println("Would you like to search by Start date?");
    System.out.println("If so enter as just a number ie: YYYY-MM-DD");
    System.out.println("If not just press enter.");
    System.out.println("Note this will override the filters if you have them!");

    do {
      dateChoice = null;
      input = "";
      breakloop = false;
      input = sc.nextLine();

      if (!(input.equals(""))) {
        try {
          DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
          dateChoice = new Timestamp((df.parse(input)).getTime());
          // Sets stardate
          options.setStartDate(dateChoice);
          breakloop = true;
        } catch (Exception exception) {
          System.out.println("Invalid option must be in format: YYYY-MM-DD\n");
          continue;
        }
      }
      if (input.equals("")) {
        breakloop = true;
      }
    } while (!breakloop);

    System.out.println("Would you like to search by End date?");
    System.out.println("If so enter as just a number ie: YYYY-MM-DD");
    System.out.println("If not just press enter.");
    System.out.println("Note this will override the filters if you have them!");

    do {
      dateChoice = null;
      input = "";
      breakloop = false;
      input = sc.nextLine();

      if (!(input.equals(""))) {
        try {
          DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
          dateChoice = new Timestamp((df.parse(input)).getTime());
          // Sets enddate
          options.setEndDate(dateChoice);
          breakloop = true;
        } catch (Exception exception) {
          System.out.println("Invalid option must be in format: YYYY-MM-DD\n");
          continue;
        }
      }
      if (input.equals("")) {
        breakloop = true;
      }
    } while (breakloop == false);
  }


  private static void clearAllFilters() {
    options = new ListingQueryOptions();
  }


}
