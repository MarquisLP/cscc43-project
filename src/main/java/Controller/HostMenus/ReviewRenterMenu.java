package Controller.HostMenus;

import database.ReviewRepository;
import database.models.Host;

import java.sql.SQLException;
import java.util.Scanner;

public class ReviewRenterMenu {
    public static void reviewRenter(Host host) {
        System.out.println("-------------REVIEW A RENTER---------------");

        Scanner sc = new Scanner(System.in);
        String input;

        System.out.print("Enter the Renter's SIN (or leave blank to exit): ");
        input = sc.nextLine();
        if (input.equals("")) {
            return;
        }
        String renterSin = input;

        Integer rating = null;
        do {
            System.out.print("Enter your rating of this renter between 1 and 5 (or leave blank to exit): ");
            input = sc.nextLine();
            if (input.equals("")) {
                return;
            }

            try {
                rating = Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                System.out.println("Rating must be a whole number");
            }

            if ((rating != null) && ((rating < 1) || (rating > 5))) {
                System.out.println("Rating must be between 1 and 5");
                rating = null;
            }
        } while (rating == null);

        String comment;
        System.out.print("Enter comment; this is optional, so type N/A if you wish to leave it blank (or type nothing and press enter to exit): ");
        input = sc.nextLine();
        if (input.equals("")) {
            return;
        }
        else if (input.equals("N/A")) {
            comment = "";
        }
        else {
            comment = input;
        }

        try {
            ReviewRepository.createRenterReview(host.getSin(), renterSin, comment, rating);
            System.out.println("Review submitted successfully!");
        } catch (IllegalArgumentException exception) {
            System.out.println("Error: You can only submit reviews for renters that have rented at least one of your listings in the past.");
        } catch (SQLException exception) {
            System.out.println("An error occurred. Please contact your administrator for help.");
        }
    }
}
