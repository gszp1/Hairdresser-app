package model;

import java.util.Date;
import java.util.Objects;

public class VisitTimeKey {

    private final Date date;

    private final int hour;

    public VisitTimeKey(Date date, int hour) {
        this.date = date;
        this.hour = hour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisitTimeKey that = (VisitTimeKey) o;
        return hour == that.hour && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, hour);
    }
}
