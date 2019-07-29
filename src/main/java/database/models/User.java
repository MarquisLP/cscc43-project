package database.models;

import com.sun.istack.internal.Nullable;

import java.sql.Date;

public class User {
    String sin;
    String name;
    String occupation;
    Date dateOfBirth;
    Address address;

    public User(String sin, String name, Date dateOfBirth, String occupation, Address address) {
        this.sin = sin;
        this.name = name;
        this.occupation = occupation;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public String getSin() {
        return sin;
    }

    public void setSin(String sin) {
        this.sin = sin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "sin: " + getSin() + "\n"
                + "name: " + getName() + "\n"
                + "occupation: " + getOccupation() + "\n"
                + "dateOfBirth: " + getDateOfBirth().toString() + "\n"
                + getAddress().toString();
    }
}
