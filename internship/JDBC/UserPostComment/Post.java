package gmx.upc;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class PostVO {
	private int post_id; // 게시글 식별
	private int user_id; // user table의 PK를 FK로 받음
	private String title; // 제목
	private String content; // 내용
	private int view; // 조회수
	private String create_time; // 게시글 작성 날짜 (java.sql.Timestamp를 사용할 수도 있습니다.)

	public int getPost_id() {
		return post_id;
	}

	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getView() {
		return view;
	}

	public void setView(int view) {
		this.view = view;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
}

public class Post {
	// DB 연결 정보
	static final String URL = "jdbc:postgresql://127.0.0.1:5432/Sports";
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

	public static void createTable(Connection connect) {
		// Connection connect = null;
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
		String createPostTable = "CREATE TABLE Post (" + "post_id INT PRIMARY KEY AUTO_INCREMENT, " // 게시글 식별
				+ "user_id INT, " // user table의 PK를 FK로 받음
				+ "title VARCHAR(100), " // 제목
				+ "content TEXT, " // 내용
				+ "view INT DEFAULT 0, " // 조회수
				+ "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " // 게시글 작성 날짜
				+ "FOREIGN KEY (user_id) REFERENCES User(user_id));"; // 외래키 설정

		try {
			// connect = DBConnect.getConnection();
			if (connect == null) {
				throw new SQLException("DB 연결에 실패하였습니다.");
			}

			state = connect.createStatement();
			if (state == null) {
				throw new SQLException("실패하였습니다.");
			}
			state.executeUpdate(createPostTable);
			System.out.println("Table Post 생성완료.");

		} catch (SQLException e) {
			System.out.println("SQLException");
			System.out.println(createPostTable); // 실패한 SQL 쿼리를 출력

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

	public static void insertValue(Connection connect) {
		Statement state = null;

		System.out.println("\n=========================Insert Values=========================\n");

		// 데이터 삽입 위한 SQL 쿼리
		String[] insertQuery = {};

		try {
			state = connect.createStatement(); // Statement 객체 생성

			// 데이터 삽입
			for (int i = 0; i < insertQuery.length; i++) {
				String query = insertQuery[i];
				try {
					state.executeUpdate(query); // SQL 쿼리 실행
					System.out.println("쿼리문 성공: " + query);
				} catch (SQLException e) {
					System.out.println("쿼리문 실패: " + query);

				}
			}
			System.out.println("데이터가 성공적으로 삽입되었습니다."); // 삽입 성공
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Statement 닫기
			try {
				if (state != null) {
					state.close();
				}
			} catch (SQLException e) {
				System.out.println("SQLException: state is null");
			}
			// Connection 닫기
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException e) {
				System.out.println("SQLException: connect is null");
			}
		}
	}

	public static ArrayList<PostVO> printAll(Connection connect) {
		Statement state = null;
		ResultSet rs = null;
		ArrayList<PostVO> postList = new ArrayList<>();

		String sql = "SELECT post_id, user_id, title, content, view, create_time FROM Post ORDER BY post_id ASC";

		try {
			state = connect.createStatement();
			rs = state.executeQuery(sql);

			while (rs.next()) {
				PostVO post = new PostVO();
				post.setPost_id(rs.getInt("post_id"));
				post.setUser_id(rs.getInt("user_id"));
				post.setTitle(rs.getString("title"));
				post.setContent(rs.getString("content"));
				post.setView(rs.getInt("view"));
				post.setCreate_time(rs.getString("create_time")); // Or use rs.getTimestamp() based on your needs

				postList.add(post);
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

		return postList;
	}

	public static void update(Connection connect) {
		System.out.println("\n=========================PreparedState=========================\n");
		PreparedStatement preState = null;
		String sql = "UPDATE exam.Post " + "SET content = ? WHERE post_id = ?";
		try {
			// 객체 생성
			preState = connect.prepareStatement(sql);
			/*
			 * SQL 구문을 사전에 컴파일 데이터 삽입 준비
			 * 
			 * 리턴된 preState은 쿼리에 데이터를 삽입하고, 실행
			 */
			preState.setString(1, "지오멕스소프트 짱"); // 첫 번째 ?의 값
			preState.setString(2, ""); // 두 번째 ?의 값
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

	public static void delete(Connection connect) {
		Statement state = null;

		System.out.println("\n=========================Delete=========================\n");

		// 삭제할 계정 id 설정
		String deleteWhat = "";

		// 삭제 SQL 쿼리
		String sql = "DELETE FROM exam.Post WHERE post_id = '" + deleteWhat + "'";

		try {
			state = connect.createStatement();
			int rowsAffected = state.executeUpdate(sql); // 변경된 행의 갯수

			if (rowsAffected > 0) {
				System.out.println("삭제완료. post_id: " + deleteWhat);
			} else {
				System.out.println("해당 post_id 찾을 수 없음: " + deleteWhat); // 변경된 행의 갯수가 존재하지 않을 때
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
		getConnection();
		// userList 출력 필요

	}

}
