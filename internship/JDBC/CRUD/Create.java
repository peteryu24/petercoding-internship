package gmx.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Create {
	public static void createTable(Connection connect) {
		//Connection connect = null;
		/*
		 * DB에 대한 실제 연결을 나타냄
		 * Connection을 통해 쿼리를 실행하거나 트랜잭션을 관리하거나 DB 메타데이터에 엑세스
		 * 
		 * 메타데이터란 
		 * DB의 구조, 스키마, 테이블, 칼럼등에 대한 정보
		 */
		Statement state = null;
		/*
		 * SQL 쿼리를 DB에 전달하고 실행하는 역할
		 * Connection 객체를 통해 Statement 객체 생성 가능
		 * 쿼리 CRUD에 사용
		 */
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
			//connect = DBConnect.getConnection();
			if (connect == null) {
				throw new SQLException("DB 연결에 실패하였습니다.");
			}

			state = connect.createStatement();
			if (state == null) {
				throw new SQLException("실패하였습니다.");
			}
			// 테이블 생성.
			state.executeUpdate(createTable);
			// 테이블 생성 성공
			System.out.println("Table 'exam.team' 생성완료.");

		} catch (SQLException e) {
			System.out.println("SQLException");
			System.out.println(createTable); // 실패한 SQL 쿼리를 출력

		} finally { // 역순으로 닫아주기
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
