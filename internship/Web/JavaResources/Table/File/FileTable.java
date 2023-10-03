package gmx.upc.file;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import gmx.upc.DBInfo;

public class FileTable {

	public void createTable() {
		Connection connect = null;
		PreparedStatement preState = null;
		System.out.println("=========================Create Table=========================\n");
		String fileTable = "CREATE TABLE exam.file (" + "fileId SERIAL PRIMARY KEY NOT NULL, " + "postId INT NOT NULL, "
				+ "fileName VARCHAR(255) NOT NULL, " + "filePath TEXT NOT NULL, "
				+ "FOREIGN KEY (postId) REFERENCES exam.post(postId) ON UPDATE CASCADE ON DELETE CASCADE" + "); "
				+ "COMMENT ON TABLE exam.file IS '파일 업로드 테이블'; " + "COMMENT ON COLUMN exam.file.fileId IS '파일 식별 id'; "
				+ "COMMENT ON COLUMN exam.file.postId IS '관련 게시물 id'; "
				+ "COMMENT ON COLUMN exam.file.fileName IS '서버에 저장된 파일 이름'; "
				+ "COMMENT ON COLUMN exam.file.filePath IS '파일 저장 경로'; ";

		try {
			connect = DBInfo.getInstance().getConnection();
			if (connect == null) {
				throw new SQLException("DB 연결에 실패하였습니다.");
			}
			preState = connect.prepareStatement(fileTable);
			if (preState == null) {
				throw new SQLException("실패하였습니다.");
			}
			preState.executeUpdate();
			System.out.println("file table 생성완료.");

		} catch (SQLException e) {
			System.out.println("SQLException");
			System.out.println(fileTable);
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
	public boolean insertFile(int postId, String fileName, String filePath) {
	    Connection connect = null;
	    PreparedStatement preState = null;
	    String insertQuery = "INSERT INTO exam.file(postId, fileName, filePath) VALUES(?, ?, ?)";

	    try {
	        connect = DBInfo.getInstance().getConnection();
	        if (connect == null) {
	            throw new SQLException("DB 연결에 실패하였습니다.");
	        }

	        preState = connect.prepareStatement(insertQuery);
	        if (preState == null) {
	            throw new SQLException("실패하였습니다.");
	        }

	        preState.setInt(1, postId);
	        preState.setString(2, fileName);
	        preState.setString(3, filePath);

	        int rowCount = preState.executeUpdate();
	        if (rowCount > 0) {
	            return true;
	        }
	    } catch (SQLException e) {
	        System.out.println("SQLException");
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
	    return false;
	}
	public boolean hasFileForPost(int postId) {
	    Connection connect = null;
	    PreparedStatement preState = null;
	    ResultSet resultSet = null;
	    String checkQuery = "SELECT COUNT(*) FROM exam.file WHERE postId = ?";

	    try {
	        connect = DBInfo.getInstance().getConnection();
	        if (connect == null) {
	            throw new SQLException("DB 연결에 실패하였습니다.");
	        }

	        preState = connect.prepareStatement(checkQuery);
	        if (preState == null) {
	            throw new SQLException("실패하였습니다.");
	        }

	        preState.setInt(1, postId);

	        resultSet = preState.executeQuery();
	        if (resultSet.next()) {
	            return resultSet.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        System.out.println("SQLException");
	        e.printStackTrace();
	    } finally {
	        try {
	            if (resultSet != null) {
	                resultSet.close();
	            }
	            if (preState != null) {
	                preState.close();
	            }
	            if (connect != null) {
	                connect.close();
	            }
	        } catch (SQLException e) {
	            System.out.println("SQLException during close");
	            e.printStackTrace();
	        }
	    }
	    return false;
	}
	public ArrayList<FileVo> getFilesByPostId(int postId) {
	    Connection connect = null;
	    PreparedStatement preState = null;
	    ResultSet resultSet = null;
	    String selectQuery = "SELECT * FROM exam.file WHERE postId = ?";
	    ArrayList<FileVo> files = new ArrayList<>();

	    try {
	        connect = DBInfo.getInstance().getConnection();
	        if (connect == null) {
	            throw new SQLException("DB 연결에 실패하였습니다.");
	        }

	        preState = connect.prepareStatement(selectQuery);
	        if (preState == null) {
	            throw new SQLException("실패하였습니다.");
	        }

	        preState.setInt(1, postId);

	        resultSet = preState.executeQuery();
	        while (resultSet.next()) {
	            FileVo fileVo = new FileVo();
	            fileVo.setFileId(resultSet.getInt("fileId"));
	            fileVo.setPostId(resultSet.getInt("postId"));
	            fileVo.setFileName(resultSet.getString("fileName"));
	            fileVo.setFilePath(resultSet.getString("filePath"));
	            files.add(fileVo);
	        }
	    } catch (SQLException e) {
	        System.out.println("SQLException");
	        e.printStackTrace();
	    } finally {
	        try {
	            if (resultSet != null) {
	                resultSet.close();
	            }
	            if (preState != null) {
	                preState.close();
	            }
	            if (connect != null) {
	                connect.close();
	            }
	        } catch (SQLException e) {
	            System.out.println("SQLException during close");
	            e.printStackTrace();
	        }
	    }
	    return files; // 해당 게시물의 모든 파일 목록을 반환
	}
	// FileTable.java
	public FileVo getFileById(int fileId) {
	    Connection connect = null;
	    PreparedStatement preState = null;
	    ResultSet resultSet = null;
	    String selectQuery = "SELECT * FROM exam.file WHERE fileId = ?";

	    try {
	        connect = DBInfo.getInstance().getConnection();
	        if (connect == null) {
	            throw new SQLException("DB 연결에 실패하였습니다.");
	        }

	        preState = connect.prepareStatement(selectQuery);
	        if (preState == null) {
	            throw new SQLException("실패하였습니다.");
	        }

	        preState.setInt(1, fileId);

	        resultSet = preState.executeQuery();
	        if (resultSet.next()) {
	            FileVo fileVo = new FileVo();
	            fileVo.setFileId(resultSet.getInt("fileId"));
	            fileVo.setPostId(resultSet.getInt("postId"));
	            fileVo.setFileName(resultSet.getString("fileName"));
	            fileVo.setFilePath(resultSet.getString("filePath"));
	            return fileVo;
	        }
	    } catch (SQLException e) {
	        System.out.println("SQLException");
	        e.printStackTrace();
	    } finally {
	        try {
	            if (resultSet != null) {
	                resultSet.close();
	            }
	            if (preState != null) {
	                preState.close();
	            }
	            if (connect != null) {
	                connect.close();
	            }
	        } catch (SQLException e) {
	            System.out.println("SQLException during close");
	            e.printStackTrace();
	        }
	    }
	    return null; // 파일을 찾지 못한 경우 null 반환
	}
	public boolean updateFile(int fileId, String newFileName, String newFilePath) {
        Connection connect = null;
        PreparedStatement preState = null;
        String updateQuery = "UPDATE exam.file SET fileName = ?, filePath = ? WHERE fileId = ?";

        try {
            connect = DBInfo.getInstance().getConnection();
            if (connect == null) {
                throw new SQLException("DB 연결에 실패하였습니다.");
            }

            preState = connect.prepareStatement(updateQuery);
            if (preState == null) {
                throw new SQLException("실패하였습니다.");
            }

            preState.setString(1, newFileName);
            preState.setString(2, newFilePath);
            preState.setInt(3, fileId);

            int rowCount = preState.executeUpdate();
            if (rowCount > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("SQLException");
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
        return false;
    }

    // 파일 삭제
    public boolean deleteFile(int fileId) {
        Connection connect = null;
        PreparedStatement preState = null;
        String deleteQuery = "DELETE FROM exam.file WHERE fileId = ?";

        try {
            connect = DBInfo.getInstance().getConnection();
            if (connect == null) {
                throw new SQLException("DB 연결에 실패하였습니다.");
            }

            preState = connect.prepareStatement(deleteQuery);
            if (preState == null) {
                throw new SQLException("실패하였습니다.");
            }

            preState.setInt(1, fileId);

            int rowCount = preState.executeUpdate();
            if (rowCount > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("SQLException");
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
        return false;
    }


	



}
