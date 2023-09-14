package gmx.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * SelectTeam 클래스는 exam.team 테이블에서 데이터를 선택(Select)하는 로직을 담당합니다.
 */
public class Select {
	/**
	 * 메인 메소드
	 * 
	 * @param args 커맨드라인 인자 (사용되지 않음)
	 */
	public static void main(String[] args) {
		// 필요한 변수 선언
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		// SQL 쿼리
		String sql = "SELECT team_id, region_name, team_name, e_team_name, orig_yyyy, "
				+ "stadium_id, zip_code1, zip_code2, address, ddd, tel, fax, homepage, owner_name " + "FROM exam.team "
				+ "ORDER BY team_id ASC";

		try {
			// DBConnection 클래스를 이용하여 데이터베이스에 연결
			con = DBConnect.getConnection();
			// SQL을 실행할 객체 생성
			stmt = con.createStatement();
			// SQL 쿼리 실행
			rs = stmt.executeQuery(sql);

			// 결과 가져오기
			while (rs.next()) {
				String team_id = rs.getString("team_id");
				String region_name = rs.getString("region_name");
				String team_name = rs.getString("team_name");
				String e_team_name = rs.getString("e_team_name");
				String orig_yyyy = rs.getString("orig_yyyy");
				String stadium_id = rs.getString("stadium_id");
				String zip_code1 = rs.getString("zip_code1");
				String zip_code2 = rs.getString("zip_code2");
				String address = rs.getString("address");
				String ddd = rs.getString("ddd");
				String tel = rs.getString("tel");
				String fax = rs.getString("fax");
				String homepage = rs.getString("homepage");
				String owner_name = rs.getString("owner_name");

				// 결과 출력
				System.out.println(team_id + "|" + region_name + "|" + team_name + "|" + e_team_name + "|" + orig_yyyy
						+ "|" + stadium_id + "|" + zip_code1 + "|" + zip_code2 + "|" + address + "|" + ddd + "|" + tel
						+ "|" + fax + "|" + homepage + "|" + owner_name);
			}

		} catch (SQLException e) {
			// 에러 처리
			e.printStackTrace();
			System.out.println(sql);
		} finally {
			// 리소스 해제
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
	}
}
