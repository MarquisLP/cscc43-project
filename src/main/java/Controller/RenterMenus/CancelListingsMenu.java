package Controller.RenterMenus;

import database.BookingRepository;
import database.SQLController;
import database.models.Booking;
import database.models.Renter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CancelListingsMenu {

  public static void CancelListingsMenu(Renter renter) {

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
    System.out.println("****CANCEL BOOKING MENU****");
    System.out.println("***************************");
    System.out.println("");

    do {
      String input = "";
      int choice = -1;

      System.out.println("Choose one of the following:");
      System.out.println("0: Return to main menu");
      System.out.println("1: See all your bookings");
      System.out.println("2: Cancel a booking");
      System.out.println("3: See cancelled bookings");

      input = sc.nextLine();
      try {
        choice = Integer.parseInt(input);
      } catch (NumberFormatException exception) {
        System.out.println("Invalid option\n");
        continue;
      }

      switch (choice) {
        case 0:
          quit = true;
          break;
        case 1:
          // 1: See all your bookings
          seeBookings(renter);
          break;
        case 2:
          // 2: Cancel a booking
          cancelBooking(renter);
          break;
        case 3:
          // 3: See cancelled bookings
          seeCancelledBookings(renter);
          break;
        default:
          System.out.println("Invalid option\n");
          break;
      }


    } while (!quit);

  }

  private static void seeBookings(Renter renter) {
    try {
      List<Booking> bookingList = new ArrayList<>();
      bookingList = BookingRepository.getBookingsByUser(renter);

      if (bookingList.size() == 0) {
        System.out.println("You don't have anything booked!");
      }
      for (Booking booking : bookingList) {
        System.out.println("");
        System.out.println(booking.toString());
      }
    } catch (SQLException exception) {
      System.out.println(exception);
    } catch (NoSuchElementException exception) {
      System.out.println(exception);
    }
  }

  private static void cancelBooking(Renter renter) {
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
      BookingRepository.cancelBooking(listingId, startTime, endTime, renter);
      System.out.println("Listing cancelled! ID: " + listingId + " from " +
          startTime + " to " + endTime);
    } catch (SQLException exception) {
      System.out.println("SQL exception");
    } catch (NoSuchElementException exception) {
      System.out.println("No element exception");
    }
  }


  private static void seeCancelledBookings(Renter renter) {
    try {
      List<Booking> bookingList = new ArrayList<>();
      bookingList = BookingRepository.getCancelledBookingsByUser(renter);

      if (bookingList.size() == 0) {
        System.out.println("You haven't cancelled anything yet");
      }
      for (Booking booking : bookingList) {
        System.out.println("");
        System.out.println(booking.toString());
      }
    } catch (SQLException exception) {
      System.out.println(exception);
    } catch (NoSuchElementException exception) {
      System.out.println(exception);
    }
  }

}
