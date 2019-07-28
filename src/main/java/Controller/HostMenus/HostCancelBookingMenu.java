package Controller.HostMenus;

import database.BookingRepository;
import database.models.Address;
import database.models.Host;
import database.models.User;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.TimeZone;

public class HostCancelBookingMenu {
    public static void cancelBooking(Host host) {
        Scanner sc = new Scanner(System.in);
        String input;
        boolean continueAsking = true;

        System.out.println("---------CANCEL A BOOKING---------");
        do {
            System.out.println("Specify the booking to cancel using this format:");
            System.out.println("    listingID startDate endDate");
            System.out.println("where startDate and endDate are in this format: yyyy-MM-dd");
            System.out.print("Enter the booking to cancel (or leave blank to exit): ");
            input = sc.nextLine();

            if (input.equals("")) {
                break;
            }

            String[] fields = input.split("\\s+");
            if (fields.length != 3) {
                System.out.println("Invalid number of fields");
                continue;
            }

            String listingId = fields[0];

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Timestamp startDate;
            try {
                startDate = new Timestamp((dateFormat.parse(fields[1]).getTime()));
                System.out.println(startDate.toString());
            } catch (ParseException exception) {
                System.out.println("Invalid format for start date");
                continue;
            }

            Timestamp endDate;
            try {
                endDate = new Timestamp((dateFormat.parse(fields[2]).getTime()));
            } catch (ParseException exception) {
                System.out.println("Invalid format for end date");
                continue;
            }

            try {
                BookingRepository.cancelBookingAsHost(listingId, startDate.toString(), endDate.toString(), host);
                continueAsking = false;
                System.out.println("Booking cancelled successfully!");
            }
            catch (NoSuchElementException exception) {
                System.out.println("The booking you specified does not exist. Please try again.");
            }
            catch (IllegalArgumentException exception) {
                System.out.println("You don't have permission to modify this listing.");
            }
            catch (SQLException exception) {
                exception.printStackTrace();
                System.out.println("An error occurred. Please contact your administrator for help.");
                continueAsking = false;
            }
        } while (continueAsking);
    }
}
