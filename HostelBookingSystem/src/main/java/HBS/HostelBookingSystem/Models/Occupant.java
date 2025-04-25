package HBS.HostelBookingSystem.Models;

import java.sql.Date;

public class Occupant {
    private int id;
    private String name;
    private String contact;
    private int roomNumber;
    private Date checkIn;
    private Date checkOut;

    public Occupant(String name, String contact, int roomNumber, Date checkIn) {
        this.name = name;
        this.contact = contact;
        this.roomNumber = roomNumber;
        this.checkIn = checkIn;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getContact() { return contact; }
    public int getRoomNumber() { return roomNumber; }
    public Date getCheckIn() { return checkIn; }
    public Date getCheckOut() { return checkOut; }

    public void setCheckOut(Date checkOut) { this.checkOut = checkOut; }
}
