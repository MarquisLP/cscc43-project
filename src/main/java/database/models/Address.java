package database.models;

public class Address {
    String postalCode;
    String city;
    String country;

    public Address() {
    }

    public Address(String postalCode, String city, String country) {
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String toString() {
        return ("Country: " + getCountry()
            + "\nCity: " + getCity()
            + "\nPostal Code: " + getPostalCode());
    }
}
