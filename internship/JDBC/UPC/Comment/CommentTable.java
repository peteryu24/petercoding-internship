package gmx.upc.comment;

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentTable {
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
		PreparedStatement preState = null;
		System.out.println("=========================Create Table=========================\n");

		// 쿼리 문자열을 정의
		String createCommentTable = "CREATE TABLE exam.comment ("
    + "comment_id INT PRIMARY KEY NOT NULL, "  // 댓글 식별
    + "user_id INT, "  // user table의 PK를 FK로 받음
    + "post_id INT, "  // post table의 PK를 FK로 받음
    + "comment TEXT NOT NULL, "  // 댓글 내용
    + "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "  // 댓글 작성 날짜
    + "FOREIGN KEY (user_id) REFERENCES exam.users(user_id), "  // 외래키 설정
    + "FOREIGN KEY (post_id) REFERENCES exam.post(post_id)); "  // 외래키 설정
    + "COMMENT ON TABLE exam.comment IS '댓글 테이블'; "
    + "COMMENT ON COLUMN exam.comment.comment_id IS '댓글 식별 id'; "
    + "COMMENT ON COLUMN exam.comment.user_id IS '댓글 작성자 식별 id(외래키)'; "
    + "COMMENT ON COLUMN exam.comment.post_id IS '댓글이 달린 게시글 식별 id(외래키)'; "
    + "COMMENT ON COLUMN exam.comment.comment IS '댓글 내용'; "
    + "COMMENT ON COLUMN exam.comment.create_time IS '댓글 작성 시간';";


		try {
			connect = CommentTable.getConnection();
			if (connect == null) {
				throw new SQLException("DB 연결에 실패하였습니다.");
			}

			preState = connect.prepareStatement(createCommentTable);
			if (preState == null) {
				throw new SQLException("실패하였습니다.");
			}
			preState.executeUpdate();
			System.out.println("comment table 생성완료.");

		} catch (SQLException e) {
			System.out.println("SQLException");
			System.out.println(createCommentTable); // 실패한 SQL 쿼리를 출력

		} finally { // 역순으로 닫아주기
			try {
				if (preState != null)
					preState.close();
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

	public static void insertValue() { // null 예외처리 필요
		Connection connect = null;
		PreparedStatement preState = null;

		System.out.println("\n=========================Insert Values=========================\n");

		try {
			connect = CommentTable.getConnection();
			if (connect == null) {
				throw new SQLException("DB 연결 실패");
			}

			connect.setAutoCommit(false);

			String insertQuery = "INSERT INTO exam.comment (comment_id, user_id, post_id, comment) VALUES (?, ?, ?, ?)";
			preState = connect.prepareStatement(insertQuery);

			// 첫 번째 쿼리
			preState.setInt(1, 3);
			preState.setInt(2, 3);
			preState.setInt(3, 3);
			preState.setString(4, "Samsung 댓글임.");
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

	public static ArrayList<CommentVo> input() {
		Connection connect = null;
		PreparedStatement preState = null;
		ResultSet rs = null;
		ArrayList<CommentVo> commentList = new ArrayList<>();

		String sql = "SELECT comment_id, user_id, post_id, comment, create_time FROM exam.comment ORDER BY comment_id ASC";

		try {
			connect = CommentTable.getConnection();
			preState = connect.prepareStatement(sql);
			rs = preState.executeQuery();

			while (rs.next()) {
				CommentVo comment = new CommentVo();
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
				if (preState != null)
					preState.close();
				if (connect != null)
					connect.close();
			} catch (SQLException e) {
				System.out.println("SQLException: " + e.getMessage());
			}
		}
		return commentList;
	

	}

	public static void update() {
		System.out.println("\n=========================PreparedState=========================\n");
		Connection connect = null;
		PreparedStatement preState = null;
		String sql = "UPDATE exam.comment " + "SET comment = ? WHERE comment_id = ?";
		try {
			connect = CommentTable.getConnection();
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
		PreparedStatement preState = null;

		System.out.println("\n=========================Delete=========================\n");

		// 삭제할 계정 id 설정
		int deleteWhat = 3;

		// 삭제 SQL 쿼리
		String sql = "DELETE FROM exam.comment WHERE comment_id = ?";

		try {
			connect = CommentTable.getConnection();
			preState = connect.prepareStatement(sql);
			preState.setInt(1, deleteWhat);

			int rowsAffected = preState.executeUpdate();
			
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
				if (preState != null)
					preState.close();
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
	public static void print() {
	    System.out.println("\n=========================Print Comment=========================\n");
	    ArrayList<CommentVo> comments = input();  

	    int commentLengthSize = comments.size();
	    for (int i = 0; i < commentLengthSize; i++) {
	        CommentVo comment = comments.get(i);
	        System.out.println("댓글 ID: " + comment.getComment_id());
	        System.out.println("사용자 ID: " + comment.getUser_id());
	        System.out.println("게시물 ID: " + comment.getPost_id());
	        System.out.println("댓글 내용: " + comment.getComment());
	        System.out.println("생성 시간: " + comment.getCreate_time());
	        System.out.println("-------------------------------");
	    }
	}


}
