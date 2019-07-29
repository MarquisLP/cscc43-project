package Controller.HostMenus;

import database.ReviewRepository;
import database.models.Host;
import database.models.UserReview;

import java.sql.SQLException;
import java.util.List;

public class ViewHostReviewsMenu {
    public static void viewHostReviews(Host host) {
        List<UserReview> hostReviews = null;

        try {
            hostReviews = ReviewRepository.getAllHostReviewsByHostSin(host.getSin());
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("An error occurred. Please contact your administrator for help.");
            return;
        }

        if (hostReviews != null) {
            System.out.println("-----------------REVIEWS ABOUT ME--------------------");
            if (hostReviews.size() == 0) {
                System.out.println("No one has submitted a review for you yet.");
            }
            else {
                for (UserReview hostReview : hostReviews) {
                    System.out.println(hostReview);
                    System.out.println("-----------------------------------------------");
                }
            }
        }
    }
}
