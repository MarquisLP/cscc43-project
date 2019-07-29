package Controller.RenterMenus;

import database.SQLController;
import database.models.Renter;
import java.util.Scanner;

public class RentersReviewMenu {

  public static void RentersReviewStuffandWhatnot(Renter renter) {

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
    System.out.println("*******REVIEWS MENU********");
    System.out.println("***************************");
    System.out.println("");

    do {
      String input = "";
      int choice = -1;

      System.out.println("Choose one of the following:");
      System.out.println("0: Return to main menu");
      System.out.println("1: Make a review");
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
}
