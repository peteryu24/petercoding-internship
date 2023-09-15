package gmx.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
	// DB 연결 정보
	static final String URL = "jdbc:postgresql://127.0.0.1:5432/Sports"; // jdbc를 통해 pgSQL을 사용하여 URL 포트 DB명
	static final String USER = "postgres";
	static final String PASS = "0000";

	// 연결 수행
	public static Connection getConnection() {
		Connection connect = null; // Connection null로 초기화
		try {
			Class.forName("org.postgresql.Driver"); // JDBC 드라이버 로딩
			/*
			 * 컴파일 타임에 직접적인 참조 없이 런타임에 동적으로 클래스를 로드하기 위해
			 * jar파일의 경로 jvm이 찾을 수 있도록
			 */
			connect = DriverManager.getConnection(URL, USER, PASS); // DB 연결
			/*
			 * JDBC API의 일부
			 * DriverManager는 여러 JDBC 드라이버를 관리하고 DB에 연결을 생성하는 역할
			 */
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException");
		} catch (SQLException e) {
			System.out.println("SQLException");
		}
		return connect;
	}

	public static void main(String[] args) {
		getConnection();

		Create.createTable(getConnection()); // 매번 Connection을 하는건 비효율적
		Insert.insertValue(getConnection());
		Select.printAll(getConnection());
		Update.updateValue(getConnection());
		Delete.delete(getConnection());
	}

}
