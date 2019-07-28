package Controller.RenterMenus;

import database.ListingRepository;
import database.ListingRepository.ListingQueryOptions;
import database.SQLController;
import database.models.Availability;
import database.models.Renter;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;



public class BookListingsMenus {

  private static Timestamp startTime = null;
  private static Timestamp endTime = null;

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
      System.out.println("2: Search by near Postal Codes");
      System.out.println("3: Search by filters");
      System.out.println("4: Search by exact address");
      System.out.println("5: Add specific dates to above search");

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
          BookListingsMenus.SeeAndBookListingsMenu(renter);
          break;
        case 2:
          // 2: Search by near Postal Codes
          searchNearPostalCodes();
          break;
        case 3:
          // 3: Search by filters
          searchByFilters();
          break;
        case 4:
          // 4: Search by exact address
          break;
        case 5:
          // 5: Add specific dates to above search
          break;
        default:
          System.out.println("Invalid option\n");
          break;
      }


    } while (quit == false);
  }

  private static void searchNearPostalCodes() {

    List<Availability> availabilities = new ArrayList<>();
    ListingQueryOptions options = new ListingQueryOptions();

    SQLController sqlMngr = null;
    Scanner sc = null;
    if (sc == null) { sc = new Scanner(System.in); }
    if (sqlMngr == null) { sqlMngr = SQLController.getInstance(); }

    String input = sc.nextLine();

    try {
      if (startTime == null && endTime == null) {
        availabilities =
            ListingRepository.getListingsNearPostalCode(input, options);
      } else {
        options.setStartDate(startTime);
        options.setEndDate(endTime);
        availabilities =
            ListingRepository.getListingsNearPostalCode(input, options);
      }
    } catch (SQLException exception) {
      System.out.println("SQL exception");
    } catch (NoSuchElementException exception) {
      System.out.println("No element exception");
    }

    System.out.println("Listings near the chosen postal code: ");
    int i = 1;
    String toPrint = "";
    for (Availability availability:availabilities) {
      toPrint = i + ": " + availability.toString();
      i = i + 1;
      System.out.println(toPrint);
    }

  }


  /**
   *
   */
  private static void searchByFilters() {

  }
}
