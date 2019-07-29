package database.models;

public class ListingReview {

  private String listingID;
  private String renterSin;
  private String comment;
  private int rating;

  public ListingReview() {

  }

  public ListingReview(String listingID, String renterSin, String comment,
      int rating) {
    this.listingID = listingID;
    this.renterSin = renterSin;
    this.comment = comment;
    this.rating = rating;
  }


  public String getListingID() {
    return listingID;
  }

  public void setListingID(String listingID) {
    this.listingID = listingID;
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


  public String toString() {
    return ("listingID = " + listingID +
        "\nrenterSin = " + renterSin +
        "\ncomment = " + comment +
        "\nrating = " + rating);
  }
}
