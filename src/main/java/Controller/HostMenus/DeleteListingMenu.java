package Controller.HostMenus;

import database.ListingRepository;
import database.models.Host;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class DeleteListingMenu {
    public static void deleteListing(Host host) {
        System.out.println("-------------DELETE A LISTING-------------");

        Scanner sc = new Scanner(System.in);
        String listingId;
        boolean continueAsking = true;

        do {
            System.out.print("Enter the ID of the listing to delete (or leave blank to exit): ");

            listingId = sc.nextLine();

            if (listingId.equals("")) {
                return;
            }

            try {
                ListingRepository.deleteListing(listingId, host);
                continueAsking = false;
                System.out.println("Listing deleted successfully!");
            } catch (IllegalArgumentException exception) {
                System.out.println("You may not delete a listing that has future bookings.");
            } catch (NoSuchElementException exception) {
                System.out.println("Error: Either the given listing does not exist, or you are not the owner of it.");
            } catch (SQLException exception) {
                exception.printStackTrace();
                System.out.println("An error occurred. Please contact your administrator help.");
                return;
            }
        } while (continueAsking);
    }
}
