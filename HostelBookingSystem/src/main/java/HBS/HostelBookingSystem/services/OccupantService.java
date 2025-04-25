package HBS.HostelBookingSystem.services;

import java.sql.*;

import HBS.HostelBookingSystem.DB.DBConnection;
import HBS.HostelBookingSystem.Models.Occupant;

public class OccupantService {
	public void bookRoom(Occupant occupant) {
	    String checkCapacity = "SELECT COUNT(*) FROM occupants WHERE room_number = ? AND check_out IS NULL";
	    String insert = "INSERT INTO occupants (name, contact, room_number, check_in) VALUES (?, ?, ?, ?)";

	    try (Connection conn = DBConnection.getConnection()) {
	        // Check if room is already occupied
	        PreparedStatement p = conn.prepareStatement(checkCapacity);
	        p.setInt(1, occupant.getRoomNumber());
	        ResultSet rs = p.executeQuery();

	        int count = 0;
	        if (rs.next()) {
	            count = rs.getInt(1);
	        }

	        if (count < 1) {
	            // ✅ Corrected: Include RETURN_GENERATED_KEYS here
	            PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
	            ps.setString(1, occupant.getName());
	            ps.setString(2, occupant.getContact());
	            ps.setInt(3, occupant.getRoomNumber());
	            ps.setDate(4, occupant.getCheckIn());

	            int i = ps.executeUpdate();
	            if (i > 0) {
	                ResultSet generatedKeys = ps.getGeneratedKeys();
	                if (generatedKeys.next()) {
	                    int occupantId = generatedKeys.getInt(1);
	                    System.out.println("Room Booked Successfully!");
	                    System.out.println("Your Occupant ID is: " + occupantId);
	                    System.out.println("Use this ID to checkout later.");
	                }
	                new RoomService().updateRoomStatus(occupant.getRoomNumber(), true);
	            } else {
	                System.out.println("Unable to book the room");
	            }
	        } else {
	            System.out.println("Room already occupied");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}



	public void viewOccupants() {
		try (Connection conn = DBConnection.getConnection()) {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM occupants");
			while (rs.next()) {
				System.out.println("Name: " + rs.getString("name") + ", Room: " + rs.getInt("room_number")
						+ ", Check-in: " + rs.getDate("check_in") + ", Check-out: " + rs.getDate("check_out"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void checkOut(int occupantId, Date checkoutDate) {
		try (Connection conn = DBConnection.getConnection()) {
			String query = "UPDATE occupants SET check_out = ? WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setDate(1, checkoutDate);
			ps.setInt(2, occupantId);
			int c=ps.executeUpdate();
			if(c>0) {
				System.out.println("Successfully Checkout");
			}
			else {
				System.out.println("Unable to checkout");
			}
			ResultSet rs = conn.createStatement()
					.executeQuery("SELECT room_number FROM occupants WHERE id = " + occupantId);
			if (rs.next()) {
				int roomNumber = rs.getInt("room_number");
				new RoomService().updateRoomStatus(roomNumber, false);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void changeRoom(int occupantId, int newRoomNumber) {
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);

			// 1. Get current room
			String getCurrentRoom = "SELECT room_number FROM occupants WHERE id = ?";
			PreparedStatement ps1 = conn.prepareStatement(getCurrentRoom);
			ps1.setInt(1, occupantId);
			ResultSet rs = ps1.executeQuery(); 

			if (!rs.next()) {
				System.out.println("Occupant not found!");
				return;
			}
			int oldRoom = rs.getInt("room_number");

			// 2. Check new room availability
			String checkRoom = "SELECT is_occupied FROM rooms WHERE room_number = ?";
			PreparedStatement ps2 = conn.prepareStatement(checkRoom);
			ps2.setInt(1, newRoomNumber);
			ResultSet rs2 = ps2.executeQuery();

			if (!rs2.next()) {
				System.out.println("New room doesn't exist!");
				return;
			}
			if (rs2.getBoolean("is_occupied")) {
				System.out.println("New room is already occupied!");
				return;
			}

			// 3. Update occupant record
			String updateOccupant = "UPDATE occupants SET room_number = ? WHERE id = ?";
			PreparedStatement ps3 = conn.prepareStatement(updateOccupant);
			ps3.setInt(1, newRoomNumber);
			ps3.setInt(2, occupantId);
			ps3.executeUpdate();

			// 4. Update room statuses
			String updateRooms = "UPDATE rooms SET is_occupied = ? WHERE room_number = ?";

			// Free old room
			PreparedStatement ps4 = conn.prepareStatement(updateRooms);
			ps4.setBoolean(1, false);
			ps4.setInt(2, oldRoom);
			ps4.executeUpdate();

			// Occupy new room
			PreparedStatement ps5 = conn.prepareStatement(updateRooms);
			ps5.setBoolean(1, true);
			ps5.setInt(2, newRoomNumber);
			ps5.executeUpdate();

			conn.commit();
			System.out.println("Room changed successfully!");

		} catch (SQLException e) {
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.setAutoCommit(true);
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
