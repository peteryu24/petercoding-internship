package gmx.upc.post;

import java.util.ArrayList;

import gmx.upc.DBInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
				+ "email VARCHAR(50), " // user table의 PK를 FK로 받음
				+ "title VARCHAR(50) NOT NULL, " // 제목
				+ "content TEXT, " // 내용
				+ "view INT DEFAULT 0, " // 조회수
				+ "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " // 게시글 작성 날짜
				+ "FOREIGN KEY (email) REFERENCES exam.users(email) ON UPDATE CASCADE ON DELETE CASCADE" + "); "
				+ "COMMENT ON TABLE exam.post IS '게시글 테이블'; " + "COMMENT ON COLUMN exam.post.post_id IS '게시글 식별 id'; "
				+ "COMMENT ON COLUMN exam.post.email IS '게시글을 작성한 사용자 식별 id(외래키)'; "
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

	public int insertValue(String title, String content, String email) { // postId 반환
	    if ((title == null || title.trim().isEmpty())) {
	        return -1; // 에러를 나타내기 위한 값 (-1)
	    }

	    Connection connect = null;
	    PreparedStatement preState = null;
	    ResultSet generatedKeys = null;
	    int generatedPostId = -1;

	    System.out.println("\n=========================Insert Values=========================\n");

	    try {
	        connect = DBInfo.getInstance().getConnection();
	        if (connect == null) {
	            throw new SQLException("DB 연결 실패");
	        }

	        connect.setAutoCommit(false);
	        String insertQuery = "INSERT INTO exam.post (title, content, email) VALUES (?,?,?)";
	        preState = connect.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

	        preState.setString(1, title);
	        preState.setString(2, content);
	        preState.setString(3, email);
	        preState.executeUpdate();

	        generatedKeys = preState.getGeneratedKeys();
	        if (generatedKeys.next()) {
	            generatedPostId = generatedKeys.getInt(1);
	        }

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
	            if (generatedKeys != null) {
	                generatedKeys.close();
	            }
	            if (preState != null) {
	                preState.close();
	            }
	            if (connect != null) {
	                connect.setAutoCommit(true);
	                connect.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return generatedPostId;
	}


	public ArrayList<PostVo> input() {
		Connection connect = null;
		PreparedStatement preState = null;
		ResultSet rs = null;
		ArrayList<PostVo> postList = new ArrayList<>();

		String sql = "SELECT post_id, email, title, content, view, create_time FROM exam.post ORDER BY post_id ASC";
		try {
			connect = DBInfo.getInstance().getConnection();
			preState = connect.prepareStatement(sql);
			rs = preState.executeQuery();

			while (rs.next()) {
				PostVo post = new PostVo();
				post.setPostId(rs.getInt("post_id"));
				post.setEmail(rs.getString("email"));
				post.setTitle(rs.getString("title"));
				post.setContent(rs.getString("content"));
				post.setView(rs.getInt("view"));
				post.setCreateTime(rs.getString("create_time"));

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

	public String update(String content, int postId) {
		System.out.println("\n=========================PreparedState=========================\n");
		
		String checker = "proceed";
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
			preState.setString(1, content); // 첫 번째 ?의 값
			preState.setInt(2, postId); // 두 번째 ?의 값
			// 쿼리 update
			preState.executeUpdate();
			System.out.println("PreparedState으로 update 완료");
		} catch (SQLException e) {
			System.out.println("SQLException");
			System.out.println(sql);
			checker = "fail";
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
		return checker;
	}

	public PostVo getPostById(int postId) {
		Connection connect = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			// 데이터베이스 연결
			connect = DBInfo.getInstance().getConnection();

			// SQL 쿼리 준비
			String sql = "SELECT * FROM exam.post WHERE post_id = ?";
			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, postId);

			// SQL 쿼리 실행
			rs = pstmt.executeQuery();

			// 결과 가져오기
			if (rs.next()) {
				PostVo post = new PostVo();
				post.setPostId(rs.getInt("post_id"));
				post.setEmail(rs.getString("email"));
				post.setTitle(rs.getString("title"));
				post.setContent(rs.getString("content"));
				post.setView(rs.getInt("view"));
				return post;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 리소스 해제
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (connect != null)
					connect.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null; // 해당 postId에 대한 게시물이 없을 경우 null 반환
	}

	public String delete(int id) {
		String isCheck = "proceed";
		Connection connect = null;
		PreparedStatement preState = null;

		System.out.println("\n=========================Delete=========================\n");

		// 삭제 SQL 쿼리
		String sql = "DELETE FROM exam.post WHERE post_id = ?";

		try {
			connect = DBInfo.getInstance().getConnection();
			preState = connect.prepareStatement(sql);
			preState.setInt(1, id);

			int affectedRows = preState.executeUpdate();

			if (affectedRows > 0) {
				System.out.println("삭제완료. post_id: " + id);
				return isCheck;
			} else {
				System.out.println("해당 post_id 찾을 수 없음: " + id); // 변경된 행의 갯수가 존재하지 않을 때
				isCheck = "fail";
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
		return isCheck;
	}

	public void print() {
		System.out.println("\n=========================Print Post=========================\n");
		ArrayList<PostVo> posts = input();

		int postLengthSize = posts.size();
		for (int i = 0; i < postLengthSize; i++) {
			PostVo post = posts.get(i);
			System.out.println("게시물 ID: " + post.getPostId());
			System.out.println("사용자 ID: " + post.getEmail());
			System.out.println("제목: " + post.getTitle());
			System.out.println("내용: " + post.getContent());
			System.out.println("조회수: " + post.getView());
			System.out.println("생성 시간: " + post.getCreateTime());
			System.out.println("-------------------------------");
		}
	}

}
