package gmx.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Create {
	public static void main(String[] args) {
		Connection con = null; // 데이터베이스 연결을 위한 Connection 객체를 null로 초기화합니다.
		Statement stmt = null; // SQL문을 실행하기 위한 Statement 객체를 null로 초기화합니다.

		// 테이블 생성을 위한 SQL 쿼리 문자열을 정의합니다.
		String createTableSQL = "CREATE TABLE exam.team (" + "team_id char(3) NOT NULL, "
				+ "region_name varchar(8) NOT NULL, " + "team_name varchar(40) NOT NULL, " + "e_team_name varchar(50), "
				+ "orig_yyyy char(4), " + "stadium_id char(3) NOT NULL, " + "zip_code1 char(3), "
				+ "zip_code2 char(3), " + "address varchar(80), " + "ddd varchar(3), " + "tel varchar(10), "
				+ "fax varchar(10), " + "homepage varchar(50), " + "owner_name varchar(10), "
				+ "CONSTRAINT team_pk PRIMARY KEY (team_id));";

		try {
			// DBConnect 클래스의 getConnection 메서드를 호출하여 데이터베이스 연결을 설정합니다.
			con = DBConnect.getConnection();
			if (con == null) {
				throw new SQLException("DB 연결에 실패하였습니다.");
			}

			// Statement 객체를 생성합니다.
			stmt = con.createStatement();
			if (stmt == null) {
				throw new SQLException("실패하였습니다.");
			}

			// SQL 쿼리를 실행하여 테이블을 생성합니다.
			stmt.executeUpdate(createTableSQL);

			// 테이블 생성이 성공적으로 완료되면, 콘솔에 메시지를 출력합니다.
			System.out.println("Table 'exam.team' has been created.");

		} catch (SQLException e) { // SQLException 예외가 발생하면 실행되는 블록입니다.
			e.printStackTrace(); // 예외 정보를 출력합니다.
			System.out.println(createTableSQL); // 실패한 SQL 쿼리를 출력합니다.

		} finally { // 예외 발생 여부와 관계없이 실행되는 블록입니다.
			// Statement 객체를 닫습니다.
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Connection 객체를 닫습니다.
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}
