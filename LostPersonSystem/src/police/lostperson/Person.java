package police.lostperson;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** Simple data holder for one lost-person record. */
public class Person {
    private final String id;
    private final String fullName;
    private final int age;
    private final String gender;
    private final String height;
    private final String lastSeenLocation;
    private final LocalDate dateLastSeen;
    private final String clothing;
    private final String features;
    private final String contactName;
    private final String contactPhone;
    private final String notes;
    private final LocalDateTime reportedOn = LocalDateTime.now();

    Person(String id, String fullName, int age, String gender, String height,
               String lastSeenLocation, LocalDate dateLastSeen, String clothing,
               String features, String contactName, String contactPhone, String notes) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.lastSeenLocation = lastSeenLocation;
        this.dateLastSeen = dateLastSeen;
        this.clothing = clothing;
        this.features = features;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.notes = notes;
    }

    String getId() { return id; }
    String getFullName() { return fullName; }
    int getAge() { return age; }
    String getGender() { return gender; }
    String getHeight() { return height; }
    String getLastSeenLocation() { return lastSeenLocation; }
    LocalDate getDateLastSeen() { return dateLastSeen; }
    String getClothing() { return clothing; }
    String getFeatures() { return features; }
    String getContactName() { return contactName; }
    String getContactPhone() { return contactPhone; }
    String getNotes() { return notes; }
    LocalDateTime getReportedOn() { return reportedOn; }
}
