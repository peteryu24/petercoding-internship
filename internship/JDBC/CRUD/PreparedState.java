package gmx.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/*
 * 쿼리를 사전 컴파일하여 런타임에 데이터를 삽입할 수 있는 플레이스 홀더('?')를 사용
 * SQL 쿼리에 동적으로 값을 삽입(값 입력 후처리)
 * 쿼리를 캐시에 저장하여 성능 향상
 * 
 * SQL Injection 공격을 방어하기 위해
 *  -타입을 미리 지정할 수 있음
 */

public class PreparedState {
	public static void pState(Connection connect) {
		System.out.println("\n=========================PreparedState=========================\n");
		PreparedStatement preState = null;
		String sql = "UPDATE exam.team SET team_name=? WHERE team_id=?"; // 동적으로 채워지는 '?'
		try {
			// 객체 생성
			preState = connect.prepareStatement(sql);
			/*
			 * SQL 구문을 사전에 컴파일 데이터 삽입 준비
			 * 
			 * 리턴된 preState은 쿼리에 데이터를 삽입하고, 실행
			 */
			preState.setString(1, "춘천FC"); // 첫 번째 ?의 값
			preState.setString(2, "K02"); // 두 번째 ?의 값
			// 쿼리 실행
			preState.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException");
			System.out.println(sql);
		} finally {
			Select.printAll(connect);
			try {
				preState.close();
			} catch (SQLException e) {
				System.out.println("SQLException: preState.close() 불가");
			}
			try {
				connect.close();
			} catch (SQLException e) {
				System.out.println("SQLException: connect.close() 불가");
			}
		}
	}
}
