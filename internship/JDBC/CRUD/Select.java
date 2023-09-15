package gmx.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Select {

	public static void printAll(Connection connect) {
		Statement state = null;
		ResultSet rs = null;
		
		System.out.println("\n=========================Print All=========================\n");

		String sql = "SELECT team_id, region_name, team_name, e_team_name, orig_yyyy, "
				+ "stadium_id, zip_code1, zip_code2, address, ddd, tel, fax, homepage, owner_name " + "FROM exam.team "
				+ "ORDER BY team_id ASC";

		try {
			state = connect.createStatement();
			rs = state.executeQuery(sql); // 쿼리 실행.

			// ResultSet 에서 데이터 추출.
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

				System.out.println(team_id + "|" + region_name + "|" + team_name + "|" + e_team_name + "|" + orig_yyyy
						+ "|" + stadium_id + "|" + zip_code1 + "|" + zip_code2 + "|" + address + "|" + ddd + "|" + tel
						+ "|" + fax + "|" + homepage + "|" + owner_name);
			}
		} catch (SQLException e) {
			System.out.println("SQLException");
			System.out.println(sql);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				System.out.println("SQLException: rs is null");
			}
			try {
				if (state != null)
					state.close();
			} catch (SQLException e) {
				System.out.println("SQLException: state is null");
			}
			try {
				if (connect != null)
					connect.close();
			} catch (SQLException e) {
				System.out.println("SQLException: connect is null");
			}
		}
	}
}
