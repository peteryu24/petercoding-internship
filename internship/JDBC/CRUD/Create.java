package gmx.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Create {
	public static void main(String[] args) {
		Connection con = null; // Connection 객체를 null로 초기화
		Statement stmt = null; // Statement 객체를 null로 초기화
		// 테이블 생성 SQL 쿼리 문자열
		String createTableSQL = "CREATE TABLE exam.team (" + "team_id char(3) NOT NULL, "
				+ "region_name varchar(8) NOT NULL, " + "team_name varchar(40) NOT NULL, " + "e_team_name varchar(50), "
				+ "orig_yyyy char(4), " + "stadium_id char(3) NOT NULL, " + "zip_code1 char(3), "
				+ "zip_code2 char(3), " + "address varchar(80), " + "ddd varchar(3), " + "tel varchar(10), "
				+ "fax varchar(10), " + "homepage varchar(50), " + "owner_name varchar(10), "
				+ "CONSTRAINT team_pk PRIMARY KEY (team_id));";

		try {
			// DBConnection 클래스의 getConnection 메서드를 호출하여 Connection 맺기
			con = DBConnect.getConnection();
			// Statement 객체 생성
			stmt = con.createStatement();
			// SQL 쿼리 실행
			stmt.executeUpdate(createTableSQL);
			// 테이블 생성 성공 메시지 출력
			System.out.println("Table 'exam.team' has been created.");
		} catch (SQLException e) {
			// 예외 발생 시 스택 트레이스와 SQL 쿼리 출력
			e.printStackTrace();
			System.out.println(createTableSQL);
		} finally {
			// Statement 객체 자원 해제
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// Connection 객체 자원 해제
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
