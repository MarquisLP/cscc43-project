package database.models;

public class UserReview {

  private String hostSin;
  private String renterSin;
  private String comment;
  private int rating;

  public UserReview() {

  }

  public UserReview(String hostSin, String renterSin, String comment, int
      rating) {
    this.hostSin = hostSin;
    this.renterSin = renterSin;
    this.comment = comment;
    this.rating = rating;
  }

  public String getHostSin() {
    return hostSin;
  }

  public void setHostSin(String hostSin) {
    this.hostSin = hostSin;
  }

  public String getRenterSin() {
    return renterSin;
  }

  public void setRenterSin(String renterSin) {
    this.renterSin = renterSin;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }
}
