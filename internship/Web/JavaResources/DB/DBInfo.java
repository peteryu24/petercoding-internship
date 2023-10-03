/*
  CREATE TABLE exam.users (
    email VARCHAR(50) PRIMARY KEY NOT NULL UNIQUE, -- 사용자 메일 주소
    nickname VARCHAR(10) NOT NULL, -- 사용자 닉네임
    password VARCHAR(15) NOT NULL, -- 사용자 비밀번호
    createTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 계정 생성 날짜
);
COMMENT ON TABLE exam.users IS '유저 정보 테이블';
COMMENT ON COLUMN exam.users.email IS '사용자 이메일';
COMMENT ON COLUMN exam.users.nickname IS '사용자 닉네임';
COMMENT ON COLUMN exam.users.password IS '사용자 비밀번호';
COMMENT ON COLUMN exam.users.createTime IS '사용자 계정 생성 시간';

CREATE TABLE exam.post (
    postId SERIAL PRIMARY KEY NOT NULL, -- 게시글 식별
    email VARCHAR(50), -- user table의 PK를 FK로 받음
    title VARCHAR(50) NOT NULL, -- 제목
    content TEXT, -- 내용
    view INT DEFAULT 0, -- 조회수
    createTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 게시글 작성 날짜
    FOREIGN KEY (email) REFERENCES exam.users(email) MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE
);
COMMENT ON TABLE exam.post IS '게시글 테이블';
COMMENT ON COLUMN exam.post.postId IS '게시글 식별 id';
COMMENT ON COLUMN exam.post.email IS '게시글을 작성한 사용자 식별 id(외래키)';
COMMENT ON COLUMN exam.post.title IS '게시글 제목';
COMMENT ON COLUMN exam.post.content IS '게시글 내용';
COMMENT ON COLUMN exam.post.view IS '조회수';
COMMENT ON COLUMN exam.post.createTime IS '게시글 작성 시간';

CREATE TABLE exam.comment (
    commentId SERIAL PRIMARY KEY NOT NULL, -- 댓글 식별
    email VARCHAR(50), -- user table의 PK를 FK로 받음
    postId INT, -- post table의 PK를 FK로 받음
    comment TEXT NOT NULL, -- 댓글 내용
    createTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 댓글 작성 날짜
    FOREIGN KEY (email) REFERENCES exam.users(email) MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (postId) REFERENCES exam.post(postId)  MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE
);
COMMENT ON TABLE exam.comment IS '댓글 테이블';
COMMENT ON COLUMN exam.comment.comment_id IS '댓글 식별 id';
COMMENT ON COLUMN exam.comment.email IS '댓글 작성자 식별 id(외래키)';
COMMENT ON COLUMN exam.comment.postId IS '댓글이 달린 게시글 식별 id(외래키)';
COMMENT ON COLUMN exam.comment.comment IS '댓글 내용';
COMMENT ON COLUMN exam.comment.createTime IS '댓글 작성 시간';
*/
package gmx.upc;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBInfo {

	/*
	 * DBCP Connection Pool
	 * 
	 * tomcat-juli.jar
	 * Apache Commons DBCP download
	 * commons-pool2
	 * 
	 * 
	 * 
	 * for file upload
	 * 
	 * Apache Commons IO
	 * Apache Commons FileUpload
	 */
    
    private static final String URL = "jdbc:postgresql://127.0.0.1:5432/UsersPostsComments";
    private static final String USER = "postgres";
    private static final String PASS = "0000";

    /*
     * DBCP에서 제공하는 클래스
     * DB 연결 풀을 관리하고 DB 연결을 효율적으로 관리
     */
    private BasicDataSource dataSource = null; 
   
    private static DBInfo dbconnect = null;

  
    private DBInfo() {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(URL);
        dataSource.setUsername(USER);
        dataSource.setPassword(PASS);
      
    }

    
    public static  DBInfo getInstance() {
        if (dbconnect == null) {
            dbconnect = new DBInfo();
        }
        return dbconnect;
    }

    
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    
    public void close() throws SQLException {
        dataSource.close();
    }
}
