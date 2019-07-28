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
            System.out.println("1. Create a new listing");
            System.out.println("2. View my listings");
            System.out.println("3. Edit a listing");
            System.out.println("4. Go to account menu");
            System.out.println("5. View reports");
            System.out.println("6. Exit");

            System.out.print("Enter the number of one of the options above: ");
            input = sc.nextLine();

            switch (input) {
                case "1":
                    break;
                case "2":
                    break;
                case "3":
                    break;
                case "4":
                    break;
                case "5":
                    break;
                case "6":
                    continueRunning = false;
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        } while (continueRunning);
    }
}
