package Controller;

import database.ReportRepository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.TimeZone;

public class ReportsMenu {
    public static void reportsMenu() {
        Scanner sc = new Scanner(System.in);
        String input;
        boolean continueRunning = true;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        do {
            System.out.println("================== REPORTS MENU ======================");
            System.out.println("0. Return to previous menu");
            System.out.println("1. Total number of bookings by city in date range");
            System.out.println("2. Total number of bookings by postal code in date range");
            System.out.println("3. Total number of listings by city");
            System.out.println("4. Total number of listings by city and country");
            System.out.println("5. Total number of listings by city, country, and postal code");
            System.out.println("6. Ranking of hosts by number of listings per country");
            System.out.println("7. Ranking of hosts by number of listings per city");
            System.out.println("8. Hosts who have more than 10% of all listings in city and country");

            System.out.print("Enter the number of one of the options above: ");
            input = sc.nextLine();

            switch (input) {
                case "0":
                    continueRunning = false;
                    break;
                case "1":
                    Timestamp startDate = null;
                    do {
                        System.out.print("Enter start date in yyyy-MM-dd format: ");
                        input = sc.nextLine();
                        try {
                            startDate = new Timestamp((dateFormat.parse(input).getTime()));
                        } catch (ParseException exception) {
                            System.out.println("Invalid format for start date");
                        }
                    } while (startDate == null);

                    Timestamp endDate = null;
                    do {
                        System.out.print("Enter end date in yyyy-MM-dd format: ");
                        input = sc.nextLine();
                        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                        try {
                            endDate = new Timestamp((dateFormat.parse(input).getTime()));
                        } catch (ParseException exception) {
                            System.out.println("Invalid format for end date");
                        }
                    } while (endDate == null);

                    try {
                        ReportRepository.reportByDateRangeInCity(dateFormat.format(startDate), dateFormat.format(endDate));
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                        System.out.println("An error occurred. Please contact your administrator for help.");
                    }
                    break;
                case "2":
                    System.out.print("Enter postal code: ");
                    String postalCode = sc.nextLine();

                    Timestamp startDate2 = null;
                    do {
                        System.out.print("Enter start date in yyyy-MM-dd format: ");
                        input = sc.nextLine();
                        try {
                            startDate2 = new Timestamp((dateFormat.parse(input).getTime()));
                        } catch (ParseException exception) {
                            System.out.println("Invalid format for start date");
                        }
                    } while (startDate2 == null);

                    Timestamp endDate2 = null;
                    do {
                        System.out.print("Enter end date in yyyy-MM-dd format: ");
                        input = sc.nextLine();
                        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                        try {
                            endDate2 = new Timestamp((dateFormat.parse(input).getTime()));
                        } catch (ParseException exception) {
                            System.out.println("Invalid format for end date");
                        }
                    } while (endDate2 == null);

                    try {
                        ReportRepository.reportByDateRangeInCityWithPostalCode(dateFormat.format(startDate2), dateFormat.format(endDate2), postalCode);
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                        System.out.println("An error occurred. Please contact your administrator for help.");
                    }
                    break;
                case "3":
                    try {
                        ReportRepository.reportlistingpercountry();
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                        System.out.println("An error occurred. Please contact your administrator for help.");
                    }
                    break;
                case "4":
                    try {
                        ReportRepository.reportlistingpercountryandcity();
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                        System.out.println("An error occurred. Please contact your administrator for help.");
                    }
                    break;
                case "5":
                    try {
                        ReportRepository.reportlistingpercountryandcityandpostal();
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                        System.out.println("An error occurred. Please contact your administrator for help.");
                    }
                    break;
                case "6":
                    try {
                        ReportRepository.rankHostByTotalListingPerCountry();
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                        System.out.println("An error occurred. Please contact your administrator for help.");
                    }
                    break;
                case "7":
                    try {
                        ReportRepository.rankHostByTotalListingRefinedToCity();
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                        System.out.println("An error occurred. Please contact your administrator for help.");
                    }
                    break;
                default:
                    break;
            }
        } while (continueRunning);
    }

}
