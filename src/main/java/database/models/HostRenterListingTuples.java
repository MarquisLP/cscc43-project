package database.models;

public class HostRenterListingTuples {
  private String hostSin;
  private String renterSin;
  private String listingId;

  public HostRenterListingTuples() {
  }

  public HostRenterListingTuples(String hostSin, String renterSin,
      String listingId) {
    this.hostSin = hostSin;
    this.renterSin = renterSin;
    this.listingId = listingId;
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

  public String getListingId() {
    return listingId;
  }

  public void setListingId(String listingId) {
    this.listingId = listingId;
  }


  public String toString() {
    return ("hostSin = " + hostSin +
        "\nrenterSin = " + renterSin +
        "\nlistingId = " + listingId);
  }
}
