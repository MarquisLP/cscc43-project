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
          break;
        case 3:
          // 3: See cancelled bookings
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
      bookingList = BookingRepository.getBookingByUser(renter);

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
