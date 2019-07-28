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
            System.out.println("3. Review a renter");
            System.out.println("4. Go to account menu");
            System.out.println("5. View reports");

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
                    break;
                case "4":
                    break;
                case "5":
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        } while (continueRunning);
    }
}
