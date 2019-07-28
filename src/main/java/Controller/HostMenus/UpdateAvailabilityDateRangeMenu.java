package Controller.HostMenus;

import database.AvailabilityRepository;
import database.models.Availability;
import database.models.Host;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.TimeZone;

public class UpdateAvailabilityDateRangeMenu {
    public static void updateAvailabilityDateRange(Host host) {
        System.out.println("---------UPDATE AVAILABILITY PRICE---------");

        Scanner sc = new Scanner(System.in);
        String input;
        boolean continueAsking = true;

        do {
            System.out.println("Use the following format:");
            System.out.println("    listingID oldStartDate oldEndDate newStartDate newEndDate");
            System.out.println("where all four dates follow this format: yyyy-MM-dd");
            System.out.print("Enter availability (or leave blank to exit): ");
            input = sc.nextLine();

            if (input.equals("")) {
                return;
            }

            String[] fields = input.split("\\s+");
            if (fields.length != 5) {
                System.out.println("Invalid number of fields");
                continue;
            }

            String listingId = fields[0];

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Timestamp oldStartDate;
            try {
                oldStartDate = new Timestamp((dateFormat.parse(fields[1]).getTime()));
            } catch (ParseException exception) {
                System.out.println("Invalid format for old start date");
                continue;
            }

            Timestamp oldEndDate;
            try {
                oldEndDate = new Timestamp((dateFormat.parse(fields[2]).getTime()));
            } catch (ParseException exception) {
                System.out.println("Invalid format for old end date");
                continue;
            }

            Timestamp newStartDate;
            try {
                newStartDate = new Timestamp((dateFormat.parse(fields[3]).getTime()));
            } catch (ParseException exception) {
                System.out.println("Invalid format for new start date");
                continue;
            }

            Timestamp newEndDate;
            try {
                newEndDate = new Timestamp((dateFormat.parse(fields[4]).getTime()));
            } catch (ParseException exception) {
                System.out.println("Invalid format for new end date");
                continue;
            }

            Availability availability = new Availability(listingId, oldStartDate, oldEndDate, 0); // price field isn't used
            try {
                AvailabilityRepository.updateAvailabilityDateRange(availability, newStartDate, newEndDate);
                continueAsking = false;
                System.out.println("Availability date range updated successfully!");
            } catch (IllegalArgumentException exception) {
                System.out.println("You may not edit an availability slot if it has already been booked.");
            } catch (NoSuchElementException exception) {
                System.out.println("The given availability does not exist.");
            } catch (SQLException exception) {
                System.out.println("An error occurred. Please contact your administrator for help.");
                return;
            }
        } while (continueAsking);
    }
}
