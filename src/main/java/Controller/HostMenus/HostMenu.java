package Controller.HostMenus;

import database.models.Host;

import java.util.Scanner;

public class HostMenu {
    public static void hostMenu(Host host) {
        Scanner sc = new Scanner(System.in);
        String input;
        boolean continueRunning = true;

        do {
            System.out.println();
            System.out.println("========HOST MENU=========");
            System.out.println("0. Exit");
            System.out.println("1. Create a new listing");
            System.out.println("2. Manage my listings");
            System.out.println("3. View renters that rented from me");
            System.out.println("4. Review a renter");
            System.out.println("5. View renter reviews I've submitted");
            System.out.println("6. View reviews about me");
            System.out.println("7. View reports");

            System.out.print("Enter the number of one of the options above: ");
            input = sc.nextLine();

            switch (input) {
                case "0":
                    continueRunning = false;
                    break;
                case "1":
                    CreateListingMenu.createListing(host);
                    break;
                case "2":
                    ManageListingsMenu.manageListingsMenu(host);
                    break;
                case "3":
                    ViewInteractedRentersMenu.viewInteractedRenters(host);
                    break;
                case "4":
                    ReviewRenterMenu.reviewRenter(host);
                    break;
                case "5":
                    ViewSubmittedRenterReviewsMenu.viewSubmittedRenterReviews(host);
                    break;
                case "6":
                    ViewHostReviewsMenu.viewHostReviews(host);
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        } while (continueRunning);
    }
}
