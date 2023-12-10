package model;

import java.io.Serializable;
import java.util.Date;

public class VisitDto implements Serializable {

    private final Date day;

    private final int hour;

    private final String phoneNumber;

    private final String id;

    private final String userId;

    private final boolean myVisit;

    public VisitDto(
            Date day,
            int hour,
            String phoneNumber,
            String id,
            String userId,
            boolean myVisit
    ) {
        this.day = day;
        this.hour = hour;
        this.phoneNumber = phoneNumber;
        this.id = id;
        this.userId = userId;
        this.myVisit = myVisit;
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


    public String getUserId() {
        return userId;
    }

    public String getId() {
        return id;
    }

    public boolean isMyVisit() {
        return myVisit;
    }

}
