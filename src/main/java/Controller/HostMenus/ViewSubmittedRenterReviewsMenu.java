package Controller.HostMenus;

import database.ReviewRepository;
import database.models.Host;
import database.models.UserReview;

import java.sql.SQLException;
import java.util.List;

public class ViewSubmittedRenterReviewsMenu {
    public static void viewSubmittedRenterReviews(Host host) {
        List<UserReview> renterReviews = null;

        try {
            renterReviews = ReviewRepository.getAllRenterReviewsByHostSin(host.getSin());
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("An error occurred. Please contact your administrator for help.");
            return;
        }

        if (renterReviews != null) {
            System.out.println("-----------------MY RENTER REVIEWS--------------------");
            if (renterReviews.size() == 0) {
                System.out.println("You have not submitted any renter reviews yet");
            }
            else {
                for (UserReview hostReview : renterReviews) {
                    System.out.println(hostReview);
                    System.out.println("-----------------------------------------------");
                }
            }
        }
    }
}
