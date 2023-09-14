package gmx.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Select {
   
     // @param args 커맨드라인 인자입니다 (사용되지 않음)
     
    public static void main(String[] args) {
        // 데이터베이스 연결과 쿼리 실행에 필요한 변수들을 선언
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        // SQL 쿼리 문자열을 정의
        String sql = "SELECT team_id, region_name, team_name, e_team_name, orig_yyyy, "
                + "stadium_id, zip_code1, zip_code2, address, ddd, tel, fax, homepage, owner_name " 
                + "FROM exam.team "
                + "ORDER BY team_id ASC";

        try {
            // DBConnect 클래스의 메소드를 이용하여 데이터베이스에 연결
            con = DBConnect.getConnection();
            // Statement 객체를 생성
            stmt = con.createStatement();
            // SQL 쿼리를 실행.
            rs = stmt.executeQuery(sql);

            // 결과셋에서 데이터를 추출.
            while (rs.next()) {
                // 각 칼럼의 값을 변수에 저장
                String team_id = rs.getString("team_id");
                String region_name = rs.getString("region_name");
                String team_name = rs.getString("team_name");
                String e_team_name = rs.getString("e_team_name");
                String orig_yyyy = rs.getString("orig_yyyy");
                String stadium_id = rs.getString("stadium_id");
                String zip_code1 = rs.getString("zip_code1");
                String zip_code2 = rs.getString("zip_code2");
                String address = rs.getString("address");
                String ddd = rs.getString("ddd");
                String tel = rs.getString("tel");
                String fax = rs.getString("fax");
                String homepage = rs.getString("homepage");
                String owner_name = rs.getString("owner_name");
                

                // 결과를 출력
                System.out.println(team_id + "|" + region_name + "|" + team_name + "|" + e_team_name + "|" + orig_yyyy
						+ "|" + stadium_id + "|" + zip_code1 + "|" + zip_code2 + "|" + address + "|" + ddd + "|" + tel
						+ "|" + fax + "|" + homepage + "|" + owner_name);
            }
        } catch (SQLException e) {
            // SQL 예외가 발생하면 에러 메시지와 쿼리를 출력
            e.printStackTrace();
            System.out.println(sql);
        } finally {
            // 사용한 자원들을 해제합니다.
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
            }
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
            }
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
            }
        }
    }
}
