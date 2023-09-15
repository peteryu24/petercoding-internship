package gmx.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Transaction {
	public static void tranSaction(Connection connect) {
		System.out.println("\n=========================Transaction=========================\n");
		PreparedStatement preState = null;
		String sql = "UPDATE exam.team SET team_name = ? WHERE team_id = ?";
		try {
			// 트랜잭션 시작
			/*
			 * JDBC의 자동 커밋 모드 비활성화
			 * 수동으로 커밋을 제어하기 위해
			 * commit()를 명시적으로 호출해야지만 커밋
			 */
			connect.setAutoCommit(false);
			// 1번째 과제
			preState = connect.prepareStatement(sql);
			preState.setString(1, "원주FC");
			preState.setString(2, "K03");
			preState.executeUpdate();
			// 2번째 과제
			preState.setString(1, "홍천FC");
			preState.setString(2, "K04");
			preState.executeUpdate();
			// 3번째 과제
			preState.setInt(1, 12345);
			preState.setString(2, "K05");
			preState.executeUpdate();
			// 세 개의 과제를 모두 성공하면 commit
			connect.commit();
			System.out.println("Transaction으로 update 완료");
		} catch (SQLException e1) {
			e1.printStackTrace();
			try {
				connect.rollback(); // false를 선언한 후 모든 변경사항 취소
				System.out.println("롤백중...");
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
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
