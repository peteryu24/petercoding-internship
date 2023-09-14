package gmx.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;

public class DBConnect {
	// DB 연결 정보
	static final String URL = "jdbc:postgresql://127.0.0.1:5432/Sports";
	static final String USER = "postgres";
	static final String PASS = "0000";

	// 연결 수행
	public static Connection getConnection() {
		Connection connect = null; // Connection null로 초기화
		try {
			Class.forName("org.postgresql.Driver"); // JDBC 드라이버 로딩
			connect = DriverManager.getConnection(URL, USER, PASS); // DB 연결
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException");
		} catch (SQLException e) {
			System.out.println("SQLException");
		}
		return connect;
	}
	public static void main(String[] args) {
		getConnection();
		Create c = new Create();
		Select s = new Select();
		Insert i = new Insert();
		Update u = new Update();
		Delete d = new Delete();
		
		c.createTable();
		i.insertValue();
		s.printAll();
		u.updateValue();
		d.delete();
	}

}
