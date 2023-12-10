package model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Visit implements Serializable {

    private final Date day;

    private final int hour;

    private final String phoneNumber;

    private final String id;

    private final User user;

    public Visit(Date day, int hour, String phoneNumber, User user) {
        this.day = day;
        this.hour = hour;
        this.phoneNumber = phoneNumber;
        this.id = UUID.randomUUID().toString();
        this.user = user;
    }

    public Date getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }


    @Override
    public String toString() {
        return "Visit{" +
                "day=" + day +
                ", hour=" + hour +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
