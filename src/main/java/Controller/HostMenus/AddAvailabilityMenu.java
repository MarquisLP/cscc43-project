package Controller.HostMenus;

import database.AvailabilityRepository;
import database.models.Availability;
import database.models.Host;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class AddAvailabilityMenu {
    public static void addAvailability(Host host) {
        System.out.println("---------ADD AVAILABILITY---------");

        Scanner sc = new Scanner(System.in);
        String input;
        boolean continueAsking = true;

        do {
            System.out.println("Use the following format:");
            System.out.println("    listingID startDate endDate price");
            System.out.println("where price is a whole number,");
            System.out.println("and startDate and endDate follow this format: yyyy-MM-dd");
            System.out.print("Enter availability (or leave blank to exit): ");
            input = sc.nextLine();

            if (input.equals("")) {
                return;
            }

            String[] fields = input.split("\\s+");
            if (fields.length != 4) {
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

            int price;
            try {
                price = Integer.parseInt(fields[3]);
            } catch (NumberFormatException exception) {
                System.out.println("Price must be a whole number");
                continue;
            }

            Availability availability = new Availability(listingId, startDate, endDate, price);
            try {
                AvailabilityRepository.createAvailability(availability);
                continueAsking = false;
                System.out.println("Availability added successfully!");
            }
            catch (UnsupportedOperationException exception) {
                System.out.println("Starting date must be later than ending date");
            }
            catch (IndexOutOfBoundsException exception) {
                System.out.println("The given listing already has an availability defined in that date range.");
            }
            catch (SQLException exception) {
                System.out.println("An error occurred. Please contact your administrator for help.");
                return;
            }
        } while (continueAsking);
    }
}
