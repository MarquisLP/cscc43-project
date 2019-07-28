package Controller.HostMenus;

import database.ListingRepository;
import database.models.Host;
import database.models.Listing;

import java.sql.SQLException;
import java.util.List;

public class ViewHostListingsMenu {
    public static void viewHostListings(Host host) {
        List<Listing> listings = null;

        System.out.println();

        try {
            listings = ListingRepository.getAllListingsForHost(host.getSin());
        } catch (SQLException exception) {
            System.out.println("An error occurred. Please contact your administrator for help.");
        }

        if (listings != null) {
            System.out.println("==========MY LISTINGS==========");
            if (listings.size() == 0) {
                System.out.println("You currently have no listings.");
            }
            else {
                for (Listing listing : listings) {
                    System.out.println(listing);
                    System.out.println("------------------------------------");
                }
            }
        }
    }
}
