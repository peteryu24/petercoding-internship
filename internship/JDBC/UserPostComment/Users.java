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

package gmx.upc;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class UserVO {
	private int user_id;
	private String nickname;
	private String email;
	private String password;
	private String create_time; // Depending on your JDBC library, you might want to use java.sql.Timestamp

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
}

public class Users {
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
		Statement state = null;
		System.out.println("=========================Create Table=========================\n");

		// 테이블 생성 쿼리를 변경
		String createTable = "CREATE TABLE exam.users (" + "user_id INT PRIMARY KEY NOT NULL, "
				+ "nickname VARCHAR(50) NOT NULL, " + "email VARCHAR(50) UNIQUE NOT NULL, "
				+ "password VARCHAR(256) NOT NULL, " + "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

		try {
			connect = Users.getConnection();
			if (connect == null) {
				throw new SQLException("DB 연결에 실패하였습니다.");
			}

			state = connect.createStatement();
			if (state == null) {
				throw new SQLException("실패하였습니다.");
			}
			state.executeUpdate(createTable);
			System.out.println("Table 'exam.users' 생성완료.");
		} catch (SQLException e) {
			System.out.println("SQLException");
			System.out.println(createTable); // 실패한 SQL 쿼리를 출력
			e.printStackTrace();
		} finally { // 역순으로 닫아주기
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

	public static void insertValue() {
		Connection connect = null;
		PreparedStatement pstmt = null;

		System.out.println("\n=========================Insert Values=========================\n");

		try {
			connect = Users.getConnection();
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

	public static void printAll() {
		Connection connect = null;
		Statement state = null;
		ResultSet rs = null;
		ArrayList<UserVO> userList = new ArrayList<>();

		String sql = "SELECT user_id, nickname, email, password, create_time FROM exam.users ORDER BY user_id ASC";

		try {
			connect = Users.getConnection();

			state = connect.createStatement();
			rs = state.executeQuery(sql);

			while (rs.next()) {
				UserVO user = new UserVO();
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
				if (state != null)
					state.close();
				if (connect != null)
					connect.close();
			} catch (SQLException e) {
				System.out.println("SQLException: " + e.getMessage());
			}
		}
		int userListLength = userList.size();
		for (int i = 0; i < userListLength; i++) {
			UserVO user = userList.get(i);
			System.out.println("User ID: " + user.getUser_id());
			System.out.println("Nickname: " + user.getNickname());
			System.out.println("Email: " + user.getEmail());
			System.out.println("Password: " + user.getPassword());
			System.out.println("Create Time: " + user.getCreate_time());
			System.out.println("=================================");
		}
	}

	public static void update() {
		Connection connect = null;
		System.out.println("\n=========================PreparedState=========================\n");
		PreparedStatement preState = null;
		String sql = "UPDATE exam.users " + "SET password = ? WHERE user_id = ?";
		try {
			connect = Users.getConnection(); // 직접 Connection 객체 생성

			// 객체 생성
			preState = connect.prepareStatement(sql);
			/*
			 * SQL 구문을 사전에 컴파일 데이터 삽입 준비
			 * 
			 * 리턴된 preState은 쿼리에 데이터를 삽입하고, 실행
			 */
			preState.setString(1, "1q2w3e4r!"); // 첫 번째 ?의 값
			preState.setInt(2, 1); // 두 번째 ?의 값
			// 쿼리 update
			preState.executeUpdate();
			System.out.println("PreparedState으로 update 완료");
		} catch (SQLException e) {
			System.out.println("SQLException");
			System.out.println(sql);
		} finally {
			try {
				if (preState != null)
					preState.close();
			} catch (SQLException e) {
				System.out.println("SQLException: preState.close() 불가");
			}
			try {
				if (connect != null)
					connect.close();
			} catch (SQLException e) {
				System.out.println("SQLException: connect.close() 불가");
			}
		}
	}

	public static void delete() {
		Connection connect = null;
		Statement state = null;

		System.out.println("\n=========================Delete=========================\n");

		// 삭제할 계정 id 설정
		String deleteWhat = " 1 ";

		// 삭제 SQL 쿼리
		String sql = "DELETE FROM exam.users WHERE user_id = '" + deleteWhat + "'";

		try {
			connect = Users.getConnection(); // 직접 Connection 객체 생성
			state = connect.createStatement();
			int rowsAffected = state.executeUpdate(sql); // 변경된 행의 갯수

			if (rowsAffected > 0) {
				System.out.println("삭제완료. user_id: " + deleteWhat);
			} else {
				System.out.println("해당 user_id 찾을 수 없음: " + deleteWhat); // 변경된 행의 갯수가 존재하지 않을 때
			}

		} catch (SQLException e) {
			System.out.println("SQLException");
			System.out.println(sql);
		} finally {
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

	public static void main(String[] args) {
		createTable();
		insertValue();
		printAll();
		update();
		printAll();
		delete();
		printAll();

	}

}
