package HBS.HostelBookingSystem.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static String driver = "com.mysql.cj.jdbc.Driver";
	 private static final String URL = "jdbc:mysql://localhost:3306/hostel_db";
	    private static final String USER = "root";
	    private static final String PASSWORD = "Karthikb@11";

	    public static Connection getConnection() throws SQLException  {
	    	try {				
	    		Class.forName(driver);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	return DriverManager.getConnection(URL, USER, PASSWORD);
	    }
}
