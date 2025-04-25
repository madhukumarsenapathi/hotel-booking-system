package HBS.HostelBookingSystem.Models;

public class MaintenanceRequest {
    private int id;
    private int roomNumber;
    private String issue;
    private String status;

    public MaintenanceRequest(int roomNumber, String issue, String status) {
        this.roomNumber = roomNumber;
        this.issue = issue;
        this.status = status;
    }

    public int getRoomNumber() { return roomNumber; }
    public String getIssue() { return issue; }
    public String getStatus() { return status; }
}
