package database.models;

import java.sql.Date;

public class Renter extends User {
    String creditCardNumber;

    public Renter(String sin, String name, Date dateOfBirth, String occupation, Address address, String creditCardNumber) {
        super(sin, name, dateOfBirth, occupation, address);
        this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }
}
