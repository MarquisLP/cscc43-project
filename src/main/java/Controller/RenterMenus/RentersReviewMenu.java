package Controller.RenterMenus;

import database.FindValidUserRepository;
import database.ReviewRepository;
import database.SQLController;
import database.models.HostRenterListingTuples;
import database.models.ListingReview;
import database.models.Renter;
import database.models.UserReview;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
      System.out.println("1: See all listings and hosts to leave reviews on");
      System.out.println("2: See reviews left on you");
      System.out.println("3: See reviews you left");
      System.out.println("4: Leave review on Host");
      System.out.println("5: Leave review on Listing");

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
          // See all listings and hosts to leave reviews on
          seeAllListingsAndHost(renter);
          break;
        case 2:
          // 2: See reviews left on you
          seeReviewsLeftOnYou(renter);
          break;
        case 3:
          // 3: See reviews you left
          seeReviewsRenterLeft(renter);
          break;
        case 4:
          // 4: Leave review on Host
          leaveReviewOnHost(renter);
          break;
        case 5:
          // 5: Leave review on Listing
          leaveReviewOnListing(renter);
          break;
        default:
          System.out.println("Invalid option\n");
          break;
      }
    } while (!quit);

  }


  private static void seeAllListingsAndHost(Renter renter) {
    List<HostRenterListingTuples> alltuples = new ArrayList<>();

    try {
      alltuples = FindValidUserRepository
              .getListingsAndHostsUsingRenterSIN(renter.getSin());

      System.out.println("All hosts and Listings you can leave a review at");
      System.out.println();
      for (HostRenterListingTuples tuple : alltuples) {
        System.out.println(tuple.toString());
        System.out.println();
      }
    } catch (SQLException exception) {
      System.out.println("SQL error my guy");
    } catch (NoSuchElementException exception) {
      System.out.println("No elements in the database");
    }
  }

  private static void seeReviewsLeftOnYou(Renter renter) {
    List<UserReview> eachReview = new ArrayList<>();

    try {

      eachReview = ReviewRepository.
          getAllRenterReviewsByRenterSin(renter.getSin());

      System.out.println("All reviews Left on you");
      System.out.println();
      for (UserReview review : eachReview) {
        System.out.println(review.toString());
        System.out.println();
      }
    } catch (SQLException exception) {
      System.out.println("SQL error my guy");
    } catch (NoSuchElementException exception) {
      System.out.println("No elements in the database");
    }

  }

  private static void seeReviewsRenterLeft(Renter renter) {
    List<UserReview> eachUserReview = new ArrayList<>();
    List<ListingReview> eachListingReview = new ArrayList<>();

    try {
      eachUserReview =
          ReviewRepository.getAllHostReviewsByRenterSin(renter.getSin());
      eachListingReview =
          ReviewRepository.getAllListingReviewsByRenterSin(renter.getSin());
      System.out.println("All reviews you left");
      System.out.println("Host reviews:");
      System.out.println();
      for (UserReview review : eachUserReview) {
        System.out.println(review.toString());
        System.out.println();
      }
      System.out.println("Listing Reviews:");
      System.out.println();
      for (ListingReview review:eachListingReview) {
        System.out.println(review.toString());
        System.out.println();
      }
    } catch (SQLException exception) {
      System.out.println("SQL error my guy");
    } catch (NoSuchElementException exception) {
      System.out.println("No elements in the database");
    }

  }

  /**
   *
   * @param renter
   */
  private static void leaveReviewOnHost(Renter renter) {

    String hostSin = "";
    String comment = "";
    String input = "";
    int rating = 0;
    SQLController sqlMngr = null;
    Scanner sc = null;
    boolean breakloop = false;

    if (sc == null) { sc = new Scanner(System.in); }
    if (sqlMngr == null) { sqlMngr = SQLController.getInstance(); }

    System.out.println("Enter the HostsSin of the unit, be exact");
    hostSin = sc.nextLine();

    System.out.println("Enter a comment if you wish");
    comment = sc.nextLine();

    System.out.println("Enter a rating from 1 - 5");
    do {
      breakloop = false;
      input = sc.nextLine();
      try {
        rating = Integer.parseInt(input);
        if (rating > 5 || rating < 1) {
          throw new NumberFormatException();
        } else {
          breakloop = true;
        }
      } catch (NumberFormatException exception) {
        System.out.println("Invalid option, must be between 1-5\n");
        continue;
      }
    } while (!breakloop);

    try {
      ReviewRepository.createHostReview(hostSin, renter.getSin(), comment, rating);
      System.out.println("Review left on! ID: " + hostSin);
    } catch (SQLException exception) {
      System.out.println("SQL exception");
    } catch (NoSuchElementException exception) {
      System.out.println("No element exception");
    }
  }

  /**
   *
   * @param renter
   */
  private static void leaveReviewOnListing(Renter renter) {

    String listing = "";
    String comment = "";
    String input = "";
    int rating = 0;
    SQLController sqlMngr = null;
    Scanner sc = null;
    boolean breakloop = false;

    if (sc == null) { sc = new Scanner(System.in); }
    if (sqlMngr == null) { sqlMngr = SQLController.getInstance(); }

    System.out.println("Enter the ListingID of the unit, be exact");
    listing = sc.nextLine();

    System.out.println("Enter a comment if you wish");
    comment = sc.nextLine();

    System.out.println("Enter a rating from 1 - 5");
    do {
      breakloop = false;
      input = sc.nextLine();
      try {
        rating = Integer.parseInt(input);
        if (rating > 5 || rating < 1) {
          throw new NumberFormatException();
        } else {
          breakloop = true;
        }
      } catch (NumberFormatException exception) {
        System.out.println("Invalid option, must be between 1-5\n");
        continue;
      }
    } while (!breakloop);

    try {
      ReviewRepository.createListingReview(listing, renter.getSin(), comment, rating);
      System.out.println("Review left on! Listing: " + listing);
    } catch (SQLException exception) {
      System.out.println("SQL exception");
    } catch (NoSuchElementException exception) {
      System.out.println("No element exception");
    }
  }


}
