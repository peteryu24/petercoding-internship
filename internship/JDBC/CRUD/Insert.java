package gmx.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Insert {
	public static void main(String[] args) {
		Connection con = null; // Connection 객체를 null로 초기화
		Statement stmt = null; // Statement 객체를 null로 초기화

		String insertQuery1 = "INSERT INTO exam.team VALUES ('K05', '전북', '현대모터스', 'CHUNBUK HYUNDAI MOTORS FC', '1995', 'D03', '560', '190', '전북 전주시 덕진구 반월동 763-1 전주월드컵경기장 내', '063', '273-1763', '273-1762', 'http://www.hyundai-motorsfc.com', '');";
		String insertQuery2 = "INSERT INTO exam.team VALUES ('K08', '성남', '일화천마', 'SEONGNAM ILHWA CHUNMA FC', '1988', 'B02', '462', '130', '경기도 성남시 분당구 야탑동 486번지 성남 제2종합운동장 내', '031', '753-3956', '753-4443', 'http://www.esifc.com', '');";

		try {
			// DBConnect 클래스의 getConnection 메서드를 호출하여 Connection 맺기
			con = DBConnect.getConnection();

			// Statement 객체 생성
			stmt = con.createStatement();

			// SQL 쿼리 실행
			stmt.executeUpdate(insertQuery1);
			stmt.executeUpdate(insertQuery2);

			// 데이터 삽입 성공 메시지 출력
			System.out.println("Data has been inserted into table 'exam.team'.");
		} catch (SQLException e) {
			// 예외 발생 시 스택 트레이스 출력
			e.printStackTrace();
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
