package HBS.HostelBookingSystem.services;

import HBS.HostelBookingSystem.DB.DBConnection;
import HBS.HostelBookingSystem.Models.Room;

import java.sql.*;

public class RoomService {
	public void updateRoomStatus(int roomNumber, boolean isOccupied) {
		try (Connection conn = DBConnection.getConnection()) {
			String query = "UPDATE rooms SET is_occupied = ? WHERE room_number = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setBoolean(1, isOccupied);
			ps.setInt(2, roomNumber);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void showAvailableRooms() {
		try (Connection conn = DBConnection.getConnection()) {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM rooms WHERE is_occupied = false");
			while (rs.next()) {
				System.out.println("Room Number: " + rs.getInt("room_number"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addRoom(Room room) {
		String sql = "INSERT INTO rooms (room_number, is_occupied) VALUES (?, ?)";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, room.getRoomNumber());
			pstmt.setBoolean(2, room.isOccupied());

			int rowsInserted = pstmt.executeUpdate();

			if (rowsInserted > 0) {
				System.out.println("Room added successfully.");
			} else {
				System.out.println("Failed to add room.");
			}

		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public void showAllRooms() {
		try (Connection conn = DBConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM rooms")) {
			while (rs.next()) {
				System.out.println("Room: " + rs.getInt("room_number") + ", Occupied: " + ((rs.getBoolean("is_occupied") == true)?"yes":"No"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateRoomOccupancyAsAdmin(int roomNumber, boolean isOccupied) {
		String sql = "UPDATE rooms SET is_occupied = ? WHERE room_number = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setBoolean(1, isOccupied);
			pstmt.setInt(2, roomNumber);

			int rowsUpdated = pstmt.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("Room occupancy updated successfully.");
			} else {
				System.out.println("Room not found.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteRoom(int roomNumber) {
		try (Connection conn = DBConnection.getConnection()) {
			conn.setAutoCommit(false);

			// Delete maintenance requests for the room
			String deleteMaintenance = "DELETE FROM maintenance_requests WHERE room_number = ?";
			try (PreparedStatement ps1 = conn.prepareStatement(deleteMaintenance)) {
				ps1.setInt(1, roomNumber);
				ps1.executeUpdate();
			}

			// Check if room has occupants
			String checkOccupants = "SELECT COUNT(*) FROM occupants WHERE room_number = ? AND check_out IS NULL";
			try (PreparedStatement ps2 = conn.prepareStatement(checkOccupants)) {
				ps2.setInt(1, roomNumber);
				ResultSet rs = ps2.executeQuery();
				if (rs.next() && rs.getInt(1) > 0) {
					System.out.println("Cannot delete room: occupants are currently residing.");
					conn.rollback();
					return;
				}
			}

			// Delete the room
			String deleteRoom = "DELETE FROM rooms WHERE room_number = ?";
			try (PreparedStatement ps3 = conn.prepareStatement(deleteRoom)) {
				ps3.setInt(1, roomNumber);
				int rows = ps3.executeUpdate();
				if (rows > 0) {
					System.out.println("Room deleted successfully.");
				} else {
					System.out.println("Room not found.");
				}
			}

			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
