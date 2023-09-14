package gmx.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Delete {
	public static void delete() {
		Connection connect = null;
		Statement state = null;
		
		System.out.println("\n=========================Delete=========================\n");

		// 삭제할 team_id 설정
		String deleteWhat = "K01";

		// 삭제 SQL 쿼리
		String sql = "DELETE FROM exam.team WHERE team_id = '" + deleteWhat + "'";

		try {

			connect = DBConnect.getConnection();
			state = connect.createStatement();
			int rowsAffected = state.executeUpdate(sql); // 변경된 행의 갯수

			if (rowsAffected > 0) {
				System.out.println("삭제완료. team_id: " + deleteWhat);
			} else {
				System.out.println("해당 team_id 찾을 수 없음: " + deleteWhat); // 변경된 행의 갯수가 존재하지 않을 때
			}

		} catch (SQLException e) {
			System.out.println("SQLException");
			System.out.println(sql);
		} finally {
			Select s = new Select();
			s.printAll();
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
