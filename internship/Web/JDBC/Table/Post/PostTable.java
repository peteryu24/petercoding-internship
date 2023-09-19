package gmx.upc.post;

import java.util.ArrayList;

import gmx.upc.DBInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostTable {
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
		String postTable = "CREATE TABLE exam.post (" + "post_id SERIAL PRIMARY KEY NOT NULL, " // 게시글 식별
				+ "user_id SERIAL, " // user table의 PK를 FK로 받음
				+ "title VARCHAR(50) NOT NULL, " // 제목
				+ "content TEXT, " // 내용
				+ "view INT DEFAULT 0, " // 조회수
				+ "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " // 게시글 작성 날짜
				+ "FOREIGN KEY (user_id) REFERENCES exam.users(user_id) MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE); " // 외래키
																															// 설정
				+ "COMMENT ON TABLE exam.post IS '게시글 테이블'; " + "COMMENT ON COLUMN exam.post.post_id IS '게시글 식별 id'; "
				+ "COMMENT ON COLUMN exam.post.user_id IS '게시글을 작성한 사용자 식별 id(외래키)'; "
				+ "COMMENT ON COLUMN exam.post.title IS '게시글 제목'; "
				+ "COMMENT ON COLUMN exam.post.content IS '게시글 내용'; " + "COMMENT ON COLUMN exam.post.view IS '조회수'; "
				+ "COMMENT ON COLUMN exam.post.create_time IS '게시글 작성 시간';";

		try {
			connect = DBInfo.getInstance().getConnection();
			if (connect == null) {
				throw new SQLException("DB 연결에 실패하였습니다.");
			}

			preState = connect.prepareStatement(postTable);
			if (preState == null) {
				throw new SQLException("실패하였습니다.");
			}
			preState.executeUpdate(); // create Post Table
			System.out.println("post table 생성완료.");

		} catch (SQLException e) {
			System.out.println("SQLException");
			System.out.println(postTable); // 실패한 SQL 쿼리를 출력
			e.printStackTrace();

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

	public void insertValue() { // null 예외처리 필요
		Connection connect = null;
		PreparedStatement preState = null;
		PostVo pv = new PostVo();

		System.out.println("\n=========================Insert Values=========================\n");

		try {
			connect = DBInfo.getInstance().getConnection();
			if (connect == null) {
				throw new SQLException("DB 연결 실패");
			}

			connect.setAutoCommit(false);
			String insertQuery = "INSERT INTO exam.post (title, content) VALUES (?, ?)";
			preState = connect.prepareStatement(insertQuery);

			// 첫 번째 쿼리
			preState.setString(1, pv.getTitle());
			preState.setString(2, pv.getContent());
			preState.executeUpdate();

			connect.commit();

			System.out.println("데이터가 성공적으로 삽입되었습니다.");

		} catch (SQLException e) {
			try {
				// 롤백
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

	public ArrayList<PostVo> input() {
		Connection connect = null;
		PreparedStatement preState = null;
		ResultSet rs = null;
		ArrayList<PostVo> postList = new ArrayList<>();

		String sql = "SELECT post_id, user_id, title, content, view, create_time FROM exam.post ORDER BY post_id ASC";
		try {
			connect = DBInfo.getInstance().getConnection();
			preState = connect.prepareStatement(sql);
			rs = preState.executeQuery();

			while (rs.next()) {
				PostVo post = new PostVo();
				post.setPost_id(rs.getInt("post_id"));
				post.setUser_id(rs.getInt("user_id"));
				post.setTitle(rs.getString("title"));
				post.setContent(rs.getString("content"));
				post.setView(rs.getInt("view"));
				post.setCreate_time(rs.getString("create_time"));

				postList.add(post);
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
		return postList;
	}

	public void update() {
		System.out.println("\n=========================PreparedState=========================\n");
		Connection connect = null;
		PreparedStatement preState = null;
		String sql = "UPDATE exam.post " + "SET content = ? WHERE post_id = ?";
		try {
			connect = DBInfo.getInstance().getConnection();
			// 객체 생성
			preState = connect.prepareStatement(sql);
			/*
			 * SQL 구문을 사전에 컴파일 데이터 삽입 준비
			 * 
			 * 리턴된 preState은 쿼리에 데이터를 삽입하고, 실행
			 */
			preState.setString(1, "지오멕스소프트 짱"); // 첫 번째 ?의 값
			preState.setInt(2, 3); // 두 번째 ?의 값
			// 쿼리 update
			preState.executeUpdate();
			System.out.println("PreparedState으로 update 완료");
		} catch (SQLException e) {
			System.out.println("SQLException");
			System.out.println(sql);
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

	public void delete() {
		Connection connect = null;
		PreparedStatement preState = null;

		System.out.println("\n=========================Delete=========================\n");

		// 삭제할 계정 id 설정
		int deleteWhat = 2;

		// 삭제 SQL 쿼리
		String sql = "DELETE FROM exam.post WHERE post_id = ?";

		try {
			connect = DBInfo.getInstance().getConnection();
			preState = connect.prepareStatement(sql);
			preState.setInt(1, deleteWhat);

			int affectedRows = preState.executeUpdate();

			if (affectedRows > 0) {
				System.out.println("삭제완료. post_id: " + deleteWhat);
			} else {
				System.out.println("해당 post_id 찾을 수 없음: " + deleteWhat); // 변경된 행의 갯수가 존재하지 않을 때
			}

		} catch (SQLException e) {
			System.out.println("SQLException");
			System.out.println(sql);
			e.printStackTrace();
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

	public void print() {
		System.out.println("\n=========================Print Post=========================\n");
		ArrayList<PostVo> posts = input();

		int postLengthSize = posts.size();
		for (int i = 0; i < postLengthSize; i++) {
			PostVo post = posts.get(i);
			System.out.println("게시물 ID: " + post.getPost_id());
			System.out.println("사용자 ID: " + post.getUser_id());
			System.out.println("제목: " + post.getTitle());
			System.out.println("내용: " + post.getContent());
			System.out.println("조회수: " + post.getView());
			System.out.println("생성 시간: " + post.getCreate_time());
			System.out.println("-------------------------------");
		}
	}

}
