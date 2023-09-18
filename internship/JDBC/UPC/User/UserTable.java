
package gmx.upc.user;

import java.util.ArrayList;

import gmx.upc.DBInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserTable {
	// DB 연결 정보
	/*
	static final String URL = "jdbc:postgresql://127.0.0.1:5432/UsersPostsComments";
	static final String USER = "postgres";
	static final String PASS = "0000";

	// 연결 수행
	public static Connection getConnection() {
		Connection connect = null;
		try {
			Class.forName("org.postgresql.Driver");
			connect = DriverManager.getConnection(URL, USER, PASS);
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException");
		} catch (SQLException e) {
			System.out.println("SQLException");
		}
		return connect;
	}
	*/

	public static void createTable() {
		Connection connect = null;
		PreparedStatement preState = null;
		System.out.println("=========================Create Table=========================\n");

		String userTable = "CREATE TABLE exam.users (" 
	            + "user_id INT PRIMARY KEY NOT NULL, "
	            + "nickname VARCHAR(10) NOT NULL, "
	            + "email VARCHAR(50) NOT NULL, "
	            + "password VARCHAR(15) NOT NULL, "
	            + "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP); "
	            + "COMMENT ON TABLE exam.users IS '유저 정보 테이블'; "
	            + "COMMENT ON COLUMN exam.users.user_id IS '사용자 식별 id'; "
	            + "COMMENT ON COLUMN exam.users.nickname IS '사용자 닉네임'; "
	            + "COMMENT ON COLUMN exam.users.email IS '사용자 이메일'; "
	            + "COMMENT ON COLUMN exam.users.password IS '사용자 비밀번호'; "
	            + "COMMENT ON COLUMN exam.users.create_time IS '사용자 계정 생성 시간';";

		try {
			connect = DBInfo.getInstance().getConnection();
			if (connect == null) {
				throw new SQLException("DB 연결에 실패하였습니다.");
			}

			preState = connect.prepareStatement(userTable);
			if (preState == null) {
				throw new SQLException("실패하였습니다.");
			}

			preState.executeUpdate();
			System.out.println("users table 생성완료.");

			

		} catch (SQLException e) {
			System.out.println("SQLException");
			System.out.println(userTable);
			e.printStackTrace();
		} finally {
			try {
				if (preState != null)
					preState.close();
			} catch (SQLException e) {
				System.out.println("SQLException: pstmt is null");
			}

			try {
				if (connect != null)
					connect.close();
			} catch (SQLException e) {
				System.out.println("SQLException: connect is null");
			}
		}
	}

	public static void insertValue() {
		Connection connect = null;
		PreparedStatement preState = null;

		System.out.println("\n=========================Insert Values=========================\n");

		try {
			connect = DBInfo.getInstance().getConnection();
			if (connect == null) {
				throw new SQLException("DB 연결 실패");
			}

			connect.setAutoCommit(false);

			String insertQuery = "INSERT INTO exam.users (user_id, nickname, email, password) VALUES (?, ?, ?, ?)";
			preState = connect.prepareStatement(insertQuery);

			// 첫 번째 쿼리
			preState.setInt(1, 1);
			preState.setString(2, "Dillan");
			preState.setString(3, "dillan123@gmail.com");
			preState.setString(4, "secret123");
			preState.executeUpdate();

			// 두 번째 쿼리
			preState.setInt(1, 2);
			preState.setString(2, "Matt");
			preState.setString(3, "matt456@yahoo.com");
			preState.setString(4, "secret456");
			preState.executeUpdate();

			// 세 번째 쿼리
			preState.setInt(1, 3);
			preState.setString(2, "Bob");
			preState.setString(3, "bob789@naver.com");
			preState.setString(4, "secret789");
			preState.executeUpdate();

			connect.commit();
			System.out.println("데이터가 성공적으로 삽입되었습니다.");

		} catch (SQLException e) {
			try {

				if (connect != null) {
					connect.rollback();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {

				if (connect != null) {
					connect.setAutoCommit(true);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				if (preState != null) {
					preState.close();
				}
			} catch (SQLException e) {
				System.out.println("SQLException: pstmt is null");
			}

			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException e) {
				System.out.println("SQLException: connect is null");
			}
		}
	}

	public static ArrayList<UserVo> input() {
		Connection connect = null;
		PreparedStatement preState = null;
		ResultSet rs = null;
		ArrayList<UserVo> userList = new ArrayList<>();

		String sql = "SELECT user_id, nickname, email, password, create_time FROM exam.users ORDER BY user_id ASC";

		try {
			connect = DBInfo.getInstance().getConnection();

			preState = connect.prepareStatement(sql);
			rs = preState.executeQuery();

			while (rs.next()) {
				UserVo user = new UserVo();
				user.setUser_id(rs.getInt("user_id"));
				user.setNickname(rs.getString("nickname"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				user.setCreate_time(rs.getString("create_time"));

				userList.add(user);
			}
		} catch (SQLException e) {
			System.out.println("SQLException");
			System.out.println(sql);
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (preState != null)
					preState.close();
				if (connect != null)
					connect.close();
			} catch (SQLException e) {
				System.out.println("SQLException: " + e.getMessage());
			}
		}
		return userList;
	}

	public static void update() {
		System.out.println("\n=========================PreparedState Update=========================\n");
		Connection connect = null;
		PreparedStatement preState = null;
		int affectedRows = 0;

		String sql = "UPDATE exam.users SET password = ? WHERE user_id = ?";

		try {
			connect = DBInfo.getInstance().getConnection();
			preState = connect.prepareStatement(sql);
			preState.setString(1, "1q2w3e4r!");
			preState.setInt(2, 1);

			affectedRows = preState.executeUpdate();

			if (affectedRows > 0) {
				System.out.println("비밀번호가 성공적으로 업데이트되었습니다.");
			} else {
				System.out.println("업데이트할 레코드가 없습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (preState != null)
					preState.close();
				if (connect != null)
					connect.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void delete() {
		System.out.println("\n=========================Delete=========================\n");
		Connection connect = null;
		PreparedStatement preState = null;
		int affectedRows = 0;

		int deleteWhat = 1;

		String sql = "DELETE FROM exam.users WHERE user_id = ?";

		try {
			connect = DBInfo.getInstance().getConnection();
			preState = connect.prepareStatement(sql);
			preState.setInt(1, deleteWhat);

			affectedRows = preState.executeUpdate();

			if (affectedRows > 0) {
				System.out.println("성공적으로 사용자를 삭제했습니다. user_id: " + deleteWhat);
			} else {
				System.out.println("삭제할 레코드가 없습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (preState != null)
					preState.close();
				if (connect != null)
					connect.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void print() {
		System.out.println("\n=========================Print User=========================\n");
		ArrayList<UserVo> users = input();

		int userLengthSize = users.size();
		for (int i = 0; i < userLengthSize; i++) {
			UserVo user = users.get(i);
			System.out.println("사용자 ID: " + user.getUser_id());
			System.out.println("별명: " + user.getNickname());
			System.out.println("이메일: " + user.getEmail());
			System.out.println("비밀번호: " + user.getPassword());
			System.out.println("생성 시간: " + user.getCreate_time());
			System.out.println("-------------------------------");
		}
	}

}
