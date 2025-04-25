package HBS.HostelBookingSystem.Models;

public class Room {

	private int roomNumber;
    private boolean isOccupied;

    public Room(int roomNumber, boolean isOccupied) {
        this.roomNumber = roomNumber;
        this.isOccupied = isOccupied;
    }

    public int getRoomNumber() { return roomNumber; }
    public boolean isOccupied() { return isOccupied; }

    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }
    public void setOccupied(boolean occupied) { isOccupied = occupied; }
}
