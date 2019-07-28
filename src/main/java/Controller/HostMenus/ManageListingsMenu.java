package Controller.HostMenus;

import database.ListingRepository;
import database.models.Host;
import database.models.Listing;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ManageListingsMenu {
    public static void manageListingsMenu(Host host) {
        String input;
        Scanner sc = new Scanner(System.in);
        boolean continueRunning = true;
        do {
            System.out.println();
            System.out.println("============= MANAGE LISTINGS ================");
            System.out.println("1. View my listings");
            System.out.println("2. Cancel a booking");
            System.out.println("3. Add availability to a listing");
            System.out.println("4. Update price for a listing's availability");
            System.out.println("5. Update date range for a listing's availability");
            System.out.println("6. Delete a listing");
            System.out.println("7. Exit");
            System.out.print("Enter the number of one of the options above: ");

            input = sc.nextLine();
            switch (input) {
                case "1":
                    ViewHostListingsMenu.viewHostListings(host);
                    break;
                case "2":
                    HostCancelBookingMenu.cancelBooking(host);
                    break;
                case "3":
                    AddAvailabilityMenu.addAvailability(host);
                    break;
                case "4":
                    break;
                case "5":
                    break;
                case "6":
                    break;
                case "7":
                    continueRunning = false;
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        } while (continueRunning);
    }
}
