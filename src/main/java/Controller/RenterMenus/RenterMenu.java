package Controller.RenterMenus;

import database.SQLController;
import database.models.Renter;
import java.util.Scanner;

public class RenterMenu {

  public static void renterMenu(Renter renter) {

    SQLController sqlMngr = null;
    Scanner sc = null;

    boolean success = true;
    if (sc == null) {
      sc = new Scanner(System.in);
    }
    if (sqlMngr == null) {
      sqlMngr = SQLController.getInstance();
    }

    boolean quit = false;
    System.out.println("");
    System.out.println("***************************");
    System.out.println("********RENTER MENU********");
    System.out.println("***************************");
    System.out.println("");

    do {
      String input = "";
      int choice = -1;

      System.out.println("Choose one of the following:");
      System.out.println("0: Logout and Quit");
      System.out.println("1: See Listings (and Book)");
      System.out.println("2: Cancel a Booking or see cancelled bookings");
      System.out.println("3: Leave a review");
      System.out.println("4: Delete my account");

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
          // 1: See Listings (and Book)
          BookListingsMenus.SeeAndBookListingsMenu(renter);
          break;
        case 2:
          // 2: Cancel a Booking or see a cancelled booking
          CancelListingsMenu.CancelListingsMenu(renter);
          break;
        case 3:
          // 3: Leave a review
          RentersReviewMenu.RentersReviewStuffandWhatnot(renter);
          break;
        case 4:
          DeleteRenterMenu.deleteRenter(renter);
          break;
        default:
          System.out.println("Invalid option\n");
          break;
      }
    } while (quit == false);
  }
}
