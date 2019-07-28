package database.models;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class Availability {

  String listingId;
  Timestamp startDate;
  Timestamp endDate;
  int price;

  public Availability(String listingId, Timestamp startDate, Timestamp endDate, int price) {
    this.listingId = listingId;
    this.startDate = startDate;
    this.endDate = endDate;
    this.price = price;
  }

  public String getListingId() {
    return listingId;
  }

  public void setListingId(String listingId) {
    this.listingId = listingId;
  }

  public Timestamp getStartDate() {
    return startDate;
  }

  public void setStartDate(Timestamp startDate) {
    this.startDate = startDate;
  }

  public Timestamp getEndDate() {
    return endDate;
  }

  public void setEndDate(Timestamp endDate) {
    this.endDate = endDate;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public String toString() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    return ("ListingID: " + getListingId()
        + ", Start: " + dateFormat.format(getStartDate())
        + ", End: " + dateFormat.format(getEndDate())
        + ", Price: " + getPrice());
  }
}
