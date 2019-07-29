package Controller.HostMenus;

import database.FindValidUserRepository;
import database.models.Host;
import database.models.HostRenterListingTuples;

import java.sql.SQLException;
import java.util.List;

public class ViewInteractedRentersMenu {
    public static void viewInteractedRenters(Host host) {
        List<HostRenterListingTuples> tuples = null;

        try {
            tuples = FindValidUserRepository.getRentersUsingHostSIN(host.getSin());
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("An error occurred. Please contact your administrator for help.");
            return;
        }

        if (tuples != null) {
            System.out.println("-----------------RENTERS THAT HAVE RENTED FROM ME--------------------");
            if (tuples.size() == 0) {
                System.out.println("No one has submitted a review for you yet.");
            }
            else {
                for (HostRenterListingTuples tuple : tuples) {
                    System.out.println("Renter SIN: " + tuple.getRenterSin()
                            + ", Listing they rented: " + tuple.getListingId());
                    System.out.println("---------------------------------------------------------------");
                }
            }
        }
    }
}
