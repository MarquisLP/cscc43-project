package database.models;

import java.sql.Timestamp;

public class Availability {
    Timestamp startDate;
    Timestamp endDate;
    int price;

    public Availability(Timestamp startDate, Timestamp endDate, int price) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
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
}
