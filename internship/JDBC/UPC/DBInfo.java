/*
  CREATE TABLE exam.users (
    user_id SERIAL PRIMARY KEY NOT NULL, -- 사용자  ID
    nickname VARCHAR(10) NOT NULL, -- 사용자 닉네임
    email VARCHAR(50) NOT NULL, -- 사용자 메일 주소
    password VARCHAR(15) NOT NULL, -- 사용자 비밀번호
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 계정 생성 날짜
);
COMMENT ON TABLE exam.users IS '유저 정보 테이블';
COMMENT ON COLUMN exam.users.user_id IS '사용자 식별 id';
COMMENT ON COLUMN exam.users.nickname IS '사용자 닉네임';
COMMENT ON COLUMN exam.users.email IS '사용자 이메일';
COMMENT ON COLUMN exam.users.password IS '사용자 비밀번호';
COMMENT ON COLUMN exam.users.create_time IS '사용자 계정 생성 시간';

CREATE TABLE exam.post (
    post_id SERIAL PRIMARY KEY NOT NULL, -- 게시글 식별
    user_id INT, -- user table의 PK를 FK로 받음
    title VARCHAR(50) NOT NULL, -- 제목
    content TEXT, -- 내용
    view INT DEFAULT 0, -- 조회수
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 게시글 작성 날짜
    FOREIGN KEY (user_id) REFERENCES exam.users(user_id) MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE
);
COMMENT ON TABLE exam.post IS '게시글 테이블';
COMMENT ON COLUMN exam.post.post_id IS '게시글 식별 id';
COMMENT ON COLUMN exam.post.user_id IS '게시글을 작성한 사용자 식별 id(외래키)';
COMMENT ON COLUMN exam.post.title IS '게시글 제목';
COMMENT ON COLUMN exam.post.content IS '게시글 내용';
COMMENT ON COLUMN exam.post.view IS '조회수';
COMMENT ON COLUMN exam.post.create_time IS '게시글 작성 시간';

CREATE TABLE exam.comment (
    comment_id SERIAL PRIMARY KEY NOT NULL, -- 댓글 식별
    user_id INT, -- user table의 PK를 FK로 받음
    post_id INT, -- post table의 PK를 FK로 받음
    comment TEXT NOT NULL, -- 댓글 내용
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 댓글 작성 날짜
    FOREIGN KEY (user_id) REFERENCES exam.users(user_id) MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (post_id) REFERENCES exam.post(post_id)  MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE
);
COMMENT ON TABLE exam.comment IS '댓글 테이블';
COMMENT ON COLUMN exam.comment.comment_id IS '댓글 식별 id';
COMMENT ON COLUMN exam.comment.user_id IS '댓글 작성자 식별 id(외래키)';
COMMENT ON COLUMN exam.comment.post_id IS '댓글이 달린 게시글 식별 id(외래키)';
COMMENT ON COLUMN exam.comment.comment IS '댓글 내용';
COMMENT ON COLUMN exam.comment.create_time IS '댓글 작성 시간';
*/
package gmx.upc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBInfo {

	private final String URL = "jdbc:yourDBMS://yourURL&PORT/SchemaName";
	private final String USER = "postgres";
	private final String PASS = "0000";

	private Connection connect = null;
	private static DBInfo dbconnect = null;

	private DBInfo() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException");
		}
	}

	public static DBInfo getInstance() {
		if (dbconnect == null) {
			dbconnect = new DBInfo();
		}
		return dbconnect;
	}

	public Connection getConnection() {
		try {
			connect = DriverManager.getConnection(URL, USER, PASS);
		} catch (SQLException e) {
			System.out.println("SQLException");
			e.printStackTrace();
		}

		return connect;
	}

}
