package messages.requests;

import messages.Message;
import messages.MessageType;
import messages.responses.AddVisitResponse;
import model.User;
import model.UserList;
import model.Visit;
import model.VisitsMap;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddVisitRequest extends Message implements Serializable {

    private Date date;

    private int hour;

    private String phoneNumber;

    public AddVisitRequest(String clientId, Date date, int hour, String phoneNumber){
        super(MessageType.ADD_VISIT_REQUEST, clientId, String.format("%s %s %s", date, hour, phoneNumber));
        this.date = date;
        this.hour = hour;
        this.phoneNumber = phoneNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean handleMessage(UserList userList, VisitsMap visitsMap) throws IOException {
        System.out.println("Received visit addition request.");
        Optional<User> user = userList.getUser(this.clientId);
        if (!user.isPresent()) {
            return false;
        }
        Optional<String> maybeError = validateVisit(visitsMap);
        if (maybeError.isPresent()) {
            user.get().getOutputStream().writeObject(
                    new AddVisitResponse(clientId, maybeError.get(), false)
            );
            return false;
        }

        Visit newVisit = new Visit(date, hour, phoneNumber, user.get());
        visitsMap.addVisit(newVisit);
        user.get().getOutputStream().writeObject(
                new AddVisitResponse(clientId, newVisit.getId(), true)
        );
        VisitsListRequest.broadcastVisitList(userList, visitsMap);
        return true;
    }

    private Optional<String> validateVisit(VisitsMap visitsMap) {
        if (!isValidPhoneNumber(phoneNumber)){
            return Optional.of(String.format("%s is not a valid phone number", phoneNumber));
        }
        if (hour < 10 || hour > 17) {
            return Optional.of(String.format("%s hour must be between 10 and 18", hour));
        }
        Optional<Visit> existingVisit = visitsMap.getVisitByDateAndHour(date, hour);
        if (existingVisit.isPresent()) {
            return Optional.of("Visit already exists");
        }
        return Optional.empty();
    }

    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\+?\\d[\\d -]{7,14}\\d$");

    public static boolean isValidPhoneNumber(String phoneNumber) {
        Matcher matcher = PHONE_NUMBER_PATTERN.matcher(phoneNumber);
        return matcher.matches();
    }
}
