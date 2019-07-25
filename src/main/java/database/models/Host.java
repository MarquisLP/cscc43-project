package database.models;

import java.sql.Date;

public class Host extends User {
    public Host(String sin, String name, Date dateOfBirth, String occupation, Address address) {
        super(sin, name, dateOfBirth, occupation, address);
    }
}
