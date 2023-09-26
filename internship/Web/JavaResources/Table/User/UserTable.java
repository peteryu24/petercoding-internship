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

	public void createTable() {
		Connection connect = null;
		PreparedStatement preState = null;
		System.out.println("=========================Create Table=========================\n");

		String userTable = "CREATE TABLE exam.users (" + "email VARCHAR(50) PRIMARY KEY NOT NULL UNIQUE, "
				+ "nickname VARCHAR(10) NOT NULL, " + "password VARCHAR(15) NOT NULL, "
				+ "createTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP); " + "COMMENT ON TABLE exam.users IS '유저 정보 테이블'; "
				+ "COMMENT ON COLUMN exam.users.nickname IS '사용자 닉네임'; "
				+ "COMMENT ON COLUMN exam.users.email IS '사용자 이메일'; "
				+ "COMMENT ON COLUMN exam.users.password IS '사용자 비밀번호'; "
				+ "COMMENT ON COLUMN exam.users.createTime IS '사용자 계정 생성 시간';";

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
				System.out.println("SQLException: preState is null");
			}

			try {
				if (connect != null)
					connect.close();
			} catch (SQLException e) {
				System.out.println("SQLException: connect is null");
			}
		}
	}

	public String insertValue(String nickname, String email, String password) {
		String checker = "proceed";
		Connection connect = null;
		PreparedStatement preState = null;
		// UserVo uv = new UserVo();

		System.out.println("\n=========================Insert Values=========================\n");

		try {
			connect = DBInfo.getInstance().getConnection();
			if (connect == null) {
				throw new SQLException("DB 연결 실패");
			}

			connect.setAutoCommit(false);

			String insertQuery = "INSERT INTO exam.users (nickname, email, password) VALUES (?, ?, ?)";
			preState = connect.prepareStatement(insertQuery);

			// 첫 번째 쿼리
			preState.setString(1, nickname);
			preState.setString(2, email);
			preState.setString(3, password);
			preState.executeUpdate();

			connect.commit();
			System.out.println("데이터가 성공적으로 삽입되었습니다.");

		} catch (SQLException e) {
			try {

				if (connect != null) {
					checker = "duplicate";
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
				System.out.println("SQLException: preState is null");
			}

			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException e) {
				System.out.println("SQLException: connect is null");
			}
		}
		return checker;
	}

	public ArrayList<UserVo> input() {
		Connection connect = null;
		PreparedStatement preState = null;
		ResultSet rs = null;
		ArrayList<UserVo> userList = new ArrayList<>();

		String sql = "SELECT  nickname, email, password, createTime FROM exam.users ORDER BY email ASC";

		try {
			connect = DBInfo.getInstance().getConnection();

			preState = connect.prepareStatement(sql);
			rs = preState.executeQuery();

			while (rs.next()) {
				UserVo user = new UserVo();
				user.setNickname(rs.getString("nickname"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				user.setCreateTime(rs.getString("createTime"));

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

	public void update() {
		System.out.println("\n=========================PreparedState Update=========================\n");
		Connection connect = null;
		PreparedStatement preState = null;
		int affectedRows = 0;

		String sql = "UPDATE exam.users SET password = ? WHERE email = ?";

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

	public void delete() {
		System.out.println("\n=========================Delete=========================\n");
		Connection connect = null;
		PreparedStatement preState = null;
		int affectedRows = 0;

		int deleteWhat = 1;

		String sql = "DELETE FROM exam.users WHERE email = ?";

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

	public void print() {
		System.out.println("\n=========================Print User=========================\n");
		ArrayList<UserVo> users = input();

		int userLengthSize = users.size();
		for (int i = 0; i < userLengthSize; i++) {
			UserVo user = users.get(i);
			System.out.println("별명: " + user.getNickname());
			System.out.println("이메일: " + user.getEmail());
			System.out.println("비밀번호: " + user.getPassword());
			System.out.println("생성 시간: " + user.getCreateTime());
			System.out.println("-------------------------------");
		}
	}

	public boolean loginCheck(String email, String password) {
		Connection connect = null;
		PreparedStatement preState = null;
		ResultSet rs = null;
		Boolean isCheck = false;
		try {

			String sql = "SELECT * FROM exam.users WHERE email = ? AND password = ?";
			connect = DBInfo.getInstance().getConnection();
			preState = connect.prepareStatement(sql);
			preState.setString(1, email);
			preState.setString(2, password);
			rs = preState.executeQuery();
			if (rs.next()) {
				isCheck = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {

				}
			}
			if (preState != null) {
				try {
					preState.close();
				} catch (SQLException e) {
				}
			}
			if (connect != null) {
				try {
					connect.close();
				} catch (SQLException e) {
				}
			}
		}
		return isCheck;
	}

}
