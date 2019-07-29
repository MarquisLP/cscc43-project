package Controller.RenterMenus;

import database.RenterRepository;
import database.models.Renter;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class DeleteRenterMenu {
    public static void deleteRenter(Renter renter) {
        System.out.println("--------------------DELETE RENTER ACCOUNT-----------------");
        System.out.println("Are you sure you wish to delete your renter account?");
        System.out.println("This operation will also permanently delete all of your bookings and related data.");

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter DELETE to confirm: ");
        String input = sc.nextLine();

        if (input.equals("DELETE")) {
            try {
                RenterRepository.deleteRenterAccount(renter);
                System.out.println("Your account has been deleted. Thank you for using MyBNB!");
                System.exit(0);
            }
            catch (NoSuchElementException exception) {
                System.out.println("Error: your account doesn't exist in the system.");
            }
            catch (SQLException exception) {
                exception.printStackTrace();
                System.out.println("An error occurred. Please contact your administrator for help.");
            }
        }
        else {
            System.out.println("Delete operation aborted");
        }
    }
}
