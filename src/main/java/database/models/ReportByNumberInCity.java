package database.models;

import java.security.PublicKey;

public class ReportByNumberInCity {

  private String cityName;
  private int number;

  public ReportByNumberInCity() {

  }

  public ReportByNumberInCity(String cityName, int number) {
    this.cityName = cityName;
    this.number = number;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }


  public String toString() {
    return ("Report:"
        + "\ncityName = " + cityName
        + "\nnumber = " + number);
  }
}
