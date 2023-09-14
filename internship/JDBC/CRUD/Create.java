package gmx.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Create {
	public static void createTable() {
		Connection connect = null; // Connection null로 초기화
		Statement state = null; // SQL문을 실행하기 위한
		System.out.println("=========================Create Table=========================\n");

		// 쿼리 문자열을 정의
		String createTable = "CREATE TABLE exam.team (" + "team_id char(3) NOT NULL, "
				+ "region_name varchar(8) NOT NULL, " + "team_name varchar(40) NOT NULL, " + "e_team_name varchar(50), "
				+ "orig_yyyy char(4), " + "stadium_id char(3) NOT NULL, " + "zip_code1 char(3), "
				+ "zip_code2 char(3), " + "address varchar(80), " + "ddd varchar(3), " + "tel varchar(10), "
				+ "fax varchar(10), " + "homepage varchar(50), " + "owner_name varchar(10), "
				+ "CONSTRAINT team_pk PRIMARY KEY (team_id));";

		try {
			// DB연결 설정
			connect = DBConnect.getConnection();
			if (connect == null) {
				throw new SQLException("DB 연결에 실패하였습니다.");
			}

			state = connect.createStatement();
			if (state == null) {
				throw new SQLException("실패하였습니다.");
			}

			// 테이블을 생성합니다.
			state.executeUpdate(createTable);

			// 테이블 생성이 성공
			System.out.println("Table 'exam.team' 생성완료.");

		} catch (SQLException e) {
			System.out.println("SQLException");
			System.out.println(createTable); // 실패한 SQL 쿼리를 출력

		} finally {
			// Statement 닫기
			try {
				if (state != null)
					state.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Connection 닫기
			try {
				if (connect != null)
					connect.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
