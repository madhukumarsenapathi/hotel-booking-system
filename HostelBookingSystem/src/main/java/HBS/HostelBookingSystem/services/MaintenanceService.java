package HBS.HostelBookingSystem.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import HBS.HostelBookingSystem.DB.DBConnection;
import HBS.HostelBookingSystem.Models.MaintenanceRequest;

public class MaintenanceService {
	public void submitRequest(MaintenanceRequest request) {
	    String insert = "INSERT INTO maintenance_requests (room_number, issue, status) VALUES (?, ?, ?)";

	    try (Connection conn = DBConnection.getConnection()) {
	        // Use RETURN_GENERATED_KEYS to get the auto-generated request_id
	        PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
	        ps.setInt(1, request.getRoomNumber());
	        ps.setString(2, request.getIssue());
	        ps.setString(3, request.getStatus());

	        int i = ps.executeUpdate();
	        if (i > 0) {
	            ResultSet rs = ps.getGeneratedKeys();
	            if (rs.next()) {
	                int requestId = rs.getInt(1);
	                System.out.println("Request submitted successfully!");
	                System.out.println("Your Maintenance Request ID is: " + requestId);
	            }
	        } else {
	            System.out.println("Failed to submit the maintenance request.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


public void updateRequestStatus(int requestId, String newStatus) {
    try (Connection conn = DBConnection.getConnection()) {
        String query = "UPDATE maintenance_requests SET status = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, newStatus);
        ps.setInt(2, requestId);
        int i=ps.executeUpdate();
        if(i>0) {
        	System.out.println("Status updated successfully");
        }
        else {
        	System.out.println("Unable to update the status");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}


