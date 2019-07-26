package database.models;


import java.sql.Time;
import java.sql.Timestamp;

public class Booking {

  String listingID;
  Timestamp startDate;
  Timestamp endDate;
  String sin;
  boolean cancelled;


  public Booking(String listingID, Timestamp startDate, Timestamp endDate,
      String sin, boolean cancelled) {
    this.cancelled = cancelled;
    this.endDate = endDate;
    this.startDate = startDate;
    this.listingID = listingID;
    this.sin = sin;
  }


  public String getListingID() {
    return listingID;
  }

  public void setListingID(String listingID) {
    this.listingID = listingID;
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

  public String getSin() {
    return sin;
  }

  public void setSin(String sin) {
    this.sin = sin;
  }

  public boolean isCancelled() {
    return cancelled;
  }

  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

  public String toString() {
    return ("Listing ID: " + getListingID()
        + "\nStart Date: " + getStartDate()
        + "\nEnd Date: " + getEndDate()
        + "\nBooked User SIN: " + getSin()
        + "\nCancelled: " + isCancelled());
  }

}

