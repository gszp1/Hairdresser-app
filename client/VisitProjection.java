package client;


import model.VisitDto;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class VisitProjection {

    private final LocalDate date;

    private final int hour;

    private final String phoneNumber;

    private final boolean isRemovable;

    private final String visitId;

    VisitProjection(LocalDate date, int hour, String phoneNumber, boolean isRemovable, String visitId) {
        this.date = date;
        this.hour = hour;
        this.phoneNumber = phoneNumber;
        this.isRemovable = isRemovable;
        this.visitId = visitId;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getHour() {
        return hour;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getVisitId() {
        return visitId;
    }

    public boolean isRemovable() {
        return isRemovable;
    }

    public static List<VisitProjection> fromVisitDtos(List<VisitDto> visitDtos) {
        return visitDtos.stream().map(visit ->
                new VisitProjection(
                    convertDateToLocalDate(visit.getDay()),
                    visit.getHour(),
                    visit.getPhoneNumber(),
                    visit.isMyVisit(),
                    visit.getId()
                )
        ).collect(Collectors.toList());
    }

    public static LocalDate convertDateToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}