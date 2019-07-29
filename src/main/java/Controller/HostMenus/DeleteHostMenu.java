package Controller.HostMenus;

import database.HostRepository;
import database.models.Host;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class DeleteHostMenu {
    public static void deleteHost(Host host) {
        System.out.println("--------------------DELETE HOST ACCOUNT-----------------");
        System.out.println("Are you sure you wish to delete your host account?");
        System.out.println("This operation will also permanently delete all of your listings and related data.");

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter DELETE to confirm: ");
        String input = sc.nextLine();

        if (input.equals("DELETE")) {
            try {
                HostRepository.deleteHostAccount(host);
                System.out.println("Your account has been deleted. Thank you for using MyBNB!");
                System.exit(0);
            }
            catch (IllegalArgumentException exception) {
                System.out.println("Error: some or all of your listings could not be deleted.");
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
