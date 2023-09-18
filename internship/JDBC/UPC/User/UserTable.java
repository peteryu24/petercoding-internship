/*
  CREATE TABLE exam.users (
    user_id INT PRIMARY KEY NOT NULL, -- 사용자  ID
    nickname VARCHAR(50) NOT NULL, -- 사용자 닉네임
    email VARCHAR(50) UNIQUE NOT NULL, -- 사용자 메일 주소
    password VARCHAR(256) NOT NULL, -- 사용자 비밀번호
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 계정 생성 날짜
);

CREATE TABLE exam.post (
    post_id INT PRIMARY KEY NOT NULL, -- 게시글 식별
    user_id INT, -- user table의 PK를 FK로 받음
    title VARCHAR(100) NOT NULL, -- 제목
    content TEXT, -- 내용
    view INT DEFAULT 0, -- 조회수
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 게시글 작성 날짜
    FOREIGN KEY (user_id) REFERENCES exam.users(user_id)
);

CREATE TABLE exam.comment (
    comment_id INT PRIMARY KEY NOT NULL, -- 댓글 식별
    user_id INT, -- user table의 PK를 FK로 받음
    post_id INT, -- post table의 PK를 FK로 받음
    comment TEXT NOT NULL, -- 댓글 내용
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 댓글 작성 날짜
    FOREIGN KEY (user_id) REFERENCES exam.users(user_id),
    FOREIGN KEY (post_id) REFERENCES exam.post(post_id)
);
*/

package gmx.upc.user;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserTable {
	// DB 연결 정보
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

	public static void createTable() {
		Connection connect = null;
		PreparedStatement preState = null;
		System.out.println("=========================Create Table=========================\n");

		// 테이블 생성 쿼리를 변경
		String createUserTable = "CREATE TABLE exam.users (" + "user_id INT PRIMARY KEY NOT NULL, "
				+ "nickname VARCHAR(50) NOT NULL, " + "email VARCHAR(50) UNIQUE NOT NULL, "
				+ "password VARCHAR(256) NOT NULL, " + "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

		try {
			connect = UserTable.getConnection();
			if (connect == null) {
				throw new SQLException("DB 연결에 실패하였습니다.");
			}

			preState = connect.prepareStatement(createUserTable);

			if (preState == null) {
				throw new SQLException("실패하였습니다.");
			}

			preState.executeUpdate();
			System.out.println("users table 생성완료.");
		} catch (SQLException e) {
			System.out.println("SQLException");
			System.out.println(createUserTable); // 실패한 SQL 쿼리를 출력
			e.printStackTrace();
		} finally { // 역순으로 닫아주기
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
		PreparedStatement pstmt = null;

		System.out.println("\n=========================Insert Values=========================\n");

		try {
			connect = UserTable.getConnection();
			if (connect == null) {
				throw new SQLException("DB 연결 실패");
			}

			connect.setAutoCommit(false);

			String insertQuery = "INSERT INTO exam.users (user_id, nickname, email, password) VALUES (?, ?, ?, ?)";
			pstmt = connect.prepareStatement(insertQuery);

			// 첫 번째 쿼리
			pstmt.setInt(1, 1);
			pstmt.setString(2, "Dillan");
			pstmt.setString(3, "dillan123@gmail.com");
			pstmt.setString(4, "secret123");
			pstmt.executeUpdate();

			// 두 번째 쿼리
			pstmt.setInt(1, 2);
			pstmt.setString(2, "Matt");
			pstmt.setString(3, "matt456@yahoo.com");
			pstmt.setString(4, "secret456");
			pstmt.executeUpdate();

			// 세 번째 쿼리
			pstmt.setInt(1, 3);
			pstmt.setString(2, "Bob");
			pstmt.setString(3, "bob789@naver.com");
			pstmt.setString(4, "secret789");
			pstmt.executeUpdate();

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
				if (pstmt != null) {
					pstmt.close();
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
			connect = UserTable.getConnection();

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
			connect = UserTable.getConnection();
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
			connect = UserTable.getConnection();
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
