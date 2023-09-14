package gmx.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteRecord {
    public static void main(String[] args) {
        // Connection과 Statement 객체를 null로 초기화
        Connection con = null;
        Statement stmt = null;

        // 삭제할 team_id 설정 (예: "001")
        String targetTeamId = "001";

        // 삭제 SQL 쿼리
        String sql = "DELETE FROM exam.team WHERE team_id = '" + targetTeamId + "'";

        try {
            // DBConnect 클래스를 이용하여 데이터베이스에 연결
            con = DBConnect.getConnection();
            // SQL을 실행할 객체 생성
            stmt = con.createStatement();
            // SQL 쿼리 실행
            int rowsAffected = stmt.executeUpdate(sql);

            if (rowsAffected > 0) {
                System.out.println("Successfully deleted the record with team_id: " + targetTeamId);
            } else {
                System.out.println("No record found with team_id: " + targetTeamId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(sql);
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
