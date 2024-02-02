package gmx.upc;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class CommentVO {
	private int comment_id; // 댓글 식별
	private int user_id; // user table의 PK를 FK로 받음
	private int post_id; // post table의 PK를 FK로 받음
	private String comment; // 댓글 내용
	private String create_time; // 댓글 작성 날짜 (java.sql.Timestamp를 사용할 수도 있습니다.)

	public int getComment_id() {
		return comment_id;
	}

	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getPost_id() {
		return post_id;
	}

	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
}

public class Comment {
	// DB 연결 정보
	static final String URL = "jdbc:yourDBMS://yourURL&PORT/SchemaName";
	static final String USER = "xxxxxxx";
	static final String PASS = "XXXXXXXX";

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
			e.printStackTrace();
		}
		return connect;
	}

	public static void createTable() {
		Connection connect = null;
		/*
		 * DB에 대한 실제 연결을 나타냄
		 * Connection을 통해 쿼리를 실행하거나 트랜잭션을 관리하거나 DB 메타데이터에 엑세스
		 * 
		 * 메타데이터란 
		 * DB의 구조, 스키마, 테이블, 칼럼등에 대한 정보
		 */
		Statement state = null;
		System.out.println("=========================Create Table=========================\n");

		// 쿼리 문자열을 정의
		String createCommentTable = "CREATE TABLE exam.comment (" + "comment_id SERIAL PRIMARY KEY, " + "user_id INT, "
				+ "post_id INT, " + "comment TEXT NOT NULL, " + "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
				+ "FOREIGN KEY (user_id) REFERENCES exam.users(user_id), "
				+ "FOREIGN KEY (post_id) REFERENCES exam.post(post_id));";

		try {
			connect = Comment.getConnection();
			if (connect == null) {
				throw new SQLException("DB 연결에 실패하였습니다.");
			}

			state = connect.createStatement();
			if (state == null) {
				throw new SQLException("실패하였습니다.");
			}
			state.executeUpdate(createCommentTable);
			System.out.println("Table Comment 생성완료.");

		} catch (SQLException e) {
			System.out.println("SQLException");
			System.out.println(createCommentTable); // 실패한 SQL 쿼리를 출력

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
			connect = Comment.getConnection();
			if (connect == null) {
				throw new SQLException("DB 연결 실패");
			}

			connect.setAutoCommit(false);

			String insertQuery = "INSERT INTO exam.comment (comment_id, user_id, post_id, comment) VALUES (?, ?, ?, ?)";
			pstmt = connect.prepareStatement(insertQuery);

			// 첫 번째 쿼리
			pstmt.setInt(1, 3);
			pstmt.setInt(2, 3);
			pstmt.setInt(3, 3);
			pstmt.setString(4, "Samsung 댓글임.");
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
		ArrayList<CommentVO> commentList = new ArrayList<>();

		String sql = "SELECT comment_id, user_id, post_id, comment, create_time FROM exam.comment ORDER BY comment_id ASC";

		try {
			connect = Comment.getConnection();
			state = connect.createStatement();
			rs = state.executeQuery(sql);

			while (rs.next()) {
				CommentVO comment = new CommentVO();
				comment.setComment_id(rs.getInt("comment_id"));
				comment.setUser_id(rs.getInt("user_id"));
				comment.setPost_id(rs.getInt("post_id"));
				comment.setComment(rs.getString("comment"));
				comment.setCreate_time(rs.getString("create_time"));

				commentList.add(comment);
			}
		} catch (SQLException e) {
			System.out.println("SQLException");
			System.out.println(sql);
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
		int commentListLength = commentList.size();
		for (int i = 0; i < commentListLength; i++) {
			CommentVO comment = commentList.get(i);
			System.out.println("Comment ID: " + comment.getComment_id());
			System.out.println("User ID: " + comment.getUser_id());
			System.out.println("Post ID: " + comment.getPost_id());
			System.out.println("Comment: " + comment.getComment());
			System.out.println("Create Time: " + comment.getCreate_time());
			System.out.println();
		}

	}

	public static void update() {
		System.out.println("\n=========================PreparedState=========================\n");
		Connection connect = null;
		PreparedStatement preState = null;
		String sql = "UPDATE exam.comment " + "SET comment = ? WHERE comment_id = ?";
		try {
			connect = Comment.getConnection();
			// 객체 생성
			preState = connect.prepareStatement(sql);
			/*
			 * SQL 구문을 사전에 컴파일 데이터 삽입 준비
			 * 
			 * 리턴된 preState은 쿼리에 데이터를 삽입하고, 실행
			 */
			preState.setString(1, "인정합니다"); // 첫 번째 ?의 값
			preState.setInt(2, 3); // 두 번째 ?의 값
			// 쿼리 update
			preState.executeUpdate();
			System.out.println("PreparedState으로 update 완료");
		} catch (SQLException e) {
			System.out.println("SQLException");
			System.out.println(sql);
			e.printStackTrace();
		} finally {
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

	public static void delete() {
		Connection connect = null;
		Statement state = null;

		System.out.println("\n=========================Delete=========================\n");

		// 삭제할 계정 id 설정
		String deleteWhat = "3";

		// 삭제 SQL 쿼리
		String sql = "DELETE FROM exam.comment WHERE comment_id = '" + deleteWhat + "'";

		try {
			connect = Comment.getConnection();
			state = connect.createStatement();
			int rowsAffected = state.executeUpdate(sql); // 변경된 행의 갯수

			if (rowsAffected > 0) {
				System.out.println("삭제완료. comment_id: " + deleteWhat);
			} else {
				System.out.println("해당 comment_id 찾을 수 없음: " + deleteWhat); // 변경된 행의 갯수가 존재하지 않을 때
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
