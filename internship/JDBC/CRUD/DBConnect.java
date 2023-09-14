package gmx.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
	// 데이터베이스 연결 정보
	static final String URL = "jdbc:postgresql://127.0.0.1:5432/Sports";
	static final String USER = "postgres";
	static final String PASS = "0000";

	// 데이터베이스 연결을 수행하는 메서드
	public static Connection getConnection() {
		Connection con = null; // Connection 객체를 null로 초기화
		try {
			Class.forName("org.postgresql.Driver"); // JDBC 드라이버 로딩
			con = DriverManager.getConnection(URL, USER, PASS); // 데이터베이스 연결
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace(); // 예외 발생 시 스택 트레이스 출력
		}
		return con; // Connection 객체 반환
	}

}
