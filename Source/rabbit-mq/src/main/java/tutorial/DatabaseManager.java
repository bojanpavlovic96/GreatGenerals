package tutorial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.dialect.Dialect;

import com.mysql.cj.api.jdbc.Statement;

public class DatabaseManager {
	public static void main(String[] args) {

		try {

			Connection connection = (Connection) DriverManager
					.getConnection("jdbc:mysql://root:root@localhost/test_database");
			if (connection == null) {
				System.out.println("not conencted");
				System.exit(1);
			} else {
				System.out.println("connected");
			}
			
			Statement statement = (Statement) connection.createStatement();

			ResultSet result = statement.executeQuery("select * from test_table");

			while (result.next()) {
				System.out.println(
						"result index: " + result.getInt(1) + " result name: " + result.getString(2));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
