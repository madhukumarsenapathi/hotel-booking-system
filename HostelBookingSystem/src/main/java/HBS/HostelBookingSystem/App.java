package HBS.HostelBookingSystem;

import java.sql.Date;
import java.util.Scanner;

import HBS.HostelBookingSystem.Models.MaintenanceRequest;
import HBS.HostelBookingSystem.Models.Occupant;
import HBS.HostelBookingSystem.Models.Room;
import HBS.HostelBookingSystem.services.MaintenanceService;
import HBS.HostelBookingSystem.services.OccupantService;
import HBS.HostelBookingSystem.services.RoomService;

public class App {
	static Scanner sc = new Scanner(System.in);
	static RoomService roomService = new RoomService();
	static OccupantService occupantService = new OccupantService();
	static MaintenanceService maintenanceService = new MaintenanceService();

	private static void adminlogin() {
		while (true) {
			System.out.println("\n--- Hostel Booking System Menu ---");
			System.out.println("1. Add Room ");
			System.out.println("2. Change Occupant's Room )");
			System.out.println("3. Delete Room ");
			System.out.println("4. View Occupants");
			System.out.println("5. Exit");
			System.out.print("Enter your choice: ");
			int choice = sc.nextInt();

			switch (choice) {

			case 1:
				System.out.print("Enter new room number: ");
				int roomNo = sc.nextInt();
				Room newRoom = new Room(roomNo, false); // Initially not occupied
				roomService.addRoom(newRoom);

				break;
			case 2:
				System.out.print("Enter Occupant ID: ");
				int occupantId = sc.nextInt();
				System.out.print("Enter New Room Number: ");
				int newR = sc.nextInt();
				occupantService.changeRoom(occupantId, newR);
				break;

			case 3:
				System.out.print("Enter room number to delete: ");
				int delRoom = sc.nextInt();
				roomService.deleteRoom(delRoom);
				break;
			
			case 4:
				occupantService.viewOccupants();
				break;

			case 5:
				System.out.println("Exiting...");
				sc.close();
				return;

			default:
				System.out.println("Invalid choice. Please try again.");
			}

		}
	}

	private static void userlogin() {
		while (true) {
			System.out.println("\n--- Hostel Booking System Menu ---");
			System.out.println("1. Book Room");
			System.out.println("2. Checkout");
			System.out.println("3. View Available Rooms");
			System.out.println("4. Maintenance Request");
			System.out.println("5. Show all rooms and status");
			System.out.println("6. Maintenance request status update");
			System.out.println("7. Exit");
			System.out.print("Enter your choice: ");
			int choice = sc.nextInt();

			switch (choice) {
			case 1:
				sc.nextLine(); // clear buffer
				System.out.print("Name: ");
				String name = sc.nextLine();
				System.out.print("Contact: ");
				String contact = sc.nextLine();
				System.out.print("Room Number: ");
				int room = sc.nextInt();
				occupantService.bookRoom(new Occupant(name, contact, room, new Date(System.currentTimeMillis())));
				break;

			case 2:
				System.out.print("Occupant ID to checkout: ");
				int id = sc.nextInt();
				occupantService.checkOut(id, new Date(System.currentTimeMillis()));
				break;

			case 3:
				roomService.showAvailableRooms();
				break;

			case 4:
				System.out.print("Room Number: ");
				int r = sc.nextInt();
				sc.nextLine(); // consume newline
				System.out.print("Issue: ");
				String issue = sc.nextLine();
				maintenanceService.submitRequest(new MaintenanceRequest(r, issue, "Pending"));
				break;

			case 5:
				roomService.showAllRooms();
				break;

			case 6:
				System.out.println("Enter request id : ");
				int rid = sc.nextInt();
				System.out.println("Enter the status (Pending or finished): ");
				String status = sc.next();
				maintenanceService.updateRequestStatus(rid, status);
				break;
			case 7:
				System.out.println("Exiting...");
				sc.close();
				return;

			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Hostel Booking System Login Portal");
		System.out.println("1. User Login");
		System.out.println("2. Admin Login");
		System.out.println("Enter your Role (1 or 2)");
		int ch = sc.nextInt();

		switch (ch) {
		case 1:
			userlogin();
			break;
		case 2:
			System.out.print("Enter admin password: ");
			String pass = sc.next();
			if (!pass.equals("admin123")) {
				System.out.println("Incorrect password. Access denied.");
				break;
			}
			adminlogin();
			break;
		default:
			System.out.println("Choose valid option");
		}

	}
}
