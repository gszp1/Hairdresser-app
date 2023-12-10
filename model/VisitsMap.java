package model;

import java.util.*;

public class VisitsMap {

    private final Map<VisitTimeKey, Visit> visitsByTime = new HashMap<>();

    synchronized public void addVisit(Visit visit) {
        visitsByTime.put(new VisitTimeKey(visit.getDay(), visit.getHour()), visit);
    }

    synchronized public boolean removeVisit(String visitId) {
        Optional<VisitTimeKey> key = getVisitById(visitId)
                .map(visit -> new VisitTimeKey(visit.getDay(), visit.getHour()));
        if (key.isPresent()){
            visitsByTime.remove(key.get());
            return true;
        }
        return false;
    }

    synchronized public Optional<Visit> getVisitById(String visitId) {
        return visitsByTime.values().stream()
                .filter(visit -> visit.getId().equals(visitId))
                .findFirst();
    }

    synchronized public Optional<Visit> getVisitByDateAndHour(Date date, int hour) {
        return Optional.ofNullable(visitsByTime.get(new VisitTimeKey(date, hour)));
    }

    synchronized public List<Visit> getVisits() {
        return new ArrayList<>(visitsByTime.values());
    }

    synchronized public List<VisitTimeKey> getVisitKeys() { return new ArrayList<>(visitsByTime.keySet());}
}
